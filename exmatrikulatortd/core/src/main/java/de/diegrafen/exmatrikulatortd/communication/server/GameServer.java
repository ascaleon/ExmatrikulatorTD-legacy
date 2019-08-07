package de.diegrafen.exmatrikulatortd.communication.server;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.kryonet.ServerDiscoveryHandler;
import de.diegrafen.exmatrikulatortd.communication.server.responses.*;
import de.diegrafen.exmatrikulatortd.controller.MainController;
import de.diegrafen.exmatrikulatortd.controller.gamelogic.LogicController;
import de.diegrafen.exmatrikulatortd.communication.Connector;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.HashMap;

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
        this.server.addListener(new ConnectionListener(this));
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

                    if (openSlotsLeft() > 0) {
                        lookingForPlayers = true;
                        String newData = mapName + "\n" + numberOfPlayers;

                        ByteBuffer buf = ByteBuffer.allocate(48);
                        buf.clear();
                        buf.put(newData.getBytes());
                        buf.flip();

                        int bytesSent = datagramChannel.send(buf, fromAddress);
                        return bytesSent > 0;
                    }
                    return false;
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
    int openSlotsLeft() {
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

    }

    boolean haveAllPlayersFinishedLoading() {
        boolean allPlayersFinishedLoading = true;

        for (boolean hasPlayerFinishedLoading : playersfinishedLoading) {
            allPlayersFinishedLoading &= hasPlayerFinishedLoading;
        }

        return allPlayersFinishedLoading;
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
    boolean areAllPlayersReady() {

        boolean allPlayersReady = true;

        for (boolean isPlayerReady : playersReady) {
            allPlayersReady &= isPlayerReady;
        }

        return allPlayersReady;
    }

    int allocatePlayerNumber() {
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
            Gdx.app.postRunnable(() -> {
                mainController.showScreen(logicController.getGameScreen());
            });
        }
    }

    boolean isLookingForPlayers() {
        return lookingForPlayers;
    }

    boolean isGameRunning() {
        return gameRunning;
    }

    void setGameRunning(boolean gameRunning) {
        this.gameRunning = gameRunning;
    }

    int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    boolean[] getSlotsFilled() {
        return slotsFilled;
    }

    boolean[] getPlayersfinishedLoading() {
        return playersfinishedLoading;
    }

    boolean[] getPlayersReady() {
        return playersReady;
    }

    String[] getPlayerNames() {
        return playerNames;
    }

    String[] getProfilePicturePaths() {
        return profilePicturePaths;
    }

    HashMap<Integer, Integer> getConnectionAndPlayerNumbers() {
        return connectionAndPlayerNumbers;
    }

    public String getMapPath() {
        return mapPath;
    }

    public String getMapName() {
        return mapName;
    }

    public Server getServer() {
        return server;
    }

    void setLookingForPlayers(boolean lookingForPlayers) {
        this.lookingForPlayers = lookingForPlayers;
    }
}
