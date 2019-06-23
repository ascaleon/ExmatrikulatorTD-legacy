package de.diegrafen.exmatrikulatortd.controller.gamelogic;

import com.badlogic.gdx.Gdx;
import de.diegrafen.exmatrikulatortd.controller.MainController;
import de.diegrafen.exmatrikulatortd.controller.factories.EnemyFactory;
import de.diegrafen.exmatrikulatortd.controller.factories.TowerFactory;
import de.diegrafen.exmatrikulatortd.controller.factories.TowerFactory.TowerType;
import de.diegrafen.exmatrikulatortd.model.Coordinates;
import de.diegrafen.exmatrikulatortd.model.Gamestate;
import de.diegrafen.exmatrikulatortd.model.Player;
import de.diegrafen.exmatrikulatortd.model.Profile;
import de.diegrafen.exmatrikulatortd.model.enemy.Enemy;
import de.diegrafen.exmatrikulatortd.model.enemy.Wave;
import de.diegrafen.exmatrikulatortd.model.tower.Tower;
import de.diegrafen.exmatrikulatortd.persistence.EnemyDao;
import de.diegrafen.exmatrikulatortd.persistence.GameStateDao;
import de.diegrafen.exmatrikulatortd.persistence.SaveStateDao;
import de.diegrafen.exmatrikulatortd.persistence.TowerDao;
import de.diegrafen.exmatrikulatortd.view.gameobjects.EnemyObject;
import de.diegrafen.exmatrikulatortd.view.gameobjects.TowerObject;
import de.diegrafen.exmatrikulatortd.view.screens.GameScreen;

import java.awt.geom.Point2D;
import java.util.*;

import static de.diegrafen.exmatrikulatortd.controller.factories.EnemyFactory.EnemyType.REGULAR_ENEMY;
import static de.diegrafen.exmatrikulatortd.controller.factories.EnemyFactory.createNewEnemy;
import static de.diegrafen.exmatrikulatortd.controller.factories.TowerFactory.TowerType.REGULAR_TOWER;
import static de.diegrafen.exmatrikulatortd.controller.factories.TowerFactory.createNewTower;
import static de.diegrafen.exmatrikulatortd.util.Constants.*;
import static de.diegrafen.exmatrikulatortd.util.HibernateUtils.getSessionFactory;

/**
 *
 * Der Standard-Spiellogik-Controller
 *
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 15.06.2019 01:24
 */
public class GameLogicController implements LogicController {

    /**
     * Der Hauptcontroller
     */
    private MainController mainController;

    /**
     * Der Spielzustand
     */
    private Gamestate gamestate;

    /**
     * Der Spielbildschirm
     */
    private GameScreen gameScreen;

    /**
     * Das lokale Spieler-Profil
     */
    private Profile profile;

    /**
     * DAO-Objekt für CRUD-Operationen mit Spielzustand-Objekten
     */
    private GameStateDao gameStateDao;

    /**
     * DAO-Objekt für CRUD-Operationen mit Spielstand-Objekten
     */
    private SaveStateDao saveStateDao;

    private EnemyDao enemyDao;

    private TowerDao towerDao;

    


    public GameLogicController (MainController mainController, Gamestate gamestate, Profile profile) {
        this.mainController = mainController;
        this.gamestate = gamestate;
        this.profile = profile;
        this.gameStateDao = new GameStateDao();
        this.saveStateDao = new SaveStateDao();
        this.enemyDao = new EnemyDao();
        this.towerDao = new TowerDao();
        gamestate.addPlayer(new Player());
        gamestate.setLocalPlayerNumber(0);
        gameStateDao.create(gamestate);
        initializeCollisionMap(TEST_COLLISION_MAP);
    }

    @Override
    public void update(float deltaTime) {
        if (!determineGameOver()) {
            if (gamestate.getRoundNumber() < gamestate.getNumberOfRounds()) {
                determineNewRound();
                if (gamestate.isRoundEnded()) {
                    gamestate.setRoundEnded(false);
                    System.out.println("Runde zuende!");
                    startNewRound();
                }
            }
            //applyPlayerDamage();
            spawnWave(deltaTime);
            applyMovement(deltaTime);
            makeAttacks(deltaTime);
        }
    }

    /**
     * Wendet Auras auf die Objekte des Spiels an
     * @param deltaTime
     */
    void applyAuras (float deltaTime) {

    }

