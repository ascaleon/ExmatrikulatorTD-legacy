package de.diegrafen.exmatrikulatortd.communication.client.requests;

import de.diegrafen.exmatrikulatortd.controller.factories.TowerFactory;
import de.diegrafen.exmatrikulatortd.model.Coordinates;
import de.diegrafen.exmatrikulatortd.model.tower.Tower;

/**
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 15.06.2019 12:31
 */
public class BuildRequest extends Request {

    private TowerFactory.TowerType towerType;

    private int xPosition;

    private int yPosition;

    private int playerNumber;

    public BuildRequest(TowerFactory.TowerType towerType, int xPosition, int yPosition, int playerNumber) {
        super();
        this.towerType = towerType;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.playerNumber = playerNumber;
    }

    public TowerFactory.TowerType getTowerType() {
        return towerType;
    }

    public int getxPosition() {
        return xPosition;
    }

    public int getyPosition() {
        return yPosition;
    }

    public int getPlayerNumber() {
        return playerNumber;
    }
}
