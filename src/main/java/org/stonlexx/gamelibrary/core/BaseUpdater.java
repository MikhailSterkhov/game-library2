package org.stonlexx.gamelibrary.core;

import lombok.NonNull;
import org.stonlexx.gamelibrary.GameLibrary;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public interface BaseUpdater<E> extends Consumer<E> {

    /**
     * Запусить задачу автообновления элемента
     *
     * @param element - обновляющийся элемент
     * @param timeUnit - тип времени для периода обновления
     * @param period - период обновления
     */
    default void startTask(@NonNull E element,
                           @NonNull TimeUnit timeUnit, long period) {

        GameLibrary.getInstance().getEventExecutors()
                .scheduleWithFixedDelay(() -> accept(element), 0, period, timeUnit);
    }
}
