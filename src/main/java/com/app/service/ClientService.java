package main.java.com.app.service;

import main.java.com.app.repository.client.ClientRepository;

public class ClientService {
    ClientRepository clientRepository;
    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }


}
