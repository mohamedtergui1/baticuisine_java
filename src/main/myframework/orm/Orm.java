package main.myframework.orm;

import main.myframework.database.PostgreSQLDatabase;
import main.myframework.interfaces.GetId;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.*;
import java.lang.reflect.Field;
import java.util.*;

public abstract class Orm<T> {
    QueryBuilder queryBuilder;

    private final Connection con;

    protected Orm() {
        this.con = PostgreSQLDatabase.getInstance().getConnection();
        queryBuilder = new QueryBuilder<>(this.con,getEntityClass());
    }

    static final Set<String> ALLOWED_TYPES = new HashSet<>(Arrays.asList("int","boolean" , "float", "java.lang.String", "char", "long", "double", "java.sql.Date"));

    protected abstract Class<T> getEntityClass();

    protected Set<Class<?>> manyRelations() {
        return new HashSet<>();
    }

    public QueryBuilder<T> buildQuery()
    {
        return queryBuilder;
    }

    public T insert(T obj) {
        if (obj == null) {
            throw new IllegalArgumentException("Object cannot be null");
        }
        String tableName = getEntityClass().getSimpleName().toLowerCase();
        String sql = generateInsertSQL(tableName, obj);


        try (PreparedStatement pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            int parameterIndex = 1;
            for (Field field : ReflectionUtil.getAllDeclaredFieldsSkipList(getEntityClass())) {
                field.setAccessible(true);
                Object value = field.get(obj);

                if (value != null && !field.getName().equals("id")) {
                    if (ALLOWED_TYPES.contains(field.getType().getName())) {
                        if (value instanceof java.sql.Date) {
                            pstmt.setDate(parameterIndex++, (java.sql.Date) value);
                        } else {
                            pstmt.setObject(parameterIndex++, value);
                        }
                    } else if (value instanceof GetId) {
                        pstmt.setInt(parameterIndex++, ((GetId) value).getId());
                    } else {
                        throw new SQLException("Unsupported type: " + field.getType().getName());
                    }
                }
            }

            int rowsAffected = pstmt.executeUpdate();

            // Get the generated ID
            if (rowsAffected > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        int generatedId = rs.getInt(1);
                        Field idField = ReflectionUtil.getFieldByName(getEntityClass(),"id");;
                        idField.setAccessible(true);
                        idField.set(obj, generatedId);  // Set the generated ID
                    }
                }
            }
            return obj;  // Return the modified object
        } catch (SQLException | IllegalAccessException e) {
            System.out.println("Error during insert: " + e.getMessage());
            return null;  // Return null if there was an error
        }
    }

    public T update(T obj) {
        if (obj == null) {
            throw new IllegalArgumentException("Object cannot be null");
        }
        String tableName = getEntityClass().getSimpleName().toLowerCase();
        String sql = generateUpdateSQL(tableName, obj);


        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            int parameterIndex = 1;
            List<Field> fields = ReflectionUtil.getAllDeclaredFieldsSkipList(getEntityClass());
            for (Field field : fields) {
                field.setAccessible(true);
                Object value = field.get(obj);


                    String typeName = field.getType().getName();
                    if(field.getType().isEnum()){
                        pstmt.setObject(parameterIndex++,  value,Types.OTHER);
                    }else
                    if (ALLOWED_TYPES.contains(typeName)) {
                        if (value instanceof java.sql.Date) {
                            pstmt.setDate(parameterIndex++, (java.sql.Date) value);
                        } else if (!field.getName().equals("id")) {
                            pstmt.setObject(parameterIndex++, value);
                        }
                    } else if (value instanceof GetId) {
                        pstmt.setInt(parameterIndex++, ((GetId) value).getId());
                    } else {
                        throw new SQLException("Unsupported type: " + typeName);
                    }

            }

            Field idField = ReflectionUtil.getFieldByName(getEntityClass(),"id");;
            idField.setAccessible(true);
            Object idValue = idField.get(obj);
            if (idValue == null) {
                throw new IllegalArgumentException("Object must have a non-null 'id' field for update.");
            }

            pstmt.setObject(parameterIndex, idValue);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0 ? obj : null;  // Return the modified object if update was successful
        } catch (SQLException | IllegalAccessException e) {
            System.out.println("Error during update: " + e.getMessage());
            return null;  // Return null if there was an error
        }
    }

    public boolean delete(T obj) {
        if (obj == null) {
            throw new IllegalArgumentException("Object cannot be null");
        }

        String tableName = getEntityClass().getSimpleName().toLowerCase();
        String sql = generateDeleteSQL(tableName, obj);

        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            Field[] fields = getEntityClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                if (field.getName().equals("id")) {
                    Object idValue = field.get(obj);
                    if (idValue != null) {
                        pstmt.setObject(1, idValue);
                    } else {
                        throw new IllegalArgumentException("ID value cannot be null for deletion.");
                    }
                    break;
                }
            }
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException | IllegalAccessException e) {
            System.out.println("Error during delete: " + e.getMessage());
            return false;
        }
    }

    public ArrayList<T> all() {
        return all(getEntityClass(), null, null);
    }
    public ArrayList<Object> allblock(Class<?> entityClass, String columnName, Object value)
    {
        String tableName = entityClass.getSimpleName().toLowerCase();
        String sql = generateSelectQuery(tableName, columnName);

        ArrayList<Object> results = new ArrayList<>();
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {

            if(value != null)
                pstmt.setObject(1, Integer.parseInt(value.toString()) );


            try (ResultSet rs = pstmt.executeQuery()) {

                while (rs.next()) {
                    Object entity = entityClass.getDeclaredConstructor().newInstance();

                    for (Field field :  ReflectionUtil.getAllDeclaredFields(entityClass)) {
                        field.setAccessible(true);
                        if (!List.class.isAssignableFrom(field.getType())) {

                            fetchBlock(rs, entity, field);
                        }
                        else {
                            Type genericType = field.getGenericType();

                            // Check if the field is a parameterized type
                            if (genericType instanceof ParameterizedType) {
                                ParameterizedType parameterizedType = (ParameterizedType) genericType;
                                Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();

                                if (actualTypeArguments.length > 0) {
                                    Class<?> listItemType = (Class<?>) actualTypeArguments[0];
                                    List<Object> relatedEntities = allblock((Class<?>) listItemType, entityClass.getSimpleName().toLowerCase() + "_id", rs.getString("id"));
                                    field.set(entity, relatedEntities);
                                } else {
                                    System.err.println("List type has no parameterized type: " + field.getType());
                                }
                            } else {
                                System.err.println("Field is not a parameterized type: " + field.getGenericType());
                            }
                        }
//
                    }

                    results.add(entity);
                }
            }
        } catch (SQLException | ReflectiveOperationException e) {
            System.err.println("Error during query execution: " + e.getMessage());
        }

        return results;
    }

    public ArrayList<T> all(Class<T> entityClass, String columnName, Object value) {
        String tableName = entityClass.getSimpleName().toLowerCase();
        String sql = generateSelectQuery(tableName, columnName);
        ArrayList<T> results = new ArrayList<>();



        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            if (value != null) {
                pstmt.setObject(1, value);
            }

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    T entity = entityClass.getDeclaredConstructor().newInstance();

                    for (Field field : ReflectionUtil.getAllDeclaredFields(entityClass)) {
                        field.setAccessible(true);
                        if (List.class.isAssignableFrom(field.getType())) {
                            // Handle list of related entities
                            Type genericType = field.getGenericType();

                            // Check if the field is a parameterized type
                            if (genericType instanceof ParameterizedType) {
                                ParameterizedType parameterizedType = (ParameterizedType) genericType;
                                Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();

                                if (actualTypeArguments.length > 0) {
                                    Class<?> listItemType = (Class<?>) actualTypeArguments[0];
                                    List<Object> relatedEntities = allblock((Class<?>) listItemType, entityClass.getSimpleName().toLowerCase() + "_id", rs.getString("id"));
                                    field.set(entity, relatedEntities);
                                } else {
                                    System.err.println("List type has no parameterized type: " + field.getType());
                                }
                            } else {
                                System.err.println("Field is not a parameterized type: " + field.getGenericType());
                            }
                        } else {
                            fetch(rs, entity, field);
                        }
                    }

                    results.add(entity);
                }
            }
        } catch (SQLException e) {
            System.err.println("SQL error during query execution: " + e.getMessage());
        } catch (ReflectiveOperationException e) {
            System.err.println("Reflection error during entity creation or field access: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
        }

        return results;
    }

    private String getColumnNameForField(Field field) {
        // Adjust this logic based on your naming conventions
        String fieldName = field.getName();
        if (field.getType().isEnum()) {
            // For enum fields, use the field name directly
            return fieldName;
        } else {
            // For other fields, follow your naming convention
            return ALLOWED_TYPES.contains(field.getType().getName()) ? fieldName : fieldName.toLowerCase() + "_id";
        }
    }

    public Object getById(Integer id, String className) {
        if (id == null || className == null || className.isEmpty()) {
            System.err.println("Invalid id or className");
            return null; // Early return if invalid
        }

        try {
            // Convert the class name to a Class object
            Class<?> clazz = Class.forName(className);
            String tableName = clazz.getSimpleName().toLowerCase(); // Use class name as table name
            String sql = generateSelectQuery(tableName, "id");


            // Prepare the statement
            try (PreparedStatement pstmt = con.prepareStatement(sql)) {
                pstmt.setInt(1, id);

                // Execute the query
                try (ResultSet rs = pstmt.executeQuery()) {
                    // Check if a result is returned
                    if (rs.next()) {
                        Object entity = clazz.getDeclaredConstructor().newInstance();

                        // Get all fields of the class, including protected ones
                        List<Field> fields = ReflectionUtil.getAllDeclaredFields(clazz);

                        // Populate the instance with data from the result set
                        for (Field field : fields) {
                            field.setAccessible(true);

                            if (List.class.isAssignableFrom(field.getType())) {
                                // Handle list of related entities
                                Type genericType = field.getGenericType();

                                // Check if the field is a parameterized type
                                if (genericType instanceof ParameterizedType) {
                                    ParameterizedType parameterizedType = (ParameterizedType) genericType;
                                    Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();

                                    if (actualTypeArguments.length > 0) {
                                        Class<?> listItemType = (Class<?>) actualTypeArguments[0];
                                    List<Object> relatedEntities = allblock((Class<?>) listItemType, clazz.getSimpleName().toLowerCase() + "_id", rs.getString("id"));
                                        field.set(entity, relatedEntities);
                                    } else {
                                        System.err.println("List type has no parameterized type: " + field.getType());
                                    }
                                } else {
                                    System.err.println("Field is not a parameterized type: " + field.getGenericType());
                                }
                            } else {
                                fetch(rs, entity, field);
                            }
                        }

                        return entity;
                    } else {
                        return null; // No record found
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error during getById: " + className + " \n" + e.getMessage());
            return null; // Handle the exception as needed
        }
    }

    private void fetch(ResultSet rs, Object entity, Field field) throws SQLException {
        field.setAccessible(true);
        String fieldName = field.getName();
        String fieldType = field.getType().getName();
        String fieldColumnName = getColumnNameForField(field);
        Object columnValue = rs.getObject(fieldColumnName);

        try {
            if (field.getType().isEnum()) {
                handleEnumField(field, entity, columnValue);
            } else {
                handleBasicTypes(field, entity, columnValue, fieldType);
            }
        } catch (Exception e) {
            logError(fieldName, e);
        }
    }

    private void handleEnumField(Field field, Object entity, Object columnValue) throws IllegalAccessException {
        if (columnValue != null) {
            String enumName = columnValue.toString();
            field.set(entity, Enum.valueOf((Class<Enum>) field.getType(), enumName));
        } else {
            field.set(entity, null);
        }
    }

    private void handleBasicTypes(Field field, Object entity, Object columnValue, String fieldType) throws IllegalAccessException {
        if (columnValue == null) {
            field.set(entity, null);
            return;
        }

        switch (fieldType) {
            case "java.lang.String":
                field.set(entity, columnValue.toString());
                break;
            case "int":
            case "java.lang.Integer":
                field.set(entity, ((Number) columnValue).intValue());
                break;
            case "double":
            case "java.lang.Double":
                field.set(entity, ((Number) columnValue).doubleValue());
                break;
            case "float":
            case "java.lang.Float":
                field.set(entity, ((Number) columnValue).floatValue());
                break;
            case "long":
            case "java.lang.Long":
                field.set(entity, ((Number) columnValue).longValue());
                break;
            case "boolean":
            case "java.lang.Boolean":
                field.set(entity, columnValue);
                break;
            case "java.sql.Date":
                field.set(entity, new java.sql.Date(((java.sql.Date) columnValue).getTime()));
                break;
            default:
                handleComplexTypes(field, entity, columnValue);
                break;
        }
    }

    private void handleComplexTypes(Field field, Object entity, Object columnValue) throws IllegalAccessException {
        if (GetId.class.isAssignableFrom(field.getType())) {
            // Handle single related entity
            Object relatedEntity = getById((Integer) columnValue, field.getType().getName());
            field.set(entity, relatedEntity);
        }   else {
            System.err.println("Unsupported field type: " + field.getType());
        }
    }

    private void logError(String fieldName, Exception e) {
        // Use a logging framework here, like log4j or SLF4J
        System.err.println("Error setting field value: " + fieldName + " - " + e.getMessage());
    }

    private void fetchBlock(ResultSet rs, Object entity, Field field) throws SQLException {

        field.setAccessible(true);
        String fieldName = field.getName();
        String fieldType = field.getType().getName();


        String fieldColumnName = getColumnNameForField(field);


        Object columnValue = rs.getObject(fieldColumnName);

        try {
            if (field.getType().isEnum()) {

                if (columnValue != null) {
                    String enumName = columnValue.toString();
                    field.set(entity, Enum.valueOf((Class<Enum>) field.getType(), enumName));
                } else {
                    field.set(entity, null);
                }
            } else {

                switch (fieldType) {
                    case "java.lang.String":
                        field.set(entity, columnValue != null ? columnValue.toString() : null);
                        break;
                    case "int":
                    case "java.lang.Integer":
                        field.set(entity, columnValue != null ? ((Number) columnValue).intValue() : null);
                        break;
                    case "double":
                    case "java.lang.Double":
                        field.set(entity, columnValue != null ? ((Number) columnValue).doubleValue() : null);
                        break;
                    case "float":
                    case "java.lang.Float":
                        field.set(entity, columnValue != null ? ((Number) columnValue).floatValue() : null);
                        break;
                    case "long":
                    case "java.lang.Long":
                        field.set(entity, columnValue != null ? ((Number) columnValue).longValue() : null);
                        break;
                    case "boolean":
                    case "java.lang.Boolean":
                        field.set(entity, columnValue != null ? ((Boolean) columnValue) : null);
                        break;
                    case "java.sql.Date":
                        field.set(entity, columnValue != null ? new java.sql.Date(((java.sql.Date) columnValue).getTime()) : null);
                        break;
                    default:
                        if (GetId.class.isAssignableFrom(field.getType())) {
                            try {

                                GetId instance = (GetId) field.getType().getDeclaredConstructor().newInstance();
                                instance.setId(Integer.parseInt(columnValue.toString()));
                                field.set(entity, instance);
                            } catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
                                     InvocationTargetException e) {

                                System.err.println("Error creating instance of GetId: " + e.getMessage());
                            }
                        }

                        break;
                }
            }
        } catch (Exception e) {
            System.err.println("Error setting field value: " + fieldName + " - " + e.getMessage());
        }
    }

    public  Object getById(Integer id){
        return getById(id,getEntityClass().getName());
    }

    public Map<String, ArrayList<?>> loadRelations(Object object) {
        Map<String, ArrayList<?>> loadRelations = new HashMap<>();

        for (Class<?> relation : manyRelations()) {

            if (object instanceof GetId ) {
                Object id = ((GetId) object).getId();

                loadRelations.put(relation.getSimpleName().toLowerCase() + "s",all( (Class<T>) relation,
                        getEntityClass().getSimpleName().toLowerCase() + "_id", id));
            }
        }
        return loadRelations;
    }

    public  int count()  {return count(getEntityClass(),null);}

    public static String generateCountQuery(Class<?> clazz, HashMap<String, HashMap<String, ArrayList<String>>> conditions) {
        String tableName = clazz.getSimpleName(); // Assuming the table name is the same as the class name

        // Start constructing the SQL query
        StringBuilder queryBuilder = new StringBuilder("SELECT COUNT(*) FROM ");
        queryBuilder.append(tableName);

        if ( conditions != null && !conditions.isEmpty()) {
            Iterator<Map.Entry<String, HashMap<String, ArrayList<String>>>> outerIterator = conditions.entrySet().iterator();

            while (outerIterator.hasNext()) {
                Map.Entry<String, HashMap<String, ArrayList<String>>> outerEntry = outerIterator.next();
                String operator = outerEntry.getKey(); // e.g., "AND" or "OR"
                HashMap<String, ArrayList<String>> columnsMap = outerEntry.getValue();

                queryBuilder.append(" WHERE ");

                Iterator<Map.Entry<String, ArrayList<String>>> innerIterator = columnsMap.entrySet().iterator();
                while (innerIterator.hasNext()) {
                    Map.Entry<String, ArrayList<String>> innerEntry = innerIterator.next();
                    String column = innerEntry.getKey();
                    ArrayList<String> values = innerEntry.getValue();

                    queryBuilder.append(column).append(" IN (");
                    for (int i = 0; i < values.size(); i++) {
                        if (i > 0) {
                            queryBuilder.append(", ");
                        }
                        queryBuilder.append("?");
                    }
                    queryBuilder.append(")");

                    if (innerIterator.hasNext()) {
                        queryBuilder.append(" ").append(operator).append(" ");
                    }
                }

                if (outerIterator.hasNext()) {
                    queryBuilder.append(" ");
                }
            }
        }

        return queryBuilder.toString();
    }

    public  int count(Class<?> clazz, HashMap<String, HashMap<String, ArrayList<String>>> conditions) {
        String query = generateCountQuery(clazz, conditions);

        try (
             PreparedStatement preparedStatement = this.con.prepareStatement(query)) {

            int paramIndex = 1;
            if ( conditions != null && !conditions.isEmpty()) {
                for (HashMap<String, ArrayList<String>> columnConditions : conditions.values()) {
                    for (ArrayList<String> values : columnConditions.values()) {
                        for (String value : values) {
                            preparedStatement.setObject(paramIndex++, value);
                        }
                    }
                }
            }
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1); // Return the count
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return 0;
    }

    private String generateSelectQuery(String tableName, String columnName) {
        if (columnName != null && !columnName.isEmpty()) {

            return "SELECT * FROM " + tableName + " WHERE " + columnName + " = ?";
        } else {


            return "SELECT * FROM " + tableName;
        }
    }

    private String generateInsertSQL(String tableName, T obj) {
        StringBuilder sql = new StringBuilder("INSERT INTO ").append(tableName).append(" (");
        StringBuilder values = new StringBuilder(" VALUES (");

        List<Field> fields = ReflectionUtil.getAllDeclaredFieldsSkipList(getEntityClass());;
        boolean firstField = true;

        for (Field field : fields) {
            try {
                field.setAccessible(true);
                Object value = field.get(obj);

                if (value != null && !field.getName().equals("id")) { // Skip the ID field
                    if (!firstField) {
                        sql.append(", ");
                        values.append(", ");
                    }

                    if (ALLOWED_TYPES.contains(field.getType().getName())) {
                        sql.append(field.getName());
                    } else {
                        sql.append(field.getName()).append("_id");
                    }
                    values.append("?");
                    firstField = false;
                }
            } catch (IllegalAccessException e) {
                System.out.println("Error accessing field: " + e.getMessage());
            }
        }

        sql.append(")");
        values.append(")");
        sql.append(values);

        return sql.toString();
    }

    private String generateDeleteSQL(String tableName, T obj) {
        StringBuilder sql = new StringBuilder("DELETE FROM ").append(tableName).append(" WHERE ");

        Field[] fields = getEntityClass().getDeclaredFields();
        boolean idFieldFound = false;

        for (Field field : fields) {
            if (field.getName().equals("id")) {
                sql.append(field.getName()).append(" = ?");
                idFieldFound = true;
                break;
            }
        }

        if (!idFieldFound) {
            throw new IllegalArgumentException("No ID field found in entity class.");
        }

        return sql.toString();
    }

    private String generateUpdateSQL(String tableName, T obj) {
        StringBuilder sql = new StringBuilder("UPDATE ").append(tableName).append(" SET  ");
        List<Field> fields = ReflectionUtil.getAllDeclaredFieldsSkipList(getEntityClass());
        boolean firstField = true;

        for (Field field : fields) {

                field.setAccessible(true);


                if ( !field.getName().equals("id")) {
                    if (!firstField) {
                        sql.append(", ");
                    }
                    if (ALLOWED_TYPES.contains(field.getType().getName()))
                        sql.append(field.getName()).append(" = ?");
                    else if(field.getType().isEnum()){
                        sql.append(field.getName()).append(" = ?");

                    }else
                        sql.append(field.getName().toLowerCase()).append("_id").append(" = ?");
                    firstField = false;
                }

        }

        sql.append(" WHERE id = ?");

        return sql.toString();
    }

}