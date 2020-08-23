package org.stonlexx.gamelibrary.core.netty.packet.mapping;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import lombok.Getter;
import lombok.NonNull;
import org.stonlexx.gamelibrary.GameLibrary;
import org.stonlexx.gamelibrary.core.netty.packet.NettyPacket;
import org.stonlexx.gamelibrary.core.netty.packet.typing.NettyPacketDirection;

import java.lang.invoke.*;
import java.util.HashMap;
import java.util.Map;

public final class NettyPacketMapper {

    @Getter
    private final Map<NettyPacketDirection, BiMap<Integer, Class<? extends NettyPacket>>> packetMap = new HashMap<>();


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

        BiMap<Integer, Class<? extends NettyPacket>> packetBiMap = packetMap.getOrDefault(nettyPacketDirection, HashBiMap.create());
        packetBiMap.put(nettyPacketId, nettyPacketClass);

        packetMap.put(nettyPacketDirection, packetBiMap);
    }

    /**
     * Получить зарегистрированный пакет по его номеру
     *
     * @param nettyPacketDirection - директория пакета
     * @param nettyPacketId - номер получаемого пакета
     */
    public NettyPacket getNettyPacket(@NonNull NettyPacketDirection nettyPacketDirection, int nettyPacketId) {
        Class<? extends NettyPacket> packetClass = packetMap.getOrDefault(nettyPacketDirection, HashBiMap.create()).get(nettyPacketId);

        if (packetClass == null) {
            throw new NullPointerException(String.format("Packet(%s) is not registered", nettyPacketId));
        }

        try {

            MethodType methodType = MethodType.methodType(Object.class);
            MethodType constructorType = MethodType.methodType(Void.TYPE);

            MethodHandles.Lookup publicLookup = GameLibrary.getInstance().getLibraryCore().getPublicLookup();
            MethodHandle methodHandle = publicLookup.findConstructor(packetClass, constructorType);

            CallSite callSite = LambdaMetafactory.metafactory(publicLookup,
                    "get", methodType, constructorType, methodHandle, MethodType.methodType(packetClass));

            return (NettyPacket) callSite.getTarget().invoke();
        }

        catch (Throwable exception) {
            exception.printStackTrace();
        }

        return null;
    }

    /**
     * Вернуть булевое выражение, говорящее
     * о том, зарегистрирован ли указанный
     * класс пакета
     *
     * @param nettyPacketClass - класс пакета
     */
    public boolean hasNettyPacket(NettyPacketDirection nettyPacketDirection, Class<? extends NettyPacket> nettyPacketClass) {
        return packetMap.get(nettyPacketDirection).containsValue(nettyPacketClass);
    }

    /**
     * Вернуть булевое выражение, говорящее
     * о том, зарегистрирован ли указанный
     * номер пакета
     *
     * @param nettyPacketId - номер пакета
     */
    public boolean hasNettyPacketById(NettyPacketDirection nettyPacketDirection, int nettyPacketId) {
        return packetMap.get(nettyPacketDirection).containsKey(nettyPacketId);
    }

}
