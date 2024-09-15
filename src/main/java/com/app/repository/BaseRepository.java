package main.java.com.app.repository;

import java.sql.SQLException;
import java.util.List;

public interface BaseRepository<T, ID> {
    boolean insert(T entity) throws SQLException;
    boolean update(T entity) throws SQLException;
    boolean delete(T entity) throws SQLException;
    T getById(ID id) throws SQLException;
    List<T> getAll() throws SQLException;
}
