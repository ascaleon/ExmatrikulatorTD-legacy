package de.diegrafen.exmatrikulatortd.communication.server.responses;

import de.diegrafen.exmatrikulatortd.model.tower.Tower;

/**
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 15.06.2019 13:30
 */
public class UpgradeResponse extends Response {

    private boolean successful;

    private Tower newTower;

    public UpgradeResponse(boolean successful, Tower newTower) {
        super();
        this.successful = successful;
        this.newTower = newTower;
    }

    public boolean wasSuccessful() {
        return successful;
    }

    public Tower getNewTower() {
        return newTower;
    }
}
