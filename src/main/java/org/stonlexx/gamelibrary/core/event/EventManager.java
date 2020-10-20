package org.stonlexx.gamelibrary.core.event;

import lombok.Getter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public final class EventManager {

    private final Map<String, List<Method>> eventMethodsMap = new HashMap<>();

    @Getter
    private final Map<Integer, CoreEventListener> listenerMap = new HashMap<>();


    /**
     * Регистрация листенера с ивентами под
     * уникальным ID
     *
     * @param listenerId   - уникальный ID листенера
     * @param coreEventListener - листенер
     */
    public void registerListener(int listenerId, CoreEventListener coreEventListener) {
        if (listenerMap.containsKey(listenerId)) {
            throw new EventException("Под данным ID (%s) уже зарегистрирован другой листенер", listenerId);
        }

        listenerMap.put(listenerId, coreEventListener);
        registerEvents(coreEventListener);
    }

    /**
     * Получить листенер по его ID
     *
     * @param listenerId - ID листенера
     */
    public CoreEventListener getListener(int listenerId) {
        return listenerMap.get(listenerId);
    }

    /**
     * Регистрация ивентов, их кеширование в мапу
     *
     * @param coreEventListener - листенер
     */
    private void registerEvents(CoreEventListener coreEventListener) {
        List<Method> methodList = Arrays.asList(coreEventListener.getClass().getMethods());

        methodList.forEach(method -> {
            if (method.getDeclaredAnnotation(EventHandler.class) == null) {
                return;
            }

            Class<?>[] parameterTypes = method.getParameterTypes();

            if (parameterTypes.length != 1) {
                return;
            }

            Class<?> eventClass = parameterTypes[0];

            if (!eventClass.getSuperclass().equals(CoreEvent.class)) {
                return;
            }

            List<Method> methods = eventMethodsMap.computeIfAbsent(eventClass.getSimpleName(), f -> new ArrayList<>());
            methods.add(method);

            eventMethodsMap.put(eventClass.getSimpleName(), methods);
        });
    }

    /**
     * Вызывать ивент
     *
     * @param coreEvent - ивент
     */
    public void callEvent(CoreEvent coreEvent) {
        List<Method> methods = eventMethodsMap.get(coreEvent.getClass().getSimpleName());

        if (methods == null) {
            return;
        }

        for (Method method : methods) {
            for (CoreEventListener coreEventListener : listenerMap.values()) {
                try {
                    method.invoke(coreEventListener, coreEvent);
                } catch (IllegalAccessException | InvocationTargetException ignored) {
                }
            }
        }
    }

}
