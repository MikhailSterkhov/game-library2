package org.stonlexx.gamelibrary.common.dependency;

import lombok.NonNull;
import lombok.SneakyThrows;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.*;

public final class DependencyInjection {

    protected static final Map<Class<?>, Object> dependencyMap = new HashMap<>();

    /**
     * Регистрация объекта депенда, который
     * имеет аннотацию {@link Depend}
     *
     * @param objectDepend - депенд
     */
    public void registerDepend(@NonNull Object objectDepend) {
        if (dependencyMap.containsKey(objectDepend.getClass())) {
            return;
        }

        dependencyMap.put(objectDepend.getClass(), objectDepend);
    }

    /**
     * Инициализировать переменные указанного объекта
     * с аннотацией {@link DependInject}, используя тип переменой
     *
     * @param object - объект с переменными
     */
    public void injectDepends(@NonNull Object object) {
        for (Field field : object.getClass().getDeclaredFields()) {
            injectFieldDepends(object, field);
        }
    }

    /**
     * Инициализировать переменную для объекта
     * с аннотацией {@link DependInject}, используя тип переменой
     *
     * @param object - объект с переменными
     * @param field  - переменная
     */
    @SneakyThrows
    private void injectFieldDepends(@NonNull Object object, @NonNull Field field) {

        if (field.getDeclaredAnnotation(DependInject.class) == null) {
            return;
        }

        Object dependObject = dependencyMap.get(field.getType());

        if (dependObject == null) {
            return;
        }

        field.setAccessible(true);
        field.set(object, dependObject);

        if (!dependencyMap.containsKey(object.getClass())) {
            dependencyMap.put(object.getClass(), object);
        }
    }

    /**
     * Сканировать все классы в указанном
     * пакейдже для поиска и регистрации новых зависимосстей
     *
     * @param packageToScan - основной пакейдж для поиска
     */
    @SneakyThrows
    public void scanDepends(@NonNull String packageToScan) {
        List<ClassLoader> classLoadersList = new LinkedList<>();

        classLoadersList.add(ClasspathHelper.contextClassLoader());
        classLoadersList.add(ClasspathHelper.staticClassLoader());

        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setUrls(ClasspathHelper.forClassLoader(classLoadersList.toArray(new ClassLoader[0])))

                .setScanners(new SubTypesScanner(false), new ResourcesScanner())
                .filterInputsBy(new FilterBuilder().include(FilterBuilder.prefix(packageToScan))));

        Set<Class<?>> classSet = reflections.getSubTypesOf(Object.class);
        for (Class<?> objectClass : classSet) {

            Object object = null;
            { // create the object instance from the object class

                Constructor<?> emptyConstructor = objectClass.getDeclaredConstructor();

                if (emptyConstructor.getParameterCount() == 0) {
                    emptyConstructor.setAccessible(true);

                    object = emptyConstructor.newInstance();
                }
            }

            if (object == null) {
                throw new RuntimeException(objectClass.getName() + " is`nt have empty constructor");
            }

            // check depend class
            if (objectClass.getDeclaredAnnotation(Depend.class) != null) {
                registerDepend(object);
            }

            // check depend inject in fields
            for (Field field : objectClass.getDeclaredFields()) {

                if (field.getDeclaredAnnotation(DependInject.class) != null) {
                    injectFieldDepends(object, field);
                }
            }
        }
    }

    public <T> T getInitializedObject(@NonNull Class<T> objectClass) {
        return ((T) dependencyMap.get(objectClass));
    }

}
