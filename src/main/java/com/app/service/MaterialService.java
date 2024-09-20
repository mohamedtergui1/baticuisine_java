package main.java.com.app.service;
import main.myframework.annotation.InjectClass;

import main.java.com.app.entities.Material;
import main.java.com.app.repository.material.MaterialRepository;

import main.java.com.app.repository.material.MaterialRepositoryImpl;
import java.util.List;

public class MaterialService {
    MaterialRepository materialRepository;
    @InjectClass(MaterialRepositoryImpl.class)
    public MaterialService(MaterialRepository materialRepository) {
        this.materialRepository = materialRepository;
    }

    public Material getMaterial(int id){
        return materialRepository.getById(id);
    }
    public List<Material> getAllMaterial(){
        return materialRepository.getAll();
    }
    public boolean updateMaterial(Material material){
        return materialRepository.update(material);
    }
    public boolean deleteMaterial(Material material){
        return materialRepository.delete(material);
    }
    public boolean addMaterial(Material material){
        return materialRepository.insert(material);
    }

}
