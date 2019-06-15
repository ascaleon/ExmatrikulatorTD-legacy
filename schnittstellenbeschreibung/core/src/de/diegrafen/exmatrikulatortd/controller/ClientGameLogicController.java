package de.diegrafen.exmatrikulatortd.controller;

import de.diegrafen.exmatrikulatortd.communication.client.GameClient;
import de.diegrafen.exmatrikulatortd.model.Coordinates;
import de.diegrafen.exmatrikulatortd.model.Gamestate;
import de.diegrafen.exmatrikulatortd.model.enemy.Enemy;
import de.diegrafen.exmatrikulatortd.model.tower.Tower;

/**
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 15.06.2019 05:29
 */
public class ClientGameLogicController extends GameLogicController {

    private GameClient gameClient;

    public ClientGameLogicController (Gamestate gameState, GameClient gameClient) {
        super(gameState);
        this.gameClient = gameClient;
    }

    @Override
    public void update(float deltaTime) {

    }

    @Override
    public boolean buildTower(Tower tower, Coordinates coordinates) {
        return false;
    }

    @Override
    public boolean sellTower(Tower tower) {
        return false;
    }

    @Override
    public boolean upgradeTower(Tower tower) {
        return false;
    }

    @Override
    public boolean sendEnemy(Enemy enemy) {
        return false;
    }

    public void refreshLocalGameState () {

    }


}
