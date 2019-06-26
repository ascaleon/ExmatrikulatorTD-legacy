package de.diegrafen.exmatrikulatortd.model.tower;

import de.diegrafen.exmatrikulatortd.model.*;
import de.diegrafen.exmatrikulatortd.model.enemy.Enemy;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static de.diegrafen.exmatrikulatortd.util.Constants.TILE_SIZE;

/**
 *
 * Über diese Klasse werden sämtliche Türme in unserem Spiel abgebildet.
 *
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 13.06.2019 21:58
 */
@Entity
@Table(name = "Towers")
public class Tower extends ObservableModel {

    /**
     * Die eindeutige Serialisierungs-ID
     */
    static final long serialVersionUID = 9304847294042L;

    /**
     * Der Name des Turmes
     */
    private String name;


    private String descriptionText;

    private int towerType;

    /**
     * Der durch den Turm verursachte Schaden.
     */
    private int attackDamage;

    /**
     * Die Reichweite des Turms.
     */
    private float attackRange;

    /**
     * Die Angriffsgeschwindigkeit des Turms.
     */
    private float attackSpeed;

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
     * Der zugehörige Spielzustand
     */
    @ManyToOne
    @JoinColumn(name="gamestate_id")
    private Gamestate gamestate;

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
    private transient float timeSinceLastSearch = 0;

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

    /**
     * Konstruktor für die Erzeugung eines neuen Turms über eine TowerFactory.
     * @param name
     * @param attackDamage
     * @param attackRange
     * @param attackType
     * @param aura
     * @param auraRange
     * @param price
     * @param sellPrice
     * @param upgradePrice
     * @param upgradeLevel
     * @param assetsName
     */
    public Tower(String name, String descriptionText, int towerType, int attackDamage, float attackRange, float attackSpeed, AttackType attackType, Aura aura, float auraRange, int price, int sellPrice, int upgradePrice, int upgradeLevel, String assetsName) {
        super();

        this.name = name;
        this.descriptionText = descriptionText;
        this.towerType = towerType;
        this.attackDamage = attackDamage;
        this.attackRange = attackRange;
        this.attackSpeed = attackSpeed;
        this.attackType = attackType;
        this.aura = aura;
        this.auraRange = auraRange;
        this.price = price;
        this.sellPrice = sellPrice;
        this.upgradePrice = upgradePrice;
        this.upgradeLevel = upgradeLevel;
        this.buffs = new ArrayList<Buff>();
        this.timeSinceLastSearch = 5f;
        this.cooldown = 0;
        this.assetsName = assetsName;
    }

    public int getAttackDamage() {
        return attackDamage;
    }

    public void setAttackDamage(int attackDamage) {
        this.attackDamage = attackDamage;
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public Gamestate getGamestate() {
        return gamestate;
    }

    public void setGamestate(Gamestate gamestate) {
        this.gamestate = gamestate;
    }

    public void setPosition(Coordinates position) {
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public String getAssetsName() {
        return assetsName;
    }

    public float getxPosition() {
        return position.getXCoordinate() * position.getTileSize();
    }

    public float getyPosition() {
        return position.getYCoordinate() * position.getTileSize();
    }

    @Override
    public float getTargetxPosition() {
        return position.getXCoordinate() * position.getTileSize();
    }

    @Override
    public float getTargetyPosition() {
        return position.getYCoordinate() * position.getTileSize();
    }

    public int getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(int sellPrice) {
        this.sellPrice = sellPrice;
    }

    public Coordinates getPosition() {
        return position;
    }

    public float getTimeSinceLastSearch() {
        return timeSinceLastSearch;
    }

    public void setTimeSinceLastSearch(float timeSinceLastSearch) {
        this.timeSinceLastSearch = timeSinceLastSearch;
    }

    public Enemy getCurrentTarget() {
        return currentTarget;
    }

    public void setCurrentTarget(Enemy currentTarget) {
        this.currentTarget = currentTarget;
    }

    public float getAttackRange() {
        return attackRange;
    }

    public void setAttackRange(float attackRange) {
        this.attackRange = attackRange;
    }

    public float getCooldown() {
        return cooldown;
    }

    public void setCooldown(float cooldown) {
        this.cooldown = cooldown;
    }

    public float getAttackSpeed() {
        return attackSpeed;
    }

    public void setAttackSpeed(float attackSpeed) {
        this.attackSpeed = attackSpeed;
    }

    public void setName(String towerName) {
        this.name = towerName;
    }

    public AttackType getAttackType() {
        return attackType;
    }

    public void setAttackType(AttackType attackType) {
        this.attackType = attackType;
    }

    public Aura getAura() {
        return aura;
    }

    public void setAura(Aura aura) {
        this.aura = aura;
    }

    public float getAuraRange() {
        return auraRange;
    }

    public void setAuraRange(float auraRange) {
        this.auraRange = auraRange;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getUpgradePrice() {
        return upgradePrice;
    }

    public void setUpgradePrice(int upgradePrice) {
        this.upgradePrice = upgradePrice;
    }

    public int getUpgradeLevel() {
        return upgradeLevel;
    }

    public void setUpgradeLevel(int upgradeLevel) {
        this.upgradeLevel = upgradeLevel;
    }

    public List<Buff> getBuffs() {
        return buffs;
    }

    public void setBuffs(List<Buff> buffs) {
        this.buffs = buffs;
    }

    public void setAssetsName(String assetsName) {
        this.assetsName = assetsName;
    }

    public int getTowerType() {
        return towerType;
    }
}
