package de.diegrafen.exmatrikulatortd.controller.gamelogic;

import de.diegrafen.exmatrikulatortd.controller.MainController;
import de.diegrafen.exmatrikulatortd.model.Coordinates;
import de.diegrafen.exmatrikulatortd.model.Gamestate;
import de.diegrafen.exmatrikulatortd.model.Profile;
import de.diegrafen.exmatrikulatortd.model.enemy.Enemy;
import de.diegrafen.exmatrikulatortd.model.tower.Tower;
import de.diegrafen.exmatrikulatortd.persistence.GameStateDao;
import de.diegrafen.exmatrikulatortd.persistence.SaveStateDao;
import de.diegrafen.exmatrikulatortd.view.gameobjects.EnemyObject;
import de.diegrafen.exmatrikulatortd.view.gameobjects.TowerObject;
import de.diegrafen.exmatrikulatortd.view.screens.GameScreen;

import java.util.HashMap;

/**
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 15.06.2019 01:24
 */
public class GameLogicController implements LogicController {

    private MainController mainController;

    private Gamestate gamestate;

    private GameScreen gameScreen;

    private Profile profile;

    private GameStateDao gameStateDao;

    private SaveStateDao saveStateDao;

    private HashMap<Tower, TowerObject> towerMapping;

    private HashMap<Enemy, EnemyObject> enemyMapping;

    public GameLogicController (MainController mainController, Gamestate gamestate, Profile profile) {
        this.mainController = mainController;
        this.gamestate = gamestate;
        this.profile = profile;
        this.gameStateDao = new GameStateDao();
        this.saveStateDao = new SaveStateDao();
        gameStateDao.create(gamestate);
    }

    @Override
    public void update(float deltaTime) {

    }

    void applyAuras (float deltaTime) {

    }

    void applyBuffsAndDebuffs (float deltaTime) {

    }

    void applyMovement (float deltaTime) {

    }

    void makeAttacks (float deltaTime) {

    }

    void applyPlayerDamage () {

    }

    void spawnWave () {

    }

    void hasRoundEnded () {

    }

    void startNewRound () {

    }

    void initializeCollisionMap () {

    }

    @Override
    public boolean buildTower(Tower tower, Coordinates coordinates) {
        return false;
    }

    @Override
    public boolean sellTower(Tower tower) {
        return false;
    }

    @Override
    public boolean upgradeTower(Tower tower) {
        return false;
    }

    @Override
    public boolean sendEnemy(Enemy enemy) {
        return false;
    }

    public Gamestate getGamestate() {
        return gamestate;
    }

    public void setGamestate(Gamestate gamestate) {
        this.gamestate = gamestate;
    }

    public GameScreen getGameScreen() {
        return gameScreen;
    }

    public void setGameScreen(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
    }

    public void exitGame (boolean saveBeforeExit) {
        mainController.setEndScreen(gamestate);
    }
}
