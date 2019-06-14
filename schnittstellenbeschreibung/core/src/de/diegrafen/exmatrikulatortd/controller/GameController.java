package de.diegrafen.exmatrikulatortd.controller;

import de.diegrafen.exmatrikulatortd.model.BaseModel;
import de.diegrafen.exmatrikulatortd.model.Player;
import de.diegrafen.exmatrikulatortd.model.tower.Tower;
import de.diegrafen.exmatrikulatortd.view.screens.BaseScreen;

/**
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 14.06.2019 02:01
 */
public abstract class GameController extends Controller {

    public GameController(BaseScreen screen, BaseModel model) {
        super(screen, model);
    }

    public abstract void buildTower (Tower tower);


    public abstract void upgradeTower (Tower tower);


    public abstract void sellTower (Tower tower);

}
