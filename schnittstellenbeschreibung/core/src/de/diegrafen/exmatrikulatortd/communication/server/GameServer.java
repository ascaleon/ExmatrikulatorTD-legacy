package de.diegrafen.exmatrikulatortd.communication.server;

import com.esotericsoftware.kryonet.Server;
import de.diegrafen.exmatrikulatortd.controller.gamelogic.LogicController;
import de.diegrafen.exmatrikulatortd.communication.Connector;

import static de.diegrafen.exmatrikulatortd.util.Constants.TCP_PORT;
import static de.diegrafen.exmatrikulatortd.util.Constants.UDP_PORT;

/**
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 15.06.2019 14:03
 */
public class GameServer extends Connector {

    private int tcpPort;

    private int udpPort;

    private Server server;

    private LogicController logicController;

    private boolean connected;

    public GameServer () {
        this.tcpPort = TCP_PORT;
        this.udpPort = UDP_PORT;
        this.server = new Server();
        this.connected = false;
        registerObjects(server.getKryo());

        attachBuildRequestListener();
        attachSellRequestListener();
        attachSendEnemyRequestListener();
        attachUpgradeRequestListener();
        attachGetServerStateRequestListener();
    }


    public boolean startServer() {
        try {
            server.bind(tcpPort, udpPort);
        } catch (java.io.IOException e) {
            e.printStackTrace();
            return false;
        }

        return connected = true;
    }

    public void shutdown () {

    }

    private void attachBuildRequestListener () {

    }

    private void attachSellRequestListener () {

    }

    private void attachSendEnemyRequestListener () {

    }

    private void attachUpgradeRequestListener () {

    }

    private void attachGetServerStateRequestListener () {

    }

    public boolean isConnected() {
        return connected;
    }

    public void setLogicController(LogicController logicController) {
        this.logicController = logicController;
    }
}
