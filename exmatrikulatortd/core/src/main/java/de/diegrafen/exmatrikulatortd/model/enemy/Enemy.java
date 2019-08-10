package de.diegrafen.exmatrikulatortd.model.enemy;

import de.diegrafen.exmatrikulatortd.model.*;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

/**
 * Über diese Klasse werden sämtliche Gegner in unserem Spiel abgebildet.
 *
 * @author Jan Romann <jan.romann@uni-bremen.de>
 */
@Entity
@Table(name = "Enemies")
public class Enemy extends ObservableModel implements ObservableEnemy {

    static final long serialVersionUID = 21268121294890L;

    private float baseSpeed;

    private float currentSpeed;

    private float baseMaxHitPoints;

    private float currentHitPoints;

    private float currentMaxHitPoints;

    @OneToMany(orphanRemoval = true, cascade=CascadeType.ALL)
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Debuff> debuffs;

//    @ManyToOne
//    @JoinColumn(name="player_id")
//    private Player attackedPlayer;

    private int playerNumber;

    private float xPosition, yPosition;

    private int amountOfDamageToPlayer;

    private int bounty;

    private int pointsGranted;

    private int sendPrice;

    private int wayPointIndex;

    private float baseArmor;

    private float currentArmor;

    private int armorType;

    private String assetsName;

    private String name;

    private String description;

    private float targetxPosition = 0;

    private float targetyPosition = 0;

    private boolean respawning;

    private boolean isAttacking;

    /**
     * Standardkonstruktor für JPA
     */
    public Enemy() {

    }

    /**
     * Standardkonstruktor für die Klasse Enemy. Legt noch keine assoziierte Spielerin, Position oder EnemyObject fest
     * und bestimmt nicht den Zustand des Gegners in Bezug auf aktuelle Lebenspunkte oder Geschwindigkeit.
     *
     * @param name                   Der Name des Gegners
     * @param baseSpeed              Die Standardbewegungsgeschwindigkeit des Gegners.
     * @param maxHitPoints           Die maximalen Lebenspunkte des Gegners.
     * @param amountOfDamageToPlayer Die Höhe des Schadens, die der Gegner der Spielerin zufügt, sobald er das Ziel erreicht.
     * @param bounty                 Die Anzahl der Ressourcen, die die Spielerin für das Ausschalten des Gegners erhält.
     * @param sendPrice              Die Kosten für das Versenden eines solchen Gegners im Multiplayer-Modus.
     * @param assetsName             Die Bezeichnung der Assets, die für die Darstellung dieses Gegners verwendet werden.
     */
    public Enemy(String name, float baseSpeed, float maxHitPoints, int amountOfDamageToPlayer, int bounty, int sendPrice,
                 int armorType, float baseArmor, String assetsName, float xPosition, float yPosition, int pointsGranted) {
        super();

        this.debuffs = new LinkedList<>();

        this.armorType = armorType;
        this.baseArmor = baseArmor;
        this.currentArmor = baseArmor;
        this.name = name;
        this.baseSpeed = baseSpeed;
        this.currentSpeed = baseSpeed;
        this.currentMaxHitPoints = maxHitPoints;
        this.currentHitPoints = maxHitPoints;
        this.baseMaxHitPoints = maxHitPoints;
        this.amountOfDamageToPlayer = amountOfDamageToPlayer;
        this.bounty = bounty;
        this.pointsGranted = pointsGranted;
        this.sendPrice = sendPrice;
        this.assetsName = assetsName;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.wayPointIndex = 0;

        this.respawning = false;
    }

    public Enemy(float baseSpeed, float currentSpeed, float baseMaxHitPoints, float currentHitPoints, float currentMaxHitPoints, List<Debuff> debuffs, Gamestate gameState, Player attackedPlayer, float xPosition, float yPosition, int amountOfDamageToPlayer, int bounty, int pointsGranted, int sendPrice, int wayPointIndex, float baseArmor, float currentArmor, int armorType, String assetsName, String name, String description, float targetxPosition, float targetyPosition, boolean respawning, Wave wave, Coordinates currentMapCell) {
        this.baseSpeed = baseSpeed;
        this.currentSpeed = currentSpeed;
        this.baseMaxHitPoints = baseMaxHitPoints;
        this.currentHitPoints = currentHitPoints;
        this.currentMaxHitPoints = currentMaxHitPoints;
        this.debuffs = debuffs;
        //this.attackedPlayer = attackedPlayer;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.amountOfDamageToPlayer = amountOfDamageToPlayer;
        this.bounty = bounty;
        this.pointsGranted = pointsGranted;
        this.sendPrice = sendPrice;
        this.wayPointIndex = wayPointIndex;
        this.baseArmor = baseArmor;
        this.currentArmor = currentArmor;
        this.armorType = armorType;
        this.assetsName = assetsName;
        this.respawning = respawning;
    }

