package main.myframework.tableenum;

import main.myframework.enums.CascadeType;
import main.myframework.interfaces.GetId;

import main.myframework.createrepositoryandservice.CreateRepositoryAndService;
import main.myframework.filesloader.FilesLoader;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Instant;


import main.myframework.annotation.DefaultValueString;
import main.myframework.annotation.StringMaxLength;
import main.myframework.annotation.StringMinLength;
import main.myframework.annotation.Nullable;
import main.myframework.annotation.DefaultValueBoolean;
import main.myframework.annotation.CompositionType;
import main.myframework.orm.ReflectionUtil;

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
    public static String generateCreateTableQuery(Class<?> clazz) {
        StringBuilder query = new StringBuilder("CREATE TABLE ");
        query.append(clazz.getSimpleName().toLowerCase()).append(" (");
        StringBuilder keys = new StringBuilder();
        List<Field> fields = ReflectionUtil.getAllDeclaredFieldsSkipListAndSuperClass(clazz);

        Boolean status = false;
        for (Field field : fields) {
            if(status)
                query.append(", ");
            status = true;
            String columnName = field.getName().toLowerCase();
            Class<?> fieldType = field.getType();
            String sqlType = sqlTypeMap.get(fieldType);
            int max = 255;
            StringMaxLength maxLength = field.getAnnotation(StringMaxLength.class);
            if (maxLength != null) {
                max = maxLength.maxLength();
            }
            Nullable nullable = field.getAnnotation(Nullable.class);

            if (field.getName().equals("id")) {
                query.append("id SERIAL PRIMARY KEY");
            }else
            if(fieldType == String.class){
                sqlType = "VARCHAR("+max+")";
                DefaultValueString defaultValueStringAnnotation = field.getAnnotation(DefaultValueString.class);
                query.append(columnName).append(" ").append(sqlType);
                if(nullable != null && nullable.value()){
                    query.append(" DEFAULT NULL");
                } else
                if (defaultValueStringAnnotation != null) {
                    query.append(" DEFAULT '").append(defaultValueStringAnnotation.value()).append("'");
                }
            }
            else if (sqlType != null) {
                query.append(columnName).append(" ").append(sqlType);
                if(nullable != null && nullable.value()){
                    query.append(" DEFAULT NULL");
                } else {
                    DefaultValueString defaultValueStringAnnotation = field.getAnnotation(DefaultValueString.class);
                    if (defaultValueStringAnnotation != null) {
                        query.append(" DEFAULT '").append(defaultValueStringAnnotation.value()).append("'");
                    }
                    DefaultValueBoolean defaultValueBooleanAnnotation = field.getAnnotation(DefaultValueBoolean.class);
                    if (defaultValueBooleanAnnotation != null) {
                        query.append(" DEFAULT ").append(defaultValueBooleanAnnotation.value());
                    }
                }

            }
            // Handle enums
            else if (fieldType.isEnum()) {

                query.append(columnName).append(" ").append(fieldType.getSimpleName()).append(" ");
                DefaultValueString defaultValueStringAnnotation = field.getAnnotation(DefaultValueString.class);
                if(nullable != null && nullable.value()){
                    query.append(" DEFAULT NULL");
                } else
                if (defaultValueStringAnnotation != null) {
                    query.append(" DEFAULT '").append(defaultValueStringAnnotation.value()).append("'");
                }
            }

            else if (isForeignKey(field.getType().getName())) {
                CascadeType cascade = CascadeType.NO_ACTION;
                String action =  "NO ACTION";
                CompositionType compositionType = field.getAnnotation(CompositionType.class);
                if(compositionType != null) {
                    cascade = compositionType.cascade();
                    action = cascade.toString().replace('_',' ');
                }
                query.append(columnName).append("_id INTEGER");
                keys.append(", CONSTRAINT fk_")
                        .append(clazz.getSimpleName().toLowerCase())
                        .append("_")
                        .append(columnName)
                        .append(" FOREIGN KEY (")
                        .append(columnName).append("_id")
                        .append(") REFERENCES public.")
                        .append(field.getType().getSimpleName().toLowerCase()).append(" (id) ")
                        .append("ON DELETE ").append(action).append(" ");
            } else {
                System.err.println("Unknown type for field " + columnName);
                continue;
            }



        }

        query.append(keys.toString());


        Class<?> parentClass = clazz.getSuperclass();
        if (parentClass != null && parentClass != Object.class) {
            query.append(") INHERITS (").append(parentClass.getSimpleName().toLowerCase()).append(")");
        } else {
            query.append(")");
        }

        query.append(";");

        return query.toString();
    }

    public static boolean dropTable(Class<?> clazz, Connection connection) {
        // Determine the table name based on the class name
        String tableName = clazz.getSimpleName();

        // SQL statement to drop the table
        String dropTableQuery = "DROP TABLE IF EXISTS " + tableName + ";";

        // Execute the drop table query
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(dropTableQuery);
            System.out.println("Table " + tableName + " dropped successfully.");
            return true;
        } catch (SQLException e) {
            System.err.println("Error dropping table " + tableName + ": " + e.getMessage());
            return false;
        }
    }

    public static boolean createTable(Class<?> clazz, Connection connection) {
        if (!doesTableExist(clazz, connection)) {
            String createTableQuery = generateCreateTableQuery(clazz);
            try (Statement stmt = connection.createStatement()) {
                stmt.execute(createTableQuery);
                System.out.println("Table created successfully for " + clazz.getSimpleName());
                if (clazz.isInterface() || java.lang.reflect.Modifier.isAbstract(clazz.getModifiers())) {
                    return true;
                }
                CreateRepositoryAndService.createRepositoryInterface(clazz);
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

    private static boolean isForeignKey(String clazzName) {
        try {
            Class<?> clazz = Class.forName(clazzName);
            return GetId.class.isAssignableFrom(clazz);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("Class not found: " + clazzName, e);
        }
    }
}
