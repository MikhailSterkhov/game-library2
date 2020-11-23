package org.stonlexx.gamelibrary.common.event;

import lombok.Getter;

import java.lang.reflect.Method;
import java.util.*;

public final class EventManager {

    private final Map<String, List<Method>> eventMethodsMap = new HashMap<>();

    @Getter
    private final List<EventListener> listenerList = new ArrayList<>();


    /**
     * Регистрация листенера с ивентами под
     * уникальным ID
     *
     * @param eventListener - листенер
     */
    public void registerListener(EventListener eventListener) {
        if (listenerList.contains(eventListener)) {
            return;
        }

        listenerList.add(eventListener);
        registerEvents(eventListener);
    }

    /**
     * Регистрация ивентов, их кеширование в мапу
     *
     * @param eventListener - листенер
     */
    private void registerEvents(EventListener eventListener) {
        List<Method> methodList = Arrays.asList(eventListener.getClass().getMethods());

        methodList.forEach(method -> {
            if (method.getDeclaredAnnotation(EventHandler.class) == null) {
                return;
            }

            Class<?>[] parameterTypes = method.getParameterTypes();

            if (parameterTypes.length != 1) {
                return;
            }

            Class<?> eventClass = parameterTypes[0];

            if (!eventClass.getSuperclass().equals(Event.class)) {
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
     * @param event - ивент
     */
    public void callEvent(Event event) {
        List<Method> methods = eventMethodsMap.get(event.getClass().getSimpleName());

        if (methods == null) {
            return;
        }

        for (Method method : methods) {
            for (EventListener eventListener : listenerList) {
                try {
                    if (eventListener.getClass().getMethod(method.getName(), method.getParameterTypes()) == null) {
                        continue;
                    }

                    method.invoke(eventListener, event);
                } catch (Exception ignored) { }
            }
        }
    }

}
