package de.diegrafen.exmatrikulatortd.communication.client;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import de.diegrafen.exmatrikulatortd.communication.server.responses.AllPlayersReadyResponse;
import de.diegrafen.exmatrikulatortd.communication.server.responses.GetGameInfoResponse;

/**
 * @author janro
 * @version 07.08.2019 02:06
 */
public class ConnectionListener implements Listener {

    private GameClient gameClient;

    ConnectionListener(GameClient gameClient) {
        this.gameClient = gameClient;
    }

    @Override
    public void received(Connection connection, Object object) {

        if (object instanceof AllPlayersReadyResponse) {
            handleAllPlayersReadyResponse((AllPlayersReadyResponse) object);
        } else if (object instanceof GetGameInfoResponse) {
            handleGetGameInfoReponse((GetGameInfoResponse) object);
        }
    }

    private void handleAllPlayersReadyResponse(AllPlayersReadyResponse allPlayersReadyResponse) {
        if (allPlayersReadyResponse.getGamestate() == null) {
            Gdx.app.postRunnable(() -> gameClient.getMainController().createNewMultiplayerClientGame(allPlayersReadyResponse.getNumberOfPlayers(), allPlayersReadyResponse.getAllocatedPlayerNumber(), allPlayersReadyResponse.getDifficulty(), allPlayersReadyResponse.getGamemode(), allPlayersReadyResponse.getMapPath(), allPlayersReadyResponse.getPlayerNames()));
        } else {
            Gdx.app.postRunnable(() -> gameClient.getMainController().loadMultiPlayerClientGame(allPlayersReadyResponse.getGamestate(), allPlayersReadyResponse.getAllocatedPlayerNumber(), allPlayersReadyResponse.getMapPath()));
        }

    }

    private void handleGetGameInfoReponse(GetGameInfoResponse getGameInfoResponse) {
        if (getGameInfoResponse.isUpdate()) {
            gameClient.setPlayerNames(getGameInfoResponse.getPlayerNames());
            gameClient.setLocalPlayerNumber(getGameInfoResponse.getAllocatedPlayerNumber());
            gameClient.setMapPath(getGameInfoResponse.getMapPath());
            gameClient.setProfilePicturePaths(getGameInfoResponse.getPlayerProfilePicturePaths());

        } else {
            gameClient.setLocalPlayerNumber(getGameInfoResponse.getAllocatedPlayerNumber());
            gameClient.setMapPath(getGameInfoResponse.getMapPath());
            gameClient.sendGetGameInfoRequest();
        }
    }
}
