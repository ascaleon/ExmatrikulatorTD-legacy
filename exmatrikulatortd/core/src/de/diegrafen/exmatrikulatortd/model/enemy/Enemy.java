package de.diegrafen.exmatrikulatortd.model.enemy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import de.diegrafen.exmatrikulatortd.model.BaseModel;
import de.diegrafen.exmatrikulatortd.model.Coordinates;
import de.diegrafen.exmatrikulatortd.model.Gamestate;
import de.diegrafen.exmatrikulatortd.model.Player;

import javax.persistence.*;
import java.util.List;

import static de.diegrafen.exmatrikulatortd.util.Constants.X_POS;
import static de.diegrafen.exmatrikulatortd.util.Constants.Y_POS;

/**
 *
 * Über diese Klasse werden sämtliche Gegner in unserem Spiel abgebildet.
 *
 * @author Jan Romann <jan.romann@uni-bremen.de>
 */
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
    @JoinColumn(name="gamestate_id")
    private Gamestate gameState;

    @ManyToOne
    @JoinColumn(name="player_id")
    private Player attackedPlayer;

    private float xSpawnPosition, ySpawnPosition;

    private float xPosition, yPosition;

    private float endXPosition = 400, endYPosition = 200;

    private int amountOfDamageToPlayer;

    private int resourcesForKill;

    private int sendPrice;

    private boolean dead;

    private boolean reachedTarget;

    private int wayPointIndex;

    private float armor;

    @Enumerated(EnumType.ORDINAL)
    private ArmorType armorType;

    private String assetsName;

    private String name;

    private float targetxPosition = 0;

    private float targetyPosition = 0;

    private boolean respawning;

    public Enemy () {

    }

    /**
     * Standardkonstruktor für die Klasse Enemy. Legt noch keine assoziierte Spielerin, Position oder EnemyObject fest
     * und bestimmt nicht den Zustand des Gegners in Bezug auf aktuelle Lebenspunkte oder Geschwindigkeit.
     *
     * @param name Der Name des Gegners
     * @param baseSpeed Die Standardbewegungsgeschwindigkeit des Gegners.
     * @param maxHitPoints Die maximalen Lebenspunkte des Gegners.
     * @param amountOfDamageToPlayer Die Höhe des Schadens, die der Gegner der Spielerin zufügt, sobald er das Ziel erreicht.
     * @param resourcesForKill Die Anzahl der Ressourcen, die die Spielerin für das Ausschalten des Gegners erhält.
     * @param sendPrice Die Kosten für das Versenden eines solchen Gegners im Multiplayer-Modus.
     * @param assetsName Die Bezeichnung der Assets, die für die Darstellung dieses Gegners verwendet werden.
     */
    public Enemy (String name, float baseSpeed, float maxHitPoints, int amountOfDamageToPlayer, int resourcesForKill,
                  int sendPrice, String assetsName, float xPosition, float yPosition) {
        this.name = name;
        this.baseSpeed = baseSpeed;
        this.currentSpeed = baseSpeed;
        this.maxHitPoints = maxHitPoints;
        this.currentHitPoints = maxHitPoints;
        this.amountOfDamageToPlayer = amountOfDamageToPlayer;
        this.resourcesForKill = resourcesForKill;
        this.sendPrice = sendPrice;
        this.assetsName = assetsName;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.wayPointIndex = 1;

        this.respawning = false;
    }

    private Coordinates getStartPosition () {
        return attackedPlayer.getWayPoints().get(0);
    }

    private Coordinates getEndPosition () {
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

    public void moveInxDirection () {
        xPosition = xPosition + currentSpeed;
    }

    public void moveInxDirection (float deltaTime) {
        xPosition = xPosition + currentSpeed * deltaTime;
    }

    /**
     * TODO: In GameLogicController verschieben
     * @param deltaTime
     */
    public void moveInTargetDirection (float deltaTime) {
        //System.out.println(wayPointIndex);
        Coordinates nextWayPoint = attackedPlayer.getWayPoints().get(wayPointIndex);
        int tileSize = gameState.getTileSize();
        targetxPosition = nextWayPoint.getXCoordinate() * tileSize + tileSize / 2;
        //System.out.println("Current X: " + xPosition);
        //System.out.println("Target X: " + targetxPosition);
        targetyPosition = nextWayPoint.getYCoordinate() * tileSize + tileSize / 2;
        //System.out.println("Current Y: " + yPosition);
        //System.out.println("Target Y: " + targetyPosition);

        //targetyPosition = Y_POS;
        //targetxPosition = X_POS;
        //System.out.println(targetyPosition);
        //System.out.println(targetxPosition);
        //if (Math.abs(targetyPosition - yPosition)  > 1 & Math.abs(targetxPosition - xPosition) > 1) {
        float angle = (float) Math.atan2(targetyPosition - yPosition, targetxPosition - xPosition);
        xPosition += (float) Math.cos(angle) * currentSpeed * Gdx.graphics.getDeltaTime();
        yPosition += (float) Math.sin(angle) * currentSpeed * Gdx.graphics.getDeltaTime();
        //}
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

    public void incrementWayPointIndex () {
        wayPointIndex++;
    }


    public void setToStartPosition () {
        Coordinates startCoordinates = attackedPlayer.getWayPoints().get(0);
        int tileSize = gameState.getTileSize();
        xPosition = startCoordinates.getXCoordinate() * tileSize + tileSize / 2;
        yPosition = startCoordinates.getYCoordinate() * tileSize + tileSize / 2;
    }

    public boolean isRespawning() {
        return respawning;
    }

    public void setRespawning(boolean respawning) {
        this.respawning = respawning;
    }
}
