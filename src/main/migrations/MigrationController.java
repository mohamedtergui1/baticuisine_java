package main.migrations;

import java.sql.Connection;
import main.migrations.filesloader.FilesLoader;
import main.migrations.tableenum.CreateTableAndEnum;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class MigrationController {
    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5432/baticuisine_java";
        String user = "myuser";
        String password = "mypassword";


        try (Connection connection = (Connection) DriverManager.getConnection(url, user, password)) {
            Set<Class<?>> entities =  FilesLoader.getEntities();
            List<Class<?>> entityList = new ArrayList<>(entities);

            Collections.reverse(entityList);
            for (Class<?> clazz : entityList ){
                CreateTableAndEnum.dropTable(clazz,connection);
            }


            for (Class<?> clazz : FilesLoader.getEnums()) {
                if( clazz.isEnum())
                {
                    System.out.println(CreateTableAndEnum.generateCreateEnumQuery((Class<? extends Enum<?>>) clazz));

                    CreateTableAndEnum.createEnum((Class<? extends Enum<?>>) clazz, connection);
                }
                else
                    System.out.println(clazz.getName() + " is not enum");

            }
            for (Class<?> clazz : entities) {
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
