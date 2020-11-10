package org.stonlexx.gamelibrary.core.event;

import lombok.Getter;

import java.lang.reflect.Method;
import java.util.*;

public final class EventManager {

    private final Map<String, List<Method>> eventMethodsMap = new HashMap<>();

    @Getter
    private final List<CoreEventListener> listenerList = new ArrayList<>();


    /**
     * Регистрация листенера с ивентами под
     * уникальным ID
     *
     * @param coreEventListener - листенер
     */
    public void registerListener(CoreEventListener coreEventListener) {
        if (listenerList.contains(coreEventListener)) {
            return;
        }

        listenerList.add(coreEventListener);
        registerEvents(coreEventListener);
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
            for (CoreEventListener coreEventListener : listenerList) {
                try {
                    if (coreEventListener.getClass().getMethod(method.getName(), method.getParameterTypes()) == null) {
                        continue;
                    }

                    method.invoke(coreEventListener, coreEvent);
                } catch (Exception ignored) { }
            }
        }
    }

}
