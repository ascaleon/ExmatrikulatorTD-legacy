package de.diegrafen.exmatrikulatortd.model.enemy;

import de.diegrafen.exmatrikulatortd.model.BaseModel;
import de.diegrafen.exmatrikulatortd.model.enemy.Enemy;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Beinhaltet Modifikatoren für die Rüstung, die Geschwindgkeit und die Lebenspunkte eines Gegners.
 *
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 15.06.2019 20:54
 */
@Entity
@Table(name = "Debuffs")
public class Debuff extends BaseModel implements Cloneable {

    /**
     * Die eindeutige Serialisierungs-ID
     */
    static final long serialVersionUID = 11958105127419241L;

    private String name;

    /**
     * Gibt an, wie lange der Debuff (noch) wirksam ist
     */
    private float duration;

    /**
     * Modifikator der Rüstung
     */
    private float armorModifier;

    /**
     * Modifikator der Geschwindigkeit
     */
    private float speedModifier;

    /**
     * Modifikator der Lebenspunkte
     */
    private float healthModifier;

    /**
     * Default-Konstruktur. Wird von JPA vorausgesetzt.
     */
    public Debuff() {

    }


    /**
     * Konstruktor, der alle Attribute initialisiert
     *
     * @param duration       Die Dauer des Debuffs
     * @param armorModifier  Der Rüstungsmodifikator des Debuffs
     * @param speedModifier  Der Geschwindigkeitsmodifikator des Debuffs
     * @param healthModifier Der Gesundheitsmodifikator des Debuffs
     */
    public Debuff(String name, float duration, float armorModifier, float speedModifier, float healthModifier) {
        this.name = name;

        if (duration < 0) {
            this.duration = 0;
        } else {
            this.duration = duration;
        }

        if (armorModifier < -1) {
            this.armorModifier = -1;
        } else {
            this.armorModifier = armorModifier;
        }

        if (speedModifier < -1) {
            this.speedModifier = -1;
        } else {
            this.speedModifier = speedModifier;
        }

        if (healthModifier < -1) {
            this.healthModifier = -1;
        } else {
            this.healthModifier = healthModifier;
        }
    }

    /**
     * Kopier-Konstruktor
     *
     * @param debuff
     */
    public Debuff(Debuff debuff) {
        this.name = debuff.getName();
        this.duration = debuff.getDuration();
        this.armorModifier = debuff.getArmorModifier();
        this.speedModifier = debuff.getSpeedModifier();
        this.healthModifier = debuff.getHealthModifier();
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

    public float getArmorModifier() {
        return armorModifier;
    }

    public float getSpeedModifier() {
        return speedModifier;
    }

    public float getHealthModifier() {
        return healthModifier;
    }
}
