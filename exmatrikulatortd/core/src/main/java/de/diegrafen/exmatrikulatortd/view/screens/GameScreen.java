package de.diegrafen.exmatrikulatortd.view.screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import de.diegrafen.exmatrikulatortd.ExmatrikulatorTD;
import de.diegrafen.exmatrikulatortd.communication.client.GameClient;
import de.diegrafen.exmatrikulatortd.communication.server.GameServer;
import de.diegrafen.exmatrikulatortd.controller.MainController;
import de.diegrafen.exmatrikulatortd.controller.gamelogic.LogicController;
import de.diegrafen.exmatrikulatortd.model.*;
import de.diegrafen.exmatrikulatortd.persistence.GameStateDao;
import de.diegrafen.exmatrikulatortd.view.gameobjects.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static com.badlogic.gdx.Input.Buttons.LEFT;
import static com.badlogic.gdx.Input.Buttons.MIDDLE;
import static com.badlogic.gdx.Input.Buttons.RIGHT;
import static de.diegrafen.exmatrikulatortd.controller.factories.EnemyFactory.HEAVY_ENEMY;
import static de.diegrafen.exmatrikulatortd.controller.factories.EnemyFactory.REGULAR_ENEMY;
import static de.diegrafen.exmatrikulatortd.controller.factories.NewGameFactory.ENDLESS_SINGLE_PLAYER_GAME;
import static de.diegrafen.exmatrikulatortd.controller.factories.TowerFactory.*;
import static de.diegrafen.exmatrikulatortd.util.Assets.MAP_PATH;

/**
 * Der GameScreen wird während des aktuellen Spiels angezeigt.
 *
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 14.06.2019 02:12
 */
public class GameScreen extends BaseScreen implements GameView {

    /**
     * Der aktuelle Spielstand.
     */
    private Gamestate gameState;

    /**
     * Das Database-Access-Objekt für die Durchführung von CRUD-Operationen
     */
    private GameStateDao gameStateDao;

    /**
     * Ist das aktuelle Spiel Single oder Multiplayer.
     */
    private boolean multiPlayer;

    /**
     * Das Spielobjekt.
     */
    private ExmatrikulatorTD game;

    /**
     * Die Karte, auf der die Türme plaziert werden, und sich die Gegner bewegen
     */
    private TiledMap tiledMap;

    /**
     * Rendert die tiledMap
     */
    private OrthogonalTiledMapRenderer orthogonalTiledMapRenderer;

    private List<Player> players;

    /**
     * Die Breite der Karte in Pixeln
     */
    private int mapWidth;


    /**
     * Die Höhe der Karte in Pixeln
     */
    private int mapHeight;

    /**
     * Eine Liste aller Spielobjekte
     */
    private List<GameObject> gameObjects;

    private boolean keyDownDown = false;

    private boolean keyUpDown = false;

    private boolean keyRightDown = false;

    private boolean keyLeftDown = false;

    private Label scoreLabel;

    private Label resourcesLabel;

    private Label livesLabel;

    private Label roundsLabel;

    private float touchDownX, touchDownY;

    private InputMultiplexer multiplexer;

    // TODO: Variablen sinnvollere Bezeichnungen geben bzw. ersetzen

    private boolean t1 = false;

    private boolean t2 = false;

    private boolean t3 = false;

    private boolean t4 = false;

    private boolean u = false;

    private boolean s = false;

    private LogicController logicController;

    /**
     * Der Konstruktor legt den MainController und das Spielerprofil fest. Außerdem erstellt er den Gamestate und den logicController.
     *
     * @param mainController Der Maincontrroller.
     * @param playerProfile  Das Spielerprofil.
     */
    public GameScreen(MainController mainController, LogicController logicController) {
        super(mainController);
        this.logicController = logicController;
        this.gameState = logicController.getGamestate();
        gameState.registerObserver(this);
        this.players = gameState.getPlayers();
        for (Player player : gameState.getPlayers()) {
            player.registerObserver(this);
        }
    }


