package be.belfius.games.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Helper {
    public static Properties loadPropertiesFile() {
        Properties prop = new Properties();
        try (InputStream resourceAsStream = 
        		Helper.class.getClassLoader().getResourceAsStream("config.properties")) {
            prop.load(resourceAsStream);
        } catch (IOException e) {
            System.err.println("Unable to load properties file : " + "development.properties");
        }
        return prop;
    }
}
