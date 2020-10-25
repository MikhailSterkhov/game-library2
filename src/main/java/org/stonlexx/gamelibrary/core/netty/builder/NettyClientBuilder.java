package org.stonlexx.gamelibrary.core.netty.builder;

import io.netty.bootstrap.Bootstrap;
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
import org.stonlexx.gamelibrary.core.netty.NettyManager;
import org.stonlexx.gamelibrary.core.netty.bootstrap.NettyBootstrap;
import org.stonlexx.gamelibrary.core.netty.handler.server.active.AbstractNettyServerActive;
import org.stonlexx.gamelibrary.core.netty.handler.server.reconnect.AbstractNettyReconnect;
import org.stonlexx.gamelibrary.core.netty.packet.NettyPacket;
import org.stonlexx.gamelibrary.core.netty.packet.typing.NettyPacketDirection;
import org.stonlexx.gamelibrary.core.netty.packet.typing.NettyPacketTyping;

import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class NettyClientBuilder<K> {

    private final InetSocketAddress inetSocketAddress;
    private final Class<K> packetKeyClass;

    private NettyPacketTyping<K> nettyPacketTyping;
    private ChannelFutureListener channelFutureListener;
    private ChannelInitializer<NioSocketChannel> channelInitializer;
    private AbstractNettyReconnect nettyReconnect;

    private final Collection<AbstractNettyServerActive> nettyServerActiveCollection = Collections.synchronizedCollection(new LinkedHashSet<>());

    private boolean standardCodec = false;


    private final NettyManager nettyManager = GameLibrary.getInstance().getNettyManager();
    private final NettyBootstrap nettyBootstrap = nettyManager.getNettyBootstrap();


// ================================================================================================================== //

    public static <K> NettyClientBuilder<K> newLocalClientBuilder(@NonNull int serverPort,
                                                                  @NonNull Class<K> packetKeyClass) {

        return newClientBuilder("localhost", serverPort, packetKeyClass);
    }

    public static <K> NettyClientBuilder<K> newClientBuilder(@NonNull String serverHost,
                                                             @NonNull int serverPort,

                                                             @NonNull Class<K> packetKeyClass) {
        InetSocketAddress inetSocketAddress
                = new InetSocketAddress(serverHost, serverPort);

        return newClientBuilder(inetSocketAddress, packetKeyClass);
    }


    public static <K> NettyClientBuilder<K> newClientBuilder(@NonNull InetSocketAddress inetSocketAddress,
                                                             @NonNull Class<K> packetKeyClass) {

        NettyClientBuilder<K> nettyServerBuilder = new NettyClientBuilder<>(inetSocketAddress, packetKeyClass);
        nettyServerBuilder.nettyPacketTyping = NettyPacketTyping.createPacketTyping(packetKeyClass, RandomStringUtils.randomAlphabetic(16));

        NettyManager nettyManager = GameLibrary.getInstance().getNettyManager();

        nettyManager.getPacketCodecManager().setDecodePacketDirection(NettyPacketDirection.TO_CLIENT);
        nettyManager.getPacketCodecManager().setEncodePacketDirection(NettyPacketDirection.TO_SERVER);

        return nettyServerBuilder;
    }

// ================================================================================================================== //


    /**
     * Применить {@link NettyPacketTyping} для каких-то
     * внутренних изменений
     *
     * @param packetTypingConsumer - обработчик применения изменений
     */
    public NettyClientBuilder<K> acceptPacketTyping(@NonNull Consumer<NettyPacketTyping<K>> packetTypingConsumer) {
        packetTypingConsumer.accept(nettyPacketTyping);
        return this;
    }

    /**
     * Зарегистрировать пакет в созданном {@link NettyPacketTyping}
     *
     * @param nettyPacketDirection - директория пакета
     * @param nettyPacketClass     - класс регистрируемого пакета
     * @param packetKeyId          - ключ, по которому пакет регистрируется
     */
    public NettyClientBuilder<K> registerPacket(@NonNull NettyPacketDirection nettyPacketDirection,

                                                @NonNull Class<? extends NettyPacket> nettyPacketClass,
                                                @NonNull K packetKeyId) {

        nettyPacketTyping.registerPacket(nettyPacketDirection, nettyPacketClass, packetKeyId);
        return this;
    }

    /**
     * Зарегистрировать пакет в созданном {@link NettyPacketTyping}
     * с автоопределением ключа для регистрации
     *
     * @param nettyPacketDirection - директория пакета
     * @param nettyPacketClass     - класс регистрируемого пакета
     */
    public NettyClientBuilder<K> registerPacket(@NonNull NettyPacketDirection nettyPacketDirection,
                                                @NonNull Class<? extends NettyPacket> nettyPacketClass) {

        nettyPacketTyping.registerPacket(nettyPacketDirection, nettyPacketClass);
        return this;
    }

    /**
     * Установить серверу стандартные кодеки
     * пакетов от GameLibrary2
     */
    public NettyClientBuilder<K> standardCodec() {

        this.standardCodec = true;
        return this;
    }

    /**
     * Прослушка результата подключения клиента
     * к серверу
     *
     * @param resultConsumer - обработчик прослушки
     */
    public NettyClientBuilder<K> futureListener(@NonNull BiConsumer<ChannelFuture, Boolean> resultConsumer) {
        return futureListener(nettyBootstrap.createFutureListener(resultConsumer));
    }

    /**
     * Прослушка результата подключения клиента
     * к серверу
     *
     * @param channelFutureListener - Слушатель результата
     */
    public NettyClientBuilder<K> futureListener(@NonNull ChannelFutureListener channelFutureListener) {
        this.channelFutureListener = channelFutureListener;
        return this;
    }

    /**
     * Обработка канала для подключения
     *
     * @param channelConsumer - обработчик канала
     */
    public NettyClientBuilder<K> channelInitializer(Consumer<NioSocketChannel> channelConsumer) {
        return channelInitializer(nettyBootstrap.createClientChannelInitializer(channelConsumer, nettyReconnect, nettyServerActiveCollection, standardCodec));
    }

    /**
     * Обработка канала для подключения
     *
     * @param channelInitializer - обработчик канала
     */
    public NettyClientBuilder<K> channelInitializer(@NonNull ChannelInitializer<NioSocketChannel> channelInitializer) {
        this.channelInitializer = channelInitializer;
        return this;
    }

    /**
     * Установить обработчик переподключения клиента
     * к серверу при неожиданном падении сервера
     *
     * @param nettyReconnect - обработчик переподключения
     */
    public NettyClientBuilder<K> reconnectHandler(@NonNull AbstractNettyReconnect nettyReconnect) {
        this.nettyReconnect = nettyReconnect;
        return this;
    }

    /**
     * Добавить обработчик подключения
     * сервера к клиенту
     *
     * @param nettyServerActive - обработчик подключения
     */
    public NettyClientBuilder<K> serverActive(@NonNull AbstractNettyServerActive nettyServerActive) {
        nettyServerActiveCollection.add(nettyServerActive);
        return this;
    }


    /**
     * После указания всех настроек и инициализации
     * всех необходимых данных и переменных,
     * подключаем клиент к серверу, вызывая указанные
     * слушатели и приводя в работу обработчики
     */
    public Bootstrap connectToServer() {
        if (channelInitializer == null) {
            channelInitializer((Consumer<NioSocketChannel>) null);
        }

        if (channelFutureListener == null) {
            futureListener((ChannelFutureListener) null);
        }

        Bootstrap bootstrap = nettyBootstrap.createClientBootstrap(inetSocketAddress, channelFutureListener, channelInitializer);

        if (nettyReconnect != null) {
            nettyReconnect.setConnectRunnable(
                    () -> nettyBootstrap.createClientBootstrap(inetSocketAddress, channelFutureListener, channelInitializer)
            );
        }

        return bootstrap;
    }

}
