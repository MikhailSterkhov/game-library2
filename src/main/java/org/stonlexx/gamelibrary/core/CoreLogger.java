package org.stonlexx.gamelibrary.core;

import jline.console.ConsoleReader;
import lombok.Getter;
import lombok.NonNull;
import org.fusesource.jansi.Ansi;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class CoreLogger extends Logger {

    protected final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");

    @Getter
    protected ConsoleReader consoleReader;


    public CoreLogger() {
        super("gameLibrary_core_logger", null);

        try {
            this.consoleReader = new ConsoleReader();

            setLevel(Level.ALL);
        }

        catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public void log(@NonNull LogRecord logRecord) {
        StringBuilder formatted = new StringBuilder();

        formatted.append(simpleDateFormat.format(logRecord.getMillis()))
                .append("  [")
                .append(logRecord.getLevel().getLocalizedName())
                .append("] ")
                .append(logRecord.getMessage())
                .append('\n');

        Throwable throwable = logRecord.getThrown();

        if (throwable != null) {
            StringWriter writer = new StringWriter();

            throwable.printStackTrace(new PrintWriter(writer));

            formatted.append(Ansi.ansi().fgRed().toString())
                    .append(writer)
                    .append(ConsoleReader.RESET_LINE);
        }

        writeLine(formatted.toString() + Ansi.ansi().reset().toString());
    }

    protected void writeLine(@NonNull String line) {
        try {

            consoleReader.print(line);

            consoleReader.drawLine();
            consoleReader.flush();
        }

        catch (Exception ignored) { }
    }

}
