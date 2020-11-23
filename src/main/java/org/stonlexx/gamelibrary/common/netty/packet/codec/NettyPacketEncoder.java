package org.stonlexx.gamelibrary.common.netty.packet.codec;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.NonNull;
import org.stonlexx.gamelibrary.GameLibrary;
import org.stonlexx.gamelibrary.common.netty.NettyManager;
import org.stonlexx.gamelibrary.common.netty.packet.NettyPacket;
import org.stonlexx.gamelibrary.common.netty.packet.buf.NettyPacketBuffer;
import org.stonlexx.gamelibrary.common.netty.packet.callback.NettyPacketCallbackHandler;
import org.stonlexx.gamelibrary.common.netty.packet.typing.NettyPacketDirection;
import org.stonlexx.gamelibrary.utility.JsonUtil;

import java.util.concurrent.TimeUnit;

public class NettyPacketEncoder
        extends MessageToByteEncoder<NettyPacket> {

    public static final Cache<Object, NettyPacketCallbackHandler> CALLBACK_NETTY_PACKET_CACHE = CacheBuilder.newBuilder()
            .expireAfterWrite(1, TimeUnit.SECONDS)
            .build();

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, NettyPacket nettyPacket, ByteBuf byteBuf) {
        NettyManager nettyManager = GameLibrary.getInstance().getNettyManager();
        NettyPacketBuffer nettyPacketBuffer = new NettyPacketBuffer(byteBuf);

        Object nettyPacketId = nettyManager.getNettyPacketId(NettyPacketDirection.ONLY_ENCODE, nettyPacket.getClass());
        if (nettyPacketId == null) {
            throw new NullPointerException("Packet " + nettyPacket.getClass().getSimpleName() + " is not registered!");
        }

        nettyPacketBuffer.writeString(nettyPacketId.getClass().getName());
        nettyPacketBuffer.writeString(JsonUtil.toJson(nettyPacketId));

        sendCallback(nettyPacketId, nettyPacketBuffer);
        nettyPacket.writePacket(nettyPacketBuffer);
    }

    /**
     * Проверить и установить разрешение на callback
     *
     * @param nettyPacketId - номер callback пакета
     * @param nettyPacketBuffer - обработчик байтов пакета
     */
    protected void sendCallback(@NonNull Object nettyPacketId,
                                 @NonNull NettyPacketBuffer nettyPacketBuffer) {

        NettyPacketCallbackHandler<?> nettyPacketCallbackHandler = CALLBACK_NETTY_PACKET_CACHE.asMap().get(nettyPacketId);
        boolean hasCallback = nettyPacketCallbackHandler != null;

        if (hasCallback) {
            nettyPacketCallbackHandler.waitCallbackResponse();
        }

        nettyPacketBuffer.writeBoolean(hasCallback);
    }

}
