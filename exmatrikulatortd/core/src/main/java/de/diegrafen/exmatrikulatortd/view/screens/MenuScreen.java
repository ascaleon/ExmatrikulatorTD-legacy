package de.diegrafen.exmatrikulatortd.view.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import de.diegrafen.exmatrikulatortd.controller.MainController;
import de.diegrafen.exmatrikulatortd.model.Highscore;
import de.diegrafen.exmatrikulatortd.model.Profile;
import de.diegrafen.exmatrikulatortd.model.SaveState;
import de.diegrafen.exmatrikulatortd.view.screens.uielements.ProfileTextButton;

import java.util.LinkedList;
import java.util.List;

import static de.diegrafen.exmatrikulatortd.util.Assets.MENU_BACKGROUND_IMAGE;
import static de.diegrafen.exmatrikulatortd.util.Assets.SINGLEPLAYER_MAP_PATH;
import static de.diegrafen.exmatrikulatortd.util.Constants.*;

/**
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 17.07.2019 19:26
 */
public class MenuScreen extends BaseScreen {

    private Table mainMenuTable;

    private Table preferencesMenuTable;

    private Table selectGameTypeTable;

    private Table loadOrNewGameTable;

    private Table saveStatesMenuTable;

    private Table savestatesTable;

    private Table selectProfileMenuTable;

    private ButtonGroup<TextButton> profilesButtonGroup;

    private Table profilesTable;

    private Table newOrEditProfileMenuTable;

    private TextField profileNameTextField;

    private SelectBox<String> difficultySelectBox;

    private Table highScoreMenuTable;

    private Table highScoreTable;

    private Table clientOrServerMenuTable;

    private Table serverListMenuTable;

    private Table serverListTable;

    private Table singlePlayerGameModeTable;

    private Table multiPlayerGameModeTable;

    private Table gameLobbyTable;

    private List<String> serverList;

    private Sprite backgroundSprite;

    private float scaleFactor = 1;

    private final Skin basicSkin = createBasicSkin();

    private final Skin skin = new Skin(Gdx.files.internal("ui-skin/golden-ui-skin.json"));

    private Long idToLoad = -1L;

    private TextButton saveNewOrEditedProfileButton = new TextButton(null, skin);

    private boolean updateProfile;

    private Array<String> difficultyList;

    public MenuScreen(MainController mainController, AssetManager assetManager) {
        super(mainController, assetManager);
        this.serverList = new LinkedList<>();
    }

    @Override
    public void init() {
        super.init();
        Texture backgroundTexture = getAssetManager().get(MENU_BACKGROUND_IMAGE, Texture.class);
        backgroundTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        backgroundSprite = new Sprite(backgroundTexture);
        difficultyList = new Array<>(false, 4);
        difficultyList.add(EASY_STRING);
        difficultyList.add(MEDIUM_STRING);
        difficultyList.add(HARD_STRING);
        difficultyList.add(TESTMODE_STRING);

        Gdx.input.setInputProcessor(getUi());

        Stack menuStack = new Stack();
        menuStack.setFillParent(true);

        createMainMenuTable(menuStack);

        createPreferenceMenuTable(menuStack);
        createSelectGameTypeTable(menuStack);
        createLoadOrNewGameTable(menuStack);
        createSaveStatesMenuTable(menuStack);
        createSelectProfileMenuTable(menuStack);
        createNewOrEditProfileMenuTable(menuStack);
        createHighscoreMenuTable(menuStack);
        createSelectClientOrServerMenu(menuStack);
        createServerListTable(menuStack);
        createGameLobbyTable(menuStack);

        getUi().addActor(menuStack);
    }

    private void createGenericMenuTable(Stack menuStack, Table menuTable) {
        menuTable.setFillParent(true);
        menuTable.setVisible(false);
        menuStack.addActor(menuTable);
    }

