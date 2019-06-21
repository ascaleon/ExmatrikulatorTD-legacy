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
     */
    public Debuff (float armorModifier, float speedModifier, float healthModifier) {
        this.armorModifier = armorModifier;
        this.speedModifier = speedModifier;
        this. healthModifier = healthModifier;
    }
}
