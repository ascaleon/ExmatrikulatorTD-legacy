package de.diegrafen.exmatrikulatortd.communication.server;

import de.diegrafen.exmatrikulatortd.model.Coordinates;
import de.diegrafen.exmatrikulatortd.model.enemy.Enemy;
import de.diegrafen.exmatrikulatortd.model.tower.Tower;

/**
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 16.06.2019 19:51
 */
public interface ServerInterface {

    boolean buildTower (Tower tower, Coordinates coordinates);

    boolean sellTower (Tower tower);

    boolean upgradeTower (Tower tower);

    boolean sendEnemy (Enemy enemy);
}