    /**
     * Die Initialisierung erstellt den SpriteBatch und lädt Texturen.
     */
    @Override
    public void init() {
        super.init();

        float width = Gdx.graphics.getWidth();
        float height = Gdx.graphics.getHeight();

        this.gameObjects = new LinkedList<>();

        getCamera().setToOrtho(false, width, height);

        multiplexer = new InputMultiplexer();

        //Gdx.input.setInputProcessor(new InputMultiplexer());

        //InputMultiplexer multiplexer = (InputMultiplexer) Gdx.input.getInputProcessor();

        InputProcessor inputProcessorCam = new InputProcessor() {
            @Override
            public boolean keyDown(int keycode) {

                int localPlayerNumber = logicController.getLocalPlayerNumber();

                if (keycode == Input.Keys.LEFT)
                    keyLeftDown = true;
                if (keycode == Input.Keys.RIGHT)
                    keyRightDown = true;
                if (keycode == Input.Keys.UP)
                    keyUpDown = true;
                if (keycode == Input.Keys.DOWN)
                    keyDownDown = true;
                if (keycode == Input.Keys.I) {

                    logicController.sendEnemy(REGULAR_ENEMY, localPlayerNumber, localPlayerNumber);
                }
                if (keycode == Input.Keys.O) {
                    logicController.sendEnemy(HEAVY_ENEMY, localPlayerNumber, localPlayerNumber);
                }
                return false;
            }

            @Override
            public boolean keyUp(int keycode) {
                if (keycode == Input.Keys.LEFT)
                    keyLeftDown = false;
                if (keycode == Input.Keys.RIGHT)
                    keyRightDown = false;
                if (keycode == Input.Keys.UP)
                    keyUpDown = false;
                if (keycode == Input.Keys.DOWN)
                    keyDownDown = false;
                return false;
            }

            @Override
            public boolean keyTyped(char character) {
                return false;
            }

            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {

                if (pointer == LEFT) {
                    Vector3 clickCoordinates = new Vector3(screenX, screenY, 0);
                    Vector3 position = getCamera().unproject(clickCoordinates);
                    touchDownX = position.x;
                    touchDownY = position.y;
                }

                return false;
            }

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {

                boolean returnvalue = false;

                Vector3 clickCoordinates = new Vector3(screenX, screenY, 0);
                Vector3 position = getCamera().unproject(clickCoordinates);

                int xCoordinate = logicController.getXCoordinateByPosition(position.x);
                int yCoordinate = logicController.getYCoordinateByPosition(position.y);

                int localPlayerNumber = logicController.getLocalPlayerNumber();

                if (button == RIGHT) {
                    returnvalue = true;
                } else if (button == LEFT) {
                    if (logicController.checkIfCoordinatesAreBuildable(xCoordinate, yCoordinate, localPlayerNumber)) {
                        if (t1) {
                            logicController.buildTower(REGULAR_TOWER, xCoordinate, yCoordinate, localPlayerNumber);
                        } else if (t2) {
                            logicController.buildTower(EXPLOSIVE_TOWER, xCoordinate, yCoordinate, localPlayerNumber);
                        } else if (t3) {
                            logicController.buildTower(CORRUPTION_TOWER, xCoordinate, yCoordinate, localPlayerNumber);
                        } else if (t4) {
                            logicController.buildTower(AURA_TOWER, xCoordinate, yCoordinate, localPlayerNumber);
                        }
                    } else if (logicController.hasCellTower(xCoordinate, yCoordinate)) {
                        if (s) {
                            logicController.sellTower(xCoordinate, yCoordinate, localPlayerNumber);
                        } else if (u) {
                            logicController.upgradeTower(xCoordinate, yCoordinate, localPlayerNumber);
                        }
                    }
                    returnvalue = true;
                }

                return returnvalue;
            }

            @Override
            public boolean touchDragged(int screenX, int screenY, int pointer) {

                Vector3 clickCoordinates = new Vector3(screenX, screenY, 0);
                Vector3 position = getCamera().unproject(clickCoordinates);

                getCamera().position.x += touchDownX - position.x;// (position.x - touchDownX, position.y - touchDownY);
                getCamera().position.y += touchDownY - position.y;//

                resetCameraToBorders();

                return false;
            }

            @Override
            public boolean mouseMoved(int screenX, int screenY) {
                return false;
            }

            @Override
            public boolean scrolled(int amount) {
                return false;
            }
        };

        //multiplexer.addProcessor(inputProcessorCam);

        initializeUserInterface();

        multiplexer.addProcessor(inputProcessorCam);
        Gdx.input.setInputProcessor(multiplexer);
    }

