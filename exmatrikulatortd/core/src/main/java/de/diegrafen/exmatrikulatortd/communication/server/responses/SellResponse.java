package de.diegrafen.exmatrikulatortd.communication.server.responses;

import de.diegrafen.exmatrikulatortd.model.tower.Tower;

/**
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 15.06.2019 13:53
 */
public class SellResponse extends Response {

    private boolean successful;

    private int xCoordinate;

    private int yCoordinate;

    private int playerNumber;

    public SellResponse(boolean successful, int xCoordinate, int yCoordinate, int playerNumber) {
        this.successful = successful;
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.playerNumber = playerNumber;
    }

    public SellResponse(boolean successful) {
        this.successful = successful;
    }

    public boolean wasSuccessful() {
        return successful;
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
