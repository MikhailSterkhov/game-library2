package org.stonlexx.gamelibrary.core;

import lombok.NonNull;
import org.apache.commons.lang3.RandomStringUtils;
import org.stonlexx.gamelibrary.GameLibrary;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class BaseUpdater {

    /**
     * Запусить задачу автообновления элемента
     *
     * @param runnable - задача обновления
     * @param timeUnit - тип времени для периода обновления
     * @param period - период обновления
     */
    public void startTask(@NonNull Runnable runnable,
                           @NonNull TimeUnit timeUnit, long period) {

        GameLibrary.getInstance().getSchedulerManager()
                .runTimer(RandomStringUtils.randomAlphabetic(255), runnable, period, period, timeUnit);
    }
}
