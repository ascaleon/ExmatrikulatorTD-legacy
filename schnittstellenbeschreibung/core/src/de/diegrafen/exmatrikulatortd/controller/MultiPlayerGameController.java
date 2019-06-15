package de.diegrafen.exmatrikulatortd.controller;

import de.diegrafen.exmatrikulatortd.model.BaseModel;
import de.diegrafen.exmatrikulatortd.model.Gamestate;
import de.diegrafen.exmatrikulatortd.model.enemy.Enemy;
import de.diegrafen.exmatrikulatortd.model.tower.Tower;
import de.diegrafen.exmatrikulatortd.view.screens.BaseScreen;

/**
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 14.06.2019 02:09
 */
public class MultiPlayerGameController extends GameController {


    public MultiPlayerGameController(BaseScreen screen, BaseModel model) {
        super(screen, model);
    }

    @Override
    public boolean buildTower(Tower tower) {
        return false;
    }

    @Override
    public void upgradeTower(Tower tower) {

    }

    @Override
    public void sellTower(Tower tower) {

    }


    public void sendEnemy(Enemy enemy) {

    }
}
