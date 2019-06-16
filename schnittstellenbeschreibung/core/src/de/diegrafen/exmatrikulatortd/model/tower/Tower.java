package de.diegrafen.exmatrikulatortd.model.tower;

import de.diegrafen.exmatrikulatortd.model.BaseModel;
import de.diegrafen.exmatrikulatortd.model.Coordinates;
import de.diegrafen.exmatrikulatortd.model.Player;
import de.diegrafen.exmatrikulatortd.model.enemy.Debuff;
import de.diegrafen.exmatrikulatortd.model.enemy.Enemy;
import de.diegrafen.exmatrikulatortd.view.gameobjects.TowerObject;

import javax.persistence.*;
import java.util.List;

/**
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 13.06.2019 21:58
 */
@Entity
@Table(name = "Towers")
public class Tower extends BaseModel {

    private String towerName;

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

    private int upgradePrice;

    private int upgradeLevel;

    @OneToOne(mappedBy = "tower")
    private Coordinates position;

    @ManyToOne
    @JoinColumn(name="player_id")
    private Player owner;

    @OneToOne
    private Enemy currentTarget;

    @OneToMany(mappedBy="tower", orphanRemoval=true)
    private List<Buff> buffs;

    /**
     * Variable, die den Zeitpunkt des letzten Suchens nach einem Gegner abspeichert. Wird nicht in der Datenbank
     * gespeichert.
     */
    private transient float timeSinceLastSearch;

    /**
     * Variable, die angibt, wie lange der Turm noch warten muss, bis er das nächste Mal angreifen darf.
     * Wird für die Wiederherstellung eines laufenden Spiels in der Datenbank gespeichert.
     */
    private float cooldown;

    private String assetsName;

    public Tower () {

    }

    public int getAttackDamage() {
        return attackDamage;
    }

    public void setAttackDamage(int attackDamage) {
        this.attackDamage = attackDamage;
    }
}
