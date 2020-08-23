package org.stonlexx.gamelibrary.core.event;

import lombok.Getter;
import org.stonlexx.gamelibrary.core.event.exception.EventException;
import org.stonlexx.gamelibrary.core.event.handler.EventHandler;
import org.stonlexx.gamelibrary.core.event.listener.GameListener;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public final class EventManager {

    private final Map<String, List<Method>> eventMethodsMap = new HashMap<>();

    @Getter
    private final Map<Integer, GameListener> listenerMap = new HashMap<>();


    /**
     * Регистрация листенера с ивентами под
     * уникальным ID
     *
     * @param listenerId   - уникальный ID листенера
     * @param gameListener - листенер
     */
    public void registerListener(int listenerId, GameListener gameListener) {
        if (listenerMap.containsKey(listenerId)) {
            throw new EventException("Под данным ID (%s) уже зарегистрирован другой листенер", listenerId);
        }

        listenerMap.put(listenerId, gameListener);
        registerEvents(gameListener);
    }

    /**
     * Получить листенер по его ID
     *
     * @param listenerId - ID листенера
     */
    public GameListener getListener(int listenerId) {
        return listenerMap.get(listenerId);
    }

    /**
     * Регистрация ивентов, их кеширование в мапу
     *
     * @param gameListener - листенер
     */
    private void registerEvents(GameListener gameListener) {
        List<Method> methodList = Arrays.asList(gameListener.getClass().getMethods());

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
            for (GameListener gameListener : listenerMap.values()) {
                try {
                    method.invoke(gameListener, coreEvent);
                } catch (IllegalAccessException | InvocationTargetException ignored) {
                }
            }
        }
    }

}
