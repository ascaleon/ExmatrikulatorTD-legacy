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

import java.util.HashMap;
import java.util.List;

import static de.diegrafen.exmatrikulatortd.controller.factories.TowerFactory.TowerType.REGULAR_TOWER;
import static de.diegrafen.exmatrikulatortd.controller.factories.TowerFactory.createNewTower;
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

    private Player localPlayer;

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
    }

    @Override
    public void update(float deltaTime) {
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
    void initializeCollisionMap () {

    }

    public void addEnemy (EnemyFactory.EnemyType enemyType) {

    }

    public void addEnemy (Enemy enemy) {
        gamestate.addEnemy(enemy);
        EnemyObject enemyObject = new EnemyObject(enemy);
        gameScreen.addEnemy(enemyObject);
        enemyDao.create(enemy);
        enemyMapping.put(enemy, enemyObject);
        System.out.println("Enemy added!");
        System.out.println(enemyMapping.toString());
    }

    public void addTower (Tower tower) {
        //tower.setOwner(localPlayer);
        //localPlayer.addTower(tower);
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
    public boolean buildTower(TowerType towerType, Coordinates coordinates) {

        boolean wasSuccessful;

        //if (gamestate.checkCoordinates(coordinates)) {
        if (true) {
            Tower tower = createNewTower(towerType);
            System.out.println(coordinates.toString());
            tower.setPosition(coordinates);
            //gamestate.addTower(tower);
            addTower(tower);
            wasSuccessful = true;
        } else {
            wasSuccessful = false;
        }

        return wasSuccessful;
    }

    public void buildRegularTower(int screenX, int screenY) {
        Coordinates coordinates = new Coordinates(screenX / TILE_SIZE, screenY / TILE_SIZE);
        buildTower(REGULAR_TOWER, coordinates);

    }

    @Override
    public boolean buildTower(Tower tower, Coordinates coordinates) {
        return false;
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
}
