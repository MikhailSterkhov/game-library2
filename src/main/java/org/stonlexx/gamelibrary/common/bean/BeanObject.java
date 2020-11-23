package org.stonlexx.gamelibrary.common.bean;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.Serializable;

@Getter
@RequiredArgsConstructor
public class BeanObject<T> implements Serializable {

    private final T beanObject;

    private final Class<? extends T> objectType;

    private final String beanName;
    private final EnumBeanScope beanScope;


    /**
     * Метод инициализации бина, как
     *  java-объекта
     */
    public void initMethod() { }


    /**
     * Метод удаление из памяти бина,
     * как java-объект
     */
    public void destroyMethod() { }

}
