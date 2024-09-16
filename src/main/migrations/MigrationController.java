package main.migrations;

import java.sql.Connection;
import main.migrations.filesloader.FilesLoader;
import main.migrations.tableenum.CreateTableAndEnum;

import java.sql.DriverManager;
import java.sql.SQLException;

public class MigrationController {
    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5432/baticuisine_java";
        String user = "myuser";
        String password = "mypassword";


        try (Connection connection = (Connection) DriverManager.getConnection(url, user, password)) {
            for (Class<?> clazz : FilesLoader.getEnums()) {
                if( clazz.isEnum())
                {
                    System.out.println(CreateTableAndEnum.generateCreateEnumQuery((Class<? extends Enum<?>>) clazz));

                    CreateTableAndEnum.createEnum((Class<? extends Enum<?>>) clazz, connection);
                }
                else
                    System.out.println(clazz.getName() + " is not enum");

            }
            for (Class<?> clazz : FilesLoader.getEntities()) {
                System.out.println(CreateTableAndEnum.generateCreateTableQuery(clazz));

                boolean success = CreateTableAndEnum.createTable(clazz, connection);

                if (!success) {
                    System.out.println("Failed to create table for entity: " + clazz.getName());
                }
            }
        } catch (SQLException e) {
            System.err.println("Connection error:");
            e.printStackTrace();
        }
    }
}