    public Enemy(Enemy enemy) {

        this.debuffs = new LinkedList<>();

        for (Debuff debuff : enemy.getDebuffs()) {
            this.debuffs.add(new Debuff(debuff));
        }

        this.baseArmor = enemy.baseArmor;
        this.currentArmor = enemy.currentArmor;
        this.name = enemy.name;
        this.description = enemy.description;
        this.baseSpeed = enemy.baseSpeed;
        this.currentSpeed = enemy.currentSpeed;
        this.baseMaxHitPoints = enemy.baseMaxHitPoints;
        this.currentMaxHitPoints = enemy.currentMaxHitPoints;
        this.currentHitPoints = enemy.currentHitPoints;
        this.amountOfDamageToPlayer = enemy.amountOfDamageToPlayer;
        this.bounty = enemy.bounty;
        this.pointsGranted = enemy.pointsGranted;
        this.sendPrice = enemy.sendPrice;
        this.assetsName = enemy.assetsName;
        this.xPosition = enemy.xPosition;
        this.yPosition = enemy.yPosition;
        this.targetxPosition = enemy.targetxPosition;
        this.targetyPosition = enemy.targetyPosition;
        this.wayPointIndex = enemy.wayPointIndex;
        this.armorType = enemy.armorType;
        this.isAttacking = enemy.isAttacking;
        this.playerNumber = enemy.playerNumber;

        this.respawning = enemy.isRespawning();
        //this.attackedPlayer = null;
    }

    public Enemy(int playerNumber, Enemy enemy) {
        this(enemy);
        this.playerNumber = playerNumber;
        //this.attackedPlayer = player;
    }

    public Enemy(Enemy enemy, int wayPointIndex) {
        this(enemy);
        this.wayPointIndex = wayPointIndex;
    }

    //public void setAttackedPlayer(Player attackedPlayer) {
        //this.attackedPlayer = attackedPlayer;
    //}

    public float getxPosition() {
        return xPosition;
    }

    public float getyPosition() {
        return yPosition;
    }

    public void setxPosition(float xPosition) {
        this.xPosition = xPosition;
    }

    public void setyPosition(float yPosition) {
        this.yPosition = yPosition;
    }

    public String getAssetsName() {
        return assetsName;
    }

    public String getName() {
        return name;
    }

//    public Player getAttackedPlayer() {
//        return attackedPlayer;
//    }

    public int getAmountOfDamageToPlayer() {
        return amountOfDamageToPlayer;
    }

    public float getTargetxPosition() {
        return targetxPosition;
    }

    public void setTargetxPosition(float targetxPosition) {
        this.targetxPosition = targetxPosition;
    }

    public float getTargetyPosition() {
        return targetyPosition;
    }

    public void setTargetyPosition(float targetyPosition) {
        this.targetyPosition = targetyPosition;
    }

    public int getWayPointIndex() {
        return wayPointIndex;
    }

    public void incrementWayPointIndex() {
        wayPointIndex++;
    }

    public boolean isRespawning() {
        return respawning;
    }

    public float getCurrentHitPoints() {
        return currentHitPoints;
    }

    public void setCurrentHitPoints(float currentHitPoints) {
        this.currentHitPoints = currentHitPoints;
    }

    @Override
    public String toString() {
        return this.name;
    }

    public int getBounty() {
        return bounty;
    }

    public int getPointsGranted() {
        return pointsGranted;
    }

    public List<Debuff> getDebuffs() {
        return debuffs;
    }

    public void setDebuffs(List<Debuff> debuffs) {
        this.debuffs = debuffs;
    }

    public void addDebuff(Debuff debuff) {
        debuffs.add(debuff);
    }

    public void removeDebuff(Debuff debuff) {
        debuffs.remove(debuff);
    }

    public float getBaseSpeed() {
        return baseSpeed;
    }

    public float getCurrentSpeed() {
        return currentSpeed;
    }

    public void setCurrentSpeed(float currentSpeed) {
        this.currentSpeed = currentSpeed;
    }

    public float getBaseArmor() {
        return baseArmor;
    }

    public float getCurrentArmor() {
        return currentArmor;
    }

    public void setCurrentArmor(float currentArmor) {
        this.currentArmor = currentArmor;
    }

    public int getSendPrice() {
        return sendPrice;
    }

    public int getArmorType() {
        return armorType;
    }

    public float getBaseMaxHitPoints() {
        return baseMaxHitPoints;
    }

    public float getCurrentMaxHitPoints() {
        return currentMaxHitPoints;
    }

    public void setCurrentMaxHitPoints(float currentMaxHitPoints) {
        this.currentMaxHitPoints = currentMaxHitPoints;
    }

    public void clearDebuffs() {
        debuffs.clear();
    }

    public String getDescription() {
        return description;
    }

    public boolean isAttacking(){return this.isAttacking;}

    public int getPlayerNumber() {
        return playerNumber;
    }

    public void setPlayerNumber(int playerNumber) {
        this.playerNumber = playerNumber;
    }
}
