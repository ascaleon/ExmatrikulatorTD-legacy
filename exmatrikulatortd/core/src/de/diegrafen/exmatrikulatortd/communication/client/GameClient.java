package de.diegrafen.exmatrikulatortd.communication.client;

import com.esotericsoftware.kryonet.Client;
import de.diegrafen.exmatrikulatortd.controller.gamelogic.LogicController;
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

    private boolean connected;

    private LogicController logicController;

    /**
     * Erzeugt einen neuen GameClient
     */
    public GameClient () {
        client = new Client();
        registerObjects(client.getKryo());
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
     * @return @code{true}, wenn die Verbindung erfolgreich hergestellt wurde. Ansonsten @code{false}
     */
    public boolean connect (String host) {
        //client.connect(5000, host, TCP_PORT, UDP_PORT);
        return false;
    }

    @Override
    public void shutdown() {
        client.close();
    }

    public void attachResponseListeners (LogicController logicController) {

    }

    public void attachBuildResponseListener (LogicController logicController) {

    }

    private void attachSellResponseListener (LogicController logicController) {

    }

    private void attachSendEnemyResponseListener (LogicController logicController) {

    }

    private void attachUpgradeResponseListener (LogicController logicController) {

    }

    private void attachGetServerStateResponseListener (LogicController logicController) {

    }

    public boolean isConnected() {
        return connected;
    }
}
