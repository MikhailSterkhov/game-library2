package org.stonlexx.gamelibrary.core.netty;

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

    private final NettyBootstrap nettyBootstrap                                             = new NettyBootstrap();
    private final NettyPacketCodecManager packetCodecManager                                = new NettyPacketCodecManager();


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

    /**
     * Получить типизацию пакета по
     * классу этого пакета
     *
     * @param nettyPacketDirection - директория пакета
     * @param nettyPacketClass - класс пакета
     */
    public NettyPacketTyping findTypingByNettyPacket(NettyPacketDirection nettyPacketDirection, Class<? extends NettyPacket> nettyPacketClass) {
        Collection<NettyPacketTyping> nettyPacketTypingCollection = NettyPacketTyping.getPacketTypingMap().values();

        for (NettyPacketTyping nettyPacketTyping : nettyPacketTypingCollection) {
            NettyPacketMapper nettyPacketMapper = nettyPacketTyping.getPacketMapper();

            if (nettyPacketMapper.hasNettyPacket(nettyPacketDirection, nettyPacketClass)) {
                return nettyPacketTyping;
            }
        }

        return null;
    }

    /**
     * Получить номер пакета по его классу
     *
     * @param nettyPacketDirection - директория пакета
     * @param nettyPacketClass - класс пакета
     */
    public int getNettyPacketId(NettyPacketDirection nettyPacketDirection, Class<? extends NettyPacket> nettyPacketClass) {
        NettyPacketTyping nettyPacketTyping = findTypingByNettyPacket(nettyPacketDirection, nettyPacketClass);

        if (nettyPacketTyping == null) {
            return -1;
        }

        NettyPacketMapper nettyPacketMapper = nettyPacketTyping.getPacketMapper();

        return nettyPacketMapper.getPacketMap().get(nettyPacketDirection).get(nettyPacketClass);
    }

}
