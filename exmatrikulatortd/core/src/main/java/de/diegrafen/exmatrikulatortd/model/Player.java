package de.diegrafen.exmatrikulatortd.model;

import de.diegrafen.exmatrikulatortd.model.enemy.Enemy;
import de.diegrafen.exmatrikulatortd.model.enemy.Wave;
import de.diegrafen.exmatrikulatortd.model.tower.Tower;
import de.diegrafen.exmatrikulatortd.view.Observer;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

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
//
//    /**
//     * Der Zustand des laufenden Spiels
//     */
//    @ManyToOne
//    @JoinColumn(name = "gamestate_id")
//    private Gamestate gameState;

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
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Tower> towers;

    /**
     * Die Angriffswellen, die mit dem Spieler assoziiert sind
     */
    @OneToMany(cascade = CascadeType.ALL)
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Wave> waves;

    /**
     * Die Angriffswellen, die mit dem Spieler assoziiert sind
     */
    @OneToMany(mappedBy = "attackedPlayer", cascade = CascadeType.ALL)
    @LazyCollection(LazyCollectionOption.FALSE)
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
     * Der Schwierigkeitsgrad des Spieles
     */
    @Enumerated(EnumType.ORDINAL)
    private Difficulty difficulty;

    private boolean victorious;

    private boolean lost;

    private int killTracker;

    private int towerCounter;

    /**
     * Default-Konstruktur. Wird von JPA vorausgesetzt.
     */
    public Player() {
        this.observers = new LinkedList<>();
    }

    public Player(int playerNumber) {
        this.attackingEnemies = new ArrayList<>();
        this.towers = new ArrayList<>();
        this.wayPoints = new ArrayList<>();
        this.waves = new ArrayList<>();
        this.observers = new ArrayList<>();
        this.playerNumber = playerNumber;

        this.timeSinceLastSpawn = 0;
        this.enemiesSpawned = false;
        this.killTracker = 0;
        this.towerCounter = 0;
    }

    /**
     * Copy-Konstruktor für den Ladevorgang
     * @param player
     * @param gamestate
     */
    public Player(Player player, Gamestate gamestate) {
        this.playerNumber = player.playerNumber;
        this.playerName = player.playerName;
        this.score = player.score;
        this.resources = player.resources;
        this.maxLives = player.maxLives;
        this.currentLives = player.currentLives;
        this.timeSinceLastSpawn = player.timeSinceLastSpawn;
        this.enemiesSpawned = player.enemiesSpawned;
        this.difficulty = player.difficulty;
        this.victorious = player.victorious;
        this.lost = player.lost;
        this.towers = new LinkedList<>();

        this.wayPoints = new LinkedList<>();
        player.getWayPoints().forEach(waypoint -> wayPoints.add(new Coordinates(waypoint)));

        this.waves = new LinkedList<>();
        player.getWaves().forEach(wave -> waves.add(new Wave(wave)));

        this.attackingEnemies = new LinkedList<>();

        for (Enemy enemy : player.getAttackingEnemies()) {
            Enemy newEnemy = new Enemy(enemy, this);
            attackingEnemies.add(newEnemy);
            gamestate.addEnemy(newEnemy);
        }
        this.killTracker = player.killTracker;
        this.towerCounter = player.towerCounter;
    }

    public void addEnemy(Enemy attackingEnemy) {
        this.attackingEnemies.add(attackingEnemy);
    }

    public void addTower(Tower tower) {
        towers.add(tower);
    }

    public List<Coordinates> getWayPoints() {
        return wayPoints;
    }

    public void setWayPoints(List<Coordinates> wayPoints) {
        this.wayPoints = wayPoints;
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

    public void setResources(int resources) {
        this.resources = resources;
    }

    public void addToResources(int resources) {
        this.resources += resources;
    }

    public void addToScore(int score) {
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

    public String getPlayerName() {
        return playerName;
    }
    public void setPlayerName(String name) {
        playerName = name;
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

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public void setVictorious(boolean victorious) {
        this.victorious = victorious;
    }

    public boolean isVictorious() {
        return victorious;
    }

    void copyWaves(List<Wave> waves) {
        this.waves = new LinkedList<>();
        for (Wave wave : waves) {
            this.waves.add(new Wave(wave));
        }
    }

    public boolean hasLost() {
        return lost;
    }

    public void setLost(boolean lost) {
        this.lost = lost;
    }

    public void incrementBodyTracker() {
        killTracker++;
    }

    public int getKillTracker(){
        return killTracker;
    }

    public void incrementTowerCounter(){
        towerCounter++;
    }
    public int getTowerCounter() {
        return towerCounter;
    }
}
