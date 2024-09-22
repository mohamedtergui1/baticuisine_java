package main.java.com.app.repository.project;

import main.java.com.app.entities.Project;
import  main.myframework.orm.Orm;


import java.util.List;

public class ProjectRepositoryImpl extends Orm<Project> implements ProjectRepository {


    @Override
    protected Class<Project> getEntityClass() {
        return Project.class;
    }

    @Override
    public Project insert(Project entity) {
        return super.insert(entity);
    }

    @Override
    public Project update(Project entity) {
        return super.update(entity);
    }

    @Override
    public boolean delete(Project project) {
        return  super.delete(project);
    }

    @Override
    public Project getById(Integer ID) {
        return (Project) super.getById(ID);
    }

    @Override
    public List<Project> getAll() {
        return super.all();
    }
}
