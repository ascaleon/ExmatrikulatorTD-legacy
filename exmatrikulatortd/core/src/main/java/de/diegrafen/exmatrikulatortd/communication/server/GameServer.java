package de.diegrafen.exmatrikulatortd.communication.server;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.kryonet.ServerDiscoveryHandler;
import de.diegrafen.exmatrikulatortd.communication.server.responses.*;
import de.diegrafen.exmatrikulatortd.controller.MainController;
import de.diegrafen.exmatrikulatortd.controller.gamelogic.LogicController;
import de.diegrafen.exmatrikulatortd.communication.Connector;
import de.diegrafen.exmatrikulatortd.model.Gamestate;
import de.diegrafen.exmatrikulatortd.model.Player;
import de.diegrafen.exmatrikulatortd.model.tower.Tower;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.HashMap;
import java.util.List;

import static de.diegrafen.exmatrikulatortd.util.Assets.MULTIPLAYER_MAP_PATH;
import static de.diegrafen.exmatrikulatortd.util.Constants.*;

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

    private int difficulty;

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
        this.server = new Server(16384*8, 2048*8);
        registerObjects(server.getKryo());
        this.server.addListener(new ConnectionListener(this));
        System.out.println("Server created!");
    }

    /**
     * Startet den GameServer
     * @param numberOfPlayers Die maximale Anzahl an Spielerinnen, die sich mit dem Server verbinden können
     */
    public void startServer(int numberOfPlayers, int difficulty) {
        try {
            server.bind(tcpPort, udpPort);
            System.out.println("Server started!");
            this.numberOfPlayers = numberOfPlayers;
            this.difficulty = difficulty;
            playersReady = new boolean[numberOfPlayers];
            playerNames = new String[numberOfPlayers];
            playerNames[0] = mainController.getCurrentProfileName();
            profilePicturePaths = new String[numberOfPlayers];
            profilePicturePaths[0] = mainController.getCurrentProfilePicturePath();
            playersfinishedLoading = new boolean[numberOfPlayers];
            slotsFilled = new boolean[numberOfPlayers];
            slotsFilled[0] = true;
            server.setDiscoveryHandler(new ServerDiscoveryHandler() {
                @Override
                public boolean onDiscoverHost(DatagramChannel datagramChannel, InetSocketAddress fromAddress) throws IOException {

                    if (openSlotsLeft() > 0) {
                        lookingForPlayers = true;
                        String newData = mapName + "\n" + numberOfPlayers + "\n" + difficulty;

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
        server.addListener(new GameListener(this, logicController));
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

        if (areAllPlayersReady()) {
            for (Connection connection : server.getConnections()) {
                int allocatedPlayerNumber = connectionAndPlayerNumbers.get(connection.getID());
                server.sendToTCP(connection.getID(), new AllPlayersReadyResponse(difficulty, numberOfPlayers, allocatedPlayerNumber, MULTIPLAYER_DUEL, mapPath, playerNames));
            }

            Gdx.app.postRunnable(() -> mainController.createNewMultiplayerServerGame(numberOfPlayers, difficulty, 0, MULTIPLAYER_DUEL, mapPath, playerNames));
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
            System.out.println("Start Game!");
            gameRunning = true;
            server.sendToAllTCP(new StartGameResponse());
            Gdx.app.postRunnable(() -> mainController.showScreen(logicController.getGameScreen()));
        }
    }

    @Override
    public void sendServerGameState(List<Tower> towers, List<Player> players) {
        server.sendToAllTCP(new GetServerStateResponse(towers, players));
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

    public Server getServer() {
        return server;
    }

    void setLookingForPlayers(boolean lookingForPlayers) {
        this.lookingForPlayers = lookingForPlayers;
    }

    void emptySlot(int connectionID) {
        slotsFilled[connectionAndPlayerNumbers.get(connectionID)] = false;
        connectionAndPlayerNumbers.remove(connectionID);
        lookingForPlayers = true;
    }

    void setPlayerReady(int connectionID) {
        playersReady[connectionAndPlayerNumbers.get(connectionID)] = true;
    }

    void registerClientAsFinishedLoading(int connectionID) {
        playersfinishedLoading[connectionAndPlayerNumbers.get(connectionID)] = true;
    }
}
