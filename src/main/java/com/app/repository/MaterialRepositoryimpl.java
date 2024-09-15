package main.java.com.app.repository;

import main.java.com.app.entities.Material;
import main.migrations.orm.Orm;

;
import java.util.List;

public class MaterialRepositoryimpl extends Orm<Material> implements BaseRepository<Material,Integer> {

    @Override
    protected Class<Material> getEntityClass() {
        return Material.class;
    }

    @Override
    public boolean insert(Material entity) {
        return super.insert(entity);
    }

    @Override
    public boolean update(Material entity) {
        return super.update(entity);
    }

    @Override
    public boolean delete(Material integer) {
        return super.delete(integer);
    }

    @Override
    public Material getById(Integer integer) {
        return null;
    }

    @Override
    public List<Material> getAll() {
        return super.all();
    }
}
