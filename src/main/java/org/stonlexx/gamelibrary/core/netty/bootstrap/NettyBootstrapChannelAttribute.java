package org.stonlexx.gamelibrary.core.netty.bootstrap;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class NettyBootstrapChannelAttribute<T> {

    private final String attributeName;

    private final T attributeObject;


// ================================================================================================================== //

    /**
     * Factory метод для создания атрибута, который
     * будет хранится в канале, инициализированным bootstrap`ом
     *
     * @param attributeName - имя атрибута
     * @param attributeObject - объект атрибута
     */
    public static <T> NettyBootstrapChannelAttribute<T> create(@NonNull String attributeName,
                                                               @NonNull T attributeObject) {

        return new NettyBootstrapChannelAttribute<>(attributeName, attributeObject);
    }

// ================================================================================================================== //

}
