package org.stonlexx.gamelibrary.core.frame;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.stonlexx.gamelibrary.core.BaseUpdater;
import org.stonlexx.gamelibrary.core.frame.component.BaseFrameComponent;
import org.stonlexx.gamelibrary.core.frame.component.BaseFrameComponentClickConsumer;
import org.stonlexx.gamelibrary.core.frame.component.BaseFrameComponentUpdater;
import org.stonlexx.gamelibrary.core.frame.listener.MouseListenerAdapter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Getter
public class BaseFrame {

    private final BaseFrameImplementation frameImplementation                               = new BaseFrameImplementation();
    private final List<BaseFrameComponent<JComponent>> frameComponentList                            = new ArrayList<>();


    private final String frameTitle;
    private final Point frameLocation;

    private final int frameWidth;
    private final int frameHeight;

    @Setter private boolean loaded;

    @Setter private boolean allowResize;
    @Setter private boolean allowMoving;
    @Setter private boolean ignoreRepaint;
    @Setter private boolean undecorated;

    @Setter private Graphics graphics;


    /**
     * Установить все необходимые настройки
     * окну и открыть его
     */
    public void showFrame() {
        this.graphics = frameImplementation.getGraphics();

        frameImplementation.setUndecorated(undecorated);
        frameImplementation.setResizable(allowResize);
        frameImplementation.setIgnoreRepaint(ignoreRepaint);

        frameImplementation.setTitle(frameTitle);
        frameImplementation.setLocation(frameLocation);
        frameImplementation.setSize(frameWidth, frameHeight);

        frameImplementation.addMouseListener(new MouseListenerAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                Point mousePoint = event.getPoint();

                JComponent swingComponent = BaseFrameComponent.KEY_LISTENER_COMPONENTS.keySet()
                        .stream()
                        .filter(keyComponent -> {
                            int mouseX = mousePoint.x;
                            int mouseY = mousePoint.y;

                            int componentStartX = keyComponent.getParent().getX() + keyComponent.getX();
                            int componentStartY = keyComponent.getParent().getY() + keyComponent.getY();

                            int componentEndX = componentStartX + keyComponent.getParent().getWidth();
                            int componentEndY = componentStartY + keyComponent.getParent().getHeight();

                            return mouseX >= componentStartX && mouseX <= componentEndX
                                    && mouseY >= componentStartY && mouseY <= componentEndY;

                        }).findFirst().orElse(null);

                if (swingComponent == null) {
                    return;
                }

                BaseFrameComponentClickConsumer componentClickConsumer
                        = BaseFrameComponent.KEY_LISTENER_COMPONENTS.get(swingComponent);

                if (componentClickConsumer != null) {
                    componentClickConsumer.accept(swingComponent, event);
                }
            }

        });

        new BaseUpdater().startTask(new Runnable() {

            private long timeCounter = 0;

            @Override
            public void run() {
                for (BaseFrameComponent<JComponent> baseFrameComponent : frameComponentList) {
                    BaseFrameComponentUpdater componentUpdater = baseFrameComponent.getComponentUpdater();

                    if (componentUpdater.getUpdateUnit() == null) {
                        continue;
                    }

                    long periodToMilliseconds = componentUpdater.getUpdateUnit().toMillis(componentUpdater.getUpdatePeriod());

                    if (timeCounter % periodToMilliseconds == 0) {
                        baseFrameComponent.getComponentAcceptable().accept(baseFrameComponent.getSwingComponent());
                        baseFrameComponent.getSwingPanel().repaint();
                    }
                }

                timeCounter++;
            }

        }, TimeUnit.MILLISECONDS, 1);

        frameImplementation.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frameImplementation.setVisible(true);
    }

    /**
     * Добавить компонент в окно
     *
     * @param baseFrameComponent - добавляемый компонет
     */
    public <C extends JComponent> void addBaseComponent(BaseFrameComponent<C> baseFrameComponent) {
        frameImplementation.add(baseFrameComponent.getSwingPanel());
        frameComponentList.add((BaseFrameComponent<JComponent>) baseFrameComponent);
    }


}