    /**
     * Wendet Buffs und Debuffs auf die Objekte des Spiels an
     * @param deltaTime
     */
    void applyBuffsAndDebuffs (float deltaTime) {

    }


    /**
     * Bewegt die Einheiten
     * @param deltaTime
     */
    void applyMovement (float deltaTime) {
        for (Enemy enemy : gamestate.getEnemies()) {
            if (Math.floor(getDistanceToNextPoint(enemy)) <= 3) {
                enemy.incrementWayPointIndex();
            }
            if (enemy.getWayPointIndex() >= enemy.getAttackedPlayer().getWayPoints().size()) {
                applyDamage(enemy);
                if (enemy.isRespawning()) {
                    enemy.setRemoved(false);
                    enemy.setWayPointIndex(0);
                    enemy.setToStartPosition();
                    addEnemy(enemy);
                }
                break;
            }

            enemy.moveInTargetDirection(deltaTime);

            if (enemy.getCurrentMapCell() != null) {
                enemy.getCurrentMapCell().removeFromEnemiesOnCell(enemy);
            }

            Coordinates newCell = getMapCellByXandY((int) enemy.getxPosition(), (int) enemy.getyPosition());
            enemy.setCurrentMapCell(newCell);
            newCell.addToEnemiesOnCell(enemy);
        }
    }

    /**
     * Lässt die Türme angreifen
     * @param deltaTime
     */
    private void makeAttacks (float deltaTime) {

        for (Tower tower : gamestate.getTowers()) {

            if (tower.getCooldown() > 0) {
                tower.setCooldown(tower.getCooldown() - deltaTime);
            }

            Enemy newTarget;

            if (!targetInRange(tower)) {
                tower.setCurrentTarget(null);
                float timeSinceLastSearch = tower.getTimeSinceLastSearch();
                if (timeSinceLastSearch >= SEARCH_TARGET_INTERVAL) {
                    List<Coordinates> visited = new LinkedList<>();
                    Coordinates startCoordinates = tower.getPosition();
                    LinkedList<Coordinates> cellsToVisit = new LinkedList<>();
                    newTarget = searchTarget(startCoordinates, visited, cellsToVisit, tower);
                    tower.setCurrentTarget(newTarget);
                    tower.setTimeSinceLastSearch(0);
                } else {
                    tower.setTimeSinceLastSearch(timeSinceLastSearch + deltaTime);
                }
            }

            if (tower.getCurrentTarget() != null) {
                letTowerAttack(tower);
            }
        }
    }

    private void letTowerAttack (Tower tower) {
        Enemy enemy = tower.getCurrentTarget();
        if (tower.getCooldown() <= 0) {
            enemy.setCurrentHitPoints(enemy.getCurrentHitPoints() - tower.getAttackDamage());
            tower.setCooldown(tower.getAttackSpeed());
        }

        if (enemy.getCurrentHitPoints() <= 0) {
            removeEnemy(enemy);
            tower.setCurrentTarget(null);
        }
    }

    private Enemy searchTarget(Coordinates startCoordinates, List<Coordinates> visited, LinkedList<Coordinates> cellsToVisit, Tower tower) {

        Enemy enemyToReturn = null;

        visited.add(startCoordinates);
        cellsToVisit.push(startCoordinates);

        while (!cellsToVisit.isEmpty()) {
            Coordinates currentCell = cellsToVisit.pop();

            List<Enemy> enemiesInCell = currentCell.getEnemiesInMapCell();
            if (!enemiesInCell.isEmpty()) {
                enemyToReturn = enemiesInCell.get(0);
                break;
            }
            for (Coordinates neighbour : currentCell.getNeighbours()) {
                if (!visited.contains(neighbour)) {
                    visited.add(neighbour);
                    if (isCellInRangeOfTower(tower, neighbour)) {
                        cellsToVisit.push(neighbour);
                    }
                }
            }
        }

        return enemyToReturn;
    }

    /**
     * Fügt Spielern Schaden zu
     */
    void applyPlayerDamage () {
        ArrayList<Enemy> enemiesToRemove = new ArrayList<Enemy>();
        for (Enemy enemy : gamestate.getEnemies()) {
            if (getDistanceToEndpoint(enemy) < TILE_SIZE / 2 ) {
                enemiesToRemove.add(enemy);
                System.out.println("Damage applied!");
            }
        }

        for (Enemy enemy : enemiesToRemove) {
            applyDamage(enemy);
        }
    }

