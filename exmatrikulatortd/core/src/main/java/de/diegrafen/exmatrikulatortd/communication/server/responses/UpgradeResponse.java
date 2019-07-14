package de.diegrafen.exmatrikulatortd.communication.server.responses;

/**
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 15.06.2019 13:30
 */
public class UpgradeResponse extends Response {

    private int xCoordinate;

    private int yCoordinate;

    private int playerNumber;

    public UpgradeResponse() {
        super();
    }

    public UpgradeResponse(int xCoordinate, int yCoordinate, int playerNumber) {
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.playerNumber = playerNumber;
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
