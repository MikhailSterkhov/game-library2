package org.stonlexx.test.frame;

import org.apache.commons.lang3.RandomStringUtils;
import org.stonlexx.gamelibrary.common.frame.BaseFrame;
import org.stonlexx.gamelibrary.common.frame.component.ComponentBuilder;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.TimeUnit;

public class TestStarter {

    public static void main(String[] args) {
        BaseFrame baseFrame = new BaseFrame("Test frame", new Point(350, 200), 1200, 800);
        baseFrame.setBackground(Color.DARK_GRAY);
        baseFrame.setAllowResize(true);

        JTextArea textArea = baseFrame.add(ComponentBuilder.newBuilder(JTextArea.class)
                .location(50, 100)
                .size(500, 100)

                .background(Color.DARK_GRAY)
                .color(Color.LIGHT_GRAY)
                .font(new Font(Font.DIALOG_INPUT, Font.BOLD, 18))

                .accept((component, builder) -> component.setText("<Напиши тут свой пароль от вк>"))
                .autoUpdater(TimeUnit.SECONDS, 1)

                .build(), BorderLayout.CENTER);

        baseFrame.add(ComponentBuilder.newBuilder(JLabel.class)
                .location(100, 10)
                .size(200, 20)

                .accept((component1, builder1) -> component1.setText(RandomStringUtils.randomAlphabetic(15)))
                .autoUpdater(TimeUnit.SECONDS, 1)

                .build());

        baseFrame.add(ComponentBuilder.newBuilder(JButton.class)
                .location(100, 300)
                .size(200, 30)

                .buttonAction((component1, event) -> System.out.println(textArea.getText()))
                .accept((component1, builder1) -> component1.setText("ACCEPT"))
                .autoUpdater(TimeUnit.SECONDS, 1)

                .build());

        baseFrame.showFrame();
    }
}
