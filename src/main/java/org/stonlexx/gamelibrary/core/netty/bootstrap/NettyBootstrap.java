package org.stonlexx.gamelibrary.core.netty.bootstrap;

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
import org.stonlexx.gamelibrary.GameLibrary;
import org.stonlexx.gamelibrary.core.netty.packet.codec.NettyPacketDecoder;
import org.stonlexx.gamelibrary.core.netty.packet.codec.NettyPacketEncoder;
import org.stonlexx.gamelibrary.core.netty.packet.handler.NettyPacketHandler;
import org.stonlexx.gamelibrary.core.netty.reconnect.client.AbstractNettyClientInactive;
import org.stonlexx.gamelibrary.core.netty.reconnect.client.NettyClientInactiveHandler;
import org.stonlexx.gamelibrary.core.netty.reconnect.server.NettyServerReconnectHandler;
import org.stonlexx.gamelibrary.core.netty.reconnect.server.AbstractNettyReconnect;
import org.stonlexx.gamelibrary.core.netty.reconnect.server.EnumReconnectReason;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public final class NettyBootstrap {

    private final NioEventLoopGroup bossEventLoop = new NioEventLoopGroup(1);
    private final NioEventLoopGroup workerEventLoop = new NioEventLoopGroup(2);


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

            if (future.isSuccess()) {
                GameLibrary.getInstance().getNettyManager().saveChannel(future.channel());
            }

            NettyServerReconnectHandler serverReconnectHandler
                    = (NettyServerReconnectHandler) future.channel().pipeline().get("reconnect-handler");

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
    public SocketAddress createSocketAddress(String addressHost, int addressPort) {
        return new InetSocketAddress(addressHost, addressPort);
    }

    /**
     * Создать инициализатор канала для подключения
     *
     * @param channelConsumer  - ответ, который возвращает канал
     * @param hasStandardCodec - разрешение на установку стандартных кодеков
     */
    public ChannelInitializer<NioSocketChannel> createChannelInitializer(Consumer<NioSocketChannel> channelConsumer,
                                                                         AbstractNettyReconnect nettyReconnect,
                                                                         AbstractNettyClientInactive nettyClientInactive,

                                                                         boolean hasStandardCodec) {
        return new ChannelInitializer<NioSocketChannel>() {

            @Override
            protected void initChannel(NioSocketChannel socketChannel) {

                if (hasStandardCodec) {
                    socketChannel.pipeline().addLast("packet-decoder", new NettyPacketDecoder());
                    socketChannel.pipeline().addLast("packet-encoder", new NettyPacketEncoder());
                }

                if (nettyReconnect != null) {
                    socketChannel.pipeline().addLast("reconnect-handler", new NettyServerReconnectHandler(nettyReconnect));
                    socketChannel.attr(AttributeKey.valueOf("reconnect-handler")).set(nettyReconnect);
                }

                if (nettyClientInactive != null) {
                    socketChannel.pipeline().addLast("client-inactive-handler", new NettyClientInactiveHandler(nettyClientInactive));
                }

                socketChannel.pipeline().addLast("packet-handler", new NettyPacketHandler());

                if (channelConsumer != null) {
                    channelConsumer.accept(socketChannel);
                }
            }

        };
    }

}
