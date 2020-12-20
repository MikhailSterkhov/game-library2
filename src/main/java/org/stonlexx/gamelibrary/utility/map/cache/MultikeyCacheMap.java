package org.stonlexx.gamelibrary.utility.map.cache;

import org.stonlexx.gamelibrary.utility.map.MultikeyMap;

public interface MultikeyCacheMap<I> extends MultikeyMap<I> {

    void cleanUp();
}
