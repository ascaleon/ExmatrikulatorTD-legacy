package de.diegrafen.exmatrikulatortd.model;

import com.badlogic.gdx.maps.tiled.TiledMap;
import de.diegrafen.exmatrikulatortd.controller.factories.EnemyFactory;
import de.diegrafen.exmatrikulatortd.model.enemy.Enemy;
import de.diegrafen.exmatrikulatortd.model.enemy.Wave;
import de.diegrafen.exmatrikulatortd.model.tower.Tower;
import de.diegrafen.exmatrikulatortd.view.Observer;
import de.diegrafen.exmatrikulatortd.view.gameobjects.GameObject;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static de.diegrafen.exmatrikulatortd.controller.factories.EnemyFactory.*;
import static de.diegrafen.exmatrikulatortd.util.Constants.TIME_BETWEEN_ROUNDS;

/**
 *
 * Der Spielzustand. Verwaltet alle spielrelevanten Informationen über das Spielfeld, die Spieler, die Türme,
 * Angriffswellen und Gegner.
 *
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 13.06.2019 21:36
 */
@Entity
@Table(name = "Gamestates")
public class Gamestate extends BaseModel implements Observable {

    /**
     * Die eindeutige Serialisierungs-ID
     */
    static final long serialVersionUID = 48546846516547L;

    private int tileSize = 64;

    /**
     * Der Name der Map
     */
    private String mapName;

    private int mapWidth = 20 * tileSize;

    private int numberOfColumns = 20;

    private int mapHeight = 20 * tileSize;

    private int numberOfRows = 20;

    /**
     * Die Spielerinnennummer der lokalen Spielinstanz. Hierüber lässt sich auf die jeweiligen Spielinformationen zugreifen.
     */
    private transient int localPlayerNumber;


    /**
     * Die Spielerinnen. Umfasst im Singleplayer-Modus ein Element und im Multiplayer-Modus zwei Elemente.
     */
    @OneToMany(mappedBy="gameState", cascade=CascadeType.ALL)
    private List<Player> players;

    /**
     * Die Kollisionsmatrix, mit der bestimmt wird, ob ein Turm an einer bestimmten Stelle auf dem Spielfeld gebaut werden kann
     */
    @OneToMany(mappedBy="gameState", cascade=CascadeType.ALL)
    private List<Coordinates> collisionMatrix;

    @OneToMany(mappedBy="gameState", cascade=CascadeType.ALL)
    private List<Enemy> enemies;

    @OneToMany(mappedBy="gamestate", cascade=CascadeType.ALL)
    private List<Tower> towers;

    /**
     * Der Schwierigkeitsgrad des Spieles
     */
    @Enumerated(EnumType.ORDINAL)
    private Difficulty difficulty;

    /**
     * Die aktuelle Rundennummer
     */
    private int roundNumber;

    /**
     * Die Anzahl der Runden
     */
    private int numberOfRounds;

    /**
     * Gibt an, ob das Spiel gerade aktiv ist
     */
    private boolean active;

    private boolean gameOver;

    private boolean newRound;

    private float timeUntilNextRound;

    private boolean roundEnded;

    private transient List<Observer> observers;

    /**
     * Konstruktor, der den Spielzustand mit Spielern und einem Schwierigkeitsgrad initialisiert
     */
    public Gamestate (List<Player> players, Difficulty difficulty) {
        this.players = players;
        this.difficulty = difficulty;
        this.timeUntilNextRound = TIME_BETWEEN_ROUNDS;
    }

    /**
     * Default-Konstruktor. Wird von JPA benötigt
     */
    public Gamestate() {
        players = new ArrayList<>();
        enemies = new ArrayList<>();
        towers = new ArrayList<>();
        collisionMatrix = new ArrayList<>();

        this.newRound = true;
        this.roundNumber = 0;
        this.timeUntilNextRound = TIME_BETWEEN_ROUNDS;
        this.roundEnded = true;
        this.numberOfRounds = 3;
        this.gameOver = false;
    }

    public void addEnemy (Enemy enemy) {
        enemies.add(enemy);
    }

    public void addEnemy (Enemy enemy, Player player) {
        enemy.setAttackedPlayer(player);
        player.addEnemy(enemy);
        enemies.add(enemy);
    }


    public List<Enemy> getEnemies() {
        return enemies;
    }

    public int getLocalPlayerNumber() {
        return localPlayerNumber;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void addTower(Tower tower) {
        towers.add(tower);
    }

    public void removeEnemy(Enemy enemy) {
        enemies.remove(enemy);
    }

    public void addPlayer (Player player) {
        players.add(player);
    }

    public Player getPlayerByNumber (int playerNumber) {
        return players.get(playerNumber);
    }

    public void setLocalPlayerNumber(int localPlayerNumber) {
        this.localPlayerNumber = localPlayerNumber;
    }

    public int getMapWidth() {
        return mapWidth;
    }

    public void setMapWidth(int mapWidth) {
        this.mapWidth = mapWidth;
    }

    public int getNumberOfColumns() {
        return numberOfColumns;
    }

    public void setNumberOfColumns(int numberOfColumns) {
        this.numberOfColumns = numberOfColumns;
    }

    public int getMapHeight() {
        return mapHeight;
    }

    public void setMapHeight(int mapHeight) {
        this.mapHeight = mapHeight;
    }

    public int getNumberOfRows() {
        return numberOfRows;
    }

    public void setNumberOfRows(int numberOfRows) {
        this.numberOfRows = numberOfRows;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public List<Coordinates> getCollisionMatrix() {
        return collisionMatrix;
    }

    public void setCollisionMatrix(List<Coordinates> collisionMatrix) {
        this.collisionMatrix = collisionMatrix;
    }

    public int getRoundNumber() {
        return roundNumber;
    }

    public void setRoundNumber(int roundNumber) {
        this.roundNumber = roundNumber;
    }

    public int getNumberOfRounds() {
        return numberOfRounds;
    }

    public void setNumberOfRounds(int numberOfRounds) {
        this.numberOfRounds = numberOfRounds;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void addCoordinatesToCollisionMatrix (Coordinates coordinates) {
        collisionMatrix.add(coordinates);
    }

    public Coordinates getMapCellByListIndex (int listIndex) {
        return collisionMatrix.get(listIndex);
    }

    public int getTileSize() {
        return tileSize;
    }

    public void removeTower(Tower tower) {
        towers.remove(tower);
    }

    public List<Tower> getTowers() {
        return towers;
    }

    public String getMapName() {
        return mapName;
    }

    public void setMapName(String mapName) {
        this.mapName = mapName;
    }

    public void setTileSize(int tileSize) {
        this.tileSize = tileSize;
    }

    public void setEnemies(List<Enemy> enemies) {
        this.enemies = enemies;
    }

    public void setTowers(List<Tower> towers) {
        this.towers = towers;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public boolean isNewRound() {
        return newRound;
    }

    public void setNewRound(boolean newRound) {
        this.newRound = newRound;
    }

    public float getTimeUntilNextRound() {
        return timeUntilNextRound;
    }

    public void setTimeUntilNextRound(float timeUntilNextRound) {
        this.timeUntilNextRound = timeUntilNextRound;
    }

    public boolean isRoundEnded() {
        return roundEnded;
    }

    public void setRoundEnded(boolean roundEnded) {
        this.roundEnded = roundEnded;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    @Override
    public void registerObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObserver() {
        observers.forEach(Observer::update);
    }
}