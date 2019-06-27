package de.diegrafen.exmatrikulatortd.persistence;

import de.diegrafen.exmatrikulatortd.model.BaseModel;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

import static de.diegrafen.exmatrikulatortd.util.HibernateUtils.getSessionFactory;

/**
 *
 * Basisklasse für DAO-Objekte.
 *
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 14.06.2019 02:26
 */
public abstract class BaseDao<T extends BaseModel> implements Dao<T>  {

    /**
     * Die aktuelle Session des Session-Managers
     */
    private Session currentSession;

    /**
     * Die aktuelle Datenbank-Transaktion
     */
    private Transaction currentTransaction;

    /**
     * Gibt die Klasse zurück, die mit dem DAO-Objekt assoziiert ist
     * @return
     */
    abstract Class<T> getClazz();

    /**
     * Öffnet die aktuelle Session
     * @return Gibt die geöffnete Session zurück
     */
    public Session openCurrentSession() {
        currentSession = getSessionFactory().openSession();
        return currentSession;
    }


    /**
     * Öffnet die aktuelle Session mit einer begonnenen Transaktion
     * @return Gibt die geöffnete Session mit einer Transaktion zurück
     */
    public Session openCurrentSessionwithTransaction() {
        currentSession = getSessionFactory().openSession();
        currentTransaction = currentSession.beginTransaction();
        return currentSession;
    }

    /**
     * Schließt die aktuelle Session
     */
    public void closeCurrentSession() {
        currentSession.close();
    }

    /**
     * Schließt die aktuelle Session
     */
    public void closeCurrentSessionwithTransaction() {
        currentTransaction.commit();
        currentSession.close();
    }

    /**
     * Entnimmt ein Objekt nach seiner ID aus der Datenbank
     * @param id Die ID des Objektes
     * @return
     */
    @Override
    public T retrieve(final Long id) {
        T t = openCurrentSessionwithTransaction().get(getClazz(), id);
        closeCurrentSessionwithTransaction();
        return t;
    }

    /**
     * Erzeugt einen neuen Datenbank-Eintrag für ein Objekt
     * @param t Das Objekt, für das der Datenbank-Eintrag erstellt werden soll
     */
    @Override
    public void create(final T t) {
        if (t == null) {
            throw new IllegalArgumentException();
        }
        openCurrentSessionwithTransaction().persist(t);
        closeCurrentSessionwithTransaction();
    }

    /**
     * Aktualisiert den Datenbankeintrag eines Objektes
     * @param t Das Objekt, dessen Datenbankeintrag aktualisiert werden soll
     */
    @Override
    public void update(final T t) {
        if (t == null) {
            throw new IllegalArgumentException();
        }
        final Long id = t.getId();
        if(id <= 0) throw new IllegalArgumentException("The id of the parameter must not be zero!");
        else {
            if (retrieve(id) != null) {
                openCurrentSessionwithTransaction().update(t);
                closeCurrentSessionwithTransaction();
            } else {
                throw new IllegalArgumentException("The specified object does not exist!");
            }
        }
    }

    /**
     * Löscht ein Objekt aus der Datenbank
     * @param t Das zu löschende Objekt
     */
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
}
