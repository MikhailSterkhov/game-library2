package org.stonlexx.gamelibrary.core.netty.packet.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.stonlexx.gamelibrary.GameLibrary;
import org.stonlexx.gamelibrary.core.netty.NettyManager;
import org.stonlexx.gamelibrary.core.netty.packet.NettyPacket;
import org.stonlexx.gamelibrary.core.netty.packet.NettyPacketHandleData;
import org.stonlexx.gamelibrary.core.netty.packet.buf.NettyPacketBuffer;
import org.stonlexx.gamelibrary.core.netty.packet.callback.NettyPacketCallbackHandler;
import org.stonlexx.gamelibrary.core.netty.packet.callback.impl.EmptyPacketCallbackHandler;
import org.stonlexx.gamelibrary.core.netty.packet.impl.AbstractNettyPacket;
import org.stonlexx.gamelibrary.core.netty.packet.typing.NettyPacketDirection;
import org.stonlexx.gamelibrary.core.netty.packet.typing.NettyPacketTyping;
import org.stonlexx.gamelibrary.utility.JsonUtil;

import java.util.List;

public class NettyPacketDecoder
        extends ByteToMessageDecoder {

    @Override
    @SneakyThrows
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> handleList) {
        NettyPacketBuffer nettyPacketBuffer = new NettyPacketBuffer(byteBuf);
        Channel channel = channelHandlerContext.channel();

        String packetIdClassName = nettyPacketBuffer.readString();
        String packetIdJson = nettyPacketBuffer.readString();

        Class<?> packetIdClass = Class.forName(packetIdClassName);
        Object nettyPacketId = JsonUtil.fromJson(packetIdJson, packetIdClass);

        NettyManager nettyManager = GameLibrary.getInstance().getNettyManager();

        NettyPacketTyping nettyPacketTyping
                = nettyManager.findTypingByNettyPacket(NettyPacketDirection.ONLY_DECODE, nettyPacketId);

        NettyPacket nettyPacket = nettyPacketTyping.getNettyPacket(NettyPacketDirection.ONLY_DECODE, nettyPacketId);
        boolean hasCallback = nettyPacketBuffer.readBoolean();

        if (nettyPacket == null) {
            return;
        }

        nettyPacket.readPacket(nettyPacketBuffer);

        try {
            if (nettyPacket instanceof AbstractNettyPacket) {
                AbstractNettyPacket abstractNettyPacket = ((AbstractNettyPacket) nettyPacket);
                NettyPacketHandleData nettyPacketHandleData = readHandleData(nettyPacketBuffer);

                abstractNettyPacket.setHandleData(nettyPacketHandleData);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }

        handleList.add(nettyPacket);


        if (hasCallback) {
            NettyPacketEncoder.CALLBACK_NETTY_PACKET_CACHE.cleanUp();
            NettyPacketCallbackHandler nettyPacketCallbackHandler = NettyPacketEncoder.CALLBACK_NETTY_PACKET_CACHE.asMap().get(nettyPacketId);

            if (nettyPacketCallbackHandler != null && nettyPacketCallbackHandler.isWaitingResponse()) {
                nettyPacketCallbackHandler.handleCallback(nettyPacket);
                return;
            }

            NettyPacketEncoder.CALLBACK_NETTY_PACKET_CACHE.put(nettyPacketId, new EmptyPacketCallbackHandler());
            channel.writeAndFlush(nettyPacket);
        }
    }

    /**
     * Прочитать и создать хранилище важных
     * данных для обработки пакета
     *
     * @param nettyPacketBuffer - хранилище байтов
     */
    protected NettyPacketHandleData readHandleData(@NonNull NettyPacketBuffer nettyPacketBuffer)
            throws Exception {

        NettyPacketHandleData nettyPacketHandleData = NettyPacketHandleData.create();

        while (nettyPacketBuffer.readBoolean()) {
            String handleDataName = nettyPacketBuffer.readString();
            String handleDataJson = nettyPacketBuffer.readString();

            String objectClassName = nettyPacketBuffer.readString();

            nettyPacketHandleData.addHandleData(handleDataName, JsonUtil.fromJson(handleDataJson, Class.forName(objectClassName)));
        }

        return nettyPacketHandleData;
    }

}
