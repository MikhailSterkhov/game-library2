package org.stonlexx.gamelibrary.core.netty.packet.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.NonNull;
import org.stonlexx.gamelibrary.GameLibrary;
import org.stonlexx.gamelibrary.core.netty.NettyManager;
import org.stonlexx.gamelibrary.core.netty.packet.AbstractNettyPacket;
import org.stonlexx.gamelibrary.core.netty.packet.NettyPacket;
import org.stonlexx.gamelibrary.core.netty.packet.NettyPacketHandleData;
import org.stonlexx.gamelibrary.core.netty.packet.buf.NettyPacketBuffer;
import org.stonlexx.gamelibrary.core.netty.packet.mapping.NettyPacketMapper;
import org.stonlexx.gamelibrary.core.netty.packet.typing.NettyPacketTyping;
import org.stonlexx.gamelibrary.utility.JsonUtil;

import java.util.ArrayList;
import java.util.List;

public class NettyPacketDecoder
        extends ByteToMessageDecoder {

    /**
     * Чтение пришедшего пакета от клиента
     *
     * @param channel - канал, с которого пришел пакет
     * @param nettyPacketBuffer - обработчик байтов
     * @param nettyPacket - пакет
     * @param nettyPacketId - номер пакета
     */
    public void decode(@NonNull Channel channel,
                       @NonNull NettyPacketBuffer nettyPacketBuffer,

                       @NonNull NettyPacket nettyPacket,
                       @NonNull Object nettyPacketId) {

        nettyPacket.readPacket(nettyPacketBuffer);

        try {
            if (nettyPacket instanceof AbstractNettyPacket) {
                AbstractNettyPacket abstractNettyPacket = ((AbstractNettyPacket) nettyPacket);
                NettyPacketHandleData nettyPacketHandleData = readHandleData(nettyPacketBuffer);

                abstractNettyPacket.setPacketHandleData(nettyPacketHandleData);
            }
        }

        catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> handleList) throws Exception {
        NettyPacketBuffer nettyPacketBuffer = new NettyPacketBuffer(byteBuf);

        Channel channel = channelHandlerContext.channel();

        String packetIdClassName = nettyPacketBuffer.readString();
        String packetIdJson = nettyPacketBuffer.readString();

        Class<?> packetIdClass = Class.forName(packetIdClassName);
        Object nettyPacketId = JsonUtil.fromJson(packetIdJson, packetIdClass);

        NettyManager nettyManager = GameLibrary.getInstance().getNettyManager();
        NettyPacketTyping nettyPacketTyping = nettyManager.findTypingByNettyPacket(nettyManager.getPacketCodecManager().getDecodePacketDirection(), nettyPacketId);

        NettyPacket nettyPacket = nettyPacketTyping.getNettyPacket(nettyManager.getPacketCodecManager().getDecodePacketDirection(), nettyPacketId);

        decode(channel, nettyPacketBuffer, nettyPacket, nettyPacketId);
        handleList.add(nettyPacket);
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
