package org.stonlexx.gamelibrary.test.impl;

import lombok.NonNull;
import lombok.SneakyThrows;
import org.stonlexx.gamelibrary.test.AsyncTest;
import org.stonlexx.gamelibrary.test.TestingFactory;
import org.stonlexx.gamelibrary.utility.query.AsyncUtil;

import java.lang.reflect.Method;
import java.util.function.Consumer;

public class AsynchronousTestingFactory extends TestingFactory<AsyncTest> {

    public AsynchronousTestingFactory() {
        super(AsyncTest.class);
    }

    @Override
    @SneakyThrows
    public void executeTests(@NonNull Class<?> classWithTestMethods, Consumer<Method> testMethodConsumer) {
        Method[] testMethodArray = classWithTestMethods.getMethods();

        for (Method testMethod : testMethodArray) {
            if (!testMethodCollection.contains(testMethod)) {
                continue;
            }

            AsyncUtil.submitAsync(() -> {

                testMethodConsumer.accept(testMethod);
                testMethod.invoke(testMethod.getDefaultValue(), new Object[testMethod.getParameterCount()]);
            });
        }
    }

}
