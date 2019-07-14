package de.diegrafen.exmatrikulatortd.controller.gamelogic;

import de.diegrafen.exmatrikulatortd.communication.client.GameClient;
import de.diegrafen.exmatrikulatortd.controller.MainController;
import de.diegrafen.exmatrikulatortd.controller.factories.TowerUpgrader;
import de.diegrafen.exmatrikulatortd.model.*;
import de.diegrafen.exmatrikulatortd.model.enemy.Enemy;
import de.diegrafen.exmatrikulatortd.model.tower.Tower;
import de.diegrafen.exmatrikulatortd.view.screens.GameView;

import static de.diegrafen.exmatrikulatortd.controller.factories.EnemyFactory.createNewEnemy;
import static de.diegrafen.exmatrikulatortd.controller.factories.TowerFactory.createNewTower;

/**
 * Spiellogik-Controller für Multiplayer-Spiele als Client
 *
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 15.06.2019 05:29
 */
public class ClientGameLogicController extends GameLogicController implements ClientLogicController {

    /**
     * Der GameClient, über den die Netzwerkkommuikation abläuft
     */
    private GameClient gameClient;

    /**
     * Konstruktor für den Spiellogik-Controller
     *
     * @param mainController Der Haupt-Controller der Anwendung
     * @param profile        Das Spieler-Profil
     * @param gameClient     Der GameClient, über den die Netzwerkkommunikation abläuft
     */
    public ClientGameLogicController(MainController mainController, Profile profile, int numberOfPlayers, int localPlayerNumber,
                                     int gamemode, GameView gameView, String mapPath, GameClient gameClient) {
        super(mainController, profile, numberOfPlayers, localPlayerNumber, gamemode, gameView, mapPath);
        this.gameClient = gameClient;
        gameClient.attachResponseListeners(this);
    }

    public ClientGameLogicController(MainController mainController, SaveState saveState, int allocatedPlayerNumber, GameView gameView, GameClient gameClient) {
        super(mainController, saveState, gameView);
        this.gameClient = gameClient;
        gameClient.attachResponseListeners(this);
        setLocalPlayerNumber(allocatedPlayerNumber);
    }


    /**
     * Rüstet einen Turm auf
     *
     * @param xCoordinate  Die x-Koordinate des Turms
     * @param yCoordinate  Die y-Koordinate des Turms
     * @param playerNumber Die Nummer der Spielerin, der der Turm gehört
     */
    @Override
    public void upgradeTower(int xCoordinate, int yCoordinate, int playerNumber) {
        gameClient.upgradeTower(xCoordinate, yCoordinate, playerNumber);
    }

    @Override
    public void upgradeTowerFromServer(int xCoordinate, int yCoordinate, int playerNumber) {
        Coordinates mapCell = getMapCellByXandYCoordinates(xCoordinate, yCoordinate);
        Player owningPlayer = getGamestate().getPlayerByNumber(playerNumber);
        Tower tower = mapCell.getTower();
        owningPlayer.setResources(owningPlayer.getResources() - tower.getUpgradePrice());
        TowerUpgrader.upgradeTower(tower);
        owningPlayer.notifyObserver();
        tower.notifyObserver();
    }

    /**
     * Schickt einen Gegner zum gegnerischen Spieler
     *
     * @param enemyType            Der Typ des zu schickenden Gegners
     */
    @Override
    public void sendEnemy(int enemyType, int playerToSendToNumber, int sendingPlayerNumber) {
        gameClient.sendEnemy(enemyType, playerToSendToNumber, sendingPlayerNumber);
    }

    @Override
    public void sendEnemyFromServer(int enemyType, int playerToSendToNumber, int sendingPlayerNumber) {
        Enemy enemy = createNewEnemy(enemyType);
        Player sendingPlayer = getGamestate().getPlayerByNumber(sendingPlayerNumber);
        Player playerToSendTo = getGamestate().getPlayerByNumber(playerToSendToNumber);

        playerToSendTo.getWaves().get(getGamestate().getRoundNumber() + 1).addEnemy(enemy);
        sendingPlayer.setResources(sendingPlayer.getResources() - enemy.getSendPrice());
        sendingPlayer.notifyObserver();
    }

    /**
     * Baut einen neuen Turm an den angegebenen Koordinaten auf der Karte
     *
     * @param towerType    Der Typ des zu bauenden Turms
     * @param xCoordinate  Die x-Koordinate der Stelle, an der der Turm gebaut werden soll
     * @param yCoordinate  Die y-Koordinate der Stelle, an der der Turm gebaut werden soll
     * @param playerNumber Die Nummer der Spielerin, die den Turm bauen will
     */
    @Override
    public void buildTower(int towerType, int xCoordinate, int yCoordinate, int playerNumber) {
        gameClient.buildTower(towerType, xCoordinate, yCoordinate, playerNumber);
    }

    @Override
    public void addTowerByServer(int towerType, int xCoordinate, int yCoordinate, int playerNumber) {
        Tower tower = createNewTower(towerType);
        int towerPrice = tower.getPrice();
        Player player = getGamestate().getPlayerByNumber(playerNumber);
        int playerResources = player.getResources();
        player.setResources(playerResources - towerPrice);
        addTower(tower, xCoordinate, yCoordinate, playerNumber);
        player.notifyObserver();
    }

    /**
     * Verkauft einen Turm
     *
     * @param xCoordinate  Die x-Koordinate des Turms
     * @param yCoordinate  Die y-Koordinate des Turms
     * @param playerNumber Die Nummer der Spielerin, der der Turm gehört
     */
    @Override
    public void sellTower(int xCoordinate, int yCoordinate, int playerNumber) {
        gameClient.sellTower(xCoordinate, yCoordinate, playerNumber);
    }

    @Override
    public void sellTowerByServer(int xCoordinate, int yCoordinate, int playerNumber) {
        Tower tower = getMapCellByXandYCoordinates(xCoordinate, yCoordinate).getTower();
        tower.getOwner().addToResources(tower.getSellPrice());
        tower.getOwner().notifyObserver();
        removeTower(tower);
    }

    /**
     * Holt den aktuellen Spielzustand vom Server
     */
    public void refreshLocalGameState() {

    }


    /**
     * Beendet das Spiel
     */
    public void exitGame() {
        gameClient.shutdown();
        super.exitGame(false);
    }
}
