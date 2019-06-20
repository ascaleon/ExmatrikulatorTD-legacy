package de.diegrafen.exmatrikulatortd.model;

import de.diegrafen.exmatrikulatortd.model.enemy.Enemy;
import de.diegrafen.exmatrikulatortd.model.tower.Tower;

import javax.persistence.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * Repräsentiert Koordinaten auf dem Spielfeld. Dient zur Realisierung der Kollisionsmatrix des Spielfeldes und
 * zur Angabe der Koordinaten eines Turmes
 *
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 13.06.2019 21:41
 */
@Entity
@Table(name = "points")
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
        this.enemiesInMapCell = new ArrayList<Enemy>();
        this.neighbours = new ArrayList<Coordinates>();
    }

    public Coordinates(int xCoordinate, int yCoordinate, int buildableByPlayer) {
        this(xCoordinate, yCoordinate);
        this.buildableByPlayer = buildableByPlayer;
    }

    public int getXCoordinate() {
        return xCoordinate;
    }

    public void setXCoordinate(int xCoordinate) {
        this.xCoordinate = xCoordinate;
    }

    public int getYCoordinate() {
        return yCoordinate;
    }

    public void setYCoordinate(int yCoordinate) {
        this.yCoordinate = yCoordinate;
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

    public Gamestate getGameState() {
        return gameState;
    }

    public void setGameState(Gamestate gameState) {
        this.gameState = gameState;
    }

    public boolean isBuildable() {
        return isBuildable;
    }

    public void setBuildable(boolean buildable) {
        isBuildable = buildable;
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

    public void setBuildableByPlayer(int buildableByPlayer) {
        this.buildableByPlayer = buildableByPlayer;
    }

    public List<Enemy> getEnemiesInMapCell() {
        return enemiesInMapCell;
    }

    public void setEnemiesInMapCell(List<Enemy> enemiesInMapCell) {
        this.enemiesInMapCell = enemiesInMapCell;
    }

    public List<Coordinates> getNeighbours() {
        return neighbours;
    }

    public void setNeighbours (List<Coordinates> neighbours) {
        this.neighbours = neighbours;
    }

    public void addNeighbour (Coordinates neighbour) {
        this.neighbours.add(neighbour);
    }

    public void removeNeighbour (Coordinates neighbour) {
        this.neighbours.remove(neighbour);
    }

    public void addToEnemiesOnCell(Enemy enemy) {
        enemiesInMapCell.add(enemy);
    }

    public void removeFromEnemiesOnCell(Enemy enemy) {
        enemiesInMapCell.remove(enemy);
    }

    @Override
    public String toString () {
        return "xPosition: " + xCoordinate + ", yPosition: " + yCoordinate;
    }
}
