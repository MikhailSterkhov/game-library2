package org.stonlexx.gamelibrary.core.netty.packet.callback;

import lombok.Getter;
import lombok.NonNull;

public abstract class NettyPacketCallbackHandler<P> {

    @Getter
    private boolean waitingResponse;


    /**
     * Обработать пришедший callback
     *
     * @param callbackNettyPacket - пакет, который пришел с callback response
     */
    public abstract void handleCallback(@NonNull P callbackNettyPacket);

    /**
     * Переопределяющийся метод
     *
     * Обработчик исключений при попыдке отправке или
     * получение callback response
     *
     * @param throwable - исключение
     */
    public void onException(@NonNull Throwable throwable) {
        throwable.printStackTrace();
    }


    /**
     * Поставить пакет в ожидание
     * callback response
     */
    public void waitCallbackResponse() {
        this.waitingResponse = true;
    }

}
