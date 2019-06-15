package de.diegrafen.exmatrikulatortd.view.screens;

import de.diegrafen.exmatrikulatortd.model.Coordinates;
import de.diegrafen.exmatrikulatortd.view.gameobjects.EnemyObject;
import de.diegrafen.exmatrikulatortd.view.gameobjects.GameObject;
import de.diegrafen.exmatrikulatortd.view.gameobjects.TowerObject;

/**
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 15.06.2019 04:20
 */
public interface GameView {

    public void addTower (GameObject towerObject);

    public void removeTower (GameObject towerObject);

    public void addEnemy (GameObject enemyObject);

    public void removeEnemy (GameObject enemyObject);

}
