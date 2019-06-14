package de.diegrafen.exmatrikulatortd.controller;

import de.diegrafen.exmatrikulatortd.model.BaseModel;
import de.diegrafen.exmatrikulatortd.model.tower.Tower;
import de.diegrafen.exmatrikulatortd.view.screens.BaseScreen;

/**
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 14.06.2019 02:08
 */
public class SinglePlayerGameController extends GameController {


    public SinglePlayerGameController(BaseScreen screen, BaseModel model) {
        super(screen, model);
    }

    @Override
    public void buildTower(Tower tower) {

    }

    @Override
    public void upgradeTower(Tower tower) {

    }

    @Override
    public void sellTower(Tower tower) {

    }
}
