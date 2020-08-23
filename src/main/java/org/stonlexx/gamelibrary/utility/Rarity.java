package org.stonlexx.gamelibrary.utility;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Rarity {

    NONE(0, 0, 0),
    COMMON(1, 1, 0),
    RARE( 2, 0.27, 0.02),
    EPIC(3, 0.07, 0.03),
    LEGENDARY(4, 0.025, 0.025);

    private final int rarity;

    private final double chance;
    private final double changeChance;

}
