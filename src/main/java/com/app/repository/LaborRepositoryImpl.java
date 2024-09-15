package main.java.com.app.repository;

import main.java.com.app.entities.Labor;
import main.migrations.orm.Orm;

import java.sql.SQLException;
import java.util.List;

public class LaborRepositoryImpl extends Orm<Labor> implements BaseRepository<Labor,Integer> {
    @Override
    protected Class<Labor> getEntityClass() {
        return Labor.class;
    }

    @Override
    public boolean insert(Labor entity) {
        return super.insert(entity);
    }

    @Override
    public boolean update(Labor entity) {
        return super.update(entity);
    }

    @Override
    public boolean delete(Labor integer) {
        return super.delete(integer);
    }

    @Override
    public Labor getById(Integer integer) {
        return getById(integer);
    }

    @Override
    public List<Labor> getAll() throws SQLException {
        return super.all();
    }
}
