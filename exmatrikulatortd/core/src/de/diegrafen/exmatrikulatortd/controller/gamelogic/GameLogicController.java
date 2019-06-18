package de.diegrafen.exmatrikulatortd.controller.gamelogic;

import de.diegrafen.exmatrikulatortd.controller.MainController;
import de.diegrafen.exmatrikulatortd.controller.factories.EnemyFactory;
import de.diegrafen.exmatrikulatortd.controller.factories.TowerFactory;
import de.diegrafen.exmatrikulatortd.controller.factories.TowerFactory.TowerType;
import de.diegrafen.exmatrikulatortd.model.Coordinates;
import de.diegrafen.exmatrikulatortd.model.Gamestate;
import de.diegrafen.exmatrikulatortd.model.Player;
import de.diegrafen.exmatrikulatortd.model.Profile;
import de.diegrafen.exmatrikulatortd.model.enemy.Enemy;
import de.diegrafen.exmatrikulatortd.model.tower.Tower;
import de.diegrafen.exmatrikulatortd.persistence.EnemyDao;
import de.diegrafen.exmatrikulatortd.persistence.GameStateDao;
import de.diegrafen.exmatrikulatortd.persistence.SaveStateDao;
import de.diegrafen.exmatrikulatortd.view.gameobjects.EnemyObject;
import de.diegrafen.exmatrikulatortd.view.gameobjects.TowerObject;
import de.diegrafen.exmatrikulatortd.view.screens.GameScreen;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static de.diegrafen.exmatrikulatortd.controller.factories.TowerFactory.TowerType.REGULAR_TOWER;
import static de.diegrafen.exmatrikulatortd.controller.factories.TowerFactory.createNewTower;
import static de.diegrafen.exmatrikulatortd.util.Constants.TEST_COLLISION_MAP;
import static de.diegrafen.exmatrikulatortd.util.Constants.TILE_SIZE;
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

    /**
     * Bildet Turm-Objekte auf ihre grafische Repräsentation ab
     */
    private HashMap<Tower, TowerObject> towerMapping;

    /**
     * Bildet Gegner-Objekte auf ihre grafische Repräsentation ab
     */
    private HashMap<Enemy, EnemyObject> enemyMapping;

    public GameLogicController (MainController mainController, Gamestate gamestate, Profile profile) {
        this.mainController = mainController;
        this.gamestate = gamestate;
        this.profile = profile;
        this.gameStateDao = new GameStateDao();
        this.saveStateDao = new SaveStateDao();
        this.enemyDao = new EnemyDao();
        gameStateDao.create(gamestate);
        enemyMapping = new HashMap<Enemy, EnemyObject>();
        towerMapping = new HashMap<Tower, TowerObject>();
        gamestate.addPlayer(new Player());
        gamestate.setLocalPlayerNumber(0);
        initializeCollisionMap(TEST_COLLISION_MAP);
    }

    @Override
    public void update(float deltaTime) {
        //applyPlayerDamage();
        applyMovement(deltaTime);
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
            //System.out.println(getDistanceToNextPoint(enemy) - gamestate.getTileSize());
            //System.out.println(Math.floor(getDistanceToNextPoint(enemy)));
            if (Math.floor(getDistanceToNextPoint(enemy)) == 0) {
                enemy.incrementWayPointIndex();
            }
            if (enemy.getWayPointIndex() >= enemy.getAttackedPlayer().getWayPoints().size()) {
                applyDamage(enemy);
                //System.out.println("Hello?");
                break;
            }
            enemy.moveInTargetDirection(deltaTime);
            EnemyObject enemyObject = enemyMapping.get(enemy);
            enemyObject.setNewPosition(enemy.getxPosition(), enemy.getyPosition());
        }
    }

    /**
     * Lässt die Türme angreifen
     * @param deltaTime
     */
    void makeAttacks (float deltaTime) {

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
        gamestate.removeEnemy(enemy);
        gameScreen.removeEnemy(enemyMapping.get(enemy));
        enemyMapping.remove(enemy);
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

    /**
     * Spawnt die nächste Angriffswelle
     */
    void spawnWave () {

    }

    /**
     * Stellt fest, ob die Runde zuende ist
     */
    boolean hasRoundEnded () {
        return false;
    }

    /**
     * Startet eine neue Runde
     */
    void startNewRound () {

    }

    /**
     * Initialisiert die Kollisionsmatrix der Map
     */
    void initializeCollisionMap (int[][] collisionMap) {
        gamestate.setNumberOfColumns(gamestate.getMapHeight() / TILE_SIZE);
        gamestate.setNumberOfRows(gamestate.getMapWidth() / TILE_SIZE);
        for (int i = 0; i < gamestate.getNumberOfColumns(); i++) {
            for (int j = 0; j < gamestate.getNumberOfRows(); j++) {
                addGameMapTile(i, j, collisionMap[i][j]);
            }
        }
    }

    private void addGameMapTile (int xCoordinate, int yCoordinate, int buildableByPlayer) {
        Coordinates coordinates = new Coordinates(xCoordinate, yCoordinate, buildableByPlayer);
        gamestate.addCoordinatesToCollisionMatrix(coordinates);
    }

    private Coordinates getMapCellByXandY (int xPosition, int yPosition) {
        int xCoordinate = xPosition / TILE_SIZE;
        int yCoordinate = yPosition / TILE_SIZE;

        int xIndex = gamestate.getNumberOfColumns() * xCoordinate;

        return gamestate.getMapCellByListIndex(xIndex + yCoordinate);
    }

    public void addEnemy (EnemyFactory.EnemyType enemyType) {

    }

    public void addEnemy (Enemy enemy) {
        Player attackedPlayer = gamestate.getPlayerByNumber(0);
        attackedPlayer.addEnemy(enemy);
        enemy.setAttackedPlayer(attackedPlayer);
        gamestate.addEnemy(enemy);
        enemy.setGameState(gamestate);
        enemy.setToStartPosition();


        EnemyObject enemyObject = new EnemyObject(enemy);
        gameScreen.addEnemy(enemyObject);

        enemyDao.create(enemy);
        enemyMapping.put(enemy, enemyObject);
        System.out.println("Enemy added!");
        System.out.println(enemyMapping.toString());
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

        TowerObject towerObject = new TowerObject(tower);
        gameScreen.addTower(towerObject);
        towerMapping.put(tower, towerObject);

        System.out.println("Tower added!");
        System.out.println(towerMapping.toString());
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
            //if (true) {
                //Tower tower = createNewTower(towerType, xPosition, yPosition);
                Tower tower = createNewTower(towerType);
                //System.out.println(coordinates.toString());

                addTower(tower, xPosition, yPosition);
                wasSuccessful = true;
            } else {
                wasSuccessful = false;
            }

            return wasSuccessful;
        }

    public boolean buildRegularTower(int screenX, int screenY) {
        //Coordinates coordinates = new Coordinates(screenX / TILE_SIZE, screenY / TILE_SIZE);
        return buildTower(REGULAR_TOWER, screenX, screenY, 0);
    }

    private boolean checkCoordinates (int xPosition, int yPosition, int playerNumber) {

        boolean isBuildable = true;

        int xIndex = xPosition / gamestate.getTileSize();

        int yIndex = yPosition / gamestate.getTileSize();

        System.out.println("xIndex: " + xIndex);

        System.out.println("yIndex: " + yIndex);

        if (xIndex >= gamestate.getNumberOfColumns() ||
                yIndex >= gamestate.getNumberOfRows()) {
            System.err.println("Es nicht erlaubt, außerhalb des Spielfelds zu bauen!");
           return false;
        }

        Coordinates mapCell;

        mapCell = getMapCellByXandY(xPosition, yPosition);

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
            removeTower(tower);
            wasSuccessful = true;
        }

        return wasSuccessful;
    }

    private void removeTower(Tower tower) {
        tower.getOwner().removeTower(tower);
        tower.getPosition().setTower(null);
        gamestate.getTowers().remove(tower);
        gameScreen.getTowers().remove(towerMapping.get(tower));
        towerMapping.remove(tower);
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






    /**
     * Baut einen neuen Turm
     *
     * @param towerType       Der zu bauende Turm
     * @param coordinates Die Koordinaten des Turmes
     * @return Wenn das Bauen erfolgreich war, true, ansonsten false
     */
    public boolean buildTower(TowerType towerType, Coordinates coordinates) {

        return true;

//        boolean wasSuccessful;
//
//        //if (gamestate.checkCoordinates(coordinates)) {
//        if (true) {
//            //Tower tower = createNewTower(towerType, xPosition, yPosition);
//            Tower tower = createNewTower(towerType);
//            System.out.println(coordinates.toString());
//            tower.setPosition(coordinates);
//            //gamestate.addTower(tower);
//            addTower(tower, 0, 0);
//            wasSuccessful = true;
//        } else {
//            wasSuccessful = false;
//        }
//
//        return wasSuccessful;
    }



    @Override
    public boolean buildTower(Tower tower, Coordinates coordinates) {
        return false;
    }


}
