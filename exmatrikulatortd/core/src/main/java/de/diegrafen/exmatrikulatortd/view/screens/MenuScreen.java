package de.diegrafen.exmatrikulatortd.view.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import de.diegrafen.exmatrikulatortd.controller.MainController;
import de.diegrafen.exmatrikulatortd.model.Highscore;

import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;

import static de.diegrafen.exmatrikulatortd.controller.factories.NewGameFactory.STANDARD_SINGLE_PLAYER_GAME;
import static de.diegrafen.exmatrikulatortd.util.Assets.MENU_BACKGROUND_IMAGE;
import static de.diegrafen.exmatrikulatortd.util.Assets.SINGLEPLAYER_MAP_PATH;

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

    private Table clientOrServerMenuTable;

    private Table serverListMenuTable;

    private Table serverListTable;

    private java.util.List<String> serverList;

    private String hostAddress = "";

    private Texture backgroundTexture;

    private Sprite backgroundSprite;

    public MenuScreen(MainController mainController, AssetManager assetManager) {
        super(mainController, assetManager);
        stage = new Stage(new ScreenViewport());
        this.serverList = new LinkedList<>();
    }

    @Override
    public void init() {
        super.init();
        backgroundTexture = getAssetManager().get(MENU_BACKGROUND_IMAGE, Texture.class);
        backgroundTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        backgroundSprite = new Sprite(backgroundTexture);

        Gdx.input.setInputProcessor(stage);

        Stack menuStack = new Stack();
        menuStack.setFillParent(true);

        createMainMenuTable(menuStack);
        createPreferenceMenuTable(menuStack);
        createSelectGameModeTable(menuStack);
        createHighscoreMenuTable(menuStack);
        createSelectClientOrServerMenu(menuStack);
        createServerListTable(menuStack);

        stage.addActor(menuStack);
        //stage.addActor(new Actor(backgroundSprite));
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
                showSetSelectGameModeMenu(mainMenuTable);
            }
        });

        preferences.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // FIXME: Irgendwie bleibt der Button aktiv, nachdem man draufgeklickt hat
                showPreferencesMenu(mainMenuTable);
            }
        });

        highScores.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                showHighScoreMenu(mainMenuTable);
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
                getMainController().createNewSinglePlayerGame(STANDARD_SINGLE_PLAYER_GAME, SINGLEPLAYER_MAP_PATH);
                showMainMenu(selectGameModeTable);
            }
        });

        newMultiPlayerGameButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                showClientOrServerMenu(selectGameModeTable);
            }
        });

        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                showMainMenu(selectGameModeTable);
                backButton.setChecked(false);
            }
        });
    }

    private void createPreferenceMenuTable(Stack menuStack) {

        // TODO: Einstellungsmöglichkeiten für Bildschirmgröße etc. hinzufügen
        // TODO: Auswahlmöglichkeit für Schwierigkeitsgrad hinzufügen

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
                showMainMenu(preferencesMenuTable);
                backButton.setChecked(false);
            }
        });
    }

    private void createHighscoreMenuTable(Stack menuStack) {

        highScoreMenuTable = new Table();
        Skin skin = new Skin(Gdx.files.internal("ui-skin/glassy-ui.json"));
        Table highScoreTable = new Table();
        final ScrollPane.ScrollPaneStyle scrollPaneStyle = new ScrollPane.ScrollPaneStyle();
        final ScrollPane upgradesScrollPane = new ScrollPane(highScoreTable, scrollPaneStyle);
        java.util.List<Highscore> zweiteHighscoreList = getMainController().retrieveHighscores(20);
        Skin basicSkin = createBasicSkin();

        highScoreTable.pad(10).defaults().expandX().space(4);

        for (Highscore highscore : zweiteHighscoreList) {
            // FIXME: Formatierung passt noch nicht so ganz.
            highScoreTable.row();
            Table rowTable = new TextButton(highscore.getProfile().getProfileName() +
                    "\nScore: " + highscore.getScore() + " Round reached: " + highscore.getRoundNumberReached() +
                    "\nDate played: " + highscore.getDatePlayed(), basicSkin);
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
                showMainMenu(highScoreMenuTable);
                backButton.setChecked(false);
            }
        });

        highScoreMenuTable.add(backButton).fillX().uniformX();
        highScoreMenuTable.row();
    }

    private void createSelectClientOrServerMenu(Stack menuStack) {

        clientOrServerMenuTable = new Table();
        Skin skin = new Skin(Gdx.files.internal("ui-skin/glassy-ui.json"));
        //Skin skin = createBasicSkin();
        TextButton createGame = new TextButton("Spiel erstellen", skin);
        TextButton searchGame = new TextButton("Spiel suchen", skin);
        TextButton backButton = new TextButton("Zurück", skin);

        clientOrServerMenuTable.setFillParent(true);
        clientOrServerMenuTable.setVisible(false);
        //table.setDebug(true);
        menuStack.addActor(clientOrServerMenuTable);
        clientOrServerMenuTable.add(createGame).fillX().uniformX();
        clientOrServerMenuTable.row().pad(10, 0, 10, 0);
        clientOrServerMenuTable.add(searchGame).fillX().uniformX();
        clientOrServerMenuTable.row();
        clientOrServerMenuTable.add(backButton).fillX().uniformX();
        clientOrServerMenuTable.row().pad(10, 0, 10, 0);


        createGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                getMainController().createServer();
                getMainController().startServer();
            }
        });

        searchGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                getMainController().createClient();
                serverList = getMainController().getLocalGameServers();
                updateServerList();
                showServerListMenu(clientOrServerMenuTable);
            }
        });

        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                showMainMenu(clientOrServerMenuTable);
                backButton.setChecked(false);
            }
        });
    }

    private void createServerListTable(Stack menuStack) {

        serverListMenuTable = new Table();
        serverListTable = new Table();
        Skin skin = new Skin(Gdx.files.internal("ui-skin/glassy-ui.json"));
        final ScrollPane.ScrollPaneStyle scrollPaneStyle = new ScrollPane.ScrollPaneStyle();
        final ScrollPane upgradesScrollPane = new ScrollPane(serverListTable, scrollPaneStyle);

        TextButton connectButton = new TextButton("Verbinden", skin);
        TextButton backButton = new TextButton("Zurück", skin);

        serverListMenuTable.setFillParent(true);
        serverListMenuTable.setVisible(false);
        //table.setDebug(true);
        menuStack.addActor(serverListMenuTable);
        serverListMenuTable.add(upgradesScrollPane).fillX().uniformX();
        serverListMenuTable.row().pad(10, 0, 10, 0);

        connectButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                getMainController().connectToServer(hostAddress);
            }
        });

        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                getMainController().shutdownClient();
                showMainMenu(serverListMenuTable);
                backButton.setChecked(false);
            }
        });

        serverListMenuTable.add(connectButton).fillX().uniformX();
        serverListMenuTable.row();
        serverListMenuTable.add(backButton).fillX().uniformX();
        serverListMenuTable.row().pad(10, 0, 10, 0);

        updateServerList();
    }

    private void updateServerList() {

        // FIXME: Die alten Zeilen werden bei einem Update irgendwie noch nicht ersetzt
        Array<Cell> cells = serverListTable.getCells();

        for (Cell cell : cells) {
            serverListTable.removeActor(cell.getActor());
        }

        Skin basicSkin = createBasicSkin();

        for (String server: serverList) {
            String[] lines = server.split("\n");
            // FIXME: Formatierung passt noch nicht so ganz.
            serverListTable.row();
            Table rowTable = new TextButton("Map-Name: " + lines[1] + "\nAnzahl Spieler: " + lines[2], basicSkin);
            serverListTable.add(rowTable);
            rowTable.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    hostAddress = lines[0];
                    System.out.println(Arrays.toString(lines));
                }
            });
        }
        hostAddress = "blah";
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        stage.act(Math.min(deltaTime, 1 / 30f));
    }

    /**
     * Eigene Zeichenanweisungen.
     *
     * @param deltaTime Die Zeit in Sekunden seit dem letzten Frame.
     */
    @Override
    public void draw(float deltaTime) {
        super.draw(deltaTime);

        getSpriteBatch().begin();
        getSpriteBatch().draw(backgroundSprite, 0, 0, getCamera().viewportWidth, getCamera().viewportHeight);
        getSpriteBatch().end();

        // tell our stage to do actions and draw itself
        stage.draw();
    }

    private void createSaveGameMenu() {
    }

    private void createNewSinglePlayerGameMenu() {

    }

    private void showMainMenu(Table callingTable) {
        mainMenuTable.setVisible(true);
        callingTable.setVisible(false);
    }

    private void showSetSelectGameModeMenu(Table callingTable) {
        selectGameModeTable.setVisible(true);
        callingTable.setVisible(false);
    }

    private void showHighScoreMenu(Table callingTable) {
        highScoreMenuTable.setVisible(true);
        callingTable.setVisible(false);
    }

    private void showPreferencesMenu(Table callingTable) {
        preferencesMenuTable.setVisible(true);
        callingTable.setVisible(false);
    }

    private void showClientOrServerMenu(Table callingTable) {
        clientOrServerMenuTable.setVisible(true);
        callingTable.setVisible(false);
    }

    private void showServerListMenu(Table callingTable) {
        serverListMenuTable.setVisible(true);
        callingTable.setVisible(false);
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
        super.resize(width, height);
        stage.getViewport().update(width, height, true);
    }



}
