package org.stonlexx.gamelibrary.common.netty.packet.annotation;

import org.stonlexx.gamelibrary.common.netty.packet.typing.NettyPacketDirection;

public @interface PacketAutoRegister {

    String id();
    NettyPacketDirection direction();
}
