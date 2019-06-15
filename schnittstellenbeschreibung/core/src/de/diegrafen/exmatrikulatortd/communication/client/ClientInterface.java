package de.diegrafen.exmatrikulatortd.communication.client;

import com.esotericsoftware.kryonet.Client;
import de.diegrafen.exmatrikulatortd.communication.client.requests.Request;
import de.diegrafen.exmatrikulatortd.communication.server.responses.Response;
import de.diegrafen.exmatrikulatortd.controller.LogicController;
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
     * @param tcpPort Die Nummer des TCP-Ports
     * @param udpPort Die Nummer des UDP-Ports
     * @return @code{true}, wenn die Verbindung erfolgreich hergestellt wurde. Ansonsten @code{false}
     */
    boolean connect (String host, int tcpPort, int udpPort);

    void attachBuildResponseListener (LogicController logicController);

    void attachSellResponseListener (LogicController logicController);

    void attachSendEnemyResponseListener (LogicController logicController);

    void attachUpgradeResponseListener (LogicController logicController);

    void attachGetServerStateResponseListener (LogicController logicController);

}
