package org.stonlexx.gamelibrary.core.netty.packet.impl;

import io.netty.channel.Channel;
import lombok.Getter;
import org.stonlexx.gamelibrary.GameLibrary;
import org.stonlexx.gamelibrary.core.event.impl.PacketHandleEvent;
import org.stonlexx.gamelibrary.core.netty.packet.NettyPacket;
import org.stonlexx.gamelibrary.core.netty.packet.buf.NettyPacketBuffer;

@Getter
public abstract class AbstractNettyPacket implements NettyPacket {


    @Override
    public void writePacket(NettyPacketBuffer packetBuffer) {
        // override me...
    }

    @Override
    public void readPacket(NettyPacketBuffer packetBuffer) {
        // override me...
    }

    @Override
    public void handle(Channel channel) {
        // override me...
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
