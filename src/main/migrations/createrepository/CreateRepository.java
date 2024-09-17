package main.migrations.createrepository;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CreateRepository {
    public static boolean createRepository(Class<?> clazz) {

        String className = clazz.getSimpleName();

        String directoryPath = "./src/main/java/com/app/repository/" + className.toLowerCase();
        String fileName = directoryPath + "/" + className + "RepositoryImpl.java";
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        File file = new File(fileName);

        try {

            if (file.createNewFile()) {

                try (FileWriter writer = new FileWriter(file)) {
                    writer.write("package main.java.com.app.repository."+className.toLowerCase()+";\n" +
                            "\n" +

                            "import main.java.com.app.entities." + className + ";\n" +
                            "import main.migrations.orm.Orm;\n" +
                            "\n" +
                            "\n" +
                            "import java.util.List;\n" +
                            "\n" +
                            "public class " + className + "RepositoryImpl extends Orm<" + className + "> implements "+className+"Repository {\n" +
                            "\n" +
                            "\n" +
                            "    @Override\n" +
                            "    protected Class<" + className + "> getEntityClass() {\n" +
                            "        return " + className + ".class;\n" +
                            "    }\n" +
                            "\n" +
                            "    @Override\n" +
                            "    public boolean insert(" + className + " entity) {\n" +
                            "        return super.insert(entity);\n" +
                            "    }\n" +
                            "\n" +
                            "    @Override\n" +
                            "    public boolean update(" + className + " entity) {\n" +
                            "        return super.update(entity);\n" +
                            "    }\n" +
                            "\n" +
                            "    @Override\n" +
                            "    public boolean delete(" + className + " " + className.toLowerCase() + ") {\n" +
                            "        return  super.delete(" + className.toLowerCase() + ");\n" +
                            "    }\n" +
                            "\n" +
                            "    @Override\n" +
                            "    public " + className + " getById(Integer ID) {\n" +
                            "        return (" + className + ") super.getById(ID);\n" +
                            "    }\n" +
                            "\n" +
                            "    @Override\n" +
                            "    public List<" + className + "> getAll() {\n" +
                            "        return super.all();\n" +
                            "    }\n" +
                            "}\n");

                }
                System.out.println("File created: " + fileName);
                return true;
            } else {
                System.out.println("File already exists: " + fileName);
                return false;
            }
        } catch (IOException e) {
            System.err.println("An error occurred while creating the file: " + e.getMessage());
            return false;
        }
    }
    public static boolean createRepositoryInterface(Class<?> clazz) {

        String className = clazz.getSimpleName();
        String directoryPath = "./src/main/java/com/app/repository/" + className.toLowerCase();
        String fileName = directoryPath + "/" + className + "Repository.java";
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        File file = new File(fileName);

        try {

            if (file.createNewFile()) {

                try (FileWriter writer = new FileWriter(file)) {
                    writer.write("package main.java.com.app.repository." + className.toLowerCase() +";\n" +
                            "\n" +
                            "import main.java.com.app.entities." + className +";\n" +
                            "import main.java.com.app.repository.BaseRepository;\n" +
                            "\n" +
                            "public interface " + className +"Repository extends BaseRepository<" + className +", Integer> {\n" +
                            "}\n");

                }
                System.out.println("File created: " + fileName);
                return true;
            } else {
                System.out.println("File already exists: " + fileName);
                return false;
            }
        } catch (IOException e) {
            System.err.println("An error occurred while creating the file: " + e.getMessage());
            return false;
        }
    }
}
