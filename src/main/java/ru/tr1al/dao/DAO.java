package ru.tr1al.dao;

import ru.tr1al.model.Model;

public interface DAO<T extends Model> {

    Class<T> getEntityClass();
}
