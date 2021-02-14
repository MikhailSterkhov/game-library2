package org.stonlexx.gamelibrary.common.dependency;

import java.lang.annotation.*;

/**
 * Данная аннотация ставится на переменные, типы
 * которых являются одним из зарегистрированных
 * депенд-классов.
 * </p>
 * Если таких депендов нет, то она будет
 * проигнорирована.
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DependInject {
}
