package org.stonlexx.gamelibrary.core.frame.component.impl;

import lombok.NonNull;
import org.stonlexx.gamelibrary.core.frame.component.BaseFrameComponentAdapter;

import javax.swing.*;
import java.awt.*;

public class TextComponent extends BaseFrameComponentAdapter<JLabel> {

    public TextComponent(String textValue) {
        this(textValue, 50, 50);
    }


    public TextComponent(String textValue, int x, int y) {
        super(new JLabel());

        swingComponent.setText(textValue);
        swingComponent.setLocation(x, y);
    }


    /**
     * Получить текст из текстового компонента
     */
    public String getText() {
        return getSwingComponent().getText();
    }

    /**
     * Получить шрифт из текстового компонента
     */
    public Font getFont() {
        return getSwingComponent().getFont();
    }


    /**
     * Изменить текст в текстовом компоненты
     *
     * @param value - новое значение для компонента
     */
    public void setText(@NonNull String value) {
        getSwingComponent().setText(value);
    }

    /**
     * Изменить шрифт в текстовом компоненты
     *
     * @param value - новое значение для компонента
     */
    public void setFont(@NonNull Font value) {
        getSwingComponent().setFont(value);
    }

}
