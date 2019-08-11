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
abstract class BaseScreen implements Screen {

    /**
     *
     */
    private final Viewport stageViewport;

    /**
     * Die Kamera.
     */
    private final OrthographicCamera camera;

    /**
     * Der MainController ist für die Verwaltung der Screens und Interaktion mit anderen Komponenten zuständig.
     */
    private MainController mainController;

    /**
     * Die Stage verwaltet den Viewport und Eingabe-Events.
     */
    private Stage ui;

    /**
     * Das zum Rendern verwendete Spritebatch
     */
    private final SpriteBatch spriteBatch;

    private final Viewport viewport;

    /**
     * Eine Bitmap-Font zum
     */
    private final BitmapFont bitmapFont = new BitmapFont();

    private final AssetManager assetManager;

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
    void init() {

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
    void update(float deltaTime) {

    }

    /**
     * Eigene Zeichenanweisungen.
     * @param deltaTime Die Zeit in Sekunden seit dem letzten Frame.
     */
    void draw(float deltaTime) {}

    /**
     * Nimmt Anpasssungen bei Änderung der Fenstergröße vor
     *
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
     * Wird ausgeführt, sobald das Spiel pausiert wird
     */
    @Override
    public void pause() {

    }

    /**
     * Wird ausgeführt, sobald das Spiel fortgeführt wird
     */
    @Override
    public void resume() {

    }

    /**
     * Wird ausgeführt, sobald das Spiel ausgeblendet wird
     */
    @Override
    public void hide() {

    }

    /**
     * Gibt nicht mehr benötigte Ressourcen frei
     */
    @Override
    public void dispose() {
        if (ui != null) {
            ui.dispose();
        }
        ui = null;
    }

    /**
     * Gibt den Hauptcontroller zurück
     * @return Der Hauptcontroller
     */
    MainController getMainController() {
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

    AssetManager getAssetManager() {
        return assetManager;
    }
}
