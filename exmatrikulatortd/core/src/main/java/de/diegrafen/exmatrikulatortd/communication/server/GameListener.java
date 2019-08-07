package de.diegrafen.exmatrikulatortd.communication.server;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import de.diegrafen.exmatrikulatortd.communication.client.requests.*;
import de.diegrafen.exmatrikulatortd.communication.server.responses.StartGameResponse;
import de.diegrafen.exmatrikulatortd.controller.gamelogic.LogicController;

/**
 * @author janro
 * @version 07.08.2019 00:54
 */
public class GameListener implements Listener {

    private GameServer gameServer;

    private LogicController logicController;

    GameListener(GameServer gameServer, LogicController logicController) {
        this.gameServer = gameServer;
        this.logicController = logicController;
    }

    @Override
    public void disconnected(Connection connection) {
        if (gameServer.isGameRunning()) {
            Gdx.app.postRunnable(logicController::gameConnectionLost);
            gameServer.setGameRunning(false);
            gameServer.getServer().close();
        }
    }

    @Override
    public void received(Connection connection, Object object) {
        if (object instanceof FinishedLoadingRequest) {
            handleFinishedLoadingRequest(connection);
        } else if (object instanceof BuildRequest) {
            handleBuildRequest((BuildRequest) object);
        } else if (object instanceof SellRequest) {
            handleSellRequest((SellRequest) object);
        } else if (object instanceof SendEnemyRequest) {
            handleSendEnemyRequest((SendEnemyRequest) object);
        } else if (object instanceof UpgradeRequest) {
            handleUpgradeRequest((UpgradeRequest) object);
        } else if (object instanceof GetServerStateRequest) {
            handleGetServerStateRequest(connection);
        }
    }

    private void handleFinishedLoadingRequest(final Connection connection) {
        System.out.println("FinishedLoadingRequest erhalten!");
        gameServer.registerClientAsFinishedLoading(connection.getID());
        System.err.println(connection.getID() + " ist bereit!");
        if (!gameServer.isGameRunning() & gameServer.haveAllPlayersFinishedLoading()) {
            gameServer.setGameRunning(true);
            gameServer.getServer().sendToAllTCP(new StartGameResponse());
            Gdx.app.postRunnable(() -> gameServer.getMainController().showScreen(logicController.getGameScreen()));
        }
    }

    /**
     * Fügt einen Request-Listener für das Bauen eines Turms hinzu und assoziiert ihn mit einem LogicController
     *
     * @param buildRequest f
     */
    private void handleBuildRequest(final BuildRequest buildRequest) {
        Gdx.app.postRunnable(() -> logicController.buildTower(buildRequest.getTowerType(), buildRequest.getxCoordinate(),
                buildRequest.getyCoordinate(), buildRequest.getPlayerNumber()));
    }

    /**
     * mainController.showScreen(logicController.getGameScreen());
     * Fügt einen Request-Listener für das Verkaufen eines Turms hinzu und assoziiert ihn mit einem LogicController
     *
     * @param sellRequest f
     */
    private void handleSellRequest(final SellRequest sellRequest) {
        Gdx.app.postRunnable(() -> logicController.sellTower(sellRequest.getxCoordinate(), sellRequest.getyCoordinate(), sellRequest.getPlayerNumber()));
    }

    /**
     * Fügt einen Request-Listener für das Senden eines Gegners hinzu und assoziiert ihn mit einem LogicController
     *
     * @param sendEnemyRequest f
     */
    private void handleSendEnemyRequest(final SendEnemyRequest sendEnemyRequest) {
        Gdx.app.postRunnable(() -> logicController.sendEnemy(sendEnemyRequest.getEnemyType(), sendEnemyRequest.getPlayerToSendTo(), sendEnemyRequest.getSendingPlayer()));
    }

    /**
     * Fügt einen Request-Listener für das Aufrüsten eines Turms hinzu und assoziiert ihn mit einem LogicController
     *
     * @param upgradeRequest f
     */
    private void handleUpgradeRequest(final UpgradeRequest upgradeRequest) {
        Gdx.app.postRunnable(() -> logicController.upgradeTower(upgradeRequest.getxCoordinate(), upgradeRequest.getyCoordinate(), upgradeRequest.getPlayerNumber()));
    }

    /**
     * Fügt einen Request-Listener für das Abrufen des Server-Spielzustandes hinzu und assoziiert ihn mit einem LogicController
     *
     * @param connection Der zu assoziierende LogicController
     */
    private void handleGetServerStateRequest(final Connection connection) {
        connection.sendTCP(logicController.getGamestate());
    }
}
