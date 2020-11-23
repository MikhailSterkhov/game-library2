package org.stonlexx.gamelibrary.common.netty.bootstrap.impl;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.stonlexx.gamelibrary.GameLibrary;
import org.stonlexx.gamelibrary.common.netty.NettyConnection;
import org.stonlexx.gamelibrary.common.netty.NettyManager;
import org.stonlexx.gamelibrary.common.netty.bootstrap.NettyBootstrap;
import org.stonlexx.gamelibrary.common.netty.bootstrap.NettyBootstrapChannel;
import org.stonlexx.gamelibrary.common.netty.builder.NettyServerBuilder;
import org.stonlexx.gamelibrary.common.netty.handler.client.active.AbstractNettyClientActive;
import org.stonlexx.gamelibrary.common.netty.handler.client.active.impl.NettyConsumerClientActive;
import org.stonlexx.gamelibrary.common.netty.handler.client.inactive.AbstractNettyClientInactive;
import org.stonlexx.gamelibrary.common.netty.handler.client.inactive.impl.NettyConsumerClientInactive;
import org.stonlexx.gamelibrary.common.netty.packet.NettyPacket;
import org.stonlexx.gamelibrary.common.netty.packet.callback.NettyPacketCallbackHandler;
import org.stonlexx.gamelibrary.common.netty.packet.typing.NettyPacketDirection;
import org.stonlexx.gamelibrary.common.netty.packet.typing.NettyPacketTyping;
import org.stonlexx.gamelibrary.utility.query.ResponseHandler;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

@RequiredArgsConstructor
@Getter
public class NettyServer implements NettyBootstrapChannel {

    protected final InetSocketAddress socketAddress;

    protected ChannelFutureListener channelFutureListener;
    protected ChannelInitializer<NioSocketChannel> channelInitializer;

    protected boolean standardCodec;

    protected final Collection<AbstractNettyClientInactive> nettyClientInactiveCollection = Collections.synchronizedCollection(new LinkedHashSet<>());
    protected final Collection<AbstractNettyClientActive> nettyClientActiveCollection = Collections.synchronizedCollection(new LinkedHashSet<>());


    protected ServerBootstrap serverBootstrap;
    protected Channel channel;
    protected NettyConnection nettyConnection;

    protected final Map<SocketAddress, NettyConnection> clientChannelMap = new LinkedHashMap<>();


// ======================================================== // STATIC // ======================================================== //

    protected static final NettyManager NETTY_MANAGER = GameLibrary.getInstance().getNettyManager();

// ============================================================================================================================= //

    {
        addNettyClientActive(new NettyConsumerClientActive(channel -> clientChannelMap.put(channel.remoteAddress(), new NettyConnection((InetSocketAddress) channel.remoteAddress(), channel))));
        addNettyClientInactive(new NettyConsumerClientInactive(channel -> clientChannelMap.remove(channel.remoteAddress())));
    }


    /**
     * Установить слушатель создания и
     * подключения канала
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
     * Создать инициализатор канала для бинда
     *
     * @param channelConsumer - ответ, который возвращает канал
     */
    public void setChannelInitializer(Consumer<NioSocketChannel> channelConsumer) {
        this.channelInitializer = NETTY_MANAGER.getNettyBootstrap().createServerChannelInitializer(channelConsumer, nettyClientActiveCollection, nettyClientInactiveCollection, standardCodec);
    }

    /**
     * Добавить обработчик отключения клиентов от сервера
     *
     * @param nettyClientInactive - обработчик отключения
     */
    public void addNettyClientInactive(@NonNull AbstractNettyClientInactive nettyClientInactive) {
        nettyClientInactiveCollection.add(nettyClientInactive);
    }

    /**
     * Добавить обработчик подключения клиентов к серверу
     *
     * @param nettyClientActive - обработчик подключения
     */
    public void addNettyClientActive(@NonNull AbstractNettyClientActive nettyClientActive) {
        nettyClientActiveCollection.add(nettyClientActive);
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
                                         Consumer<ServerPacketRegistry<K>> packetRegistryConsumer) {

        ServerPacketRegistry<K> serverPacketRegistry = new ServerPacketRegistry<>(
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
     * имеют аннотацию {@link org.stonlexx.gamelibrary.common.netty.packet.annotation.PacketAutoRegister}
     * и хранятся в указанном пакейдже
     *
     * @param packageName - имя пакейджа для скана
     */
    public void autoRegisterPackets(@NonNull String packageName) {
        NETTY_MANAGER.getAutoRegisterPacketTyping().autoRegisterPackets(packageName);
    }

    /**
     * Отправить пакет всем клиентам, подключенным
     * к серверу
     *
     * @param nettyPacket - пакет
     */
    public void sendPacketToClients(@NonNull NettyPacket nettyPacket) {
        for (NettyConnection nettyConnection : clientChannelMap.values())
            nettyConnection.sendPacket(nettyPacket);
    }

    /**
     * Отправить пакет всем клиентам, подключенным
     * к серверу
     *
     * @param nettyPacket - пакет
     */
    public <P extends NettyPacket> void sendPacketToClients(@NonNull P nettyPacket, @NonNull NettyPacketCallbackHandler<P> nettyPacketCallbackHandler) {
        for (NettyConnection nettyConnection : clientChannelMap.values())
            nettyConnection.sendPacket(nettyPacket, nettyPacketCallbackHandler);
    }

    /**
     * Получить подключенный к данному серверу клиент
     * по его адресу
     *
     * @param socketAddress - адрес клиента
     */
    public NettyConnection getConnectedClient(@NonNull SocketAddress socketAddress) {
        return clientChannelMap.get(socketAddress);
    }


    /**
     * После указания всех настроек и инициализации
     * всех необходимых данных и переменных,
     * биндом порт сервера, вызывая указанные
     * слушатели и приводя в работу обработчики
     */
    public void bind() {
        if (channelFutureListener == null) {
            setChannelFutureListener(null);
        }

        if (channelInitializer == null) {
            setChannelInitializer(null);
        }

        NettyServerBuilder<String> nettyServerBuilder = NettyServerBuilder.newServerBuilder(socketAddress, String.class)
                .futureListener(channelFutureListener)
                .channelInitializer(channelInitializer);

        this.serverBootstrap = nettyServerBuilder.bindServer();
    }


    @RequiredArgsConstructor(access = AccessLevel.PACKAGE)
    @Getter
    public static class ServerPacketRegistry<K> {

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


    private static final NettyBootstrap NETTY_BOOTSTRAP = GameLibrary.getInstance().getNettyManager().getNettyBootstrap();

    public static NettyServer create(@NonNull InetSocketAddress inetSocketAddress) {
        return NETTY_BOOTSTRAP.createServer(inetSocketAddress);
    }

    public static NettyServer createLocal(int serverPort) {
        return NETTY_BOOTSTRAP.createLocalServer(serverPort);
    }

}
