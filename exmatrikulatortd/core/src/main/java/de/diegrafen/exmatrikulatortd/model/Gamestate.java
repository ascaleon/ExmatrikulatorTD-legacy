package de.diegrafen.exmatrikulatortd.model;

import de.diegrafen.exmatrikulatortd.model.enemy.Enemy;
import de.diegrafen.exmatrikulatortd.model.enemy.Wave;
import de.diegrafen.exmatrikulatortd.model.tower.Projectile;
import de.diegrafen.exmatrikulatortd.model.tower.Tower;
import de.diegrafen.exmatrikulatortd.view.Observer;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static de.diegrafen.exmatrikulatortd.util.Constants.TIME_BETWEEN_ROUNDS;

/**
 * Der Spielzustand. Verwaltet alle spielrelevanten Informationen über das Spielfeld, die Spieler, die Türme,
 * Angriffswellen und Gegner.
 *
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 13.06.2019 21:36
 */
@Entity
public class Gamestate extends BaseModel implements Observable {

    /**
     * Die eindeutige Serialisierungs-ID
     */
    static final long serialVersionUID = 48546846516547L;

    private int tileWidth;

    private int tileHeight;

    /**
     * Der Name der Map
     */
    private String mapName;

    private int numberOfColumns;

    private int numberOfRows;

    /**
     * Die Spielerinnen. Umfasst im Singleplayer-Modus ein Element und im Multiplayer-Modus zwei Elemente.
     */
    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(cascade=CascadeType.ALL)
    private List<Player> players;

    /**
     * Die Kollisionsmatrix, mit der bestimmt wird, ob ein Turm an einer bestimmten Stelle auf dem Spielfeld gebaut werden kann
     */
    @OneToMany(cascade=CascadeType.ALL)
    @LazyCollection(LazyCollectionOption.FALSE)
    private final List<Coordinates> collisionMatrix;

    @OneToMany(cascade=CascadeType.ALL)
    @LazyCollection(LazyCollectionOption.FALSE)
    private final List<Enemy> enemies;

    @OneToMany(cascade=CascadeType.ALL)
    @LazyCollection(LazyCollectionOption.FALSE)
    private final List<Tower> towers;

    @OneToMany(orphanRemoval = true, cascade=CascadeType.ALL)
    @LazyCollection(LazyCollectionOption.FALSE)
    private final List<Projectile> projectiles;

    /**
     * Die aktuelle Rundennummer
     */
    private int roundNumber;

    /**
     * Die Anzahl der Runden
     */
    private final int numberOfRounds;

    /**
     * Gibt an, ob das Spiel gerade aktiv ist
     * TODO: Könnte obsolet sein.
     */
    private boolean active;

    private boolean gameOver;

    private boolean newRound;

    private float timeUntilNextRound;

    private boolean roundEnded;

    private boolean endlessGame = false;

    private final transient List<Observer> observers;

    private int gameMode;

    /**
     * Konstruktor, der den Spielzustand mit Spielern und einem Schwierigkeitsgrad initialisiert
     */
    public Gamestate (List<Player> players, List<Wave> waves) {
        //TODO: Redundanz verringern (vgl. den Standardkonstruktor
        this.enemies = new ArrayList<>();
        this.towers = new ArrayList<>();
        this.projectiles = new ArrayList<>();
        this.collisionMatrix = new ArrayList<>();
        this.observers = new LinkedList<>();

        this.players = new LinkedList<>(players);
        this.players.forEach(player -> player.copyWaves(waves));
        this.numberOfRounds = waves.size();
        this.timeUntilNextRound = TIME_BETWEEN_ROUNDS;
        this.newRound = true;
    }

    /**
     * Default-Konstruktor. Wird von JPA benötigt
     */
    public Gamestate() {
        players = new ArrayList<>();
        enemies = new LinkedList<>();
        towers = new LinkedList<>();
        projectiles = new LinkedList<>();
        collisionMatrix = new LinkedList<>();
        this.observers = new LinkedList<>();

        this.newRound = true;
        this.roundNumber = 0;
        this.timeUntilNextRound = TIME_BETWEEN_ROUNDS;
        this.roundEnded = true;
        this.numberOfRounds = 5;
        this.gameOver = false;
    }

