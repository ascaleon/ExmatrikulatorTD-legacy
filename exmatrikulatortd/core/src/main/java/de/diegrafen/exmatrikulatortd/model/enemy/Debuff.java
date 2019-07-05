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
    private float armorBonus;

    /**
     * Modifikator der Geschwindigkeit
     */
    private float speedMultiplier;

    /**
     * Modifikator der Lebenspunkte
     */
    private float healthBonus;

    /**
     * Default-Konstruktur. Wird von JPA vorausgesetzt.
     */
    public Debuff() {
        this.speedMultiplier = 1;
    }

    /**
     * Konstruktor, der alle Attribute initialisiert
     *
     * @param duration       Die Dauer des Debuffs
     * @param armorModifier  Der Rüstungsmodifikator des Debuffs
     * @param speedModifier  Der Geschwindigkeitsmodifikator des Debuffs
     * @param healthModifier Der Gesundheitsmodifikator des Debuffs
     */


    public Debuff(String name, float duration, float armorBonus, float speedMultiplier, float healthBonus) {
        this.name = name;
        this.armorBonus = armorBonus;
        this.healthBonus = healthBonus;

        if (duration < 0) {
            this.duration = 0;
        } else {
            this.duration = duration;
        }

        if (speedMultiplier < 0) {
            this.speedMultiplier = 0;
        } else {
            this.speedMultiplier = speedMultiplier;
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
        this.armorBonus = debuff.getArmorBonus();
        this.speedMultiplier = debuff.getSpeedMultiplier();
        this.healthBonus = debuff.getHealthBonus();
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
        if (duration < 0) {
            this.duration = 0;
        } else {
            this.duration = duration;
        }
    }

    public float getArmorBonus() {
        return armorBonus;
    }

    public void setArmorBonus(float armorBonus) {
        this.armorBonus = armorBonus;
    }

    public float getSpeedMultiplier() {
        return speedMultiplier;
    }

    public void setSpeedMultiplier(float speedMultiplier) {
        if (speedMultiplier < 0) {
            this.speedMultiplier = 0;
        } else {
            this.speedMultiplier = speedMultiplier;
        }
    }

    public float getHealthBonus() {
        return healthBonus;
    }

    public void setHealthBonus(float healthBonus) {
        this.healthBonus = healthBonus;
    }
}
