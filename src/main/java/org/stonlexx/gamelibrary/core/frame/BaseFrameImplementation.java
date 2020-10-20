package org.stonlexx.gamelibrary.core.frame;

import lombok.Getter;
import org.stonlexx.gamelibrary.core.frame.component.BaseFrameComponent;

import javax.swing.*;
import java.awt.*;

/**
 * Оболочка под JFrame
 */
public class BaseFrameImplementation extends JFrame {

    @Getter
    private boolean fullScreen = false;

    @Getter
    private ImageIcon backgroundImage = null;


    /**
     * Создать окно игры, созданное на основе стандартного
     * окна windows с заданными размерами и координатами
     *
     * @param x      - координата X
     * @param y      - координата Y
     * @param width  - ширина окна
     * @param height - высота окна
     */
    public BaseFrameImplementation(String title, ImageIcon imageIcon, int x, int y, int width, int height) {
        super(title);

        setLayeredPane(new JLayeredPane());

        if (imageIcon != null) {
            setIconImage(imageIcon.getImage());
        }

        setSize(width, height);
        setLocation(x, y);

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    /**
     * Создать окно игры, которое будет установлено
     * на полный экран
     */
    public BaseFrameImplementation(String title) {
        this(title, null, 150, 150,

                Toolkit.getDefaultToolkit().getScreenSize().width,
                Toolkit.getDefaultToolkit().getScreenSize().height);
    }

    /**
     * Создать окно игры, которое будет установлено
     * на полный экран
     */
    public BaseFrameImplementation(String title, ImageIcon imageIcon) {
        this(title, imageIcon, 150, 150,

                Toolkit.getDefaultToolkit().getScreenSize().width,
                Toolkit.getDefaultToolkit().getScreenSize().height);
    }

    /**
     * Создать окно игры, которое будет установлено
     * на полный экран
     */
    public BaseFrameImplementation(String title, ImageIcon imageIcon, int x, int y) {
        this(title, imageIcon, x, y,

                Toolkit.getDefaultToolkit().getScreenSize().width,
                Toolkit.getDefaultToolkit().getScreenSize().height);
    }

    /**
     * Создать окно игры, которое будет установлено
     * на полный экран
     */
    public BaseFrameImplementation() {
        this("unknown", null, 50, 50,

                Toolkit.getDefaultToolkit().getScreenSize().width,
                Toolkit.getDefaultToolkit().getScreenSize().height);
    }


    /**
     * Установить фрейму полный экран в
     * зависимости от булевого выражения
     */
    public void setFullScreen(boolean fullScreen) {
        this.fullScreen = fullScreen;

        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice device = env.getScreenDevices()[0];

        device.setFullScreenWindow(fullScreen ? this : null);
    }


    @Override
    public void paint(Graphics graphics) {

        //устанавливаем изображение background
        if (backgroundImage != null) {
            graphics.drawImage(backgroundImage.getImage(), getX(), getY(), getWidth(), getHeight(),
                    backgroundImage.getImageObserver());
        }

        //устанавливаем нужные цвета
        setBackground(getLayeredPane().getBackground());
        setForeground(getLayeredPane().getForeground());

        //отрисовываем все компоненты
        super.paint(graphics);
    }

    @Override
    public Component add(Component component) {
        return getLayeredPane().add(component);
    }

    @Override
    public void setTitle(String title) {
        super.setTitle(title);

        revisibleFrame();
    }

    /**
     * Перепоказать окно
     */
    public void revisibleFrame() {
        setVisible(false);
        setVisible(true);
    }

    /**
     * Добавить компонент-оболочку во фрейм
     *
     * @param gameComponent - компонент-оболочка
     */
    public void add(BaseFrameComponent<? extends JComponent> gameComponent) {
        add(gameComponent.getSwingPanel());
    }

    /**
     * Установить изображение фона фрейма
     *
     * @param backgroundImage - изображение
     */
    public void setBackgroundImage(ImageIcon backgroundImage) {
        this.backgroundImage = backgroundImage;

        repaint();
    }

}
