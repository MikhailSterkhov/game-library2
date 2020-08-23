package org.stonlexx.test;

import org.stonlexx.gamelibrary.GameLibrary;
import org.stonlexx.gamelibrary.core.configuration.ConfigurationApplicationContext;
import org.stonlexx.gamelibrary.core.configuration.LibraryCoreConfiguration;
import org.stonlexx.test.bean.TestPlayer;

public class TestStarter {

    public static void main(String[] args) {
        GameLibrary.getInstance().setLogger(new TestLogger());

        LibraryCoreConfiguration coreConfiguration = GameLibrary.getInstance().getLibraryCore().getCoreConfiguration();
        coreConfiguration.addPropertyConfiguration(TestStarter.class.getClassLoader(), "test.properties");

        ConfigurationApplicationContext applicationContext = coreConfiguration.getApplicationContext();
        applicationContext.scanPackages("org.stonlexx.test");

        TestPlayer testPlayer = applicationContext.getBean("testPlayer", TestPlayer.class);

        applicationContext.destroy();
    }

}
