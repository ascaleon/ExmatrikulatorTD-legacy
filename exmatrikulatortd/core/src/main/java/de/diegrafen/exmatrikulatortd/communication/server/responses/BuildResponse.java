package de.diegrafen.exmatrikulatortd.communication.server.responses;

import de.diegrafen.exmatrikulatortd.controller.factories.TowerFactory;

import static de.diegrafen.exmatrikulatortd.controller.factories.TowerFactory.*;

/**
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 15.06.2019 13:16
 */
public class BuildResponse extends Response {

    private int towerType;

    private int xCoordinate;

    private int yCoordinate;

    private int playerNumber;

    public BuildResponse() {
        super();
    }

    public BuildResponse(int towerType, int xCoordinate, int yCoordinate, int playerNumber) {
        this.towerType = towerType;
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.playerNumber = playerNumber;
    }

    public int getTowerType() {
        return towerType;
    }

    public int getxCoordinate() {
        return xCoordinate;
    }

    public int getyCoordinate() {
        return yCoordinate;
    }

    public int getPlayerNumber() {
        return playerNumber;
    }
}
