package main.java.com.app.service;

import main.java.com.app.entities.Project;
import main.java.com.app.repository.project.ProjectRepository;

import java.util.List;

public class ProjectService {
    ProjectRepository projectRepository;
    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public Project getProject(int id){
        return projectRepository.getById(id);
    }
    public List<Project> getAllProject(){
        return projectRepository.getAll();
    }
    public boolean updateProject(Project project){
        return projectRepository.update(project);
    }
    public boolean deleteProject(Project project){
        return projectRepository.delete(project);
    }
    public boolean addProject(Project project){
        return projectRepository.insert(project);
    }

}
