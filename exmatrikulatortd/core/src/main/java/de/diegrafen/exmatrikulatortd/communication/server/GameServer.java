package de.diegrafen.exmatrikulatortd.communication.server;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.kryonet.ServerDiscoveryHandler;
import de.diegrafen.exmatrikulatortd.communication.client.requests.*;
import de.diegrafen.exmatrikulatortd.communication.server.responses.*;
import de.diegrafen.exmatrikulatortd.controller.MainController;
import de.diegrafen.exmatrikulatortd.controller.factories.EnemyFactory;
import de.diegrafen.exmatrikulatortd.controller.factories.TowerFactory;
import de.diegrafen.exmatrikulatortd.controller.gamelogic.LogicController;
import de.diegrafen.exmatrikulatortd.communication.Connector;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import static de.diegrafen.exmatrikulatortd.controller.factories.NewGameFactory.MULTIPLAYER_DUEL;
import static de.diegrafen.exmatrikulatortd.util.Constants.TCP_PORT;
import static de.diegrafen.exmatrikulatortd.util.Constants.UDP_PORT;

/**
 * GameServer-Klasse
 *
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 15.06.2019 14:03
 */
public class GameServer extends Connector {

    /**
     * Der Port, über den TCP-Verbindungen entgegengenommen werden
     */
    private int tcpPort;

    /**
     * Der Port, über den UDP-Verbindungen entgegengenommen werden
     */
    private int udpPort;

    /**
     * Der Server, über den die Kommunikation abläuft
     */
    private Server server;

    private MainController mainController;

    /**
     * Der Logik-Controller, mit dem der GameServer interagiert
     */
    private LogicController logicController;

    /**
     * Gibt an, ob der GameServer verbunden ist
     */
    private boolean connected;

    private boolean lookingForPlayers = true;

    private boolean gameRunning = false;

    private int numberOfPlayers;

    private boolean[] slotsFilled;

    private HashMap<Integer, Integer> connectionAndPlayerNumbers = new HashMap<>();

    /**
     * Erzeugt einen neuen GameServer
     */
    public GameServer() {
        this.tcpPort = TCP_PORT;
        this.udpPort = UDP_PORT;
        this.server = new Server();
        this.connected = false;
        registerObjects(server.getKryo());
        attachGetGameInfoRequestListener();
        System.out.println("Server created!");
    }


    /**
     * Startet den GameServer
     *
     * @return true, wenn das Starten erfolgreich war, ansonsten false
     */
    public boolean startServer(int numberOfPlayers) {
        try {
            server.bind(tcpPort, udpPort);
            System.out.println("Server started!");
            this.numberOfPlayers = numberOfPlayers;
            slotsFilled = new boolean[numberOfPlayers];
            slotsFilled[0] = true;
            server.setDiscoveryHandler(new ServerDiscoveryHandler() {
                @Override
                public boolean onDiscoverHost(DatagramChannel datagramChannel, InetSocketAddress fromAddress) throws IOException {

                    String mapName = "map1";

                    String numberOfPlayers = Integer.toString(2);

                    if (openSlotsLeft() > 0) {
                        lookingForPlayers = true;
                        String newData = mapName + "\n" + numberOfPlayers;

                        ByteBuffer buf = ByteBuffer.allocate(48);
                        buf.clear();
                        buf.put(newData.getBytes());
                        buf.flip();

                        int bytesSent = datagramChannel.send(buf, fromAddress);
                        return bytesSent > 0;
                    } else {
                        return false;
                    }
                }
            });
            server.start();
        } catch (final java.io.IOException e) {
            e.printStackTrace();
            return false;
        }

        return connected = true;
    }

    private int openSlotsLeft() {
        int openSlots = 0;

        for (int i = 0; i < slotsFilled.length; i++) {
            if (slotsFilled[i]) {
                openSlots++;
            }
        }

        return openSlots;
    }

    /**
     * Beendet den Server
     */
    @Override
    public void shutdown() {
        server.close();
    }

    /**
     * Fügt RequestListeners hinzu und assoziiert ihn mit einem LogicController
     *
     * @param logicController Der zu assoziierende LogicController
     */
    public void attachRequestListeners(final LogicController logicController) {
        attachBuildRequestListener(logicController);
        attachSellRequestListener(logicController);
        attachSendEnemyRequestListener(logicController);
        attachUpgradeRequestListener(logicController);
        attachGetServerStateRequestListener(logicController);
    }

