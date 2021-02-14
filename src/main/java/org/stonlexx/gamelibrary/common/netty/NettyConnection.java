package org.stonlexx.gamelibrary.common.netty;

import io.netty.channel.Channel;
import lombok.*;
import org.stonlexx.gamelibrary.GameLibrary;
import org.stonlexx.gamelibrary.common.netty.packet.NettyPacket;
import org.stonlexx.gamelibrary.common.netty.packet.callback.NettyPacketCallbackHandler;
import org.stonlexx.gamelibrary.common.netty.packet.codec.NettyPacketEncoder;
import org.stonlexx.gamelibrary.common.netty.packet.typing.NettyPacketDirection;

import java.net.InetSocketAddress;

@AllArgsConstructor
@Getter
public class NettyConnection {

    private final InetSocketAddress inetSocketAddress;

    @Setter
    private Channel channel;


    /**
     * Отправить пакет на канал подключения
     *
     * @param nettyPacket - пакет
     */
    public void sendPacket(@NonNull NettyPacket nettyPacket) {
        if (channel == null || !channel.isActive() || !channel.isOpen() || !channel.isWritable() || !channel.isRegistered()) {
            return;
        }

        channel.writeAndFlush(nettyPacket);
    }

    /**
     * Отправить пакет на канал подключения
     * с ожиданием callback response
     *
     * @param nettyPacket - пакет
     * @param nettyPacketCallbackHandler - обработчик callback response
     */
    public <P extends NettyPacket> void sendPacket(@NonNull P nettyPacket, @NonNull NettyPacketCallbackHandler<P> nettyPacketCallbackHandler) {
        try {
            Object nettyPacketId = GameLibrary.getInstance().getNettyManager().getNettyPacketId(NettyPacketDirection.ONLY_ENCODE, nettyPacket.getClass());
            NettyPacketEncoder.CALLBACK_NETTY_PACKET_CACHE.put(nettyPacketId, nettyPacketCallbackHandler);

            sendPacket(nettyPacket);
        }
        catch (Exception exception) {
            nettyPacketCallbackHandler.onException(exception);
        }
    }
}
