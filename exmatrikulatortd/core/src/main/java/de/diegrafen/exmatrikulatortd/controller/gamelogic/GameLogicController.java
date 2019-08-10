package de.diegrafen.exmatrikulatortd.controller.gamelogic;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import de.diegrafen.exmatrikulatortd.communication.server.GameServer;
import de.diegrafen.exmatrikulatortd.controller.MainController;
import de.diegrafen.exmatrikulatortd.model.*;
import de.diegrafen.exmatrikulatortd.model.enemy.Enemy;
import de.diegrafen.exmatrikulatortd.model.tower.*;
import de.diegrafen.exmatrikulatortd.persistence.*;
import de.diegrafen.exmatrikulatortd.view.screens.GameView;

import java.util.*;

import static de.diegrafen.exmatrikulatortd.controller.factories.EnemyFactory.createNewEnemy;
import static de.diegrafen.exmatrikulatortd.controller.factories.NewGameFactory.*;

import static de.diegrafen.exmatrikulatortd.util.Constants.*;

/**
 * Der Standard-Spiellogik-Controller.
 *
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 15.06.2019 01:24
 */
public class GameLogicController implements LogicController {

    /**
     * Der Hauptcontroller
     */
    private final MainController mainController;

    /**
     * Der Spielzustand
     */
    private Gamestate gamestate;

    /**
     * Der Spielbildschirm
     */
    private final GameView gameScreen;

    /**
     * DAO für CRUD-Operationen mit Spielzustand-Objekten
     */
    private final GameStateDao gameStateDao;

    /**
     * DAO für CRUD-Operationen mit Spielstand-Objekten
     */
    private final SaveStateDao saveStateDao;

    private final TowerDao towerDao;

    private List<Tower> buildableTowers;

    private int localPlayerNumber;

    private final boolean multiplayer;

    private GameServer gameServer;

    private boolean server;

    private boolean pause;

    private final String mapPath;

    private boolean loaded = false;

    private final GameLogicUnit gameLogicUnit;

    /**
     *
     */
    public GameLogicController(MainController mainController, int difficulty, int numberOfPlayers, int localPlayerNumber,
                               int gamemode, GameView gameScreen, String mapPath, GameServer gameServer, String[] names) {
        this(mainController, difficulty, numberOfPlayers, localPlayerNumber, gamemode, gameScreen, mapPath, names);
        this.gameServer = gameServer;
        this.server = true;
        gameServer.attachRequestListeners(this);
        gameServer.serverFinishedLoading();
    }

    /**
     *
     */
    public GameLogicController(MainController mainController, int difficulty, int numberOfPlayers, int localPlayerNumber,
                               int gamemode, GameView gameScreen, String mapPath, String[] names) {
        this.mainController = mainController;
        this.mapPath = mapPath;
        this.gameStateDao = new GameStateDao();
        this.saveStateDao = new SaveStateDao();
        this.towerDao = new TowerDao();
        this.localPlayerNumber = localPlayerNumber;
        this.multiplayer = gamemode >= MULTIPLAYER_DUEL;
        this.gameScreen = gameScreen;
        this.gameScreen.setLogicController(this);

        this.gamestate = createGameState(gamemode, numberOfPlayers, difficulty, names);
        this.gameLogicUnit = new GameLogicUnit(this);
        this.gameScreen.setGameState(gamestate);
        this.gamestate.registerObserver(gameScreen);
        this.gamestate.getPlayers().forEach(player -> player.registerObserver(gameScreen));

        initializeMap(mapPath);

        this.gameScreen.loadMap(mapPath);
    }

    private Gamestate createGameState(int gamemode, int numberOfPlayers, int difficulty, String[] names) {
        // TODO: Informationen wie Spielerinnen-Name etc. müssen auch irgendwie berücksichtigt werden
        return createNewGame(gamemode, numberOfPlayers, difficulty, names);
    }

    public GameLogicController(MainController mainController, SaveState saveState, GameView gameView, GameServer gameServer) {
        this(mainController, saveState, gameView);
        this.gameServer = gameServer;
        this.server = true;
        gameServer.attachRequestListeners(this);
    }

