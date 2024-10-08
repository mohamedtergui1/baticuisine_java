package main.java.com.app.repository.labor;

import main.java.com.app.entities.Labor;
import  main.myframework.orm.Orm;


import java.util.List;

public class LaborRepositoryImpl extends Orm<Labor> implements LaborRepository {


    @Override
    protected Class<Labor> getEntityClass() {
        return Labor.class;
    }

    @Override
    public Labor insert(Labor entity) {
        return super.insert(entity);
    }

    @Override
    public Labor update(Labor entity) {
        return super.update(entity);
    }

    @Override
    public boolean delete(Labor labor) {
        return  super.delete(labor);
    }

    @Override
    public Labor getById(Integer ID) {
        return (Labor) super.getById(ID);
    }

    @Override
    public List<Labor> getAll() {
        return super.all();
    }
}
