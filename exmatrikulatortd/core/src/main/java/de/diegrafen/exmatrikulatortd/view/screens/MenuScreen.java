package de.diegrafen.exmatrikulatortd.view.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import de.diegrafen.exmatrikulatortd.controller.MainController;
import de.diegrafen.exmatrikulatortd.model.Difficulty;
import de.diegrafen.exmatrikulatortd.model.Highscore;
import de.diegrafen.exmatrikulatortd.model.Profile;

import java.util.Date;
import java.util.LinkedList;

/**
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 14.06.2019 04:34
 */
public class MenuScreen extends BaseScreen {

    private Stage stage;

    private Table mainMenuTable;

    private Table preferencesMenuTable;

    private Table selectGameModeTable;

    private Table highScoreMenuTable;


    public MenuScreen(MainController mainController, Game game) {
        super(mainController, game);
        stage = new Stage(new ScreenViewport());
    }

    @Override
    public void init() {
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

        Stack menuStack = new Stack();
        menuStack.setFillParent(true);

        createMainMenuTable(menuStack);
        createPreferenceMenuTable(menuStack);
        createSelectGameModeTable(menuStack);
        createHighscoreMenuTable(menuStack);

        stage.addActor(menuStack);
    }

    private void createMainMenuTable(Stack menuStack) {
        Skin skin = new Skin(Gdx.files.internal("ui-skin/glassy-ui.json"));
        //Skin skin = createBasicSkin();
        mainMenuTable = new Table();
        TextButton newGame = new TextButton("Neues Spiel", skin);
        TextButton selectProfile = new TextButton("Profil auswählen", skin);
        TextButton highScores = new TextButton("Highscores", skin);
        TextButton preferences = new TextButton("Einstellungen", skin);
        TextButton exit = new TextButton("Spiel verlassen", skin);

        mainMenuTable.setFillParent(true);
        //table.setDebug(true);
        menuStack.addActor(mainMenuTable);
        mainMenuTable.add(newGame).fillX().uniformX();
        mainMenuTable.row().pad(10, 0, 10, 0);
        mainMenuTable.add(selectProfile).fillX().uniformX();
        mainMenuTable.row();
        mainMenuTable.add(highScores).fillX().uniformX();
        mainMenuTable.row().pad(10, 0, 10, 0);
        mainMenuTable.add(preferences).fillX().uniformX();
        mainMenuTable.row();
        mainMenuTable.add(exit).fillX().uniformX();

        newGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                showSetSelectGameModeMenu();
            }
        });

        preferences.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // FIXME: Irgendwie bleibt der Button aktiv, nachdem man draufgeklickt hat
                showPreferencesMenu();
            }
        });

        highScores.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                showHighScoreMenu();
            }
        });

        exit.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });
    }

    private void createSelectGameModeTable(Stack menuStack) {
        Skin skin = new Skin(Gdx.files.internal("ui-skin/glassy-ui.json"));
        //Skin skin = createBasicSkin();
        selectGameModeTable = new Table();
        TextButton newSinglePlayerGameButton = new TextButton("Singleplayer", skin);
        TextButton newMultiPlayerGameButton = new TextButton("Multiplayer", skin);
        TextButton backButton = new TextButton("Zurück", skin);

        selectGameModeTable.setFillParent(true);
        selectGameModeTable.setVisible(false);
        selectGameModeTable.setDebug(true);
        menuStack.addActor(selectGameModeTable);
        selectGameModeTable.add(newSinglePlayerGameButton).fillX().uniformX();
        selectGameModeTable.row().pad(10, 0, 10, 0);
        selectGameModeTable.add(newMultiPlayerGameButton).fillX().uniformX();
        selectGameModeTable.row();
        selectGameModeTable.add(backButton).fillX().uniformX();
        selectGameModeTable.row().pad(10, 0, 10, 0);

        newSinglePlayerGameButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                getMainController().createNewSinglePlayerGame();
            }
        });

        newMultiPlayerGameButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("Multiplayer!");
            }
        });

        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                showMainMenu();
                backButton.setChecked(false);
            }
        });
    }

    private void createPreferenceMenuTable(Stack menuStack) {

        // TODO: Einstellungsmöglichkeiten für Bildschirmgröße etc. hinzufügen

        highScoreMenuTable = new Table();
        Skin skin = new Skin(Gdx.files.internal("ui-skin/glassy-ui.json"));
        //Skin skin = createBasicSkin();
        preferencesMenuTable = new Table();
        TextButton backButton = new TextButton("Zurück", skin);

        preferencesMenuTable.setFillParent(true);
        preferencesMenuTable.setVisible(false);
        //table.setDebug(true);
        menuStack.addActor(preferencesMenuTable);
        preferencesMenuTable.add(backButton).fillX().uniformX();
        preferencesMenuTable.row().pad(10, 0, 10, 0);

        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                showMainMenu();
                backButton.setChecked(false);
            }
        });
    }

    private void createHighscoreMenuTable(Stack menuStack) {

        InputListener stopTouchDown = new InputListener() {
            public boolean touchDown (InputEvent event, float x, float y, int pointer, int button) {
                event.stop();
                return false;
            }
        };

        Skin skin = new Skin(Gdx.files.internal("ui-skin/glassy-ui.json"));

        Table highScoreTable = new Table();

        final ScrollPane.ScrollPaneStyle scrollPaneStyle = new ScrollPane.ScrollPaneStyle();
        final ScrollPane upgradesScrollPane = new ScrollPane(highScoreTable, scrollPaneStyle);

        highScoreTable.pad(10).defaults().expandX().space(4);

        java.util.List<Highscore> zweiteHighscoreList = getMainController().retrieveHighscores(20);

        System.out.println(zweiteHighscoreList.size());

        Skin basicSkin = createBasicSkin();

        for (Highscore highscore : zweiteHighscoreList) {
            // FIXME: Formatierung passt noch nicht so ganz.
            highScoreTable.row();
            //highScoreTable.row().pad(10, 0, 0, 0);
            Table rowTable = new TextButton(highscore.getProfile().getProfileName() +
                    "\nScore: " + highscore.getScore() + " Round reached: " + highscore.getRoundNumberReached() +
                    "\nDate played: " + highscore.getDatePlayed(), basicSkin);
            //rowTable.defaults().expandX().fillX();
            //rowTable.add(new Label(highscore.getProfile().getProfileName(), skin)).expandX().fillX();
            //rowTable.row();
            //rowTable.add(new Label("Score: " + highscore.getScore(), skin)).expandX().fillX();
            //rowTable.add(new Label("Round reached: " + highscore.getRoundNumberReached(), skin)).expandX().fillX();
            //rowTable.row();
            //rowTable.add(new Label("Date played: " + highscore.getDatePlayed().toString(), skin)).expandX().fillX();
            highScoreTable.add(rowTable);
            rowTable.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    System.out.println("Der Score von " + highscore.getProfile().getProfileName() + " beträgt: " + highscore.getScore());
                }
            });
        }


        TextButton backButton = new TextButton("Zurück", skin);

        highScoreMenuTable.setFillParent(true);
        highScoreMenuTable.setVisible(false);
        //table.setDebug(true);
        menuStack.addActor(highScoreMenuTable);
        highScoreMenuTable.add(upgradesScrollPane).fillX().uniformX();
        highScoreMenuTable.row().pad(10, 0, 10, 0);

        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                showMainMenu();
                backButton.setChecked(false);
            }
        });


        highScoreMenuTable.add(backButton).fillX().uniformX();
        highScoreMenuTable.row();
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

    private void createSaveGameMenu() {

    }

    private void createNewSinglePlayerGameMenu() {

    }

    private void showMainMenu() {
        mainMenuTable.setVisible(true);
        preferencesMenuTable.setVisible(false);
        selectGameModeTable.setVisible(false);
        highScoreMenuTable.setVisible(false);
    }

    private void showSetSelectGameModeMenu() {
        mainMenuTable.setVisible(false);
        preferencesMenuTable.setVisible(false);
        selectGameModeTable.setVisible(true);
        highScoreMenuTable.setVisible(false);
    }

    private void showHighScoreMenu() {
        mainMenuTable.setVisible(false);
        preferencesMenuTable.setVisible(false);
        selectGameModeTable.setVisible(false);
        highScoreMenuTable.setVisible(true);
    }

    private void showPreferencesMenu() {
        mainMenuTable.setVisible(false);
        preferencesMenuTable.setVisible(true);
        selectGameModeTable.setVisible(false);
        highScoreMenuTable.setVisible(false);
    }



    private Skin createBasicSkin() {
        BitmapFont bitmapFont = getBitmapFont();
        Skin skin = new Skin();
        skin.add("default", bitmapFont);

        Pixmap pixmap = new Pixmap(Gdx.graphics.getWidth() / 4, Gdx.graphics.getHeight() / 10, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        skin.add("background", new Texture(pixmap));

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.up = skin.newDrawable("background", Color.GRAY);
        textButtonStyle.down = skin.newDrawable("background", Color.DARK_GRAY);
        textButtonStyle.checked = skin.newDrawable("background", Color.DARK_GRAY);
        textButtonStyle.over = skin.newDrawable("background", Color.LIGHT_GRAY);
        textButtonStyle.font = skin.getFont("default");
        skin.add("default", textButtonStyle);

        return skin;
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
