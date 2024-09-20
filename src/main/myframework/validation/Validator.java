package main.myframework.validation;

import main.myframework.annotation.StringValidation;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class Validator {

    public static List<String> validate(Object obj) throws IllegalAccessException {
        List<String> errorMessages = new ArrayList<>();
        Class<?> clazz = obj.getClass();

        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            Object value = field.get(obj);

            StringValidation validation = field.getAnnotation(StringValidation.class);
            if (validation != null) {
                if (value == null) {
                    if (!validation.nullable()) {
                        errorMessages.add(field.getName() + ": " + validation.message());
                    }
                } else if (value instanceof String) {
                    String stringValue = (String) value;
                    int length = stringValue.length();

                    if (length < validation.minLength()) {
                        errorMessages.add(field.getName() + ": " + validation.message());
                    }

                    if (length > validation.maxLength()) {
                        errorMessages.add(field.getName() + ": " + validation.message());
                    }
                } else {
                    errorMessages.add(field.getName() + ": " + validation.message());
                }
            }
        }

        return errorMessages;
    }
}
