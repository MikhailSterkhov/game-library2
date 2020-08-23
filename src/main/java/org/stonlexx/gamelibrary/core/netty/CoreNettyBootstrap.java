package org.stonlexx.gamelibrary.core.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.AttributeKey;
import lombok.NonNull;
import org.stonlexx.gamelibrary.GameLibrary;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public final class CoreNettyBootstrap {


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
                                           @NonNull ChannelFutureListener futureListener,
                                           @NonNull ChannelInitializer<SocketChannel> channelInitializer,

                                           @NonNull Object... attributes) {

        Bootstrap bootstrap = new Bootstrap()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)

                .channel(NioSocketChannel.class)
                .group(GameLibrary.getInstance().getEventExecutors());

        // add attributes to handler
        for (Object attributeObject : attributes) {

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

            ChannelFuture channelFuture = bootstrap.connect(socketAddress).sync();

            if (futureListener != null) {
                channelFuture.addListener(futureListener);
            }

            return bootstrap;
        }

        // if you`re даун then returns null
        catch (InterruptedException exception) {
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
                                                 @NonNull ChannelFutureListener futureListener,
                                                 @NonNull ChannelInitializer<SocketChannel> channelInitializer,

                                                 @NonNull Object... attributes) {

        ServerBootstrap serverBootstrap = new ServerBootstrap()
                .option(ChannelOption.SO_REUSEADDR, true)

                .channel(NioServerSocketChannel.class)
                .group(GameLibrary.getInstance().getEventExecutors());

        // add attributes to handler
        for (Object attributeObject : attributes) {

            String attributeName = attributeObject.getClass().getSimpleName();
            attributeName = String.valueOf(attributeName.charAt(0)).toLowerCase() + attributeName.substring(1);

            serverBootstrap.childAttr(AttributeKey.newInstance(attributeName), attributeObject);
        }

        // add channel initializer
        if (channelInitializer != null) {
            serverBootstrap.childHandler(channelInitializer);
        }

        // bind bootstrap, add listener & sync
        try {

            ChannelFuture channelFuture = serverBootstrap.bind(socketAddress).sync();

            if (futureListener != null) {
                channelFuture.addListener(futureListener);
            }

            return serverBootstrap;
        }

        // if you`re даун then returns null
        catch (InterruptedException exception) {
            exception.printStackTrace();
        }

        return null;
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
        return InetSocketAddress.createUnresolved(addressHost, addressPort);
    }

    /**
     * Создать инициализатор канала для подключения
     *
     * @param channelConsumer - ответ, который возвращает канал
     */
    public ChannelInitializer<SocketChannel> createChannelInitializer(Consumer<SocketChannel> channelConsumer) {
        return new ChannelInitializer<SocketChannel>() {

            @Override
            protected void initChannel(SocketChannel socketChannel) {
                try {
                    socketChannel.config().setOption(ChannelOption.IP_TOS, 0x18);
                }

                catch (ChannelException exception) {
                    // IP_TOS is not supported (Windows XP / Windows Server 2003)
                }

                socketChannel.config().setAllocator(PooledByteBufAllocator.DEFAULT);

                // accept socket channel to consumer
                if (channelConsumer == null) {
                    return;
                }

                channelConsumer.accept(socketChannel);
            }

        };
    }

}
