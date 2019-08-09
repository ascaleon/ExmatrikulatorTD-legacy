package de.diegrafen.exmatrikulatortd.model;

import de.diegrafen.exmatrikulatortd.model.tower.Tower;

import javax.persistence.*;

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
public class Coordinates extends BaseModel {

    /**
     * Die eindeutige Serialisierungs-ID
     */
    static final long serialVersionUID = 8375877489294115L;

    /**
     * Die x-Position der Koordinate
     */
    private int xCoordinate;

    private int width;

    /**
     * Die y-Position der Koordinate
     */
    private int yCoordinate;

    private int height;

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
    private Coordinates(int xCoordinate, int yCoordinate, int width, int height) {
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.width = width;
        this.height = height;
    }

    public Coordinates(int xCoordinate, int yCoordinate, int buildableByPlayer, int width, int height) {
        this(xCoordinate, yCoordinate, width, height);
        this.buildableByPlayer = buildableByPlayer;
    }

    public Coordinates(int xCoordinate, int yCoordinate, int playerNumber, int waypointIndex, int width, int height) {
        this(xCoordinate, yCoordinate, width, height);
        this.playerNumber = playerNumber;
        this.waypointIndex = waypointIndex;
    }

    /**
     * Kopierkonstruktor
     *
     * @param coordinates Die zu kopierenden Koordinaten
     */
    public Coordinates(Coordinates coordinates) {
        this.xCoordinate = coordinates.xCoordinate;
        this.yCoordinate = coordinates.yCoordinate;
        this.playerNumber = coordinates.playerNumber;
        this.buildableByPlayer = coordinates.buildableByPlayer;
        this.waypointIndex = coordinates.waypointIndex;
        this.width = coordinates.width;
        this.height = coordinates.height;
        this.tower = null;
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

    @Override
    public String toString () {
        return "xPosition: " + xCoordinate + ", yPosition: " + yCoordinate;
    }

    public int getWaypointIndex() {
        return waypointIndex;
    }

    private int getPlayerNumber() {
        return playerNumber;
    }

    public void setPlayerNumber(int playerNumber) {
        this.playerNumber = playerNumber;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
