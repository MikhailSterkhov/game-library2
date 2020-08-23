package org.stonlexx.gamelibrary.core.netty.packet;

import io.netty.channel.Channel;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.stonlexx.gamelibrary.GameLibrary;
import org.stonlexx.gamelibrary.core.event.impl.PacketHandleEvent;
import org.stonlexx.gamelibrary.core.netty.packet.buf.NettyPacketBuffer;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class AbstractNettyPacket implements NettyPacket {

    @Getter
    protected final PacketHandleData packetHandleData = PacketHandleData.create();


    @Override
    public abstract void writePacket(NettyPacketBuffer packetBuffer);

    @Override
    public abstract void readPacket(NettyPacketBuffer packetBuffer);

    @Override
    public abstract void handle(Channel channel);


    /**
     * Вызвать ивент обработки пакета
     *
     * @param channel - канал, с которого пришел пакет
     */
    protected void handleEvent(Channel channel) {
        GameLibrary.getInstance().getLibraryCore().getEventManager().callEvent(new PacketHandleEvent(channel, this));
    }

}
