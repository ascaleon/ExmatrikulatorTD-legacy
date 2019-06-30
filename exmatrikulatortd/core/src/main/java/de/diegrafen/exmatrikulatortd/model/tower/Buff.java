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
     * Gibt an, wie lange der Buff (noch) wirksam ist
     */
    private float duration;

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
     * @param duration Die Dauer des Buffs
     * @param attackSpeedModifier Der Angriffsgeschwindigkeitsmodifikator des Buffs
     * @param attackDamageModifier Der Angriffsschadensmodifikator des Buffs
     * @param tower Der Turm, der mit dem Debuff belegt ist
     */
    public Buff(float duration, float attackSpeedModifier, float attackDamageModifier, Tower tower) {
        this.duration = duration;
        this.attackSpeedModifier = attackSpeedModifier;
        this.attackDamageModifier = attackDamageModifier;
        this.tower = tower;
    }

    public float getDuration() {
        return duration;
    }

    public void setDuration(float duration) {
        this.duration = duration;
    }

    public float getAttackSpeedModifier() {
        return attackSpeedModifier;
    }

    public void setAttackSpeedModifier(float attackSpeedModifier) {
        this.attackSpeedModifier = attackSpeedModifier;
    }

    public float getAttackDamageModifier() {
        return attackDamageModifier;
    }

    public void setAttackDamageModifier(float attackDamageModifier) {
        this.attackDamageModifier = attackDamageModifier;
    }

    public Tower getTower() {
        return tower;
    }

    public void setTower(Tower tower) {
        this.tower = tower;
    }
}
