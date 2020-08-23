package org.stonlexx.test.bean;

import lombok.Getter;
import lombok.Setter;
import org.stonlexx.gamelibrary.core.bean.Bean;
import org.stonlexx.gamelibrary.core.bean.BeanValue;

@Bean
@Getter
public class TestObject implements ITestObject {

    @BeanValue("${testObject.string}")
    @Setter
    private String string;
}