    /**
     * Wird immer nach einem Bestimmten Zeitabstand aufgerufen und die Logik des Spiels berechnet, damit danach in render() neu gezeichnet werden kann.
     *
     * @param deltaTime Die Zeit in Sekunden seit dem letzten Frame.
     */
    @Override
    public void update(float deltaTime) {
        if (!isPause()) {
            logicController.update(deltaTime);
        }
    }

    @Override
    public void update() {
        Player localPlayer = players.get(logicController.getLocalPlayerNumber());

        scoreLabel.setText(localPlayer.getScore());
        livesLabel.setText(localPlayer.getCurrentLives() + "/" + localPlayer.getMaxLives());
        resourcesLabel.setText(localPlayer.getResources());
        if (gameState.isEndlessGame()) {
            roundsLabel.setText(Integer.toString(gameState.getRoundNumber() + 1));
        } else {
            roundsLabel.setText((gameState.getRoundNumber() + 1) + "/" + gameState.getNumberOfRounds());
        }
    }

    /**
     * Eigene Zeichenanweisungen.
     *
     * @param deltaTime Die Zeit in Sekunden seit dem letzten Frame.
     */
    @Override
    public void draw(float deltaTime) {
        super.draw(deltaTime);

        float translateValue = 5;

        if (keyLeftDown) {
            getCamera().translate(-translateValue, 0);
            System.out.println("Keyleftdown");
        }
        if (keyRightDown) {
            getCamera().translate(translateValue, 0);
        }
        if (keyUpDown) {
            getCamera().translate(0, translateValue);
        }
        if (keyDownDown) {
            getCamera().translate(0, -translateValue);
        }

        resetCameraToBorders();

        getCamera().update();
        if (orthogonalTiledMapRenderer != null) {
            orthogonalTiledMapRenderer.setView(getCamera());
            orthogonalTiledMapRenderer.render();
        }

        getSpriteBatch().setProjectionMatrix(getCamera().combined);
        getSpriteBatch().begin();

        List<GameObject> objectsToRemove = new ArrayList<>();

        if (gameObjects != null) {
            for (GameObject gameObject : gameObjects) {
                if (gameObject.isRemoved()) {
                    objectsToRemove.add(gameObject);
                } else {
                    if (isPause() | gameState.isGameOver()) {
                        gameObject.setAnimated(false);
                    } else {
                        gameObject.setAnimated(true);
                    }
                    gameObject.draw(getSpriteBatch(), deltaTime);
                }
            }
        }
        objectsToRemove.forEach(this::removeGameObject);

        getSpriteBatch().end();
    }

    /**
     * Gibt nicht mehr benötigte Ressourcen frei, um Speicherlecks zu vermeiden.
     */
    @Override
    public void dispose() {
        super.dispose();
        gameObjects.forEach(GameObject::dispose);
        tiledMap.dispose();
    }

