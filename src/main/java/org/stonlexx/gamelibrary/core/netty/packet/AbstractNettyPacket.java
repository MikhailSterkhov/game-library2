package org.stonlexx.gamelibrary.core.netty.packet;

import io.netty.channel.Channel;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.stonlexx.gamelibrary.GameLibrary;
import org.stonlexx.gamelibrary.core.event.impl.PacketHandleEvent;
import org.stonlexx.gamelibrary.core.netty.packet.buf.NettyPacketBuffer;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public abstract class AbstractNettyPacket implements NettyPacket {

    @Setter
    protected NettyPacketHandleData packetHandleData = NettyPacketHandleData.create();


    @Override
    public void writePacket(NettyPacketBuffer packetBuffer) {
    }

    @Override
    public void readPacket(NettyPacketBuffer packetBuffer) {
    }

    @Override
    public void handle(Channel channel) {
    }



    /**
     * Вызвать ивент обработки пакета
     *
     * @param channel - канал, с которого пришел пакет
     */
    protected void handleEvent(Channel channel) {
        GameLibrary.getInstance().getEventManager().callEvent(new PacketHandleEvent(channel, this));
    }

}
