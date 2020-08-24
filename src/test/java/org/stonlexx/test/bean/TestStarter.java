package org.stonlexx.test.bean;

import org.stonlexx.gamelibrary.GameLibrary;
import org.stonlexx.gamelibrary.core.bean.manager.BeanManager;
import org.stonlexx.gamelibrary.core.configuration.PropertyConfigurationManager;

public class TestStarter {

    public static void main(String[] args) {
        PropertyConfigurationManager coreConfiguration = GameLibrary.getInstance().getPropertyManager();
        coreConfiguration.addPropertyConfiguration(TestStarter.class.getClassLoader(), "test.properties");

        BeanManager beanManager = GameLibrary.getInstance().getBeanManager();
        beanManager.scanPackages("org.stonlexx.test");

        TestPlayer testPlayer = beanManager.getBean(TestPlayer.class).getBeanObject();

        beanManager.destroy();
    }

}
