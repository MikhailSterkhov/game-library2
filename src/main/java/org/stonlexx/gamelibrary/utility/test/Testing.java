package org.stonlexx.gamelibrary.utility.test;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.stonlexx.gamelibrary.utility.test.impl.AsynchronousTestingFactory;
import org.stonlexx.gamelibrary.utility.test.impl.ThreadTestingFactory;

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
