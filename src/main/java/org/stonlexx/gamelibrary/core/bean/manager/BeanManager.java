package org.stonlexx.gamelibrary.core.bean.manager;

import lombok.Getter;
import lombok.NonNull;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import org.stonlexx.gamelibrary.GameLibrary;
import org.stonlexx.gamelibrary.core.bean.*;
import org.stonlexx.gamelibrary.core.bean.exception.BeanException;
import org.stonlexx.gamelibrary.core.configuration.property.LibraryCoreProperty;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

public final class BeanManager {

    @Getter
    private final Map<String, BeanObject<?>> beanMap = new HashMap<>();


    /**
     * Сканировать все классы в указанном
     * пакейдже для поиска и регистрации новых бинов
     *
     * @param beanScanPackage - основной пакейдж для поиска
     */
    public void scanPackages(String beanScanPackage) {
        List<ClassLoader> classLoadersList = new LinkedList<>();

        classLoadersList.add(ClasspathHelper.contextClassLoader());
        classLoadersList.add(ClasspathHelper.staticClassLoader());

        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .setUrls(ClasspathHelper.forClassLoader(classLoadersList.toArray(new ClassLoader[0])))

                .setScanners(new SubTypesScanner(false), new ResourcesScanner())
                .filterInputsBy(new FilterBuilder().include(FilterBuilder.prefix(beanScanPackage))));

        Set<Class<?>> classSet = reflections.getSubTypesOf(Object.class);

