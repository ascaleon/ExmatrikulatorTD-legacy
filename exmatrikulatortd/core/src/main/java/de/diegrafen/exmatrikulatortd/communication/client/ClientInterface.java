package de.diegrafen.exmatrikulatortd.communication.client;

import de.diegrafen.exmatrikulatortd.controller.factories.TowerFactory;
import de.diegrafen.exmatrikulatortd.controller.gamelogic.LogicController;
import de.diegrafen.exmatrikulatortd.model.Coordinates;
import de.diegrafen.exmatrikulatortd.model.Gamestate;
import de.diegrafen.exmatrikulatortd.model.enemy.Enemy;
import de.diegrafen.exmatrikulatortd.model.tower.Tower;

/**
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 15.06.2019 05:37
 */
public interface ClientInterface {

    //boolean buildTower (Tower tower, Coordinates coordinates);

    void buildTower(TowerFactory.TowerType towerType, int xPosition, int yPosition, int playerNumber);

    void sellTower (Tower tower);

    void upgradeTower (Tower tower);

    void sendEnemy (Enemy enemy);

    Gamestate refreshLocalGameState ();

    /**
     * Stellt die Verbindung zu einem Server her.
     * @param host Die Hostadresse
     * @return @code{true}, wenn die Verbindung erfolgreich hergestellt wurde. Ansonsten @code{false}
     */
    boolean connect (String host);

    void shutdown ();

}
