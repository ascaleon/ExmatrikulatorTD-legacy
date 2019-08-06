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
import de.diegrafen.exmatrikulatortd.controller.MainController;
import de.diegrafen.exmatrikulatortd.model.Difficulty;
import de.diegrafen.exmatrikulatortd.model.Highscore;
import de.diegrafen.exmatrikulatortd.model.Profile;

import java.util.LinkedList;

import static de.diegrafen.exmatrikulatortd.controller.factories.NewGameFactory.STANDARD_SINGLE_PLAYER_GAME;
import static de.diegrafen.exmatrikulatortd.util.Assets.MENU_BACKGROUND_IMAGE;
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

    private Table loadOrNewGameTable;

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

    private Table clientOrServerMenuTable;

    private Table serverListMenuTable;

    private Table serverListTable;
    
    private Table singlePlayerGameModeTable;

    private Table multiPlayerGameModeTable;

    private Table gameLobbyTable;

    private java.util.List<String> serverList;

    private Sprite backgroundSprite;

    private float scaleFactor = 1;

    private final Skin basicSkin=createBasicSkin();

    private final Skin skin=new Skin(Gdx.files.internal("ui-skin/golden-ui-skin.json"));

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

        Gdx.input.setInputProcessor(getUi());

        Stack menuStack = new Stack();
        menuStack.setFillParent(true);

        createMainMenuTable(menuStack);

        createPreferenceMenuTable(menuStack);
        createSelectGameTypeTable(menuStack);
        createSelectProfileMenuTable(menuStack);
        createNewProfileMenuTable(menuStack);
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

        createGenericMenuTable(menuStack,mainMenuTable);
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

    private void createSelectGameTypeTable(Stack menuStack) {
        selectGameTypeTable = new Table();
        TextButton newSinglePlayerGameButton = new TextButton("Singleplayer", skin);
        TextButton newMultiPlayerGameButton = new TextButton("Multiplayer", skin);
        TextButton backButton = new TextButton("Zurück", skin);

        createGenericMenuTable(menuStack,selectGameTypeTable);

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

    private void refreshSavestatesTable(){
        savestatesTable.clearChildren();
    }

    private void createLoadOrNewGameTable(Stack menuStack){
        loadOrNewGameTable = new Table();

        TextButton newSinglePlayerGameButton = new TextButton("Neues Spiel",skin);
        TextButton loadSaveStateButton = new TextButton("Spiel laden", skin);
        TextButton backButton = new TextButton("Zurueck", skin);

        createGenericMenuTable(menuStack, loadOrNewGameTable);

        loadOrNewGameTable.add(newSinglePlayerGameButton).fillX().uniformX();
        loadOrNewGameTable.row().pad(10, 0, 10, 0);
        loadOrNewGameTable.add(loadSaveStateButton).fillX().uniformX();
        loadOrNewGameTable.row().pad(10, 0, 10, 0);
        loadOrNewGameTable.add(backButton).fillX().uniformX();

        newSinglePlayerGameButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                getMainController().createNewSinglePlayerGame(STANDARD_SINGLE_PLAYER_GAME, SINGLEPLAYER_MAP_PATH);
                showMainMenu(selectGameTypeTable);
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

    private void createPreferenceMenuTable(Stack menuStack) {

        // TODO: Einstellungsmöglichkeiten für Bildschirmgröße etc. hinzufügen
        // TODO: Auswahlmöglichkeit für Schwierigkeitsgrad hinzufügen

        preferencesMenuTable = new Table();
        TextButton backButton = new TextButton("Zurück", skin);

        createGenericMenuTable(menuStack,preferencesMenuTable);

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

        createGenericMenuTable(menuStack,highScoreMenuTable);

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

        createGenericMenuTable(menuStack,clientOrServerMenuTable);

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

    private void createGameLobbyTable(Stack menuStack) {

        gameLobbyTable = new Table();
        Table playerTable = new Table();
        final ScrollPane.ScrollPaneStyle scrollPaneStyle = new ScrollPane.ScrollPaneStyle();
        final ScrollPane playersScrollPane = new ScrollPane(playerTable, scrollPaneStyle);

        TextButton backButton = new TextButton("Zurück", skin);

        createGenericMenuTable(menuStack,gameLobbyTable);

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
        final ScrollPane.ScrollPaneStyle scrollPaneStyle = new ScrollPane.ScrollPaneStyle();
        final ScrollPane upgradesScrollPane = new ScrollPane(serverListTable, scrollPaneStyle);

        TextButton connectButton = new TextButton("Verbinden", skin);
        TextButton backButton = new TextButton("Zurück", skin);

        createGenericMenuTable(menuStack,serverListMenuTable);

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
                showMainMenu(serverListMenuTable);
                backButton.setChecked(false);
            }
        });

        serverListMenuTable.add(connectButton).fillX().uniformX();
        serverListMenuTable.row();
        serverListMenuTable.add(backButton).fillX().uniformX();
        serverListMenuTable.row().pad(10, 0, 10, 0);

        //updateServerList();
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

    private void createSaveGameMenu() {

    }

    private void createNewSinglePlayerGameMenu() {

    }

    private void showTable(Table callingTable, Table calledTable) {
        callingTable.setVisible(false);
        calledTable.setVisible(true);
    }

    private void showMainMenu(Table callingTable) {
        showTable(callingTable,mainMenuTable);
    }

    private void showSelectGameTypeMenu(Table callingTable) {
        if(getMainController().noProfilesYet()){
            showNewProfileMenu(callingTable);
        } else{
            showTable(callingTable,selectGameTypeTable);
        }
    }

    private void showLoadOrNewGameMenu(Table callingTable){
        showTable(loadOrNewGameTable, callingTable);
    }

    private void showClientOrServerMenu(Table callingTable) {
        showTable(clientOrServerMenuTable, callingTable);
    }

    private void showSelectProfileMenu(Table callingTable) {
        refreshProfilesTable();
        showTable(callingTable,selectProfileMenuTable);
    }

    private void showNewProfileMenu(Table callingTable) {
        showTable(callingTable,newProfileMenuTable);
    }

    private void showNewProfileMenu(Table callingTable, final Profile profile){
        profileNameTextField.setText(profile.getProfileName());
        difficultySelectBox.setSelected(profile.getPreferredDifficulty());
        showNewProfileMenu(callingTable);
    }

    private void showHighScoreMenu(Table callingTable) {
        refreshHighscoresTable();
        showTable(callingTable,highScoreMenuTable);
    }

    private void showPreferencesMenu(Table callingTable) {
        showTable(callingTable,preferencesMenuTable);
    }

    private void showServerListMenu(Table callingTable) {
        showTable(callingTable,serverListTable);
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
