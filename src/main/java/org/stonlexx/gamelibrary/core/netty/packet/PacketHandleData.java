package org.stonlexx.gamelibrary.core.netty.packet;

import lombok.Getter;
import lombok.NonNull;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

public class PacketHandleData {

    @Getter
    private final Map<String, WeakReference<?>> handleDataMap = new HashMap<>();


// ==================================== // FACTORY // ==================================== //

    /**
     * Создать хранилище пакетных данных
     * для его обработки
     */
    public static PacketHandleData create() {
        return new PacketHandleData();
    }

// ==================================== // FACTORY // ==================================== //

    private PacketHandleData() { }


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

}
