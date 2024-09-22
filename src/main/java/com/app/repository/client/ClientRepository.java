package main.java.com.app.repository.client;

import main.java.com.app.entities.Client;
import main.java.com.app.repository.BaseRepository;

import java.util.List;

public interface ClientRepository extends BaseRepository<Client, Integer> {
    List<Client> getByName(String line);
}
