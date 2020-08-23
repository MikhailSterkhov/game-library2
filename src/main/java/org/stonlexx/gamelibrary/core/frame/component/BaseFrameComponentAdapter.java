package org.stonlexx.gamelibrary.core.frame.component;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.stonlexx.gamelibrary.core.BaseUpdater;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Getter
public abstract class BaseFrameComponentAdapter<SC extends Component> implements BaseFrameComponent {


    protected final JPanel swingPanel                                                     = new JPanel();
    protected final BaseUpdater<BaseFrameComponent> componentUpdater                      = new ComponentUpdater();

    protected final SC swingComponent;


    @Override
    public void startAutoUpdate(@NonNull TimeUnit timeUnit, long period) {
        componentUpdater.startTask(this, timeUnit, period);
    }


    /**
     * Наследствие задачи автообновления
     * компонента для прорисовки новых значений
     * на фрейме swing`еров
     */
    private static class ComponentUpdater implements BaseUpdater<BaseFrameComponent> {

        @Override
        public void accept(BaseFrameComponent frameComponent) {
            frameComponent.getSwingPanel().repaint();
            frameComponent.getSwingPanel().updateUI();

            frameComponent.getSwingComponent().repaint();
            frameComponent.getSwingComponent().updateUI();
        }
    }

}