    public GameLogicController(MainController mainController, SaveState saveState, GameView gameView) {
        this.gameStateDao = new GameStateDao();
        this.saveStateDao = new SaveStateDao();
        this.towerDao = new TowerDao();
        this.mainController = mainController;
        if (saveState.getGamestate() == null) {
            System.out.println("Kein Gamestate vorhanden?");
        }
        this.gamestate = gameStateDao.retrieve(saveState.getGamestate().getId());
        this.gameLogicUnit = new GameLogicUnit(this);
        this.mapPath = saveState.getMapPath();
        this.localPlayerNumber = saveState.getLocalPlayerNumber();
        this.multiplayer = saveState.isMultiplayer();
        this.gameScreen = gameView;
        this.loaded = true;
        this.gameScreen.setLogicController(this);
        this.gameScreen.setGameState(gamestate);
        this.gamestate.registerObserver(gameScreen);
        this.gamestate.getPlayers().forEach(player -> player.registerObserver(gameScreen));
        initializeMap(mapPath);

        this.gameScreen.loadMap(mapPath);

        reinitializeGame(this.gameScreen, this.gamestate);
    }

    @Override
    public void createTowerButtons(GameView gameView) {
        buildableTowers = towerDao.retrieveTemplateTowers();
        buildableTowers.forEach(gameView::addTowerButton);
    }

    /**
     * Reinitialisiert den Spielbildschirm nach dem Laden
     *
     * @param gameScreen Der Spielbildschirm, der reinitialisiert werden soll
     * @param gamestate  Der Spielzustand für die Reinitialisierung
     */
    private void reinitializeGame(GameView gameScreen, Gamestate gamestate) {
        gameScreen.clearGameObjects();
        gamestate.getProjectiles().forEach(projectile -> {
            gameScreen.addProjectile(projectile);
            projectile.notifyObserver();
        });
        gamestate.getTowers().forEach(tower -> {
            gameScreen.addTower(tower);
            tower.notifyObserver();
        });
        gamestate.getEnemies().forEach(enemy -> {
            gameScreen.addEnemy(enemy);
            enemy.notifyObserver();
        });
    }

    /**
     * @param deltaTime Die Zeit, die seit dem Rendern des letzten Frames vergangen ist
     */
    @Override
    public void update(float deltaTime) {
        float maxDelta = 1f / MIN_NUMBER_OF_UPDATES;
        if (loaded) {
            loaded = false;
            deltaTime = maxDelta;
        }
        if (deltaTime > maxDelta) {
            skipFrames(deltaTime, maxDelta);
        } else if (!gamestate.isGameOver() && !pause) {
            determineNewRound();
            determineGameOver();
            if (gamestate.isRoundEnded()) {
                determineGameOver();
            }
                if (!gamestate.isGameOver()) {
                    if (gamestate.isRoundEnded()) {
                        gamestate.setRoundEnded(false);
                        startNewRound();
                    }
                    gameLogicUnit.spawnWave(deltaTime);
                    gameLogicUnit.applyAuras(deltaTime, gamestate);
                    gameLogicUnit.applyMovement(deltaTime, gamestate);
                    gameLogicUnit.makeAttacks(deltaTime);
                    gameLogicUnit.moveProjectiles(deltaTime);
                    gameLogicUnit.applyBuffsToTowers(deltaTime);
                    gameLogicUnit.applyDebuffsToEnemies(deltaTime);
                    gameLogicUnit.applyAttackDelay(deltaTime);
                }
            }
        }

    /**
     * Führt, wenn deltaTime einen festgelegten Maximalwert überschreitet, mehrere Update-Zyklen hintereinander aus,
     * um den zeitlichen Abstand auszugleichen
     *
     * @param deltaTime Die Zeit seit dem letzten Rendern
     * @param maxDeltaTime Der Maximalwert, der zwischen zwei Updates liegen darf
     */
    private void skipFrames(float deltaTime, float maxDeltaTime){
        float remainingDeltaTime = deltaTime;
        while (remainingDeltaTime > maxDeltaTime) {
            update(maxDeltaTime);
            remainingDeltaTime -= maxDeltaTime;
        }
        update(remainingDeltaTime);
    }

    /**
     * Stellt fest, ob die Runde zuende ist
     */
    private void determineNewRound() {
        if (gamestate.getEnemies().isEmpty() && !gamestate.isNewRound()) {
            gamestate.setRoundEnded(true);
            if (gamestate.getRoundNumber() < gamestate.getNumberOfRounds()) {
                gamestate.setRoundNumber(gamestate.getRoundNumber() + 1);
            }

            gamestate.notifyObserver();
            System.out.println("Runde zuende!");
        }
    }

