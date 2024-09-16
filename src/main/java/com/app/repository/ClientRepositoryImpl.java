package main.java.com.app.repository;

import main.java.com.app.entities.Client;
import main.migrations.orm.Orm;


import java.util.List;

public class ClientRepositoryImpl extends Orm<Client> implements BaseRepository<Client,Integer> {


    @Override
    protected Class<Client> getEntityClass() {
        return Client.class;
    }

    @Override
    public boolean insert(Client entity) {
        return super.insert(entity);
    }

    @Override
    public boolean update(Client entity) {
        return super.update(entity);
    }

    @Override
    public boolean delete(Client client) {
        return  super.delete(client);
    }

    @Override
    public Client getById(Integer ID) {
        return (Client) super.getById(ID);
    }

    @Override
    public List<Client> getAll() {
        return super.all();
    }
}
