package org.stonlexx.gamelibrary.core.netty.handler.server.reconnect;

import io.netty.channel.Channel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class AbstractNettyReconnect {

    protected Channel channel;
    protected Throwable exception;
    protected EnumReconnectReason reason;

    protected Runnable connectRunnable;


    /**
     * Что будет происходит при попытке
     * переподключиться к серверу
     */
    public abstract void onReconnect();


    /**
     * Обработка переподключения к серверу
     */
    public void handleReconnect() {
        onReconnect();
        connectRunnable.run();
    }

}
