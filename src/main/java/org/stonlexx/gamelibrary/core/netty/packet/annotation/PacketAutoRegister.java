package org.stonlexx.gamelibrary.core.netty.packet.annotation;

import org.stonlexx.gamelibrary.core.netty.packet.typing.NettyPacketDirection;

public @interface PacketAutoRegister {

    String id();
    NettyPacketDirection direction();
}
