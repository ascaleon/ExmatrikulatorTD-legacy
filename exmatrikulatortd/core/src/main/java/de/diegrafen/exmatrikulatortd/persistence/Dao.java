package de.diegrafen.exmatrikulatortd.persistence;

import java.io.Serializable;

/**
 *
 * Interface für Dao-Objekte. Setzt die Implementierung der CRUD-Operationen (Create, Retrieve, Update, Delete) voraus
 *
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 14.06.2019 02:18
 */
interface Dao<T> extends Serializable {

    /**
     * Fügt ein Objekt der Datenbank hinzu
     * @param object Das hinzuzufügende Objekt
     * @throws IllegalArgumentException Wenn ein nicht zulässiges Objekt der Datenbank hinzugefügt werden soll
     */
    void create(final T object) throws IllegalArgumentException;

    /**
     * Entnimmt ein Objekt der Datenbank unter Angabe seiner ID
     * @param id Die ID des Objektes
     * @return Das zu entnehmende Objekt
     */
    T retrieve(Long id);

    /**
     * Aktualisiert den Datenbankeintrag eines Objektes
     * @param object Das zu aktualisierende Objekt
     * @throws IllegalArgumentException Wenn der Datenbankeintrag eines nicht zulässigen Objekt aktualisiert werden soll
     */
    void update(T object) throws IllegalArgumentException;

    /**
     * Löscht ein Objekt aus der Datenbank
     * @param object Das zu löschende Objekt
     * @throws IllegalArgumentException Wenn ein nicht zulässiges Objekt aus der Datenbank entfernt werden soll
     */
    void delete(final T object) throws IllegalArgumentException;
}