    /**
     * Lädt die Karte.
     *
     * @param mapPath Der Pfad zur .tmx-Datei, die die Karte beinhaltet
     */
    public void loadMap(String mapPath) {
        tiledMap = new TmxMapLoader().load(mapPath);
        MapProperties mapProperties = tiledMap.getProperties();
        mapWidth = mapProperties.get("width", Integer.class) * mapProperties.get("tilewidth", Integer.class);
        mapHeight = mapProperties.get("height", Integer.class) * mapProperties.get("tileheight", Integer.class);
        orthogonalTiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);
        getCamera().update();
        orthogonalTiledMapRenderer.setView(getCamera());
    }

    private void initializeUserInterface() {

        int sizeX = 100;
        int sizeY = 100;


        final Stack mainUiStack = new Stack();
        mainUiStack.setFillParent(true);

        final Table defaultScreen = new Table();
        defaultScreen.setFillParent(true);

        final Table statsTable = new Table();
        //statsTable.setBackground(background);
        Label.LabelStyle infoLabelsStyle = new Label.LabelStyle();
        infoLabelsStyle.font = getBitmapFont();
        Label.LabelStyle scoreLabelStyle = new Label.LabelStyle();
        scoreLabelStyle.font = getBitmapFont();
        Label.LabelStyle liveLabelStyle = new Label.LabelStyle();
        liveLabelStyle.font = getBitmapFont();
        Label.LabelStyle roundLabelStyle = new Label.LabelStyle();
        liveLabelStyle.font = getBitmapFont();

        Player localPlayer = players.get(logicController.getLocalPlayerNumber());

        // score
        statsTable.add(new Label("Punkte: ", infoLabelsStyle)).left().padLeft(10).expandX();
        scoreLabel = new Label(Integer.toString(localPlayer.getScore()), scoreLabelStyle);
        statsTable.add(scoreLabel).left().align(RIGHT);
        statsTable.row();
        // money
        statsTable.add(new Label("Geld: ", infoLabelsStyle)).left().padLeft(10).expandX();
        resourcesLabel = new Label(Integer.toString(localPlayer.getResources()), scoreLabelStyle);
        statsTable.add(resourcesLabel).left().align(RIGHT);
        statsTable.row();
        // lives
        statsTable.add(new Label("Leben: ", infoLabelsStyle)).left().padLeft(10).expandX();
        livesLabel = new Label(localPlayer.getCurrentLives() + "/" + localPlayer.getMaxLives(), liveLabelStyle);
        statsTable.add(livesLabel).left().align(RIGHT);
        statsTable.row();
        // Rounds
        statsTable.add(new Label("Semester: ", infoLabelsStyle)).left().padLeft(10).expandX();
        String roundsLabelText = (gameState.getRoundNumber() + 1) + "/" + gameState.getNumberOfRounds();
        System.out.println();
        roundsLabel = new Label(roundsLabelText, liveLabelStyle);
        statsTable.add(roundsLabel).left().align(RIGHT);
        statsTable.row();

        //Tower selection es können ganz einfach mehr Buttons mit copy paste erstellt werden.
        Skin skin = new Skin(Gdx.files.internal("ui-skin/glassy-ui.json"));
        TextButtonStyle style = new TextButtonStyle();
        final Table towerSelect = new Table();
        //towerSelect.setDebug(true);

        //Die einzelnen Towerbuttons
        TextButton tower1 = new TextButton("T1", skin);
        TextButton tower2 = new TextButton("T2", skin);
        TextButton tower3 = new TextButton("T3", skin);
        TextButton tower4 = new TextButton("T4", skin);
        TextButton upgrade = new TextButton("^", skin);
        TextButton sell = new TextButton("$$$", skin);
//        tower1.setSize(10, 10);
//        tower2.setSize(10, 10);
//        tower3.setSize(10, 10);
//        tower4.setSize(10, 10);

        //Nur nen paar parameter, ka ob die überhaupt noch gebraucht werden
        tower1.getLabel().setFontScale(1, 1);
        tower2.getLabel().setFontScale(1,1);
        tower3.getLabel().setFontScale(1,1);
        tower4.getLabel().setFontScale(1,1);
        //tower4.setColor(Color.WHITE);

        //InputListener für die Buttons
        tower1.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                    tower1.setColor(Color.GREEN);
                    t1 = true;
                    tower2.setColor(Color.valueOf("ffffffff"));
                    t2 = false;
                    tower3.setColor(Color.valueOf("ffffffff"));
                    t3 = false;
                    tower4.setColor(Color.valueOf("ffffffff"));
                    t4 = false;
                    upgrade.setColor(Color.valueOf("ffffffff"));
                    u = false;
                    sell.setColor(Color.valueOf("ffffffff"));
                    s = false;
            }
        });
        tower2.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                tower1.setColor(Color.valueOf("ffffffff"));
                t1 = false;
                tower2.setColor(Color.GREEN);
                t2 = true;
                tower3.setColor(Color.valueOf("ffffffff"));
                t3 = false;
                tower4.setColor(Color.valueOf("ffffffff"));
                t4 = false;
                upgrade.setColor(Color.valueOf("ffffffff"));
                u = false;
                sell.setColor(Color.valueOf("ffffffff"));
                s = false;
            }
        });
        tower3.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                tower1.setColor(Color.valueOf("ffffffff"));
                t1 = false;
                tower2.setColor(Color.valueOf("ffffffff"));
                t2 = false;
                tower3.setColor(Color.GREEN);
                t3 = true;
                tower4.setColor(Color.valueOf("ffffffff"));
                t4 = false;
                upgrade.setColor(Color.valueOf("ffffffff"));
                u = false;
                sell.setColor(Color.valueOf("ffffffff"));
                s = false;
            }
        });
        tower4.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                tower1.setColor(Color.valueOf("ffffffff"));
                t1 = false;
                tower2.setColor(Color.valueOf("ffffffff"));
                t2 = false;
                tower3.setColor(Color.valueOf("ffffffff"));
                t3 = false;
                tower4.setColor(Color.GREEN);
                t4 = true;
                upgrade.setColor(Color.valueOf("ffffffff"));
                u = false;
                sell.setColor(Color.valueOf("ffffffff"));
                s = false;
            }
        });
        upgrade.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                tower1.setColor(Color.valueOf("ffffffff"));
                t1 = false;
                tower2.setColor(Color.valueOf("ffffffff"));
                t2 = false;
                tower3.setColor(Color.valueOf("ffffffff"));
                t3 = false;
                tower4.setColor(Color.valueOf("ffffffff"));
                t4 = false;
                upgrade.setColor(Color.GREEN);
                u = true;
                sell.setColor(Color.valueOf("ffffffff"));
                s = false;
            }
        });
        sell.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                tower1.setColor(Color.valueOf("ffffffff"));
                t1 = false;
                tower2.setColor(Color.valueOf("ffffffff"));
                t2 = false;
                tower3.setColor(Color.valueOf("ffffffff"));
                t3 = false;
                tower4.setColor(Color.valueOf("ffffffff"));
                t4 = false;
                upgrade.setColor(Color.valueOf("ffffffff"));
                u = false;
                sell.setColor(Color.GREEN);
                s = true;
            }
        });
        //Towerbuttons der Tabelle hinzufügen
        towerSelect.add(tower1).size(sizeX, sizeY).spaceRight(5);
        towerSelect.add(tower2).size(sizeX, sizeY).spaceRight(5);
        towerSelect.add(tower3).size(sizeX, sizeY).spaceRight(5);
        towerSelect.add(tower4).size(sizeX, sizeY).spaceRight(10);
        towerSelect.add(upgrade).size(sizeX, sizeY).spaceRight(10);
        towerSelect.add(sell).size(sizeX, sizeY);
