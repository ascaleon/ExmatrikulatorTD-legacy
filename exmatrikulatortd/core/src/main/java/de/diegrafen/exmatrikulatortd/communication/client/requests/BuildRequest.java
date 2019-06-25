package de.diegrafen.exmatrikulatortd.communication.client.requests;

import de.diegrafen.exmatrikulatortd.model.Coordinates;
import de.diegrafen.exmatrikulatortd.model.tower.Tower;

/**
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 15.06.2019 12:31
 */
public class BuildRequest extends Request {

    private Tower tower;

    private Coordinates coordinates;

    public BuildRequest (Tower tower, Coordinates coordinates) {
        super();
        this.tower=tower;
        this.coordinates=coordinates;
    }
}
