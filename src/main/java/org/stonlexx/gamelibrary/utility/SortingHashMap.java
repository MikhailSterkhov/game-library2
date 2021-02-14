package org.stonlexx.gamelibrary.utility;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Getter
public class SortingHashMap<K, V>
        extends LinkedHashMap<K, V>
        implements Map<K, V> {

    transient final BiFunction<SortingHashMap<K, V>, V, Integer> keyExtractor;


    public SortingHashMap<K, V> sortableMap(boolean reversed) {
        SortingHashMap<K, V> sortingHashMap = new SortingHashMap<>(keyExtractor);

        Set<Entry<K, V>> entrySet = entrySet().stream()
                .sorted(Comparator.comparing(entry -> keyExtractor.apply(this, entry.getValue())))
                .collect(Collectors.toCollection(LinkedHashSet::new));

        if (reversed) {
            entrySet = entrySet().stream()
                    .sorted(Comparator.comparing(entry -> keyExtractor.apply(this, ((Entry<K, V>)entry).getValue())).reversed())
                    .collect(Collectors.toCollection(LinkedHashSet::new));
        }

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

    public K byValue(V value) {
        for (K key : keySet()) {
            if (get(key).equals(value)) {
                return key;
            }
        }

        return null;
    }

}
