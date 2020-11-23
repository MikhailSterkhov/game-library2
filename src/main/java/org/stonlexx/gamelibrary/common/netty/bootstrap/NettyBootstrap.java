package org.stonlexx.gamelibrary.common.netty.bootstrap;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
import lombok.Getter;
import lombok.NonNull;
import org.stonlexx.gamelibrary.common.netty.bootstrap.impl.NettyClient;
import org.stonlexx.gamelibrary.common.netty.bootstrap.impl.NettyServer;
import org.stonlexx.gamelibrary.common.netty.handler.client.active.AbstractNettyClientActive;
import org.stonlexx.gamelibrary.common.netty.handler.client.inactive.AbstractNettyClientInactive;
import org.stonlexx.gamelibrary.common.netty.handler.client.inactive.NettyClientInactiveHandler;
import org.stonlexx.gamelibrary.common.netty.handler.server.active.AbstractNettyServerActive;
import org.stonlexx.gamelibrary.common.netty.handler.server.active.NettyServerActiveHandler;
import org.stonlexx.gamelibrary.common.netty.handler.server.reconnect.AbstractNettyReconnect;
import org.stonlexx.gamelibrary.common.netty.handler.server.reconnect.EnumReconnectReason;
import org.stonlexx.gamelibrary.common.netty.handler.server.reconnect.NettyServerReconnectHandler;
import org.stonlexx.gamelibrary.common.netty.packet.codec.NettyPacketDecoder;
import org.stonlexx.gamelibrary.common.netty.packet.codec.NettyPacketEncoder;
import org.stonlexx.gamelibrary.common.netty.packet.codec.frame.Varint21FrameDecoder;
import org.stonlexx.gamelibrary.common.netty.packet.codec.frame.Varint21LengthFieldEncoder;
import org.stonlexx.gamelibrary.common.netty.packet.handler.NettyPacketHandler;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.Collection;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public final class NettyBootstrap {

    private final NioEventLoopGroup bossEventLoop = new NioEventLoopGroup(1);
    private final NioEventLoopGroup workerEventLoop = new NioEventLoopGroup(2);

    @Getter
    private NettyServer savedNettyServer;

    @Getter
    private NettyClient savedNettyClient;


    /**
     * Создать клиентский bootstrap для подключения
     * к уже заранее забиндованому серверному bootstrap
     *
     * @param socketAddress      - адрес серверного bootstrap
     * @param futureListener     - ответ от подключения
     * @param channelInitializer - инициализация канала подключения
     */
    public Bootstrap createClientBootstrap(@NonNull SocketAddress socketAddress,

                                           ChannelFutureListener futureListener,
                                           ChannelInitializer<NioSocketChannel> channelInitializer) {

        Bootstrap bootstrap = new Bootstrap()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)

                .remoteAddress(socketAddress)

                .channel(NioSocketChannel.class)
                .group(bossEventLoop);

        // add channel initializer
        if (channelInitializer != null) {
            bootstrap.handler(channelInitializer);
        }

        // bind bootstrap, add listener & sync
        try {
            ChannelFuture channelFuture = bootstrap.connect();

            if (futureListener != null) {
                channelFuture.addListener(futureListener);
            }

            channelFuture.channel().closeFuture();
            return bootstrap;
        }

        // if you`re даун then returns null
        catch (Exception exception) {
            exception.printStackTrace();
        }

        return null;
    }

    /**
     * Создать серверный bootstrap для создания
     * канала передачи байтов между клиентами
     *
     * @param socketAddress      - адрес серверного bootstrap
     * @param futureListener     - ответ от подключения
     * @param channelInitializer - инициализация канала подключения
     */
    public ServerBootstrap createServerBootstrap(@NonNull SocketAddress socketAddress,

                                                 ChannelFutureListener futureListener,
                                                 ChannelInitializer<NioSocketChannel> channelInitializer) {

        ServerBootstrap serverBootstrap = new ServerBootstrap()

                .option(ChannelOption.SO_REUSEADDR, true)
                .option(ChannelOption.SO_BACKLOG, 120)
                .childOption(ChannelOption.SO_KEEPALIVE, true)

                .localAddress(socketAddress)

                .channel(NioServerSocketChannel.class)
                .group(bossEventLoop, workerEventLoop);

        // add channel initializer
        if (channelInitializer != null) {
            serverBootstrap.childHandler(channelInitializer);
        }

        // bind bootstrap, add listener & sync
        ChannelFuture channelFuture = serverBootstrap.bind();

        if (futureListener != null) {
            channelFuture.addListener(futureListener);
        }

        channelFuture.channel().closeFuture();
        return serverBootstrap;
    }

    /**
     * Создать листенер для получения результатов
     * после подключения к bootstrap
     *
     * @param futureConsumer - ответы от бутстрапа
     */
    public ChannelFutureListener createFutureListener(@NonNull BiConsumer<ChannelFuture, Boolean> futureConsumer) {
        return future -> {

            NettyServerReconnectHandler serverReconnectHandler
                    = (NettyServerReconnectHandler) future.channel().pipeline().get("netty-reconnect-handler");

            if (serverReconnectHandler != null && !future.isSuccess()) {
                serverReconnectHandler.tryReconnect(future.channel(), EnumReconnectReason.CHANNEL_INACTIVE, null);

                return;
            }

            futureConsumer.accept(future, future.isSuccess());
        };
    }

    /**
     * Создать и инициализировать сокетный адрес
     * для подключения или создания подключения к каналу
     *
     * @param addressHost - хост адреса
     * @param addressPort - порт адреса
     */
    public InetSocketAddress createSocketAddress(String addressHost, int addressPort) {
        return new InetSocketAddress(addressHost, addressPort);
    }

    /**
     * Создать инициализатор серверного канала для подключения
     *
     * @param channelConsumer               - ответ, который возвращает канал
     * @param nettyClientActiveCollection   - список обработчиков подключения клиентов к серверу
     * @param nettyClientInactiveCollection - список обработчиков отключения клиентов от сервера
     * @param hasStandardCodec              - разрешение на установку стандартных кодеков
     */
    public ChannelInitializer<NioSocketChannel> createServerChannelInitializer(Consumer<NioSocketChannel> channelConsumer,
                                                                               Collection<AbstractNettyClientActive> nettyClientActiveCollection,
                                                                               Collection<AbstractNettyClientInactive> nettyClientInactiveCollection,

                                                                               boolean hasStandardCodec) {
        return createChannelInitializer(channelConsumer, socketChannel -> {

            // bootstrap`s active
            if (nettyClientActiveCollection != null) {
                for (AbstractNettyClientActive nettyClientActive : nettyClientActiveCollection) {
                    nettyClientActive.onClientActive(socketChannel);
                }
            }

            // bootstrap`s inactive
            if (nettyClientInactiveCollection != null && !nettyClientInactiveCollection.isEmpty()) {
                socketChannel.pipeline().addLast("client-inactive-handler", new NettyClientInactiveHandler(nettyClientInactiveCollection));
            }

        }, hasStandardCodec);
    }

    /**
     * Создать инициализатор клиентского канала для подключения
     *
     * @param channelConsumer             - ответ, который возвращает канал
     * @param nettyReconnect              - обработчик переподключения клиента к серверу при его падении
     * @param nettyServerActiveCollection - список обработчиков подключения сервера к клиенту
     * @param hasStandardCodec            - разрешение на установку стандартных кодеков
     */
    public ChannelInitializer<NioSocketChannel> createClientChannelInitializer(Consumer<NioSocketChannel> channelConsumer,
                                                                               AbstractNettyReconnect nettyReconnect,
                                                                               Collection<AbstractNettyServerActive> nettyServerActiveCollection,

                                                                               boolean hasStandardCodec) {
        return createChannelInitializer(channelConsumer, socketChannel -> {

            // bootstrap`s active
            if (nettyServerActiveCollection != null && !nettyServerActiveCollection.isEmpty()) {
                socketChannel.pipeline().addLast("netty-server-active-handler", new NettyServerActiveHandler(nettyServerActiveCollection));
            }

            // server reconnect
            if (nettyReconnect != null) {
                socketChannel.attr(AttributeKey.valueOf("reconnect-handler")).set(nettyReconnect);
                socketChannel.pipeline().addLast("netty-reconnect-handler", new NettyServerReconnectHandler(nettyReconnect));
            }

        }, hasStandardCodec);
    }

    /**
     * Создать инициализатор общего канала для подключения
     *
     * @param channelConsumer  - ответ, который возвращает канал
     * @param systemConsumer   - общий консумер, который обрабатывает данные, зависимые от типа бутстрапа
     * @param hasStandardCodec - разрешение на установку стандартных кодеков
     */
    private ChannelInitializer<NioSocketChannel> createChannelInitializer(Consumer<NioSocketChannel> channelConsumer,
                                                                         Consumer<NioSocketChannel> systemConsumer,

                                                                         boolean hasStandardCodec) {
        return new ChannelInitializer<NioSocketChannel>() {

            @Override
            protected void initChannel(NioSocketChannel socketChannel) {

                // check allow to set default codecs by library
                if (hasStandardCodec) {
                    socketChannel.pipeline().addLast("netty-frame-decoder", new Varint21FrameDecoder());
                    socketChannel.pipeline().addLast("netty-frame-prepender", new Varint21LengthFieldEncoder());

                    socketChannel.pipeline().addAfter("netty-frame-decoder", "netty-packet-decoder", new NettyPacketDecoder());
                    socketChannel.pipeline().addAfter("netty-frame-prepender", "netty-packet-encoder", new NettyPacketEncoder());
                }

                // other handlers
                socketChannel.pipeline().addLast("netty-packet-handler", new NettyPacketHandler());

                // submit consumers
                if (channelConsumer != null) channelConsumer.accept(socketChannel);
                if (systemConsumer != null) systemConsumer.accept(socketChannel);
            }

        };
    }

    /**
     * Создать инициализатор общего канала для подключения
     *
     * @param channelConsumer  - ответ, который возвращает канал
     * @param hasStandardCodec - разрешение на установку стандартных кодеков
     */
    public ChannelInitializer<NioSocketChannel> createChannelInitializer(Consumer<NioSocketChannel> channelConsumer,
                                                                         boolean hasStandardCodec) {
        return new ChannelInitializer<NioSocketChannel>() {

            @Override
            protected void initChannel(NioSocketChannel socketChannel) {

                // check allow to set default codecs by library
                if (hasStandardCodec) {
                    socketChannel.pipeline().addLast("netty-frame-decoder", new Varint21FrameDecoder());
                    socketChannel.pipeline().addLast("netty-frame-prepender", new Varint21LengthFieldEncoder());

                    socketChannel.pipeline().addAfter("netty-frame-decoder", "netty-packet-decoder", new NettyPacketDecoder());
                    socketChannel.pipeline().addAfter("netty-frame-prepender", "netty-packet-encoder", new NettyPacketEncoder());
                }

                // other handlers
                socketChannel.pipeline().addLast("netty-packet-handler", new NettyPacketHandler());

                // submit consumers
                if (channelConsumer != null) channelConsumer.accept(socketChannel);
            }

        };
    }

    /**
     * Создать Netty сервер
     *
     * @param socketAddress - адрес, по которому создать сервер
     */
    public NettyServer createServer(@NonNull InetSocketAddress socketAddress) {
        return (savedNettyServer = new NettyServer(socketAddress));
    }

    /**
     * Создать локальный Netty сервер
     *
     * @param serverPort - порт локального сервера
     */
    public NettyServer createLocalServer(int serverPort) {
        return createServer(createSocketAddress("localhost", serverPort));
    }

    /**
     * Создать Netty клиент
     *
     * @param socketAddress - адрес сервера, к которому будет подключаться клиент
     */
    public NettyClient createClient(@NonNull InetSocketAddress socketAddress) {
        return (savedNettyClient = new NettyClient(socketAddress));
    }

    /**
     * Создать локальный Netty клиент
     *
     * @param serverPort - порт локального сервера
     */
    public NettyClient createLocalClient(int serverPort) {
        return createClient(createSocketAddress("localhost", serverPort));
    }

}