    private void createMainMenuTable(Stack menuStack) {
        mainMenuTable = new Table();
        TextButton newGame = new TextButton("Neues Spiel", skin);
        TextButton selectProfile = new TextButton("Profil auswählen", skin);
        TextButton highScores = new TextButton("Highscores", skin);
        TextButton preferences = new TextButton("Einstellungen", skin);
        TextButton exit = new TextButton("Spiel verlassen", skin);

        createGenericMenuTable(menuStack, mainMenuTable);
        mainMenuTable.setVisible(true);

        addUIElement(mainMenuTable, newGame);
        addUIElement(mainMenuTable, selectProfile);
        addUIElement(mainMenuTable, highScores);
        addUIElement(mainMenuTable, preferences);
        addUIElement(mainMenuTable, exit);

        newGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                showSelectGameTypeMenu(mainMenuTable);
                System.out.println("Ohai?");
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

    private void createSelectGameTypeTable(Stack menuStack) {
        selectGameTypeTable = new Table();
        TextButton newSinglePlayerGameButton = new TextButton("Singleplayer", skin);
        TextButton newMultiPlayerGameButton = new TextButton("Multiplayer", skin);
        TextButton backButton = new TextButton("Zurück", skin);

        createGenericMenuTable(menuStack, selectGameTypeTable);

        addUIElement(selectGameTypeTable, newSinglePlayerGameButton);
        addUIElement(selectGameTypeTable, newMultiPlayerGameButton);
        addUIElement(selectGameTypeTable, backButton);

        newSinglePlayerGameButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                showLoadOrNewGameMenu(selectGameTypeTable);
            }
        });

        newMultiPlayerGameButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                showClientOrServerMenu(selectGameTypeTable);
            }
        });

        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                showMainMenu(selectGameTypeTable);
                backButton.setChecked(false);
            }
        });
    }

    private void refreshSavestatesTable() {
        savestatesTable.clearChildren();
        getMainController().updateSaveStateButtons(this);
    }

    public void addSaveStateButton(String text, long saveStateId) {
        savestatesTable.row();
        TextButton savestateButton = new TextButton(text, skin);
        savestateButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                //getMainController().loadSinglePlayerGame(saveStateId);
                idToLoad = saveStateId;
            }
        });
        savestatesTable.add(savestateButton);
    }

    private void createLoadOrNewGameTable(Stack menuStack) {
        loadOrNewGameTable = new Table();

        TextButton newSinglePlayerGameButton = new TextButton("Neues Spiel", skin);
        TextButton loadSaveStateButton = new TextButton("Spiel laden", skin);
        TextButton backButton = new TextButton("Zurueck", skin);

        createGenericMenuTable(menuStack, loadOrNewGameTable);

        addUIElement(loadOrNewGameTable, newSinglePlayerGameButton);
        addUIElement(loadOrNewGameTable, loadSaveStateButton);
        addUIElement(loadOrNewGameTable, backButton);

        newSinglePlayerGameButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                getMainController().createNewSinglePlayerGame(STANDARD_SINGLE_PLAYER_GAME, getMainController().getCurrentProfilePreferredDifficulty(), SINGLEPLAYER_MAP_PATH);
                showMainMenu(selectGameTypeTable);
            }
        });

        loadSaveStateButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                refreshSavestatesTable();
                showSaveStatesMenuTable(loadOrNewGameTable);
            }
        });

        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                showSelectGameTypeMenu(loadOrNewGameTable);
                backButton.setChecked(false);
            }
        });
    }

    private void refreshProfilesTable() {
        profilesButtonGroup.clear();
        profilesTable.clearChildren();
        profilesTable.add(new Label("Profil auswählen.", skin));
        getMainController().updateProfileButtons(this);

    }

    public void addProfileButton(final String profileName, final long profileId, final boolean isCurrentProfile) {
        profilesTable.row();
        ProfileTextButton profileButton = new ProfileTextButton(profileName, skin, profileId);

        profileButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                profilesButtonGroup.getButtons().forEach(button -> button.setColor(Color.WHITE));
                getMainController().setCurrentProfile(profileId);
                profileButton.setColor(Color.GREEN);
                refreshSavestatesTable();
            }
        });

        if (isCurrentProfile) {
            profileButton.setChecked(true);
            profileButton.setColor(Color.GREEN);
        }

        profilesTable.add(profileButton);
        profilesButtonGroup.add(profileButton);
    }

    /**
     * Erzeugt das Menü zur Auswahl von Profilen
     *
     * @param menuStack
     */
    private void createSelectProfileMenuTable(Stack menuStack) {
        selectProfileMenuTable = new Table();

        profilesTable = new Table();

        profilesButtonGroup = new ButtonGroup<>();
        profilesButtonGroup.setMaxCheckCount(1);
        profilesButtonGroup.setMinCheckCount(0);
        profilesButtonGroup.setUncheckLast(true);

        final ScrollPane profilesTableScrollPane = new ScrollPane(profilesTable, skin);

        refreshProfilesTable();

        Table buttonsTable = new Table();

        TextButton createNewProfile = new TextButton("Neues Profil", skin);
        TextButton editProfile = new TextButton("Profil bearbeiten", skin);
        TextButton deleteProfile = new TextButton("Profil entfernen", skin);
        TextButton backButton = new TextButton("Zurück", skin);

        profilesTable.pad(10).defaults().expandX().space(4);

        createGenericMenuTable(menuStack, selectProfileMenuTable);

        selectProfileMenuTable.row().pad(10, 0, 10, 0);

        createNewProfile.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                showNewProfileMenu(selectProfileMenuTable);
            }
        });

        editProfile.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (getMainController().hasCurrentProfile()) {
                    showEditProfileMenu(selectProfileMenuTable);
                }
            }
        });

        deleteProfile.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

                final ProfileTextButton selectedProfileButton = (ProfileTextButton) profilesButtonGroup.getChecked();
                if (selectedProfileButton != null) {
                    getMainController().deleteProfile(selectedProfileButton.getID());
                }
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

        addUIElement(buttonsTable, createNewProfile);
        addUIElement(buttonsTable, editProfile);
        addUIElement(buttonsTable, deleteProfile);
        addUIElement(buttonsTable, backButton);

        selectProfileMenuTable.add(profilesTableScrollPane).expand();
        selectProfileMenuTable.add(buttonsTable).expand();
    }



    private void createSaveStatesMenuTable(Stack menuStack) {
        saveStatesMenuTable = new Table();
        savestatesTable = new Table();

        final ScrollPane savestatesTableScrollPane = new ScrollPane(savestatesTable, skin);
        Table buttonsTable = new Table();
        TextButton loadButton = new TextButton("Spielstand laden", skin);
        TextButton deleteButton = new TextButton("Spielstand löschen", skin);
        TextButton backButton = new TextButton("Zurück", skin);

        savestatesTable.pad(10).defaults().expandX().space(4);

        createGenericMenuTable(menuStack, saveStatesMenuTable);

        saveStatesMenuTable.row().pad(10, 0, 10, 0);

        loadButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (idToLoad != -1L) {
                    getMainController().loadSinglePlayerGame(idToLoad);
                }
                loadButton.setChecked(false);
            }
        });

        deleteButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                getMainController().deleteSaveState(idToLoad);
                idToLoad = -1L;
                refreshSavestatesTable();
            }
        });

        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                showLoadOrNewGameMenu(saveStatesMenuTable);
                backButton.setChecked(false);
            }
        });

        addUIElement(buttonsTable, loadButton);
        addUIElement(buttonsTable, deleteButton);
        addUIElement(buttonsTable, backButton);

        saveStatesMenuTable.add(savestatesTableScrollPane).expand();
        saveStatesMenuTable.add(buttonsTable).expand();
    }

    private void cleanupNewOrEditProfileMenu() {
        profileNameTextField.setColor(Color.WHITE);
        profileNameTextField.setText("");
        difficultySelectBox.setSelected(EASY_STRING);
    }

    private void createNewOrEditProfileMenuTable(Stack menuStack) {
        newOrEditProfileMenuTable = new Table();
        profileNameTextField = new TextField("", skin);
        difficultySelectBox = new SelectBox<>(skin);
        TextButton backButton = new TextButton("Zurück", skin);

        profileNameTextField.setMessageText("Name");
        profileNameTextField.setMaxLength(255);

        difficultySelectBox.setItems(difficultyList);

        saveNewOrEditedProfileButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                final String profileName = profileNameTextField.getText();
                if (!profileName.isEmpty()) {
                    int difficulty = difficultyList.indexOf((String) difficultySelectBox.getSelected(), false);
                    if (updateProfile) {
                        getMainController().updateProfile(profileName, difficulty, "");
                    } else {
                        getMainController().createNewProfile(profileName, difficulty, "");
                    }
                    cleanupNewOrEditProfileMenu();
                    showSelectProfileMenu(newOrEditProfileMenuTable);
                } else {
                    profileNameTextField.setColor(Color.RED);
                }
            }
        });

        createGenericMenuTable(menuStack, newOrEditProfileMenuTable);

        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                showSelectProfileMenu(newOrEditProfileMenuTable);
                backButton.setChecked(false);
                cleanupNewOrEditProfileMenu();
            }
        });

        addUIElement(newOrEditProfileMenuTable, profileNameTextField);
        addUIElement(newOrEditProfileMenuTable, difficultySelectBox);
        addUIElement(newOrEditProfileMenuTable, saveNewOrEditedProfileButton);
        addUIElement(newOrEditProfileMenuTable, backButton);
    }

    private void createPreferenceMenuTable(Stack menuStack) {

        // TODO: Einstellungsmöglichkeiten für Bildschirmgröße etc. hinzufügen
        // TODO: Auswahlmöglichkeit für Schwierigkeitsgrad hinzufügen

        preferencesMenuTable = new Table();
        TextButton backButton = new TextButton("Zurück", skin);

        createGenericMenuTable(menuStack, preferencesMenuTable);

        addUIElement(preferencesMenuTable, backButton);

        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                showMainMenu(preferencesMenuTable);
                backButton.setChecked(false);
            }
        });
    }

    private void refreshHighscoresTable() {
        List<Highscore> highscoreList = getMainController().retrieveHighscores(20);
        highScoreTable.clearChildren();

        for (Highscore highscore : highscoreList) {
            highScoreTable.row();

            String playerName;
            if (highscore.getProfile() == null) {
                playerName = "???";
            } else {
                playerName = highscore.getProfile().getProfileName();
            }

            TextButton rowTable = new TextButton("Player: " + playerName +
                    "\nScore: " + highscore.getScore() + " Round reached: " + highscore.getRoundNumberReached() +
                    "\nDate played: " + DATE_FORMAT.format(highscore.getDatePlayed()), basicSkin);
            rowTable.setDisabled(true);
            highScoreTable.add(rowTable);
        }
    }

    private void createHighscoreMenuTable(Stack menuStack) {

        highScoreMenuTable = new Table();
        highScoreTable = new Table();
        final ScrollPane upgradesScrollPane = new ScrollPane(highScoreTable, skin);

        highScoreTable.pad(10).defaults().expandX().space(4);

        refreshHighscoresTable();

        TextButton backButton = new TextButton("Zurück", skin);

        createGenericMenuTable(menuStack, highScoreMenuTable);

        addUIElement(highScoreMenuTable, upgradesScrollPane);
        addUIElement(highScoreMenuTable, backButton);

        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                showMainMenu(highScoreMenuTable);
                backButton.setChecked(false);
            }
        });
    }

    private void createSelectClientOrServerMenu(Stack menuStack) {

        clientOrServerMenuTable = new Table();
        TextButton createGame = new TextButton("Spiel erstellen", skin);
        TextButton searchGame = new TextButton("Spiel suchen", skin);
        TextButton backButton = new TextButton("Zurück", skin);

        createGenericMenuTable(menuStack, clientOrServerMenuTable);

        addUIElement(clientOrServerMenuTable, createGame);
        addUIElement(clientOrServerMenuTable, searchGame);
        addUIElement(clientOrServerMenuTable, backButton);

        createGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                getMainController().createServer();
                getMainController().startServer();
                showGameLobbyMenu(clientOrServerMenuTable);
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
                showSelectGameTypeMenu(clientOrServerMenuTable);
                //showMainMenu(clientOrServerMenuTable);
                backButton.setChecked(false);
            }
        });
    }

    /**
     * Erzeugt die Gamelobby für den Multiplayermodus
     *
     * @param menuStack Der Menü-Stack, über den die einzelnen Untermenüs aufgerufen werden können
     */
    private void createGameLobbyTable(Stack menuStack) {
        gameLobbyTable = new Table();
        Table playerTable = new Table();
        final ScrollPane playersScrollPane = new ScrollPane(playerTable, skin);

        TextButton backButton = new TextButton("Zurück", skin);
        TextButton setReadyButton = new TextButton("Bereit!", skin);

        createGenericMenuTable(menuStack, gameLobbyTable);

        addUIElement(gameLobbyTable, playersScrollPane);
        addUIElement(gameLobbyTable, setReadyButton);
        addUIElement(gameLobbyTable, backButton);

        setReadyButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                getMainController().toggleReady();
            }
        });

        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                getMainController().shutdownConnections();
                showMainMenu(gameLobbyTable);
                backButton.setChecked(false);
            }
        });
    }

    /**
     * Aktualisiert die Serverliste
     */
    private void updateServerList() {
        serverListTable.clearChildren();

        for (String server : serverList) {
            String[] lines = server.split("\n");
            serverListTable.row();
            System.out.println(lines.length);
            if (lines.length == 4) {
                System.out.println("Hallo?");
                int difficulty = Integer.parseInt(lines[3]);
                String difficultyString = difficultyList.get(difficulty);
                TextButton rowTable = new TextButton("Map: " + lines[1] + "\nAnzahl Spieler: " + lines[2] +
                        "\n Schwierigkeitsgrad: " + difficultyString, basicSkin);
                serverListTable.add(rowTable);
                rowTable.addListener(new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        getMainController().setHostAdress(lines[0]);
                        if (getMainController().connectToServer(lines[0])) {
                            showGameLobbyMenu(serverListMenuTable);
                        }
                    }
                });
            }
        }
    }

    /**
     * Erzeugt das Untermenü mit der Liste der Server im lokalen Netzwerk
     *
     * @param menuStack Der Menü-Stack, über den die einzelnen Untermenüs aufgerufen werden können
     */
    private void createServerListTable(Stack menuStack) {

        serverListMenuTable = new Table();
        serverListTable = new Table();
        final ScrollPane serverScrollPane = new ScrollPane(serverListTable, skin);

        TextButton connectButton = new TextButton("Verbinden", skin);
        TextButton backButton = new TextButton("Zurück", skin);

        createGenericMenuTable(menuStack, serverListMenuTable);

        addUIElement(serverListMenuTable, serverScrollPane);
        addUIElement(serverListMenuTable, connectButton);
        addUIElement(serverListMenuTable, backButton);

        connectButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (getMainController().connectToServer(getMainController().getHostAdress())) {
                    showGameLobbyMenu(serverListMenuTable);
                }
            }
        });

        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                getMainController().shutdownConnections();
                showMainMenu(serverListMenuTable);
                backButton.setChecked(false);
            }
        });
    }

    /**
     * Eigene Zeichenanweisungen.
     *
     * @param deltaTime Die Zeit in Sekunden seit dem letzten Frame.
     */
    @Override
    public void draw(float deltaTime) {
        super.draw(deltaTime);

        // Scalefaktor für Kamerafahrt.
        if (scaleFactor < 1.3) {
            scaleFactor += (0.002) * deltaTime;
        }

        getSpriteBatch().setProjectionMatrix(getCamera().combined);
        getSpriteBatch().begin();
        // FIXME: Kamerafahrt funktioniert irgendwie noch nicht.
        getSpriteBatch().draw(backgroundSprite, -getCamera().viewportWidth / 2, -getCamera().viewportHeight / 2, getCamera().viewportWidth * scaleFactor, getCamera().viewportHeight * scaleFactor);
        getSpriteBatch().end();
    }

    private void showTable(Table callingTable, Table calledTable) {
        callingTable.setVisible(false);
        calledTable.setVisible(true);
    }

    private void showMainMenu(Table callingTable) {
        showTable(callingTable, mainMenuTable);
    }

    private void showSelectGameTypeMenu(Table callingTable) {
        if (getMainController().noProfilesYet()) {
            showNewProfileMenu(callingTable);
            System.out.println("Hallo?");
        } else if (!getMainController().hasCurrentProfile()) {
            showSelectProfileMenu(callingTable);
        } else {
            showTable(callingTable, selectGameTypeTable);
        }
    }

    private void showLoadOrNewGameMenu(Table callingTable) {
        showTable(callingTable, loadOrNewGameTable);
    }

    private void showSaveStatesMenuTable(Table callingTable) {
        showTable(callingTable, saveStatesMenuTable);
    }

    private void showClientOrServerMenu(Table callingTable) {
        showTable(callingTable, clientOrServerMenuTable);
    }

    private void showGameLobbyMenu(Table callingTable) {
        showTable(callingTable, gameLobbyTable);
    }

    private void showSelectProfileMenu(Table callingTable) {
        refreshProfilesTable();
        showTable(callingTable, selectProfileMenuTable);
    }

    private void showNewProfileMenu(Table callingTable) {
        saveNewOrEditedProfileButton.setText("Profil erstellen");
        updateProfile = false;
        showTable(callingTable, newOrEditProfileMenuTable);
    }

    private void showEditProfileMenu(Table callingTable) {
        saveNewOrEditedProfileButton.setText("Profil aktualisieren");
        profileNameTextField.setText(getMainController().getCurrentProfileName());
        difficultySelectBox.setSelected(difficultyList.get(getMainController().getCurrentProfilePreferredDifficulty()));
        updateProfile = true;
        showTable(callingTable, newOrEditProfileMenuTable);
    }

    private void showHighScoreMenu(Table callingTable) {
        refreshHighscoresTable();
        showTable(callingTable, highScoreMenuTable);
    }

    private void showPreferencesMenu(Table callingTable) {
        showTable(callingTable, preferencesMenuTable);
    }

    private void showServerListMenu(Table callingTable) {
        showTable(callingTable, serverListMenuTable);
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

    private void addUIElement(Table table, Actor button) {
        table.add(button).fillX().uniformX();
        table.row().pad(10, 0, 0, 0);
    }

}