    /**
     * Fügt einen Request-Listener für das Bauen eines Turms hinzu und assoziiert ihn mit einem LogicController
     *
     * @param logicController Der zu assoziierende LogicController
     */
    private void attachBuildRequestListener(final LogicController logicController) {
        server.addListener(new Listener() {
            public void received(Connection connection, Object object) {
                if (object instanceof BuildRequest) {
                    final BuildRequest request = (BuildRequest) object;
                    final boolean successful = logicController.buildTower(request.getTowerType(), request.getxCoordinate(), request.getyCoordinate(), request.getPlayerNumber());

                    if (successful) {
                        server.sendToAllTCP(new BuildResponse(successful, request.getTowerType(), request.getxCoordinate(), request.getyCoordinate(), request.getPlayerNumber()));
                    } else {
                        connection.sendTCP(new BuildResponse(successful));
                    }
                }
            }
        });
    }

    /**
     * Fügt einen Request-Listener für das Verkaufen eines Turms hinzu und assoziiert ihn mit einem LogicController
     *
     * @param logicController Der zu assoziierende LogicController
     */
    private void attachSellRequestListener(final LogicController logicController) {
        server.addListener(new Listener() {
            public void received(Connection connection, Object object) {
                if (object instanceof SellRequest) {
                    final SellRequest request = (SellRequest) object;
                    final boolean successful = logicController.sellTower(request.getxCoordinate(), request.getyCoordinate(), request.getPlayerNumber());

                    if (successful) {
                        server.sendToAllTCP(new SellResponse(successful, request.getxCoordinate(), request.getyCoordinate(), request.getPlayerNumber()));
                    } else {
                        connection.sendTCP(new SellResponse(successful));
                    }
                }
            }
        });
    }

    /**
     * Fügt einen Request-Listener für das Senden eines Gegners hinzu und assoziiert ihn mit einem LogicController
     *
     * @param logicController Der zu assoziierende LogicController
     */
    private void attachSendEnemyRequestListener(final LogicController logicController) {
        server.addListener(new Listener() {
            public void received(Connection connection, Object object) {
                if (object instanceof SendEnemyRequest) {
                    final SendEnemyRequest request = (SendEnemyRequest) object;
                    final boolean successful = logicController.sendEnemy(request.getEnemyType(), request.getPlayerToSendTo(), request.getSendingPlayer());

                    if (successful) {
                        server.sendToAllTCP(new SendEnemyResponse(successful, request.getEnemyType(), request.getPlayerToSendTo(), request.getSendingPlayer()));
                    } else {
                        connection.sendTCP(new SendEnemyResponse(successful));
                    }
                }
            }
        });
    }

    /**
     * Fügt einen Request-Listener für das Aufrüsten eines Turms hinzu und assoziiert ihn mit einem LogicController
     *
     * @param logicController Der zu assoziierende LogicController
     */
    private void attachUpgradeRequestListener(final LogicController logicController) {
        server.addListener(new Listener() {
            public void received(Connection connection, Object object) {
                if (object instanceof UpgradeRequest) {
                    final UpgradeRequest request = (UpgradeRequest) object;
                    final boolean successful = logicController.upgradeTower(request.getxCoordinate(), request.getyCoordinate(), request.getPlayerNumber());

                    if (successful) {
                        server.sendToAllTCP(new UpgradeResponse(successful, request.getxCoordinate(), request.getyCoordinate(), request.getPlayerNumber()));
                    } else {
                        connection.sendTCP(new UpgradeResponse(successful));
                    }
                }
            }
        });
    }

    /**
     * Fügt einen Request-Listener für das Abrufen des Server-Spielzustandes hinzu und assoziiert ihn mit einem LogicController
     *
     * @param logicController Der zu assoziierende LogicController
     */
    private void attachGetServerStateRequestListener(final LogicController logicController) {
        server.addListener(new Listener() {
            public void received(Connection connection, Object object) {
                if (object instanceof GetServerStateRequest) {
                    connection.sendTCP(logicController.getGamestate());
                }
            }
        });
    }

