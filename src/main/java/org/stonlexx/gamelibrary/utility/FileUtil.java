package org.stonlexx.gamelibrary.utility;

import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.io.*;

@UtilityClass
public class FileUtil {

    @SneakyThrows
    public void read(@NonNull File file,
                     @NonNull ThrowableFileHandler<FileReader> fileHandler) {

        FileReader fileReader = new FileReader(file);
        fileHandler.handle(fileReader);

        fileReader.close();
    }

    @SneakyThrows
    public void write(@NonNull File file,
                      @NonNull ThrowableFileHandler<FileWriter> fileHandler) {

        FileWriter fileWriter = new FileWriter(file);
        fileHandler.handle(fileWriter);

        fileWriter.flush();
        fileWriter.close();
    }

    @SneakyThrows
    public void input(@NonNull File file,
                      @NonNull ThrowableFileHandler<FileInputStream> fileHandler) {

        FileInputStream fileInputStream = new FileInputStream(file);
        fileHandler.handle(fileInputStream);

        fileInputStream.close();
    }

    @SneakyThrows
    public void output(@NonNull File file,
                       @NonNull ThrowableFileHandler<FileOutputStream> fileHandler) {

        FileOutputStream fileOutputStream = new FileOutputStream(file);
        fileHandler.handle(fileOutputStream);

        fileOutputStream.flush();
        fileOutputStream.close();
    }


    public interface ThrowableFileHandler<H> {

        void handle(H handler) throws Exception;
    }

}
