package de.diegrafen.exmatrikulatortd.communication.client;

import com.esotericsoftware.kryonet.Client;
import de.diegrafen.exmatrikulatortd.communication.client.requests.*;
import de.diegrafen.exmatrikulatortd.controller.gamelogic.LogicController;
import de.diegrafen.exmatrikulatortd.communication.Connector;
import de.diegrafen.exmatrikulatortd.model.Coordinates;
import de.diegrafen.exmatrikulatortd.model.Gamestate;
import de.diegrafen.exmatrikulatortd.model.enemy.Enemy;
import de.diegrafen.exmatrikulatortd.model.tower.Tower;

import java.io.IOException;

/**
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 15.06.2019 13:45
 */
public class GameClient extends Connector implements ClientInterface {

    private Client client;

    private int tcpPort;    // uninitialisiert

    private int udpPort;    // uninitialisiert

    private boolean connected;

    private LogicController logicController;

    /**
     * Erzeugt einen neuen GameClient
     */
    public GameClient() {
        client = new Client();
        registerObjects(client.getKryo());
    }

    public void sendRequest(Request request) {
        client.sendTCP(request);
    }

    @Override
    public boolean buildTower(Tower tower, Coordinates coordinates) {
        BuildRequest buildRequest = new BuildRequest(tower, coordinates);
        sendRequest(buildRequest);
        return false;
    }

    @Override
    public boolean sellTower(Tower tower) {
        SellRequest sellRequest = new SellRequest(tower);
        sendRequest(sellRequest);
        return false;
    }

    @Override
    public boolean upgradeTower(Tower tower) {
        UpgradeRequest upgradeRequest = new UpgradeRequest(tower);
        sendRequest(upgradeRequest);
        return false;
    }

    @Override
    public boolean sendEnemy(Enemy enemy) {
        SendEnemyRequest sendEnemyRequest = new SendEnemyRequest(enemy);
        sendRequest(sendEnemyRequest);
        return false;
    }

    @Override
    public Gamestate refreshLocalGameState() {
        GetServerStateRequest getServerStateRequest = new GetServerStateRequest();
        sendRequest(getServerStateRequest);
        return null;
    }

    /**
     * Stellt die Verbindung
     *
     * @param host Die Hostadresse
     * @return @code{true}, wenn die Verbindung erfolgreich hergestellt wurde. Ansonsten @code{false}
     */
    public boolean connect(String host) {
        try{
            client.connect(5000, host, tcpPort, udpPort);
            if(client.isConnected()) connected=true;
            return true;
        } catch (final IOException e){
            e.printStackTrace();    // nur zum Debugging!
            connected=false;
            return false;
        }
    }

    @Override
    public void shutdown() {
        client.close();
        connected=false;
    }

    public void attachResponseListeners(LogicController logicController) {

    }

    public void attachBuildResponseListener(LogicController logicController) {

    }

    private void attachSellResponseListener(LogicController logicController) {

    }

    private void attachSendEnemyResponseListener(LogicController logicController) {

    }

    private void attachUpgradeResponseListener(LogicController logicController) {

    }

    private void attachGetServerStateResponseListener(LogicController logicController) {

    }

    public boolean isConnected() {
        return connected;
    }
}
