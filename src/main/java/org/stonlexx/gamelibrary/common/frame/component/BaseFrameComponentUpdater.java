package org.stonlexx.gamelibrary.common.frame.component;

import java.util.concurrent.TimeUnit;

public interface BaseFrameComponentUpdater {

    TimeUnit getUpdateUnit();

    long getUpdatePeriod();


    void setUpdateUnit(TimeUnit timeUnit);

    void setUpdatePeriod(long period);
}
