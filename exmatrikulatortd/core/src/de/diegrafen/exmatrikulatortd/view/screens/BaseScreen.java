package de.diegrafen.exmatrikulatortd.view.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import de.diegrafen.exmatrikulatortd.ExmatrikulatorTD;
import de.diegrafen.exmatrikulatortd.controller.gamelogic.GameLogicController;
import de.diegrafen.exmatrikulatortd.controller.MainController;

/**
 * Die Abstrakte Screen Klasse, die für alle Game-Screens verwendet wird.
 */
public abstract class BaseScreen implements Screen {

    /**
     * Das Spielobjekt.
    */
    private Game game;

    /**
     * Die Kamera.
     */
    private OrthographicCamera camera;

    /**
     * Der MainController ist für die Verwaltung der Screens und Interaktion mit anderen Komponenten zuständig.
     */
    private MainController mainController;

    /**
     * Der GameLogicController ist für die Logik des Spiels zuständig.
     */
    GameLogicController gameLogicController;

    /**
     * Die Stage verwaltet den Viewport und Eingabe-Events.
     */
    private Stage ui;

    private SpriteBatch spriteBatch;

    /**
     * Der Konstruktor legt den Maincontroller, das Spielobject sowie die Stage fest.
     * @param mainController Der MainController für den Screen.
     */
    public BaseScreen (MainController mainController, Game game) {
        this.spriteBatch = new SpriteBatch();
        this.mainController = mainController;
        this.game = game;
        ui = new Stage(new ScreenViewport());
    }

    /**
     * Der Konstruktor ohne MainController und Game erstellt lediglich eine neue Stage.
     */
    public BaseScreen () {
        ui = new Stage(new ScreenViewport());
    }

    /**
     * Wenn dieser Screen vom Spiel gezeigt wird, wird diese Methode aufgerufen.
     * Hier wird die Inputverarbeitung erstellt und der Screen initialisiert.
     */
    @Override
    public void show() {
        // Set Debug Mode
        //ui.setDebugAll(gameController.isDebugOn());

        // Map the controller
        InputMultiplexer input = new InputMultiplexer();
        input.addProcessor(ui);

        // Add an input processor to toggle debug mode via F3.
        //input.addProcessor(new DebugProcessor(ui, gameController));
        Gdx.input.setInputProcessor(input);

        // Screen-specific initialization
        init();
    }

    /**
     * Der Screen wird initialisiert.
     */
    public void init() {
        camera = new OrthographicCamera();
    }

    /**
     * Wird immer nach einem Bestimmten Zeitabstand aufgerufen und der Bildinhalt wird neu gerendert.
     * @param deltaTime Die Zeit in Sekunden seit dem letzten Frame.
     */
    @Override
    public void render(float deltaTime) {
        Gdx.gl.glClearColor(255, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        update(deltaTime);
        draw(deltaTime);
        if(ui != null) {
            ui.act(deltaTime);
            ui.draw();
        }
    }

    /**
     * Wird immer nach einem Bestimmten Zeitabstand aufgerufen und die Logik des Spiels berechnet, damit danach in render() neu gezeichnet werden kann.
     * @param deltaTime Die Zeit in Sekunden seit dem letzten Frame.
     */
    public void update(float deltaTime) {

    }

    /**
     * Eigene Zeichenanweisungen.
     * @param deltaTime Die Zeit in Sekunden seit dem letzten Frame.
     */
    public void draw(float deltaTime) {}

    /**
     * Falls das Fenster mit dem Spiel in der Größe angepasst wird, so muss auch der Viewport angepasst werden.
     * @param width Die neue Breite.
     * @param height Die neue Höhe.
     */
    @Override
    public void resize(int width, int height) {
        //ui.getViewport().update(width, height);
    }

    /**
     * Das Spiel wird pausiert.
     */
    @Override
    public void pause() {

    }

    /**
     * Das Spiel wird fortgeführt.
     */
    @Override
    public void resume() {

    }

    /**
     * Das Spiel zeigt diesen Screen nicht mehr an, er wird wieder versteckt.
     */
    @Override
    public void hide() {

    }

    /**
     * Die nicht mehr benötigten Recourccen werden freigegeben.
     */
    @Override
    public void dispose() {
        if(ui != null) ui.dispose();
        ui = null;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(ExmatrikulatorTD game) {
        this.game = game;
    }

    public MainController getMainController() {
        return mainController;
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    public SpriteBatch getSpriteBatch() {
        return spriteBatch;
    }

    public void setSpriteBatch(SpriteBatch spriteBatch) {
        this.spriteBatch = spriteBatch;
    }

    public OrthographicCamera getCamera() {
        return camera;
    }
}
