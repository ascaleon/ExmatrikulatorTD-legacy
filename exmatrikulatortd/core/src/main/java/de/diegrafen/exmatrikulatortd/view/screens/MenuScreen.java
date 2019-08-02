package de.diegrafen.exmatrikulatortd.view.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import de.diegrafen.exmatrikulatortd.controller.MainController;
import de.diegrafen.exmatrikulatortd.model.Difficulty;
import de.diegrafen.exmatrikulatortd.model.Highscore;
import de.diegrafen.exmatrikulatortd.model.Profile;

import java.util.Arrays;
import java.util.LinkedList;

import static de.diegrafen.exmatrikulatortd.controller.factories.NewGameFactory.STANDARD_SINGLE_PLAYER_GAME;
import static de.diegrafen.exmatrikulatortd.util.Assets.SINGLEPLAYER_MAP_PATH;

/**
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 14.06.2019 04:34
 */
public class MenuScreen extends BaseScreen {

    private Stage stage;

    private Table mainMenuTable;

    private Table selectGameModeTable;

    private Table savestatesTable;

    private java.util.List<Profile> profiles;

    private Table selectProfileMenuTable;

    private ButtonGroup<TextButton> profilesButtonGroup;

    private Table profilesTable;

    private Table newProfileMenuTable;

    private TextField profileNameTextField;

    private SelectBox difficultySelectBox;

    private Table highScoreMenuTable;

    private Table highScoreTable;

    private Table preferencesMenuTable;

    private Table clientOrServerMenuTable;

    private Table serverListMenuTable;

    private Table serverListTable;

    private java.util.List<String> serverList;

    private String hostAddress = "";

    final private Skin skin = new Skin(Gdx.files.internal("ui-skin/glassy-ui.json"));

    final private Skin basicSkin = createBasicSkin();

    public MenuScreen(MainController mainController, AssetManager assetManager) {
        super(mainController, assetManager);
        stage = new Stage(new ScreenViewport());
        this.serverList = new LinkedList<>();
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
        Gdx.input.setInputProcessor(stage);

        Stack menuStack = new Stack();
        menuStack.setFillParent(true);

        createMainMenuTable(menuStack);
        createSelectGameModeTable(menuStack);
        createSelectClientOrServerMenu(menuStack);
        createServerListTable(menuStack);
        createSelectProfileMenuTable(menuStack);
        createNewProfileMenuTable(menuStack);
        createHighscoreMenuTable(menuStack);
        createPreferenceMenuTable(menuStack);

        stage.addActor(menuStack);
    }

    private void createGenericMenuTable(Stack menuStack, Table menuTable) {
        menuTable.setFillParent(true);
        menuTable.setVisible(false);
        menuStack.addActor(menuTable);
    }

