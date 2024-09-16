package main.migrations.tableenum;

import main.java.com.app.interfaces.GetId;
import main.migrations.checkextends.CheckExtends;
import main.migrations.createrepository.CreateRepository;
import main.migrations.filesloader.FilesLoader;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Instant;

public class CreateTableAndEnum {

    private static final Map<Class<?>, String> sqlTypeMap = new HashMap<>();

    static {
        sqlTypeMap.put(Integer.class, "INTEGER");
        sqlTypeMap.put(int.class, "INTEGER");
        sqlTypeMap.put(Long.class, "BIGINT");
        sqlTypeMap.put(long.class, "BIGINT");
        sqlTypeMap.put(Double.class, "DOUBLE PRECISION");
        sqlTypeMap.put(double.class, "DOUBLE PRECISION");
        sqlTypeMap.put(Float.class, "REAL");
        sqlTypeMap.put(float.class, "REAL");
        sqlTypeMap.put(String.class, "VARCHAR(255)");
        sqlTypeMap.put(Date.class, "TIMESTAMP");
        sqlTypeMap.put(LocalDate.class, "DATE");
        sqlTypeMap.put(LocalDateTime.class, "TIMESTAMP");
        sqlTypeMap.put(Instant.class, "TIMESTAMPTZ");
        sqlTypeMap.put(boolean.class, "BOOLEAN");
    }

    // The PostgreSQL connection URL, username, and password should be passed from outside
    public static boolean createTable(Class<?> clazz, Connection connection) {
        if (!doesTableExist(clazz, connection)) {
            String createTableQuery = generateCreateTableQuery(clazz);
            try (Statement stmt = connection.createStatement()) {
                stmt.execute(createTableQuery);
                System.out.println("Table created successfully for " + clazz.getSimpleName());
                CreateRepository.createRepository(clazz);
                return true;
            } catch (SQLException e) {
                System.err.println("Error creating table for " + clazz.getSimpleName() + ": " + e.getMessage());
                return false;
            }
        } else {
            System.out.println("Table already exists for " + clazz.getSimpleName());
            return false;
        }
    }

    public static boolean createEnum(Class<? extends Enum<?>> clazz, Connection connection) {
        if (!doesEnumTypeExist(clazz, connection)) {
            String createEnumQuery = generateCreateEnumQuery(clazz);
            try (Statement stmt = connection.createStatement()) {
                stmt.execute(createEnumQuery);
                System.out.println("Enum type created successfully for " + clazz.getSimpleName());
                return true;
            } catch (SQLException e) {
                System.err.println("Error creating enum type for " + clazz.getSimpleName() + ": " + e.getMessage());
                return false;
            }
        } else {
            System.out.println("Enum type already exists for " + clazz.getSimpleName());
            return false;
        }
    }

    private static boolean doesEnumTypeExist(Class<? extends Enum<?>> clazz, Connection connection) {
        String enumName = clazz.getSimpleName();
        String query = "SELECT 1 FROM pg_type WHERE typname = '" + enumName + "'";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            return rs.next();
        } catch (SQLException e) {
            System.err.println("Error checking existence of enum type " + enumName + ": " + e.getMessage());
            return false;
        }
    }

    public static String generateCreateEnumQuery(Class<? extends Enum<?>> clazz) {
        if (!clazz.isEnum()) {
            throw new IllegalArgumentException("Class must be an enum");
        }

        StringBuilder sql = new StringBuilder();
        sql.append("CREATE TYPE ").append(clazz.getSimpleName()).append(" AS ENUM (");

        // Append enum values
        String enumValues = Arrays.stream(clazz.getEnumConstants())
                .map(enumConstant -> "'" + enumConstant.toString() + "'")
                .reduce((a, b) -> a + ", " + b)
                .orElse("");

        sql.append(enumValues);
        sql.append(");");

        return sql.toString();
    }

    private static boolean doesTableExist(Class<?> clazz, Connection connection) {
        try {
            DatabaseMetaData metaData = connection.getMetaData();
            ResultSet tables = metaData.getTables(null, null, clazz.getSimpleName().toLowerCase(), null);
            return tables.next();
        } catch (SQLException e) {
            System.err.println("Error checking table existence: " + e.getMessage());
            return false;
        }
    }

    public static String generateCreateTableQuery(Class<?> clazz) {
        StringBuilder query = new StringBuilder("CREATE TABLE ");
        query.append(clazz.getSimpleName().toLowerCase()).append(" (");
        StringBuilder keys = new StringBuilder();
        Field[] fields = clazz.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            String columnName = field.getName().toLowerCase();
            Class<?> fieldType = field.getType();
            String sqlType = sqlTypeMap.get(fieldType);

            if (field.getName().equals("id")) {
                query.append("id SERIAL PRIMARY KEY");
            } else if (sqlType != null) {
                query.append(columnName).append(" ").append(sqlType);
            } else if (fieldType.isEnum()) {
                query.append(columnName).append( " " + fieldType.getSimpleName());
            } else if (isForeignKey(field.getType().getName())) {
                query.append(columnName).append("_id INTEGER");
                keys.append(", CONSTRAINT fk_")
                        .append(clazz.getSimpleName().toLowerCase())
                        .append("_")
                        .append(columnName)
                        .append(" FOREIGN KEY (")
                        .append(field.getType().getSimpleName().toLowerCase() + "_id")
                        .append(") REFERENCES public.").append( field.getType().getSimpleName().toLowerCase() +" (id) ")
                        .append("ON UPDATE NO ACTION ")
                        .append("ON DELETE NO ACTION");
            } else {
                System.err.println("Unknown type for field " + columnName);
                continue;
            }

            if (i < fields.length - 1) {
                query.append(", ");
            }
        }

        query.append(keys.toString());

        Class<?> parentClass = CheckExtends.check(FilesLoader.getEntities(), clazz);
        if (parentClass != null) {
            query.append(") INHERITS (").append(parentClass.getSimpleName().toLowerCase()).append(")");
        } else {
            query.append(")");
        }

        query.append(";");
        return query.toString();
    }

    private static boolean isForeignKey(String clazzName) {
        try {
            Class<?> clazz = Class.forName(clazzName);
            return GetId.class.isAssignableFrom(clazz);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("Class not found: " + clazzName, e);
        }
    }
}