    private void applyDamage(Enemy enemy) {
        Player attackedPlayer = enemy.getAttackedPlayer();
        attackedPlayer.setCurrentLives(attackedPlayer.getCurrentLives() - enemy.getAmountOfDamageToPlayer());
        removeEnemy(enemy);
    }

    private void removeEnemy(Enemy enemy) {
        enemy.getAttackedPlayer().removeEnemy(enemy);
        enemy.setAttackedPlayer(null);
        enemy.getCurrentMapCell().removeFromEnemiesOnCell(enemy);
        enemy.setCurrentMapCell(null);
        gamestate.removeEnemy(enemy);
        enemy.setGameState(null);

        enemy.setRemoved(true);
    }

    private float getDistanceToEndpoint (Enemy enemy) {
        float x1 = enemy.getxPosition();
        float x2 = enemy.getEndXPosition();
        float y1 = enemy.getyPosition();
        float y2 = enemy.getEndYPosition();
        return (float) Point2D.distance(x1, y1, x2, y2);
    }

    private float getDistanceToNextPoint (Enemy enemy) {
        float x1 = enemy.getxPosition();
        float x2 = enemy.getTargetxPosition();
        float y1 = enemy.getyPosition();
        float y2 = enemy.getTargetyPosition();
        return (float) Point2D.distance(x1, y1, x2, y2);
    }

    private boolean isCellInRangeOfTower (Tower tower, Coordinates coordinates) {

        Coordinates towerCoordinates = tower.getPosition();

        float x1 = towerCoordinates.getXCoordinate() * TILE_SIZE;
        float x2 = coordinates.getXCoordinate() * TILE_SIZE;
        float y1 = towerCoordinates.getXCoordinate() * TILE_SIZE;
        float y2 = coordinates.getYCoordinate() * TILE_SIZE;
        float distance = (float) Point2D.distance(x1, y1, x2, y2);
        return tower.getAttackRange() >= distance;
    }

    private boolean targetInRange(Tower tower) {

        Enemy target = tower.getCurrentTarget();

        return target != null && isCellInRangeOfTower(tower, target.getCurrentMapCell());
    }

    /**
     * Spawnt die nächste Angriffswelle
     */
    void spawnWave (float deltaTime) {
        float timeUntilNextRound = gamestate.getTimeUntilNextRound();
        if (timeUntilNextRound <= 0) {

            int roundNumber = gamestate.getRoundNumber();

            for (Player player : gamestate.getPlayers()) {
                if (player.getWaves().get(roundNumber).getEnemies().size() == 0) {
                    player.setEnemiesSpawned(true);
                    // TODO: Mehrspieler*innen-Szenario berücksichtigen
                    gamestate.setNewRound(false);
                }

                if (!player.isEnemiesSpawned() && player.getTimeSinceLastSpawn() > TIME_BETWEEN_SPAWNS) {
                    Enemy enemy = player.getWaves().get(roundNumber).getEnemies().get(0);
                    // TODO: Replace with .pop()
                    player.getWaves().get(roundNumber).getEnemies().remove(0);
                    addEnemy(enemy);
                    player.setTimeSinceLastSpawn(0);
                } else {
                    player.setTimeSinceLastSpawn(player.getTimeSinceLastSpawn() + deltaTime);
                }
            }
        }

        gamestate.setTimeUntilNextRound(timeUntilNextRound - deltaTime);
    }

    /**
     * Stellt fest, ob die Runde zuende ist
     */
    private void determineNewRound () {
        if (gamestate.getEnemies().isEmpty() && !gamestate.isNewRound()) {
            gamestate.setRoundEnded(true);
            gamestate.setRoundNumber(gamestate.getRoundNumber() + 1);
            gameStateDao.update(gamestate);
        }
    }

    private boolean determineGameOver() {
        boolean gameOver = true;
        for (Player player : gamestate.getPlayers()) {
            if (!player.getWaves().isEmpty()) {
                gameOver = false;
            }
        }
        gamestate.setGameOver(gameOver);
        return gameOver;
    }

