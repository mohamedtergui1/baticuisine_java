package main.java.com.app.service;
import main.myframework.annotation.InjectClass;

import main.java.com.app.entities.Project;
import main.java.com.app.repository.project.ProjectRepository;

import main.java.com.app.repository.project.ProjectRepositoryImpl;
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

}
