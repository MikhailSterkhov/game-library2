package org.stonlexx.gamelibrary.common.frame.component;

import lombok.NonNull;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;

public interface BaseFrameComponent<C extends JComponent> {

    Map<JComponent, BaseFrameComponentClickConsumer> KEY_LISTENER_COMPONENTS
            = new HashMap<>();


    /**
     * Запустить автообновление компонента
     *
     * @param timeUnit - тип времени для периода обновления
     * @param period - период обновления
     */
    void startAutoUpdate(@NonNull TimeUnit timeUnit, long period);
    void setComponentAcceptable(@NonNull BiConsumer<C, ComponentBuilder<C>> componentAcceptable);


    /**
     * Получить задачу автообновления
     * данного компонента
     */
    BaseFrameComponentUpdater getComponentUpdater();
    BiConsumer<C, ComponentBuilder<C>> getComponentAcceptable();


    C getSwingComponent();
    JPanel getSwingPanel();


    void initialize();
}
