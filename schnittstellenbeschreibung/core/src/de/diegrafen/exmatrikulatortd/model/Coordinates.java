package de.diegrafen.exmatrikulatortd.model;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.awt.geom.Point2D;

/**
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 13.06.2019 21:41
 */
@Entity
@Table(name = "Coordinates")
public class Coordinates extends BaseModel {

    private int xCoordinate;

    private int yCoordinate;

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