        for (Class<?> beanClass : classSet) {
            Bean beanAnnotation = beanClass.getDeclaredAnnotation(Bean.class);

            if (beanAnnotation == null) {
                continue;
            }

            registerBean(beanClass);
        }
    }

    /**
     * Зарегистрировать новый бин
     *
     * @param beanClass - класс бина
     */
    public void registerBean(@NonNull Class<?> beanClass) {
        Bean beanAnnotation = beanClass.getDeclaredAnnotation(Bean.class);

        if (beanAnnotation == null) {
            return;
        }

        if (beanAnnotation.value().isEmpty()) {

            String beanName = beanClass.getSimpleName();
            beanName = String.valueOf(beanName.charAt(0)).toLowerCase() + beanName.substring(1);

            registerBean(beanClass, beanName);
            return;
        }

        registerBean(beanClass, beanAnnotation.value());
    }

    /**
     * Зарегистрировать новый бин
     *
     * @param beanClass - класс бина
     * @param beanName - кастомное имя бина
     */
    public void registerBean(@NonNull Class<?> beanClass,
                             @NonNull String beanName) {

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
    public <T> void registerBean(@NonNull Class<T> beanClass,
                                 @NonNull String beanName,
                                 @NonNull EnumBeanScope beanScope) {
        try {
            // initialize bean

            T beanObject = null;
            Constructor<?>[] beanConstructorArray = beanClass.getConstructors();

            if (beanConstructorArray.length <= 1) {
                beanObject = beanClass.getDeclaredConstructor().newInstance();

            } else {
                int constructorCounter = 0;

                for (Constructor<?> beanConstructor : beanConstructorArray) {
                    QualifierConstructor qualifierConstructor = beanConstructor.getAnnotation(QualifierConstructor.class);

                    if (qualifierConstructor != null) {
                        beanObject = newBeanByConstructor(beanClass, beanConstructor);
                        break;
                    }

                    constructorCounter++;
                    if (constructorCounter > 1) {
                        throw new BeanException(beanName + " - constructors length > 1, please qualifier a required constructor by @QualifierConstructor");
                    }
                }
            }

            // initialize fields
            for (Field field : beanClass.getDeclaredFields()) {
                field.setAccessible(true);

                BeanQualifier beanQualifier = field.getDeclaredAnnotation(BeanQualifier.class);
                BeanValue beanValue = field.getDeclaredAnnotation(BeanValue.class);

                if (beanQualifier != null) {
                    BeanObject<?> qualifierBean = getBean(beanQualifier.value());

                    if (qualifierBean == null) {
                        throw new BeanException(beanName + " - qualifier bean(" + beanQualifier.value() + ") returns null");
                    }

                    field.set(beanObject, qualifierBean.getBeanObject());
                }

                if (beanValue != null) {
                    String value = beanValue.value();

                    if (value.startsWith("${")) {
                        String propertyKey = value.substring(2, value.length() - 1);

                        for (LibraryCoreProperty coreProperty : GameLibrary.getInstance().getPropertyManager().getCorePropertyMap().values()) {
                            String propertyValue = coreProperty.getProperty(propertyKey);

                            if (propertyValue == null) {
                                continue;
                            }

                            field.set(beanObject, field.getType().cast(propertyValue));
                        }

                    } else {
                        field.set(beanObject, field.getType().cast(value));
                    }
                }
            }

            // register bean
            registerBean(beanClass, beanName, beanScope, beanObject);
        }

        catch (Throwable exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Зарегистрировать новый бин
     *
     * @param beanClass - класс бина
     * @param beanName - кастомное имя бина
     * @param beanScope - видимость бина
     */
    public <T> void registerBean(@NonNull Class<T> beanClass,
                                 @NonNull String beanName,
                                 @NonNull EnumBeanScope beanScope,
                                 @NonNull T beanObject) {

        // cache & invoke @InitMethod bean

        beanMap.put(beanName, new BeanObject<>(beanObject, beanClass, beanName, beanScope));
        beanInit(beanName);
    }

    private <T> T newBeanByConstructor(@NonNull Class<T> beanClass,
                                       @NonNull Constructor<?> beanConstructor) throws Exception {

        Collection<Object> constructorParameters = new ArrayList<>();

        int parameterCounter = 0;
        for (Parameter constructorParameter : beanConstructor.getParameters()) {
            BeanObject<?> beanObject = getBean(constructorParameter.getType());

            if (beanObject == null) {
                BeanQualifier beanQualifier = constructorParameter.getDeclaredAnnotation(BeanQualifier.class);

                if (beanQualifier == null) {
                    throw new BeanException("has parameters not instanceof a Bean");
                }

                beanObject = getBean(beanQualifier.value());
            }

            constructorParameters.add(beanObject.getBeanObject());
            parameterCounter++;
        }

        return (T) beanConstructor.newInstance(constructorParameters.toArray());
    }

    private void beanAnnotationMethodInvoke(@NonNull String beanName,
                                            @NonNull Class<? extends Annotation> annotationClass) {

        BeanObject<?> beanObject = getBean(beanName);

        try {
            for (Method method : beanObject.getObjectType().getDeclaredMethods()) {

                if (method.getDeclaredAnnotation(annotationClass) != null) {
                    method.invoke(beanObject.getBeanObject());

                    break;
                }
            }
        }

        catch (Throwable exception) {
            exception.printStackTrace();
        }
    }

    private void beanInit(@NonNull String beanName) {
        beanAnnotationMethodInvoke(beanName, InitMethod.class);
    }

    private void beanDestroy(@NonNull String beanName) {
        beanAnnotationMethodInvoke(beanName, DestroyMethod.class);
    }

    /**
     * Получить бин по его имени и типу класса
     *
     * @param beanName - имя бина
     * @param beanType - тип бина
     */
    public <T> T getBean(@NonNull String beanName,
                         @NonNull Class<T> beanType) {

        BeanObject<T> beanObject = (BeanObject<T>) getBean(beanName);

        if (beanObject == null) {
            return null;
        }

        return beanObject.getBeanObject();
    }

    /**
     * Получить бин по его классу
     *
     * @param beanClass - имя бина
     */
    public <T> BeanObject<T> getBean(Class<T> beanClass) {
        BeanObject<T> beanObject = null;

        int equalBeanCounter = 0;
        for (BeanObject<?> registeredBean : beanMap.values()) {

            if (!registeredBean.getObjectType().isAssignableFrom(beanClass)) {
                continue;
            }

            beanObject = (BeanObject<T>) registeredBean;
            equalBeanCounter++;

            if (equalBeanCounter > 1) {
                throw new BeanException("searched 2 beans by " + beanClass.getName() + ", qualifier required bean by @BeanQualifier");
            }
        }

        return beanObject;
    }

    /**
     * Получить бин по его имени
     *
     * @param beanName - имя бина
     */
    public BeanObject<?> getBean(String beanName) {
        BeanObject<?> beanObject = beanMap.get(beanName);

        if (beanObject == null) {
            return null;
        }

        if (beanObject.getBeanScope().equals(EnumBeanScope.PROTOTYPE)) {
            beanInit(beanName);
        }

        return beanObject;
    }


    public void destroy() {
        Collection<BeanObject<?>> beanCollection = beanMap.values();

        for (BeanObject<?> beanObject : beanCollection) {
            beanDestroy(beanObject.getBeanName());
            beanObject = null; //хз, поможет ли, но надеюсь, что оно удалится из памяти
        }
    }
}
