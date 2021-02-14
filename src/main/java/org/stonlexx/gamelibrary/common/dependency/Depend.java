package org.stonlexx.gamelibrary.common.dependency;

import java.lang.annotation.*;

/**
 * Данная аннотация нужна только в том случае,
 * если при включении программы идет скан пакейджей
 * на обнаружение депенд-классов
 * </p>
 * Так алгоритму будет проще найти его.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Depend {
}
