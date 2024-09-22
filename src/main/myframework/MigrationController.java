package main.myframework;

import java.sql.Connection;

import main.myframework.database.PostgreSQLDatabase;
import main.myframework.filesloader.FilesLoader;
import main.myframework.tableenum.CreateTableAndEnum;

import java.sql.SQLException;
import java.util.*;

public class MigrationController {
    public static void main(String[] args) {
        System.out.println("Hello World!");
        try (Connection connection = PostgreSQLDatabase.getInstance().getConnection()) {
            Set<Class<?>> entities =  FilesLoader.getEntities();
            List<Class<?>> entityList = new ArrayList<>(entities);

//            Collections.reverse(entityList);
//            for (Class<?> clazz : entityList ){
//                CreateTableAndEnum.dropTable(clazz,connection);
//            }


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