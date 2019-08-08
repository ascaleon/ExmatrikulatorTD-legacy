package de.diegrafen.exmatrikulatortd.communication.client;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import de.diegrafen.exmatrikulatortd.communication.server.responses.*;
import de.diegrafen.exmatrikulatortd.controller.gamelogic.ClientLogicController;
import de.diegrafen.exmatrikulatortd.controller.gamelogic.LogicController;

import static de.diegrafen.exmatrikulatortd.controller.factories.NewGameFactory.MULTIPLAYER_DUEL;

/**
 * @author janro
 * @version 06.08.2019 21:01
 */
public class GameListener implements Listener {

    private GameClient gameClient;

    private ClientLogicController clientLogicController;

    GameListener(GameClient gameClient, ClientLogicController clientLogicController) {
        this.gameClient = gameClient;
        this.clientLogicController = clientLogicController;
    }

    @Override
    public void connected(Connection connection) {

    }

    @Override
    public void disconnected(Connection connection) {
        if (gameClient.isGameRunning()) {
            // Todo: Statt exitGame() aufzurufen, das Anzeigen eines passenden Fensters triggern
            System.err.println("Der Server hat die Verbindung geschlossen!");
            Gdx.app.postRunnable(clientLogicController::gameConnectionLost);
            gameClient.setGameRunning(false);
            gameClient.getMainController().shutdownConnections();
        }
    }

    @Override
    public void received(Connection connection, Object object) {

        if (object instanceof ErrorResponse) {
            handleErrorResponse((ErrorResponse) object);
        } else if (object instanceof AllPlayersReadyResponse) {
            handleAllPlayersReadyResponse();
        } else if (object instanceof StartGameResponse) {
            handleStartGameReponse();
        } else if (object instanceof BuildResponse) {
            handleBuildReponse((BuildResponse) object);
        } else if (object instanceof SellResponse) {
            handleSellReponse((SellResponse) object);
        } else if (object instanceof SendEnemyResponse) {
            handleSendEnemyResponse((SendEnemyResponse) object);
        } else if (object instanceof GetServerStateResponse) {
            handleGetServerStateResponse((GetServerStateResponse) object);
        } else if (object instanceof UpgradeResponse) {
            handleUpgradeReponse((UpgradeResponse) object);
        }
    }

    private void handleErrorResponse(ErrorResponse errorResponse) {
        clientLogicController.displayErrorMessage(errorResponse.getErrorMessage(), errorResponse.getPlayerNumber());
    }

    private void handleAllPlayersReadyResponse() {
        Gdx.app.postRunnable(() -> gameClient.getMainController().createNewMultiplayerClientGame(2, gameClient.getLocalPlayerNumber(), MULTIPLAYER_DUEL, gameClient.getMapPath()));
    }

    private void handleStartGameReponse() {
        if (!gameClient.isGameRunning()) {
            gameClient.setGameRunning(true);
            System.err.println("Spiel läuft!");
            Gdx.app.postRunnable(() -> gameClient.getMainController().showScreen(clientLogicController.getGameScreen()));
        }
    }

    /**
     * @param buildResponse
     */
    private void handleBuildReponse(final BuildResponse buildResponse) {
        Gdx.app.postRunnable(() -> clientLogicController.addTowerFromServer(buildResponse.getTowerType(),
                buildResponse.getxCoordinate(), buildResponse.getyCoordinate(), buildResponse.getPlayerNumber()));
        System.out.println("Response received!");
    }

    private void handleSellReponse(final SellResponse sellResponse) {
        Gdx.app.postRunnable(() -> clientLogicController.sellTowerFromServer(sellResponse.getxCoordinate(), sellResponse.getyCoordinate(), sellResponse.getPlayerNumber()));
    }

    /**
     * @param sendEnemyResponse
     */
    private void handleSendEnemyResponse(SendEnemyResponse sendEnemyResponse) {
        Gdx.app.postRunnable(() -> clientLogicController.sendEnemyFromServer(sendEnemyResponse.getEnemyType(), sendEnemyResponse.getPlayerToSendTo(), sendEnemyResponse.getSendingPlayer()));
    }


    private void handleGetServerStateResponse(final GetServerStateResponse getServerStateResponse) {
        clientLogicController.setGamestateFromServer(getServerStateResponse.getGamestate());
    }

    /**
     * @param logicController Der LogicController, an den die empfangene Antwort weitergeleitet wird
     */
    private void handleUpgradeReponse(final UpgradeResponse upgradeResponse) {
        Gdx.app.postRunnable(() -> clientLogicController.upgradeTowerFromServer(upgradeResponse.getxCoordinate(), upgradeResponse.getyCoordinate(), upgradeResponse.getPlayerNumber()));
    }

}
