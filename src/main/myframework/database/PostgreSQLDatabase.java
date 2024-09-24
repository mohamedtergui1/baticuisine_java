package main.myframework.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;

public class PostgreSQLDatabase {
    private static PostgreSQLDatabase instance;
    private Connection connection;
    private final String url;
    private final String user;
    private final String password;
    static String FILE_CONFIG_NAME  = "./.DB_CONFIG" ;
    private Map<String,String> config ;

    private PostgreSQLDatabase() {
        config = ConfigReader.readConfigFile();
        this.url = "jdbc:postgresql://"+config.get("host") + ":" + config.get("port")+ "/" + config.get("db_name");
        this.user = config.get("username");
        this.password = config.get("password");
        connect();
    }

    public static synchronized PostgreSQLDatabase getInstance() {
        if (instance == null) {
            instance = new PostgreSQLDatabase();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

    private void connect() {
        try {
            Class.forName("org.postgresql.Driver");

            connection = DriverManager.getConnection(url, user, password);

        } catch (ClassNotFoundException e) {
            System.out.println("JDBC Driver not found: " + e.getMessage());
        } catch (SQLException e) {
            System.out.println("Connection error: " + e.getMessage());
        }
    }



}
