package de.diegrafen.exmatrikulatortd.communication.client.requests;

import de.diegrafen.exmatrikulatortd.model.tower.Tower;

/**
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 15.06.2019 12:31
 */
public class UpgradeRequest extends Request {

    private Tower towerToUpgrade;

    public UpgradeRequest(Tower towerToUpgrade) {
        super();
        this.towerToUpgrade = towerToUpgrade;
    }
}
