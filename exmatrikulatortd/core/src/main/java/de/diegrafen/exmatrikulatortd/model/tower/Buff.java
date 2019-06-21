package de.diegrafen.exmatrikulatortd.model.tower;

import de.diegrafen.exmatrikulatortd.model.BaseModel;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * Beinhaltet Modifikatoren für Angriffsgeschwindigkeit und -schaden eines Turmes.
 *
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 15.06.2019 20:57
 */
@Entity
@Table(name = "Buffs")
public class Buff extends BaseModel {

    /**
     * Die eindeutige Serialisierungs-ID
     */
    static final long serialVersionUID = 98478993L;

    /**
     * Modifikator der Angriffsgeschwindikgeit
     */
    private float attackSpeedModifier;

    /**
     * Modifikator des Angriffsschadens
     */
    private float attackDamageModifier;

    /**
     * Der mit dem Buff assoziierte Turm
     */
    @ManyToOne
    private Tower tower;

    /**
     * Default-Konstruktur. Wird von JPA vorausgesetzt.
     */
    public Buff() {
    }

    /**
     * Konstruktor, der alle Attribute initialisiert
     */
    public Buff (float attackSpeedModifier, float attackDamageModifier) {
        this.attackSpeedModifier = attackSpeedModifier;
        this.attackDamageModifier =attackDamageModifier;
    }
}
