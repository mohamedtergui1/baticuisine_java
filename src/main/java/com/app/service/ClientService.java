package main.java.com.app.service;
import main.myframework.annotation.InjectClass;

import main.java.com.app.entities.Client;
import main.java.com.app.repository.client.ClientRepository;

import main.java.com.app.repository.client.ClientRepositoryImpl;
import java.util.List;

public class ClientService {
    ClientRepository clientRepository;
    @InjectClass(ClientRepositoryImpl.class)
    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public Client getClient(int id){
        return clientRepository.getById(id);
    }
    public List<Client> getAllClient(){
        return clientRepository.getAll();
    }
    public boolean updateClient(Client client){
        return clientRepository.update(client);
    }
    public boolean deleteClient(Client client){
        return clientRepository.delete(client);
    }
    public boolean addClient(Client client){
        return clientRepository.insert(client);
    }
    public List<Client> SearchByName(String name){
        return clientRepository.searchByName(name);
    }
}
