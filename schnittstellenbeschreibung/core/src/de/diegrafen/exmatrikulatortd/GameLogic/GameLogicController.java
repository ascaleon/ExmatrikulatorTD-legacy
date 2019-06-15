package de.diegrafen.exmatrikulatortd.GameLogic;

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

    @Override
    public void update() {

    }

    public void buildTower (Tower tower, int xPosition, int yPosition) {

    }

    @Override
    public void sellTower(Tower tower) {

    }

    @Override
    public void upgradeTower(Tower tower) {

    }

    @Override
    public void sendEnemy(Enemy enemy) {

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
}
