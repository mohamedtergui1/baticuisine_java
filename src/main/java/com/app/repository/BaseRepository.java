package main.java.com.app.repository;

import java.sql.SQLException;
import java.util.List;

public interface BaseRepository<T, ID> {
    boolean insert(T entity);
    boolean update(T entity);
    boolean delete(T entity);
    T getById(ID id);
    List<T> getAll();
}
