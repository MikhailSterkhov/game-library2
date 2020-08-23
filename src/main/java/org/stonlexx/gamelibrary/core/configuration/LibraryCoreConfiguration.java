package org.stonlexx.gamelibrary.core.configuration;

import lombok.Getter;
import lombok.NonNull;
import org.stonlexx.gamelibrary.core.configuration.property.LibraryCoreProperty;

import java.io.*;
import java.lang.invoke.MethodHandles;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Getter
public final class LibraryCoreConfiguration {

    private final Map<String, LibraryCoreProperty> corePropertyMap = new HashMap<>();
    private final ConfigurationApplicationContext applicationContext = new ConfigurationApplicationContext();

    private final MethodHandles.Lookup publicLookup = MethodHandles.publicLookup();


    /**
     * Добавить Property конфигурацию в кеш
     *
     * @param classLoader - хранилище классов и файлов
     * @param resourceName - имя получаемого ресурса
     */
    public void addPropertyConfiguration(@NonNull ClassLoader classLoader,
                                         @NonNull String resourceName) {

        addPropertyConfiguration(classLoader.getResource(resourceName), resourceName);
    }

    /**
     * Добавить Property конфигурацию в кеш
     *
     * @param resourceStream - хранилище данных ресурса
     * @param resourceName - имя получаемого ресурса
     */
    public void addPropertyConfiguration(@NonNull URL resourceStream,
                                         @NonNull String resourceName) {

        try {
            URLConnection urlConnection = resourceStream.openConnection();

            InputStreamReader inputStreamReader = new InputStreamReader(urlConnection.getInputStream(), StandardCharsets.UTF_8);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            LibraryCoreProperty coreProperty = new LibraryCoreProperty(bufferedReader);
            corePropertyMap.put(resourceName.toLowerCase(), coreProperty);
        }

        catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Получить кешированную Property конфигурацию
     *
     * @param classLoader - хранилище классов и файлов
     * @param resourceName - мя получаемого ресурса
     */
    public LibraryCoreProperty getPropertyConfiguration(@NonNull ClassLoader classLoader,
                                                        @NonNull String resourceName) {

        if (!corePropertyMap.containsKey(resourceName.toLowerCase())) {
            addPropertyConfiguration(classLoader, resourceName);
        }

        return corePropertyMap.get(resourceName.toLowerCase());
    }

}
