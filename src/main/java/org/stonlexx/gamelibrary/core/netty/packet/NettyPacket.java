package org.stonlexx.gamelibrary.core.netty.packet;

import io.netty.channel.Channel;
import lombok.NonNull;
import org.stonlexx.gamelibrary.core.netty.packet.buf.NettyPacketBuffer;

public interface NettyPacket {

    /**
     * Записать данные в пакет перед его
     * отправкой на клиент
     *
     * @param packetBuffer - хранилище байтов
     */
    void writePacket(@NonNull NettyPacketBuffer packetBuffer);

    /**
     * Прочитать байты пакета перед его обработкой
     *
     * @param packetBuffer - хранилище байтов
     */
    void readPacket(@NonNull NettyPacketBuffer packetBuffer);

    /**
     * Обработать пакет
     *
     * @param channel - канал, с которого пришел пакет
     */
    void handle(@NonNull Channel channel);
}
