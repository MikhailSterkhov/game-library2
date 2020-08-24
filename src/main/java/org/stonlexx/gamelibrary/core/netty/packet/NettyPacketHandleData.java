package org.stonlexx.gamelibrary.core.netty.packet;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class NettyPacketHandleData {

    @Getter
    private final Map<String, WeakReference<?>> handleDataMap = new HashMap<>();


// ================================================================================================================== //

    /**
     * Создать хранилище пакетных данных
     * для его обработки
     */
    public static NettyPacketHandleData create() {
        return new NettyPacketHandleData();
    }

// ================================================================================================================== //


    /**
     * Получить хранилище объекта, который хранится
     * по определенному имени данных
     *
     * @param objectType - тип объекта, который хотим получить
     * @param handleDataName - имя данных для объекта
     */
    public <V> WeakReference<V> getHandleDataValue(@NonNull Class<V> objectType,
                                                   @NonNull String handleDataName) {

        return (WeakReference<V>) handleDataMap.get(handleDataName.toLowerCase());
    }

    /**
     * Получить объект из хранилища, который хранится
     * по определенному имени данных
     *
     * @param objectType - тип объекта, который хотим получить
     * @param handleDataName - имя данных для объекта
     */
    public <V> V getHandleDataObject(@NonNull Class<V> objectType,
                                     @NonNull String handleDataName) {

        return getHandleDataValue(objectType, handleDataName).get();
    }

    /**
     * Добавить данные в хранилище
     *
     * @param handleDataName - имя данных
     * @param handleDataValue - значение данных
     */
    public <V> void addHandleData(@NonNull String handleDataName,
                                  @NonNull V handleDataValue) {

        handleDataMap.put(handleDataName.toLowerCase(), new WeakReference<>(handleDataValue));
    }

}
