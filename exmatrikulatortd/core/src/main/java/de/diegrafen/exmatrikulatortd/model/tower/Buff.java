package de.diegrafen.exmatrikulatortd.model.tower;

import de.diegrafen.exmatrikulatortd.model.BaseModel;
import de.diegrafen.exmatrikulatortd.model.enemy.Debuff;

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

    private String name;

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

    private boolean permanent;

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
     */
    public Buff(String name, float duration, float attackSpeedModifier, float attackDamageModifier, boolean permanent) {
        this.name = name;
        this.duration = duration;
        this.attackSpeedModifier = attackSpeedModifier;
        this.attackDamageModifier = attackDamageModifier;
        this.permanent = permanent;
    }

    /**
     * Kopier-Konstruktor
     *
     * @param buff
     */
    public Buff(Buff buff) {
        this.name = buff.getName();
        this.duration = buff.getDuration();
        this.attackSpeedModifier = buff.getAttackSpeedModifier();
        this.attackDamageModifier = buff.getAttackDamageModifier();
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

    public boolean isPermanent() {
        return permanent;
    }

    public void setPermanent(boolean permanent) {
        this.permanent = permanent;
    }
}
