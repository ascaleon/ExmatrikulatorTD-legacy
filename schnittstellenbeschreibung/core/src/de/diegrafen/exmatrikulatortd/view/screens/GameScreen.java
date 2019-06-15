package de.diegrafen.exmatrikulatortd.view.screens;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import de.diegrafen.exmatrikulatortd.ExmatrikulatorTD;
import de.diegrafen.exmatrikulatortd.controller.GameLogicController;
import de.diegrafen.exmatrikulatortd.model.Gamestate;
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

//    public GameScreen(SinglePlayerGameController singlePlayerGameController, Gamestate gameState) {
//        super(singlePlayerGameController, gameState);
//        this.multiPlayer = false;
//    }
//
//    public GameScreen(MultiPlayerGameController multiPlayerGameController, Gamestate gameState, Connection connection) {
//        super(multiPlayerGameController, gameState);
//        this.multiPlayer = true;
//    }

    public GameScreen(GameLogicController gameLogicController) {
        super();
        this.gameLogicController = gameLogicController;
        gameLogicController.setGameScreen(this);
        gameLogicController.initGameScreen();
    }

    @Override
    void update (float deltaTime) {
        super.update(deltaTime);
        gameLogicController.update(deltaTime);


    }

    @Override
    public void addTower(TowerObject towerObject) {

    }

    @Override
    public void removeTower(TowerObject towerObject) {

    }

    @Override
    public void addEnemy(EnemyObject enemyObject) {

    }

    @Override
    public void removeEnemy(EnemyObject enemyObject) {

    }

    private void loadMap (String mapPath) {

    }

    private void initializeUserInterface () {

    }
}
