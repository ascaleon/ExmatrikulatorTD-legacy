package de.diegrafen.exmatrikulatortd.communication.client;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import de.diegrafen.exmatrikulatortd.communication.server.responses.AllPlayersReadyResponse;
import de.diegrafen.exmatrikulatortd.communication.server.responses.GetGameInfoResponse;

import static de.diegrafen.exmatrikulatortd.controller.factories.NewGameFactory.MULTIPLAYER_DUEL;

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
            handleAllPlayersReadyReponse();
        } else if (object instanceof GetGameInfoResponse) {
            handleGetGameInfoReponse((GetGameInfoResponse) object);
        }
    }

    private void handleAllPlayersReadyReponse() {
        Gdx.app.postRunnable(() -> gameClient.getMainController().createNewMultiplayerClientGame(2, gameClient.getLocalPlayerNumber(), MULTIPLAYER_DUEL, gameClient.getMapPath()));
    }

    private void handleGetGameInfoReponse(GetGameInfoResponse getGameInfoResponse) {
        //if (response.isUpdate()) {
        // Update-Code kommt hierhin
        //} else {
        if (!getGameInfoResponse.isUpdate()) {
            gameClient.setLocalPlayerNumber(getGameInfoResponse.getAllocatedPlayerNumber());
            gameClient.setMapPath(getGameInfoResponse.getMapPath());
        }
    }
}
