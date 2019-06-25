package de.diegrafen.exmatrikulatortd.communication.server.responses;

import de.diegrafen.exmatrikulatortd.model.Gamestate;

/**
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 15.06.2019 12:35
 */
public class GetServerStateResponse extends Response {

    Gamestate gamestate;

    public GetServerStateResponse(Gamestate gamestate) {
        super();
        this.gamestate=gamestate;
    }

}
