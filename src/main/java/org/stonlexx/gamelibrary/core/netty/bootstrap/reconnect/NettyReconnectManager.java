package org.stonlexx.gamelibrary.core.netty.bootstrap.reconnect;

import io.netty.channel.Channel;
import lombok.Getter;
import lombok.NonNull;
import org.stonlexx.gamelibrary.core.netty.packet.handler.NettyServerReconnectHandler;

import java.util.concurrent.ConcurrentHashMap;

public final class NettyReconnectManager {

    @Getter
    private final ConcurrentHashMap<Channel, NettyReconnectHandler> reconnectChannelCollection
            = new ConcurrentHashMap<>();


    /**
     * Добавить канал Netty клиента в список тех, что
     * способны переподключаться к серверу, в случае
     * непредвиденного отключения от него
     *
     * @param channel - канал
     */
    public void addReconnectToChannel(@NonNull Channel channel,
                                      @NonNull NettyReconnectHandler nettyReconnectHandler) {

        channel.pipeline().addLast("reconnect-handler", new NettyServerReconnectHandler(nettyReconnectHandler));
    }

}
