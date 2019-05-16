package de.diegrafen.towerwars.net;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

/**
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 16.05.2019 00:30
 */
public class MultiplayerServer extends Connector {

    private Server server;

    public MultiplayerServer () {
        server = new Server();
        Kryo kryo = server.getKryo();
        registerObjects(kryo);
    }

    public void startServer () {
        server.start();
    }

    public void closeServer () {
        server.close();
    }

/*   server.addListener(new Listener() {
        @Override
        public void received(Connection connection, Object object) {
            if(object instanceof SRq) {
                System.out.println("Server " +  ((SRq) object).data);
                SRq sRq = new SRq();
                sRq.data = "Data";
                connection.sendTCP(sRq);
            }
        }
    });*/

    public boolean bindServer (int tcpPort, int udpPort) {
        try {
            server.bind(tcpPort, udpPort);
            return true;
        } catch (Exception e) {
            System.err.println("Failed to bind to port!");
            return false;
        }
    }

    public Server getServer() {
        return server;
    }

    public void sendTCPToClients (Object object) {
        for (Connection connection : server.getConnections()) {
            connection.sendTCP(object);
        }
    }

    public void sendUDPToClients (Object object) {
        for (Connection connection : server.getConnections()) {
            connection.sendUDP(object);
        }
    }
}
