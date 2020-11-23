package org.stonlexx.gamelibrary.test;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.stonlexx.gamelibrary.test.impl.AsynchronousTestingFactory;
import org.stonlexx.gamelibrary.test.impl.ThreadTestingFactory;

@RequiredArgsConstructor
public enum Testing {

    ASYNCHRONOUS(
            new AsynchronousTestingFactory()
    ),

    THREAD(
            new ThreadTestingFactory()
    );


    @Getter
    private final TestingFactory factory;
}
