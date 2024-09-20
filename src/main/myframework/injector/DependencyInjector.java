package main.myframework.injector;

import main.myframework.annotation.InjectClass;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class DependencyInjector {

    public static <T> T createInstance(Class<T> clazz) {
        for (Constructor<?> constructor : clazz.getDeclaredConstructors()) {
            if (constructor.isAnnotationPresent(InjectClass.class)) {
                InjectClass injectClass = constructor.getAnnotation(InjectClass.class);
                Class<?> toInject = injectClass.value();

                try {
                    // Create an instance of the class to inject
                    Object injectedInstance = toInject.getDeclaredConstructor().newInstance();

                    // Now create the instance of the class that has the constructor
                    return (T) constructor.newInstance(injectedInstance);
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                         NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }
        }
        return null; // or throw an exception
    }
}
