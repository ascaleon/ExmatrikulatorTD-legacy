package de.diegrafen.exmatrikulatortd.model;

import de.diegrafen.exmatrikulatortd.model.enemy.Enemy;
import de.diegrafen.exmatrikulatortd.model.enemy.Wave;
import de.diegrafen.exmatrikulatortd.model.tower.Tower;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Die Spielerklasse. Verwaltet die Informationen über den Spieler wie Name, Anzahl der Leben und die erzielten Punkte
 * sowie über die Wellen von Angreifern, die dem Spieler angreifen werden.
 */
@Entity
@Table(name = "Players")
public class Player extends BaseModel {

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
    @OneToMany(mappedBy="owner")
    private List<Tower> towers;

    /**
     * Die Angriffswellen, die mit dem Spieler assoziiert sind
     */
    @OneToMany(mappedBy="player")
    private List<Wave> waves;

    /**
     * Die Angriffswellen, die mit dem Spieler assoziiert sind
     */
    @OneToMany(mappedBy="attackedPlayer")
    private List<Enemy> attackingEnemies;

    /**
     * Die Wegpunkte, die Gegner auf der Karte auf dem Weg zum Endpunkt zurücklegen
     */
    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Coordinates> wayPoints;

    /**
     * Default-Konstruktur. Wird von JPA vorausgesetzt.
     */
    public Player () {
        this.attackingEnemies = new ArrayList<Enemy>();
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

    /**
     * Fügt der nächsten Welle einen Gegner hinzu
     */
    public void addEnemyToNextWave () {

    }

}
