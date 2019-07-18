package de.diegrafen.exmatrikulatortd.model;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

/**
 * Superklasse für alle Klassen im model-Paket. Verfügt über ein ID-Attribut, das automatisch bei Abgleichung
 * mit der Datenbank zugewiesen wird.
 */
@MappedSuperclass
public abstract class BaseModel implements Serializable {

    /**
     * Die eindeutige Serialisierungs-ID
     */
    static final long serialVersionUID = 3758163733L;

    /**
     * Die ID des Objektes
     */
    @Id
    @GeneratedValue
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Setzt die Id des Objektes auf {@code 0}.
     */
    public void clearId() {
        this.id = 0L;
    }

    @Override
    public String toString() {
        return String.format(getClass().getSimpleName() +  " {id: %d}", id);
    }

    @Override
    public int hashCode() {
        return id.intValue();
    }
}
