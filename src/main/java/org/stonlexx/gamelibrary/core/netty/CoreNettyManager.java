package org.stonlexx.gamelibrary.core.netty;

import lombok.Getter;
import lombok.NonNull;
import org.stonlexx.gamelibrary.core.netty.bootstrap.CoreNettyBootstrap;
import org.stonlexx.gamelibrary.core.netty.packet.NettyPacket;
import org.stonlexx.gamelibrary.core.netty.packet.typing.NettyPacketDirection;
import org.stonlexx.gamelibrary.core.netty.packet.typing.NettyPacketTyping;

@Getter
public final class CoreNettyManager {

    private final CoreNettyBootstrap nettyBootstrap = new CoreNettyBootstrap();


    /**
     * Создать (если не существет) и получить
     * типизацию пакетов
     *
     * @param typingName - название типизации
     */
    public NettyPacketTyping getPacketTyping(String typingName) {
        return NettyPacketTyping.getPacketTyping(typingName);
    }

    /**
     * Зарегистрировать пакет в маппере
     *
     * @param nettyPacketTyping - типизация пакета
     * @param nettyPacketDirection - директория пакета
     * @param nettyPacketClass - класс регистрируемого пакета
     * @param packetId - номер пакета для регистрации
     */
    public void registerPacket(@NonNull NettyPacketTyping nettyPacketTyping,

                               @NonNull NettyPacketDirection nettyPacketDirection,
                               @NonNull Class<? extends NettyPacket> nettyPacketClass,

                               int packetId) {

        nettyPacketTyping.registerPacket(nettyPacketDirection, nettyPacketClass, packetId);
    }

    /**
     * Зарегистрировать пакет в маппере
     *
     * @param packetTypingName - типизация пакета
     * @param nettyPacketDirection - директория пакета
     * @param nettyPacketClass - класс регистрируемого пакета
     * @param packetId - номер пакета для регистрации
     */
    public void registerPacket(@NonNull String packetTypingName,

                               @NonNull NettyPacketDirection nettyPacketDirection,
                               @NonNull Class<? extends NettyPacket> nettyPacketClass,

                               int packetId) {

        getPacketTyping(packetTypingName).registerPacket(nettyPacketDirection, nettyPacketClass, packetId);
    }

    /**
     * Получить зарегистрированный пакет по его номеру
     *
     * @param nettyPacketTyping - типизация пакета
     * @param nettyPacketDirection - директория пакета
     * @param packetId - номер получаемого пакета
     */
    public NettyPacket getNettyPacket(@NonNull NettyPacketTyping nettyPacketTyping,
                                      @NonNull NettyPacketDirection nettyPacketDirection,

                                      int packetId) {

        return nettyPacketTyping.getNettyPacket(nettyPacketDirection, packetId);
    }

    /**
     * Получить зарегистрированный пакет по его номеру
     *
     * @param packetTypingName - типизация пакета
     * @param nettyPacketDirection - директория пакета
     * @param packetId - номер получаемого пакета
     */
    public NettyPacket getNettyPacket(@NonNull String packetTypingName,
                                      @NonNull NettyPacketDirection nettyPacketDirection,

                                      int packetId) {

        return getPacketTyping(packetTypingName).getNettyPacket(nettyPacketDirection, packetId);
    }

}
