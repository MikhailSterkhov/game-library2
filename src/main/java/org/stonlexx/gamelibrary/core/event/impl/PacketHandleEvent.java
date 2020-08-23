package org.stonlexx.gamelibrary.core.event.impl;

import io.netty.channel.Channel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.stonlexx.gamelibrary.core.event.CoreEvent;
import org.stonlexx.gamelibrary.core.netty.packet.NettyPacket;

@RequiredArgsConstructor
@Getter
public class PacketHandleEvent extends CoreEvent {

    private final Channel channel;
    private final NettyPacket nettyPacket;
}
