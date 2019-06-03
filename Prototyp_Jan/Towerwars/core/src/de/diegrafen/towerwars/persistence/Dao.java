package de.diegrafen.towerwars.persistence;

import java.io.Serializable;
import java.util.List;

public interface Dao<T> extends Serializable {

    void create(final T object) throws Exception;

    T retrieve(Long id) throws Exception;

    void update(T object) throws Exception;

    void delete(final T object) throws Exception;
}