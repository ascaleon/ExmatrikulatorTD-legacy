package de.diegrafen.exmatrikulatortd.view.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import de.diegrafen.exmatrikulatortd.controller.MainController;
import de.diegrafen.exmatrikulatortd.model.Highscore;
import de.diegrafen.exmatrikulatortd.view.screens.uielements.ProfileTextButton;

import java.util.LinkedList;
import java.util.List;

import static de.diegrafen.exmatrikulatortd.util.Assets.*;
import static de.diegrafen.exmatrikulatortd.util.Constants.*;

/**
 * Bildschirm für das Hauptmenü
 *
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 17.07.2019 19:26
 */
public class MenuScreen extends BaseScreen {

    /**
     * Tabelle für das Hauptmenü
     */
    private Table mainMenuTable;

    /**
     * Tabelle für das Einstellungs-Menü
     */
    private Table preferencesMenuTable;

    /**
     * Tabelle für das Untermenü, um entweder Single- oder Multiplayer zu spielen
     */
    private Table selectGameTypeTable;

    /**
     * Untermenü für den Singleplayer, um entweder einen Spielstand zu laden oder ein neues Spiel anzufangen
     */
    private Table loadOrNewGameTable;

    /**
     * Untermenü für die Verwaltung von Spielständen
     */
    private Table saveStatesMenuTable;

    /**
     * Tabelle zur Auflistung der gespeicherten Spielstände
     */
    private Table savestatesTable;

    /**
     * Menü für die Verwaltung von Profilen
     */
    private Table selectProfileMenuTable;

    /**
     * ButtonGroup, um die Menge an Buttons für jeweils ein Profil besser zu verwalten
     */
    private ButtonGroup<TextButton> profilesButtonGroup;

    /**
     * Tabelle zur Auflistung der Profile
     */
    private Table profilesTable;

    /**
     * Menü-Tabelle für das Bearbeiten oder Aktualisieren eines Profils
     */
    private Table newOrEditProfileMenuTable;

    /**
     * Ein Textfeld, in das der Name eines zu erstellenden oder zu aktualisierenden Profils eingetragen wird
     */
    private TextField profileNameTextField;

    /**
     * Die Auswahlbox für den Schwierigkeitsgrad eines Profils
     */
    private SelectBox<String> difficultySelectBox;

    /**
     * Die Highscore-Menü-Tabelle
     */
    private Table highScoreMenuTable;

    /**
     * Die Tabelle mit der Liste der Highscores
     */
    private Table highScoreTable;

    /**
     * Die Menü-Tabelle zur Auswahl von Client- oder Server-Modus
     */
    private Table clientOrServerMenuTable;

    /**
     * Das Menü, in dem die Liste der Server angezeigt wird
     */
    private Table serverListMenuTable;

    /**
     * Die Serverlisten-Tabelle
     */
    private Table serverListTable;

    private Table singlePlayerGameModeTable;

    private Table multiPlayerGameModeTable;

    private Table multiPlayerLoadMenuTable;

    private Table multiPlayerSaveStatesTable;

    /**
     * Die GameLobby-Tabelle
     */
    private Table gameLobbyTable;

    /**
     * Die Liste mit den verfügbaren Servern
     */
    private List<String> serverList;

    /**
     * Der Sprite des Hintergrunds
     */
    private Sprite backgroundSprite;

    /**
     * Der Skalierungsfaktor für eine "Kamerafahrt" über das Hintergrundbild
     */
    private float scaleFactor = 1;

    /**
     * Der Menü-Skin
     */
    private Skin skin;

    /**
     * Die aktuell ausgewählte Spielstands-ID, die geladen oder gelöscht werden soll
     */
    private Long idToLoad = -1L;

    /**
     * Button, der das Menü zum Aktualisieren und Erstellen von Profilen aufruft
     */
    private TextButton saveNewOrEditedProfileButton;

    /**
     * Gibt an, ob sich das Menü im Zustand "Profil aktualisieren" (true) oder "Profil bearbeiten" (false) befindet
     */
    private boolean updateProfile;

    /**
     * Speichert die auswählbaren Schwierigkeitsgrade
     */
    private Array<String> difficultyList;

    /**
     * Konstruktor für den Menü-Bildschirm
     *
     * @param mainController Der Main-Controller des Spiels
     * @param assetManager   Der Asset-Manager des Spiels
     */
    public MenuScreen(MainController mainController, AssetManager assetManager) {
        super(mainController, assetManager);
        this.serverList = new LinkedList<>();
    }

