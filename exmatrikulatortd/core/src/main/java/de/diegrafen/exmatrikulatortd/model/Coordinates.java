package de.diegrafen.exmatrikulatortd.model;

import de.diegrafen.exmatrikulatortd.model.enemy.Enemy;
import de.diegrafen.exmatrikulatortd.model.tower.Tower;

import javax.persistence.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import static de.diegrafen.exmatrikulatortd.util.Constants.TILE_SIZE;

/**
 *
 * Repräsentiert Koordinaten auf dem Spielfeld. Dient zur Realisierung der Kollisionsmatrix des Spielfeldes und
 * zur Angabe der Koordinaten eines Turmes
 *
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 13.06.2019 21:41
 */
@Entity
@Table(name = "waypoints")
@SecondaryTable(name = "collision_matrix")
@NamedQueries({
        @NamedQuery(name="Coordinates.findAll",
                query="SELECT c FROM Coordinates c"),
        @NamedQuery(name="Coordinates.findByXandY",
                query="SELECT c FROM Coordinates c WHERE c.xCoordinate = :xCoordinate AND c.yCoordinate = :yCoordinate"),
        @NamedQuery(name="Coordinates.findBuildableFields",
                query="SELECT c FROM Coordinates c WHERE c.xCoordinate = :xCoordinate AND c.yCoordinate = :yCoordinate")
})
public class Coordinates extends BaseModel {

    /**
     * Die eindeutige Serialisierungs-ID
     */
    static final long serialVersionUID = 8375877489294115L;

    /**
     * Die x-Position der Koordinate
     */
    private int xCoordinate;

    /**
     * Die y-Position der Koordinate
     */
    private int yCoordinate;

    /**
     * Der assoziierte Spielzustand
     */
    @ManyToOne
    private Gamestate gameState;

    /**
     * Gibt an, ob das Spielfeld an der angegebenen Stelle bebaubar ist.
     */
    @Column(table = "collision_matrix")
    private boolean isBuildable;

    @Column(table = "waypoints")
    private int waypointIndex;

    @Column(table = "waypoints")
    private int playerNumber;

    /**
     * Der mit der Koordinate assoziierte Turm
     */
    @OneToOne
    @JoinColumn(table = "collision_matrix")
    private Tower tower;

    @OneToMany(mappedBy="currentMapCell")
    private List<Enemy> enemiesInMapCell;

    @ManyToMany
    private List<Coordinates> neighbours;


    /**
     * Gibt an, welcher Spieler an der Koordinate bauen darf
     */
    private int buildableByPlayer;

    private int tileSize = TILE_SIZE;

    /**
     * Default-Konstruktor. Wird von JPA benötigt.
     */
    public Coordinates() {

    }

    /**
     * Konstruktor, der die Koordinate mit einer x- und einer y-Position initialisiert
     * @param xCoordinate
     * @param yCoordinate
     */
    public Coordinates(int xCoordinate, int yCoordinate) {
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
    }

    public Coordinates(int xCoordinate, int yCoordinate, int buildableByPlayer) {
        this(xCoordinate, yCoordinate);
        this.buildableByPlayer = buildableByPlayer;
        this.enemiesInMapCell = new ArrayList<>();
        this.neighbours = new ArrayList<>();
    }

    public Coordinates(int xCoordinate, int yCoordinate, int playerNumber, int waypointIndex) {
        this(xCoordinate, yCoordinate);
        this.playerNumber = playerNumber;
        this.waypointIndex = waypointIndex;
    }

    public int getXCoordinate() {
        return xCoordinate;
    }

    public int getYCoordinate() {
        return yCoordinate;
    }

    public int getxCoordinate() {
        return xCoordinate;
    }

    public void setxCoordinate(int xCoordinate) {
        this.xCoordinate = xCoordinate;
    }

    public int getyCoordinate() {
        return yCoordinate;
    }

    public void setyCoordinate(int yCoordinate) {
        this.yCoordinate = yCoordinate;
    }

    public Tower getTower() {
        return tower;
    }

    public void setTower(Tower tower) {
        this.tower = tower;
    }

    public int getBuildableByPlayer() {
        return buildableByPlayer;
    }

    public List<Coordinates> getNeighbours() {
        return neighbours;
    }

    public void addNeighbour (Coordinates neighbour) {
        this.neighbours.add(neighbour);
    }

    public int getTileSize() {
        return tileSize;
    }

    @Override
    public String toString () {
        return "xPosition: " + xCoordinate + ", yPosition: " + yCoordinate;
    }

    public int getWaypointIndex() {
        return waypointIndex;
    }

    public void setWaypointIndex(int waypointIndex) {
        this.waypointIndex = waypointIndex;
    }

    public int getPlayerNumber() {
        return playerNumber;
    }

    public void setPlayerNumber(int playerNumber) {
        this.playerNumber = playerNumber;
    }
}
