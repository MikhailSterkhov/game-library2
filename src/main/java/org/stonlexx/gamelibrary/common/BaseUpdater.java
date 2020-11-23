package org.stonlexx.gamelibrary.common;

import lombok.NonNull;
import org.apache.commons.lang3.RandomStringUtils;
import org.stonlexx.gamelibrary.GameLibrary;

import java.util.concurrent.TimeUnit;

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
                .runTimer(RandomStringUtils.randomAlphabetic(256), runnable, period, period, timeUnit);
    }
}
