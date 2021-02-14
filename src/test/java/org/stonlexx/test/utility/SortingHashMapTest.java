package org.stonlexx.test.utility;

import org.stonlexx.gamelibrary.utility.SortingHashMap;

public class SortingHashMapTest {

    public static void main(String[] args) {
        SortingHashMap<Integer, String> sortingHashMap = new SortingHashMap<>(SortingHashMap::byValue);

        // Добавляем элмементы с безобразной сортировкой ключей
        sortingHashMap.put(5, "?");
        sortingHashMap.put(2, "точно");
        sortingHashMap.put(1, "Это");
        sortingHashMap.put(4, "работать");
        sortingHashMap.put(3, "будет");

        // Сортируем по значению мапы, не переворачивая ее строки
        sortingHashMap = sortingHashMap.sortableMap(false);

        for (String mapValue : sortingHashMap.values()) {
            System.out.print(mapValue + " ");
        }

        System.out.println();
    }
}
