package org.stonlexx.test;

import org.stonlexx.gamelibrary.common.CommonLogger;

import java.util.logging.LogRecord;

/**
 * Для того, чтобы поставить в библиотеку
 * свой логгер, нужно переопределить его
 * специальным сеттером:
 *
 * {@code GameLibrary.getInstance().setLogger(new YourLogger()) }
 *
 * После чего, геттер логгера, который также получается
 * из {@link org.stonlexx.gamelibrary.GameLibrary}, будет возвращать именно Ваш логгер
 */
public class TestLogger extends CommonLogger {

    @Override
    public void log(LogRecord logRecord) {
        writeLine(simpleDateFormat.format(System.currentTimeMillis()) + " " + logRecord.getMessage() + "\n");
    }

}
