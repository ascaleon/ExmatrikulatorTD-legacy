package de.diegrafen.exmatrikulatortd.communication.client;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import de.diegrafen.exmatrikulatortd.communication.client.requests.BuildRequest;
import de.diegrafen.exmatrikulatortd.communication.server.responses.*;
import de.diegrafen.exmatrikulatortd.controller.factories.EnemyFactory;
import de.diegrafen.exmatrikulatortd.controller.factories.TowerFactory;
import de.diegrafen.exmatrikulatortd.communication.client.requests.*;
import de.diegrafen.exmatrikulatortd.controller.gamelogic.LogicController;
import de.diegrafen.exmatrikulatortd.communication.Connector;
import de.diegrafen.exmatrikulatortd.model.Gamestate;
import de.diegrafen.exmatrikulatortd.model.tower.Tower;

import java.io.IOException;

import static de.diegrafen.exmatrikulatortd.util.Constants.TCP_PORT;
import static de.diegrafen.exmatrikulatortd.util.Constants.UDP_PORT;

/**
 *
 * Diese Klasse realisiert die Netzwerkkommunikation für Client-Instanzen. Über die Implementierung der
 * Schnittstellen ConnectorInterface bzw. ClientInterface kann ein LogicController Spielzüge an
 * den Server senden. Antworten bzw. Nachrichten werden vom GameClient sodann an den LogicController
 * zurückgegeben, um den lokalen Spielzustand an den des Servers anzugleichen.
 *
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @author Nick Michalek <michalek@uni-bremen.de>
 * @version 25.06.2019 23:24
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

    /**
     * Sendet eine Anfrage an den Server
     *
     * @param request Die zu sendende Anfrage
     */
    private void sendRequest(Request request) {
        client.sendTCP(request);
    }

    /**
     *
     * @param towerType    Der Typ des zu bauenden Turms
     * @param xCoordinate  Die x-Koordinate der Stelle, an der der Turm gebaut werden soll
     * @param yCoordinate  Die y-Koordinate der Stelle, an der der Turm gebaut werden soll
     * @param playerNumber Die Nummer der Spielerin, die den Turm bauen will
     */
    @Override
    public void buildTower(int towerType, int xCoordinate, int yCoordinate, int playerNumber) {
        BuildRequest sellRequest = new BuildRequest(towerType, xCoordinate, yCoordinate, playerNumber);
        sendRequest(sellRequest);
    }

    /**
     * Verkauft einen Turm
     *
     * @param xCoordinate  Die x-Koordinate des Turms
     * @param yCoordinate  Die y-Koordinate des Turms
     * @param playerNumber Die Nummer der Spielerin, der der Turm gehört
     */
    @Override
    public void sellTower(int xCoordinate, int yCoordinate, int playerNumber) {
        SellRequest sellRequest = new SellRequest(xCoordinate, yCoordinate, playerNumber);
        sendRequest(sellRequest);
    }

    /**
     *
     * @param xCoordinate  Die x-Koordinate des Turms
     * @param yCoordinate  Die y-Koordinate des Turms
     * @param playerNumber Die Nummer der Spielerin, der der Turm gehört
     */
    @Override
    public void upgradeTower(int xCoordinate, int yCoordinate, int playerNumber) {
        UpgradeRequest upgradeRequest = new UpgradeRequest(xCoordinate, yCoordinate, playerNumber);
        sendRequest(upgradeRequest);
    }

    /**
     *
     * @param enemyType Der Typ des zu schickenden Gegners
     */
    @Override
    public void sendEnemy(int enemyType) {
        SendEnemyRequest sendEnemyRequest = new SendEnemyRequest(enemyType);
        sendRequest(sendEnemyRequest);
    }

    /**
     * Fragt eine aktuelle Kopie des Server-Spielzustandes an
     */
    @Override
    public void refreshLocalGameState() {
        sendRequest(new GetServerStateRequest());
    }

    /**
     * Stellt die Verbindung zum Server her
     *
     * @param host Die Hostadresse des Servers
     * @return @code{true}, wenn die Verbindung erfolgreich hergestellt wurde. Ansonsten @code{false}
     */
    public boolean connect(final String host) {
        try {
            client.connect(5000, host, tcpPort, udpPort);
            if (client.isConnected()) {
                connected = true;
            }
            return true;
        } catch (final IOException e) {
            e.printStackTrace();    // nur zum Debugging!
            connected = false;
        }

        return connected;
    }

    /**
     * Schließt die Verbindung zum Server und beendet den Client.
     */
    @Override
    public void shutdown() {
        client.close();
        connected = false;
    }

    /**
     *
     * Fügt eine Reihe von Listenern zum Client hinzu, die darauf warten, dass eine Nachricht vom Server eintrifft
     * und eine entsprechende Aktion beim zugewiesenen LogicController auslösen
     *
     * @param logicController Der LogicController, an den empfangene Antworten weitergeleitet werden
     */
    public void attachResponseListeners(final LogicController logicController) {
        attachBuildResponseListener(logicController);
        attachGetServerStateResponseListener(logicController);
        attachSellResponseListener(logicController);
        attachSendEnemyResponseListener(logicController);
        attachUpgradeResponseListener(logicController);
    }

    /**
     *
     * @param logicController Der LogicController, an den die empfangene Antwort weitergeleitet wird
     */
    private void attachBuildResponseListener(final LogicController logicController) {
        client.addListener(new Listener() {
            public void received(Connection connection, Object object) {
                if (object instanceof BuildResponse) {
                    final BuildResponse response = (BuildResponse) object;

                    if (response.wasSuccessful()) {
                        logicController.buildTower(response.getTowerType(), response.getxCoordinate(), response.getyCoordinate(), response.getPlayerNumber());
                    } else {
                        logicController.buildFailed();
                    }
                }
            }
        });
    }

    /**
     *
     * @param logicController Der LogicController, an den die empfangene Antwort weitergeleitet wird
     */
    private void attachSellResponseListener(final LogicController logicController) {
        client.addListener(new Listener() {
            public void received(Connection connection, Object object) {
                if (object instanceof SellResponse) {
                    final SellResponse response = (SellResponse) object;

                    if (response.wasSuccessful()) {
                        logicController.sellTower(response.getxCoordinate(),response.getyCoordinate(),response.getPlayerNumber());
                    } //else {
                        //logicController.sellFailed() ?
                    //}
                }
            }
        });
    }

    /**
     *
     * @param logicController Der LogicController, an den die empfangene Antwort weitergeleitet wird
     */
    private void attachSendEnemyResponseListener(final LogicController logicController) {
        client.addListener(new Listener() {
            public void received(Connection connection, Object object) {
                if (object instanceof SendEnemyResponse) {
                    final SendEnemyResponse response = (SendEnemyResponse) object;

                    if(response.wasSuccessful()) {
                        logicController.sendEnemy(response.getEnemyType());
                    } else{
                        logicController.sendFailed();
                    }
                }
            }
        });
    }

    /**
     *
     * @param logicController Der LogicController, an den die empfangene Antwort weitergeleitet wird
     */
    private void attachUpgradeResponseListener(final LogicController logicController) {
        client.addListener(new Listener() {
            public void received(Connection connection, Object object) {
                if (object instanceof UpgradeResponse) {
                    final UpgradeResponse response = (UpgradeResponse) object;

                    if (response.wasSuccessful()) {
                        logicController.upgradeTower(response.getxCoordinate(), response.getyCoordinate(), response.getPlayerNumber());
                    } else {
                        logicController.upgradeFailed();
                    }
                }
            }
        });
    }

    /**
     *
     * @param logicController Der LogicController, an den die empfangene Antwort weitergeleitet wird
     */
    private void attachGetServerStateResponseListener(final LogicController logicController) {
        client.addListener(new Listener() {
            public void received(Connection connection, Object object) {
                if (object instanceof GetServerStateResponse) {
                    GetServerStateResponse response = (GetServerStateResponse) object;

                    if (response.getGamestate() != null) {
                        logicController.setGamestate(response.getGamestate());
                    }
                }
            }
        });
    }

    /**
     * Gibt an, ob der GameClient mit einem Server verbunden ist
     * @return true, wenn der Client verbunden ist, ansonsten false.
     */
    public boolean isConnected() {
        return connected;
    }
}