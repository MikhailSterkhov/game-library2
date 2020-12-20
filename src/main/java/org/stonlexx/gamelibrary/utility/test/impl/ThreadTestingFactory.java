package org.stonlexx.gamelibrary.utility.test.impl;

import lombok.NonNull;
import lombok.SneakyThrows;
import org.stonlexx.gamelibrary.utility.test.TestingFactory;
import org.stonlexx.gamelibrary.utility.test.ThreadTest;

import java.lang.reflect.Method;
import java.util.function.Consumer;

public class ThreadTestingFactory extends TestingFactory<ThreadTest> {

    public ThreadTestingFactory() {
        super(ThreadTest.class);
    }

    @Override
    @SneakyThrows
    public void executeTests(@NonNull Object objectWithTestMethods, Consumer<Method> testMethodConsumer) {
        Method[] testMethodArray = objectWithTestMethods.getClass().getMethods();

        for (Method testMethod : testMethodArray) {
            if (!testMethodCollection.contains(testMethod)) {
                continue;
            }

            if (testMethodConsumer != null) {
                testMethodConsumer.accept(testMethod);
            }

            testMethod.invoke(objectWithTestMethods, new Object[testMethod.getParameterCount()]);
        }
    }
}
