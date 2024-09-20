package main.java.com.app.repository.estimate;

import main.java.com.app.entities.Estimate;
import main.myframework.orm.Orm;


import java.util.List;

public class EstimateRepositoryImpl extends Orm<Estimate> implements EstimateRepository {


    @Override
    protected Class<Estimate> getEntityClass() {
        return Estimate.class;
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
    public boolean delete(Estimate estimate) {
        return  super.delete(estimate);
    }

    @Override
    public Estimate getById(Integer ID) {
        return (Estimate) super.getById(ID);
    }

    @Override
    public List<Estimate> getAll() {
        return super.all();
    }
}
