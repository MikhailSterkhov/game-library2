package org.stonlexx.test.utility;

import org.stonlexx.gamelibrary.utility.TimedArrayList;

public class TimedArrayListTest {

    public static void main(String[] args) {
        TimedArrayList<Integer> timedList = new TimedArrayList<>();

        // Всего 4 элеемента
        timedList.add(228); //index: 0
        timedList.add(822); //index: 1
        timedList.add(1337); //index: 2
        timedList.add(-128); //index: 3

        System.out.println("Время добавления элемента под индексом 1: " + timedList.getElementTime(1));
        System.out.println("Время добавления элемента под индексом 3: " + timedList.getElementTime(3));
    }
}
