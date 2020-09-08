package org.stonlexx.gamelibrary.core.netty.packet.mapping;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import lombok.Getter;
import lombok.NonNull;
import org.stonlexx.gamelibrary.core.netty.exception.NettyException;
import org.stonlexx.gamelibrary.core.netty.packet.NettyPacket;
import org.stonlexx.gamelibrary.core.netty.packet.typing.NettyPacketDirection;

import java.util.EnumMap;

public final class NettyPacketMapper {

    @Getter
    private final EnumMap<NettyPacketDirection, BiMap<Class<? extends NettyPacket>, Integer>> packetMap
            = new EnumMap<NettyPacketDirection, BiMap<Class<? extends NettyPacket>, Integer>>(NettyPacketDirection.class) {{

        put(NettyPacketDirection.TO_CLIENT, HashBiMap.create());
        put(NettyPacketDirection.TO_SERVER, HashBiMap.create());
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

                               int nettyPacketId) {

        if (nettyPacketDirection == NettyPacketDirection.TO_ALL) {

            registerPacket(NettyPacketDirection.TO_SERVER, nettyPacketClass, nettyPacketId);
            registerPacket(NettyPacketDirection.TO_CLIENT, nettyPacketClass, nettyPacketId);
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
    public NettyPacket getNettyPacket(@NonNull NettyPacketDirection nettyPacketDirection, int nettyPacketId) {
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
        return packetMap.get(NettyPacketDirection.TO_SERVER).size() + packetMap.get(NettyPacketDirection.TO_CLIENT).size();
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
    public boolean hasNettyPacketById(NettyPacketDirection nettyPacketDirection, int nettyPacketId) {
        return packetMap.get(nettyPacketDirection).containsValue(nettyPacketId);
    }

}