    /**
     * Startet eine neue Runde
     */
    void startNewRound () {
        System.out.println("New round started!");
        gamestate.setNewRound(true);
        gamestate.setRoundEnded(false);
        for (Player player : gamestate.getPlayers()) {
            player.setEnemiesSpawned(false);
        }
        gamestate.setTimeUntilNextRound(TIME_BETWEEN_ROUNDS);
    }

    /**
     * Initialisiert die Kollisionsmatrix der Map
     */
    void initializeCollisionMap (int[][] collisionMap) {

        int numberOfColumns = gamestate.getNumberOfColumns(); //gamestate.getMapHeight() / TILE_SIZE;
        int numberOfRows = gamestate.getNumberOfRows(); //gamestate.getMapWidth() / TILE_SIZE;

        //gamestate.setNumberOfColumns(numberOfColumns);
        //gamestate.setNumberOfRows(numberOfRows);
        for (int i = 0; i < numberOfColumns; i++) {
            for (int j = 0; j < numberOfRows; j++) {
                System.out.println("Column: " + i + ", Row: " + j);
                addGameMapTile(i, j, collisionMap[i][j]);
            }
        }

        for (Coordinates mapCell : gamestate.getCollisionMatrix()) {

            int mapCellXCoordinate = mapCell.getxCoordinate();
            int mapCellYCoordinate = mapCell.getyCoordinate();

            int numberOfCols = gamestate.getNumberOfColumns();
            if (mapCellXCoordinate > 0) {
                mapCell.addNeighbour(gamestate.getMapCellByListIndex(numberOfCols * (mapCellXCoordinate - 1) + mapCellYCoordinate));
            }
            if (mapCellXCoordinate < numberOfColumns - 1 ) {
                mapCell.addNeighbour(gamestate.getMapCellByListIndex(numberOfCols * (mapCellXCoordinate + 1) + mapCellYCoordinate));
            }
            if (mapCellYCoordinate > 0) {
                mapCell.addNeighbour(gamestate.getMapCellByListIndex(numberOfCols * mapCellXCoordinate + mapCellYCoordinate - 1));
            }
            if (mapCellYCoordinate < numberOfColumns - 1 ) {
                mapCell.addNeighbour(gamestate.getMapCellByListIndex(numberOfCols * mapCellXCoordinate + mapCellYCoordinate + 1));
            }
        }

        gameStateDao.update(gamestate);

    }

    private void addGameMapTile (int xCoordinate, int yCoordinate, int buildableByPlayer) {
        Coordinates coordinates = new Coordinates(xCoordinate, yCoordinate, buildableByPlayer);
        gamestate.addCoordinatesToCollisionMatrix(coordinates);
    }

    private Coordinates getMapCellByXandY (int xPosition, int yPosition) {
        int xCoordinate = xPosition / TILE_SIZE;
        int yCoordinate = yPosition / TILE_SIZE;

        //System.out.println("x-Koordinate: " + xCoordinate);
        //System.out.println("y-Koordinate: " + yCoordinate);

        int xIndex = gamestate.getNumberOfColumns() * xCoordinate;

        try {
            return gamestate.getMapCellByListIndex(xIndex + yCoordinate);
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
            return gamestate.getMapCellByListIndex(50);
        }


    }

    public void addEnemy (Enemy enemy) {
        Player attackedPlayer = gamestate.getPlayerByNumber(0);
        attackedPlayer.addEnemy(enemy);
        enemy.setAttackedPlayer(attackedPlayer);
        gamestate.addEnemy(enemy);
        enemy.setGameState(gamestate);
        enemy.setToStartPosition();
        gameScreen.addEnemy(enemy);

        //enemyDao.update(enemy);
    }

    public void addTower (Tower tower, int xPosition, int yPosition) {
        Player owningPlayer = gamestate.getPlayerByNumber(0);
        tower.setOwner(owningPlayer);
        owningPlayer.addTower(tower);

        Coordinates coordinates = getMapCellByXandY(xPosition, yPosition);
        tower.setPosition(coordinates);
        coordinates.setTower(tower);

        tower.setGamestate(gamestate);
        gamestate.addTower(tower);
        gameScreen.addTower(tower);

        //towerDao.create(tower);
    }

