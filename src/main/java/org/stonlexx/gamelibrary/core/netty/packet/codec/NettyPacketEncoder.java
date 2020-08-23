package org.stonlexx.gamelibrary.core.netty.packet.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.NonNull;
import org.stonlexx.gamelibrary.GameLibrary;
import org.stonlexx.gamelibrary.core.netty.NettyManager;
import org.stonlexx.gamelibrary.core.netty.packet.NettyPacket;
import org.stonlexx.gamelibrary.core.netty.packet.NettyPacketHandleData;
import org.stonlexx.gamelibrary.core.netty.packet.buf.NettyPacketBuffer;
import org.stonlexx.gamelibrary.utility.JsonUtil;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Map;

public abstract class NettyPacketEncoder<NP extends NettyPacket>
        extends MessageToByteEncoder<NP> {


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

                                @NonNull NP nettyPacket, int nettyPacketId)
            throws IOException;

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, NP nettyPacket, ByteBuf byteBuf) throws Exception {
        Channel channel = channelHandlerContext.channel();

        NettyManager nettyManager = GameLibrary.getInstance().getLibraryCore().getNettyManager();
        NettyPacketBuffer nettyPacketBuffer = new NettyPacketBuffer(byteBuf);

        int nettyPacketId = nettyManager.getNettyPacketId(nettyManager.getPacketCodecManager().getEncodePacketDirection(), nettyPacket.getClass());

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

            nettyPacketBuffer.writeString(handleDataValueObject.getClass().getSimpleName());
        });

        nettyPacketBuffer.writeBoolean(false);
    }

}
