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

}
