package main.myframework.createrepositoryandservice;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CreateRepositoryAndService {
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
                createService(clazz);
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
                createRepository(clazz);
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

    public static boolean createService(Class<?> clazz) {

        String className = clazz.getSimpleName();
        String directoryPath = "./src/main/java/com/app/service";
        String fileName = directoryPath + "/" + className + "Service.java";
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        File file = new File(fileName);

        try {

            if (file.createNewFile()) {

                try (FileWriter writer = new FileWriter(file)) {
                    writer.write("package main.java.com.app.service;\nimport main.migrations.annotation.InjectClass;\n" +
                            "\n" +
                            "import main.java.com.app.entities."+className+";\n" +
                            "import main.java.com.app.repository."+ className.toLowerCase() +"."+className+"Repository;\n" +
                            "\nimport main.java.com.app.repository."+className.toLowerCase()+"."+className+"RepositoryImpl;\n" +
                            "import java.util.List;\n" +
                            "\n" +
                            "public class "+className+"Service {\n" +
                            "    "+className+"Repository "+ className.toLowerCase() +"Repository;\n    @InjectClass(" + className + "RepositoryImpl.class)\n" +
                            "    public "+className+"Service("+className+"Repository "+ className.toLowerCase() +"Repository) {\n" +
                            "        this."+ className.toLowerCase() +"Repository = "+ className.toLowerCase() +"Repository;\n" +
                            "    }\n" +
                            "\n" +
                            "    public "+className+" get"+className+"(int id){\n" +
                            "        return "+ className.toLowerCase() +"Repository.getById(id);\n" +
                            "    }\n" +
                            "    public List<"+className+"> getAll"+className+"(){\n" +
                            "        return "+ className.toLowerCase() +"Repository.getAll();\n" +
                            "    }\n" +
                            "    public boolean update"+className+"("+className+" "+ className.toLowerCase() +"){\n" +
                            "        return "+ className.toLowerCase() +"Repository.update("+ className.toLowerCase() +");\n" +
                            "    }\n" +
                            "    public boolean delete"+className+"("+className+" "+ className.toLowerCase() +"){\n" +
                            "        return "+ className.toLowerCase() +"Repository.delete("+ className.toLowerCase() +");\n" +
                            "    }\n" +
                            "    public boolean add"+className+"("+className+" "+ className.toLowerCase() +"){\n" +
                            "        return "+ className.toLowerCase() +"Repository.insert("+ className.toLowerCase() +");\n" +
                            "    }\n" +
                            "\n" +
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