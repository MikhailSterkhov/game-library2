package org.stonlexx.gamelibrary.core.netty.packet.typing;

import lombok.*;
import org.stonlexx.gamelibrary.core.netty.packet.NettyPacket;
import org.stonlexx.gamelibrary.core.netty.packet.mapping.NettyPacketMapper;
import org.stonlexx.gamelibrary.utility.query.ResponseHandler;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class NettyPacketTyping<T> {

    private final String typingName;
    private final NettyPacketMapper<T> packetMapper = new NettyPacketMapper<T>();

    @Setter
    private ResponseHandler<T, Class<? extends NettyPacket>> packetKeyHandler;


// ================================================================================================================== //

    @Getter
    private static final Map<String, NettyPacketTyping<?>> packetTypingMap = new HashMap<>();

    public static <T> NettyPacketTyping<T> getPacketTyping(@NonNull Class<T> packetKeyClass,
                                                           @NonNull String typingName) {

        return getPacketTyping(packetKeyClass, typingName, null);
    }

    public static <T> NettyPacketTyping<T> getPacketTyping(@NonNull Class<T> packetKeyClass,
                                                           @NonNull String typingName,

                                                           ResponseHandler<T, Class<? extends NettyPacket>> packetKeyHandler) {

        NettyPacketTyping<T> nettyPacketTyping = (NettyPacketTyping<T>) packetTypingMap.computeIfAbsent(typingName.toLowerCase(), NettyPacketTyping::new);
        nettyPacketTyping.packetKeyHandler = packetKeyHandler;

        return nettyPacketTyping;
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

                               T packetId) {

        packetMapper.registerPacket(nettyPacketDirection, nettyPacketClass, packetId);
    }

    /**
     * Зарегистрировать пакет в маппере
     *
     * @param nettyPacketDirection - директория пакета
     * @param nettyPacketClass - класс регистрируемого пакета
     */
    public void registerPacket(@NonNull NettyPacketDirection nettyPacketDirection,
                               @NonNull Class<? extends NettyPacket> nettyPacketClass) {

        packetMapper.registerPacket(nettyPacketDirection, nettyPacketClass, packetKeyHandler);
    }

    /**
     * Получить зарегистрированный пакет по его номеру
     *
     * @param nettyPacketDirection - директория пакета
     * @param packetId - номер получаемого пакета
     */
    public NettyPacket getNettyPacket(@NonNull NettyPacketDirection nettyPacketDirection, T packetId) {
        return packetMapper.getNettyPacket(nettyPacketDirection, packetId);
    }
}
