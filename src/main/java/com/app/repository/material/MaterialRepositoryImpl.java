package main.java.com.app.repository.material;

import main.java.com.app.entities.Material;
import  main.myframework.orm.Orm;


import java.util.List;

public class MaterialRepositoryImpl extends Orm<Material> implements MaterialRepository {


    @Override
    protected Class<Material> getEntityClass() {
        return Material.class;
    }

    @Override
    public Material insert(Material entity) {
        return super.insert(entity);
    }

    @Override
    public Material update(Material entity) {
        return super.update(entity);
    }

    @Override
    public boolean delete(Material material) {
        return  super.delete(material);
    }

    @Override
    public Material getById(Integer ID) {
        return (Material) super.getById(ID);
    }

    @Override
    public List<Material> getAll() {
        return super.all();
    }
}
