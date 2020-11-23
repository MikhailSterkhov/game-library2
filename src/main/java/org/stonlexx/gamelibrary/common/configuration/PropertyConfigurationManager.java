package org.stonlexx.gamelibrary.common.configuration;

import lombok.Getter;
import lombok.NonNull;
import org.stonlexx.gamelibrary.common.configuration.property.CommonProperty;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Getter
public final class PropertyConfigurationManager {

    private final Map<String, CommonProperty> corePropertyMap = new HashMap<>();


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

            CommonProperty coreProperty = new CommonProperty(bufferedReader);
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
    public CommonProperty getPropertyConfiguration(@NonNull ClassLoader classLoader,
                                                   @NonNull String resourceName) {

        if (!corePropertyMap.containsKey(resourceName.toLowerCase())) {
            addPropertyConfiguration(classLoader, resourceName);
        }

        return corePropertyMap.get(resourceName.toLowerCase());
    }

}
