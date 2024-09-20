package main.java.com.app.repository.client;

import main.java.com.app.entities.Client;
import main.myframework.orm.Orm;


import java.util.List;

public class ClientRepositoryImpl extends Orm<Client> implements ClientRepository {

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

    @Override
    public List<Client> searchByName(String name) {
        return super.buildQuery().select().whereLike("name",name).fetchAll();
    }

}
