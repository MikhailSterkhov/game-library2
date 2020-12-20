package org.stonlexx.test.testing;

import org.stonlexx.gamelibrary.utility.test.AsyncTest;
import org.stonlexx.gamelibrary.utility.test.Testing;
import org.stonlexx.gamelibrary.utility.test.TestingFactory;

public class TestStarter {

    public static void main(String[] args) {
        TestingFactory<AsyncTest> testingFactory = Testing.ASYNCHRONOUS.getFactory();

        testingFactory.setMainPackage("org.stonlexx.test");
        testingFactory.executeTests(new TestStarter() /* Can write an object class or the object */,
                null);
    }

    @AsyncTest
    public void anyoneMethod() {
        System.out.println("Test of testing method with test print :P");
    }

}
