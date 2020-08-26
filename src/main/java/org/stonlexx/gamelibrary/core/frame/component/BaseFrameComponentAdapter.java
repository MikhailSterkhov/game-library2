package org.stonlexx.gamelibrary.core.frame.component;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.swing.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

@RequiredArgsConstructor
@Getter
public abstract class BaseFrameComponentAdapter<C extends JComponent>
        implements BaseFrameComponent<C> {


    protected final BaseFrameComponentUpdater componentUpdater                                   = new ComponentUpdater();
    protected final JPanel swingPanel                                                            = new JPanel();

    protected final C swingComponent;

    @Setter
    @Getter
    protected Consumer<C> componentAcceptable;


    @Override
    public void startAutoUpdate(@NonNull TimeUnit timeUnit, long period) {
        componentUpdater.setUpdateUnit(timeUnit);
        componentUpdater.setUpdatePeriod(period);
    }

    @Override
    public void initialize() {

        // panel options
        swingPanel.setLocation(swingComponent.getLocation());
        swingPanel.setSize(swingComponent.getSize());

        swingPanel.setBackground(swingComponent.getBackground());

        // component options
        swingComponent.setLocation(0, 0);

        // add component
        swingPanel.add(swingComponent);
    }


    @Getter
    @Setter
    public static class ComponentUpdater implements BaseFrameComponentUpdater {

        private TimeUnit updateUnit;
        private long updatePeriod;
    }

}
