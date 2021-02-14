package org.stonlexx.test.utility;

import org.stonlexx.gamelibrary.utility.FileUtil;
import org.stonlexx.gamelibrary.utility.test.ThreadTest;

import java.io.BufferedReader;
import java.io.File;

public class FilesTest {

    public static void main(String[] args) {
        readTest();
        downloadTest();
    }

    @ThreadTest
    private static void downloadTest() {
        File currentFile = new File("src\\test\\resources\\test-directory\\sample-archive.zip");

        FileUtil.downloadFile("https://file-examples-com.github.io/uploads/2017/02/zip_2MB.zip",
                currentFile.toPath(), (handler -> System.out.println("Downloading file from URL...")));

        System.out.println("Success!");
    }

    @ThreadTest
    private static void readTest() {
        File currentFile = new File("src\\test\\resources\\test-directory\\test-file.txt");

        FileUtil.read(currentFile, handler -> {

            try (BufferedReader bufferedReader = new BufferedReader(handler)) {

                while (bufferedReader.ready()) {
                    System.out.println(bufferedReader.readLine());
                }
            }
        });
    }
}
