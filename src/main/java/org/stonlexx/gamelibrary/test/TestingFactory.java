package org.stonlexx.gamelibrary.test;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

@Getter
@RequiredArgsConstructor
public abstract class TestingFactory<T extends Annotation> {

    protected String mainPackage;

    protected Collection<Method> testMethodCollection;
    protected Collection<Class<?>> classesWithTestMethodsCollection;

    protected final Class<T> annotationType;


    /**
     * Установить главный Package проекта,
     * при анализе которого будут искаться
     * тестовые методы
     *
     * @param mainPackage - главный Package проекта
     */
    public void setMainPackage(@NonNull String mainPackage) {
        List<ClassLoader> classLoadersList = new LinkedList<>();

        classLoadersList.add(ClasspathHelper.contextClassLoader());
        classLoadersList.add(ClasspathHelper.staticClassLoader());

        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setUrls(ClasspathHelper.forClassLoader(classLoadersList.toArray(new ClassLoader[0])))

                .setScanners(new SubTypesScanner(false), new ResourcesScanner())
                .filterInputsBy(new FilterBuilder().include(FilterBuilder.prefix(mainPackage))));

        this.classesWithTestMethodsCollection = new ArrayList<>();

        this.testMethodCollection = reflections.getMethodsAnnotatedWith(annotationType);
        this.mainPackage = mainPackage;

        for (Method testMethod : testMethodCollection) {
            if (classesWithTestMethodsCollection.contains(testMethod.getDeclaringClass())) {
                continue;
            }

            classesWithTestMethodsCollection.add(testMethod.getDeclaringClass());
        }
    }

    /**
     * Применить все тестовые методы указанного класса,
     * обработав их через Consumer
     *
     * @param classWithTestMethods - класс, из которого черпать тест методы
     * @param testMethodConsumer - обработчик тестовых методов
     */
    public abstract void executeTests(@NonNull Class<?> classWithTestMethods, Consumer<Method> testMethodConsumer);

    /**
     * Применить все тестовые методы, обработав
     * их через Consumer
     *
     * @param testMethodConsumer - обработчик тестовых методов
     */
    public void executeAllTests(Consumer<Method> testMethodConsumer) {
        for (Class<?> classWithTestMethods : classesWithTestMethodsCollection) {
            executeTests(classWithTestMethods, testMethodConsumer);
        }
    }
}
