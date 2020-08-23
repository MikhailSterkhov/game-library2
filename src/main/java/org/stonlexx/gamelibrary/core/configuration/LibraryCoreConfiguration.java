package org.stonlexx.gamelibrary.core.configuration;

import lombok.Getter;
import lombok.NonNull;
import org.stonlexx.gamelibrary.core.configuration.property.LibraryCoreProperty;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@Getter
public final class LibraryCoreConfiguration {

    private final Map<String, LibraryCoreProperty> corePropertyMap = new HashMap<>();
    private final ConfigurationApplicationContext applicationContext = new ConfigurationApplicationContext();


    /**
     * Добавить Property конфигурацию в кеш
     *
     * @param classLoader - хранилище классов и файлов
     * @param resourceName - имя получаемого ресурса
     */
    public void addPropertyConfiguration(@NonNull ClassLoader classLoader,
                                         @NonNull String resourceName) {

        addPropertyConfiguration(classLoader.getResourceAsStream(resourceName), resourceName);
    }

    /**
     * Добавить Property конфигурацию в кеш
     *
     * @param resourceStream - хранилище данных ресурса
     * @param resourceName - имя получаемого ресурса
     */
    public void addPropertyConfiguration(@NonNull InputStream resourceStream,
                                         @NonNull String resourceName) {

        InputStreamReader inputStreamReader = new InputStreamReader(resourceStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        LibraryCoreProperty coreProperty = new LibraryCoreProperty(bufferedReader);

        corePropertyMap.put(resourceName.toLowerCase(), coreProperty);
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
