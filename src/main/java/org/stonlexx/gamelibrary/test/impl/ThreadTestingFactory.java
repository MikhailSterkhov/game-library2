package org.stonlexx.gamelibrary.test.impl;

import lombok.NonNull;
import lombok.SneakyThrows;
import org.stonlexx.gamelibrary.test.TestingFactory;
import org.stonlexx.gamelibrary.test.ThreadTest;

import java.lang.reflect.Method;
import java.util.function.Consumer;

public class ThreadTestingFactory extends TestingFactory<ThreadTest> {

    public ThreadTestingFactory() {
        super(ThreadTest.class);
    }

    @Override
    @SneakyThrows
    public void executeTests(@NonNull Class<?> classWithTestMethods, Consumer<Method> testMethodConsumer) {
        Method[] testMethodArray = classWithTestMethods.getMethods();

        for (Method testMethod : testMethodArray) {
            if (!testMethodCollection.contains(testMethod)) {
                continue;
            }

            testMethodConsumer.accept(testMethod);
            testMethod.invoke(testMethod.getDefaultValue(), new Object[testMethod.getParameterCount()]);
        }
    }

}
