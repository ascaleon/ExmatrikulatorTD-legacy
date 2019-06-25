package de.diegrafen.exmatrikulatortd.communication.client;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import de.diegrafen.exmatrikulatortd.communication.client.requests.BuildRequest;
import de.diegrafen.exmatrikulatortd.communication.server.responses.BuildResponse;
import de.diegrafen.exmatrikulatortd.controller.factories.EnemyFactory;
import de.diegrafen.exmatrikulatortd.controller.factories.TowerFactory;
import de.diegrafen.exmatrikulatortd.communication.client.requests.*;
import de.diegrafen.exmatrikulatortd.controller.gamelogic.LogicController;
import de.diegrafen.exmatrikulatortd.communication.Connector;
import de.diegrafen.exmatrikulatortd.model.Gamestate;

import java.io.IOException;

import static de.diegrafen.exmatrikulatortd.util.Constants.TCP_PORT;
import static de.diegrafen.exmatrikulatortd.util.Constants.UDP_PORT;

/**
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 15.06.2019 13:45
 */
public class GameClient extends Connector implements ClientInterface {

    private Client client;

    private int tcpPort;

    private int udpPort;

    private boolean connected;

    private LogicController logicController;

    /**
     * Erzeugt einen neuen GameClient
     */
    public GameClient() {
        client = new Client();
        registerObjects(client.getKryo());
        tcpPort = TCP_PORT;
        udpPort = UDP_PORT;
    }

    private void sendRequest(Request request) {
        client.sendTCP(request);
    }

    @Override
    public void buildTower(TowerFactory.TowerType towerType, int xPosition, int yPosition, int playerNumber) {
        sendRequest(new BuildRequest(towerType, xPosition, yPosition, playerNumber));
    }

    /**
     * Verkauft einen Turm
     *
     * @param xPosition
     * @param yPosition
     * @param playerNumber
     * @return Wenn das Verkaufen erfolgreich war, true, ansonsten false
     */
    @Override
    public void sellTower(int xPosition, int yPosition, int playerNumber) {
        SellRequest sellRequest = new SellRequest(xPosition, yPosition, playerNumber);
        sendRequest(sellRequest);
    }

    /**
     * Rüstet einen Turm auf
     *
     * @param xPosition
     * @param yPosition
     * @param playerNumber
     * @return Wenn das Aufrüsten erfolgreich war, true, ansonsten false
     */
    @Override
    public void upgradeTower (int xPosition, int yPosition, int playerNumber) {
        UpgradeRequest upgradeRequest = new UpgradeRequest(xPosition, yPosition, playerNumber);
        sendRequest(upgradeRequest);
    }

    /**
     * Schickt einen Gegner zum gegnerischen Spieler
     *
     * @param enemyType@return Wenn das Schicken erfolgreich war, true, ansonsten false
     */
    @Override
    public void sendEnemy(EnemyFactory.EnemyType enemyType) {
        SendEnemyRequest sendEnemyRequest = new SendEnemyRequest(enemyType);
        sendRequest(sendEnemyRequest);
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

    public void attachResponseListeners (LogicController logicController) {
        attachBuildResponseListener(logicController);
    }

    private void attachBuildResponseListener (LogicController logicController) {
        client.addListener(new Listener() {
            public void received(Connection connection, Object object) {
                if (object instanceof BuildResponse) {
                    BuildResponse response = (BuildResponse) object;

                    if (response.wasSuccessful()) {
                        logicController.buildTower(response.getTowerType(), response.getxPosition(), response.getyPosition(), response.getPlayerNumber());
                    } else {
                        logicController.buildFailed();
                    }
                }
            }
        });
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
