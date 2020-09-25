package org.stonlexx.gamelibrary.core.netty.builder;

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
import org.stonlexx.gamelibrary.core.netty.packet.NettyPacket;
import org.stonlexx.gamelibrary.core.netty.packet.typing.NettyPacketDirection;
import org.stonlexx.gamelibrary.core.netty.packet.typing.NettyPacketTyping;

import java.net.InetSocketAddress;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class NettyServerBuilder<K> {

    private final InetSocketAddress inetSocketAddress;
    private final Class<K> packetKeyClass;

    private NettyPacketTyping<K> nettyPacketTyping;
    private ChannelFutureListener channelFutureListener;
    private ChannelInitializer<NioSocketChannel> channelInitializer;

    private Object[] bootstrapAttributes = new Object[0];


    private final NettyManager nettyManager = GameLibrary.getInstance().getNettyManager();
    private final NettyBootstrap nettyBootstrap = nettyManager.getNettyBootstrap();


// ================================================================================================================== //

    public static <K> NettyServerBuilder<K> newServerBuilder(@NonNull String serverHost,
                                                             @NonNull int serverPort,

                                                             @NonNull Class<K> packetKeyClass) {
        InetSocketAddress inetSocketAddress
                = InetSocketAddress.createUnresolved(serverHost, serverPort);

        return newServerBuilder(inetSocketAddress, packetKeyClass);
    }


    public static <K> NettyServerBuilder<K> newServerBuilder(@NonNull InetSocketAddress inetSocketAddress,
                                                             @NonNull Class<K> packetKeyClass) {

        NettyServerBuilder<K> nettyServerBuilder = new NettyServerBuilder<>(inetSocketAddress, packetKeyClass);
        nettyServerBuilder.nettyPacketTyping = NettyPacketTyping.getPacketTyping(packetKeyClass, RandomStringUtils.randomAlphabetic(16));

        return nettyServerBuilder;
    }

// ================================================================================================================== //


    /**
     * Применить {@link NettyPacketTyping} для каких-то
     * внутренних изменений
     *
     * @param packetTypingConsumer - обработчик применения изменений
     */
    public NettyServerBuilder<K> acceptPacketTyping(@NonNull Consumer<NettyPacketTyping<K>> packetTypingConsumer) {

        packetTypingConsumer.accept(nettyPacketTyping);
        return this;
    }

    /**
     * Зарегистрировать пакет в созданном {@link NettyPacketTyping}
     *
     * @param nettyPacketDirection - директория пакета
     * @param nettyPacketClass - класс регистрируемого пакета
     * @param packetKeyId - ключ, по которому пакет регистрируется
     */
    public NettyServerBuilder<K> registerPacket(@NonNull NettyPacketDirection nettyPacketDirection,

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
     * @param nettyPacketClass - класс регистрируемого пакета
     */
    public NettyServerBuilder<K> registerPacket(@NonNull NettyPacketDirection nettyPacketDirection,
                                                @NonNull Class<? extends NettyPacket> nettyPacketClass) {

        nettyPacketTyping.registerPacket(nettyPacketDirection, nettyPacketClass);
        return this;
    }

    /**
     * Указать атрибуты в ныне созданном
     * канале после бинда порта сервера
     */
    public NettyServerBuilder<K> bootstrapAttributes(@NonNull Object... bootstrapAttributes) {

        this.bootstrapAttributes = bootstrapAttributes;
        return this;
    }

    /**
     * Прослушка результата бинда порта сервера
     *
     * @param resultConsumer - обработчик прослушки
     */
    public NettyServerBuilder<K> futureListener(@NonNull BiConsumer<ChannelFuture, Boolean> resultConsumer) {
        return futureListener(nettyBootstrap.createFutureListener(resultConsumer));
    }

    /**
     * Прослушка результата бинда порта сервера
     *
     * @param channelFutureListener - Слушатель результата
     */
    public NettyServerBuilder<K> futureListener(@NonNull ChannelFutureListener channelFutureListener) {

        this.channelFutureListener = channelFutureListener;
        return this;
    }

    /**
     * Обработка канала для бинда
     *
     * @param channelConsumer - обработчик канала
     */
    public NettyServerBuilder<K> channelInitializer(Consumer<NioSocketChannel> channelConsumer) {
        return channelInitializer(nettyBootstrap.createChannelInitializer(channelConsumer));
    }

    /**
     * Обработка канала для бинда
     *
     * @param channelInitializer - обработчик канала
     */
    public NettyServerBuilder<K> channelInitializer(@NonNull ChannelInitializer<NioSocketChannel> channelInitializer) {

        this.channelInitializer = channelInitializer;
        return this;
    }


    /**
     * После указания всех настроек и инициализации
     * всех необходимых данных и переменных,
     * биндом порт сервера, вызывая указанные
     * слушатели и приводя в работу обработчики
     */
    public void bindServer() {
        nettyBootstrap.createServerBootstrap(
                inetSocketAddress, channelFutureListener, channelInitializer, bootstrapAttributes
        );
    }

}
