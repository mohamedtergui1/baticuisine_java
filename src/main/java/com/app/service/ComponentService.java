package main.java.com.app.service;

import main.java.com.app.entities.Component;
import main.java.com.app.repository.component.ComponentRepository;

import java.util.List;

public class ComponentService {
    ComponentRepository componentRepository;
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
