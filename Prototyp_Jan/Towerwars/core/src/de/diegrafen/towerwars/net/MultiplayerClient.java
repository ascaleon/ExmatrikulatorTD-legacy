package de.diegrafen.towerwars.net;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

/**
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 16.05.2019 00:10
 */
public class MultiplayerClient extends Connector {

    private Client client;

    public MultiplayerClient (Client client) {
        super(client.getKryo());
    }

    public void startClient () {
        client.start();
    }

    public void closeClient () {
        client.close();
    }


/*    public void registerAction () {
        client.addListener(new Listener() {
            @Override
            public void received(Connection connection, Object object) {
                if(object instanceof SRq) {
                    Gdx.app.log("Client", ((SRq) object).data);
                }
            }
        });
    }*/

    public boolean connectClient (int timeout, String hostAddress, int tcpPort, int udpPort) {
        try {
            //client.connect(6000, "localhost", 54555, 54777);
            client.connect(timeout, hostAddress, tcpPort, udpPort);
            return client.isConnected();
        } catch (Exception e) {
            System.err.println("Failed to connect to server!");
            return false;
        }
    }

    public Client getClient() {
        return client;
    }

    public void sendTCP (Object object) {
        client.sendTCP(object);
    }

    public void sendUDP (Object object) {
        client.sendUDP(object);
    }
}
