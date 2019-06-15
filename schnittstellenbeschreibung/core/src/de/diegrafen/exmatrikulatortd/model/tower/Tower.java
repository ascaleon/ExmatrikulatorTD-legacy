package de.diegrafen.exmatrikulatortd.model.tower;

import de.diegrafen.exmatrikulatortd.model.BaseModel;
import de.diegrafen.exmatrikulatortd.model.Coordinates;
import de.diegrafen.exmatrikulatortd.model.Player;
import de.diegrafen.exmatrikulatortd.model.enemy.Enemy;

import javax.persistence.*;
import java.util.List;

/**
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 13.06.2019 21:58
 */
@Entity
@Table(name = "Towers")
public class Tower extends BaseModel {

    /**
     * Der durch den Turm verursachte Schaden.
     */
    private int attackDamage;

    /**
     * Die Reichweite des Turms.
     */
    private float attackRange;

    @Enumerated(EnumType.STRING)
    private AttackType attackType;

    @Enumerated(EnumType.STRING)
    private Aura aura;

    private float auraRange;

    private int price;

    private int sellPrice;

    private int upgradeLevel;

    @OneToOne(mappedBy = "tower")
    private Coordinates position;

    public int getAttackDamage() {
        return attackDamage;
    }

    public void setAttackDamage(int attackDamage) {
        this.attackDamage = attackDamage;
    }

    @ManyToOne
    @JoinColumn(name="player_id")
    private Player owner;

    private transient Enemy currentTarget;

    private String textureName;
}
