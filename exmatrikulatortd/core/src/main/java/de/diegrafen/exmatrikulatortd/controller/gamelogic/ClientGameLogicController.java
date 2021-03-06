package de.diegrafen.exmatrikulatortd.controller.gamelogic;

import de.diegrafen.exmatrikulatortd.communication.client.GameClient;
import de.diegrafen.exmatrikulatortd.controller.MainController;
import de.diegrafen.exmatrikulatortd.model.*;
import de.diegrafen.exmatrikulatortd.model.enemy.Enemy;
import de.diegrafen.exmatrikulatortd.model.tower.Tower;
import de.diegrafen.exmatrikulatortd.view.screens.GameView;

import java.util.LinkedList;
import java.util.List;

import static de.diegrafen.exmatrikulatortd.controller.factories.EnemyFactory.createNewEnemy;
import static de.diegrafen.exmatrikulatortd.controller.factories.TowerFactory.createNewTower;

/**
 * Spiellogik-Controller für Multiplayer-Spiele als Client
 *
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 15.06.2019 05:29
 */
public class ClientGameLogicController extends GameLogicController implements ClientLogicController {

    private List<Tower> towersToUpdate;

    private List<Player> playersToUpdate;

    private float timeUntilNextRoundToUpdate = 0;

    private boolean synchronize = false;

    /**
     * Der GameClient, über den die Netzwerkkommuikation abläuft
     */
    private final GameClient gameClient;

    /**
     * Konstruktor für den Spiellogik-Controller
     *
     * @param mainController Der Haupt-Controller der Anwendung
     * @param gameClient     Der GameClient, über den die Netzwerkkommunikation abläuft
     */
    public ClientGameLogicController(MainController mainController, int difficulty, int numberOfPlayers, int localPlayerNumber,
                                     int gamemode, GameView gameView, String mapPath, GameClient gameClient, String[] names) {
        super(mainController, difficulty, numberOfPlayers, localPlayerNumber, gamemode, gameView, mapPath, names);
        this.gameClient = gameClient;
        gameClient.attachResponseListeners(this);
        gameClient.reportFinishedLoading();
        this.towersToUpdate = new LinkedList<>();
        this.playersToUpdate = new LinkedList<>();
    }

    public ClientGameLogicController(MainController mainController, Gamestate gamestate, int allocatedPlayerNumber, GameView gameView, String mapPath, GameClient gameClient) {
        super(mainController, gamestate, gameView, allocatedPlayerNumber, true, mapPath);
        this.gameClient = gameClient;
        gameClient.attachResponseListeners(this);
        gameClient.reportFinishedLoading();
        this.towersToUpdate = new LinkedList<>();
        this.playersToUpdate = new LinkedList<>();
    }

