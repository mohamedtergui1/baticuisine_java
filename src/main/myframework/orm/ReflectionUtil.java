package main.myframework.orm;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class ReflectionUtil {

    public static List<Field> getAllDeclaredFields(Class<?> clazz) {
        List<Field> allFields = new ArrayList<>();

        // Loop through the class hierarchy
        while (clazz != null) {
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                allFields.add(field); // Add all declared fields (protected, private, etc.)
            }
            // Move to the superclass
            clazz = clazz.getSuperclass();
        }

        return allFields;
    }
    public static Field getFieldByName(Class<?> clazz, String fieldName)  {
        List<Field> fields = getAllDeclaredFields(clazz);
        for (Field field : fields) {
            if (field.getName().equals(fieldName)) {
                return field;
            }
        }
        return null;
    }

    public static List<Field> getAllDeclaredFieldsSkipList(Class<?> clazz) {
        List<Field> allFields = new ArrayList<>();

        // Loop through the class hierarchy
        while (clazz != null) {
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                // Check if the field's type is not a List
                if (!List.class.isAssignableFrom(field.getType())) {
                    allFields.add(field);
                }
            }
            // Move to the superclass
            clazz = clazz.getSuperclass();
        }

        return allFields;
    }

    public static List<Field> getAllDeclaredFieldsSkipListAndSuperClass(Class<?> clazz) {
        List<Field> allFields = new ArrayList<>();


            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                // Check if the field's type is not a List
                if (!List.class.isAssignableFrom(field.getType())) {
                    allFields.add(field);
                }
            }



        return allFields;
    }

}
