package de.diegrafen.exmatrikulatortd.persistence;

import de.diegrafen.exmatrikulatortd.model.BaseModel;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

import static de.diegrafen.exmatrikulatortd.util.HibernateUtils.getSessionFactory;

/**
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 14.06.2019 02:26
 */
public abstract class BaseDao<T extends BaseModel> implements Dao<T>  {

    private Session currentSession;

    private Transaction currentTransaction;

    abstract Class<T> getClazz();

    public Session openCurrentSession() {
        return null;
    }

    public Session openCurrentSessionwithTransaction() {
        return null;
    }

    public void closeCurrentSession() {
        currentSession.close();
    }

    public void closeCurrentSessionwithTransaction() {

    }

    @Override
    public T retrieve(final Long id) {
        return null;
    }

    @Override
    public void create(final T t){

    }

    @Override
    public void update(final T t) {

    }

    @Override
    public void delete(final T t) {

    }

    public List<T> findAll() {
        return null;
    }

    public void deleteAll() {

    }
}
