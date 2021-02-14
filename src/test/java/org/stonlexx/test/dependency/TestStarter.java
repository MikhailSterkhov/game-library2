package org.stonlexx.test.dependency;

import org.stonlexx.gamelibrary.common.dependency.DependencyInjection;

public class TestStarter {

    public static void main(String[] args) {
        DependencyInjection dependencyInjection = new DependencyInjection();
        AnyoneObject anyoneObject;

// ===============================> Method 1: Scan packages <================================ //

        // Method 1: Scan packages
        dependencyInjection.scanDepends("org.stonlexx.test.dependency");

        anyoneObject = dependencyInjection.getInitializedObject(AnyoneObject.class);
        anyoneObject.checkDepend();

// =======================> Method 2: Self register & inject depends <======================= //

        // Method 2: Self register & inject depends
        anyoneObject = new AnyoneObject();

        dependencyInjection.registerDepend(new MainDepend());
        dependencyInjection.injectDepends(anyoneObject);

        anyoneObject.checkDepend();
    }

}
