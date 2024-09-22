package main.myframework.annotation;

import main.myframework.enums.CascadeType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface CompositionType {
    String value() default "";
    CascadeType cascade() default CascadeType.NO_ACTION;
}