package de.diegrafen.exmatrikulatortd.communication.server;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.kryonet.ServerDiscoveryHandler;
import de.diegrafen.exmatrikulatortd.communication.client.requests.*;
import de.diegrafen.exmatrikulatortd.communication.server.responses.*;
import de.diegrafen.exmatrikulatortd.controller.MainController;
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
import static de.diegrafen.exmatrikulatortd.util.Assets.MULTIPLAYER_MAP_PATH;
import static de.diegrafen.exmatrikulatortd.util.Constants.TCP_PORT;
import static de.diegrafen.exmatrikulatortd.util.Constants.UDP_PORT;

/**
 * GameServer-Klasse
 *
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 15.06.2019 14:03
 */
public class GameServer extends Connector implements ServerInterface {

    /**
     * Der Port, über den TCP-Verbindungen entgegengenommen werden
     */
    private final int tcpPort;

    /**
     * Der Port, über den UDP-Verbindungen entgegengenommen werden
     */
    private final int udpPort;

    /**
     * Der Server, über den die Kommunikation abläuft
     */
    private final Server server;

    private MainController mainController;

    /**
     * Der Logik-Controller, mit dem der GameServer interagiert
     */
    private LogicController logicController;

    private boolean lookingForPlayers = true;

    private boolean gameRunning = false;

    private int numberOfPlayers;

    private boolean[] slotsFilled;

    private boolean[] playersfinishedLoading;

    private boolean[] playersReady;

    private String[] playerNames;

    private String[] profilePicturePaths;

    private final HashMap<Integer, Integer> connectionAndPlayerNumbers = new HashMap<>();

    /**
     * Der zum Starten des Spiels verwendete Kartenpfad
     */
    private final String mapPath = MULTIPLAYER_MAP_PATH;

    private final String mapName = "Die Magieakademie";

    /**
     * Erzeugt einen neuen GameServer
     */
    public GameServer() {
        this.tcpPort = TCP_PORT;
        this.udpPort = UDP_PORT;
        this.server = new Server();
        registerObjects(server.getKryo());
        // TODO: Listener besser organisieren!
        attachGetGameInfoRequestListener();
        attachClientReadyRequestListener();
        System.out.println("Server created!");
    }


