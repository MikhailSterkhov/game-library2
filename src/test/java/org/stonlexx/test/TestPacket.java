package org.stonlexx.test;

import io.netty.channel.Channel;
import org.stonlexx.gamelibrary.core.netty.packet.AbstractNettyPacket;
import org.stonlexx.gamelibrary.core.netty.packet.buf.NettyPacketBuffer;
import org.stonlexx.gamelibrary.utility.location.PointLocation;
import org.stonlexx.test.bean.TestPlayer;

public class TestPacket extends AbstractNettyPacket {

    @Override
    public void writePacket(NettyPacketBuffer packetBuffer) {
        packetBuffer.writeString("tEst l1ne");
    }

    @Override
    public void readPacket(NettyPacketBuffer packetBuffer) {
        packetBuffer.readString(); // Response: "tEst l1ne"
    }

    @Override
    public void handle(Channel channel) {
        // u can get any packet data value

        PointLocation pointLocation = packetHandleData.getHandleDataObject(PointLocation.class, "location");
        TestPlayer testPlayer = packetHandleData.getHandleDataObject(TestPlayer.class, "player");
    }

}
