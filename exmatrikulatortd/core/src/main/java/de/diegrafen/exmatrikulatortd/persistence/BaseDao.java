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
        return null;
    }

    /**
     * Öffnet die aktuelle Session mit einer begonnenen Transaktion
     * @return Gibt die geöffnete Session mit einer Transaktion zurück
     */
    public Session openCurrentSessionwithTransaction() {
        return null;
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

    }

    /**
     * Entnimmt ein Objekt nach seiner ID aus der Datenbank
     * @param id Die ID des Objektes
     * @return
     */
    @Override
    public T retrieve(final Long id) {
        return null;
    }

    /**
     * Erzeugt einen neuen Datenbank-Eintrag für ein Objekt
     * @param t Das Objekt, für das der Datenbank-Eintrag erstellt werden soll
     */
    @Override
    public void create(final T t){

    }

    /**
     * Aktualisiert den Datenbankeintrag eines Objektes
     * @param t Das Objekt, dessen Datenbankeintrag aktualisiert werden soll
     */
    @Override
    public void update(final T t) {

    }

    /**
     * Löscht ein Objekt aus der Datenbank
     * @param t Das zu löschende Objekt
     */
    @Override
    public void delete(final T t) {

    }

    /**
     * Gibt eine Liste aller Objekte des mit dem DAO-OBjekt assoziierten Typs in der Datenbank zurück
     * @return Eine Liste aller Objekte des mit dem DAO-OBjekt assoziierten Typs in der Datenbank
     */
    public List<T> findAll() {
        return null;
    }

    /**
     * Löscht alle Einträge des mit dem DAO-OBjekt assoziierten Typs in der Datenbank
     */
    public void deleteAll() {

    }
}
