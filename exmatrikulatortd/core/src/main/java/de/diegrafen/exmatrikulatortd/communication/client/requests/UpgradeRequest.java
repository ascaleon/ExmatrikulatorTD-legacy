package de.diegrafen.exmatrikulatortd.communication.client.requests;

import de.diegrafen.exmatrikulatortd.model.tower.Tower;

/**
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 15.06.2019 12:31
 */
public class UpgradeRequest extends Request {

    private int xPosition;

    private int yPosition;

    private int playerNumber;

    public UpgradeRequest(int xPosition, int yPosition, int playerNumber) {
        super();
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.playerNumber = playerNumber;
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
