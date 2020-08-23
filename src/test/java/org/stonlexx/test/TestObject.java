package org.stonlexx.test;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.stonlexx.gamelibrary.core.bean.Bean;
import org.stonlexx.gamelibrary.core.bean.BeanValue;

@Bean
@RequiredArgsConstructor
@Getter
public class TestObject {

    @BeanValue("Миша долбаеб")
    private final String string;
}
