package de.diegrafen.exmatrikulatortd.view.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import de.diegrafen.exmatrikulatortd.ExmatrikulatorTD;
import de.diegrafen.exmatrikulatortd.communication.client.GameClient;
import de.diegrafen.exmatrikulatortd.communication.server.GameServer;
import de.diegrafen.exmatrikulatortd.controller.gamelogic.ClientGameLogicController;
import de.diegrafen.exmatrikulatortd.controller.gamelogic.GameLogicController;
import de.diegrafen.exmatrikulatortd.controller.MainController;
import de.diegrafen.exmatrikulatortd.controller.gamelogic.ServerGameLogicController;
import de.diegrafen.exmatrikulatortd.model.Gamestate;
import de.diegrafen.exmatrikulatortd.model.Profile;
import de.diegrafen.exmatrikulatortd.persistence.GameStateDao;
import de.diegrafen.exmatrikulatortd.view.gameobjects.EnemyObject;
import de.diegrafen.exmatrikulatortd.view.gameobjects.TowerObject;

import java.util.List;

import static de.diegrafen.exmatrikulatortd.util.HibernateUtils.getSessionFactory;

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

    SpriteBatch batch;
    Texture img;

    public GameScreen(MainController mainController, Profile playerProfile) {
        super(mainController);
        this.gameState = new Gamestate();
        this.gameLogicController = new GameLogicController(mainController, gameState, playerProfile);
        gameLogicController.setGameScreen(this);
    }

    public GameScreen(MainController mainController, Profile playerProfile, Gamestate gamestate) {
        this(mainController, playerProfile);
        this.gamestate = gamestate;
    }

    public GameScreen(MainController mainController, Profile playerProfile, GameClient gameClient) {
        super(mainController);
        this.gameState = new Gamestate();
        this.gameLogicController = new ClientGameLogicController(mainController, gameState, playerProfile, gameClient);
        gameLogicController.setGameScreen(this);
    }

    public GameScreen(MainController mainController, Profile playerProfile, GameClient gameClient, Gamestate gamestate) {
        this(mainController, playerProfile, gameClient);
        gamestate = gameClient.refreshLocalGameState();
        this.gameState =  gamestate;
    }


    public GameScreen(MainController mainController, Profile playerProfile, GameServer gameServer) {
        super(mainController);
        this.gameState = new Gamestate();
        this.gameLogicController = new ServerGameLogicController(mainController, gameState, playerProfile, gameServer);
        gameLogicController.setGameScreen(this);
    }

    public GameScreen(MainController mainController, Profile playerProfile, GameServer gameServer, Gamestate gamestate) {
        this(mainController, playerProfile, gameServer);
        this.gamestate = gamestate;
    }


    @Override
    public void init () {
        batch = new SpriteBatch();
        img = new Texture("badlogic.jpg");
        getSessionFactory();

    }

    @Override
    public void update (float deltaTime) {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        batch.draw(img, 0, 0);
        batch.end();
        if (enemies != null) {
            for (EnemyObject enemy : enemies) {
                enemy.update(deltaTime);
            }
        }

        if (towers != null) {
            for (TowerObject tower : towers) {
                tower.update(deltaTime);
            }
        }

    }

    @Override
    public void dispose() {
        super.dispose();
        batch.dispose();
    	img.dispose();
    }

    private void loadMap (String mapPath) {

    }

    private void initializeUserInterface () {

    }

    private void reinitializeGameScreen () {

    }

    @Override
    public void addTower(TowerObject towerObject) {
        towers.add(towerObject);
    }

    @Override
    public void removeTower(TowerObject towerObject) {
        towers.remove(towerObject);
        towerObject.dispose();
    }

    @Override
    public void addEnemy(EnemyObject enemyObject) {
        enemies.remove(enemyObject);
        enemyObject.dispose();

    }

    @Override
    public void removeEnemy(EnemyObject enemyObject) {
        enemyObject.dispose();
    }
}
