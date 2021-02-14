package org.stonlexx.test.dependency;

import org.stonlexx.gamelibrary.common.dependency.DependInject;
import org.stonlexx.gamelibrary.utility.test.ThreadTest;

public class AnyoneObject {

    @DependInject
    protected MainDepend mainDepend;


    @ThreadTest
    public void checkDepend() {
        System.out.println("Depend level: " + mainDepend.getDependLevel());
        System.out.println("Depend name: " + mainDepend.getDependName());
    }
}
