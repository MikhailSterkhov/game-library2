package org.stonlexx.test.bean;

import lombok.Getter;
import org.stonlexx.gamelibrary.core.bean.*;

@Getter
@Bean
public class TestPlayer {

    @BeanQualifier("testObject")
    private ITestObject testObject;

    @InitMethod
    public void initialize() {
        System.out.println("testObject element: " + testObject.getString());
    }

}
