package org.stonlexx.gamelibrary.common.netty.handler.client.inactive;

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
