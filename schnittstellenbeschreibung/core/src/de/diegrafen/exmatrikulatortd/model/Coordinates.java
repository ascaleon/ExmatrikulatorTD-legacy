package de.diegrafen.exmatrikulatortd.model;

import de.diegrafen.exmatrikulatortd.model.tower.Tower;

import javax.persistence.*;
import java.awt.geom.Point2D;

/**
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

    private int xCoordinate;

    private int yCoordinate;

    @ManyToOne
    private Gamestate gameState;

    @Column(table = "collision_matrix")
    private boolean isBuildable;

    @OneToOne
    @JoinColumn(table = "collision_matrix")
    private Tower tower;

    public Coordinates() {

    }

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
