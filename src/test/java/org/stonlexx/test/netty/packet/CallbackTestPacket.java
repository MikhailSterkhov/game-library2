package org.stonlexx.test.netty.packet;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.stonlexx.gamelibrary.common.netty.packet.buf.NettyPacketBuffer;
import org.stonlexx.gamelibrary.common.netty.packet.impl.AbstractNettyPacket;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CallbackTestPacket extends AbstractNettyPacket {

    private String string;


    @Override
    public void writePacket(@NonNull NettyPacketBuffer nettyPacketBuffer) {
        nettyPacketBuffer.writeString(string);
    }

    @Override
    public void readPacket(@NonNull NettyPacketBuffer nettyPacketBuffer) {
        this.string = nettyPacketBuffer.readString();
    }

}
