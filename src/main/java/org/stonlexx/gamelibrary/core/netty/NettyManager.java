package org.stonlexx.gamelibrary.core.netty;

import io.netty.channel.Channel;
import lombok.Getter;
import lombok.NonNull;
import org.stonlexx.gamelibrary.core.netty.bootstrap.NettyBootstrap;
import org.stonlexx.gamelibrary.core.netty.packet.NettyPacket;
import org.stonlexx.gamelibrary.core.netty.packet.codec.NettyPacketCodecManager;
import org.stonlexx.gamelibrary.core.netty.packet.mapping.NettyPacketMapper;
import org.stonlexx.gamelibrary.core.netty.packet.typing.NettyPacketDirection;
import org.stonlexx.gamelibrary.core.netty.packet.typing.NettyPacketTyping;

import java.util.Collection;

@Getter
public final class NettyManager {

    private final NettyBootstrap nettyBootstrap                 = new NettyBootstrap();
    private final NettyPacketCodecManager packetCodecManager    = new NettyPacketCodecManager();

    private Channel savedChannel;


    /**
     * Сохранить успешно подключенный канал
     *
     * @param channel - канал
     */
    public void saveChannel(Channel channel) {
        if (channel != null && (!channel.isActive() || !channel.isOpen())) {
            return;
        }

        this.savedChannel = channel;
    }

    /**
     * Отправить пакет сохраненному каналу
     *
     * @param nettyPacket - пакет
     */
    public void sendPacket(@NonNull NettyPacket nettyPacket) {
        savedChannel.writeAndFlush(nettyPacket);
    }

    /**
     * Создать (если не существет) и получить
     * типизацию пакетов
     *
     * @param typingName - название типизации
     */
    public <T> NettyPacketTyping<T> getPacketTyping(Class<T> packetKeyClass, String typingName) {
        return NettyPacketTyping.createPacketTyping(packetKeyClass, typingName);
    }

    /**
     * Зарегистрировать пакет в маппере
     *
     * @param nettyPacketTyping    - типизация пакета
     * @param nettyPacketDirection - директория пакета
     * @param nettyPacketClass     - класс регистрируемого пакета
     * @param packetId             - номер пакета для регистрации
     */
    public <T> void registerPacket(@NonNull NettyPacketTyping<T> nettyPacketTyping,

                                   @NonNull NettyPacketDirection nettyPacketDirection,
                                   @NonNull Class<? extends NettyPacket> nettyPacketClass,

                                   T packetId) {

        nettyPacketTyping.registerPacket(nettyPacketDirection, nettyPacketClass, packetId);
    }

    /**
     * Зарегистрировать пакет в маппере
     *
     * @param packetTypingName     - типизация пакета
     * @param nettyPacketDirection - директория пакета
     * @param nettyPacketClass     - класс регистрируемого пакета
     * @param packetId             - номер пакета для регистрации
     */
    public <T> void registerPacket(@NonNull Class<T> packetKeyClass,
                                   @NonNull String packetTypingName,

                                   @NonNull NettyPacketDirection nettyPacketDirection,
                                   @NonNull Class<? extends NettyPacket> nettyPacketClass,

                                   T packetId) {

        getPacketTyping(packetKeyClass, packetTypingName).registerPacket(nettyPacketDirection, nettyPacketClass, packetId);
    }

    /**
     * Получить зарегистрированный пакет по его номеру
     *
     * @param nettyPacketTyping    - типизация пакета
     * @param nettyPacketDirection - директория пакета
     * @param packetId             - номер получаемого пакета
     */
    public <T> NettyPacket getNettyPacket(@NonNull NettyPacketTyping<T> nettyPacketTyping,
                                          @NonNull NettyPacketDirection nettyPacketDirection,

                                          T packetId) {

        return nettyPacketTyping.getNettyPacket(nettyPacketDirection, packetId);
    }

    /**
     * Получить зарегистрированный пакет по его номеру
     *
     * @param packetTypingName     - типизация пакета
     * @param nettyPacketDirection - директория пакета
     * @param packetId             - номер получаемого пакета
     */
    public <T> NettyPacket getNettyPacket(@NonNull Class<T> packetKeyClass,
                                          @NonNull String packetTypingName,
                                          @NonNull NettyPacketDirection nettyPacketDirection,

                                          T packetId) {

        return getPacketTyping(packetKeyClass, packetTypingName).getNettyPacket(nettyPacketDirection, packetId);
    }

    /**
     * Получить типизацию пакета по
     * классу этого пакета
     *
     * @param nettyPacketDirection - директория пакета
     * @param nettyPacketClass     - класс пакета
     */
    public <T> NettyPacketTyping<T> findTypingByNettyPacket(@NonNull NettyPacketDirection nettyPacketDirection,
                                                            @NonNull Class<? extends NettyPacket> nettyPacketClass) {

        Collection<NettyPacketTyping<?>> nettyPacketTypingCollection = NettyPacketTyping.getPacketTypingMap().values();

        for (NettyPacketTyping<?> nettyPacketTyping : nettyPacketTypingCollection) {
            NettyPacketMapper<T> nettyPacketMapper = (NettyPacketMapper<T>) nettyPacketTyping.getPacketMapper();

            if (nettyPacketMapper.hasNettyPacket(nettyPacketDirection, nettyPacketClass)) {
                return (NettyPacketTyping<T>) nettyPacketTyping;
            }
        }

        return null;
    }

    /**
     * Получить типизацию пакета по
     * классу этого пакета
     *
     * @param nettyPacketDirection - директория пакета
     * @param nettyPacketId        - номер пакета
     */
    public <T> NettyPacketTyping<T> findTypingByNettyPacket(@NonNull NettyPacketDirection nettyPacketDirection,
                                                            @NonNull T nettyPacketId) {

        Collection<NettyPacketTyping<?>> nettyPacketTypingCollection = NettyPacketTyping.getPacketTypingMap().values();

        for (NettyPacketTyping<?> nettyPacketTyping : nettyPacketTypingCollection) {
            NettyPacketMapper<T> nettyPacketMapper = (NettyPacketMapper<T>) nettyPacketTyping.getPacketMapper();

            if (nettyPacketMapper.hasNettyPacketById(nettyPacketDirection, nettyPacketId)) {
                return (NettyPacketTyping<T>) nettyPacketTyping;
            }
        }

        return null;
    }

    /**
     * Получить номер пакета по его классу
     *
     * @param nettyPacketDirection - директория пакета
     * @param nettyPacketClass     - класс пакета
     */
    public <T> T getNettyPacketId(NettyPacketDirection nettyPacketDirection, Class<? extends NettyPacket> nettyPacketClass) {
        NettyPacketTyping<T> nettyPacketTyping = findTypingByNettyPacket(nettyPacketDirection, nettyPacketClass);

        if (nettyPacketTyping == null) {
            return null;
        }

        NettyPacketMapper<T> nettyPacketMapper = nettyPacketTyping.getPacketMapper();
        return nettyPacketMapper.getPacketMap().get(nettyPacketDirection).get(nettyPacketClass);
    }

}
