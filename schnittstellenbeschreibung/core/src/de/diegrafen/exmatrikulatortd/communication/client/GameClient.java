package de.diegrafen.exmatrikulatortd.communication.client;

import com.esotericsoftware.kryonet.Client;
import de.diegrafen.exmatrikulatortd.controller.LogicController;
import de.diegrafen.exmatrikulatortd.communication.Connector;
import de.diegrafen.exmatrikulatortd.model.Coordinates;
import de.diegrafen.exmatrikulatortd.model.Gamestate;
import de.diegrafen.exmatrikulatortd.model.enemy.Enemy;
import de.diegrafen.exmatrikulatortd.model.tower.Tower;

/**
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 15.06.2019 13:45
 */
public class GameClient extends Connector implements ClientInterface {

    private Client client;

    /**
     * Erzeugt einen neuen GameClient
     */
    public GameClient () {
        client = new Client();
    }

    @Override
    public boolean buildTower(Tower tower, Coordinates coordinates) {
        return false;
    }

    @Override
    public boolean sellTower(Tower tower) {
        return false;
    }

    @Override
    public boolean upgradeTower(Tower tower) {
        return false;
    }

    @Override
    public boolean sendEnemy(Enemy enemy) {
        return false;
    }

    @Override
    public Gamestate refreshLocalGameState() {
        return null;
    }

    /**
     * Stellt die Verbindung
     * @param host Die Hostadresse
     * @param tcpPort Die Nummer des TCP-Ports
     * @param udpPort Die Nummer des UDP-Ports
     * @return @code{true}, wenn die Verbindung erfolgreich hergestellt wurde. Ansonsten @code{false}
     */
    public boolean connect (String host, int tcpPort, int udpPort) {
        //client.connect(5000, host, tcpPort, udpPort);
        return false;
    }

    public void attachBuildResponseListener (LogicController logicController) {

    }

    public void attachSellResponseListener (LogicController logicController) {

    }

    public void attachSendEnemyResponseListener (LogicController logicController) {

    }

    public void attachUpgradeResponseListener (LogicController logicController) {

    }

    public void attachGetServerStateResponseListener (LogicController logicController) {

    }
}
