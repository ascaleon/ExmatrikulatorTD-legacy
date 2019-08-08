package de.diegrafen.exmatrikulatortd.communication.server.responses;

import de.diegrafen.exmatrikulatortd.model.Gamestate;
import de.diegrafen.exmatrikulatortd.model.tower.Tower;

import java.util.List;

/**
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 15.06.2019 12:35
 */
public class GetServerStateResponse extends Response {

    private Gamestate gamestate;

    private List<Tower> towers;


    public GetServerStateResponse() {
        super();
    }

    public GetServerStateResponse(List<Tower> towers) {
        super();
        this.towers = towers;
        //this.gamestate = gamestate;
    }

    public Gamestate getGamestate() {
        return gamestate;
    }

    public List<Tower> getTowers() {
        return towers;
    }
}