//        towerSelect.add(new TextButton("Tower 1", skin)).size(50,10);
//        towerSelect.add(new TextButton("Tower 2", skin)).size(50,10);

        //Exit
        final Table exit = new Table();
        TextButton exitButton = new TextButton("| |", skin);
        exitButton.setSize(10,10);
        exitButton.getLabel().setFontScale(1,1);
        exitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(exitButton.getColor().equals(Color.valueOf("ffffffff"))) {
                    System.out.println("Pausiert");
                    exitButton.setColor(255,0,0,255);
                    exitButton.setText(">");
                    //Gdx.files.internal("ui-skin/glassy-ui.png");
                    //Gdx.app.exit();
                    setPause(!isPause());
                }
                else{
                    exitButton.setColor(Color.valueOf("ffffffff"));
                    exitButton.setText("| |");
                    setPause(!isPause());
                }

            }
        });
        exit.add(exitButton).size(sizeX, sizeY);
        //Gdx.input.setInputProcessor(stage);
        //getUi().addActor(exitButton);

        //Toprow table
        final Table topRow = new Table();
        //topRow.setDebug(true);
        //topRow.add(exit).left();
        //topRow.add(towerSelect).center().align(MIDDLE).spaceLeft(10).spaceRight(10).expandX();
        topRow.add(exit).top().right();
        topRow.setBounds(0,50,defaultScreen.getWidth(), defaultScreen.getHeight());

        final Table bottomOfScreen = new Table();
        bottomOfScreen.add(towerSelect).expandX();
        bottomOfScreen.align(MIDDLE);
        //bottomOfScreen.setDebug(true);

        defaultScreen.add(topRow).top().right().expandX();
        defaultScreen.row();
        //defaultScreen.setDebug(true);

