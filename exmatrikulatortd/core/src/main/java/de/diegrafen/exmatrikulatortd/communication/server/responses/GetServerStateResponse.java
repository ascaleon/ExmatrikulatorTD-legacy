package de.diegrafen.exmatrikulatortd.communication.server.responses;

import de.diegrafen.exmatrikulatortd.model.Gamestate;
import de.diegrafen.exmatrikulatortd.model.Player;
import de.diegrafen.exmatrikulatortd.model.enemy.Enemy;
import de.diegrafen.exmatrikulatortd.model.tower.Projectile;
import de.diegrafen.exmatrikulatortd.model.tower.Tower;

import java.util.List;

/**
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 15.06.2019 12:35
 */
public class GetServerStateResponse extends Response {

    private List<Tower> towers;

    private List<Player> players;

    private float timeUntilNextRound;

    public GetServerStateResponse() {
        super();
    }

    public GetServerStateResponse(List<Tower> towers, List<Player> players, float timeUntilNextRound) {
        this();
        this.towers = towers;
        this.players = players;
        this.timeUntilNextRound = timeUntilNextRound;
    }


    public List<Tower> getTowers() {
        return towers;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public float getTimeUntilNextRound() {
        return timeUntilNextRound;
    }
}
