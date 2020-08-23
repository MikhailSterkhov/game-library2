package org.stonlexx.gamelibrary.core.netty.packet.mapping;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import lombok.Getter;
import lombok.NonNull;
import org.stonlexx.gamelibrary.core.netty.packet.NettyPacket;
import org.stonlexx.gamelibrary.core.netty.packet.typing.NettyPacketDirection;

import java.lang.invoke.*;
import java.util.HashMap;
import java.util.Map;

public class NettyPacketMapper {

    @Getter
    private final Map<NettyPacketDirection, BiMap<Integer, Class<? extends NettyPacket>>> packetMap = new HashMap<>();

    @Getter
    private final MethodHandles.Lookup lookup = MethodHandles.publicLookup();


    /**
     * Зарегистрировать пакет в маппере
     *
     * @param nettyPacketDirection - директория пакета
     * @param nettyPacketClass - класс регистрируемого пакета
     * @param packetId - номер пакета для регистрации
     */
    public void registerPacket(@NonNull NettyPacketDirection nettyPacketDirection,
                               @NonNull Class<? extends NettyPacket> nettyPacketClass,

                               int packetId) {

        BiMap<Integer, Class<? extends NettyPacket>> packetBiMap = packetMap.getOrDefault(nettyPacketDirection, HashBiMap.create());
        packetBiMap.put(packetId, nettyPacketClass);

        packetMap.put(nettyPacketDirection, packetBiMap);
    }

    /**
     * Получить зарегистрированный пакет по его номеру
     *
     * @param nettyPacketDirection - директория пакета
     * @param packetId - номер получаемого пакета
     */
    public NettyPacket getNettyPacket(@NonNull NettyPacketDirection nettyPacketDirection, int packetId) {
        Class<? extends NettyPacket> packetClass = packetMap.getOrDefault(nettyPacketDirection, HashBiMap.create()).get(packetId);

        if (packetClass == null) {
            throw new NullPointerException(String.format("Packet(%s) is not registered", packetId));
        }

        try {

            MethodType methodType = MethodType.methodType(Object.class);
            MethodType constructorType = MethodType.methodType(Void.TYPE);

            MethodHandle methodHandle = lookup.findConstructor(packetClass, constructorType);

            CallSite callSite = LambdaMetafactory.metafactory(lookup,
                    "get", methodType, constructorType, methodHandle, MethodType.methodType(packetClass));

            return (NettyPacket) callSite.getTarget().invoke();
        }

        catch (Throwable exception) {
            exception.printStackTrace();
        }

        return null;
    }

}
