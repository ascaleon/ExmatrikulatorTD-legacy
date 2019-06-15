package de.diegrafen.exmatrikulatortd.communication.client;

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

    boolean buildTower (Tower tower, Coordinates coordinates);

    boolean sellTower (Tower tower);

    boolean upgradeTower (Tower tower);

    boolean sendEnemy (Enemy enemy);

    Gamestate refreshLocalGameState ();

    /**
     * Stellt die Verbindung zu einem Server her.
     * @param host Die Hostadresse
     * @return @code{true}, wenn die Verbindung erfolgreich hergestellt wurde. Ansonsten @code{false}
     */
    boolean connect (String host);

    void shutdown ();

    void attachBuildResponseListener (LogicController logicController);

    void attachSellResponseListener (LogicController logicController);

    void attachSendEnemyResponseListener (LogicController logicController);

    void attachUpgradeResponseListener (LogicController logicController);

    void attachGetServerStateResponseListener (LogicController logicController);

}
