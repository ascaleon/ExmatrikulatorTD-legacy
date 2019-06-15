package de.diegrafen.exmatrikulatortd.view.screens;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.esotericsoftware.kryonet.Connection;
import de.diegrafen.exmatrikulatortd.ExmatrikulatorTD;
import de.diegrafen.exmatrikulatortd.GameLogic.GameLogicController;
import de.diegrafen.exmatrikulatortd.controller.MultiPlayerGameController;
import de.diegrafen.exmatrikulatortd.controller.SinglePlayerGameController;
import de.diegrafen.exmatrikulatortd.model.BaseModel;
import de.diegrafen.exmatrikulatortd.model.Coordinates;
import de.diegrafen.exmatrikulatortd.model.Gamestate;
import de.diegrafen.exmatrikulatortd.model.enemy.Enemy;
import de.diegrafen.exmatrikulatortd.model.tower.Tower;
import de.diegrafen.exmatrikulatortd.persistence.GameStateDao;
import de.diegrafen.exmatrikulatortd.view.gameobjects.EnemyObject;
import de.diegrafen.exmatrikulatortd.view.gameobjects.TowerObject;

import java.util.List;

/**
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 14.06.2019 02:12
 */
public class GameScreen extends BaseScreen implements GameView {

    private GameLogicController gameLogicController;

    private Gamestate gameState;

    private GameStateDao gameStateDao;

    private Gamestate gamestate;

    private boolean multiPlayer;

    private ExmatrikulatorTD game;

    private TiledMap map;

    private OrthogonalTiledMapRenderer mapRenderer;

    private List<EnemyObject> enemies;

    private List<TowerObject> towers;

    public GameScreen(SinglePlayerGameController singlePlayerGameController, Gamestate gameState) {
        super(singlePlayerGameController, gameState);
        this.multiPlayer = false;
    }

    public GameScreen(MultiPlayerGameController multiPlayerGameController, Gamestate gameState, Connection connection) {
        super(multiPlayerGameController, gameState);
        this.multiPlayer = true;
    }

    public GameScreen(GameLogicController gameLogicController, Gamestate gameState) {
        super();
        this.gameLogicController = gameLogicController;
        this.gameState = gameState;
    }

    @Override
    public void addTower(Tower tower) {

    }

    @Override
    public void removeTower(Tower tower) {

    }

    @Override
    public void addEnemy(Enemy enemy) {

    }

    @Override
    public void removeEnemy(Enemy enemy) {

    }

    private void loadMap (String mapPath) {

    }
}