    private void createMainMenuTable(Stack menuStack) {
        //Skin skin = new Skin(Gdx.files.internal("ui-skin/glassy-ui.json"));
        mainMenuTable = new Table();
        TextButton newGame = new TextButton("Neues Spiel", skin);
        TextButton selectProfile = new TextButton("Profil auswählen", skin);
        TextButton highScores = new TextButton("Highscores", skin);
        TextButton preferences = new TextButton("Einstellungen", skin);
        TextButton exit = new TextButton("Spiel verlassen", skin);

        createGenericMenuTable(menuStack, mainMenuTable);

        mainMenuTable.setVisible(true);
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

        selectProfile.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                showSelectProfileMenu(mainMenuTable);
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
        //Skin skin = new Skin(Gdx.files.internal("ui-skin/glassy-ui.json"))
        selectGameModeTable = new Table();
        TextButton newSinglePlayerGameButton = new TextButton("Singleplayer", skin);
        TextButton newMultiPlayerGameButton = new TextButton("Multiplayer", skin);
        TextButton backButton = new TextButton("Zurück", skin);

        createGenericMenuTable(menuStack, selectGameModeTable);

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

        //Skin skin = new Skin(Gdx.files.internal("ui-skin/glassy-ui.json"));
        preferencesMenuTable = new Table();
        TextButton resetButton = new TextButton("Emergency", skin);
        TextButton backButton = new TextButton("Zurück", skin);

        createGenericMenuTable(menuStack, preferencesMenuTable);

        preferencesMenuTable.add(resetButton).fillX().uniformX();
        preferencesMenuTable.row().pad(10, 0, 10, 0);
        preferencesMenuTable.add(backButton).fillX().uniformX();
        preferencesMenuTable.row().pad(10, 0, 10, 0);

        resetButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("reset");
            }
        });

        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                showMainMenu(preferencesMenuTable);
                backButton.setChecked(false);
            }
        });
    }

    private void refreshProfilesTable() {
        profiles = getMainController().retrieveProfiles();
        //if(!profilesButtonGroup.getButtons().isEmpty()) profilesButtonGroup.remove(profilesButtonGroup.getButtons().toArray());
        for (final TextButton button : profilesButtonGroup.getButtons()) profilesButtonGroup.remove(button);
        profilesTable.clearChildren();
        for (final Profile p : profiles) {
            profilesTable.row();
            TextButton profileButton = new TextButton(p.getProfileName(), basicSkin);

            final Profile currentProfile = getMainController().getCurrentProfile();
            if(currentProfile!=null && currentProfile.getProfileName().equals(p.getProfileName())) profileButton.setColor(Color.GREEN);

            profilesTable.add(profileButton);
            profilesButtonGroup.add(profileButton);
        }
    }

    private Profile getSelectedProfileFromButtonGroup(){
        final TextButton selectedProfileButton = profilesButtonGroup.getChecked();
        final String selectedProfileButtonText = selectedProfileButton.getText().toString();
        for(Profile p : profiles){
            if(p.getProfileName().equals(selectedProfileButtonText)) return p;
        }
        return null;
    }

    private void createSelectProfileMenuTable(Stack menuStack) {
        // Es soll immer nur ein Profil zur Zeit bearbeit oder entfernt werden
        selectProfileMenuTable = new Table();

        profilesTable = new Table();

        profilesButtonGroup = new ButtonGroup<>();
        profilesButtonGroup.setMaxCheckCount(1);
        profilesButtonGroup.setMinCheckCount(1);
        profilesButtonGroup.setUncheckLast(true);

        final ScrollPane.ScrollPaneStyle scrollPaneStyle = new ScrollPane.ScrollPaneStyle();
        final ScrollPane profilesTableScrollPane = new ScrollPane(profilesTable, scrollPaneStyle);

        refreshProfilesTable();

        Table buttonsTable = new Table();

        TextButton switchProfile = new TextButton("Profil wechseln", skin);
        TextButton createNewProfile = new TextButton("Neues Profil", skin);
        TextButton editProfile = new TextButton("Profil bearbeiten", skin);
        TextButton deleteProfile = new TextButton("Profil entfernen", skin);
        TextButton backButton = new TextButton("Zurück", skin);

        profilesTable.pad(10).defaults().expandX().space(4);

        createGenericMenuTable(menuStack, selectProfileMenuTable);

        selectProfileMenuTable.row().pad(10, 0, 10, 0);

        switchProfile.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Profile p = getSelectedProfileFromButtonGroup();
                if(p!=null) getMainController().setCurrentProfile(p);
            }
        });

        createNewProfile.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                showNewProfileMenu(selectProfileMenuTable);
            }
        });

        editProfile.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Profile p = getSelectedProfileFromButtonGroup();
                showNewProfileMenu(selectProfileMenuTable,p);
            }
        });

        deleteProfile.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Profile p = getSelectedProfileFromButtonGroup();
                if(p!=null) getMainController().deleteProfile(p);
                refreshProfilesTable();
            }
        });

        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                showMainMenu(selectProfileMenuTable);
                backButton.setChecked(false);
            }
        });

        buttonsTable.add(switchProfile).fillX();
        buttonsTable.row().pad(10, 0, 10, 0);
        buttonsTable.add(createNewProfile).fillX();
        buttonsTable.row().pad(10, 0, 10, 0);
        buttonsTable.add(editProfile).fillX();
        buttonsTable.row().pad(10, 0, 10, 0);
        buttonsTable.add(deleteProfile).fillX();
        buttonsTable.row().pad(10, 0, 10, 0);
        buttonsTable.add(backButton).fillX();

        selectProfileMenuTable.add(profilesTableScrollPane).expand();
        selectProfileMenuTable.add(buttonsTable).expand();
    }

    private void refreshHighscoresTable(){
        java.util.List<Highscore> zweiteHighscoreList = getMainController().retrieveHighscores(20);
        highScoreTable.clearChildren();

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
    }

    private void createHighscoreMenuTable(Stack menuStack) {
        highScoreMenuTable = new Table();
        highScoreTable = new Table();
        final ScrollPane.ScrollPaneStyle scrollPaneStyle = new ScrollPane.ScrollPaneStyle();
        final ScrollPane upgradesScrollPane = new ScrollPane(highScoreTable, scrollPaneStyle);

        highScoreTable.pad(10).defaults().expandX().space(4);

        refreshHighscoresTable();

        TextButton backButton = new TextButton("Zurück", skin);

        createGenericMenuTable(menuStack, highScoreMenuTable);

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
        TextButton createGame = new TextButton("Spiel erstellen", skin);
        TextButton searchGame = new TextButton("Spiel suchen", skin);
        TextButton backButton = new TextButton("Zurück", skin);

        createGenericMenuTable(menuStack, clientOrServerMenuTable);

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
        final ScrollPane.ScrollPaneStyle scrollPaneStyle = new ScrollPane.ScrollPaneStyle();
        final ScrollPane upgradesScrollPane = new ScrollPane(serverListTable, scrollPaneStyle);

        TextButton connectButton = new TextButton("Verbinden", skin);
        TextButton backButton = new TextButton("Zurück", skin);

        createGenericMenuTable(menuStack, serverListMenuTable);

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

        for (String server : serverList) {
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

    private void createNewProfileMenuTable(Stack menuStack) {
        newProfileMenuTable = new Table();
        profileNameTextField = new TextField("", skin);
        difficultySelectBox = new SelectBox(skin);
        TextButton createProfileButton = new TextButton("Profil erstellen", skin);
        TextButton backButton = new TextButton("Zurück", skin);

        profileNameTextField.setMessageText("Name");
        profileNameTextField.setMaxLength(255);
        difficultySelectBox.setItems(Difficulty.EASY, Difficulty.MEDIUM, Difficulty.HARD, Difficulty.TESTMODE);

        createGenericMenuTable(menuStack, newProfileMenuTable);

        createProfileButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                final String profileName = profileNameTextField.getText();
                if (!profileName.isEmpty()) {
                    getMainController().createNewProfile(profileName, (Difficulty) difficultySelectBox.getSelected(), "");
                    profileNameTextField.setColor(Color.WHITE);
                    profileNameTextField.setText("");
                    showSelectProfileMenu(newProfileMenuTable);
                } else {
                    profileNameTextField.setColor(Color.RED);
                }
            }
        });

        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                showSelectProfileMenu(newProfileMenuTable);
                backButton.setChecked(false);
                profileNameTextField.setText("");
                difficultySelectBox.setSelected(Difficulty.EASY);
            }
        });

        newProfileMenuTable.add(profileNameTextField).fill();
        newProfileMenuTable.row().pad(10, 0, 10, 0);
        newProfileMenuTable.add(difficultySelectBox).fill();
        newProfileMenuTable.row().pad(10, 0, 10, 0);
        newProfileMenuTable.add(createProfileButton);
        newProfileMenuTable.row().pad(10, 0, 10, 0);
        newProfileMenuTable.add(backButton).fillX().uniformX();
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

    private void showMenu(final Table menu, final Table callingTable) {
        menu.setVisible(true);
        callingTable.setVisible(false);
    }

    private void showMainMenu(Table callingTable) {
        showMenu(mainMenuTable, callingTable);
    }

    private void showSetSelectGameModeMenu(Table callingTable) {
        if(getMainController().noProfilesYet()){
            showNewProfileMenu(callingTable);
        } else{
            showMenu(selectGameModeTable, callingTable);
        }
    }

    private void showClientOrServerMenu(Table callingTable) {
        showMenu(clientOrServerMenuTable, callingTable);
    }

    private void showSelectProfileMenu(Table callingTable) {
        refreshProfilesTable();
        showMenu(selectProfileMenuTable, callingTable);
    }

    private void showNewProfileMenu(Table callingTable) {
        showMenu(newProfileMenuTable, callingTable);
    }

    private void showNewProfileMenu(Table callingTable, final Profile profile){
        profileNameTextField.setText(profile.getProfileName());
        difficultySelectBox.setSelected(profile.getPreferredDifficulty());
        showNewProfileMenu(callingTable);
    }

    private void showHighScoreMenu(Table callingTable) {
        refreshHighscoresTable();
        showMenu(highScoreMenuTable, callingTable);
    }

    private void showPreferencesMenu(Table callingTable) {
        showMenu(preferencesMenuTable, callingTable);
    }

    private void showServerListMenu(Table callingTable) {
        showMenu(serverListMenuTable, callingTable);
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
