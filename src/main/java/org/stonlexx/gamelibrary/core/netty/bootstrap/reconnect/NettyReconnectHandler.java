package org.stonlexx.gamelibrary.core.netty.bootstrap.reconnect;

import io.netty.channel.Channel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class NettyReconnectHandler {

    private Channel channel;
    private Throwable exception;
    private EnumReconnectReason reason;


    // Обработчик переподключения клиента
    // к серверу
    public abstract void handleReconnect();
}
