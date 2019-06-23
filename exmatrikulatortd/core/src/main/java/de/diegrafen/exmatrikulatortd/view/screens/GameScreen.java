package de.diegrafen.exmatrikulatortd.view.screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector3;
import de.diegrafen.exmatrikulatortd.ExmatrikulatorTD;
import de.diegrafen.exmatrikulatortd.communication.client.GameClient;
import de.diegrafen.exmatrikulatortd.communication.server.GameServer;
import de.diegrafen.exmatrikulatortd.controller.gamelogic.ClientGameLogicController;
import de.diegrafen.exmatrikulatortd.controller.gamelogic.GameLogicController;
import de.diegrafen.exmatrikulatortd.controller.MainController;
import de.diegrafen.exmatrikulatortd.controller.gamelogic.ServerGameLogicController;
import de.diegrafen.exmatrikulatortd.model.Gamestate;
import de.diegrafen.exmatrikulatortd.model.Observable;
import de.diegrafen.exmatrikulatortd.model.Player;
import de.diegrafen.exmatrikulatortd.model.Profile;
import de.diegrafen.exmatrikulatortd.model.enemy.Enemy;
import de.diegrafen.exmatrikulatortd.model.tower.Tower;
import de.diegrafen.exmatrikulatortd.persistence.GameStateDao;
import de.diegrafen.exmatrikulatortd.view.gameobjects.BaseObject;
import de.diegrafen.exmatrikulatortd.view.gameobjects.EnemyObject;
import de.diegrafen.exmatrikulatortd.view.gameobjects.GameObject;
import de.diegrafen.exmatrikulatortd.view.gameobjects.TowerObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static com.badlogic.gdx.Input.Buttons.LEFT;
import static com.badlogic.gdx.Input.Buttons.RIGHT;
import static de.diegrafen.exmatrikulatortd.controller.factories.EnemyFactory.EnemyType.REGULAR_ENEMY;
import static de.diegrafen.exmatrikulatortd.controller.factories.EnemyFactory.createNewEnemy;
import static de.diegrafen.exmatrikulatortd.util.Assets.MAP_PATH;
import static de.diegrafen.exmatrikulatortd.util.Constants.setX;
import static de.diegrafen.exmatrikulatortd.util.Constants.setY;
import static de.diegrafen.exmatrikulatortd.util.HibernateUtils.getSessionFactory;

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
     * Eine Liste aller Spielobjekte
     */
    private List<GameObject> gameObjects;

    private InputProcessor inputProcessor;

    private boolean keyDownDown = false;

    private boolean keyUpDown = false;

    private boolean keyRightDown = false;

    private boolean keyLeftDown = false;

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

        float width = Gdx.graphics.getWidth();
        float height = Gdx.graphics.getHeight();

        this.gameObjects = new LinkedList<>();
        Player player = new Player();
        //gameLogicController.addEnemy(createNewEnemy(REGULAR_ENEMY));

        getCamera().setToOrtho(false, width, height);
        getCamera().update();
        tiledMap = new TmxMapLoader().load(MAP_PATH);
        orthogonalTiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);

        Gdx.input.setInputProcessor(new InputMultiplexer());

        InputMultiplexer multiplexer = (InputMultiplexer) Gdx.input.getInputProcessor();
        inputProcessor = new InputProcessor() {
            @Override
            public boolean keyDown(int keycode) {
                if(keycode == Input.Keys.LEFT)
                    //getCamera().translate(-32,0);
                    keyLeftDown = true;
                if(keycode == Input.Keys.RIGHT)
                    //getCamera().translate(32,0);
                    keyRightDown = true;
                if(keycode == Input.Keys.UP)
                    //getCamera().translate(0,32);
                    keyUpDown = true;
                if(keycode == Input.Keys.DOWN)
                    //getCamera().translate(0,-32);
                    keyDownDown = true;
                //if(keycode == Input.Keys.NUM_1)
                    //tiledMap.getLayers().get(0).setVisible(!tiledMap.getLayers().get(0).isVisible());
                //if(keycode == Input.Keys.NUM_2)
                    //tiledMap.getLayers().get(1).setVisible(!tiledMap.getLayers().get(1).isVisible());
                return false;
            }

            @Override
            public boolean keyUp(int keycode) {
                if(keycode == Input.Keys.LEFT)
                    //getCamera().translate(-32,0);
                    keyLeftDown = false;
                if(keycode == Input.Keys.RIGHT)
                    //getCamera().translate(32,0);
                    keyRightDown = false;
                if(keycode == Input.Keys.UP)
                    //getCamera().translate(0,32);
                    keyUpDown = false;
                if(keycode == Input.Keys.DOWN)
                    //getCamera().translate(0,-32);
                    keyDownDown = false;
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

                //screenY = Gdx.graphics.getHeight() - screenY;

                Vector3 clickCoordinates = new Vector3(screenX,screenY,0);
                Vector3 position = getCamera().unproject(clickCoordinates);

                screenY = Gdx.graphics.getHeight() - screenY;

                System.out.println("xPosition: " + screenX);
                System.out.println("yPosition: " + screenY);

                if (button == LEFT) {
                    returnvalue = true;
                } else if (button == RIGHT) {
                    if (!gameLogicController.buildRegularTower((int) position.x, (int) position.y)) {
                        gameLogicController.sellTower((int) position.x, (int) position.y, 0);
                    }
                    returnvalue = true;
                }

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
     *
     * Aktua
     *
     *  Wird immer nach einem Bestimmten Zeitabstand aufgerufen und die Logik des Spiels berechnet, damit danach in render() neu gezeichnet werden kann.
     *  @param deltaTime Die Zeit in Sekunden seit dem letzten Frame.
     */
    @Override
    public void update (float deltaTime) {
        gameLogicController.update(deltaTime);
    }

    /**
     * Eigene Zeichenanweisungen.
     *
     * @param deltaTime Die Zeit in Sekunden seit dem letzten Frame.
     */
    @Override
    public void draw(float deltaTime) {
        super.draw(deltaTime);

        float translateValue = 5;

        if(keyLeftDown)
            getCamera().translate(-translateValue,0);
        if(keyRightDown)
            getCamera().translate(translateValue,0);
        if(keyUpDown)
            getCamera().translate(0,translateValue);
        if(keyDownDown)
            getCamera().translate(0,-translateValue);

        //Gdx.gl.glClearColor(1, 0, 0, 1);
        //Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        //Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        getCamera().update();
        orthogonalTiledMapRenderer.setView(getCamera());
        orthogonalTiledMapRenderer.render();

        getSpriteBatch().setProjectionMatrix(getCamera().combined);

        getSpriteBatch().begin();

        List<GameObject> objectsToRemove = new ArrayList<>();

        if (gameObjects != null) {
            for (GameObject gameObject : gameObjects) {
                if (gameObject.isRemoved()) {
                    objectsToRemove.add(gameObject);
                } else {
                    gameObject.draw(getSpriteBatch());
                }
            }
        }

        objectsToRemove.forEach(this::removeGameObject);

        getSpriteBatch().end();
    }

    /**
     * Die nicht mehr benötigten Recourccen werden freigegeben.
     */
    @Override
    public void dispose() {
        super.dispose();
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

    public void addTower (Observable observable) {
        gameObjects.add(new TowerObject(observable));
    }

    @Override
    public void addEnemy(Observable observable) {
        gameObjects.add(new EnemyObject(observable));
    }

    public void addTower(TowerObject towerObject) {
        gameObjects.add(towerObject);
    }

    public void removeTower(TowerObject towerObject) {
        gameObjects.remove(towerObject);
        towerObject.dispose();
    }

    public void addEnemy(EnemyObject enemyObject) {
        gameObjects.add(enemyObject);
    }

    public void removeEnemy(EnemyObject enemyObject) {
        gameObjects.remove(enemyObject);
        enemyObject.dispose();
    }

    private void removeGameObject (GameObject gameObject) {
        gameObjects.remove(gameObject);
        gameObject.dispose();
    }

    @Override
    public void addObservable(Observable observable) {
        if (observable instanceof Tower) {
            System.out.println("Well.");
        }
    }
}
