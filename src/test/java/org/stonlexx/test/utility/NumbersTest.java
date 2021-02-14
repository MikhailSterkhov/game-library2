package org.stonlexx.test.utility;

import org.stonlexx.gamelibrary.utility.NumberUtil;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class NumbersTest {

    public static void main(String[] args) {

        // Форматирование и правильное написание суффиксов к числам
        System.out.println( NumberUtil.formatting(1, "корова", "коровы", "коров") );
        System.out.println( NumberUtil.formatting(52, "корова", "коровы", "коров") );
        System.out.println( NumberUtil.formatting(111, "корова", "коровы", "коров") );
        System.out.println();

        // Разделение числа на разряды
        System.out.println( NumberUtil.spaced(1000000, '.') );
        System.out.println( NumberUtil.spaced(5234534, '.') );
        System.out.println( NumberUtil.spaced(111, '.') );
        System.out.println();

        // Преобразование времени в формате 1d, 5s, 44ms в 1 день, 5 секунд, 44 миллисекунды в определенную единицу времени
        System.out.println( NumberUtil.parseTimeToMillis("2m", TimeUnit.SECONDS) );
        System.out.println( NumberUtil.parseTimeToMillis("30s", TimeUnit.MILLISECONDS) );
        System.out.println( NumberUtil.parseTimeToMillis("1ms", TimeUnit.NANOSECONDS) );
        System.out.println();

        // Массив чисел в рационе
        System.out.println( Arrays.toString(NumberUtil.toManyArray(5, 15)) );
        System.out.println();

        // Форматирование секунд/миллисекунд в конкретизированное выстроенное время
        System.out.println( NumberUtil.getTime(41234) );
        System.out.println( NumberUtil.getTime(83283724692L) );
    }

}
