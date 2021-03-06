package de.diegrafen.exmatrikulatortd.communication.client.requests;

/**
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 15.06.2019 12:31
 */
public class BuildRequest extends Request {

    private int towerType;

    private int xCoordinate;

    private int yCoordinate;

    private int playerNumber;

    public BuildRequest() {
        super();
    }

    public BuildRequest(int towerType, int xCoordinate, int yCoordinate, int playerNumber) {
        super();
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
