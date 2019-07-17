package de.diegrafen.exmatrikulatortd.view.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import de.diegrafen.exmatrikulatortd.controller.MainController;
import de.diegrafen.exmatrikulatortd.model.Highscore;

import java.util.LinkedList;

import static de.diegrafen.exmatrikulatortd.controller.factories.NewGameFactory.STANDARD_SINGLE_PLAYER_GAME;
import static de.diegrafen.exmatrikulatortd.util.Assets.SINGLEPLAYER_MAP_PATH;

/**
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 17.07.2019 19:26
 */
public class MenuScreen extends BaseScreen {

    //private Stage stage;

    private Table mainMenuTable;

    private Table preferencesMenuTable;

    private Table selectGameTypeTable;

    private Table highScoreMenuTable;

    private Table clientOrServerMenuTable;

    private Table serverListMenuTable;

    private Table serverListTable;
    
    private Table singlePlayerGameModeTable;

    private Table multiPlayerGameModeTable;

    private Table gameLobbyTable;

    private Table playerTable;

    private java.util.List<String> serverList;

    public MenuScreen(MainController mainController, AssetManager assetManager) {
        super(mainController, assetManager);
        this.serverList = new LinkedList<>();
    }

    @Override
    public void init() {
        Gdx.input.setInputProcessor(getUi());

        Stack menuStack = new Stack();
        menuStack.setFillParent(true);

        createMainMenuTable(menuStack);
        createPreferenceMenuTable(menuStack);
        createselectGameTypeTable(menuStack);
        createHighscoreMenuTable(menuStack);
        createSelectClientOrServerMenu(menuStack);
        createServerListTable(menuStack);
        createGameLobbyTable(menuStack);

        getUi().addActor(menuStack);
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
                showTable(mainMenuTable, selectGameTypeTable);
            }
        });

        preferences.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                showTable(mainMenuTable, preferencesMenuTable);
            }
        });

        highScores.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                showTable(mainMenuTable, highScoreMenuTable);
            }
        });

        exit.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.exit();
            }
        });
    }

    private void createselectGameTypeTable(Stack menuStack) {
        Skin skin = new Skin(Gdx.files.internal("ui-skin/glassy-ui.json"));
        //Skin skin = createBasicSkin();
        selectGameTypeTable = new Table();
        TextButton newSinglePlayerGameButton = new TextButton("Singleplayer", skin);
        TextButton newMultiPlayerGameButton = new TextButton("Multiplayer", skin);
        TextButton backButton = new TextButton("Zurück", skin);

        selectGameTypeTable.setFillParent(true);
        selectGameTypeTable.setVisible(false);
        selectGameTypeTable.setDebug(true);
        menuStack.addActor(selectGameTypeTable);
        selectGameTypeTable.add(newSinglePlayerGameButton).fillX().uniformX();
        selectGameTypeTable.row().pad(10, 0, 10, 0);
        selectGameTypeTable.add(newMultiPlayerGameButton).fillX().uniformX();
        selectGameTypeTable.row();
        selectGameTypeTable.add(backButton).fillX().uniformX();
        selectGameTypeTable.row().pad(10, 0, 10, 0);

        newSinglePlayerGameButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                getMainController().createNewSinglePlayerGame(STANDARD_SINGLE_PLAYER_GAME, SINGLEPLAYER_MAP_PATH);
            }
        });

        newMultiPlayerGameButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                showTable(selectGameTypeTable, clientOrServerMenuTable);
            }
        });

        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                showTable(selectGameTypeTable, mainMenuTable);
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
                showTable(preferencesMenuTable, mainMenuTable);
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
                showTable(highScoreMenuTable, mainMenuTable);
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
                showTable(clientOrServerMenuTable, gameLobbyTable);
            }
        });

        searchGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                getMainController().createClient();
                serverList = getMainController().getLocalGameServers();
                updateServerList();
                showTable(clientOrServerMenuTable, serverListMenuTable);
            }
        });

        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                showTable(clientOrServerMenuTable, mainMenuTable);
                backButton.setChecked(false);
            }
        });
    }

    private void createGameLobbyTable(Stack menuStack) {

        gameLobbyTable = new Table();
        playerTable = new Table();
        Skin skin = new Skin(Gdx.files.internal("ui-skin/glassy-ui.json"));
        final ScrollPane.ScrollPaneStyle scrollPaneStyle = new ScrollPane.ScrollPaneStyle();
        final ScrollPane playersScrollPane = new ScrollPane(playerTable, scrollPaneStyle);

        TextButton backButton = new TextButton("Zurück", skin);

        gameLobbyTable.setFillParent(true);
        gameLobbyTable.setVisible(false);
        //table.setDebug(true);
        menuStack.addActor(gameLobbyTable);
        gameLobbyTable.add(playersScrollPane).fillX().uniformX();
        gameLobbyTable.row().pad(10, 0, 10, 0);

        TextButton setReadyButton = new TextButton("Bereit!", skin);

        setReadyButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                getMainController().toggleReady();
            }
        });

        gameLobbyTable.add(setReadyButton).fillX().uniformX();
        gameLobbyTable.row();

        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                getMainController().shutdownConnections();
                showTable(gameLobbyTable, mainMenuTable);
                backButton.setChecked(false);
            }
        });
        gameLobbyTable.add(backButton).fillX().uniformX();
        gameLobbyTable.row().pad(10, 0, 10, 0);
    }

    private void updateServerList() {

        serverListTable.clearChildren();

        Skin basicSkin = createBasicSkin();

        for (String server: serverList) {
            String[] lines = server.split("\n");
            // FIXME: Formatierung passt noch nicht so ganz.
            serverListTable.row();
            Table rowTable = new TextButton("Map: " + lines[1] + "\nAnzahl Spieler: " + lines[2], basicSkin);
            serverListTable.add(rowTable);
            rowTable.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    getMainController().setHostAdress(lines[0]);
                }
            });
        }
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
                if (getMainController().connectToServer(getMainController().getHostAdress())) {
                    showTable(serverListMenuTable, gameLobbyTable);
                }
            }
        });

        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                getMainController().shutdownConnections();
                showTable(serverListMenuTable, mainMenuTable);
                backButton.setChecked(false);
            }
        });

        serverListMenuTable.add(connectButton).fillX().uniformX();
        serverListMenuTable.row();
        serverListMenuTable.add(backButton).fillX().uniformX();
        serverListMenuTable.row().pad(10, 0, 10, 0);

        //updateServerList();
    }

    private void createSaveGameMenu() {

    }

    private void createNewSinglePlayerGameMenu() {

    }

    private void showTable(Table callingTable, Table calledTable) {
        callingTable.setVisible(false);
        calledTable.setVisible(true);
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
}
