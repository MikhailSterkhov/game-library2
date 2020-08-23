package org.stonlexx.gamelibrary.core.scheduler;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.stonlexx.gamelibrary.GameLibrary;

import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public abstract class CoreScheduler implements Runnable {

    @Getter
    private final String identifier;


    /**
     * Отмена и закрытие потока
     */
    public void cancel() {
        GameLibrary.getInstance().getSchedulerManager().cancelScheduler(identifier);
    }

    /**
     * Запустить асинхронный поток
     */
    public void runAsync() {
        GameLibrary.getInstance().getSchedulerManager().runAsync(this);
    }

    /**
     * Запустить поток через определенное
     * количество времени
     *
     * @param delay - время
     * @param timeUnit - единица времени
     */
    public void runLater(long delay, TimeUnit timeUnit) {
        GameLibrary.getInstance().getSchedulerManager().runLater(identifier, this, delay, timeUnit);
    }

    /**
     * Запустить цикличный поток через
     * определенное количество времени
     *
     * @param delay - время
     * @param period - период цикличного воспроизведения
     * @param timeUnit - единица времени
     */
    public void runTimer(long delay, long period, TimeUnit timeUnit) {
        GameLibrary.getInstance().getSchedulerManager().runTimer(identifier, this, delay, period, timeUnit);
    }

}
