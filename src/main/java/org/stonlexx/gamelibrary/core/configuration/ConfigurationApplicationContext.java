package org.stonlexx.gamelibrary.core.configuration;

import lombok.Getter;
import lombok.NonNull;
import org.stonlexx.gamelibrary.core.bean.Bean;
import org.stonlexx.gamelibrary.core.bean.BeanObject;
import org.stonlexx.gamelibrary.core.bean.BeanScope;
import org.stonlexx.gamelibrary.core.bean.EnumBeanScope;

import java.io.DataInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.*;

public class ConfigurationApplicationContext {

    @Getter
    private final Map<String, BeanObject<?>> beanMap = new HashMap<>();


    /**
     * Сканировать все классы в указанном
     * пакейдже для поиска и регистрации новых бинов
     *
     * @param classLoader - загрузчик (хранилище) классов
     * @param beanScanPackage - основной пакейдж для поиска
     */
    public void scanPackages(ClassLoader classLoader, String beanScanPackage) {
        List<Class<?>> classList = getPackageClasses(classLoader, beanScanPackage);

        for (Class<?> beanClass : classList) {
            Bean beanAnnotation = beanClass.getDeclaredAnnotation(Bean.class);

            if (beanAnnotation == null) {
                continue;
            }

            if (beanAnnotation.value().isEmpty()) {
                registerBean(beanClass);

                continue;
            }

            registerBean(beanClass, beanAnnotation.value());
        }
    }

    /**
     * Получить список классов из указанного и внутренних пакейджей
     * для определенного хранилища классов
     *
     * @param classLoader - загрузчик (хранилище) классов
     * @param beanScanPackage - пакейдж
     */
    public List<Class<?>> getPackageClasses(ClassLoader classLoader, String beanScanPackage) {
        List<Class<?>> classList = new ArrayList<>();
        URL packageUrl = classLoader.getResource(beanScanPackage);

        if (packageUrl == null) {
            return classList;
        }

        try {
            DataInputStream dataInputStream = new DataInputStream((InputStream) packageUrl.getContent());

            if (!dataInputStream.markSupported()) {
                return classList;
            }

            String line;
            while ((line = dataInputStream.readLine()) != null) {

                if (!line.endsWith(".class")) {
                    classList.addAll(getPackageClasses(classLoader, line));

                    continue;
                }

                Class<?> loadedClass = classLoader.loadClass(line.substring(0, line.length() - ".class".length()));
                classList.add(loadedClass);
            }
        }

        catch (Exception exception) {
            exception.printStackTrace();
        }

        return classList;
    }


    /**
     * Зарегистрировать новый бин
     *
     * @param beanClass - класс бина
     */
    public void registerBean(Class<?> beanClass) {
        String beanName = beanClass.getSimpleName();
        beanName = String.valueOf(beanName.indexOf(0)).toLowerCase() + beanName.substring(1);

        registerBean(beanClass, beanName);
    }

    /**
     * Зарегистрировать новый бин
     *
     * @param beanClass - класс бина
     * @param beanName - кастомное имя бина
     */
    public void registerBean(Class<?> beanClass, String beanName) {
        BeanScope beanScope = beanClass.getDeclaredAnnotation(BeanScope.class);

        registerBean(beanClass, beanName, beanScope == null ? EnumBeanScope.SINGLETON : beanScope.value());
    }

    /**
     * Зарегистрировать новый бин
     *
     * @param beanClass - класс бина
     * @param beanName - кастомное имя бина
     * @param beanScope - видимость бина
     */
    public void registerBean(Class<?> beanClass, String beanName, EnumBeanScope beanScope) {

    }

    /**
     * Получить бин по его имени и типу класса
     *
     * @param beanName - имя бина
     * @param beanType - тип бина
     */
    public <T> T getBean(@NonNull String beanName,
                         @NonNull Class<T> beanType) {

        return (T) getBean(beanName).getBeanObject();
    }

    /**
     * Получить бин по его имени и типу класса
     *
     * @param beanName - имя бина
     */
    public BeanObject<?> getBean(String beanName) {
        BeanObject<?> beanObject = beanMap.get(beanName.toLowerCase());
        beanObject.initMethod();

        return beanObject;
    }


    public void destroy() {
        Collection<BeanObject<?>> beanCollection = beanMap.values();

        for (BeanObject<?> beanObject : beanCollection) {
            beanObject.destroyMethod();

            beanMap.remove(beanObject.getBeanName().toLowerCase());
            beanObject = null; //хз, поможет ли, но надеюсь, что оно удалится из памяти
        }
    }
}