    /**
     * Startet den GameServer
     * @param numberOfPlayers Die maximale Anzahl an Spielerinnen, die sich mit dem Server verbinden können
     */
    public void startServer(int numberOfPlayers) {
        try {
            server.bind(tcpPort, udpPort);
            System.out.println("Server started!");
            this.numberOfPlayers = numberOfPlayers;
            playersReady = new boolean[numberOfPlayers];
            playersfinishedLoading = new boolean[numberOfPlayers];
            slotsFilled = new boolean[numberOfPlayers];
            slotsFilled[0] = true;
            server.setDiscoveryHandler(new ServerDiscoveryHandler() {
                @Override
                public boolean onDiscoverHost(DatagramChannel datagramChannel, InetSocketAddress fromAddress) throws IOException {

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
        }
    }

    /**
     * Zählt, wie viele Spielerinnen-Slots noch offen sind
     * @return Die Anzahl der offenen Spielerinnen-Slots
     */
    private int openSlotsLeft() {
        int openSlots = 0;

        for (boolean slotFilled : slotsFilled) {
            if (!slotFilled) {
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
        this.logicController = logicController;

        attachClientFinishedLoadingRequest(logicController);
        attachBuildRequestListener(logicController);
        attachSellRequestListener(logicController);
        attachSendEnemyRequestListener(logicController);
        attachUpgradeRequestListener(logicController);
        attachGetServerStateRequestListener(logicController);
        // TODO: Die letzten Listener scheinen nicht mehr erreicht zu werden
    }

    private void attachClientFinishedLoadingRequest(LogicController logicController) {
        server.addListener(new Listener() {
            @Override
            public void received(Connection connection, Object object) {
                if (object instanceof FinishedLoadingRequest) {
                    System.out.println("FinishedLoadingRequest erhalten!");
                    playersfinishedLoading[connectionAndPlayerNumbers.get(connection.getID())] = true;
                    System.err.println(connection.getID() + " ist bereit!");
                    if (haveAllPlayersFinishedLoading()) {
                        gameRunning = true;
                        server.sendToAllTCP(new StartGameResponse());
                        //gameRunning = true;
                        Gdx.app.postRunnable(() -> mainController.showScreen(logicController.getGameScreen()));
                        }
                    }
                }
            });
        }

    private void attachClientReadyRequestListener() {
        server.addListener(new Listener() {
            @Override
            public void received(Connection connection, Object object) {
                if (object instanceof ClientReadyRequest) {
                    playersReady[connectionAndPlayerNumbers.get(connection.getID())] = true;

                    System.out.println("ClientReadyRequest erhalten!");
                    if (areAllPlayersReady()) {
                        server.sendToAllTCP(new AllPlayersReadyResponse());
                        //mainController.showLoadScreen();
                        Gdx.app.postRunnable(() -> mainController.createNewMultiplayerServerGame(numberOfPlayers, 0, MULTIPLAYER_DUEL, mapPath));
                    }
                }
            }
        });
    }

    private boolean haveAllPlayersFinishedLoading() {
        boolean allPlayersFinishedLoading = true;

        for (boolean hasPlayerFinishedLoading : playersfinishedLoading) {
            allPlayersFinishedLoading &= hasPlayerFinishedLoading;
        }

        return allPlayersFinishedLoading;
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
                    Gdx.app.postRunnable(() -> logicController.buildTower(request.getTowerType(), request.getxCoordinate(), request.getyCoordinate(), request.getPlayerNumber()));
                }
            }
        });
    }

    /**
     * mainController.showScreen(logicController.getGameScreen());
     * Fügt einen Request-Listener für das Verkaufen eines Turms hinzu und assoziiert ihn mit einem LogicController
     *
     * @param logicController Der zu assoziierende LogicController
     */
    private void attachSellRequestListener(final LogicController logicController) {
        server.addListener(new Listener() {
            public void received(Connection connection, Object object) {
                if (object instanceof SellRequest) {
                    final SellRequest request = (SellRequest) object;
                    Gdx.app.postRunnable(() -> logicController.sellTower(request.getxCoordinate(), request.getyCoordinate(), request.getPlayerNumber()));
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
                    Gdx.app.postRunnable(() -> logicController.sendEnemy(request.getEnemyType(), request.getPlayerToSendTo(), request.getSendingPlayer()));

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
                    Gdx.app.postRunnable(() -> logicController.upgradeTower(request.getxCoordinate(), request.getyCoordinate(), request.getPlayerNumber()));

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

        // TODO: Alle RequestListener in einem zusammenfassen
        server.addListener(new Listener() {
            final List<String> playerNames = new LinkedList<>();
            final List<String> playerProfilePicturePaths = new LinkedList<>();

            @Override
            public void connected(Connection connection) {

                int playerNumber = allocatePlayerNumber();

                connectionAndPlayerNumbers.put(connection.getID(), playerNumber);

                if (openSlotsLeft() <= 0) {
                    lookingForPlayers = false;
                }

                GetGameInfoResponse getGameInfoResponse = new GetGameInfoResponse(true, playerNumber, playerNames, playerProfilePicturePaths, mapPath);
                server.sendToAllExceptTCP(connection.getID(), getGameInfoResponse);
                getGameInfoResponse.setUpdate(false);
                connection.sendTCP(getGameInfoResponse);
                System.out.println("Looking for Players? " + lookingForPlayers);
                //if (!lookingForPlayers) {
                    //gameRunning = true;
                //}
            }

            @Override
            public void disconnected(Connection connection) {
                if (!gameRunning) {
                    slotsFilled[connectionAndPlayerNumbers.get(connection.getID())] = false;
                    connectionAndPlayerNumbers.remove(connection.getID());
                    lookingForPlayers = true;
                } else {
                    Gdx.app.postRunnable(logicController::gameConnectionLost);
                    gameRunning = false;
                    server.close();
                }
            }

            @Override
            public void received(Connection connection, Object object) {
                if (object instanceof GetGameInfoRequest) {
                    connection.sendTCP(new GetGameInfoResponse(true, 1, playerNames, playerProfilePicturePaths, mapPath));
                }
            }
        });
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
        server.sendToAllTCP(new BuildResponse(towerType, xCoordinate, yCoordinate, playerNumber));
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
        server.sendToAllTCP(new SellResponse(xCoordinate, yCoordinate, playerNumber));
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
        server.sendToAllTCP(new UpgradeResponse(xCoordinate, yCoordinate, playerNumber));
    }

    /**
     * Schickt einen Gegner zum gegnerischen Spieler
     *
     * @param enemyType      Der Typ des zu schickenden Gegners
     * @param playerToSendTo Die Nummer der Spielerin, an die der Gegner geschickt werden soll
     * @param sendingPlayer  Die Nummer der sendenden Spielerin
     */
    @Override
    public void sendEnemy(int enemyType, int playerToSendTo, int sendingPlayer) {
        server.sendToAllTCP(new SendEnemyResponse(enemyType, playerToSendTo, sendingPlayer));
    }

    @Override
    public void setServerReady() {
        playersReady[0] = true;

        System.out.println(areAllPlayersReady());

        if (areAllPlayersReady()) {
            server.sendToAllTCP(new AllPlayersReadyResponse());
            Gdx.app.postRunnable(() -> mainController.createNewMultiplayerServerGame(numberOfPlayers, 0, MULTIPLAYER_DUEL, mapPath));
        }
    }

    /**
     * Überprüft, ob alle Spielerinnen bereit sind
     * @return {@code true}, wenn alle Spielerinnen bereit sind, ansonsten {@code false}
     */
    private boolean areAllPlayersReady() {

        boolean allPlayersReady = true;

        for (boolean isPlayerReady : playersReady) {
            allPlayersReady &= isPlayerReady;
        }

        return allPlayersReady;
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
        return this.mainController;
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }


    @Override
    public void sendErrorMessage(String errorMessage, int playerNumber) {
        server.sendToAllTCP(new ErrorResponse(errorMessage, playerNumber));
    }

    @Override
    public void serverFinishedLoading() {
        playersfinishedLoading[0] = true;
        if (haveAllPlayersFinishedLoading()) {
            gameRunning = true;
            server.sendToAllTCP(new StartGameResponse());
            Gdx.app.postRunnable(() -> mainController.showScreen(logicController.getGameScreen()));
        }
    }
}
