package org.stonlexx.test.frame;

import org.apache.commons.lang3.RandomStringUtils;
import org.stonlexx.gamelibrary.core.frame.BaseFrame;
import org.stonlexx.gamelibrary.core.frame.component.ComponentBuilder;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.TimeUnit;

public class TestStarter {

    public static void main(String[] args) {
        BaseFrame baseFrame = new BaseFrame("Test frame", new Point(350, 200), 1200, 800);

        baseFrame.setAllowResize(true);
        //baseFrame.setUndecorated(true);
        //baseFrame.setAllowMoving(false);

        baseFrame.addBaseComponent(ComponentBuilder.newBuilder(JLabel.class)
                .name("test-label-component")
                .location(150, 300)
                .size(200, 50)

                .background(Color.DARK_GRAY)
                .color(Color.WHITE)

                .click((component, event) -> System.out.println("Ты кликнул по строке " + event.getClickCount() + " раз"))

                .autoUpdater(TimeUnit.SECONDS, 2)
                .accept(component -> component.setText(RandomStringUtils.randomAlphabetic(25))).build());

        baseFrame.showFrame();
    }
}
