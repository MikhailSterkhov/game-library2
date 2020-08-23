package org.stonlexx.gamelibrary.core.netty.packet.codec.impl;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import io.netty.channel.Channel;
import lombok.NonNull;
import org.stonlexx.gamelibrary.core.netty.packet.AbstractNettyPacket;
import org.stonlexx.gamelibrary.core.netty.packet.NettyPacket;
import org.stonlexx.gamelibrary.core.netty.packet.buf.NettyPacketBuffer;
import org.stonlexx.gamelibrary.core.netty.packet.codec.NettyPacketEncoder;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;

public class StandardNettyPacketEncoder extends NettyPacketEncoder {

    @Override
    public void encode(@NonNull Channel channel,
                       @NonNull NettyPacketBuffer nettyPacketBuffer,

                       @NonNull NettyPacket nettyPacket, int nettyPacketId) {

        nettyPacket.writePacket(nettyPacketBuffer);

        if (nettyPacket instanceof AbstractNettyPacket) {
            AbstractNettyPacket abstractNettyPacket = ((AbstractNettyPacket) nettyPacket);

            writeHandleData(abstractNettyPacket.getPacketHandleData(), nettyPacketBuffer);
        }
    }
}
