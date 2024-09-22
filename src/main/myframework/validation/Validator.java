package main.myframework.validation;

import main.myframework.annotation.StringMaxLength;

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

            StringMaxLength validation = field.getAnnotation(StringMaxLength.class);
            if (validation != null) {
                if (value == null) {
                    if (validation != null) {
                        errorMessages.add(field.getName() + ": "  );
                    }
                } else if (value instanceof String) {
                    String stringValue = (String) value;
                    int length = stringValue.length();

                    if (length < validation.maxLength()) {
                        errorMessages.add(field.getName() + ": "  );
                    }

                    if (length > validation.maxLength()) {
                        errorMessages.add(field.getName() + ": "  );
                    }
                } else {
                    errorMessages.add(field.getName());
                }
            }
        }

        return errorMessages;
    }
}
