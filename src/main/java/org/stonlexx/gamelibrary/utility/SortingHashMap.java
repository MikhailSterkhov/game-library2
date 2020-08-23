package org.stonlexx.gamelibrary.utility;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Getter
public class SortingHashMap<K, V>
        extends LinkedHashMap<K, V>
        implements Map<K, V> {

    transient final boolean reversed;
    transient final Function<V, Integer> keyExtractor;


    public SortingHashMap<K, V> sortableMap() {
        SortingHashMap<K, V> sortingHashMap = new SortingHashMap<>(reversed, keyExtractor);

        Set<Entry<K, V>> entrySet = entrySet().stream()
                .sorted(Comparator.comparing(entry -> keyExtractor.apply(entry.getValue())))
                .collect(Collectors.toCollection(LinkedHashSet::new));

        for (Entry<K, V> mapEntry : entrySet) {
            sortingHashMap.put(mapEntry.getKey(), mapEntry.getValue());
        }

        return sortingHashMap;
    }


    public Stream<K> keyStream() {
        return keySet().stream();
    }

    public Stream<V> valueStream() {
        return values().stream();
    }

}
