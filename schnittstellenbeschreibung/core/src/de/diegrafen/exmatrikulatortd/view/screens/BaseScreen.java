package de.diegrafen.exmatrikulatortd.view.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import de.diegrafen.exmatrikulatortd.ExmatrikulatorTD;
import de.diegrafen.exmatrikulatortd.GameLogic.GameLogicController;
import de.diegrafen.exmatrikulatortd.controller.GameController;
import de.diegrafen.exmatrikulatortd.model.Gamestate;

public abstract class BaseScreen implements Screen {

    private ExmatrikulatorTD game;

    private OrthographicCamera camera;

    GameLogicController gameLogicController;

    protected GameController gameController;
    protected Gamestate gameState;
    protected Stage ui;

    public BaseScreen (ExmatrikulatorTD game) {
        this.game = game;
    }

    public BaseScreen(GameController gameController, Gamestate gameState) {
        this.gameController = gameController;
        this.gameState = gameState;
        ui = new Stage(new ScreenViewport());
    }

    public BaseScreen () {

    }


    @Override
    public void show() {
        // Set Debug Mode
        //ui.setDebugAll(gameController.isDebugOn());

        // Map the controller
        InputMultiplexer input = new InputMultiplexer();
        //input.addProcessor(ui);

        // Add an input processor to toggle debug mode via F3.
        //input.addProcessor(new DebugProcessor(ui, gameController));
        Gdx.input.setInputProcessor(input);

        // Screen-specific initialization
        init();
    }

    public void init() { }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        draw(delta);
        if(ui != null) {
            ui.act(delta);
            ui.draw();
        }
    }

    /**
     * Override this sucker to implement any custom drawing
     * @param delta The number of seconds that have passed since the last frame.
     */
    public void draw(float delta) {}

    @Override
    public void resize(int width, int height) {
        //ui.getViewport().update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        if(ui != null) ui.dispose();
        ui = null;
    }
}
