package de.diegrafen.exmatrikulatortd.persistence;

import java.io.Serializable;

/**
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 14.06.2019 02:18
 */
public interface Dao<T> extends Serializable {

    void create(final T object) throws IllegalArgumentException;

    T retrieve(Long id) throws IllegalArgumentException;

    void update(T object) throws IllegalArgumentException;

    void delete(final T object) throws IllegalArgumentException;
}