//        defaultScreen.add(towerSelect).top().center();
//        defaultScreen.add(exit).top().right();
//        defaultScreen.row();
        defaultScreen.add(statsTable).top().right().expandX().colspan(4);
        defaultScreen.row();

        //defaultScreen.add(new ProgressBar(0, localPlayer.getAttackingEnemies().size(), 1, false, skin)).top().expandX();
        defaultScreen.add().expand().colspan(3);
        defaultScreen.row();
        defaultScreen.add(bottomOfScreen).bottom().center();
        mainUiStack.addActor(defaultScreen);

        //getUi().addActor(defaultScreen);
        getUi().addActor(mainUiStack);
        multiplexer.addProcessor(getUi());
        //InputProcessor inputProcessorButton;
        //Gdx.input.setInputProcessor(getUi());
    }

    private void reinitializeGameScreen() {

    }

    /**
     * Generiert aus einem beobachtbarem Objekt ein neues Turm-Spielobjekt
     *
     * @param observableUnit Das hinzuzufügende, beobachtbareObjekt
     */
    @Override
    public void addTower(ObservableUnit observableUnit) {
        gameObjects.add(new TowerObject(observableUnit));
    }

    /**
     * Generiert aus einem beobachtbarem Objekt ein neues Gegner-Spielobjekt
     *
     * @param observableUnit Das hinzuzufügende, beobachtbareObjekt
     */
    @Override
    public void addEnemy(ObservableUnit observableUnit) {
        gameObjects.add(new EnemyObject(observableUnit));
    }

    /**
     * Generiert aus einem beobachtbarem Objekt ein neues Projektil-Spielobjekt
     *
     * @param observableUnit Das hinzuzufügende, beobachtbareObjekt
     */
    @Override
    public void addProjectile(ObservableUnit observableUnit) {
        gameObjects.add(new ProjectileObject(observableUnit));
    }

    @Override
    public void displayErrorMessage(String message) {

    }

    private void removeGameObject(GameObject gameObject) {
        gameObjects.remove(gameObject);
        gameObject.dispose();
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    private void resetCameraToBorders() {
        float cameraHalfWidth = getCamera().viewportWidth * .5f;
        float cameraHalfHeight = getCamera().viewportHeight * .5f;

        float cameraLeft = getCamera().position.x - cameraHalfWidth;
        float cameraRight = getCamera().position.x + cameraHalfWidth;
        float cameraUp = getCamera().position.y + cameraHalfHeight;
        float cameraDown = getCamera().position.y - cameraHalfHeight;


        if (cameraLeft < 0) {
            getCamera().position.x = cameraHalfWidth;
        }
        if (cameraRight > mapWidth) {
            getCamera().position.x = mapWidth - cameraHalfWidth;
        }
        if (cameraUp > mapHeight) {
            getCamera().position.y = mapHeight - cameraHalfHeight;
        }
        if (cameraDown < 0) {
            getCamera().position.y = cameraHalfHeight;
        }
    }
}
