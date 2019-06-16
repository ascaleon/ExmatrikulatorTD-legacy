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
 *
 * Über diese Klasse werden sämtliche Türme in unserem Spiel abgebildet.
 *
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 13.06.2019 21:58
 */
@Entity
@Table(name = "Towers")
public class Tower extends BaseModel {

    /**
     * Die eindeutige Serialisierungs-ID
     */
    static final long serialVersionUID = 9304847294042L;

    /**
     * Der Name des Turmes
     */
    private String towerName;

    /**
     * Der durch den Turm verursachte Schaden.
     */
    private int attackDamage;

    /**
     * Die Reichweite des Turms.
     */
    private float attackRange;

    /**
     * Der Angriffstyp des Turmes
     */
    @Enumerated(EnumType.STRING)
    private AttackType attackType;

    /**
     * Der Aura-Typ des Turmes
     */
    @Enumerated(EnumType.STRING)
    private Aura aura;

    /**
     * Die Aura-Reichweite des Turmes
     */
    private float auraRange;

    /**
     * Die Kosten für das Bauen des Turmes
     */
    private int price;

    /**
     * Die Anzahl der Ressourcen, die man für den Verkauf des Turmes erhält
     */
    private int sellPrice;

    /**
     * Die Kosten für das Aufrüsten des Turmes
     */
    private int upgradePrice;

    /**
     * Die Ausbaustufe des Turmes
     */
    private int upgradeLevel;

    /**
     * Die Position des Turmes auf der Spielkarte
     */
    @OneToOne(mappedBy = "tower")
    private Coordinates position;

    /**
     * Der Spieler, dem der Turm gehört
     */
    @ManyToOne
    @JoinColumn(name="player_id")
    private Player owner;

    /**
     * Das aktuelle Angriffsziel des Turmes
     */
    @OneToOne
    private Enemy currentTarget;

    /**
     * Die Buffs, über die der Turm aktuell verfügt
     */
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

    /**
     * Die Bezeichnung der Assets, die mit der grafischen Darstellung des Turmes assoziiert sind
     */
    private String assetsName;

    /**
     * Default-Konstruktur. Wird von JPA vorausgesetzt.
     */
    public Tower () {

    }


    public int getAttackDamage() {
        return attackDamage;
    }

    public void setAttackDamage(int attackDamage) {
        this.attackDamage = attackDamage;
    }
}
