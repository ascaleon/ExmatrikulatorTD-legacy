package de.diegrafen.exmatrikulatortd.view.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import de.diegrafen.exmatrikulatortd.ExmatrikulatorTD;
import de.diegrafen.exmatrikulatortd.communication.client.GameClient;
import de.diegrafen.exmatrikulatortd.communication.server.GameServer;
import de.diegrafen.exmatrikulatortd.controller.gamelogic.ClientGameLogicController;
import de.diegrafen.exmatrikulatortd.controller.gamelogic.GameLogicController;
import de.diegrafen.exmatrikulatortd.controller.MainController;
import de.diegrafen.exmatrikulatortd.controller.gamelogic.ServerGameLogicController;
import de.diegrafen.exmatrikulatortd.model.Gamestate;
import de.diegrafen.exmatrikulatortd.model.Player;
import de.diegrafen.exmatrikulatortd.model.Profile;
import de.diegrafen.exmatrikulatortd.persistence.GameStateDao;
import de.diegrafen.exmatrikulatortd.view.gameobjects.EnemyObject;
import de.diegrafen.exmatrikulatortd.view.gameobjects.TowerObject;

import java.util.ArrayList;
import java.util.List;

import static com.badlogic.gdx.Input.Buttons.LEFT;
import static com.badlogic.gdx.Input.Buttons.RIGHT;
import static de.diegrafen.exmatrikulatortd.controller.factories.EnemyFactory.EnemyType.REGULAR_ENEMY;
import static de.diegrafen.exmatrikulatortd.controller.factories.EnemyFactory.createNewEnemy;
import static de.diegrafen.exmatrikulatortd.util.Assets.MAP_PATH;
import static de.diegrafen.exmatrikulatortd.util.Constants.setX;
import static de.diegrafen.exmatrikulatortd.util.Constants.setY;

/**
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 14.06.2019 02:12
 *
 * Der GameScreen wird während des aktuellen Spiels angezeigt.
 */
public class GameScreen extends BaseScreen implements GameView {

    /**
     * Der GameLogicController.
     */
    private GameLogicController gameLogicController;

    /**
     * Der aktuelle Spielstand.
     */
    private Gamestate gameState;

    /**
     * Der Spielstand für die Datenbankimplementierung.
     */
    private GameStateDao gameStateDao;

    /**
     * Ist das aktuelle Spiel Single oder Multiplayer.
     */
    private boolean multiPlayer;

    /**
     * Das Spielobjekt.
     */
    private ExmatrikulatorTD game;

    /**
     * Die Karte, auf der die Türme plaziert werden und Gegner laufen.
     */
    private TiledMap tiledMap;

    /**
     * Der renderer zeichnet die Karte.
     */
    private OrthogonalTiledMapRenderer orthogonalTiledMapRenderer;

    /**
     * Eine Liste der Gegner.
     */
    private List<EnemyObject> enemies;

    /**
     * Eine Liste der Türme.
     */
    private List<TowerObject> towers;

    private InputProcessor inputProcessor;

    /**
     * Der Konstruktor legt den MainController und das Spielerprofil fest. Außerdem erstellt er den Gamestate und den GameLogicController.
     * @param mainController Der Maincontrroller.
     * @param playerProfile Das Spielerprofil.
     */
    public GameScreen(MainController mainController, Game game, Profile playerProfile) {
        super(mainController, game);
        this.gameState = new Gamestate();
        this.gameLogicController = new GameLogicController(mainController, gameState, playerProfile);
        gameLogicController.setGameScreen(this);
    }


    public GameScreen(MainController mainController, Game game, Profile playerProfile, Gamestate gameState) {
        this(mainController, game, playerProfile);
        this.gameState = gameState;
        this.gameLogicController = new GameLogicController(mainController, gameState, playerProfile);
        gameLogicController.setGameScreen(this);
    }

    public GameScreen(MainController mainController, Game game, Profile playerProfile, GameClient gameClient) {
        super(mainController, game);
        this.gameState = new Gamestate();
        this.gameLogicController = new ClientGameLogicController(mainController, gameState, playerProfile, gameClient);
        gameLogicController.setGameScreen(this);
    }

    public GameScreen(MainController mainController, Game game, Profile playerProfile, GameClient gameClient, Gamestate gamestate) {
        this(mainController, game, playerProfile, gameClient);
        gamestate = gameClient.refreshLocalGameState();
        this.gameState =  gamestate;
        this.gameLogicController = new ClientGameLogicController(mainController, gameState, playerProfile, gameClient);
        gameLogicController.setGameScreen(this);
    }


