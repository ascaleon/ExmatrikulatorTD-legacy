package de.diegrafen.exmatrikulatortd.communication.server;

import com.esotericsoftware.kryonet.Server;
import de.diegrafen.exmatrikulatortd.controller.LogicController;
import de.diegrafen.exmatrikulatortd.communication.Connector;

/**
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 15.06.2019 14:03
 */
public class GameServer extends Connector {

    private int tcpPort;

    private int udpPort;

    private Server server;

    private LogicController logicController;

    public GameServer (int tcpPort, int udpPort, LogicController logicController) {
        this.tcpPort = tcpPort;
        this.udpPort = udpPort;
        this.server = new Server();

        this.logicController = logicController;

        attachBuildRequestListener();
        attachSellRequestListener();
        attachSendEnemyRequestListener();
        attachUpgradeRequestListener();
        attachGetServerStateRequestListener();
    }


    public boolean startServer() {
        server.start();
        try {
            server.bind(tcpPort, udpPort);
        } catch (java.io.IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
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


}
