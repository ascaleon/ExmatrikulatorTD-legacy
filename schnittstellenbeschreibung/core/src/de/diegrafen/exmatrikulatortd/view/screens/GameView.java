package de.diegrafen.exmatrikulatortd.view.screens;

import de.diegrafen.exmatrikulatortd.model.Coordinates;
import de.diegrafen.exmatrikulatortd.model.enemy.Enemy;
import de.diegrafen.exmatrikulatortd.model.tower.Tower;

/**
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 15.06.2019 04:20
 */
public interface GameView {

    public void addTower (Tower tower);

    public void removeTower (Tower tower);

    public void addEnemy (Enemy enemy);

    public void removeEnemy (Enemy enemy);

}
