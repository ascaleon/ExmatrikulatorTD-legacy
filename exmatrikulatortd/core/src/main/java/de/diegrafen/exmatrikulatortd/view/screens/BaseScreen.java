package de.diegrafen.exmatrikulatortd.view.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.*;
import de.diegrafen.exmatrikulatortd.controller.MainController;

/**
 * Die Abstrakte Screen Klasse, die für alle Game-Screens verwendet wird.
 */
public abstract class BaseScreen implements Screen {

    /**
     *
     */
    private Viewport stageViewport;

    /**
     * Die Kamera.
     */
    private OrthographicCamera camera;

    /**
     * Der MainController ist für die Verwaltung der Screens und Interaktion mit anderen Komponenten zuständig.
     */
    private MainController mainController;

    /**
     * Die Stage verwaltet den Viewport und Eingabe-Events.
     */
    private Stage ui;

    private SpriteBatch spriteBatch;

    private Viewport viewport;

    private BitmapFont bitmapFont = new BitmapFont();

    private AssetManager assetManager;

    /**
     * Der Konstruktor legt den Maincontroller, das Spielobject sowie die Stage fest.
     * @param mainController Der MainController für den Screen.
     */
    BaseScreen(MainController mainController, AssetManager assetManager) {
        this.mainController = mainController;
        this.assetManager = assetManager;
        this.camera = new OrthographicCamera();
        viewport = new StretchViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera); //new ScreenViewport(camera); //ExtendViewport(800, 600, camera);
        stageViewport = new ScreenViewport(new OrthographicCamera());

        this.spriteBatch = new SpriteBatch();
        ui = new Stage(stageViewport);
    }

    /**
     * Wenn dieser Screen vom Spiel gezeigt wird, wird diese Methode aufgerufen.
     * Hier wird die Inputverarbeitung erstellt und der Screen initialisiert.
     */
    @Override
    public void show() {
        init();
    }

    /**
     * Der Screen wird initialisiert.
     */
    public void init() {

    }

    /**
     * Wird immer nach einem Bestimmten Zeitabstand aufgerufen und der Bildinhalt wird neu gerendert.
     * @param deltaTime Die Zeit in Sekunden seit dem letzten Frame.
     */
    @Override
    public void render(float deltaTime) {
        //Gdx.graphics.setContinuousRendering(false);
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        update(deltaTime);
        draw(deltaTime);
        if(ui != null) {
            ui.act(deltaTime);
            ui.draw();
        }
        //if(!isPause()){
            //Gdx.graphics.requestRendering();
        //}
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
        ui.getViewport().update(width, height, true);
        viewport.update(width,height);
        camera.update();
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

    public MainController getMainController() {
        return mainController;
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
    }

    SpriteBatch getSpriteBatch() {
        return spriteBatch;
    }

    OrthographicCamera getCamera() {
        return camera;
    }

    Stage getUi() {
        return ui;
    }

    BitmapFont getBitmapFont() {
        return bitmapFont;
    }

    Viewport getStageViewport() {
        return stageViewport;
    }

    public AssetManager getAssetManager() {
        return assetManager;
    }
}
