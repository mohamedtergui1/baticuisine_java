package main.java.com.app.service;
import main.java.com.app.entities.Labor;
import main.java.com.app.entities.Material;
import main.myframework.annotation.InjectClass;

import main.java.com.app.entities.Project;
import main.java.com.app.repository.project.ProjectRepository;

import main.java.com.app.repository.project.ProjectRepositoryImpl;
import main.myframework.injector.DependencyInjector;

import java.util.List;

public class ProjectService {
    ProjectRepository projectRepository;
    @InjectClass(ProjectRepositoryImpl.class)
    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public Project getProject(int id){
        return projectRepository.getById(id);
    }
    public List<Project> getAllProject(){
        return projectRepository.getAll();
    }
    public Project updateProject(Project project){
        return projectRepository.update(project);
    }
    public boolean deleteProject(Project project){
        return projectRepository.delete(project);
    }
    public Project addProject(Project project){
        return projectRepository.insert(project);
    }
    public Project calculProjectCost(Project project, double tva, double mb){
        project.getLabors().forEach((e)->{
            e.setVatRate(tva);
            e.setProject(project);
            DependencyInjector.createInstance(LaborService.class).updateLabor(e);
        });
        project.getMaterials().forEach((e)->{
            e.setVatRate(tva);
            e.setProject(project);
            DependencyInjector.createInstance(MaterialService.class).updateMaterial(e);
        });
        project.setTotalCost(project.getLabors().stream()
                .map(Labor::calculateCost)
                .reduce(0.0, Double::sum)  + project.getMaterials().stream().map(Material::calculateCost).reduce(0.0, Double::sum));
        project.setTotalCost(project.getTotalCost());
        project.setProfitMargin(mb);
        projectRepository.update(project);
        return project;
    }
}
