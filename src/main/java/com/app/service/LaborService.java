package main.java.com.app.service;

import main.java.com.app.entities.Labor;
import main.java.com.app.repository.labor.LaborRepository;

import java.util.List;

public class LaborService {
    LaborRepository laborRepository;
    public LaborService(LaborRepository laborRepository) {
        this.laborRepository = laborRepository;
    }

    public Labor getLabor(int id){
        return laborRepository.getById(id);
    }
    public List<Labor> getAllLabor(){
        return laborRepository.getAll();
    }
    public boolean updateLabor(Labor labor){
        return laborRepository.update(labor);
    }
    public boolean deleteLabor(Labor labor){
        return laborRepository.delete(labor);
    }
    public boolean addLabor(Labor labor){
        return laborRepository.insert(labor);
    }

}
