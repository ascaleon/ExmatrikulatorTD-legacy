package de.diegrafen.exmatrikulatortd.model;

import de.diegrafen.exmatrikulatortd.controller.factories.WaveFactory;
import de.diegrafen.exmatrikulatortd.model.enemy.Enemy;
import de.diegrafen.exmatrikulatortd.model.enemy.Wave;
import de.diegrafen.exmatrikulatortd.model.tower.Tower;
import de.diegrafen.exmatrikulatortd.view.Observer;
import de.diegrafen.exmatrikulatortd.view.gameobjects.GameObject;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static de.diegrafen.exmatrikulatortd.controller.factories.WaveFactory.createWave;

/**
 * Die Spielerklasse. Verwaltet die Informationen über den Spieler wie Name, Anzahl der Leben und die erzielten Punkte
 * sowie über die Wellen von Angreifern, die dem Spieler angreifen werden.
 */
@Entity
@Table(name = "Players")
public class Player extends BaseModel implements Observable {

    /**
     * Die eindeutige Serialisierungs-ID
     */
    static final long serialVersionUID = 4918147183123L;

    /**
     * Der Zustand des laufenden Spiels
     */
    @ManyToOne
    @JoinColumn(name="gamestate_id")
    private Gamestate gameState;

    /**
     * Die Spielernummer
     */
    private int playerNumber;

    /**
     * Der Spielername
     */
    private String playerName;

    /**
     * Der aktuelle Punktestand des Spielers
     */
    private int score;

    /**
     * Die aktuellen Ressourcen des Spielers
     */
    private int resources;

    /**
     * Die maximalen Lebenspunkte des Spielers
     */
    private int maxLives;

    /**
     * Die aktuellen Lebenspunkte des Spielers
     */
    private int currentLives;

    /**
     * Die Türme des Spielers
     */
    @OneToMany(mappedBy="owner", cascade=CascadeType.ALL)
    private List<Tower> towers;

    /**
     * Die Angriffswellen, die mit dem Spieler assoziiert sind
     */
    @OneToMany(mappedBy="player", cascade=CascadeType.ALL)
    private List<Wave> waves;

    /**
     * Die Angriffswellen, die mit dem Spieler assoziiert sind
     */
    @OneToMany(mappedBy="attackedPlayer", cascade=CascadeType.ALL)
    private List<Enemy> attackingEnemies;

    /**
     * Die Wegpunkte, die Gegner auf der Karte auf dem Weg zum Endpunkt zurücklegen
     */
    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Coordinates> wayPoints;

    private float timeSinceLastSpawn;

    private boolean enemiesSpawned;

    private transient List<Observer> observers;

    /**
     * Default-Konstruktur. Wird von JPA vorausgesetzt.
     */
    public Player () {
        this.attackingEnemies = new ArrayList<>();
        this.towers = new ArrayList<>();
        this.wayPoints = new ArrayList<>();
        this.waves = new ArrayList<>();
        this.observers = new ArrayList<>();

        this.timeSinceLastSpawn = 0;
        this.enemiesSpawned = false;

        this.currentLives = 25;
        this.maxLives = 25;
        this.resources = 1000;
        this.score = 0;

        waves.add(createWave(WaveFactory.WaveType.REGULAR_AND_HEAVY_WAVE));
        waves.add(createWave(WaveFactory.WaveType.REGULAR_WAVE));
        waves.add(createWave(WaveFactory.WaveType.REGULAR_AND_HEAVY_WAVE));

        wayPoints.add(new Coordinates(0,20-14));
        wayPoints.add(new Coordinates(3,20-14));
        wayPoints.add(new Coordinates(3,20-17));
        wayPoints.add(new Coordinates(17,20-17));
        wayPoints.add(new Coordinates(17,20-13));
        wayPoints.add(new Coordinates(10,20-13));
        wayPoints.add(new Coordinates(10,20-4));
        wayPoints.add(new Coordinates(17,20-4));
        wayPoints.add(new Coordinates(17,20-2));
    }

    public void addEnemy (Enemy attackingEnemy) {
        this.attackingEnemies.add(attackingEnemy);
    }

    public void addTower (Tower tower) {
        towers.add(tower);
    }

    public List<Coordinates> getWayPoints() {
        return wayPoints;
    }

    public void setWayPoints(List<Coordinates> wayPoints) {
        this.wayPoints = wayPoints;
    }

    public Coordinates getWayPointByIndex (int index) {
        return wayPoints.get(index);
    }

    /**
     * Fügt der nächsten Welle einen Gegner hinzu
     */
    public void addEnemyToNextWave () {

    }

    public int getMaxLives() {
        return maxLives;
    }

    public void setMaxLives(int maxLives) {
        this.maxLives = maxLives;
    }

    public int getCurrentLives() {
        return currentLives;
    }

    public void setCurrentLives(int currentLives) {
        this.currentLives = currentLives;
        notifyObserver();
    }

    public void removeEnemy(Enemy enemy) {
        attackingEnemies.remove(enemy);
    }

    public int getPlayerNumber() {
        return playerNumber;
    }

    public void setPlayerNumber(int playerNumber) {
        this.playerNumber = playerNumber;
    }

    public int getResources() {
        return resources;
    }

    public void setResources (int resources) {
        this.resources = resources;
    }

    public void addToResources (int resources) {
        this.resources += resources;
    }

    public void addToScore (int score) {
        this.score += score;
    }

    public void removeTower(Tower tower) {
        towers.remove(tower);
    }

    public float getTimeSinceLastSpawn() {
        return timeSinceLastSpawn;
    }

    public void setTimeSinceLastSpawn(float timeSinceLastSpawn) {
        this.timeSinceLastSpawn = timeSinceLastSpawn;
    }

    public Gamestate getGameState() {
        return gameState;
    }

    public void setGameState(Gamestate gameState) {
        this.gameState = gameState;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public List<Tower> getTowers() {
        return towers;
    }

    public void setTowers(List<Tower> towers) {
        this.towers = towers;
    }

    public List<Wave> getWaves() {
        return waves;
    }

    public void setWaves(List<Wave> waves) {
        this.waves = waves;
    }

    public List<Enemy> getAttackingEnemies() {
        return attackingEnemies;
    }

    public void setAttackingEnemies(List<Enemy> attackingEnemies) {
        this.attackingEnemies = attackingEnemies;
    }

    public boolean isEnemiesSpawned() {
        return enemiesSpawned;
    }

    public void setEnemiesSpawned(boolean enemiesSpawned) {
        this.enemiesSpawned = enemiesSpawned;
    }

    @Override
    public void registerObserver(Observer observer) {
        this.observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        this.observers.remove(observer);
    }

    @Override
    public void notifyObserver() {
        observers.forEach(Observer::update);
    }
}
