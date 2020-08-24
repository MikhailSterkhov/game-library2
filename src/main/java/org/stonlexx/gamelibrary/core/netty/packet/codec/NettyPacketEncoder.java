package org.stonlexx.gamelibrary.core.netty.packet.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.NonNull;
import org.stonlexx.gamelibrary.GameLibrary;
import org.stonlexx.gamelibrary.core.netty.NettyManager;
import org.stonlexx.gamelibrary.core.netty.packet.NettyPacket;
import org.stonlexx.gamelibrary.core.netty.packet.NettyPacketHandleData;
import org.stonlexx.gamelibrary.core.netty.packet.buf.NettyPacketBuffer;
import org.stonlexx.gamelibrary.utility.JsonUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.Map;

public abstract class NettyPacketEncoder
        extends MessageToByteEncoder<NettyPacket> {


    /**
     * Чтение пришедшего пакета от клиента
     *
     * @param channel - канал, с которого пришел пакет
     * @param nettyPacketBuffer - обработчик байтов
     * @param nettyPacket - пакет
     * @param nettyPacketId - номер пакета
     */
    public abstract void encode(@NonNull Channel channel,
                                @NonNull NettyPacketBuffer nettyPacketBuffer,

                                @NonNull NettyPacket nettyPacket, int nettyPacketId)
            throws IOException;

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, NettyPacket nettyPacket, ByteBuf byteBuf) throws Exception {
        Channel channel = channelHandlerContext.channel();

        NettyManager nettyManager = GameLibrary.getInstance().getNettyManager();
        NettyPacketBuffer nettyPacketBuffer = new NettyPacketBuffer(byteBuf);

        int nettyPacketId = nettyManager.getNettyPacketId(nettyManager.getPacketCodecManager().getEncodePacketDirection(), nettyPacket.getClass());

        nettyPacketBuffer.writeVarInt(nettyPacketId);

        encode(channel, nettyPacketBuffer, nettyPacket, nettyPacketId);
    }

    /**
     * Записать хранилище важных в хранилище байтов
     * и отправить на обработку
     *
     * @param nettyPacketBuffer - хранилище байтов
     */
    protected void writeHandleData(@NonNull NettyPacketHandleData nettyPacketHandleData,
                                   @NonNull NettyPacketBuffer nettyPacketBuffer) {

        Map<String, WeakReference<?>> handleDataMap = nettyPacketHandleData.getHandleDataMap();

        handleDataMap.forEach((handleDataName, handleDataValue) -> {
            Object handleDataValueObject = handleDataValue.get();

            if (handleDataValueObject == null) {
                return;
            }

            nettyPacketBuffer.writeBoolean(true);

            nettyPacketBuffer.writeString(handleDataName);
            nettyPacketBuffer.writeString(JsonUtil.toJson(handleDataValueObject));

            nettyPacketBuffer.writeString(handleDataValueObject.getClass().getName());
        });

        nettyPacketBuffer.writeBoolean(false);
    }

}
