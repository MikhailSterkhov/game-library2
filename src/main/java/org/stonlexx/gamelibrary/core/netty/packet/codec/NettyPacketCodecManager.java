package org.stonlexx.gamelibrary.core.netty.packet.codec;

import lombok.Getter;
import lombok.Setter;
import org.stonlexx.gamelibrary.core.netty.packet.NettyPacketHandleData;
import org.stonlexx.gamelibrary.core.netty.packet.typing.NettyPacketDirection;
import org.stonlexx.gamelibrary.core.netty.packet.typing.NettyPacketTyping;

@Getter
public final class NettyPacketCodecManager {

    @Setter
    private NettyPacketDirection decodePacketDirection = NettyPacketDirection.TO_SERVER,
                                 encodePacketDirection = NettyPacketDirection.TO_CLIENT;

    private NettyPacketTyping<Integer> packetTypingByInteger = NettyPacketTyping.createPacketTyping(int.class, "default");
    private NettyPacketTyping<Long> packetTypingByLong = NettyPacketTyping.createPacketTyping(long.class, "default");
    private NettyPacketTyping<Float> packetTypingByFloat = NettyPacketTyping.createPacketTyping(float.class, "default");
    private NettyPacketTyping<String> packetTypingByString = NettyPacketTyping.createPacketTyping(String.class, "default");


    /**
     * для хранения необходимых данных, которые не круто
     * было бы выводить в отдельный manager или unit
     *
     * например, Handler, Version, и т.д.
     */
    private final NettyPacketHandleData nettyPacketHandleData = NettyPacketHandleData.create();

}
