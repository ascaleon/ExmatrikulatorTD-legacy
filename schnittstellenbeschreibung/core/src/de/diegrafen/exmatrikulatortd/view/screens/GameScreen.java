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
    private TiledMap map;

    /**
     * Der renderer zeichnet die Karte.
     */
    private OrthogonalTiledMapRenderer mapRenderer;

    /**
     * Eine Liste der Gegner.
     */
    private List<EnemyObject> enemies;

    /**
     * Eine Liste der Türme.
     */
    private List<TowerObject> towers;

    /**
     * Die Sprites und Texturen
     */
    SpriteBatch batch;
    Texture img;

    /**
     * Der Konstruktor legt den MainController und das Spielerprofil fest. Außerdem erstellt er den Gamestate und den GameLogicController.
     * @param mainController Der Maincontrroller.
     * @param playerProfile Das Spielerprofil.
     */
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


    /**
     * Die Initialisierung erstellt den SpriteBatch und lädt Texturen.
     */
    @Override
    public void init () {
        batch = new SpriteBatch();
        img = new Texture("badlogic.jpg");
        getSessionFactory();

    }

    /**
     *  Wird immer nach einem Bestimmten Zeitabstand aufgerufen und die Logik des Spiels berechnet, damit danach in render() neu gezeichnet werden kann.
     *  @param deltaTime Die Zeit in Sekunden seit dem letzten Frame.
     */
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

    /**
     * Die nicht mehr benötigten Recourccen werden freigegeben.
     */
    @Override
    public void dispose() {
        super.dispose();
        batch.dispose();
    	img.dispose();
    }

    /**
     * Lädt die Karte.
     */
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
