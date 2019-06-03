package de.diegrafen.towerwars.persistence;

import de.diegrafen.towerwars.models.BaseEntity;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

import static de.diegrafen.towerwars.util.HibernateUtils.getSessionFactory;

public abstract class BaseDao<T extends BaseEntity> implements Dao<T>  {

    private Session currentSession;

    private Transaction currentTransaction;

    abstract Class<T> getClazz();

    public Session openCurrentSession() {
        currentSession = getSessionFactory().openSession();
        return currentSession;
    }

    public Session openCurrentSessionwithTransaction() {
        currentSession = getSessionFactory().openSession();
        currentTransaction = currentSession.beginTransaction();
        return currentSession;
    }

    public void closeCurrentSession() {
        currentSession.close();
    }

    public void closeCurrentSessionwithTransaction() {
        currentTransaction.commit();
        currentSession.close();
    }

    public T retrieve(final Long id) {
        T t = openCurrentSessionwithTransaction().get(getClazz(), id);
        closeCurrentSessionwithTransaction();
        return t;
    }

    @Override
    public void create(final T t) throws Exception {
        if (t == null) {
            throw new IllegalArgumentException();
        }
        //assertNotNull(t);
        openCurrentSessionwithTransaction().persist(t);
        closeCurrentSessionwithTransaction();
        //entityManager.persist(t);
    }

    @Override
    public void update(final T t) throws Exception {
        if (t == null) {
            throw new IllegalArgumentException();
        }
        //assertNotNull(t);
        final Long id = t.getId();
        if(id <= 0) throw new IllegalArgumentException("The id of the parameter must not be zero!");
        else {
            if (retrieve(id) != null) {
                openCurrentSessionwithTransaction().update(t);
                closeCurrentSessionwithTransaction();
                //entityManager.merge(t);
            } else {
                throw new IllegalArgumentException("The specified object does not exist!");
            }
        }
    }

    @Override
    public synchronized void delete(final T t) {
        if (t == null) {
            throw new IllegalArgumentException();
        }
        //assertNotNull(t);
        if (t.getId() > 0) {
            final T entity = retrieve(t.getId());
            if (entity != null) {
                openCurrentSessionwithTransaction().delete(t);
                closeCurrentSessionwithTransaction();
            }
            t.clearId();
        }
        else throw new IllegalArgumentException("The id of the parameter must not be zero!");
    }

    //@SuppressWarnings("unchecked")
    public List<T> findAll() {
        openCurrentSession();
        List<T> list = currentSession.createNamedQuery(getClazz().getSimpleName() + ".findAll", getClazz()).getResultList();
        closeCurrentSession();
        return list;
    }

    public void deleteAll() {
        List<T> entityList = findAll();
        for (T t : entityList) {
            delete(t);
        }
    }
}
