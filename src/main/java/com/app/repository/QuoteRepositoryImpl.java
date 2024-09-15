package main.java.com.app.repository;

import main.java.com.app.entities.Quote;
import main.migrations.orm.Orm;

import java.sql.SQLException;
import java.util.List;

public class QuoteRepositoryImpl extends Orm<Quote> implements BaseRepository<Quote,Integer> {
    @Override
    protected Class<Quote> getEntityClass() {
        return null;
    }

    @Override
    public boolean insert(Quote entity) {
        return super.insert(entity);
    }

    @Override
    public boolean update(Quote entity) {
        return super.update(entity);
    }

    @Override
    public boolean delete(Quote integer) {
        return super.delete(integer);
    }

    @Override
    public Quote getById(Integer integer) {
        return (Quote) super.getById(integer);
    }

    @Override
    public List<Quote> getAll() throws SQLException {
        return super.all();
    }
}
