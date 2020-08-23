package org.stonlexx.gamelibrary.core.frame.component;

import lombok.NonNull;
import org.stonlexx.gamelibrary.core.BaseUpdater;

import javax.swing.*;
import java.util.concurrent.TimeUnit;

public interface BaseFrameComponent {


    /**
     * Запустить автообновление компонента
     *
     * @param timeUnit - тип времени для периода обновления
     * @param period - период обновления
     */
    void startAutoUpdate(@NonNull TimeUnit timeUnit, long period);


    /**
     * Получить задачу автообновления
     * данного компонента
     */
    BaseUpdater<BaseFrameComponent> getComponentUpdater();


    JComponent getSwingComponent();
    JPanel getSwingPanel();
}
