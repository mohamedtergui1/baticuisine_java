package main.myframework.database;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ConfigReader {

    public static Map<String, String> readConfigFile() {
        Map<String, String> configMap = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(PostgreSQLDatabase.FILE_CONFIG_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Skip lines that start with "//"
                if (line.trim().startsWith("//")) {
                    continue; // Skip this iteration
                }

                // Split the line at the first '=' character
                String[] parts = line.split("=", 2);
                if (parts.length == 2) {
                    // Trim spaces and put in the map
                    String key = parts[0].trim();
                    String value = parts[1].trim();
                    configMap.put(key, value);
                }
            }
        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception as needed
        }

        return configMap;
    }
}
