package main.java.com.app.repository;

import java.sql.SQLException;
import java.util.List;

public interface BaseRepository<T, ID> {
    T insert(T entity);
    T update(T entity);
    boolean delete(T entity);
    T getById(ID id);
    List<T> getAll();
}
