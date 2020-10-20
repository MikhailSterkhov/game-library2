package org.stonlexx.gamelibrary.core.netty.reconnect.server;

import io.netty.channel.Channel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class AbstractNettyReconnect {

    private Channel channel;
    private Throwable exception;
    private EnumReconnectReason reason;

    private Runnable connectRunnable;


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
