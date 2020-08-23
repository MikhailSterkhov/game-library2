package org.stonlexx.test.bean;

import lombok.Getter;
import org.stonlexx.gamelibrary.core.bean.*;

@Getter
@Bean
public class TestPlayer {

    //@BeanQualifier("testObject")
    private final ITestObject testObject;

    public TestPlayer() {
        this.testObject = new TestObject();
        this.testObject.setString("123");
    }

    @QualifierConstructor
    public TestPlayer(@BeanQualifier("testObject") ITestObject testObject) {
        this.testObject = testObject;
    }


    @InitMethod
    public void initialize() {
        System.out.println("testObject element: " + testObject.getString());
    }

}