    /**
     * Initialisiert den Bildschirm
     */
    @Override
    public void init() {
        super.init();
        Texture backgroundTexture = getAssetManager().get(MENU_BACKGROUND_IMAGE, Texture.class);
        skin = getAssetManager().get(SKIN, Skin.class);
        saveNewOrEditedProfileButton = new TextButton(null, skin);
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
        createMultiPlayerLoadMenuTable(menuStack);
        createSelectProfileMenuTable(menuStack);
        createNewOrEditProfileMenuTable(menuStack);
        createHighscoreMenuTable(menuStack);
        createSelectClientOrServerMenu(menuStack);
        createServerListTable(menuStack);
        createGameLobbyTable(menuStack);

        getUi().addActor(menuStack);
    }

    /**
     * Setzt die Standardwerte für eine neue Menü-Tabelle und fügt sie dem Menu-Stack hinzu
     *
     * @param menuStack Der Menu-Stack, über den die einzelnen Untermenüs aufgerufen werden können
     * @param menuTable Die hinzuzufügende Tabelle
     */
    private void createGenericMenuTable(Stack menuStack, Table menuTable) {
        menuTable.setFillParent(true);
        menuTable.setVisible(false);
        menuStack.addActor(menuTable);
    }

    /**
     * Erzeugt das Hauptmenü
     *
     * @param menuStack Der Menü-Stack, über den die einzelnen Untermenüs aufgerufen werden können
     */
    private void createMainMenuTable(Stack menuStack) {
        mainMenuTable = new Table();
        TextButton newGame = new TextButton("Neues Spiel", skin);
        TextButton selectProfile = new TextButton("Profil auswählen", skin);
        TextButton highScores = new TextButton("Highscores", skin);
        TextButton preferences = new TextButton("Einstellungen", skin);
        TextButton exit = new TextButton("Spiel verlassen", skin);

        createGenericMenuTable(menuStack, mainMenuTable);
        // Da by-default alle Tabellen nicht sichtbar sind, müssen wir das Hauptmenu selbst auf visible setzen
        mainMenuTable.setVisible(true);

        addUIElement(mainMenuTable, newGame);
        addUIElement(mainMenuTable, selectProfile);
        addUIElement(mainMenuTable, highScores);
        // addUIElement(mainMenuTable, preferences); // Auskommentiert, da aktuell keine Einstellungen vorhanden sind
        addUIElement(mainMenuTable, exit);

        newGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                showSelectGameTypeMenu(mainMenuTable);
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

    /**
     * Erzeugt ein Menü zur Wahl zwischen Single- und Multiplayer
     *
     * @param menuStack Der Menü-Stack, über den die einzelnen Untermenüs aufgerufen werden können
     */
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

    /**
     * Aktualisiert die Tabelle mit den Spielständen
     */
    private void refreshSavestatesTable() {
        savestatesTable.clearChildren();
        getMainController().updateSaveStateButtons(this);
    }

    /**
     * Fügt dem einen neuen Button zur Auswahl von Spielständen hinzu
     *
     * @param text        Der Buttontext
     * @param saveStateId Die ID des Spielstands
     */
    public void addSaveStateButton(String text, long saveStateId, boolean multiplayer) {
        savestatesTable.row();
        TextButton savestateButton = new TextButton(text, skin);
        savestateButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                idToLoad = saveStateId;
            }
        });
        if (multiplayer) {
            multiPlayerSaveStatesTable.add(savestateButton);
        } else {
            savestatesTable.add(savestateButton);
        }
    }

    /**
     * Erzeugt ein Menü, bei dem man im Singleplayer auswählen kann, ob man ein neues Spiel
     * erstellen will oder einen Spielstand laden möchte.
     *
     * @param menuStack Der Menü-Stack, über den die einzelnen Untermenüs aufgerufen werden können
     */
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
                // Wenn das Menü für die Spielstände gezeigt werden soll, wird zuerst die Tabelle dazu aktualisiert
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

    /**
     * Aktualisiert die Tabelle mit den auswählbaren Profilen
     */
    private void refreshProfilesTable() {
        profilesButtonGroup.clear();
        // Damit nicht Profile, die vorher die Tabelle befüllten, doppelt auftauchen
        profilesTable.clearChildren();
        profilesTable.add(new Label("Profil auswählen.", skin));
        getMainController().updateProfileButtons(this);

    }

    /**
     * Fügt dem Profilauswahlbildschirm einen neuen Button hinzu
     *
     * @param profileName    Der Name des Profils
     * @param profileId      Die ID des Profils
     * @param currentProfile Gibt an, ob sich bei dem hinzugefügten Profil um das aktuell ausgewählte handelt
     */
    public void addProfileButton(final String profileName, final long profileId, final boolean currentProfile) {
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

        if (currentProfile) {
            profileButton.setChecked(true);
            profileButton.setColor(Color.GREEN);
        }

        profilesTable.add(profileButton);
        profilesButtonGroup.add(profileButton);
    }

    /**
     * Erzeugt das Menü zur Auswahl von Profilen
     *
     * @param menuStack Der Menü-Stack, über den die einzelnen Untermenüs aufgerufen werden können
     */
    private void createSelectProfileMenuTable(Stack menuStack) {
        selectProfileMenuTable = new Table();

        profilesTable = new Table();

        profilesButtonGroup = new ButtonGroup<>();
        profilesButtonGroup.setMaxCheckCount(1);
        profilesButtonGroup.setMinCheckCount(0);
        profilesButtonGroup.setUncheckLast(true);

        final ScrollPane profilesTableScrollPane = new ScrollPane(profilesTable, skin);

        // Aufrufen von refreshProfilesTable bewirkt bei Initialisierung, dass die Tabelle überhaupt erst befüllt wird
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
        createSaveStatesMenuTable(menuStack, saveStatesMenuTable, savestatesTable, false);
    }

    /**
     * Erzeugt das Menü zum Laden von Spielständen
     *
     * @param menuStack Der Menü-Stack, über den die einzelnen Untermenüs aufgerufen werden können
     */
    private void createSaveStatesMenuTable(Stack menuStack, Table menuTable, Table listTable, boolean multiplayer) {
        final ScrollPane savestatesTableScrollPane = new ScrollPane(listTable, skin);
        Table buttonsTable = new Table();
        TextButton loadButton = new TextButton("Spielstand laden", skin);
        TextButton deleteButton = new TextButton("Spielstand löschen", skin);
        TextButton backButton = new TextButton("Zurück", skin);

        listTable.pad(10).defaults().expandX().space(4);

        createGenericMenuTable(menuStack, menuTable);

        menuTable.row().pad(10, 0, 10, 0);

        loadButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (idToLoad != -1L) {
                    if (!multiplayer) {
                        getMainController().loadSinglePlayerGame(idToLoad);
                    } else {
                        getMainController().createServer(idToLoad);
                        getMainController().startServer();
                        showGameLobbyMenu(menuTable);
                    }
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
                if (multiplayer) {
                    showClientOrServerMenu(menuTable);
                } else {
                    showLoadOrNewGameMenu(menuTable);
                }
                backButton.setChecked(false);
            }
        });

        addUIElement(buttonsTable, loadButton);
        addUIElement(buttonsTable, deleteButton);
        addUIElement(buttonsTable, backButton);

        menuTable.add(savestatesTableScrollPane).expand();
        menuTable.add(buttonsTable).expand();
    }

    /**
     * Setzt das Profilerstellungs- und -bearbeitungsmenü zurück
     */
    private void cleanupNewOrEditProfileMenu() {
        // Damit nicht beim erneuten Öffnen des Menu die Werte von letzter Bearbeitung erhalten bleiben!
        profileNameTextField.setColor(Color.WHITE);
        profileNameTextField.setText("");
        difficultySelectBox.setSelected(EASY_STRING);
    }

    /**
     * Erzeugt as Menü zur Verwaltung von Profilen
     *
     * @param menuStack Der Menü-Stack, über den die einzelnen Untermenüs aufgerufen werden können
     */
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

    /**
     * Erzeugt ein Einstellungsmenü
     *
     * @param menuStack Der Menü-Stack, über den die einzelnen Untermenüs aufgerufen werden können
     */
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

    /**
     * Aktualisiert die Highscore-Tabelle
     */
    private void refreshHighscoresTable() {
        // Maximal 20 Highscores in der Liste
        List<Highscore> highscoreList = getMainController().retrieveHighscores(20);
        // Wie bei refreshProfilesTable
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
                    "\nDate played: " + DATE_FORMAT.format(highscore.getDatePlayed()), skin);
            rowTable.setDisabled(true);
            highScoreTable.add(rowTable);
        }
    }

    /**
     * Erzeugt einen Bildschirm, auf dem die Highscores angezeigt werden
     *
     * @param menuStack Der Menü-Stack, über den die einzelnen Untermenüs aufgerufen werden können
     */
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

    /**
     * Erzeugt einen Auswahlbildschirm für das Suchen oder Erstellen von Partien im Multiplayermodus
     *
     * @param menuStack Der Menü-Stack, über den die einzelnen Untermenüs aufgerufen werden können
     */
    private void createSelectClientOrServerMenu(Stack menuStack) {

        clientOrServerMenuTable = new Table();
        TextButton createGame = new TextButton("Spiel erstellen", skin);
        TextButton loadGame = new TextButton("Spiel laden", skin);
        TextButton searchGame = new TextButton("Spiel suchen", skin);
        TextButton backButton = new TextButton("Zurück", skin);

        createGenericMenuTable(menuStack, clientOrServerMenuTable);

        addUIElement(clientOrServerMenuTable, createGame);
        addUIElement(clientOrServerMenuTable, loadGame);
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

        loadGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                showMultiplayerLoadMenu(clientOrServerMenuTable);
            }
        });

        searchGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                getMainController().createClient();
                updateServerList();
                showServerListMenu(clientOrServerMenuTable);
            }
        });

        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                showSelectGameTypeMenu(clientOrServerMenuTable);
                backButton.setChecked(false);
            }
        });
    }

    private void showMultiplayerLoadMenu(Table callingTable) {
        showTable(callingTable, multiPlayerLoadMenuTable);
    }



    private void createMultiPlayerLoadMenuTable(Stack menuStack) {
        multiPlayerLoadMenuTable = new Table();
        multiPlayerSaveStatesTable = new Table();
        createSaveStatesMenuTable(menuStack, multiPlayerLoadMenuTable, multiPlayerSaveStatesTable, true);
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
                // Ist man nicht mehr im Begriff, mit jmd. anderem zu spielen, besteht auch kein Grund dazu, die Netzwerkfunktionalität laufen zu lassen
                getMainController().shutdownConnections();
                showClientOrServerMenu(gameLobbyTable);
                backButton.setChecked(false);
            }
        });
    }

    /**
     * Aktualisiert die Serverliste
     */
    private void updateServerList() {
        // Wie bei refreshProfilesTable
        serverListTable.clearChildren();

        serverList = getMainController().getLocalGameServers();

        for (String server : serverList) {
            String[] lines = server.split("\n");
            serverListTable.row();
            if (lines.length == 4) {
                int difficulty = Integer.parseInt(lines[3]);
                String difficultyString = difficultyList.get(difficulty);
                TextButton rowTable = new TextButton("Map: " + lines[1] + "\nAnzahl Spieler: " + lines[2] +
                        "\n Schwierigkeitsgrad: " + difficultyString, skin);
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

        TextButton refreshButton = new TextButton("Aktualisieren", skin);
        TextButton backButton = new TextButton("Zurück", skin);

        createGenericMenuTable(menuStack, serverListMenuTable);

        addUIElement(serverListMenuTable, serverScrollPane);
        addUIElement(serverListMenuTable, refreshButton);
        addUIElement(serverListMenuTable, backButton);

        refreshButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                updateServerList();
            }
        });

        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                getMainController().shutdownConnections();
                showClientOrServerMenu(serverListMenuTable);
                backButton.setChecked(false);
            }
        });
    }

    /**
     * Zeichnet die grafischen Element des Bildschirms
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
        getSpriteBatch().draw(backgroundSprite, -getCamera().viewportWidth / 2, -getCamera().viewportHeight / 2, getCamera().viewportWidth * scaleFactor, getCamera().viewportHeight * scaleFactor);
        getSpriteBatch().end();
    }

    /**
     * Blendet eine aufgerufene Tabelle ein, während die aufrufende Tabelle ausgeblendet wird
     *
     * @param callingTable Die aufrufende Tabelle
     * @param calledTable  Die aufgerufene Tabelle
     */
    private void showTable(Table callingTable, Table calledTable) {
        callingTable.setVisible(false);
        calledTable.setVisible(true);
    }

    /**
     * Zeigt das Hauptmenü an. Die aufrufende Tabelle
     * wird ausgeblendet
     *
     * @param callingTable Die aufrufende Tabelle
     */
    private void showMainMenu(Table callingTable) {
        showTable(callingTable, mainMenuTable);
    }

    /**
     * Zeigt ein Menü zur Auswahl zwischen Single- und Multiplayer an. Die aufrufende Tabelle
     * wird ausgeblendet
     *
     * @param callingTable Die aufrufende Tabelle
     */
    private void showSelectGameTypeMenu(Table callingTable) {
        if (getMainController().noProfilesYet()) {
            showNewProfileMenu(callingTable);
        } else if (!getMainController().hasCurrentProfile()) {
            showSelectProfileMenu(callingTable);
        } else {
            showTable(callingTable, selectGameTypeTable);
        }
    }

    /**
     * Zeigt ein Menü an, bei dem man im Singleplayer auswählen kann, ob man ein neues Spiel
     * erstellen will oder einen Spielstand laden möchte. Die aufrufende Tabelle wird
     * ausgeblendet
     *
     * @param callingTable Die aufrufende Tabelle
     */
    private void showLoadOrNewGameMenu(Table callingTable) {
        showTable(callingTable, loadOrNewGameTable);
    }

    /**
     * Zeigt das Menü zum Laden von Spielständen an, während die aufrufende Tabelle
     * ausgeblendet wird
     *
     * @param callingTable Die aufrufende Tabelle
     */
    private void showSaveStatesMenuTable(Table callingTable) {
        showTable(callingTable, saveStatesMenuTable);
    }

    /**
     * Zeigt das Menü zur Auswahl des Spielens als Client oder als Server an, während die aufrufende Tabelle
     * ausgeblendet wird
     *
     * @param callingTable Die aufrufende Tabelle
     */
    private void showClientOrServerMenu(Table callingTable) {
        showTable(callingTable, clientOrServerMenuTable);
    }

    /**
     * Zeigt die Spiellobby an, während die aufrufende Tabelle ausgeblendet wird
     *
     * @param callingTable Die aufrufende Tabelle
     */
    private void showGameLobbyMenu(Table callingTable) {
        showTable(callingTable, gameLobbyTable);
    }

    /**
     * Zeigt das Menü zum Auswählen von Profilen an, während die aufrufende Tabelle ausgeblendet wird
     *
     * @param callingTable Die aufrufende Tabelle
     */
    private void showSelectProfileMenu(Table callingTable) {
        refreshProfilesTable();
        showTable(callingTable, selectProfileMenuTable);
    }

    /**
     * Zeigt das Menü zum Erstellen von Profilen an, während die aufrufende Tabelle ausgeblendet wird
     *
     * @param callingTable Die aufrufende Tabelle
     */
    private void showNewProfileMenu(Table callingTable) {
        saveNewOrEditedProfileButton.setText("Profil erstellen");
        updateProfile = false;
        showTable(callingTable, newOrEditProfileMenuTable);
    }

    /**
     * Zeigt das Menü zum Bearbeiten von Profilen an, während die aufrufende Tabelle ausgeblendet wird
     *
     * @param callingTable Die aufrufende Tabelle
     */
    private void showEditProfileMenu(Table callingTable) {
        saveNewOrEditedProfileButton.setText("Profil aktualisieren");
        profileNameTextField.setText(getMainController().getCurrentProfileName());
        difficultySelectBox.setSelected(difficultyList.get(getMainController().getCurrentProfilePreferredDifficulty()));
        updateProfile = true;
        showTable(callingTable, newOrEditProfileMenuTable);
    }

    /**
     * Aktualisiert und zeigt die Highscore-Tabelle an, während die aufrufende Tabelle ausgeblendet wird
     *
     * @param callingTable Die aufrufende Tabelle
     */
    private void showHighScoreMenu(Table callingTable) {
        refreshHighscoresTable();
        showTable(callingTable, highScoreMenuTable);
    }

    /**
     * Zeigt das Menü für Einstellungen an, während die aufrufende Tabelle ausgeblendet wird
     * @param callingTable Die aufrufende Tabelle
     */
    private void showPreferencesMenu(Table callingTable) {
        showTable(callingTable, preferencesMenuTable);
    }

    /**
     * Zeigt das Untermenü für die Listung von Servern an, während die aufrufende Tabelle ausgeblendet wird
     * @param callingTable Die aufrufende Tabelle
     */
    private void showServerListMenu(Table callingTable) {
        showTable(callingTable, serverListMenuTable);
    }

    /**
     * Fügt einer Tabelle ein UI-Element hinzu
     *
     * @param table Die Tabelle, der das Element hinzugefügt werden soll
     * @param actor Das hinzuzufügende Element
     */
    private void addUIElement(Table table, Actor actor) {
        table.add(actor).fillX().uniformX();
        table.row().pad(10, 0, 0, 0);
    }

}
