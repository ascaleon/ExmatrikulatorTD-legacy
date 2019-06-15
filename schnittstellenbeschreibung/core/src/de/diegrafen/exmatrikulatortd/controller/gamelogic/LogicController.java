package de.diegrafen.exmatrikulatortd.controller.gamelogic;

import de.diegrafen.exmatrikulatortd.model.Coordinates;
import de.diegrafen.exmatrikulatortd.model.enemy.Enemy;
import de.diegrafen.exmatrikulatortd.model.tower.Tower;

/**
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 15.06.2019 05:28
 */
public interface LogicController {

    public void update(float deltaTime);

    public boolean buildTower (Tower tower, Coordinates coordinates);

    public boolean sellTower (Tower tower);

    public boolean upgradeTower (Tower tower);

    public boolean sendEnemy (Enemy enemy);

}