    public GameScreen(MainController mainController, Game game, Profile playerProfile, GameServer gameServer) {
        super(mainController, game);
        this.gameState = new Gamestate();
        this.gameLogicController = new ServerGameLogicController(mainController, gameState, playerProfile, gameServer);
        gameLogicController.setGameScreen(this);
    }

    public GameScreen(MainController mainController, Game game, Profile playerProfile, GameServer gameServer, Gamestate gameState) {
        super(mainController, game);
        this.gameState = gameState;
        this.gameLogicController = new ServerGameLogicController(mainController, gameState, playerProfile, gameServer);
        gameLogicController.setGameScreen(this);
    }


    /**
     * Die Initialisierung erstellt den SpriteBatch und lädt Texturen.
     */
    @Override
    public void init () {
        super.init();
        enemies = new ArrayList<EnemyObject>();
        towers = new ArrayList<TowerObject>();
        Player player = new Player();
        gameLogicController.addEnemy(createNewEnemy(REGULAR_ENEMY));
        //getSessionFactory();
        InputMultiplexer multiplexer = (InputMultiplexer) Gdx.input.getInputProcessor();
        inputProcessor = new InputProcessor() {
            @Override
            public boolean keyDown(int keycode) {
                return false;
            }

            @Override
            public boolean keyUp(int keycode) {
                return false;
            }

            @Override
            public boolean keyTyped(char character) {
                return false;
            }

            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                return false;
            }

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {

                boolean returnvalue = false;

                screenY = Gdx.graphics.getHeight() - screenY;

                if (button == LEFT) {
                    setX(screenX);
                    setY(screenY);

                    returnvalue = true;
                } else if (button == RIGHT) {
                    System.out.println("Rechtsklick!");
                    //Tower tower = createNewTower(SLOW_TOWER);
                    //gameLogicController.buildTower(tower, new Coordinates((int) screenX / TILE_SIZE, (int) screenY / TILE_SIZE));
                    gameLogicController.buildRegularTower(screenX, screenY);
                    //Coordinates coordinates = new Coordinates((int) screenX / TILE_SIZE, (int) screenY / TILE_SIZE);
                    //gameLogicController.buildTower(REGULAR_TOWER, coordinates);
                    returnvalue = true;
                }

                System.out.println("xPos:" + screenX);
                System.out.println("yPos:" + screenY);

                return returnvalue;
            }

            @Override
            public boolean touchDragged(int screenX, int screenY, int pointer) {
                return false;
            }

            @Override
            public boolean mouseMoved(int screenX, int screenY) {
                return false;
            }

            @Override
            public boolean scrolled(int amount) {
                return false;
            }
        };

        multiplexer.addProcessor(inputProcessor);

        loadMap(MAP_PATH);



    }

    /**
     *  Wird immer nach einem Bestimmten Zeitabstand aufgerufen und die Logik des Spiels berechnet, damit danach in render() neu gezeichnet werden kann.
     *  @param deltaTime Die Zeit in Sekunden seit dem letzten Frame.
     */
    @Override
    public void update (float deltaTime) {
        getSpriteBatch().begin();
        //batch.draw(img, 0, 0);
        //System.out.println("Game Loop!");
        //orthogonalTiledMapRenderer.render();
        gameLogicController.update(deltaTime);
        if (enemies != null) {
            for (EnemyObject enemy : enemies) {
                enemy.draw(getSpriteBatch());
            }
        }

        if (towers != null) {
            for (TowerObject tower : towers) {
                //tower.update(deltaTime);
                tower.draw(getSpriteBatch());
            }
        }
        getSpriteBatch().end();

    }

    /**
     * Die nicht mehr benötigten Recourccen werden freigegeben.
     */
    @Override
    public void dispose() {
        super.dispose();
    	//img.dispose();
    }

    /**
     * Lädt die Karte.
     */
    private void loadMap (String mapPath) {
        tiledMap = new TmxMapLoader().load(mapPath);
        orthogonalTiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
        getCamera().update();
        orthogonalTiledMapRenderer.setView(getCamera());
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
        enemies.add(enemyObject);
    }

    @Override
    public void removeEnemy(EnemyObject enemyObject) {
        enemies.remove(enemyObject);
        enemyObject.dispose();
    }
}
