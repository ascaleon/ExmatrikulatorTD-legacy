package de.diegrafen.exmatrikulatortd.model.tower;

import de.diegrafen.exmatrikulatortd.model.*;
import de.diegrafen.exmatrikulatortd.model.enemy.Debuff;
import de.diegrafen.exmatrikulatortd.model.enemy.Enemy;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Über diese Klasse werden sämtliche Türme in unserem Spiel abgebildet.
 *
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 13.06.2019 21:58
 */
@Entity
@Table(name = "Towers")
@NamedQueries({
        @NamedQuery(name="Tower.findTemplateTowers",
                query="SELECT t FROM Tower t WHERE t.template = TRUE"),
})
public class Tower extends ObservableModel implements ObservableTower {

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

    /**
     * Der Angriffstyp des Turmes
     */
    private int attackType;

    @Enumerated(EnumType.STRING)
    private AttackStyle attackStyle;

    /**
     * Die zeit nach dem angriff, an der der schaden berechnet/das projektil losgeschickt wird
     */
    private float baseAttackDelay;

    private float currentAttackDelay;

    private float attackDelayTimer;

    /**
     * Der Aura-Typ des Turmes
     */
    @OneToMany(cascade = CascadeType.ALL)
    @LazyCollection(LazyCollectionOption.FALSE)
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
    @JoinColumn(name = "player_id")
    private Player owner;

    /**
     * Das aktuelle Angriffsziel des Turmes
     */
    @ManyToOne
    private Enemy currentTarget;