    /**
     * @param deltaTime Die Zeit, die seit dem Rendern des letzten Frames vergangen ist
     */
    @Override
    public void update(float deltaTime) {
        if (synchronize) {
            synchronizeGame(towersToUpdate, playersToUpdate, timeUntilNextRoundToUpdate);
            synchronize = false;
            towersToUpdate.clear();
            playersToUpdate.clear();
            timeUntilNextRoundToUpdate = -1;
        }
        super.update(deltaTime);
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

    /**
     * Rüstet einen Turm auf. Da angenommen wird, dass die Prüfung des
     * Spielzuges bereits durch den Server erfolgt ist, wird nicht überprüft, ob der Spielzug legal ist.
     *
     * @param xCoordinate  Die x-Koordinate, an der sich der aufzurüstende Turm befindet
     * @param yCoordinate  Die y-Koordinate, an der sich der aufzurüstende Turm befindet
     * @param playerNumber Die Nummer der Spielerin, der der Turm gehört
     */
    @Override
    public void upgradeTowerFromServer(int xCoordinate, int yCoordinate, int playerNumber) {
        Coordinates mapCell = getMapCellByXandYCoordinates(xCoordinate, yCoordinate);
        Player owningPlayer = getGamestate().getPlayerByNumber(playerNumber);
        Tower tower = mapCell.getTower();
        owningPlayer.setResources(owningPlayer.getResources() - tower.getUpgradePrice());
        super.upgradeTower(tower);
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


    /**
     * Sendet einen Gegner von einer Spielerin an eine andere Spielerin. Da angenommen wird, dass die Prüfung des
     * Spielzuges bereits durch den Server erfolgt ist, wird nicht überprüft, ob der Spielzug legal ist.
     *
     * @param enemyType            Die Nummer des Gegnertyps
     * @param playerToSendToNumber Die Nummer der Spielerin, an die der Gegner geschickt wird
     * @param sendingPlayerNumber  Die Nummer der Spielerin, die den Turm geschickt hat
     */
    @Override
    public void sendEnemyFromServer(int enemyType, int playerToSendToNumber, int sendingPlayerNumber) {
        Enemy enemy = createNewEnemy(enemyType);
        Player sendingPlayer = getGamestate().getPlayerByNumber(sendingPlayerNumber);
        Player playerToSendTo = getGamestate().getPlayerByNumber(playerToSendToNumber);

        playerToSendTo.getWaves().get(getGamestate().getRoundNumber()).addEnemy(enemy);
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

    /**
     * Fügt dem Spiel einen Turm hinzu. Da angenommen wird, dass die Prüfung des Spielzuges bereits durch den Server
     * erfolgt ist, wird nicht überprüft, ob der Spielzug legal ist.
     *
     * @param towerType    Die Nummer des Turmtyps
     * @param xCoordinate  Die x-Koordinate, an der der Turm gebaut werden soll
     * @param yCoordinate  Die y-Koordinate, an der der Turm gebaut werden soll
     * @param playerNumber Die Nummer der Spielerin, die den Turm baut
     */
    @Override
    public void addTowerFromServer(int towerType, int xCoordinate, int yCoordinate, int playerNumber) {
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

    /**
     * Verkauft einen Turm. Da angenommen wird, dass die Prüfung des Spielzuges bereits durch den Server
     * erfolgt ist, wird nicht überprüft, ob der Spielzug legal ist.
     *
     * @param xCoordinate  Die x-Koordinate, an der sich der zu verkaufende Turm befindet
     * @param yCoordinate  Die y-Koordinate, an der sich der zu verkaufende Turm befindet
     * @param playerNumber Die Spielernummer der Turm-Eigentümerin
     */
    @Override
    public void sellTowerFromServer(int xCoordinate, int yCoordinate, int playerNumber) {
        Tower tower = getMapCellByXandYCoordinates(xCoordinate, yCoordinate).getTower();
        Player player = getGamestate().getPlayers().get(playerNumber);
        player.addToResources(tower.getSellPrice());
        player.notifyObserver();
        removeTower(tower);
    }

    /**
     * Beendet das Spiel
     */
    @Override
    public void exitGame(boolean saveBeforeExit) {
        super.exitGame(false);
        gameClient.shutdown();
    }

    @Override
    public void setGamestateFromServer(List<Tower> towers, List<Player> players, float timeUntilNextRound) {
        towersToUpdate = towers;
        playersToUpdate = players;
        timeUntilNextRoundToUpdate = timeUntilNextRound;
    }

    /**
     * Synchronisiert den Spielzustand mit empfangenen Serverinformationen
     *
     * @param towers Die Türme, die synchronisiert werden sollen
     *
     */
    private void synchronizeGame(List<Tower> towers, List<Player> players, float timeUntilNextRound) {
        getGamestate().setPlayers(players);
        getGamestate().setTimeUntilNextRound(timeUntilNextRound);
        removeAllTowers();
        towers.forEach(tower -> {
            int xCoordinate = getXCoordinateByPosition(tower.getxPosition());
            int yCoordinate = getYCoordinateByPosition(tower.getyPosition());
            addTower(tower, xCoordinate, yCoordinate, tower.getPlayerNumber());
            tower.notifyObserver();
        });
    }
}
