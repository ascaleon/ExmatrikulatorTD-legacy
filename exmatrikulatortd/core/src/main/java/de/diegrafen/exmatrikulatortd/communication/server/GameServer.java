package de.diegrafen.exmatrikulatortd.communication.server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import de.diegrafen.exmatrikulatortd.communication.client.requests.*;
import de.diegrafen.exmatrikulatortd.communication.server.responses.BuildResponse;
import de.diegrafen.exmatrikulatortd.communication.server.responses.SellResponse;
import de.diegrafen.exmatrikulatortd.communication.server.responses.SendEnemyResponse;
import de.diegrafen.exmatrikulatortd.communication.server.responses.UpgradeResponse;
import de.diegrafen.exmatrikulatortd.controller.factories.EnemyFactory;
import de.diegrafen.exmatrikulatortd.controller.factories.TowerFactory;
import de.diegrafen.exmatrikulatortd.controller.gamelogic.LogicController;
import de.diegrafen.exmatrikulatortd.communication.Connector;

import static de.diegrafen.exmatrikulatortd.util.Constants.TCP_PORT;
import static de.diegrafen.exmatrikulatortd.util.Constants.UDP_PORT;

/**
 *
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

    /**
     * Der Logik-Controller, mit dem der GameServer interagiert
     */
    private LogicController logicController;

    /**
     * Gibt an, ob der GameServer verbunden ist
     */
    private boolean connected;

    /**
     * Erzeugt einen neuen GameServer
     */
    public GameServer () {
        this.tcpPort = TCP_PORT;
        this.udpPort = UDP_PORT;
        this.server = new Server();
        this.connected = false;
        registerObjects(server.getKryo());
    }


    /**
     * Startet den GameServer
     * @return true, wenn das Starten erfolgreich war, ansonsten false
     */
    public boolean startServer() {
        try {
            server.bind(tcpPort, udpPort);
        } catch (final java.io.IOException e) {
            e.printStackTrace();
            return false;
        }

        return connected = true;
    }

    /**
     * Beendet den Server
     */
    @Override
    public void shutdown () {
        server.close();
    }

    /**
     * Fügt RequestListeners hinzu und assoziiert ihn mit einem LogicController
     * @param logicController Der zu assoziierende LogicController
     */
    public void attachRequestListeners (final LogicController logicController) {
        attachBuildRequestListener(logicController);
        attachSellRequestListener(logicController);
        attachSendEnemyRequestListener(logicController);
        attachUpgradeRequestListener(logicController);
        attachGetServerStateRequestListener(logicController);
    }

    /**
     * Fügt einen Request-Listener für das Bauen eines Turms hinzu und assoziiert ihn mit einem LogicController
     * @param logicController Der zu assoziierende LogicController
     */
    private void attachBuildRequestListener (final LogicController logicController) {
        server.addListener(new Listener() {
            public void received (Connection connection, Object object) {
                if (object instanceof BuildRequest) {
                    final BuildRequest request = (BuildRequest) object;
                    final boolean successful = logicController.buildTower(request.getTowerType(), request.getxPosition(), request.getyPosition(), request.getPlayerNumber());

                    if (successful) {
                        server.sendToAllTCP(new BuildResponse(successful, request.getTowerType(), request.getxPosition(), request.getyPosition(), request.getPlayerNumber()));
                    } else {
                        connection.sendTCP(new BuildResponse(successful));
                    }
                }
            }
        });
    }

    /**
     * Fügt einen Request-Listener für das Verkaufen eines Turms hinzu und assoziiert ihn mit einem LogicController
     * @param logicController Der zu assoziierende LogicController
     */
    private void attachSellRequestListener (final LogicController logicController) {
        server.addListener(new Listener() {
            public void received (Connection connection, Object object) {
                if (object instanceof SellRequest) {
                    final SellRequest request = (SellRequest) object;
                    final boolean successful = logicController.sellTower(request.getxPosition(), request.getyPosition(), request.getPlayerNumber());

                    if (successful) {
                        server.sendToAllTCP(new SellResponse(successful, request.getxPosition(), request.getyPosition(), request.getPlayerNumber()));
                    } else {
                        connection.sendTCP(new SellResponse(successful));
                    }
                }
            }
        });
    }

    /**
     * Fügt einen Request-Listener für das Senden eines Gegners hinzu und assoziiert ihn mit einem LogicController
     * @param logicController Der zu assoziierende LogicController
     */
    private void attachSendEnemyRequestListener (final LogicController logicController) {
        server.addListener(new Listener() {
            public void received (Connection connection, Object object) {
                if (object instanceof SendEnemyRequest) {
                    final SendEnemyRequest request = (SendEnemyRequest) object;
                    final boolean successful = logicController.sendEnemy(request.getEnemyType());

                    if (successful) {
                        server.sendToAllTCP(new SendEnemyResponse(successful, request.getEnemyType()));
                    } else {
                        connection.sendTCP(new SendEnemyResponse(successful));
                    }
                }
            }
        });
    }

    /**
     * Fügt einen Request-Listener für das Aufrüsten eines Turms hinzu und assoziiert ihn mit einem LogicController
     * @param logicController Der zu assoziierende LogicController
     */
    private void attachUpgradeRequestListener (final LogicController logicController) {
        server.addListener(new Listener() {
            public void received (Connection connection, Object object) {
                if (object instanceof UpgradeRequest) {
                    final UpgradeRequest request = (UpgradeRequest) object;
                    final boolean successful = logicController.upgradeTower(request.getxPosition(), request.getyPosition(), request.getPlayerNumber());

                    if (successful) {
                        server.sendToAllTCP(new UpgradeResponse(successful, request.getxPosition(), request.getyPosition(), request.getPlayerNumber()));
                    } else {
                        connection.sendTCP(new UpgradeResponse(successful));
                    }
                }
            }
        });
    }

    /**
     * Fügt einen Request-Listener für das Abrufen des Server-Spielzustandes hinzu und assoziiert ihn mit einem LogicController
     * @param logicController Der zu assoziierende LogicController
     */
    private void attachGetServerStateRequestListener (final LogicController logicController) {
        server.addListener(new Listener() {
            public void received (Connection connection, Object object) {
                if (object instanceof GetServerStateRequest) {
                    connection.sendTCP(logicController.getGamestate());
                }
            }
        });
    }

    /**
     * Gibt an, ob der Server verbunden ist
     * @return Gibt zurück, ob der Server verbunden ist
     */
    public boolean isConnected() {
        return connected;
    }

    /**
     * Baut einen neuen Turm
     *
     * @param towerType
     * @param xPosition
     * @param yPosition
     * @param playerNumber
     * @return Wenn das Bauen erfolgreich war, true, ansonsten false
     */
    @Override
    public void buildTower(TowerFactory.TowerType towerType, int xPosition, int yPosition, int playerNumber) {
        server.sendToAllTCP(new BuildResponse(true, towerType, xPosition, yPosition, playerNumber));
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
        server.sendToAllTCP(new SellResponse(true, xPosition, yPosition, playerNumber));
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
    public void upgradeTower(int xPosition, int yPosition, int playerNumber) {

    }

    /**
     * Schickt einen Gegner zum gegnerischen Spieler
     *
     * @param enemyType@return Wenn das Schicken erfolgreich war, true, ansonsten false
     */
    @Override
    public void sendEnemy(EnemyFactory.EnemyType enemyType) {

    }
}
