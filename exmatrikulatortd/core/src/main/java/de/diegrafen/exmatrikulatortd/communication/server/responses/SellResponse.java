package de.diegrafen.exmatrikulatortd.communication.server.responses;

import de.diegrafen.exmatrikulatortd.model.tower.Tower;

/**
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 15.06.2019 13:53
 */
public class SellResponse extends Response {

    private boolean successful;

    private int xPosition;

    private int yPosition;

    private int playerNumber;

    public SellResponse(boolean successful, int xPosition, int yPosition, int playerNumber) {
        this.successful = successful;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.playerNumber = playerNumber;
    }

    public SellResponse(boolean successful) {
        this.successful = successful;
    }

    public boolean wasSuccessful() {
        return successful;
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
