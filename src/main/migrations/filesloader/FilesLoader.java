package main.migrations.filesloader;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.stream.Collectors;

public class FilesLoader {

    public static Set<Class<?>> getEntities() {
        Set<Class<?>> entities = new LinkedHashSet<>();
        File directory = new File("./src/main/java/com/app/entities");

        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles((dir, name) -> name.endsWith(".java"));
            if (files != null) {
                try {
                    // Retrieve file creation dates
                    List<File> sortedFiles = Arrays.stream(files)
                            .sorted(Comparator.comparing(FilesLoader::getCreationDate))
                            .collect(Collectors.toList());

                    URL[] urls = { directory.toURI().toURL() };
                    URLClassLoader classLoader = new URLClassLoader(urls);

                    for (File file : sortedFiles) {
                        String className = file.getName().replace(".java", "");
                        String fullClassName = "main.java.com.app.entities." + className;

                        try {
                            Class<?> clazz = classLoader.loadClass(fullClassName);
                            entities.add(clazz);
                        } catch (ClassNotFoundException e) {
                            System.out.println("Class not found: " + fullClassName);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("The directory is empty or cannot be read.");
            }
        } else {
            System.out.println("The specified path is not a directory or does not exist.");
        }
        return entities;
    }

    public static Set<Class<?>> getEnums() {
        Set<Class<?>> enums = new LinkedHashSet<>();
        File directory = new File("./src/main/java/com/app/enums");

        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles((dir, name) -> name.endsWith(".java"));
            if (files != null) {
                try {
                    // Retrieve file creation dates
                    List<File> sortedFiles = Arrays.stream(files)
                            .sorted(Comparator.comparing(FilesLoader::getCreationDate))
                            .collect(Collectors.toList());

                    URL[] urls = { directory.toURI().toURL() };
                    URLClassLoader classLoader = new URLClassLoader(urls);

                    for (File file : sortedFiles) {
                        String className = file.getName().replace(".java", "");
                        String fullClassName = "main.java.com.app.enums." + className;

                        try {
                            Class<?> clazz = classLoader.loadClass(fullClassName);
                            enums.add(clazz);
                        } catch (ClassNotFoundException e) {
                            System.out.println("Class not found: " + fullClassName);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("The directory is empty or cannot be read.");
            }
        } else {
            System.out.println("The specified path is not a directory or does not exist.");
        }
        return enums;
    }

    private static Date getCreationDate(File file) {
        try {
            BasicFileAttributes attrs = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
            return new Date(attrs.creationTime().toMillis());
        } catch (Exception e) {
            e.printStackTrace();
            return new Date(0); // Default to epoch if creation date cannot be retrieved
        }
    }
}
