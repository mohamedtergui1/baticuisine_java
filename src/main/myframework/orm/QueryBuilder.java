package main.myframework.orm;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class QueryBuilder<T> {
    private StringBuilder query;
    private Connection con;
    private Class<?> clazz;
    private Map<String, String> typeValue = new HashMap<>();

    public QueryBuilder(Connection con, Class<?> clazz) {
        this.query = new StringBuilder();
        this.con = con;
        this.clazz = clazz;
    }

    public QueryBuilder<T> select() {
        query.setLength(0);
        typeValue.clear();
        query.append("SELECT * FROM ").append(clazz.getSimpleName());
        return this;
    }

    public QueryBuilder<T> whereLike(String column, String value) {
        String formattedValue = "%" + value + "%";
        typeValue.put("java.lang.String", formattedValue);
        query.append(" WHERE ").append(column).append(" LIKE ?");
        return this;
    }

    public QueryBuilder<T> andWhereLike(String column, String value) {
        String formattedValue = "%" + value + "%";
        typeValue.put("java.lang.String", formattedValue);
        query.append(" AND ").append(column).append(" LIKE ?");
        return this;
    }

    public List<T> fetchAll() {
        return fetchAll((Class<T>) clazz);
    }

    public List<T> fetchAll(Class<T> clazz) {
        List<T> results = new ArrayList<>();
        System.out.println(query.toString());

        try (PreparedStatement pstmt = con.prepareStatement(this.query.toString())) {
            AtomicInteger index = new AtomicInteger(1);

            // Set parameters in the PreparedStatement
            for (Map.Entry<String, String> entry : this.typeValue.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();

                if (Orm.ALLOWED_TYPES.contains(key)) {
                    try {
                        switch (key) {
                            case "int":
                                pstmt.setInt(index.getAndIncrement(), Integer.parseInt(value));
                                break;
                            case "boolean":
                                pstmt.setBoolean(index.getAndIncrement(), Boolean.parseBoolean(value));
                                break;
                            case "float":
                                pstmt.setFloat(index.getAndIncrement(), Float.parseFloat(value));
                                break;
                            case "java.lang.String":
                                pstmt.setString(index.getAndIncrement(), value);
                                break;
                            case "char":
                                pstmt.setString(index.getAndIncrement(), String.valueOf(value.charAt(0)));
                                break;
                            case "long":
                                pstmt.setLong(index.getAndIncrement(), Long.parseLong(value));
                                break;
                            case "double":
                                pstmt.setDouble(index.getAndIncrement(), Double.parseDouble(value));
                                break;
                            case "java.sql.Date":
                                pstmt.setDate(index.getAndIncrement(), java.sql.Date.valueOf(value));
                                break;
                            default:
                                throw new IllegalArgumentException("Unsupported type: " + key);
                        }
                    } catch (SQLException e) {
                        System.err.println("Error setting parameter: " + e.getMessage());
                    } catch (NumberFormatException e) {
                        System.err.println("Error parsing value for type " + key + ": " + e.getMessage());
                    }
                } else {
                    throw new IllegalArgumentException("Unsupported type: " + key);
                }
            }

            // Execute the query and process the results
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    T instance = mapRow(rs);
                    results.add(instance);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("SQL error occurred: " + e.getMessage(), e);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e.getMessage(), e);
        } finally {
            this.query.setLength(0); // Clear the query for future use
            this.typeValue.clear();   // Clear the type values
        }

        return results;
    }

    private T mapRow(ResultSet rs) throws SQLException {
        try {
            T instance = (T) clazz.getDeclaredConstructor().newInstance();
            for (Field field : clazz.getDeclaredFields()) {
                field.setAccessible(true); // Allow access to private fields
                Object value = rs.getObject(field.getName()); // Get value from ResultSet
                field.set(instance, value); // Set value to the instance
            }
            return instance;
        } catch (ReflectiveOperationException e) {
            throw new SQLException("Error creating instance of " + clazz.getName(), e);
        }
    }

    public T fetch() {
        return fetch((Class<T>) clazz); // Calls the overloaded method with the class determined by getEntityClass()
    }

    public T fetch(Class<T> clazz) {
        T result = null;
        System.out.println(query.toString());

        try (PreparedStatement pstmt = con.prepareStatement(this.query.toString())) {
            AtomicInteger index = new AtomicInteger(1);

            // Set parameters in the PreparedStatement
            for (Map.Entry<String, String> entry : this.typeValue.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();

                if (Orm.ALLOWED_TYPES.contains(key)) {
                    try {
                        switch (key) {
                            case "int":
                                pstmt.setInt(index.getAndIncrement(), Integer.parseInt(value));
                                break;
                            case "boolean":
                                pstmt.setBoolean(index.getAndIncrement(), Boolean.parseBoolean(value));
                                break;
                            case "float":
                                pstmt.setFloat(index.getAndIncrement(), Float.parseFloat(value));
                                break;
                            case "java.lang.String":
                                pstmt.setString(index.getAndIncrement(), value);
                                break;
                            case "char":
                                pstmt.setString(index.getAndIncrement(), String.valueOf(value.charAt(0)));
                                break;
                            case "long":
                                pstmt.setLong(index.getAndIncrement(), Long.parseLong(value));
                                break;
                            case "double":
                                pstmt.setDouble(index.getAndIncrement(), Double.parseDouble(value));
                                break;
                            case "java.sql.Date":
                                pstmt.setDate(index.getAndIncrement(), java.sql.Date.valueOf(value));
                                break;
                            default:
                                throw new IllegalArgumentException("Unsupported type: " + key);
                        }
                    } catch (SQLException e) {
                        System.err.println("Error setting parameter: " + e.getMessage());
                    } catch (NumberFormatException e) {
                        System.err.println("Error parsing value for type " + key + ": " + e.getMessage());
                    }
                } else {
                    throw new IllegalArgumentException("Unsupported type: " + key);
                }
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    result = mapRow(rs); // Use a method to convert ResultSet to T
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("SQL error occurred: " + e.getMessage(), e);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e.getMessage(), e);
        }

        return result; // Return the fetched entity or null if not found
    }
}
