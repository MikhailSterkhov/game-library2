package org.stonlexx.gamelibrary.common.bean;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface BeanScope {

    EnumBeanScope value() default EnumBeanScope.SINGLETON;
}
