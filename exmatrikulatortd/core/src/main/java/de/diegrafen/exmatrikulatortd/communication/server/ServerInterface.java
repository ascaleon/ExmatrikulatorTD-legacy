package de.diegrafen.exmatrikulatortd.communication.server;

import de.diegrafen.exmatrikulatortd.controller.factories.TowerFactory;
import de.diegrafen.exmatrikulatortd.model.Coordinates;
import de.diegrafen.exmatrikulatortd.model.enemy.Enemy;
import de.diegrafen.exmatrikulatortd.model.tower.Tower;

/**
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 16.06.2019 19:51
 */
public interface ServerInterface {

    /**
     * Sendet eine BuildResponse an den Client
     * @param tower Der zu bauende Turm
     * @param coordinates Die Koordinaten des Turms
     */
    void buildTower(TowerFactory.TowerType towerType, int xPosition, int yPosition, int playerNumber);

    /**
     * Sendet eine SellResponse an den Client
     * @param tower Der zu bauende Turm
     */
    void sellTower (Tower tower);

    /**
     * Sendet eine UpgradeResponse an den Client
     * @param tower Der aufzurüstende Turm
     */
    void upgradeTower (Tower tower);

    /**
     * Sendet eine SendEnemyResponse an den Client
     * @param enemy Der zu sendende Gegner
     */
    void sendEnemy (Enemy enemy);
}
