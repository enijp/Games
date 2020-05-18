package be.belfius.games.repository;

import java.sql.Connection;
import java.sql.SQLException;

import org.junit.rules.ExternalResource;

import be.belfius.games.utils.Database;

public class DatabaseRule extends ExternalResource {
    private Connection connection;

    @Override
    protected void before() throws Throwable {
        connection = new Database().createConnection();
    }

    @Override
    protected void after() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }
}