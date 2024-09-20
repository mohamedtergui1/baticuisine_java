package main.java.com.app.repository.component;

import main.java.com.app.entities.Component;
import main.myframework.orm.Orm;


import java.util.List;

public class ComponentRepositoryImpl extends Orm<Component> implements ComponentRepository {


    @Override
    protected Class<Component> getEntityClass() {
        return Component.class;
    }

    @Override
    public boolean insert(Component entity) {
        return super.insert(entity);
    }

    @Override
    public boolean update(Component entity) {
        return super.update(entity);
    }

    @Override
    public boolean delete(Component component) {
        return  super.delete(component);
    }

    @Override
    public Component getById(Integer ID) {
        return (Component) super.getById(ID);
    }

    @Override
    public List<Component> getAll() {
        return super.all();
    }
}
