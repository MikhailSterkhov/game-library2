package org.stonlexx.gamelibrary.core.netty.packet.codec.impl;

import io.netty.channel.Channel;
import lombok.NonNull;
import org.stonlexx.gamelibrary.core.netty.packet.AbstractNettyPacket;
import org.stonlexx.gamelibrary.core.netty.packet.NettyPacket;
import org.stonlexx.gamelibrary.core.netty.packet.NettyPacketHandleData;
import org.stonlexx.gamelibrary.core.netty.packet.buf.NettyPacketBuffer;
import org.stonlexx.gamelibrary.core.netty.packet.codec.NettyPacketDecoder;

public class StandardNettyPacketDecoder extends NettyPacketDecoder {

    @Override
    public void decode(@NonNull Channel channel,
                       @NonNull NettyPacketBuffer nettyPacketBuffer,

                       @NonNull NettyPacket nettyPacket, int nettyPacketId) {

        nettyPacket.readPacket(nettyPacketBuffer);

        try {
            if (nettyPacket instanceof AbstractNettyPacket) {
                AbstractNettyPacket abstractNettyPacket = ((AbstractNettyPacket) nettyPacket);
                NettyPacketHandleData nettyPacketHandleData = readHandleData(nettyPacketBuffer);

                abstractNettyPacket.setPacketHandleData(nettyPacketHandleData);
            }
        }

        catch (Exception exception) {
            exception.printStackTrace();
        }
    }

}
