package de.diegrafen.exmatrikulatortd.view.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import de.diegrafen.exmatrikulatortd.controller.MainController;
import de.diegrafen.exmatrikulatortd.persistence.GameStateDao;

/**
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 14.06.2019 04:34
 */
public class MenuScreen extends BaseScreen {

    private BitmapFont font;

    FitViewport gameViewport;

    Stage stage;




    public MenuScreen (MainController mainController, Game game) {
        super(mainController, game);
        stage = new Stage(new ScreenViewport());
    }

    @Override
    public void init () {
        super.init();
        //System.out.println("Dies ist der MenuScreen!");
        //font = new BitmapFont();
        //gameViewport = new FitViewport(800,480, getCamera());
        //Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void show() {
        System.out.println("Dies ist der MenuScreen!");

        Gdx.input.setInputProcessor(stage);

        Table table = new Table();
        table.setFillParent(true);
        table.setDebug(true);
        stage.addActor(table);

        // temporary until we have asset manager in
        Skin skin = new Skin(Gdx.files.internal("ui-skin/glassy-ui.json"));

        //create buttons
        TextButton newGame = new TextButton("New Game", skin);
        TextButton preferences = new TextButton("Preferences", skin);
        TextButton exit = new TextButton("Exit", skin);

        //add buttons to table
        table.add(newGame).fillX().uniformX();
        table.row().pad(10, 0, 10, 0);
        table.add(preferences).fillX().uniformX();
        table.row();
        table.add(exit).fillX().uniformX();

        // create button listeners
        exit.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });

        newGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                getMainController().createNewSinglePlayerGame();
            }
        });

        preferences.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                //parent.changeScreen(Box2DTutorial.PREFERENCES);
                //getMainController().createNewSinglePlayerGame();
            }

        });
        //getMainController().createNewSinglePlayerGame();
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        // clear the screen ready for next set of images to be drawn
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // tell our stage to do actions and draw itself
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();

    }

    private void createMainMenu () {

    }

    private void createSaveGameMenu () {

    }

    private void createNewSinglePlayerGameMenu () {

    }

    @Override
    public void dispose() {
        super.dispose();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }
}