    /**
     * Die Buffs, über die der Turm aktuell verfügt
     */
    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL)
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Buff> buffs;

    /**
     * Variable, die den Zeitpunkt des letzten Suchens nach einem Gegner abspeichert.
     */
    private float timeSinceLastSearch = 0;

    /**
     * Variable, die angibt, wie lange der Turm noch warten muss, bis er das nächste Mal angreifen darf.
     * Wird für die Wiederherstellung eines laufenden Spiels in der Datenbank gespeichert.
     */
    private float cooldown;

    /**
     * Die Bezeichnung der Assets, die mit der grafischen Darstellung des Turmes assoziiert sind
     */
    private String assetsName;

    private float splashAmount;

    private float splashRadius;

    @OneToMany(cascade = CascadeType.ALL)
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Debuff> attackDebuffs;

    private String projectileName;

    private String projectileAssetsName;

    private float projectileSpeed;

    private Boolean attacking = false;

    private String selectedPortraitPath;

    private String portraitPath;

    private boolean template;

    /**
     * Default-Konstruktur. Wird von JPA vorausgesetzt.
     */
    public Tower() {

    }

    /**
     * Konstruktor für den Attacktype IMMEDIATE
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
    public Tower(String name, String descriptionText, int towerType, float baseAttackDamage,
                 float attackRange, float baseAttackSpeed, int attackType, float baseAttackDelay, List<Aura> auras, float auraRange, int price,
                 int sellPrice, int upgradePrice, int upgradeLevel, int maxUpgradeLevel, String assetsName, String portraitPath, String selectedPortraitPath,
                 float splashAmount, float splashRadius, List<Debuff> attackDebuffs, boolean template) {
        super();

        this.name = name;
        this.descriptionText = descriptionText;
        this.towerType = towerType;
        this.attackStyle = AttackStyle.IMMEDIATE;
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
        this.portraitPath = portraitPath;
        this.selectedPortraitPath = selectedPortraitPath;
        this.splashAmount = splashAmount;
        this.splashRadius = splashRadius;
        this.attackDebuffs = attackDebuffs;
        this.template = template;
        this.baseAttackDelay = baseAttackDelay;
        this.currentAttackDelay = baseAttackDelay;
        this.attackDelayTimer = baseAttackDelay;
    }

    /**
     * Konstruktor für den Attacktype PROJECTILE
     *
     * @param name
     * @param descriptionText
     * @param towerType
     * @param baseAttackDamage
     * @param attackRange
     * @param baseAttackSpeed
     * @param attackType
     * @param baseAttackDelay
     * @param auras
     * @param auraRange
     * @param price
     * @param sellPrice
     * @param upgradePrice
     * @param upgradeLevel
     * @param maxUpgradeLevel
     * @param assetsName
     * @param splashAmount
     * @param splashRadius
     * @param attackDebuffs
     * @param projectileName
     * @param projectileAssetsName
     * @param projectileSpeed
     */
    public Tower(String name, String descriptionText, int towerType, float baseAttackDamage, float attackRange,
                 float baseAttackSpeed, int attackType, float baseAttackDelay, List<Aura> auras, float auraRange, int price, int sellPrice,
                 int upgradePrice, int upgradeLevel, int maxUpgradeLevel, String assetsName, String portraitPath, String selectedPortraitPath, float splashAmount,
                 float splashRadius, List<Debuff> attackDebuffs, String projectileName, String projectileAssetsName,
                 float projectileSpeed, boolean template) {
        this(name, descriptionText, towerType, baseAttackDamage,
                attackRange, baseAttackSpeed, attackType, baseAttackDelay, auras, auraRange, price,
                sellPrice, upgradePrice, upgradeLevel, maxUpgradeLevel, assetsName, portraitPath, selectedPortraitPath,
                splashAmount, splashRadius, attackDebuffs, template);
        this.attackStyle = AttackStyle.PROJECTILE;
        this.projectileName = projectileName;
        this.projectileAssetsName = projectileAssetsName;
        this.projectileSpeed = projectileSpeed;
        this.currentAttackDelay = baseAttackDelay;
        this.attackDelayTimer = baseAttackDelay;

    }

    /**
     * Kopier-Konstruktor
     *
     * @param tower Der zu kopierende Turm
     */
    public Tower(Tower tower) {
        this.name = tower.name;
        this.descriptionText = tower.descriptionText;
        this.towerType = tower.towerType;
        this.baseAttackDamage = tower.baseAttackDamage;
        this.currentAttackDamage = tower.currentAttackDamage;
        this.attackRange = tower.attackRange;
        this.baseAttackSpeed = tower.baseAttackSpeed;
        this.currentAttackSpeed = tower.currentAttackSpeed;
        this.attackType = tower.attackType;
        this.attackStyle = tower.attackStyle;
        this.auraRange = tower.auraRange;
        this.price = tower.price;
        this.sellPrice = tower.sellPrice;
        this.upgradePrice = tower.upgradePrice;
        this.upgradeLevel = tower.upgradeLevel;
        this.maxUpgradeLevel = tower.maxUpgradeLevel;
        this.timeSinceLastSearch = tower.timeSinceLastSearch;
        this.cooldown = tower.cooldown;
        this.assetsName = tower.assetsName;
        this.portraitPath = tower.portraitPath;
        this.selectedPortraitPath = tower.selectedPortraitPath;
        this.splashAmount = tower.splashAmount;
        this.splashRadius = tower.splashRadius;
        this.projectileName = tower.projectileName;
        this.projectileAssetsName = tower.projectileAssetsName;
        this.projectileSpeed = tower.projectileSpeed;
        this.attacking = tower.attacking;
        this.baseAttackDelay = tower.baseAttackDelay;
        this.currentAttackDelay = tower.currentAttackDelay;
        this.attackDelayTimer = tower.attackDelayTimer;

        this.auras = new LinkedList<>();
        this.buffs = new LinkedList<>();
        this.attackDebuffs = new LinkedList<>();

        tower.getAuras().forEach(aura -> auras.add(new Aura(aura)));
        tower.getBuffs().forEach(buff -> buffs.add(new Buff(buff)));
        tower.getAttackDebuffs().forEach(debuff -> attackDebuffs.add(new Debuff(debuff)));

        this.template = false;
        this.position = null;
        this.currentTarget = null;
        this.owner = null;
        //this.gamestate = null;
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
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

    @Override
    public float getxPosition() {
        if (position != null) {
            return position.getXCoordinate() * position.getWidth();
        } else {
            return 0;
        }
    }

    @Override
    public float getyPosition() {
        if (position != null) {
            return position.getYCoordinate() * position.getHeight();
        } else {
            return 0;
        }
    }

    @Override
    public float getTargetxPosition() {
        if (currentTarget != null) {
            return currentTarget.getxPosition();
        } else {
            return 0;
        }
    }

    @Override
    public float getTargetyPosition() {
        if (currentTarget != null) {
            return currentTarget.getyPosition();
        } else {
            return 0;
        }
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

    public float getCooldown() {
        return cooldown;
    }

    public void setCooldown(float cooldown) {
        this.cooldown = cooldown;
    }

    public void setName(String towerName) {
        this.name = towerName;
    }

    public int getAttackType() {
        return attackType;
    }

    public List<Aura> getAuras() {
        return auras;
    }

    public float getAuraRange() {
        return auraRange;
    }

    public int getPrice() {
        return price;
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

    public float getCurrentAttackSpeed() {
        return currentAttackSpeed;
    }

    public void setCurrentAttackSpeed(float currentAttackSpeed) {
        this.currentAttackSpeed = currentAttackSpeed;
    }

    public AttackStyle getAttackStyle() {
        return attackStyle;
    }

    public List<Debuff> getAttackDebuffs() {
        return attackDebuffs;
    }

    public String getDescriptionText() {
        return descriptionText;
    }

    public float getSplashAmount() {
        return splashAmount;
    }

    public float getSplashRadius() {
        return splashRadius;
    }

    String getProjectileAssetsName() {
        return this.projectileAssetsName;
    }

    String getProjectileName() {
        return projectileName;
    }

    float getProjectileSpeed() {
        return projectileSpeed;
    }

    public void setAttacking(Boolean attacking) {
        this.attacking = attacking;
    }

    public boolean isAttacking() {
        return this.attacking;
    }

    public float getAttackSpeed() {
        return currentAttackSpeed;
    }

    public float getBaseAttackDelay() {
        return baseAttackDelay;
    }

    public float getCurrentAttackDelay() {
        return currentAttackDelay;
    }

    public void setCurrentAttackDelay(float attackdelay) {
        currentAttackDelay = attackdelay;
    }

    public float getAttackDelayTimer() {
        return attackDelayTimer;
    }

    public void setAttackDelayTimer(float attackdelay) {
        attackDelayTimer = attackdelay;
    }

    @Override
    public String getPortraitPath() {
        return this.portraitPath;
    }

    @Override
    public String getSelectedPortraitPath() {
        return this.selectedPortraitPath;
    }
}
