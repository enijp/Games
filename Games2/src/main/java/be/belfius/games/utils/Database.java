package be.belfius.games.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {

    public Connection createConnection() throws SQLException {
        return DriverManager.getConnection(Helper.loadPropertiesFile().getProperty("db.url")
                , Helper.loadPropertiesFile().getProperty("db.username")
                , Helper.loadPropertiesFile().getProperty("db.password"));
    }
}



