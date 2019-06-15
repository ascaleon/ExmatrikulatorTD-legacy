package de.diegrafen.exmatrikulatortd.model.enemy;

import de.diegrafen.exmatrikulatortd.model.BaseModel;
import de.diegrafen.exmatrikulatortd.model.enemy.Enemy;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 15.06.2019 20:54
 */
@Entity
@Table(name = "Debuffs")
public class Debuff extends BaseModel {

    private float armorModifier;

    private float speedModifier;

    private float healthModifier;

    @ManyToOne
    private Enemy enemy;

    public Debuff() {

    }

    public Debuff (float armorModifier, float speedModifier, float healthModifier) {
        this.armorModifier = armorModifier;
        this.speedModifier = speedModifier;
        this. healthModifier = healthModifier;
    }
}
