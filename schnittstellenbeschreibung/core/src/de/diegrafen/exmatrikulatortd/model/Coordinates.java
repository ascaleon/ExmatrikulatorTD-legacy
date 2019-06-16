package de.diegrafen.exmatrikulatortd.model;

import de.diegrafen.exmatrikulatortd.model.tower.Tower;

import javax.persistence.*;
import java.awt.geom.Point2D;

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
}
