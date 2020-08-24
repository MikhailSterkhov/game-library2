package org.stonlexx.test.netty;

import io.netty.channel.Channel;
import org.stonlexx.gamelibrary.GameLibrary;
import org.stonlexx.gamelibrary.core.netty.packet.AbstractNettyPacket;
import org.stonlexx.gamelibrary.core.netty.packet.buf.NettyPacketBuffer;
import org.stonlexx.gamelibrary.utility.location.PointLocation;

public class TestPacket extends AbstractNettyPacket {

    @Override
    public void writePacket(NettyPacketBuffer packetBuffer) {
        packetBuffer.writeString("tEst l1ne");

        packetHandleData.addHandleData("location", new PointLocation(2.1, 2.2, 8));
    }

    @Override
    public void readPacket(NettyPacketBuffer packetBuffer) {
        String testLine = packetBuffer.readString();

        GameLibrary.getInstance().getLogger().info("Packet response: " + testLine);
    }

    @Override
    public void handle(Channel channel) {
        PointLocation pointLocation = packetHandleData.getHandleDataObject(PointLocation.class, "location");

        GameLibrary.getInstance().getLogger().info("Packet handle data value: " + pointLocation.toString());
    }

}
