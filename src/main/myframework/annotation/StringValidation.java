package main.myframework.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface StringValidation {
    int minLength() default 0;
    int maxLength() default Integer.MAX_VALUE;
    boolean nullable() default false;
    String message() default "Invalid field value";
}
