package org.stonlexx.gamelibrary.common.event.impl;

import io.netty.channel.Channel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.stonlexx.gamelibrary.common.event.Event;
import org.stonlexx.gamelibrary.common.netty.packet.NettyPacket;

@RequiredArgsConstructor
@Getter
public class PacketHandleEvent extends Event {

    private final Channel channel;
    private final NettyPacket nettyPacket;
}
