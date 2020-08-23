package org.stonlexx.gamelibrary.core.netty.packet.typing;

import lombok.*;
import org.stonlexx.gamelibrary.core.netty.packet.NettyPacket;
import org.stonlexx.gamelibrary.core.netty.packet.mapping.NettyPacketMapper;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class NettyPacketTyping {

    private final String typingName;

    private final NettyPacketMapper packetMapper = new NettyPacketMapper();


// ================================================================================================================== //

    @Getter
    private static final Map<String, NettyPacketTyping> packetTypingMap = new HashMap<>();

    public static NettyPacketTyping getPacketTyping(String typingName) {
        return packetTypingMap.computeIfAbsent(typingName.toLowerCase(), NettyPacketTyping::new);
    }

// ================================================================================================================== //


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

        packetMapper.registerPacket(nettyPacketDirection, nettyPacketClass, packetId);
    }

    /**
     * Получить зарегистрированный пакет по его номеру
     *
     * @param nettyPacketDirection - директория пакета
     * @param packetId - номер получаемого пакета
     */
    public NettyPacket getNettyPacket(@NonNull NettyPacketDirection nettyPacketDirection, int packetId) {
        return packetMapper.getNettyPacket(nettyPacketDirection, packetId);
    }
}
