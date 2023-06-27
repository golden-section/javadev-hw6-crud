package org.gs;

import org.flywaydb.core.Flyway;
import org.gs.props.PropertiesUtil;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Statement;

public class Database {
    private static final Database INSTANCE = new Database();
    private static final String DB_URL_KEY = "db.url";
    private final Connection connection;

    private Database() {
        try {
            String dbUrl = PropertiesUtil.getPropertyValue(DB_URL_KEY);
            connection = DriverManager.getConnection(dbUrl);
            flywayMigration(dbUrl);
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

    public static Database getInstance(){
        return INSTANCE;
    }

    public Connection getConnection() {
        return connection;
    }

    public void executeUpdate(String query) {
        try(Statement statement = INSTANCE.getConnection().createStatement()) {
            statement.executeUpdate(query);
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }

    private void flywayMigration(String dbConnectionUrl) {
        Flyway flyway = Flyway.configure().dataSource(dbConnectionUrl, null, null).load();
        flyway.migrate();
    }

    public void close() {
        try {
            connection.close();
        } catch (SQLException exception) {
            throw new RuntimeException(exception);
        }
    }
}