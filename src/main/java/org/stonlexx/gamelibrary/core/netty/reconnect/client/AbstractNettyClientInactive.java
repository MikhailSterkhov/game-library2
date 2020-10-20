package org.stonlexx.gamelibrary.core.netty.reconnect.client;

import io.netty.channel.Channel;
import lombok.NonNull;

public abstract class AbstractNettyClientInactive {

    /**
     * Что происходит при отключении
     * клиенского бутстрапа от сервера
     *
     * @param clientChannel - канал клиента, который отключился
     */
    public abstract void onClientInactive(@NonNull Channel clientChannel);
}
