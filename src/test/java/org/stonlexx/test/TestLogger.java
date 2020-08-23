package org.stonlexx.test;

import org.stonlexx.gamelibrary.core.CoreLogger;

import java.util.logging.LogRecord;

public class TestLogger extends CoreLogger {

    @Override
    public void log(LogRecord logRecord) {
        writeLine(simpleDateFormat.format(System.currentTimeMillis()) + " " + logRecord.getMessage() + "\n");
    }

}
