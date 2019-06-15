package de.diegrafen.exmatrikulatortd.communication.client;

import de.diegrafen.exmatrikulatortd.communication.server.Response;
import de.diegrafen.exmatrikulatortd.model.enemy.Enemy;
import de.diegrafen.exmatrikulatortd.model.tower.Tower;

/**
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 15.06.2019 05:37
 */
public interface ClientInterface {

    public void buildTower (Tower tower, int xPosition, int yPosition);

    public void sellTower (Tower tower);

    public void upgradeTower (Tower tower);

    public void sendEnemy (Enemy enemy);

    public void updateLocalGameState (Response response);

}
