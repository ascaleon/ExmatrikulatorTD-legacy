package de.diegrafen.exmatrikulatortd.model.tower;

import de.diegrafen.exmatrikulatortd.model.BaseModel;

import javax.persistence.Entity;
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

    private String name;

    /**
     * Gibt an, wie lange der Buff (noch) wirksam ist
     */
    private float duration;

    /**
     * Multiplikator der Angriffsgeschwindikgeit
     */
    private float attackSpeedMultiplier;

    /**
     * Multiplikator des Angriffsschadens
     */
    private float attackDamageMultiplier;

    private boolean permanent;

    /**
     * Default-Konstruktur. Wird von JPA vorausgesetzt.
     */
    public Buff() {
    }

    /**
     * Konstruktor, der alle Attribute initialisiert
     * @param duration Die Dauer des Buffs
     * @param attackSpeedMultiplier Der Angriffsgeschwindigkeitsmultiplikator des Buffs
     * @param attackDamageMultiplier Der Angriffsschadensmultiplikator des Buffs
     */
    public Buff(String name, float duration, float attackSpeedMultiplier, float attackDamageMultiplier, boolean permanent) {
        this.name = name;
        this.duration = duration;
        this.attackSpeedMultiplier = attackSpeedMultiplier;
        this.attackDamageMultiplier = attackDamageMultiplier;
        this.permanent = permanent;
    }

    /**
     * Kopier-Konstruktor
     *
     * @param buff Der Buff, der kopiert werden soll
     */
    public Buff(Buff buff) {
        this.name = buff.getName();
        this.duration = buff.getDuration();
        this.attackSpeedMultiplier = buff.getAttackSpeedMultiplier();
        this.attackDamageMultiplier = buff.getAttackDamageMultiplier();
        this.permanent = buff.isPermanent();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getDuration() {
        return duration;
    }

    public void setDuration(float duration) {
        this.duration = duration;
    }

    public float getAttackSpeedMultiplier() {
        return attackSpeedMultiplier;
    }

    public float getAttackDamageMultiplier() {
        return attackDamageMultiplier;
    }

    public boolean isPermanent() {
        return permanent;
    }
}
