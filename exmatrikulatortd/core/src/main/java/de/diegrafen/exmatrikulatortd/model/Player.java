package de.diegrafen.exmatrikulatortd.model;

import de.diegrafen.exmatrikulatortd.model.enemy.Wave;
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
     * Die Angriffswellen, die mit dem Spieler assoziiert sind
     */
    @OneToMany(cascade = CascadeType.ALL)
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Wave> waves;

    /**
     * Die Wegpunkte, die Gegner auf der Karte auf dem Weg zum Endpunkt zurücklegen
     */
    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Coordinates> wayPoints;

    private float timeSinceLastSpawn;

    private boolean enemiesSpawned;

    private transient List<Observer> observers;

    /**
     * Der Schwierigkeitsgrad des Spieles
     */
    private int difficulty;

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
     */
    public Player(Player player) {
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

        this.wayPoints = new LinkedList<>();
        player.getWayPoints().forEach(waypoint -> wayPoints.add(new Coordinates(waypoint)));

        this.waves = new LinkedList<>();
        player.getWaves().forEach(wave -> waves.add(new Wave(wave)));

        this.killTracker = player.killTracker;
        this.towerCounter = player.towerCounter;
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

    public float getTimeSinceLastSpawn() {
        return timeSinceLastSpawn;
    }

    public void setTimeSinceLastSpawn(float timeSinceLastSpawn) {
        this.timeSinceLastSpawn = timeSinceLastSpawn;
    }

    public String getPlayerName() {
        return playerName;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public List<Wave> getWaves() {
        return waves;
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

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
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

    public void incrementKillCounter() {
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
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
