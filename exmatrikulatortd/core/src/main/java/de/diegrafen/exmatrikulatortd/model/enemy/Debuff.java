package de.diegrafen.exmatrikulatortd.model.enemy;

import de.diegrafen.exmatrikulatortd.model.BaseModel;
import de.diegrafen.exmatrikulatortd.model.enemy.Enemy;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * Beinhaltet Modifikatoren für die Rüstung, die Geschwindgkeit und die Lebenspunkte eines Gegners.
 *
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 15.06.2019 20:54
 */
@Entity
@Table(name = "Debuffs")
public class Debuff extends BaseModel {

    /**
     * Die eindeutige Serialisierungs-ID
     */
    static final long serialVersionUID = 11958105127419241L;

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
     * Der mit dem Debuff assoziierte Gegner
     */
    @ManyToOne
    private Enemy enemy;

    /**
     * Default-Konstruktur. Wird von JPA vorausgesetzt.
     */
    public Debuff() {

    }

    /**
     * Konstruktor, der alle Attribute initialisiert
     * @param duration Die Dauer des Debuffs
     * @param armorModifier Der Rüstungsmodifikator des Debuffs
     * @param speedModifier Der Geschwindigkeitsmodifikator des Debuffs
     * @param healthModifier Der Gesundheitsmodifikator des Debuffs
     * @param enemy Der Gegner, der mit dem Debuff belegt ist
     */
    public Debuff(float duration, float armorModifier, float speedModifier, float healthModifier, Enemy enemy) {
        this.duration = duration;
        this.armorModifier = armorModifier;
        this.speedModifier = speedModifier;
        this.healthModifier = healthModifier;
        this.enemy = enemy;
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

    public void setArmorModifier(float armorModifier) {
        this.armorModifier = armorModifier;
    }

    public float getSpeedModifier() {
        return speedModifier;
    }

    public void setSpeedModifier(float speedModifier) {
        this.speedModifier = speedModifier;
    }

    public float getHealthModifier() {
        return healthModifier;
    }

    public void setHealthModifier(float healthModifier) {
        this.healthModifier = healthModifier;
    }

    public Enemy getEnemy() {
        return enemy;
    }

    public void setEnemy(Enemy enemy) {
        this.enemy = enemy;
    }
}
