package main.java.com.app.repository;

import main.java.com.app.entities.Project;
import main.migrations.orm.Orm;

import java.sql.SQLException;
import java.util.List;

public class ProjectRepositoryImpl extends Orm<Project> implements BaseRepository<Project,Integer> {
    @Override
    protected Class<Project> getEntityClass() {
        return Project.class;
    }

    @Override
    public boolean insert(Project entity) {
       return super.insert(entity);

    }

    @Override
    public boolean update(Project entity) {
       return super.update(entity);
    }

    @Override
    public boolean delete(Project integer) {
       return super.delete(integer);
    }

    @Override
    public Project getById(Integer integer) {
        return (Project) super.getById(integer);
    }

    @Override
    public List<Project> getAll() throws SQLException {
        return super.all();
    }
}