    /**
     * Baut einen neuen Turm
     *
     * @param towerType       Der zu bauende Turm
     * @param coordinates Die Koordinaten des Turmes
     * @return Wenn das Bauen erfolgreich war, true, ansonsten false
     */
    public boolean buildTower(TowerType towerType, int xPosition, int yPosition, int playerNumber) {

        boolean wasSuccessful;

        if (checkCoordinates(xPosition, yPosition, playerNumber)) {
                Tower tower = createNewTower(towerType);
                addTower(tower, xPosition, yPosition);
                wasSuccessful = true;
            } else {
                wasSuccessful = false;
            }

            return wasSuccessful;
        }

    public boolean buildRegularTower(final int mapX, final int mapY) {
        return buildTower(REGULAR_TOWER, mapX, mapY, 0);
    }

    private boolean checkCoordinates (int xPosition, int yPosition, int playerNumber) {

        boolean isBuildable = true;

        int xIndex = xPosition / gamestate.getTileSize();

        int yIndex = yPosition / gamestate.getTileSize();

        if (xIndex >= gamestate.getNumberOfColumns() ||
                yIndex >= gamestate.getNumberOfRows()) {
            System.err.println("Es nicht erlaubt, außerhalb des Spielfelds zu bauen!");
           return false;
        }

        Coordinates mapCell = getMapCellByXandY(xPosition, yPosition);

        if (mapCell.getTower() != null) {
            isBuildable = false;
        } else if (mapCell.getBuildableByPlayer() != playerNumber) {
            isBuildable = false;
        }

        return isBuildable;
    }

    /**
     * Verkauft einen Turm
     *
     * @param tower Der zu verkaufende Turm
     * @return Wenn das Verkaufen erfolgreich war, true, ansonsten false
     */
    @Override
    public boolean sellTower(Tower tower) {
        return false;
    }

    public boolean sellTower(int screenX, int screenY, int playerNumber) {

        boolean wasSuccessful = false;

        if (screenX / gamestate.getTileSize() >= gamestate.getNumberOfColumns() ||
                screenY / gamestate.getTileSize() >= gamestate.getNumberOfRows()
        ) {
            System.err.println("Du hast außerhalb des Spielfeldes geklickt!");
            return false;
        }

        Coordinates mapCell = getMapCellByXandY(screenX, screenY);

        Tower tower = mapCell.getTower();

        if (tower == null ) {
            System.err.println("Hier gibt es keinen Turm zum Verkaufen!");
        } else if (tower.getOwner().getPlayerNumber() != playerNumber) {
            System.err.println("Du darfst diesen Turm nicht verkaufen!");
        } else {
            tower.getOwner().addToResources(tower.getSellPrice());
            tower.setRemoved(true);
            removeTower(tower);
            wasSuccessful = true;
        }

        return wasSuccessful;
    }

    private void removeTower(Tower tower) {
        tower.getOwner().removeTower(tower);
        tower.getPosition().setTower(null);
        gamestate.getTowers().remove(tower);
        tower.setRemoved(true);
    }

    /**
     * Rüstet einen Turm auf
     *
     * @param tower Der zu aufzurüstende Turm
     * @return Wenn das Aufrüsten erfolgreich war, true, ansonsten false
     */
    @Override
    public boolean upgradeTower(Tower tower) {
        return false;
    }

    /**
     * Schickt einen Gegner zum gegnerischen Spieler
     *
     * @param enemy Der zu schickende Gegner
     * @return Wenn das Schicken erfolgreich war, true, ansonsten false
     */
    @Override
    public boolean sendEnemy(Enemy enemy) {
        return false;
    }

    /**
     * Gibt den Spielzustand zurück
     * @return
     */
    public Gamestate getGamestate() {
        return gamestate;
    }

    /**
     * Legt den Spielzustand fest
     * @param gamestate Der festzulegende Spielzustand
     */
    public void setGamestate(Gamestate gamestate) {
        this.gamestate = gamestate;
    }

    /**
     * Gibt
     * @return
     */
    public GameScreen getGameScreen() {
        return gameScreen;
    }

    public void setGameScreen(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
    }

    /**
     * Beendet das Spiel
     * @param saveBeforeExit Gibt an, ob das Spiel vorher gespeichert werden soll
     */
    public void exitGame (boolean saveBeforeExit) {
        mainController.setEndScreen(gamestate);
    }

    @Override
    public boolean buildTower(Tower tower, Coordinates coordinates) {
        return false;
    }


}
