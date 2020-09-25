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
import lombok.NonNull;
import org.stonlexx.gamelibrary.core.netty.packet.codec.NettyPacketDecoder;
import org.stonlexx.gamelibrary.core.netty.packet.codec.NettyPacketEncoder;
import org.stonlexx.gamelibrary.core.netty.packet.handler.NettyPacketHandler;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public final class NettyBootstrap {

    private final NioEventLoopGroup bossEventLoop = new NioEventLoopGroup(1);
    private final NioEventLoopGroup workerEventLoop = new NioEventLoopGroup(2);


    /**
     * Создать клиентский bootstrap для подключения
     * к уже заранее забиндованому серверному bootstrap
     *
     * @param socketAddress - адрес серверного bootstrap
     * @param futureListener - ответ от подключения
     * @param channelInitializer - инициализация канала подключения
     * @param attributes - атрибуты для канала
     */
    public Bootstrap createClientBootstrap(@NonNull SocketAddress socketAddress,
                                           ChannelFutureListener futureListener,
                                           ChannelInitializer<NioSocketChannel> channelInitializer,

                                           @NonNull Object... attributes) {

        Bootstrap bootstrap = new Bootstrap()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)

                .remoteAddress(socketAddress)

                .channel(NioSocketChannel.class)
                .group(bossEventLoop);

        // add attributes to handler
        for (Object attributeObject : attributes) {

            if (attributeObject instanceof NettyBootstrapChannelAttribute) {
                NettyBootstrapChannelAttribute<?> channelAttribute = ((NettyBootstrapChannelAttribute<?>) attributeObject);

                bootstrap.attr(AttributeKey.newInstance(channelAttribute.getAttributeName()), channelAttribute.getAttributeObject());
                continue;
            }

            String attributeName = attributeObject.getClass().getSimpleName();
            attributeName = String.valueOf(attributeName.charAt(0)).toLowerCase() + attributeName.substring(1);

            bootstrap.attr(AttributeKey.newInstance(attributeName), attributeObject);
        }

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
     * @param socketAddress - адрес серверного bootstrap
     * @param futureListener - ответ от подключения
     * @param channelInitializer - инициализация канала подключения
     * @param attributes - атрибуты для канала
     */
    public ServerBootstrap createServerBootstrap(@NonNull SocketAddress socketAddress,
                                                 ChannelFutureListener futureListener,
                                                 ChannelInitializer<NioSocketChannel> channelInitializer,

                                                 @NonNull Object... attributes) {

        ServerBootstrap serverBootstrap = new ServerBootstrap()

                .option(ChannelOption.SO_REUSEADDR, true)
                .option(ChannelOption.SO_BACKLOG, 120)
                .childOption(ChannelOption.SO_KEEPALIVE, true)

                .localAddress(socketAddress)

                .channel(NioServerSocketChannel.class)
                .group(bossEventLoop, workerEventLoop);

        // add attributes to handler
        for (Object attributeObject : attributes) {

            if (attributeObject instanceof NettyBootstrapChannelAttribute) {
                NettyBootstrapChannelAttribute<?> channelAttribute = ((NettyBootstrapChannelAttribute<?>) attributeObject);

                serverBootstrap.childAttr(AttributeKey.newInstance(channelAttribute.getAttributeName()), channelAttribute.getAttributeObject());
                continue;
            }

            String attributeName = attributeObject.getClass().getSimpleName();
            attributeName = String.valueOf(attributeName.charAt(0)).toLowerCase() + attributeName.substring(1);

            serverBootstrap.childAttr(AttributeKey.newInstance(attributeName), attributeObject);
        }

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
        return future -> futureConsumer.accept(future, future.isSuccess());
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
     * @param channelConsumer - ответ, который возвращает канал
     */
    public ChannelInitializer<NioSocketChannel> createChannelInitializer(Consumer<NioSocketChannel> channelConsumer) {
        return createChannelInitializer(channelConsumer, null, null);
    }

    /**
     * Создать инициализатор канала для подключения
     *
     * @param channelConsumer - ответ, который возвращает канал
     *
     * @param nettyPacketDecoder - кодек, принимающий байты
     * @param nettyPacketEncoder - кодек, отправляющий байты
     */
    public ChannelInitializer<NioSocketChannel> createChannelInitializer(Consumer<NioSocketChannel> channelConsumer,

                                                                      NettyPacketDecoder nettyPacketDecoder,
                                                                      NettyPacketEncoder nettyPacketEncoder) {
        return new ChannelInitializer<NioSocketChannel>() {

            @Override
            protected void initChannel(NioSocketChannel socketChannel) {

                if (nettyPacketDecoder != null) {
                    socketChannel.pipeline().addLast("packet-decoder", nettyPacketDecoder);
                }

                if (nettyPacketEncoder != null) {
                    socketChannel.pipeline().addLast("packet-encoder", nettyPacketEncoder);
                }

                socketChannel.pipeline().addLast("packet-handler", new NettyPacketHandler());


                // accept socket channel to consumer
                if (channelConsumer == null) {
                    return;
                }

                channelConsumer.accept(socketChannel);
            }

        };
    }

}