    /**
     * Ermittelt, ob das Spiel vorbei ist und setzt in diesem Fall das Attribut gameOver des GameStates auf true.
     * Ansonsten wird das Attribut auf false gesetzt.
     */
    private void determineGameOver() {
        boolean gameOver = false;

        determineLosers();

        if (!gamestate.isEndlessGame() && gamestate.getRoundNumber() >= gamestate.getNumberOfRounds()) {
            gameOver = true;
            for (Player player : gamestate.getPlayers()) {
                player.setVictorious(true);
            }
            System.out.println("Alle waren Sieger, obwohl einer nur gewinnen kann...");
        } else if (multiplayer && determineWinner() >= 0) {
            int victoriousPlayer = determineWinner();
            gamestate.getPlayerByNumber(victoriousPlayer).setVictorious(true);
            gameOver = true;
            System.out.println("Spielerin " + (victoriousPlayer + 1) + " hat gewonnen!");
        } else if (haveAllPlayersLost()) {
            gameOver = true;
            System.out.println("Alle Spielerinnen haben verloren!");
        }

        gamestate.setGameOver(gameOver);
        if(gamestate.isGameOver()){
            Player localPlayer = getLocalPlayer();
            HighscoreDao highscoreDao = mainController.getHighScoreDao();
            try {
                highscoreDao.create(new Highscore(mainController.getCurrentProfile(), localPlayer.getScore(), gamestate.getRoundNumber(), new Date()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            Highscore highscore = highscoreDao.findHighestScoreForProfile(mainController.getCurrentProfile());
            int highscoreValue;
            if (highscore != null) {
                highscoreValue = highscore.getScore();
            } else {
                highscoreValue = 0;
            }
            gameScreen.endOfGameScreen(localPlayer.isVictorious(), localPlayer.getScore(), highscoreValue);
        }
    }

    private void determineLosers() {

        for (Player player : gamestate.getPlayers()) {
            if (!player.hasLost() & player.getCurrentLives() <= 0) {
                player.setLost(true);
                System.out.println("Spielerin " + (player.getPlayerNumber() + 1) + " hat verloren!");
            }
        }
    }

    /**
     * Bestimmt, ob alle Spielerinnen verloren haben
     *
     * @return true, wenn alle Spielerinnen verloren haben, ansonsten false.
     */
    private boolean haveAllPlayersLost() {

        boolean allPlayersLost = true;

        for (Player player : gamestate.getPlayers()) {
            allPlayersLost &= player.hasLost();
        }
        return allPlayersLost;

    }

    /**
     * Bestimmt die Nummer der siegreichen Spielerin, sollte es eine solche geben
     *
     * @return Wenn es eine siegreiche Spielerin geben sollte, deren Spielernummer. Ansonsten -1
     */
    private int determineWinner() {

        int victoriousPlayer = -1;

        int numberOfPlayers = gamestate.getPlayers().size();

        for (Player player : gamestate.getPlayers()) {
            if (player.hasLost()) {
                numberOfPlayers -= 1;
            } else {
                victoriousPlayer = player.getPlayerNumber();
            }
        }

        if (numberOfPlayers != 1) {
            victoriousPlayer = -1;
        }

        return victoriousPlayer;
    }

    /**
     * Startet eine neue Runde
     */
    private void startNewRound() {
        System.out.println("New round started!");
        for (Player player : gamestate.getPlayers()) {
            player.setEnemiesSpawned(false);
        }
        if (server) {
            List<Tower> towersToSend = new LinkedList<>();
            gamestate.getTowers().forEach(tower -> towersToSend.add(new Tower(tower)));
            gameServer.sendServerGameState(towersToSend);
        }
        gamestate.setNewRound(true);
        gamestate.setRoundEnded(false);

        gamestate.setTimeUntilNextRound(TIME_BETWEEN_ROUNDS);
    }

    /**
     * Initialisiert die Kollisionsmatrix der Karte
     *
     * @param mapPath Der Dateipfad der zu ladenden Karte
     */
    public void initializeMap(String mapPath) {

        TiledMap tiledMap = new TmxMapLoader().load(mapPath);

        int numberOfColumns = (int) tiledMap.getProperties().get("width");
        int numberOfRows = (int) tiledMap.getProperties().get("height");
        int tileWidth = (int) tiledMap.getProperties().get("tilewidth");
        int tileHeight = (int) tiledMap.getProperties().get("tileheight");

        gamestate.setTileWidth(tileWidth);
        gamestate.setTileHeight(tileHeight);
        gamestate.setNumberOfColumns(numberOfColumns);
        gamestate.setNumberOfRows(numberOfRows);

        for (int playerNumber = 0; playerNumber < gamestate.getPlayers().size(); playerNumber++) {
            Player player = gamestate.getPlayerByNumber(playerNumber);
            TiledMapTileLayer waypointLayer = (TiledMapTileLayer) tiledMap.getLayers().get("WaypointsPlayer" + playerNumber);
            List<Coordinates> wayPoints = new LinkedList<>();
            for (int rowNumber = 0; rowNumber < numberOfRows; rowNumber++) {
                for (int columnNumber = 0; columnNumber < numberOfColumns; columnNumber++) {
                    if (waypointLayer.getCell(columnNumber, rowNumber) != null) {
                        int waypointIndex = (int) waypointLayer.getCell(columnNumber, rowNumber).getTile().getProperties().get("waypointNumber");
                        wayPoints.add(new Coordinates(columnNumber, rowNumber, playerNumber, waypointIndex, tileWidth, tileHeight));
                    }
                }
            }
            wayPoints.sort(Comparator.comparingInt(Coordinates::getWaypointIndex));
            player.setWayPoints(wayPoints);
        }

        TiledMapTileLayer buildPermissionLayer = (TiledMapTileLayer) tiledMap.getLayers().get("Kollisionsmatrix");

        for (int i = 0; i < numberOfRows; i++) {
            for (int j = 0; j < numberOfColumns; j++) {
                int buildableByPlayer = (int) buildPermissionLayer.getCell(j, i).getTile().getProperties().get("buildableByPlayer");
                addGameMapTile(j, i, buildableByPlayer, tileWidth, tileHeight);
            }
        }
    }

    /**
     * Fügt der im Spielzustand vorhandenen Karte ein neues Feld hinzu
     *
     * @param xCoordinate       Die x-Koordinate des Feldes auf der Karte
     * @param yCoordinate       Die y-Koordinate des Feldes auf der Karte
     * @param buildableByPlayer Die Nummer der Spielerin, die auf dem Feld bauen darf. -1, wenn das Feld nicht bebaubar ist
     */
    private void addGameMapTile(int xCoordinate, int yCoordinate, int buildableByPlayer, int tileWidth, int tileHeight) {
        Coordinates coordinates = new Coordinates(xCoordinate, yCoordinate, buildableByPlayer, tileWidth, tileHeight);
        gamestate.addCoordinatesToCollisionMatrix(coordinates);
    }

    /**
     * Gibt die y-Koordinate zurück, die der angegebenen x-Position auf der Karte entspricht
     *
     * @param xPosition Die x-Position, für die die x-Koordinate ermittelt werden soll
     * @return Die passende x-Koordinate
     */
    public int getXCoordinateByPosition(float xPosition) {
        return (int) xPosition / gamestate.getTileWidth();
    }

    /**
     * Gibt die y-Koordinate zurück, die der angegebenen y-Position auf der Karte entspricht
     *
     * @param yPosition Die y-Position, für die die y-Koordinate ermittelt werden soll
     * @return Die passende y-Koordinate
     */
    public int getYCoordinateByPosition(float yPosition) {
        return (int) yPosition / gamestate.getTileHeight();
    }

    Coordinates getMapCellByXandYCoordinates(int xCoordinate, int yCoordinate) {

        yCoordinate *= gamestate.getNumberOfColumns();

        return gamestate.getMapCellByListIndex(xCoordinate + yCoordinate);
    }


    /**
     *
     * @param tower
     * @param xCoordinate
     * @param yCoordinate
     * @param playerNumber
     */
    void addTower(Tower tower, int xCoordinate, int yCoordinate, int playerNumber) {
        tower.setPlayerNumber(playerNumber);

        Coordinates coordinates = getMapCellByXandYCoordinates(xCoordinate, yCoordinate);
        coordinates.setTower(tower);
        tower.setxPosition(xCoordinate * gamestate.getTileWidth());
        tower.setyPosition(yCoordinate * gamestate.getTileHeight());

        gamestate.addTower(tower);
        gameScreen.addTower(tower);
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

        if (isActiveMultiplayerRound()) {
            displayErrorMessage("Während der Runde darf nicht gebaut werden!", playerNumber);
            return;
        }

        if (checkIfCoordinatesAreBuildable(xCoordinate, yCoordinate, playerNumber)) {
            Tower tower = new Tower(buildableTowers.get(towerType));
            int towerPrice = tower.getPrice();
            Player player = gamestate.getPlayerByNumber(playerNumber);
            int playerResources = player.getResources();
            if (playerResources >= towerPrice) {
                if (server) {
                    gameServer.buildTower(towerType, xCoordinate, yCoordinate, playerNumber);
                }
                player.setResources(playerResources - towerPrice);
                addTower(tower, xCoordinate, yCoordinate, playerNumber);
                player.notifyObserver();

            } else {
                displayErrorMessage("Nicht genug Ressourcen!", playerNumber);
            }
        } else {
            displayErrorMessage("Hier kann nicht gebaut werden!", playerNumber);
        }
    }

    /**
     * Überprüft, ob ausgewählte Koordinaten vom angegebenen Spieler bebaut werden dürfen.
     *
     * @param xCoordinate Die x-Koordinate des betreffenden Feldes
     * @param yCoordinate Die y-Koordinate des betreffenden Feldes
     * @param playerNumber Die Nummer des betreffenden Spielers
     * @return true, wenn die betreffende Map-Zelle bebaut werden darf, ansonsten false
     */
    public boolean checkIfCoordinatesAreBuildable(int xCoordinate, int yCoordinate, int playerNumber) {

        boolean buildable = true;

        if (!coordinatesOnTheMap(xCoordinate, yCoordinate, gamestate)) {
            buildable = false;
        } else if (!playerExists(playerNumber, gamestate)) {
            buildable = false;
        } else {
            Coordinates mapCell = getMapCellByXandYCoordinates(xCoordinate, yCoordinate);
            if (mapCell.getTower() != null) {
                buildable = false;
            } else if (mapCell.getBuildableByPlayer() != playerNumber) {
                buildable = false;
            }
        }

        return buildable;
    }

    /**
     * Überprüft, ob sich Koordinaten auf dem Spielfeld befinden
     *
     * @param xCoordinate Die zu überprüfende x-Koordinate
     * @param yCoordinate Die zu überprüfende y-Koordinate
     * @param gamestate Der Spielzustand
     * @return true, wenn die Koordinaten auf dem Spielfeld sind, ansonsten false
     */
    private boolean coordinatesOnTheMap(int xCoordinate, int yCoordinate, Gamestate gamestate) {

        boolean onTheMap;

        if (xCoordinate < 0 | yCoordinate < 0) {
            onTheMap = false;
        } else {
            onTheMap = xCoordinate < gamestate.getNumberOfColumns() & yCoordinate < gamestate.getNumberOfRows();
        }

        return onTheMap;
    }

    /**
     * Überprüft, ob ein Spieler zu einer Spielernummer existiert
     * @param playerNumber
     * @param gamestate
     * @return
     */
    private boolean playerExists(int playerNumber, Gamestate gamestate) {
        return playerNumber >= 0 | gamestate.getPlayers().size() < playerNumber;
    }

    /**
     * Überprüft, ob sich auf einem Feld des Spielwelt ein Turm befindet
     *
     * @param xCoordinate
     * @param yCoordinate
     * @return
     */
    public boolean hasCellTower(int xCoordinate, int yCoordinate) {
        Coordinates coordinates = getMapCellByXandYCoordinates(xCoordinate, yCoordinate);

        return coordinates.getTower() != null;
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

        if (isActiveMultiplayerRound()) {
            displayErrorMessage("Während der Runde darf nicht verkauft werden!", playerNumber);
            return;
        }

        Coordinates mapCell = getMapCellByXandYCoordinates(xCoordinate, yCoordinate);

        Tower tower = mapCell.getTower();

        if (tower == null) {
            displayErrorMessage("Hier gibt es keinen Turm zum Verkaufen!", playerNumber);
        } else if (tower.getPlayerNumber() != playerNumber) {
            displayErrorMessage("Du darfst diesen Turm nicht verkaufen!", playerNumber);
        } else {
            Player player = gamestate.getPlayers().get(playerNumber);
            player.addToResources(tower.getSellPrice());
            player.notifyObserver();
            removeTower(tower);
            if (server) {
                gameServer.sellTower(xCoordinate, yCoordinate, playerNumber);
            }
        }
    }

    /**
     * Entfernt einen Turm aus dem Spiel
     *
     * @param tower Der zu entfernende Turm
     */
    void removeTower(Tower tower) {
        int xCoordinate = getXCoordinateByPosition(tower.getxPosition());
        int yCoordinate = getYCoordinateByPosition(tower.getyPosition());
        getMapCellByXandYCoordinates(xCoordinate, yCoordinate).setTower(null);
        gamestate.removeTower(tower);
        tower.setRemoved(true);
        tower.notifyObserver();
    }

    /**
     * Entfernt alle Türme aus dem Spiel
     */
    void removeAllTowers() {
        for (Tower tower : gamestate.getTowers()) {
            int xCoordinate = getXCoordinateByPosition(tower.getxPosition());
            int yCoordinate = getYCoordinateByPosition(tower.getyPosition());
            getMapCellByXandYCoordinates(xCoordinate, yCoordinate).setTower(null);
            tower.setRemoved(true);
            tower.notifyObserver();
        }
        gamestate.getTowers().clear();
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

        if (isActiveMultiplayerRound()) {
            displayErrorMessage("Während der Runde darf nicht aufgerüstet werden!", playerNumber);
            return;
        }

        Coordinates mapCell = getMapCellByXandYCoordinates(xCoordinate, yCoordinate);
        Player owningPlayer = gamestate.getPlayerByNumber(playerNumber);
        Tower tower = mapCell.getTower();

        if (tower.getPlayerNumber() != playerNumber) {
            displayErrorMessage("Du darfst nur eigene Türme aufrüsten!", playerNumber);
        } else if (owningPlayer.getResources() < tower.getUpgradePrice()) {
            displayErrorMessage("Du hast nicht genug Geld, um diesen Turm aufzurüsten!", playerNumber);
        } else if (tower.getUpgradeLevel() < tower.getMaxUpgradeLevel()){
            owningPlayer.setResources(owningPlayer.getResources() - tower.getUpgradePrice());
            upgradeTower(tower);
            owningPlayer.notifyObserver();
            tower.notifyObserver();
            if (server) {
                gameServer.upgradeTower(xCoordinate, yCoordinate, playerNumber);
            }

        }
        else {
            displayErrorMessage("Das maximale Upgradelevel wurde erreicht", playerNumber);
        }
    }

    /**
     * Schickt einen Gegner zu einem gegnerischen Spieler
     *
     * @param enemyType            Der Typ des zu schickenden Gegners
     * @param playerToSendToNumber Die Nummer der Spielerin, an die der Gegner geschickt werden soll
     * @param sendingPlayerNumber  Die Nummer des Spielers, der den Gegner sendet
     */
    @Override
    public void sendEnemy(int enemyType, int playerToSendToNumber, int sendingPlayerNumber) {

        if (isActiveMultiplayerRound()) {
            displayErrorMessage("Während der Runde dürfen keine Gegner geschickt werden!", sendingPlayerNumber);
            return;
        }

        if (!multiplayer) {
            return;
        }

        Enemy enemy = createNewEnemy(enemyType);
        Player sendingPlayer = gamestate.getPlayerByNumber(sendingPlayerNumber);
        if (sendingPlayer.getResources() >= enemy.getSendPrice()) {
            Player playerToSendTo = gamestate.getPlayerByNumber(playerToSendToNumber);
            if (playerToSendTo.getWaves().size() > gamestate.getRoundNumber()) {
                playerToSendTo.getWaves().get(gamestate.getRoundNumber()).addEnemy(enemy);
                sendingPlayer.setResources(sendingPlayer.getResources() - enemy.getSendPrice());
                sendingPlayer.notifyObserver();
                System.out.println("Enemy added!");
                if (server) {
                    gameServer.sendEnemy(enemyType, playerToSendToNumber, sendingPlayerNumber);
                }
            }
        } else {
            displayErrorMessage("Nicht genug Geld vorhanden, um diesen Gegner zu senden!", sendingPlayerNumber);
        }
    }

    /**
     * Gibt den Spielzustand zurück
     *
     * @return Der aktuelle Spielzustand
     */
    public Gamestate getGamestate() {
        return gamestate;
    }

    /**
     * Legt den Spielzustand fest
     *
     * @param gamestate Der festzulegende Spielzustand
     */
    public void setGamestate(Gamestate gamestate) {
        this.gamestate = gamestate;
    }

    /**
     * Zeigt eine Fehlermeldung für einen Spieler an. Sendet im Multiplayer-Medus die Fehlernachricht an den betreffenden
     * Spieler
     *
     * @param errorMessage Die Fehlermeldung
     * @param playerNumber Die Nummer des Spielers, bei dem der Fehler aufgetreten ist
     */
    @Override
    public void displayErrorMessage(String errorMessage, int playerNumber) {
        if (playerNumber == localPlayerNumber) {
            gameScreen.displayErrorMessage(errorMessage);
        } else {
            gameServer.sendErrorMessage(errorMessage, playerNumber);
        }
    }

    /**
     * Gibt den Spielbildschirm zurück
     * @return Der Spielbildschirm
     */
    public GameView getGameScreen() {
        return gameScreen;
    }

    /**
     * Speichert das Spiel
     *
     * @param saveGameName Der Name des Spielstandes
     */
    @Override
    public void saveGame(String saveGameName) {
        Gamestate newGameState = new Gamestate(gamestate);
        gameStateDao.create(newGameState);
        SaveState saveState = new SaveState(saveGameName, new Date(), multiplayer, mainController.getCurrentProfile(), newGameState, localPlayerNumber, mapPath);
        saveStateDao.create(saveState);
    }

    /**
     * Lädt ein Spiel
     * @param id
     */
    @Override
    public void loadGame(int id) {

    }

    /**
     * Rüstet einen Turm gemäß der in ihm festgelegten Upgrade-Wert auf.
     * @param tower Der aufzurüstende Turm
     */
    void upgradeTower(Tower tower) {
        tower.setUpgradeLevel(tower.getUpgradeLevel() + 1);
        tower.setBaseAttackDamage(tower.getBaseAttackDamage() + tower.getAttackDamageUpgradeBonus()); // tower.getAttackDamageUpgradeBonus()
        tower.setBaseAttackSpeed(tower.getBaseAttackSpeed() * tower.getAttackSpeedUpgradeMultiplier());
        tower.setAttackRange(tower.getAttackRange() + tower.getAttackRangeUpgradeBonus());
        tower.setAuraRange(tower.getAuraRange() + tower.getAuraRangeUpgradeBonus());
        tower.setUpgradePrice(tower.getUpgradePrice() * 2);
        tower.setSellPrice(tower.getUpgradePrice() * 2);
        tower.notifyObserver();
        System.out.println("Upgraded!");
    }

    /**
     * Beendet das Spiel
     *
     * @param saveBeforeExit Gibt an, ob das Spiel vorher gespeichert werden soll
     */
    public void exitGame(boolean saveBeforeExit) {
        gameScreen.dispose();
        if (saveBeforeExit) {
            saveGame("Blah!");
        }
        if (server) {
            gameServer.shutdown();
        }
        mainController.setEndScreen(gamestate);
    }

    /**
     * Gibt den lokalen Spieler zurück
     *
     * @return Der lokale Spieler
     */
    @Override
    public Player getLocalPlayer() {
        return gamestate.getPlayers().get(localPlayerNumber);
    }

    /**
     * Gibt die Nummer des lokalen Spielers zurück
     *
     * @return
     */
    public int getLocalPlayerNumber() {
        return localPlayerNumber;
    }

    void setLocalPlayerNumber(int localPlayerNumber) {
        this.localPlayerNumber = localPlayerNumber;
    }

    public boolean isPause() {
        return pause;
    }

    public void setPause(boolean pause) {
        this.pause = pause;
    }

    @Override
    public void gameConnectionLost() {
        exitGame(false);
    }

    @Override
    public boolean isActiveRound() {
        // TODO: Bestimmung, wann eine Runde aktiv ist, sollte vereinfacht werden
        return gamestate.getTimeUntilNextRound() < 0;
    }

    private boolean isActiveMultiplayerRound() {
        return multiplayer & isActiveRound();
    }

    @Override
    public boolean isMultiplayer() {
        return multiplayer;
    }
}
