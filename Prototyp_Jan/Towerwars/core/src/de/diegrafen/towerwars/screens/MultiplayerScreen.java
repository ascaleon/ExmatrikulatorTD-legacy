package de.diegrafen.towerwars.screens;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import de.diegrafen.towerwars.net.GameState;
import de.diegrafen.towerwars.net.MultiplayerClient;
import de.diegrafen.towerwars.net.MultiplayerServer;

/**
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 16.05.2019 00:28
 */
public class MultiplayerScreen {

    private boolean isServer = false;

    private MultiplayerClient multiplayerClient;

    private MultiplayerServer multiplayerServer;

    private GameState gameState;

    public MultiplayerScreen (MultiplayerClient multiplayerClient) {
        this.multiplayerClient = multiplayerClient;
    }

    public MultiplayerScreen (MultiplayerClient multiplayerClient, MultiplayerServer multiplayerServer) {
        this.multiplayerClient = multiplayerClient;
        registerClientActions();
        this.multiplayerServer = multiplayerServer;
        this.isServer = true;
    }

    private void registerClientActions () {
        multiplayerClient.getClient().addListener(new Listener() {
            @Override
            public void received(Connection connection, Object object) {
                if(object instanceof GameState) {
                    Gdx.app.log("Received Game State!", object.toString());
                }
            }
        });
    }

    private void registerServerActions () {
        multiplayerServer.getServer().addListener(new Listener() {
            @Override
            public void received(Connection connection, Object object) {
                if(object instanceof GameState) {
                    System.out.println("Server " +  object.toString());
                    gameState = new GameState(GameState.GameStateType.SERVER);
                    connection.sendTCP(gameState);
                }
            }
        });
    }
}
