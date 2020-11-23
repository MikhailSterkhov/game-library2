package org.stonlexx.gamelibrary.common.netty.handler.client.active;

import io.netty.channel.Channel;
import lombok.NonNull;

public abstract class AbstractNettyClientActive {

    /**
     * Что происходит при подключении
     * клиенского бутстрапа к серверу
     *
     * @param clientChannel - канал клиента, который подключился
     */
    public abstract void onClientActive(@NonNull Channel clientChannel);
}
