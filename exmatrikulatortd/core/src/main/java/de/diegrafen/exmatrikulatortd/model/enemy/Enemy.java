package de.diegrafen.exmatrikulatortd.model.enemy;

import com.badlogic.gdx.Gdx;
import de.diegrafen.exmatrikulatortd.model.*;
import de.diegrafen.exmatrikulatortd.view.gameobjects.GameObject;

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
public class Enemy extends ObservableModel {

    static final long serialVersionUID = 21268121294890L;

    private float baseSpeed;

    private float currentSpeed;

    private float maxHitPoints;

    private float currentHitPoints;

    @OneToMany(orphanRemoval = true, cascade=CascadeType.ALL)
    private List<Debuff> debuffs;

    @ManyToOne
    @JoinColumn(name = "gamestate_id")
    private Gamestate gameState;

    @ManyToOne
    @JoinColumn(name = "player_id")
    private Player attackedPlayer;

    private float xPosition, yPosition;

    private float endXPosition = 400, endYPosition = 200;

    private int amountOfDamageToPlayer;

    private int bounty;

    private int pointsGranted;

    private int sendPrice;

    private float xVelocity, yVelocity;

    private boolean dead;

    private boolean reachedTarget;

    private int wayPointIndex;

    private float baseArmor;

    private float currentArmor;

    @Enumerated(EnumType.ORDINAL)
    private ArmorType armorType;

    private String assetsName;

    private String name;

    private String description;

    private float targetxPosition = 0;

    private float targetyPosition = 0;

    private boolean respawning;

    @ManyToOne
    @JoinColumn(name = "wave_id")
    private Wave wave;

    @ManyToOne
    @JoinColumn(name = "mapcell_id")
    private Coordinates currentMapCell;

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
    public Enemy(String name, float baseSpeed, float maxHitPoints, int amountOfDamageToPlayer, int bounty,
                 int sendPrice, String assetsName, float xPosition, float yPosition, int pointsGranted) {
        super();

        this.debuffs = new LinkedList<>();

        // TODO: Rüstungswert ergänzen

        this.name = name;
        this.baseSpeed = baseSpeed;
        this.currentSpeed = baseSpeed;
        this.maxHitPoints = maxHitPoints;
        this.currentHitPoints = maxHitPoints;
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

    private Coordinates getStartPosition() {
        return attackedPlayer.getWayPoints().get(0);
    }

    private Coordinates getEndPosition() {
        int size = attackedPlayer.getWayPoints().size();
        return attackedPlayer.getWayPoints().get(size - 1);
    }

    public void setAttackedPlayer(Player attackedPlayer) {
        this.attackedPlayer = attackedPlayer;
    }

    public float getxPosition() {
        return xPosition;
    }

    public float getyPosition() {
        return yPosition;
    }

    public String getAssetsName() {
        return assetsName;
    }

    public String getName() {
        return name;
    }

    /**
     * TODO: In GameLogicController verschieben
     *
     * @param deltaTime
     */
    public void moveInTargetDirection(float deltaTime) {
        Coordinates nextWayPoint = attackedPlayer.getWayPoints().get(wayPointIndex);
        int tileSize = gameState.getTileSize();
        targetxPosition = nextWayPoint.getXCoordinate() * tileSize;// + tileSize / 2;
        targetyPosition = nextWayPoint.getYCoordinate() * tileSize;// + tileSize / 2;

        float angle = (float) Math.atan2(targetyPosition - yPosition, targetxPosition - xPosition);
        xPosition += (float) Math.cos(angle) * currentSpeed * deltaTime;
        yPosition += (float) Math.sin(angle) * currentSpeed * deltaTime;
    }

    public float getxSpawnPosition() {
        return getStartPosition().getxCoordinate() * gameState.getTileSize();
    }

    public float getySpawnPosition() {
        return getStartPosition().getyCoordinate() * gameState.getTileSize();
    }

    public int getEndXPosition() {
        return getEndPosition().getXCoordinate() * gameState.getTileSize();
    }

    public float getEndYPosition() {
        return endYPosition * gameState.getTileSize();
    }

    public int getNextXPosition() {
        return attackedPlayer.getWayPoints().get(wayPointIndex).getXCoordinate() * gameState.getTileSize();
    }

    public float getNextYPosition() {
        return attackedPlayer.getWayPoints().get(wayPointIndex).getYCoordinate() * gameState.getTileSize();
    }

    public Player getAttackedPlayer() {
        return attackedPlayer;
    }

    public int getAmountOfDamageToPlayer() {
        return amountOfDamageToPlayer;
    }

    public Gamestate getGameState() {
        return gameState;
    }

    public void setGameState(Gamestate gameState) {
        this.gameState = gameState;
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

    public void setWayPointIndex(int wayPointIndex) {

        this.wayPointIndex = wayPointIndex;
    }

    public void incrementWayPointIndex() {
        wayPointIndex++;
    }


    public void setToStartPosition() {
        Coordinates startCoordinates = attackedPlayer.getWayPoints().get(0);
        int tileSize = gameState.getTileSize();
        xPosition = startCoordinates.getXCoordinate() * tileSize;// + tileSize / 2;
        yPosition = startCoordinates.getYCoordinate() * tileSize;// + tileSize / 2;
    }

    public boolean isRespawning() {
        return respawning;
    }

    public Coordinates getCurrentMapCell() {
        return currentMapCell;
    }

    public void setCurrentMapCell(Coordinates currentMapCell) {
        this.currentMapCell = currentMapCell;
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

    public void setWave(Wave wave) {
        this.wave = wave;
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

    public void setBaseSpeed(float baseSpeed) {
        this.baseSpeed = baseSpeed;
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

    public void setBaseArmor(float baseArmor) {
        this.baseArmor = baseArmor;
    }

    public float getCurrentArmor() {
        return currentArmor;
    }

    public void setCurrentArmor(float currentArmor) {
        this.currentArmor = currentArmor;
    }
}
