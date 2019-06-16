package de.diegrafen.exmatrikulatortd.communication.server;

import com.esotericsoftware.kryonet.Server;
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
        } catch (java.io.IOException e) {
            e.printStackTrace();
            return false;
        }

        return connected = true;
    }

    /**
     * Beendet den Server
     */
    public void shutdown () {

    }

    /**
     * Fügt RequestListeners hinzu und assoziiert ihn mit einem LogicController
     * @param logicController Der zu assoziierende LogicController
     */
    public void attachRequestListeners (LogicController logicController) {

    }

    /**
     * Fügt einen Request-Listener für das Bauen eines Turms hinzu und assoziiert ihn mit einem LogicController
     * @param logicController Der zu assoziierende LogicController
     */
    private void attachBuildRequestListener (LogicController logicController) {

    }

    /**
     * Fügt einen Request-Listener für das Verkaufen eines Turms hinzu und assoziiert ihn mit einem LogicController
     * @param logicController Der zu assoziierende LogicController
     */
    private void attachSellRequestListener (LogicController logicController) {

    }

    /**
     * Fügt einen Request-Listener für das Senden eines Gegners hinzu und assoziiert ihn mit einem LogicController
     * @param logicController Der zu assoziierende LogicController
     */
    private void attachSendEnemyRequestListener (LogicController logicController) {

    }

    /**
     * Fügt einen Request-Listener für das Aufrüsten eines Turms hinzu und assoziiert ihn mit einem LogicController
     * @param logicController Der zu assoziierende LogicController
     */
    private void attachUpgradeRequestListener (LogicController logicController) {

    }

    /**
     * Fügt einen Request-Listener für das Abrufen des Server-Spielzustandes hinzu und assoziiert ihn mit einem LogicController
     * @param logicController Der zu assoziierende LogicController
     */
    private void attachGetServerStateRequestListener (LogicController logicController) {

    }

    /**
     * Gibt an, ob der Server verbunden ist
     * @return Gibt zurück, ob der Server verbunden ist
     */
    public boolean isConnected() {
        return connected;
    }

    public void setLogicController(LogicController logicController) {
        this.logicController = logicController;
    }
}
