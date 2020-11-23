package org.stonlexx.test.netty.packet;

import io.netty.channel.Channel;
import org.stonlexx.gamelibrary.GameLibrary;
import org.stonlexx.gamelibrary.common.netty.packet.buf.NettyPacketBuffer;
import org.stonlexx.gamelibrary.common.netty.packet.impl.AbstractNettyPacket;

public class IntTestPacket extends AbstractNettyPacket {

    @Override
    public void writePacket(NettyPacketBuffer packetBuffer) {
        packetBuffer.writeString("tEst l1ne");
    }

    @Override
    public void readPacket(NettyPacketBuffer packetBuffer) {
        String testLine = packetBuffer.readString();

        GameLibrary.getInstance().getLogger().info("Packet response: " + testLine);
    }

    @Override
    public void handle(Channel channel) {
        GameLibrary.getInstance().getLogger().info("Packet handled");
    }

}
