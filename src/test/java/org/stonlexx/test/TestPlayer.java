package org.stonlexx.test;

import lombok.Getter;
import org.stonlexx.gamelibrary.core.bean.Bean;
import org.stonlexx.gamelibrary.core.bean.BeanQualifier;

@Getter
@Bean
public class TestPlayer {

    @BeanQualifier("testObject")
    private TestObject testObject;


    public void test() {
        System.out.println(testObject.getString());
    }

}
