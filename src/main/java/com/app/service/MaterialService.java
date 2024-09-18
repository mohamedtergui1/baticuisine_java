package main.java.com.app.service;

import main.java.com.app.entities.Material;
import main.java.com.app.repository.material.MaterialRepository;

import java.util.List;

public class MaterialService {
    MaterialRepository materialRepository;
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
