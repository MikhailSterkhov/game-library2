package org.stonlexx.gamelibrary.core.netty.bootstrap.impl;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.stonlexx.gamelibrary.GameLibrary;
import org.stonlexx.gamelibrary.core.netty.NettyConnection;
import org.stonlexx.gamelibrary.core.netty.NettyManager;
import org.stonlexx.gamelibrary.core.netty.bootstrap.NettyBootstrapChannel;
import org.stonlexx.gamelibrary.core.netty.builder.NettyClientBuilder;
import org.stonlexx.gamelibrary.core.netty.handler.server.active.AbstractNettyServerActive;
import org.stonlexx.gamelibrary.core.netty.handler.server.active.impl.NettyConsumerServerActive;
import org.stonlexx.gamelibrary.core.netty.handler.server.reconnect.AbstractNettyReconnect;
import org.stonlexx.gamelibrary.core.netty.packet.NettyPacket;
import org.stonlexx.gamelibrary.core.netty.packet.typing.NettyPacketDirection;
import org.stonlexx.gamelibrary.core.netty.packet.typing.NettyPacketTyping;
import org.stonlexx.gamelibrary.utility.query.ResponseHandler;

import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

@RequiredArgsConstructor
@Getter
public class NettyClient implements NettyBootstrapChannel {

    protected final InetSocketAddress socketAddress;

    protected ChannelFutureListener channelFutureListener;
    protected ChannelInitializer<NioSocketChannel> channelInitializer;

    protected boolean standardCodec;

    @Setter
    protected AbstractNettyReconnect nettyReconnect;

    protected final Collection<AbstractNettyServerActive> nettyServerActiveCollection = Collections.synchronizedCollection(new LinkedHashSet<>());

    protected Bootstrap bootstrap;
    protected Channel channel;

    protected NettyConnection serverConnection;
    protected NettyConnection nettyConnection;


// ======================================================== // STATIC // ======================================================== //

    protected static final NettyManager NETTY_MANAGER = GameLibrary.getInstance().getNettyManager();

// ============================================================================================================================= //

    {
        addNettyServerActive(new NettyConsumerServerActive(channel -> this.serverConnection = new NettyConnection((InetSocketAddress) channel.remoteAddress(), channel)));
    }


    /**
     * Установить слушатель создания и
     * подключения канала к серверу
     *
     * @param channelFutureConsumer - обработчик слушателя
     */
    public void setChannelFutureListener(BiConsumer<ChannelFuture, Boolean> channelFutureConsumer) {
        this.channelFutureListener = NETTY_MANAGER.getNettyBootstrap().createFutureListener((channelFuture, isSuccess) -> {

            this.channel = channelFuture.channel();
            this.nettyConnection = new NettyConnection(socketAddress, channel);

            if (channelFutureConsumer != null) {
                channelFutureConsumer.accept(channelFuture, isSuccess);
            }
        });
    }

    /**
     * Создать инициализатор канала для подключения
     *
     * @param channelConsumer - ответ, который возвращает канал
     */
    public void setChannelInitializer(Consumer<NioSocketChannel> channelConsumer) {
        this.channelInitializer = NETTY_MANAGER.getNettyBootstrap()
                .createClientChannelInitializer(channelConsumer, nettyReconnect, nettyServerActiveCollection, standardCodec);
    }

    /**
     * Добавить обработчик подключения
     * сервера к клиенту
     *
     * @param nettyServerActive - обработчик подключения
     */
    public void addNettyServerActive(@NonNull AbstractNettyServerActive nettyServerActive) {
        nettyServerActiveCollection.add(nettyServerActive);
    }

    /**
     * Установить серверу стандартные кодеки
     * пакетов от GameLibrary2
     */
    public void setStandardCodec() {
        this.standardCodec = true;
    }

    /**
     * Применить {@link NettyPacketTyping} для регистрации
     * новых пакетов и внутренних изменений
     *
     * @param packetKeyClass         - класс для указания типа ключей для пакетов
     * @param packetRegistryConsumer - обработчик регистратора пакетов
     */
    public <K> void createPacketRegistry(@NonNull Class<K> packetKeyClass,

                                         ResponseHandler<K, Class<? extends NettyPacket>> packetKeyHandler,
                                         Consumer<ClientPacketRegistry<K>> packetRegistryConsumer) {

        ClientPacketRegistry<K> serverPacketRegistry = new ClientPacketRegistry<>(
                packetKeyClass, NettyPacketTyping.createPacketTyping(packetKeyClass, RandomStringUtils.randomAlphabetic(16)));

        if (packetKeyHandler != null) {
            serverPacketRegistry.nettyPacketTyping.setPacketKeyHandler(packetKeyHandler);
        }

        if (packetRegistryConsumer != null) {
            packetRegistryConsumer.accept(serverPacketRegistry);
        }
    }

    /**
     * Зарегистрировать все пакеты, которые
     * имеют аннотацию {@link org.stonlexx.gamelibrary.core.netty.packet.annotation.PacketAutoRegister}
     * и хранятся в указанном пакейдже
     *
     * @param packageName - имя пакейджа для скана
     */
    public void autoRegisterPackets(@NonNull String packageName) {
        NETTY_MANAGER.getAutoRegisterPacketTyping().autoRegisterPackets(packageName);
    }


    /**
     * После указания всех настроек и инициализации
     * всех необходимых данных и переменных,
     * подключаем клиент к серверу, вызывая указанные
     * слушатели и приводя в работу обработчики
     */
    public void connect() {
        if (channelFutureListener == null) {
            setChannelFutureListener(null);
        }

        if (channelInitializer == null) {
            setChannelInitializer(null);
        }

        NettyClientBuilder<String> nettyClientBuilder = NettyClientBuilder.newClientBuilder(socketAddress, String.class)
                .futureListener(channelFutureListener)
                .channelInitializer(channelInitializer);

        if (nettyReconnect != null) {
            nettyClientBuilder.reconnectHandler(nettyReconnect);
        }

        this.bootstrap = nettyClientBuilder.connectToServer();
    }


    @RequiredArgsConstructor(access = AccessLevel.PACKAGE)
    @Getter
    public static class ClientPacketRegistry<K> {

        private final Class<K> packetKeyClass;
        private final NettyPacketTyping<K> nettyPacketTyping;


        /**
         * Зарегистрировать пакет в созданном {@link NettyPacketTyping}
         *
         * @param nettyPacketDirection - директория пакета
         * @param nettyPacketClass     - класс регистрируемого пакета
         * @param packetKeyId          - ключ, по которому пакет регистрируется
         */
        public void registerPacket(@NonNull NettyPacketDirection nettyPacketDirection,

                                   @NonNull Class<? extends NettyPacket> nettyPacketClass,
                                   @NonNull K packetKeyId) {

            nettyPacketTyping.registerPacket(nettyPacketDirection, nettyPacketClass, packetKeyId);
        }

        /**
         * Зарегистрировать пакет в созданном {@link NettyPacketTyping}
         * с автоопределением ключа для регистрации
         *
         * @param nettyPacketDirection - директория пакета
         * @param nettyPacketClass     - класс регистрируемого пакета
         */
        public void registerPacket(@NonNull NettyPacketDirection nettyPacketDirection,
                                   @NonNull Class<? extends NettyPacket> nettyPacketClass) {

            nettyPacketTyping.registerPacket(nettyPacketDirection, nettyPacketClass);
        }
    }

}
