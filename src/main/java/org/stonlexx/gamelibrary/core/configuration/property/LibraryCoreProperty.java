package org.stonlexx.gamelibrary.core.configuration.property;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.io.Reader;
import java.util.Objects;
import java.util.Properties;

@RequiredArgsConstructor
@Getter
public class LibraryCoreProperty {

    private final Reader propertyReader;
    private final Properties properties = new Properties();


    public void hasPropertyReadable() {
        if (!properties.isEmpty()) {
            return;
        }

        try {
            Objects.requireNonNull(propertyReader, "property reader equals null");

            properties.load(propertyReader);
        }

        catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public String getProperty(String propertyKey) {
        hasPropertyReadable();

        return properties.getProperty(propertyKey);
    }

    public String getPropertyOrDefault(String propertyKey, String defaultValue) {
        hasPropertyReadable();

        return properties.getProperty(propertyKey, defaultValue);
    }

    public void setProperty(String propertyKey, String propertyValue) {
        hasPropertyReadable();

        properties.setProperty(propertyKey, propertyValue);
    }

}
