package main.migrations.checkextends;


import java.lang.reflect.Constructor;
import java.util.Set;


public class CheckExtends {
    public static Class<?> check(Set<Class<?>> classes, Class<?> clazz) {
        classes.remove(clazz);
        if (clazz.isInterface() || java.lang.reflect.Modifier.isAbstract(clazz.getModifiers())) {
            return null;
        }
        try {
            Constructor<?> constructor = clazz.getDeclaredConstructor();
            Object obj = constructor.newInstance();
            for (Class<?> interfaceClass : classes) {
                if (interfaceClass.isInstance(obj)) {
                        return interfaceClass;
                }
            }
        } catch (NoSuchMethodException e) {
            System.out.println("No no-argument constructor available for class: " + clazz.getName());
        } catch (InstantiationException | IllegalAccessException e) {
            System.out.println("Error instantiating class: " + clazz.getName());
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