    /**
     * Kopier-Konstruktor. Kopiert bis auf die ID alle Attribute
     * @param gamestate Der Spielzustand, der kopiert werden soll.
     */
    public Gamestate(Gamestate gamestate) {
        this.tileWidth = gamestate.getTileWidth();
        this.tileHeight = gamestate.getTileHeight();
        this.mapName = gamestate.getMapName();
        this.numberOfColumns = gamestate.getNumberOfColumns();
        this.numberOfRows = gamestate.getNumberOfRows();
        this.roundNumber = gamestate.getRoundNumber();
        this.numberOfRounds = gamestate.getNumberOfRounds();
        this.active = gamestate.isActive();
        this.gameOver = gamestate.isGameOver();
        this.newRound = gamestate.isNewRound();
        this.timeUntilNextRound = gamestate.getTimeUntilNextRound();
        this.roundEnded = gamestate.isRoundEnded();
        this.endlessGame = gamestate.isEndlessGame();
        this.gameMode = gamestate.getGameMode();
        this.observers = new ArrayList<>();

        this.players = new ArrayList<>();
        this.enemies = new LinkedList<>();
        this.towers = new LinkedList<>();
        this.projectiles = new LinkedList<>();
        this.collisionMatrix = new LinkedList<>();

        // TODO: Kopierkonstruktor vereinfachen, wenn möglich.

        gamestate.getPlayers().forEach(player -> {
            players.add(new Player(player, this));
            System.out.println(player.getPlayerNumber());
        });

        gamestate.getProjectiles().forEach(projectile -> projectiles.add(new Projectile(projectile)));

        for (Coordinates mapCell : gamestate.getCollisionMatrix()) {
            Coordinates coordinates = new Coordinates(mapCell);
            this.collisionMatrix.add(coordinates);
            Tower tower = coordinates.getTower();
            if (tower != null) {
                tower.setPosition(coordinates);
                Player owner = players.get(coordinates.getBuildableByPlayer());
                owner.addTower(tower);
                tower.setOwner(owner);
                this.towers.add(tower);
            }
        }

        for (int i = 0; i < gamestate.getProjectiles().size(); i++) {
            Projectile projectile = gamestate.getProjectiles().get(i);
            int enemyIndex = gamestate.getEnemies().indexOf(projectile.getTarget());
            int towerIndex = gamestate.getTowers().indexOf(projectile.getTowerThatShot());

            Enemy enemy = null;

            if (enemyIndex >= 0) {
                enemy = enemies.get(enemyIndex);
                this.projectiles.get(i).setTarget(enemy);
            }

            if (towerIndex >= 0) {
                Tower tower = towers.get(towerIndex);
                this.projectiles.get(i).setTowerThatShot(tower);
                if (enemy != null) {
                    tower.setCurrentTarget(enemy);
                }
            }
        }
    }

    public void addEnemy (Enemy enemy) {
        enemies.add(enemy);
    }

    public List<Enemy> getEnemies() {
        return enemies;
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

    public int getNumberOfColumns() {
        return numberOfColumns;
    }

    public void setNumberOfColumns(int numberOfColumns) {
        this.numberOfColumns = numberOfColumns;
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

    public int getRoundNumber() {
        return roundNumber;
    }

    public void setRoundNumber(int roundNumber) {
        this.roundNumber = roundNumber;
    }

    public int getNumberOfRounds() {
        return numberOfRounds;
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

    public void removeTower(Tower tower) {
        towers.remove(tower);
    }

    public List<Tower> getTowers() {
        return towers;
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

    public List<Projectile> getProjectiles() {
        return projectiles;
    }

    public void addProjectile(Projectile projectile) {
        this.projectiles.add(projectile);
    }

    public void removeProjectile(Projectile projectile) {
        this.projectiles.remove(projectile);
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

    public boolean isEndlessGame() {
        return endlessGame;
    }

    public void setEndlessGame(boolean endlessGame) {
        this.endlessGame = endlessGame;
    }

    public int getGameMode() {
        return gameMode;
    }

    public void setGameMode(int gameMode) {
        this.gameMode = gameMode;
    }

    public int getTileWidth() {
        return tileWidth;
    }

    public void setTileWidth(int tileWidth) {
        this.tileWidth = tileWidth;
    }

    public int getTileHeight() {
        return tileHeight;
    }

    public void setTileHeight(int tileHeight) {
        this.tileHeight = tileHeight;
    }

    public String getMapName() {
        return mapName;
    }
}