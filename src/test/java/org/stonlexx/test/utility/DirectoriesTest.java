package org.stonlexx.test.utility;

import org.stonlexx.gamelibrary.utility.Directories;

import java.io.File;
import java.nio.file.Path;

public class DirectoriesTest {

    public static void main(String[] args) {
        Path directory = new File("test-directory").toPath();

        // Очистить директорию, не удаляя ее
        Directories.clearDirectory(directory.toFile(), false);

        // Очистить и удалить директорию
        Directories.clearDirectory(directory.toFile(), true);

        // Скопировать все файлы директории в новую, создав ее автоматически
        Directories.copyDirectory(directory, new File("new-test-directory").toPath());
    }
}