    private void attachGetGameInfoRequestListener() {
        server.addListener(new Listener() {
            List<String> playerNames = new LinkedList<>();
            List<String> playerProfilePicturePaths = new LinkedList<>();
            @Override
            public void connected(Connection connection) {

                int playerNumber = allocatePlayerNumber();

                connectionAndPlayerNumbers.put(connection.getID(), playerNumber);

                if (openSlotsLeft() <= 0) {
                    lookingForPlayers = false;
                }

                GetGameInfoResponse getGameInfoResponse = new GetGameInfoResponse(true, playerNumber, playerNames, playerProfilePicturePaths);
                server.sendToAllExceptTCP(connection.getID(), getGameInfoResponse);
                getGameInfoResponse.setUpdate(false);
                connection.sendTCP(getGameInfoResponse);
                if (!lookingForPlayers) {
                    Gdx.app.postRunnable(() -> mainController.createNewMultiplayerServerGame(numberOfPlayers, 0, MULTIPLAYER_DUEL));
                }
            }

            @Override
            public void disconnected(Connection connection) {
                if (!gameRunning) {
                    slotsFilled[connectionAndPlayerNumbers.get(connection.getID())] = false;
                    connectionAndPlayerNumbers.remove(connection.getID());
                    lookingForPlayers = true;
                } else {
                    // TODO: Hier irgendwie das Triggern einer "Player left"-Message einbauen
                }
            }

            @Override
            public void received(Connection connection, Object object) {
                if (object instanceof GetGameInfoRequest) {
                    connection.sendTCP(new GetGameInfoResponse(true, 1, playerNames, playerProfilePicturePaths));
                }
            }
        });
    }

    /**
     * Gibt an, ob der Server verbunden ist
     *
     * @return Gibt zurück, ob der Server verbunden ist
     */
    public boolean isConnected() {
        return connected;
    }

    /**
     * Sendet an alle Clients, dass ein Turm gebaut wurde.
     *
     * @param towerType    Der Typ des zu bauenden Turms
     * @param xCoordinate  Die x-Koordinate der Stelle, an der der Turm gebaut werden soll
     * @param yCoordinate  Die y-Koordinate der Stelle, an der der Turm gebaut werden soll
     * @param playerNumber Die Nummer der Spielerin, die den Turm bauen will
     */
    @Override
    public void buildTower(int towerType, int xCoordinate, int yCoordinate, int playerNumber) {
        server.sendToAllTCP(new BuildResponse(true, towerType, xCoordinate, yCoordinate, playerNumber));
    }

    /**
     * Sendet an alle Clients, dass ein Turm verkauft wurde.
     *
     * @param xCoordinate  Die x-Koordinate des Turms
     * @param yCoordinate  Die y-Koordinate des Turms
     * @param playerNumber Die Nummer der Spielerin, der der Turm gehört
     */
    @Override
    public void sellTower(int xCoordinate, int yCoordinate, int playerNumber) {
        server.sendToAllTCP(new SellResponse(true, xCoordinate, yCoordinate, playerNumber));
    }

    /**
     * Sendet an alle Clients die Nachricht, dass ein Turm ausgebaut wurde.
     *
     * @param xCoordinate  Die x-Koordinate des Turms
     * @param yCoordinate  Die y-Koordinate des Turms
     * @param playerNumber Die Nummer der Spielerin, der der Turm gehört
     */
    @Override
    public void upgradeTower(int xCoordinate, int yCoordinate, int playerNumber) {
        server.sendToAllTCP(new UpgradeResponse(true, xCoordinate, yCoordinate, playerNumber));
    }

    /**
     * TODO: Diese Methode passt, glaube ich, noch nicht so ganz. --JKR
     * Schickt einen Gegner zum gegnerischen Spieler
     *
     * @param enemyType      Der Typ des zu schickenden Gegners
     * @param playerToSendTo
     * @param sendingPlayer
     */
    @Override
    public void sendEnemy(int enemyType, int playerToSendTo, int sendingPlayer) {
        server.sendToAllTCP(new SendEnemyResponse(true, enemyType, playerToSendTo, sendingPlayer));

    }

    public void setNumberOfPlayers(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
    }


    private int allocatePlayerNumber() {
        int returnValue = -1;

        for (int i = 0; i < slotsFilled.length; i++) {
            if (!slotsFilled[i]) {
                returnValue = i;
                slotsFilled[i] = true;
                break;
            }
        }

        return returnValue;
    }

    public MainController getMainController() {
        return mainController;
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }
}
