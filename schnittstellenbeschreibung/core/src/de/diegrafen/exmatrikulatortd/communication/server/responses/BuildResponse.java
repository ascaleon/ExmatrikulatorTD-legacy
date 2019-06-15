package de.diegrafen.exmatrikulatortd.communication.server.responses;

import de.diegrafen.exmatrikulatortd.model.Gamestate;
import de.diegrafen.exmatrikulatortd.model.tower.Tower;

/**
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 15.06.2019 13:16
 */
public class BuildResponse extends Response {

    private boolean wasSuccessful;

    private Tower tower;

    public BuildResponse (boolean wasSuccessful, Tower tower) {

    }

}
