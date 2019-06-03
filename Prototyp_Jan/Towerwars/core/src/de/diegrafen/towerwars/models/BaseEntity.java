package de.diegrafen.towerwars.models;

import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

//@EntityListeners(JPAEntityListener.class)
@MappedSuperclass
public class BaseEntity implements Serializable {

    static final long serialVersionUID = 3758163733L;

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
        this.id = 0l;
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
