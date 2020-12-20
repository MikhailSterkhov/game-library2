package org.stonlexx.gamelibrary.utility.test.impl;

import lombok.NonNull;
import org.stonlexx.gamelibrary.utility.test.AsyncTest;
import org.stonlexx.gamelibrary.utility.test.TestingFactory;
import org.stonlexx.gamelibrary.utility.query.AsyncUtil;

import java.lang.reflect.Method;
import java.util.function.Consumer;

public class AsynchronousTestingFactory extends TestingFactory<AsyncTest> {

    public AsynchronousTestingFactory() {
        super(AsyncTest.class);
    }

    @Override
    public void executeTests(@NonNull Object objectWithTestMethods, Consumer<Method> testMethodConsumer) {
        Method[] testMethodArray = objectWithTestMethods.getClass().getMethods();

        for (Method testMethod : testMethodArray) {
            if (!testMethodCollection.contains(testMethod)) {
                continue;
            }

            AsyncUtil.submitThrowsAsync(() -> {

                if (testMethodConsumer != null) {
                    testMethodConsumer.accept(testMethod);
                }

                testMethod.invoke(objectWithTestMethods, new Object[testMethod.getParameterCount()]);
            });
        }
    }

}
