package org.stonlexx.test.utility;

import org.stonlexx.gamelibrary.utility.Reference;
import org.stonlexx.gamelibrary.utility.location.PointLocation;
import org.stonlexx.gamelibrary.utility.test.ThreadTest;
import org.stonlexx.test.netty.packet.ARTestPacket;

import java.util.concurrent.TimeUnit;

public class ReferenceTest {

    public static void main(String[] args) {
        System.out.println("Процесс выполнился за " + Reference.measureDelay(ReferenceTest::test, TimeUnit.MILLISECONDS) + "ms");
        System.out.println("Неизвестный процесс выполнился за " + Reference.measureDelay(null, TimeUnit.MILLISECONDS) + "ms");
    }

    @ThreadTest
    private static void test() {
        Reference.dump(new PointLocation(10.5, -20, 33.5));
        Reference.dump(new ARTestPacket());
    }

}
