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
     * Der grundlegend durch den Turm verursachte Schaden.
     */
    private float baseAttackDamage;

    /**
     * Der aktuell durch den Turm verursachte Schaden.
     */
    private float currentAttackDamage;

    /**
     * Die Reichweite des Turms.
     */
    private float attackRange;

    /**
     * Die grundlegende Angriffsgeschwindigkeit des Turms.
     */
    private float baseAttackSpeed;

    /**
     * Die aktuelle Angriffsgeschwindigkeit des Turms.
     */
    private float currentAttackSpeed;

    // TODO: Für Buffs müssen Attribute geschaffen werden, die den aktuellen Wert von Geschwindigkeit, Schaden etc. abbilden
    //private float currentAttackSpeed;

    /**
     * Der Angriffstyp des Turmes
     */
    @Enumerated(EnumType.STRING)
    private AttackType attackType;

    /**
     * Der Aura-Typ des Turmes
     */
    @OneToMany(cascade=CascadeType.ALL)
    private List<Aura> auras;

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
     * Die maximale Ausbaustufe des Turms
     */
    private int maxUpgradeLevel;

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
    @OneToMany(orphanRemoval = true, cascade=CascadeType.ALL)
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
     *
     * @param name
     * @param descriptionText
     * @param towerType
     * @param baseAttackDamage
     * @param attackRange
     * @param baseAttackSpeed
     * @param attackType
     * @param auras
     * @param auraRange
     * @param price
     * @param sellPrice
     * @param upgradePrice
     * @param upgradeLevel
     * @param maxUpgradeLevel
     * @param assetsName
     */
    public Tower(String name, String descriptionText, int towerType, float baseAttackDamage, float attackRange, float baseAttackSpeed, AttackType attackType, List<Aura> auras, float auraRange, int price, int sellPrice, int upgradePrice, int upgradeLevel, int maxUpgradeLevel, String assetsName) {
        super();

        this.name = name;
        this.descriptionText = descriptionText;
        this.towerType = towerType;
        this.baseAttackDamage = baseAttackDamage;
        this.currentAttackDamage = baseAttackDamage;
        this.attackRange = attackRange;
        this.baseAttackSpeed = baseAttackSpeed;
        this.currentAttackSpeed = baseAttackSpeed;
        this.attackType = attackType;
        this.auras = auras;
        this.auraRange = auraRange;
        this.price = price;
        this.sellPrice = sellPrice;
        this.upgradePrice = upgradePrice;
        this.upgradeLevel = upgradeLevel;
        this.maxUpgradeLevel = maxUpgradeLevel;
        this.buffs = new ArrayList<>();
        this.timeSinceLastSearch = 5f;
        this.cooldown = baseAttackSpeed / 2;
        this.assetsName = assetsName;
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

    public void setName(String towerName) {
        this.name = towerName;
    }

    public AttackType getAttackType() {
        return attackType;
    }

    public void setAttackType(AttackType attackType) {
        this.attackType = attackType;
    }

    public List<Aura> getAuras() {
        return auras;
    }

    public void setAura(List<Aura> auras) {
        this.auras = auras;
    }

    public void addAura(Aura aura) {
        auras.add(aura);
    }

    public void removeAura(Aura aura) {
        auras.remove(aura);
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

    public void addBuff(Buff buff) {
        buffs.add(buff);
    }

    public void removeBuff(Buff buff) {
        buffs.remove(buff);
    }

    public void setAssetsName(String assetsName) {
        this.assetsName = assetsName;
    }

    public int getTowerType() {
        return towerType;
    }

    public int getMaxUpgradeLevel() {
        return maxUpgradeLevel;
    }

    public float getBaseAttackDamage() {
        return baseAttackDamage;
    }

    public void setBaseAttackDamage(float baseAttackDamage) {
        this.baseAttackDamage = baseAttackDamage;
    }

    public float getCurrentAttackDamage() {
        return currentAttackDamage;
    }

    public void setCurrentAttackDamage(float currentAttackDamage) {
        this.currentAttackDamage = currentAttackDamage;
    }

    public float getBaseAttackSpeed() {
        return baseAttackSpeed;
    }

    public void setBaseAttackSpeed(float baseAttackSpeed) {
        this.baseAttackSpeed = baseAttackSpeed;
    }

    public float getCurrentAttackSpeed() {
        return currentAttackSpeed;
    }

    public void setCurrentAttackSpeed(float currentAttackSpeed) {
        this.currentAttackSpeed = currentAttackSpeed;
    }
}
