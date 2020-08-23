package org.stonlexx.gamelibrary.core.frame;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.stonlexx.gamelibrary.core.frame.component.BaseFrameComponent;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Getter
public class BaseFrame {

    private final JFrame swingFrame = new JFrame();

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
        this.graphics = swingFrame.getGraphics();

        swingFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        swingFrame.setTitle(frameTitle);
        swingFrame.setLocation(frameLocation);
        swingFrame.setSize(frameWidth, frameHeight);

        swingFrame.setResizable(allowResize);
        swingFrame.setIgnoreRepaint(ignoreRepaint);
        swingFrame.setUndecorated(undecorated);

        swingFrame.setVisible(true);
    }

    /**
     * Добавить компонент в окно с возможностью автоперерисовки
     *
     * @param componentUpdater - разрешение на автоперерисовку
     * @param frameComponent - добавляемый компонет
     */
    public void addBaseComponent(boolean componentUpdater, BaseFrameComponent frameComponent) {
        if (componentUpdater) {
            frameComponent.startAutoUpdate(TimeUnit.MILLISECONDS, 20);
        }

        swingFrame.add(frameComponent.getSwingComponent());
    }



}
