package main.java.com.app.repository;

import main.java.com.app.entities.Estimate;
import main.migrations.orm.Orm;

import java.sql.SQLException;
import java.util.List;

public class EstimateRepositoryImpl extends Orm<Estimate> implements BaseRepository<Estimate,Integer> {
    @Override
    protected Class<Estimate> getEntityClass() {
        return null;
    }

    @Override
    public boolean insert(Estimate entity) {
        return super.insert(entity);
    }

    @Override
    public boolean update(Estimate entity) {
        return super.update(entity);
    }

    @Override
    public boolean delete(Estimate integer) {
        return super.delete(integer);
    }

    @Override
    public Estimate getById(Integer integer) {
        return (Estimate) super.getById(integer);
    }

    @Override
    public List<Estimate> getAll() throws SQLException {
        return super.all();
    }
}
