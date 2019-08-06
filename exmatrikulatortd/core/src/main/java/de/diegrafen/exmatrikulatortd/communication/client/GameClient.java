package de.diegrafen.exmatrikulatortd.communication.client;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.ClientDiscoveryHandler;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import de.diegrafen.exmatrikulatortd.communication.server.responses.*;
import de.diegrafen.exmatrikulatortd.controller.MainController;
import de.diegrafen.exmatrikulatortd.communication.client.requests.*;
import de.diegrafen.exmatrikulatortd.controller.gamelogic.ClientLogicController;
import de.diegrafen.exmatrikulatortd.communication.Connector;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.LinkedList;
import java.util.List;

import static de.diegrafen.exmatrikulatortd.controller.factories.NewGameFactory.MULTIPLAYER_DUEL;
import static de.diegrafen.exmatrikulatortd.util.Constants.TCP_PORT;
import static de.diegrafen.exmatrikulatortd.util.Constants.UDP_PORT;

/**
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

    private final Client client;

    private final int tcpPort;

    private final int udpPort;

    private boolean connected;

    private List<String> receivedSessionInfo;

    private int localPlayerNumber;

    private MainController mainController;

    private boolean gameRunning = false;

    private String mapPath;

    /**
     * Erzeugt einen neuen GameClient
     */
    public GameClient() {
        client = new Client();
        registerObjects(client.getKryo());
        tcpPort = TCP_PORT;
        udpPort = UDP_PORT;
        this.receivedSessionInfo = new LinkedList<>();
        System.out.println("Client created!");
        client.setDiscoveryHandler(new ClientDiscoveryHandler() {

            @Override
            public DatagramPacket onRequestNewDatagramPacket() {
                return new DatagramPacket(new byte[48], 48);
            }

            @Override
            public void onDiscoveredHost(DatagramPacket datagramPacket) {
                String serverInformation = "";
                serverInformation += datagramPacket.getAddress().getHostAddress() + "\n";
                //System.out.println("Ohai.");
                String packageInfo = new String(datagramPacket.getData(), 0, datagramPacket.getLength());
                //System.out.println(packageInfo);
                serverInformation += packageInfo;
                receivedSessionInfo.add(serverInformation);
            }
        });
        attachGetGameInfoReponseListener();
        attachGameReadyResponseListener();
        client.start(); // Startet den Client in einem neuen Thread
    }

    /**
     * Sendet eine Anfrage an den Server
     *
     * @param request Die zu sendende Anfrage
     */
    private void sendRequest(Request request) {
        int bytesSent = client.sendTCP(request);
        System.out.println("Bytes sent: " + bytesSent);

    }

    /**
     * Gibt eine Liste von InetAddresses zurueck, die entdeckte Server im lokalen Netzwerk enthaelt.
     *
     * @return @code{List<InetAddress>} mit gefundenen Servern
     */
    public List<InetAddress> discoverLocalServers() {
        return client.discoverHosts(udpPort, 5000);
    }

    /**
     * @param towerType    Der Typ des zu bauenden Turms
     * @param xCoordinate  Die x-Koordinate der Stelle, an der der Turm gebaut werden soll
     * @param yCoordinate  Die y-Koordinate der Stelle, an der der Turm gebaut werden soll
     * @param playerNumber Die Nummer der Spielerin, die den Turm bauen will
     */
    @Override
    public void buildTower(int towerType, int xCoordinate, int yCoordinate, int playerNumber) {
        BuildRequest buildRequest = new BuildRequest(towerType, xCoordinate, yCoordinate, playerNumber);
        sendRequest(buildRequest);
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
     * Schickt einen Gegner zum gegnerischen Spieler
     *
     * @param enemyType      Der Typ des zu schickenden Gegners
     */
    @Override
    public void sendEnemy(int enemyType, int playerToSendTo, int sendingPlayer) {
        SendEnemyRequest sendEnemyRequest = new SendEnemyRequest(enemyType, playerToSendTo, sendingPlayer);
        sendRequest(sendEnemyRequest);
    }

    /**
     * Fragt eine aktuelle Kopie des Server-Spielzustandes an
     */
    @Override
    public void refreshLocalGameState() {
        sendRequest(new GetServerStateRequest());
    }

    @Override
    public void reportReadiness() {
        System.out.println("Ich bin bereit!");
        sendRequest(new ClientReadyRequest());
    }

    /**
     * Stellt die Verbindung zum Server her
     *
     * @param host Die Hostadresse des Servers
     * @return @code{true}, wenn die Verbindung erfolgreich hergestellt wurde. Ansonsten @code{false}
     */
    public boolean connect(final String host) {
        connected = false;
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

    @Override
    public void reportFinishedLoading() {
        System.out.println("Ich habe fertig geladen!");
        sendRequest(new FinishedLoadingRequest());
    }

    /**
     * Fügt eine Reihe von Listenern zum Client hinzu, die darauf warten, dass eine Nachricht vom Server eintrifft
     * und eine entsprechende Aktion beim zugewiesenen LogicController auslösen
     *
     * @param logicController Der LogicController, an den empfangene Antworten weitergeleitet werden
     */
    public void attachResponseListeners(final ClientLogicController logicController) {

        attachBuildResponseListener(logicController);
        attachGetServerStateResponseListener(logicController);
        attachSellResponseListener(logicController);
        attachSendEnemyResponseListener(logicController);
        attachUpgradeResponseListener(logicController);
        attachStartGameReponseListener(logicController);
        attachErrorResponseListener(logicController);
    }

    private void attachErrorResponseListener(ClientLogicController logicController) {
        client.addListener(new Listener() {
            @Override
            public void received(Connection connection, Object object) {
                if (object instanceof ErrorResponse) {
                    ErrorResponse response = (ErrorResponse) object;
                    logicController.displayErrorMessage(response.getErrorMessage(), response.getPlayerNumber());
                }
            }

            @Override
            public void disconnected(Connection connection) {
                if (gameRunning) {
                    // Todo: Statt exitGame() aufzurufen, das Anzeigen eines passenden Fensters triggern
                    System.err.println("Der Server hat die Verbindung geschlossen!");
                    Gdx.app.postRunnable(logicController::gameConnectionLost);
                    gameRunning = false;
                    mainController.shutdownConnections();
                }
            }
        });
    }

    private void attachGameReadyResponseListener() {
        client.addListener(new Listener() {
            @Override
            public void received(Connection connection, Object object) {
                if (object instanceof AllPlayersReadyResponse) {
                    //mainController.showLoadScreen();
                    Gdx.app.postRunnable(() -> mainController.createNewMultiplayerClientGame(2, localPlayerNumber, MULTIPLAYER_DUEL, mapPath));
                }
            }
        });
    }

    private void attachStartGameReponseListener(ClientLogicController logicController) {
        client.addListener(new Listener() {
            @Override
            public void received(Connection connection, Object object) {
                if (object instanceof StartGameResponse) {
                    if (!gameRunning) {
                        gameRunning = true;
                        System.err.println("Spiel läuft!");
                        Gdx.app.postRunnable(() -> mainController.showScreen(logicController.getGameScreen()));
                    }
                }
            }
        });
    }

    /**
     * @param logicController Der LogicController, an den die empfangene Antwort weitergeleitet wird
     */
    private void attachBuildResponseListener(final ClientLogicController logicController) {
        client.addListener(new Listener() {
            public void received(Connection connection, Object object) {
                if (object instanceof BuildResponse) {
                    final BuildResponse response = (BuildResponse) object;

                    Gdx.app.postRunnable(() -> logicController.addTowerFromServer(response.getTowerType(),
                            response.getxCoordinate(), response.getyCoordinate(), response.getPlayerNumber()));

                    System.out.println("Response received!");
                }
            }
        });
    }

    /**
     * @param logicController Der LogicController, an den die empfangene Antwort weitergeleitet wird
     */
    private void attachSellResponseListener(final ClientLogicController logicController) {
        client.addListener(new Listener() {
            public void received(Connection connection, Object object) {
                if (object instanceof SellResponse) {
                    final SellResponse response = (SellResponse) object;
                    Gdx.app.postRunnable(() -> logicController.sellTowerFromServer(response.getxCoordinate(), response.getyCoordinate(), response.getPlayerNumber()));
                }
            }
        });
    }

    /**
     * @param logicController Der LogicController, an den die empfangene Antwort weitergeleitet wird
     */
    private void attachSendEnemyResponseListener(final ClientLogicController logicController) {
        client.addListener(new Listener() {
            public void received(Connection connection, Object object) {
                if (object instanceof SendEnemyResponse) {
                    final SendEnemyResponse response = (SendEnemyResponse) object;
                    Gdx.app.postRunnable(() -> logicController.sendEnemyFromServer(response.getEnemyType(), response.getPlayerToSendTo(), response.getSendingPlayer()));

                }
            }
        });
    }

    /**
     * @param logicController Der LogicController, an den die empfangene Antwort weitergeleitet wird
     */
    private void attachUpgradeResponseListener(final ClientLogicController logicController) {
        client.addListener(new Listener() {
            public void received(Connection connection, Object object) {
                if (object instanceof UpgradeResponse) {
                    final UpgradeResponse response = (UpgradeResponse) object;
                    Gdx.app.postRunnable(() -> logicController.upgradeTowerFromServer(response.getxCoordinate(), response.getyCoordinate(), response.getPlayerNumber()));

                }
            }
        });
    }

    /**
     * @param logicController Der LogicController, an den die empfangene Antwort weitergeleitet wird
     */
    private void attachGetServerStateResponseListener(final ClientLogicController logicController) {
        client.addListener(new Listener() {
            public void received(Connection connection, Object object) {
                if (object instanceof GetServerStateResponse) {
                    GetServerStateResponse response = (GetServerStateResponse) object;
                    logicController.setGamestate(response.getGamestate());
                }
            }
        });
    }

    private void attachGetGameInfoReponseListener() {
        client.addListener(new Listener() {
            @Override
            public void received(Connection connection, Object object) {
                if (object instanceof GetGameInfoResponse) {
                    GetGameInfoResponse response = (GetGameInfoResponse) object;
                    //if (response.isUpdate()) {
                        // Update-Code kommt hierhin
                    //} else {
                    if (!response.isUpdate()) {
                        localPlayerNumber = response.getAllocatedPlayerNumber();
                        mapPath = response.getMapPath();
                    }
                }
            }
        });
    }

    public List<String> getReceivedSessionInfo() {
        List<String> sessionInfo = new LinkedList<>(receivedSessionInfo);
        receivedSessionInfo = new LinkedList<>();
        return sessionInfo;
    }

    public MainController getMainController() {
        return mainController;
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }
}
