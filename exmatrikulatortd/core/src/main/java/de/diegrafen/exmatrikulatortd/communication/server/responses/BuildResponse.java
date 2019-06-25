package de.diegrafen.exmatrikulatortd.communication.server.responses;

import de.diegrafen.exmatrikulatortd.controller.factories.TowerFactory;

/**
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 15.06.2019 13:16
 */
public class BuildResponse extends Response {

    private boolean successful;

    private TowerFactory.TowerType towerType;

    private int xPosition;

    private int yPosition;

    private int playerNumber;

    public BuildResponse(boolean successful) {
        this.successful = successful;
    }

    public BuildResponse(boolean successful, TowerFactory.TowerType towerType, int xPosition, int yPosition, int playerNumber) {
        this.successful = successful;
        this.towerType = towerType;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.playerNumber = playerNumber;
    }

    public boolean wasSuccessful() {
        return successful;
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
