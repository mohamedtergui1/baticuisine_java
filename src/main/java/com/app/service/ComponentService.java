package main.java.com.app.service;
import main.myframework.annotation.InjectClass;

import main.java.com.app.entities.Component;
import main.java.com.app.repository.component.ComponentRepository;

import main.java.com.app.repository.component.ComponentRepositoryImpl;
import java.util.List;

public class ComponentService {
    ComponentRepository componentRepository;
    @InjectClass(ComponentRepositoryImpl.class)
    public ComponentService(ComponentRepository componentRepository) {
        this.componentRepository = componentRepository;
    }

    public Component getComponent(int id){
        return componentRepository.getById(id);
    }
    public List<Component> getAllComponent(){
        return componentRepository.getAll();
    }
    public boolean updateComponent(Component component){
        return componentRepository.update(component);
    }
    public boolean deleteComponent(Component component){
        return componentRepository.delete(component);
    }
    public boolean addComponent(Component component){
        return componentRepository.insert(component);
    }

}
