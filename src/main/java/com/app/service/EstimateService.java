package main.java.com.app.service;

import main.java.com.app.entities.Estimate;
import main.java.com.app.repository.estimate.EstimateRepository;

import java.util.List;

public class EstimateService {
    EstimateRepository estimateRepository;
    public EstimateService(EstimateRepository estimateRepository) {
        this.estimateRepository = estimateRepository;
    }

    public Estimate getEstimate(int id){
        return estimateRepository.getById(id);
    }
    public List<Estimate> getAllEstimate(){
        return estimateRepository.getAll();
    }
    public boolean updateEstimate(Estimate estimate){
        return estimateRepository.update(estimate);
    }
    public boolean deleteEstimate(Estimate estimate){
        return estimateRepository.delete(estimate);
    }
    public boolean addEstimate(Estimate estimate){
        return estimateRepository.insert(estimate);
    }

}
