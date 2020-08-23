package org.stonlexx.gamelibrary.core.netty.packet;

import io.netty.channel.Channel;
import org.stonlexx.gamelibrary.core.netty.packet.buf.NettyPacketBuffer;

public interface NettyPacket {

    /**
     * Записать данные в пакет перед его
     * отправкой на клиент
     *
     * @param packetBuffer - хранилище байтов
     */
    void writePacket(NettyPacketBuffer packetBuffer);

    /**
     * Прочитать байты пакета перед его обработкой
     *
     * @param packetBuffer - хранилище байтов
     */
    void readPacket(NettyPacketBuffer packetBuffer);


    /**
     * Обработать пакет
     *
     * @param channel - канал, с которого пришел пакет
     */
    void handle(Channel channel);
}
