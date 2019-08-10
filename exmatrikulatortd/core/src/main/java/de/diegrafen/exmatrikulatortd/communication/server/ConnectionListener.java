package de.diegrafen.exmatrikulatortd.communication.server;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import de.diegrafen.exmatrikulatortd.communication.client.requests.ClientReadyRequest;
import de.diegrafen.exmatrikulatortd.communication.client.requests.GetGameInfoRequest;
import de.diegrafen.exmatrikulatortd.communication.client.requests.SendPlayerNameRequest;
import de.diegrafen.exmatrikulatortd.communication.server.responses.AllPlayersReadyResponse;
import de.diegrafen.exmatrikulatortd.communication.server.responses.GetGameInfoResponse;
import de.diegrafen.exmatrikulatortd.controller.gamelogic.LogicController;

import java.util.LinkedList;
import java.util.List;

import static de.diegrafen.exmatrikulatortd.controller.factories.NewGameFactory.MULTIPLAYER_DUEL;

/**
 * @author janro
 * @version 07.08.2019 01:25
 */
public class ConnectionListener implements Listener {

    private GameServer gameServer;

    ConnectionListener(GameServer gameServer) {
        this.gameServer = gameServer;
    }

    @Override
    public void connected(Connection connection) {
        int playerNumber = gameServer.allocatePlayerNumber();

        gameServer.getConnectionAndPlayerNumbers().put(connection.getID(), playerNumber);

        if (gameServer.openSlotsLeft() <= 0) {
            gameServer.setLookingForPlayers(false);
        }

        GetGameInfoResponse getGameInfoResponse = new GetGameInfoResponse(true, playerNumber, gameServer.getPlayerNames(), gameServer.getProfilePicturePaths(), gameServer.getMapPath());
        gameServer.getServer().sendToAllExceptTCP(connection.getID(), getGameInfoResponse);
        getGameInfoResponse.setUpdate(false);
        connection.sendTCP(getGameInfoResponse);
    }

    @Override
    public void disconnected(Connection connection) {
        if (!gameServer.isGameRunning()) {
            gameServer.emptySlot(connection.getID());
        }
    }

    @Override
    public void received(Connection connection, Object object) {
        if (object instanceof GetGameInfoRequest) {
            handleGetGameInfoRequest(connection, (GetGameInfoRequest) object);
        } else if (object instanceof ClientReadyRequest) {
            handleClientReadyRequest(connection);
        } else if (object instanceof SendPlayerNameRequest) {
            handleSendPlayerNameRequest(connection, (SendPlayerNameRequest) object);
        }
    }

    private void handleGetGameInfoRequest(Connection connection, GetGameInfoRequest getGameInfoRequest) {
        int playerNumber = gameServer.getConnectionAndPlayerNumbers().get(connection.getID());
        gameServer.getPlayerNames()[playerNumber] = getGameInfoRequest.getPlayerName();
        gameServer.getProfilePicturePaths()[playerNumber] = getGameInfoRequest.getProfilePicturePath();

        connection.sendTCP(new GetGameInfoResponse(true, playerNumber, gameServer.getPlayerNames(), gameServer.getProfilePicturePaths(), gameServer.getMapPath()));
    }

    private void handleClientReadyRequest(Connection connection) {
        gameServer.setPlayerReady(connection.getID());

        System.out.println("ClientReadyRequest erhalten!");
        if (gameServer.areAllPlayersReady()) {
            gameServer.getServer().sendToAllTCP(new AllPlayersReadyResponse());
            //mainController.showLoadScreen();
            Gdx.app.postRunnable(() -> gameServer.getMainController().createNewMultiplayerServerGame(gameServer.getNumberOfPlayers(), gameServer.getMainController().getCurrentProfilePreferredDifficulty(), 0, MULTIPLAYER_DUEL, gameServer.getMapPath(), gameServer.getPlayerNames()));
        }
    }

    private void handleSendPlayerNameRequest(Connection connection, SendPlayerNameRequest sendPlayerNameRequest) {
        int playerNumber = gameServer.getConnectionAndPlayerNumbers().get(connection.getID());
        gameServer.getPlayerNames()[playerNumber] = sendPlayerNameRequest.getPlayerName();
        gameServer.getServer().sendToAllTCP(new GetGameInfoResponse());
    }
}
