package main.migrations;

import main.migrations.filesloader.FilesLoader;
import main.migrations.table.CreateTable;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MigrationController {
    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5432/baticuisine_java";
        String user = "myuser";
        String password = "mypassword";
        for (Class<?> clazz : FilesLoader.getEntities()) {
            System.out.println(CreateTable.generateCreateTableQuery(clazz));

        }
        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            for (Class<?> clazz : FilesLoader.getEntities()) {
                boolean success = CreateTable.createTable(clazz, connection);
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
