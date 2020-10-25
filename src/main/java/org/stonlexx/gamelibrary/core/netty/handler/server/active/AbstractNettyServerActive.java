package org.stonlexx.gamelibrary.core.netty.handler.server.active;

import io.netty.channel.Channel;
import lombok.NonNull;

public abstract class AbstractNettyServerActive {

    /**
     * Что происходит при подключении
     * клиенского бутстрапа к серверу
     *
     * @param serverChannel - канал сервера, который подключился
     */
    public abstract void onServerActive(@NonNull Channel serverChannel);

}
