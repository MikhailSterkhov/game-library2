package org.stonlexx.gamelibrary.core.frame.component;

import java.util.concurrent.TimeUnit;

public interface BaseFrameComponentUpdater {

    TimeUnit getUpdateUnit();

    long getUpdatePeriod();


    void setUpdateUnit(TimeUnit timeUnit);

    void setUpdatePeriod(long period);
}
