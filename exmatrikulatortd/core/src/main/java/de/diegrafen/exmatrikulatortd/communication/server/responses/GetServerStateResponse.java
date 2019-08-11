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

    private Gamestate gamestate;

    private List<Tower> towers;

    private List<Player> players;

    private List<Enemy> enemies;

    private List<Projectile> projectiles;

    public GetServerStateResponse() {
        super();
    }

    public GetServerStateResponse(List<Tower> towers, List<Player> players, List<Enemy> enemies, List<Projectile> projectiles) {
        super();
        this.towers = towers;
        this.players = players;
        this.enemies = enemies;
        this.projectiles = projectiles;
    }

    public GetServerStateResponse(Gamestate gamestate) {
        this.gamestate = gamestate;
    }

    public Gamestate getGamestate() {
        return gamestate;
    }

    public List<Tower> getTowers() {
        return towers;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }

    public List<Projectile> getProjectiles() {
        return projectiles;
    }
}
