package org.stonlexx.gamelibrary.utility;

import lombok.experimental.UtilityClass;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@UtilityClass
public class AsyncUtil {

    /**
     * scheduled для того, чтобы чуть что можно было
     * какой-нибудь шедулер заебошить, ну так, по фану )0)))0)
     */
    public final ScheduledExecutorService EXECUTOR_SERVICE = Executors.newSingleThreadScheduledExecutor();


    /**
     * Пропидорить раннебл в аснхронном потоке
     *
     * @param command - раннебл
     */
    public void submitAsync(Runnable command) {
        EXECUTOR_SERVICE.submit( command );
    }

}
