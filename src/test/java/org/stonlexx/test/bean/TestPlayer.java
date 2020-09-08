package org.stonlexx.test.bean;

import lombok.Getter;
import org.stonlexx.gamelibrary.core.bean.*;

@Getter
@Bean
public class TestPlayer {

    @BeanQualifier("testObject")
    private ITestObject testObject;


    @InitMethod
    public void onInitialize() {
        System.out.println("testObject element: " + testObject.getString());
    }

    @DestroyMethod
    public void onDestroy() {
        System.out.println("player has been destroyed!");
    }

}
