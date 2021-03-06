package de.diegrafen.exmatrikulatortd.view.screens;

import de.diegrafen.exmatrikulatortd.model.Coordinates;
import de.diegrafen.exmatrikulatortd.model.enemy.Enemy;
import de.diegrafen.exmatrikulatortd.view.gameobjects.EnemyObject;
import de.diegrafen.exmatrikulatortd.view.gameobjects.GameObject;
import de.diegrafen.exmatrikulatortd.view.gameobjects.TowerObject;

/**
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 15.06.2019 04:20
 */
public interface GameView {

    public void addTower (TowerObject towerObject);

    public void removeTower (TowerObject towerObject);

    public void addEnemy (EnemyObject enemyObject);

    public void removeEnemy (EnemyObject enemyObject);

}
