package org.stonlexx.gamelibrary.core.bean;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface BeanScope {

    EnumBeanScope value() default EnumBeanScope.SINGLETON;
}
