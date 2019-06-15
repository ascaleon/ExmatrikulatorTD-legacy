package de.diegrafen.exmatrikulatortd.GameLogic;

import de.diegrafen.exmatrikulatortd.model.enemy.Enemy;
import de.diegrafen.exmatrikulatortd.model.tower.Tower;

/**
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 15.06.2019 05:28
 */
public interface LogicController {

    public void update();

    public void buildTower (Tower tower, int xPosition, int yPosition);

    public void sellTower (Tower tower);

    public void upgradeTower (Tower tower);

    public void sendEnemy (Enemy enemy);

}
