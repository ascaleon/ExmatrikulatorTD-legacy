package de.diegrafen.exmatrikulatortd.controller;

import de.diegrafen.exmatrikulatortd.model.Coordinates;
import de.diegrafen.exmatrikulatortd.model.Gamestate;
import de.diegrafen.exmatrikulatortd.model.enemy.Enemy;
import de.diegrafen.exmatrikulatortd.model.tower.Tower;
import de.diegrafen.exmatrikulatortd.view.screens.GameScreen;

/**
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 15.06.2019 01:24
 */
public class GameLogicController implements LogicController {

    private Gamestate gamestate;

    private GameScreen gameScreen;

    public GameLogicController (Gamestate gamestate) {
        this.gamestate = gamestate;
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

    public Gamestate getGamestate() {
        return gamestate;
    }

    public void setGamestate(Gamestate gamestate) {
        this.gamestate = gamestate;
    }

    public GameScreen getGameScreen() {
        return gameScreen;
    }

    public void setGameScreen(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
    }

    public void initGameScreen() {
    }
}
