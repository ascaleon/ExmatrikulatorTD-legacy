package de.diegrafen.exmatrikulatortd.model.enemy;

import de.diegrafen.exmatrikulatortd.model.BaseModel;
import de.diegrafen.exmatrikulatortd.model.Coordinates;
import de.diegrafen.exmatrikulatortd.model.Player;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "Enemies")
public class Enemy extends BaseModel {

    static final long serialVersionUID = 21268121294890L;

    private float baseSpeed;

    private float currentSpeed;

    private float maxHitPoints;

    private float currentHitPoints;

    @OneToMany(mappedBy="enemy", orphanRemoval=true)
    private List<Debuff> debuffs;

    @ManyToOne
    @JoinColumn(name="player_id")
    private Player player;

    private float xPosition;

    private float yPosition;

    private int amountOfDamageToPlayer;

    private int resourcesForKill;

    private int sendPrice;

    private boolean isDead;

    private boolean reachedTarget;

    private int wayPointIndex;

    @Enumerated(EnumType.ORDINAL)
    private ArmorType armorType;

    private String assetsName;

    public Enemy () {

    }

    /**
     * Standardkonstruktor für die Klasse Enemy. Legt noch keine assoziierte Spielerin, Position oder EnemyObject fest
     * und bestimmt nicht den Zustand des Gegners in Bezug auf aktuelle Lebenspunkte oder Geschwindigkeit.
     *
     * @param baseSpeed Die Standardbewegungsgeschwindigkeit des Gegners.
     * @param maxHitPoints Die maximalen Lebenspunkte des Gegners.
     * @param amountOfDamageToPlayer Die Höhe des Schadens, die der Gegner der Spielerin zufügt, sobald er das Ziel erreicht.
     * @param resourcesForKill Die Anzahl der Ressourcen, die die Spielerin für das Ausschalten des Gegners erhält.
     * @param sendPrice Die Kosten für das Versenden eines solchen Gegners im Multiplayer-Modus.
     * @param assetsName Die Bezeichnung der Assets, die für die Darstellung dieses Gegners verwendet werden.
     */
    public Enemy (float baseSpeed, float maxHitPoints, int amountOfDamageToPlayer, int resourcesForKill,
                  int sendPrice, String assetsName) {
        this.baseSpeed = baseSpeed;
        this.maxHitPoints = maxHitPoints;
        this.amountOfDamageToPlayer = amountOfDamageToPlayer;
        this.resourcesForKill = resourcesForKill;
        this.sendPrice = sendPrice;
        this.assetsName = assetsName;
    }

    private Coordinates getStartPosition () {
        return player.getWayPoints().get(0);
    }

    private Coordinates getEndPosition () {
        int size = player.getWayPoints().size();
        return player.getWayPoints().get(size);
    }

}
