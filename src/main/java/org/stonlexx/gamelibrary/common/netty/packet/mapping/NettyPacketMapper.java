package org.stonlexx.gamelibrary.common.netty.packet.mapping;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import lombok.Getter;
import lombok.NonNull;
import org.stonlexx.gamelibrary.common.netty.exception.NettyException;
import org.stonlexx.gamelibrary.common.netty.packet.NettyPacket;
import org.stonlexx.gamelibrary.common.netty.packet.typing.NettyPacketDirection;
import org.stonlexx.gamelibrary.utility.query.ResponseHandler;

import java.util.EnumMap;

public final class NettyPacketMapper<T> {

    @Getter
    private final EnumMap<NettyPacketDirection, BiMap<Class<? extends NettyPacket>, T>> packetMap
            = new EnumMap<NettyPacketDirection, BiMap<Class<? extends NettyPacket>, T>>(NettyPacketDirection.class) {{

        put(NettyPacketDirection.ONLY_DECODE, HashBiMap.create());
        put(NettyPacketDirection.ONLY_ENCODE, HashBiMap.create());
    }};


    /**
     * Зарегистрировать пакет в маппере
     *
     * @param nettyPacketDirection - директория пакета
     * @param nettyPacketClass - класс регистрируемого пакета
     * @param nettyPacketId - номер пакета для регистрации
     */
    public void registerPacket(@NonNull NettyPacketDirection nettyPacketDirection,
                               @NonNull Class<? extends NettyPacket> nettyPacketClass,

                               T nettyPacketId) {

        if (nettyPacketDirection == NettyPacketDirection.GLOBAL || nettyPacketDirection == NettyPacketDirection.CALLBACK) {

            registerPacket(NettyPacketDirection.ONLY_DECODE, nettyPacketClass, nettyPacketId);
            registerPacket(NettyPacketDirection.ONLY_ENCODE, nettyPacketClass, nettyPacketId);
            return;
        }

        packetMap.get(nettyPacketDirection).put(nettyPacketClass, nettyPacketId);
    }

    /**
     * Зарегистрировать пакет в маппере
     *
     * @param nettyPacketDirection - директория пакета
     * @param nettyPacketClass - класс регистрируемого пакета
     */
    public void registerPacket(@NonNull NettyPacketDirection nettyPacketDirection,
                               @NonNull Class<? extends NettyPacket> nettyPacketClass,

                               ResponseHandler<T, Class<? extends NettyPacket>> packetKeyHandler) {

        if (packetKeyHandler == null) {
            return;
        }

        T nettyPacketId = packetKeyHandler.handleResponse(nettyPacketClass);

        if (nettyPacketDirection == NettyPacketDirection.GLOBAL || nettyPacketDirection == NettyPacketDirection.CALLBACK) {

            registerPacket(NettyPacketDirection.ONLY_DECODE, nettyPacketClass, nettyPacketId);
            registerPacket(NettyPacketDirection.ONLY_ENCODE, nettyPacketClass, nettyPacketId);
            return;
        }

        packetMap.get(nettyPacketDirection).put(nettyPacketClass, nettyPacketId);
    }

    /**
     * Получить зарегистрированный пакет по его номеру
     *
     * @param nettyPacketDirection - директория пакета
     * @param nettyPacketId - номер получаемого пакета
     */
    public NettyPacket getNettyPacket(@NonNull NettyPacketDirection nettyPacketDirection, T nettyPacketId) {
        Class<? extends NettyPacket> packetClass = packetMap.get(nettyPacketDirection).inverse().get(nettyPacketId);

        if (packetClass == null) {
            throw new NettyException(String.format("NettyPacket(id:%s) is not registered", nettyPacketId));
        }

        try {

            return packetClass.getConstructor().newInstance();
        }

        catch (Throwable exception) {
            exception.printStackTrace();
        }

        return null;
    }

    /**
     * Получить общее количество зарегистрированных пакетов
     */
    public int getRegisteredPacketsCount() {
        return packetMap.get(NettyPacketDirection.ONLY_DECODE).size()
                + packetMap.get(NettyPacketDirection.ONLY_ENCODE).size();
    }

    /**
     * Вернуть булевое выражение, говорящее
     * о том, зарегистрирован ли указанный
     * класс пакета
     *
     * @param nettyPacketClass - класс пакета
     */
    public boolean hasNettyPacket(NettyPacketDirection nettyPacketDirection, Class<? extends NettyPacket> nettyPacketClass) {
        return packetMap.get(nettyPacketDirection).containsKey(nettyPacketClass);
    }

    /**
     * Вернуть булевое выражение, говорящее
     * о том, зарегистрирован ли указанный
     * номер пакета
     *
     * @param nettyPacketId - номер пакета
     */
    public boolean hasNettyPacketById(NettyPacketDirection nettyPacketDirection, T nettyPacketId) {
        return packetMap.get(nettyPacketDirection).containsValue(nettyPacketId);
    }

}
