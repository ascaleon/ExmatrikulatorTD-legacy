package de.diegrafen.exmatrikulatortd.model.tower;

import de.diegrafen.exmatrikulatortd.model.BaseModel;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 15.06.2019 20:57
 */
@Entity
@Table(name = "Buffs")
public class Buff extends BaseModel {

    private float attackSpeedModifier;

    private float attackDamageModifier;

    @ManyToOne
    private Tower tower;

    public Buff() {
    }

    public Buff (float attackSpeedModifier, float attackDamageModifier) {
        this.attackSpeedModifier = attackSpeedModifier;
        this.attackDamageModifier =attackDamageModifier;
    }
}
