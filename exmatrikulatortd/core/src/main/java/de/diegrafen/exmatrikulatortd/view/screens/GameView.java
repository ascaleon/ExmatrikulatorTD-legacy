package de.diegrafen.exmatrikulatortd.view.screens;

import de.diegrafen.exmatrikulatortd.model.Coordinates;
import de.diegrafen.exmatrikulatortd.model.Observable;
import de.diegrafen.exmatrikulatortd.model.enemy.Enemy;
import de.diegrafen.exmatrikulatortd.model.tower.Tower;
import de.diegrafen.exmatrikulatortd.view.gameobjects.EnemyObject;
import de.diegrafen.exmatrikulatortd.view.gameobjects.GameObject;
import de.diegrafen.exmatrikulatortd.view.gameobjects.TowerObject;

/**
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 15.06.2019 04:20
 */
public interface GameView {

    void addObservable (Observable observable);

    void addTower (Observable observable);

    void addEnemy (Observable observable);

    //void addTower (TowerObject towerObject);

    //void removeTower (TowerObject towerObject);

    //void addEnemy (EnemyObject enemyObject);

    //void removeEnemy (EnemyObject enemyObject);

}
