package de.diegrafen.exmatrikulatortd.view.screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import de.diegrafen.exmatrikulatortd.controller.MainController;
import de.diegrafen.exmatrikulatortd.controller.gamelogic.LogicController;
import de.diegrafen.exmatrikulatortd.model.*;
import de.diegrafen.exmatrikulatortd.model.enemy.ObservableEnemy;
import de.diegrafen.exmatrikulatortd.model.tower.ObservableTower;
import de.diegrafen.exmatrikulatortd.view.gameobjects.*;
import de.diegrafen.exmatrikulatortd.view.screens.uielements.TowerButton;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static com.badlogic.gdx.Input.Buttons.LEFT;
import static com.badlogic.gdx.Input.Buttons.MIDDLE;
import static com.badlogic.gdx.Input.Buttons.RIGHT;
import static de.diegrafen.exmatrikulatortd.controller.factories.EnemyFactory.HEAVY_ENEMY;
import static de.diegrafen.exmatrikulatortd.controller.factories.EnemyFactory.REGULAR_ENEMY;
import static de.diegrafen.exmatrikulatortd.util.Constants.CAMERA_TRANSLATE_VALUE;

/**
 * Der GameScreen wird während des aktuellen Spiels angezeigt.
 *
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 14.06.2019 02:12
 */
public class GameScreen extends BaseScreen implements GameView {

    private final Table defaultScreen = new Table();
    /**
     * Der aktuelle Spielstand.
     */
    private Gamestate gameState;
    /**
     * Die Karte, auf der die Türme plaziert werden, und sich die Gegner bewegen
     */
    private TiledMap tiledMap;

    private int[] backgroundLayers;

    private int[] foregroundLayers;

    /**
     * Rendert die tiledMap
     */
    private OrthogonalTiledMapRenderer orthogonalTiledMapRenderer;

    //private List<ObservableModel> players;

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
    private final List<GameObject> gameObjects;

    private boolean keyDownDown = false;

    private boolean keyUpDown = false;

    private boolean keyRightDown = false;

    private boolean keyLeftDown = false;

    private Label scoreLabel;
    private Label opponentScore;
    private Label resourcesLabel;

    private Label livesLabel;

    private Label roundsLabel;

    private Label timelabel;

    private float touchDownX;

    private float touchDownY;

    private InputMultiplexer multiplexer;

    private Group pauseGroup;
    // TODO: Variablen sinnvollere Bezeichnungen geben bzw. ersetzen

    private final Skin skin = new Skin(Gdx.files.internal("ui-skin/golden-ui-skin.json"));

    private List<TowerButton> towerButtons;

    private List<TowerObject> previewTowers;

    private ImageButton upgrade;
    private ImageButton sell;

    private TowerObject previewTower;

    private LogicController logicController;

    private ProgressBar playerHealth;
    private ProgressBar opponentHealth;

    private Table messageArea;
    private Table upgradeSell;
    private Table countdown;

    private Group popUp;

    private Label messageLabel;
    private float timer;
    private float tableDecayTimer;

    private int xCoord, yCoord;

    private static final int X_SIZE = 100;

    private static final int Y_SIZE = 100;

    private static final int NUM_KEY_OFFSET = 8;

    private TooltipManager tooltipManager;

    private Table towerSelect;

    private float mouseXPosition;

    private float mouseYPosition;

    /**
     * Der Konstruktor legt den MainController und das Spielerprofil fest. Außerdem erstellt er den Gamestate und den logicController.
     *
     * @param mainController Der Maincontrroller.
     */
    public GameScreen(MainController mainController, AssetManager assetManager) {
        super(mainController, assetManager);
        this.gameObjects = new ArrayList<>();
    }

    /**
     * Die Initialisierung erstellt den SpriteBatch und lädt Texturen.
     */
    @Override
    public void init() {
        float width = Gdx.graphics.getWidth();
        float height = Gdx.graphics.getHeight();

        getCamera().setToOrtho(false, width, height);

        multiplexer = new InputMultiplexer();

        towerButtons = new LinkedList<>();

        previewTowers = new LinkedList<>();

        tooltipManager = new TooltipManager();
        tooltipManager.instant();

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
                    int numberOfPlayers = gameState.getPlayers().size();
                    if (numberOfPlayers > 1) {
                        int playerToSendTo = Math.floorMod(localPlayerNumber - numberOfPlayers + 1, numberOfPlayers);
                        logicController.sendEnemy(REGULAR_ENEMY, playerToSendTo, localPlayerNumber);
                    } else {
                        logicController.sendEnemy(REGULAR_ENEMY, localPlayerNumber, localPlayerNumber);
                    }
                }
                if (keycode == Input.Keys.O) {
                    int numberOfPlayers = gameState.getPlayers().size();
                    if (numberOfPlayers > 1) {
                        int playerToSendTo = Math.floorMod(localPlayerNumber - numberOfPlayers + 1, numberOfPlayers);
                        logicController.sendEnemy(HEAVY_ENEMY, playerToSendTo, localPlayerNumber);
                    } else {
                        logicController.sendEnemy(HEAVY_ENEMY, localPlayerNumber, localPlayerNumber);
                    }
                }

                if (keycode >= Input.Keys.NUM_0 & keycode <= Input.Keys.NUM_9) {
                    int buttonNumber;
                    if (keycode == Input.Keys.NUM_0) {
                        buttonNumber = 9;
                    } else {
                        buttonNumber = keycode - NUM_KEY_OFFSET;
                    }
                    System.out.println(buttonNumber);
                    if (towerButtons.size() > buttonNumber) {
                        toggleButton(buttonNumber);
                    }
                }

                if (keycode == Input.Keys.D) {
                    upgrade.setChecked(!upgrade.isChecked());
                }
                if (keycode == Input.Keys.S) {
                    sell.setChecked(!sell.isChecked());
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

                Vector3 clickCoordinates = new Vector3(screenX, screenY, 0);
                Vector3 position = getCamera().unproject(clickCoordinates);

                int xCoordinate = logicController.getXCoordinateByPosition(position.x);
                int yCoordinate = logicController.getYCoordinateByPosition(position.y);

                int localPlayerNumber = logicController.getLocalPlayerNumber();

                if (button == RIGHT) {
                    if (logicController.hasCellTower(xCoordinate, yCoordinate)) {
                        Vector3 clickCoordinates2 = new Vector3(screenX, screenY, 0);
                        Vector3 position2 = getStageViewport().unproject(clickCoordinates2);
                        xCoord = xCoordinate;
                        yCoord = yCoordinate;
                        popUpButtons(Math.round(position2.x), Math.round(position2.y));
                        return true;
                    }
                } else if (button == LEFT) {
                    if (logicController.checkIfCoordinatesAreBuildable(xCoordinate, yCoordinate, localPlayerNumber)) {
                        for (TowerButton towerButton : towerButtons) {
                            if (towerButton.isChecked()) {
                                logicController.buildTower(towerButton.getTowerNumber(), xCoordinate, yCoordinate, localPlayerNumber);
                                return true;
                            }
                        }
                    } else if (logicController.hasCellTower(xCoordinate, yCoordinate)) {
                        if (sell.isChecked()) {
                            logicController.sellTower(xCoordinate, yCoordinate, localPlayerNumber);
                            return true;
                        } else if (upgrade.isChecked()) {
                            logicController.upgradeTower(xCoordinate, yCoordinate, localPlayerNumber);
                            return true;
                        }
                    }
                }
                return false;
            }

            @Override
            public boolean touchDragged(int screenX, int screenY, int pointer) {

                Vector3 clickCoordinates = new Vector3(screenX, screenY, 0);
                Vector3 position = getCamera().unproject(clickCoordinates);

                getCamera().position.x += touchDownX - position.x;// (position.x - touchDownX, position.y - touchDownY);
                getCamera().position.y += touchDownY - position.y;

                resetCameraToBorders();

                if(popUp.isVisible()){
                    popUp.setVisible(false);
                    tableDecayTimer = 0;
                }

                return false;
            }

            @Override
            public boolean mouseMoved(int screenX, int screenY) {
                Vector3 movedtoCoordinates = new Vector3(screenX, screenY, 0);
                Vector3 position = getCamera().unproject(movedtoCoordinates);
                mouseXPosition = position.x;
                mouseYPosition = position.y;
                return updatePreviewTower(position.x, position.y);
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

    private boolean updatePreviewTower(float xPosition, float yPosition) {
        int xCoordinate = logicController.getXCoordinateByPosition(xPosition);
        int yCoordinate = logicController.getYCoordinateByPosition(yPosition);
        for (int i = 0; i < towerButtons.size(); i++) {
            TowerButton towerButton = towerButtons.get(i);
            if (towerButton.isChecked()) {
                if (logicController.checkIfCoordinatesAreBuildable(xCoordinate, yCoordinate, logicController.getLocalPlayerNumber())) {
                    if (previewTower == null) {
                        previewTower = previewTowers.get(i);
                    }
                    MapProperties properties = tiledMap.getProperties();
                    previewTower.setxPosition(xCoordinate * properties.get("tilewidth", Integer.class));
                    previewTower.setyPosition(yCoordinate * properties.get("tileheight", Integer.class));
                } else {
                    previewTower = null;
                }
                return true;
            }
        }
        previewTower = null;
        return false;
    }

    /**
     * Wird immer nach einem Bestimmten Zeitabstand aufgerufen und die Logik des Spiels berechnet, damit danach in render() neu gezeichnet werden kann.
     *
     * @param deltaTime Die Zeit in Sekunden seit dem letzten Frame.
     */
    @Override
    public void update(float deltaTime) {
        logicController.update(deltaTime);
        if (tableDecayTimer <= 0) {
            popUp.setVisible(false);
        } else {
            tableDecayTimer = tableDecayTimer - deltaTime;
        }
        if (messageLabel != null) {
            if (timer <= 0) {
                messageArea.removeActor(messageLabel);
            } else {
                timer = timer - deltaTime;
                messageLabel.setColor(1, 0, 0, 1 * timer / 3);
            }
        }
        if (gameState.getTimeUntilNextRound() <= 0) {
            countdown.setVisible(false);
        } else {
            countdown.setVisible(true);
        }
    }

    @Override
    public void update() {
        Player localPlayer = logicController.getLocalPlayer();

        if (logicController.isMultiplayer()) {
            Player opposingPlayer = logicController.getLocalPlayer();
            opponentHealth.setValue(opposingPlayer.getCurrentLives());
            opponentScore.setText(opposingPlayer.getScore());
        }
        scoreLabel.setText(localPlayer.getScore());
        livesLabel.setText(localPlayer.getCurrentLives() + "/" + localPlayer.getMaxLives());
        playerHealth.setValue(localPlayer.getCurrentLives());

        resourcesLabel.setText(localPlayer.getResources());
        if (gameState.isEndlessGame()) {
            roundsLabel.setText(Integer.toString(gameState.getRoundNumber() + 1));
        } else {
            roundsLabel.setText((gameState.getRoundNumber() + 1) + "/" + gameState.getNumberOfRounds());
        }
        timelabel.setText((int) Math.max(0, gameState.getTimeUntilNextRound()));
    }

    /**
     * Eigene Zeichenanweisungen.
     *
     * @param deltaTime Die Zeit in Sekunden seit dem letzten Frame.
     */
    @Override
    public void draw(float deltaTime) {
        super.draw(deltaTime);

        float oldCameraXValue = getCamera().position.x;
        float oldCameraYValue = getCamera().position.y;

        if (keyLeftDown) {
            getCamera().translate(-CAMERA_TRANSLATE_VALUE, 0);
        }
        if (keyRightDown) {
            getCamera().translate(CAMERA_TRANSLATE_VALUE, 0);
        }
        if (keyUpDown) {
            getCamera().translate(0, CAMERA_TRANSLATE_VALUE);
        }
        if (keyDownDown) {
            getCamera().translate(0, -CAMERA_TRANSLATE_VALUE);
        }

        resetCameraToBorders();

        float cameraDeltaX = oldCameraXValue - getCamera().position.x;
        float cameraDeltaY = oldCameraYValue - getCamera().position.y;

        popUp.setPosition(popUp.getX() + cameraDeltaX, popUp.getY() + cameraDeltaY);

        getCamera().update();
        if (orthogonalTiledMapRenderer != null) {
            orthogonalTiledMapRenderer.setView(getCamera());
            orthogonalTiledMapRenderer.render(backgroundLayers);
        }

        getSpriteBatch().setProjectionMatrix(getCamera().combined);
        getSpriteBatch().begin();

        List<GameObject> objectsToRemove = new ArrayList<>();

        gameObjects.sort((o1, o2) -> Float.compare(o2.getyPosition(), o1.getyPosition()));

        for (GameObject gameObject : gameObjects) {
            if (gameObject.isRemoved()) {
                objectsToRemove.add(gameObject);
            } else {
                if (logicController.isPause() | gameState.isGameOver()) {
                    gameObject.setAnimated(false);
                } else {
                    gameObject.setAnimated(true);
                }
                gameObject.draw(getSpriteBatch(), deltaTime);
            }
        }
        objectsToRemove.forEach(this::removeGameObject);
        if (previewTower != null) {
            previewTower.draw(getSpriteBatch(), deltaTime);
        }
        getSpriteBatch().end();

        if (orthogonalTiledMapRenderer != null) {
            orthogonalTiledMapRenderer.render(foregroundLayers);
        }
    }

    /**
     * Gibt nicht mehr benötigte Ressourcen frei, um Speicherlecks zu vermeiden.
     */
    @Override
    public void dispose() {
        super.dispose();
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
        List<Integer> background = new LinkedList<>();
        List<Integer> foreground = new LinkedList<>();
        for (int i = 0; i < tiledMap.getLayers().size(); i++) {
            if ((int) tiledMap.getLayers().get(i).getProperties().get("zIndex") < 1) {
                background.add(i);
            } else {
                foreground.add(i);
            }
        }
        backgroundLayers = background.stream().mapToInt(i->i).toArray();
        foregroundLayers = foreground.stream().mapToInt(i->i).toArray();
        getCamera().update();
        orthogonalTiledMapRenderer.setView(getCamera());
    }

    /**
     * Erstellt das Userinterface mit Scene2d Tabellen
     */
    private void initializeUserInterface() {

        Table opponent = new Table();
        Table sendEnemy = new Table();

        final Stack mainUiStack = new Stack();
        mainUiStack.setFillParent(true);

        Player localPlayer = logicController.getLocalPlayer();

        defaultScreen.setFillParent(true);

        TooltipManager ttm = new TooltipManager();
        ttm.instant();

        Pixmap pixRed = new Pixmap(100, 20, Pixmap.Format.RGBA8888);
        pixRed.setColor(Color.RED);
        pixRed.fill();
        TextureRegionDrawable redBG = new TextureRegionDrawable(new TextureRegion(new Texture(pixRed)));
        pixRed.dispose();
        Pixmap pixHidden = new Pixmap(0, 20, Pixmap.Format.RGBA8888);
        pixHidden.setColor(Color.GREEN);
        pixHidden.fill();
        TextureRegionDrawable hiddenBar = new TextureRegionDrawable(new TextureRegion(new Texture(pixHidden)));
        pixHidden.dispose();
        Pixmap pixGreen = new Pixmap(100, 20, Pixmap.Format.RGBA8888);
        pixGreen.setColor(Color.GREEN);
        pixGreen.fill();
        TextureRegionDrawable greenBar = new TextureRegionDrawable(new TextureRegion(new Texture(pixGreen)));
        ProgressBar.ProgressBarStyle progressBarStyle = new ProgressBar.ProgressBarStyle();
        progressBarStyle.background = redBG;
        progressBarStyle.knob = hiddenBar;
        progressBarStyle.knobBefore = greenBar;

        if (!logicController.isMultiplayer()) {
            //int localPlayerNumber = logicController.getLocalPlayerNumber();
            int numberOfPlayers = gameState.getPlayers().size();
            Player opposingPlayer = gameState.getPlayers().get((numberOfPlayers - logicController.getLocalPlayerNumber()) % numberOfPlayers);
            opponentHealth = new ProgressBar(0, opposingPlayer.getMaxLives(), 1, false, progressBarStyle);
            opponentHealth.setScale(1 / 2);
            opponentHealth.setValue(opposingPlayer.getCurrentLives());
            opponentHealth.setAnimateDuration(1);
            Label.LabelStyle scoreLabelStyle = new Label.LabelStyle();
            scoreLabelStyle.font = getBitmapFont();
            opponentScore = new Label("Punkte " + opposingPlayer.getScore(), scoreLabelStyle);
            opponent.setBounds(0, 50, 100, 100);
            opponent.add(opponentScore).left().row();
            opponent.add(opponentHealth).left();

//            Drawable sendRegEnemyIcon = new TextureRegionDrawable(new Texture(Gdx.files.internal("sendEnemyRegularIcon.png")));
//            Drawable sendHvyEnemyIcon = new TextureRegionDrawable(new Texture(Gdx.files.internal("sendEnemyHeavyIcon.png")));

            Drawable sendRegEnemyIcon = new TextureRegionDrawable(new Texture(Gdx.files.internal("buff_portrait.png")));
            Drawable sendHvyEnemyIcon = new TextureRegionDrawable(new Texture(Gdx.files.internal("buff_portrait.png")));

            ImageButton sendRegEnemy = new ImageButton(sendRegEnemyIcon);
            ImageButton sendHvyEnemy = new ImageButton(sendHvyEnemyIcon);

            //TextButton sendRegEnemy = new TextButton("sre", skin);
            //TextButton sendHvyEnemy = new TextButton("she", skin);
            //sendRegEnemy.scaleBy(5);
            //sendHvyEnemy.setScale(5);
            sendRegEnemy.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    logicController.sendEnemy(REGULAR_ENEMY, opposingPlayer.getPlayerNumber(), localPlayer.getPlayerNumber());
                }
            });
            sendHvyEnemy.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    logicController.sendEnemy(HEAVY_ENEMY, opposingPlayer.getPlayerNumber(), localPlayer.getPlayerNumber());
                }
            });
            sendRegEnemy.addListener(new TextTooltip("Sende deinem Gegenspieler einen zusätzlichen leichten Gegner \n" + "Kosten: 50 Gold", ttm, skin));
            sendHvyEnemy.addListener(new TextTooltip("Sende deinem Gegenspieler einen zusätzlichen schweren Gegner \n" + "Kosten: 100 Gold", ttm, skin));
            sendEnemy.add(sendRegEnemy).size(X_SIZE, Y_SIZE).padBottom(15).row();
            sendEnemy.add(sendHvyEnemy).size(X_SIZE, Y_SIZE);

        }

        final Table statsTable = new Table();
        //statsTable.setBackground(background);
        Label.LabelStyle infoLabelsStyle = new Label.LabelStyle();
        infoLabelsStyle.font = getBitmapFont();
        Label.LabelStyle scoreLabelStyle = new Label.LabelStyle();
        scoreLabelStyle.font = getBitmapFont();
        Label.LabelStyle liveLabelStyle = new Label.LabelStyle();
        liveLabelStyle.font = getBitmapFont();
        Label.LabelStyle roundLabelStyle = new Label.LabelStyle();
        roundLabelStyle.font = getBitmapFont();


        // score
        statsTable.add(new Label("Punkte: ", infoLabelsStyle)).right().padLeft(10).expandX();
        scoreLabel = new Label(Integer.toString(localPlayer.getScore()), scoreLabelStyle);
        statsTable.add(scoreLabel).left().align(RIGHT);
        statsTable.row();
        // money
        statsTable.add(new Label("Geld: ", infoLabelsStyle)).right().padLeft(10).expandX();
        resourcesLabel = new Label(Integer.toString(localPlayer.getResources()), scoreLabelStyle);
        statsTable.add(resourcesLabel).left().align(RIGHT);
        statsTable.row();
        // lives
        statsTable.add(new Label("Leben: ", infoLabelsStyle)).right().padLeft(10).expandX();
        livesLabel = new Label(localPlayer.getCurrentLives() + "/" + localPlayer.getMaxLives(), liveLabelStyle);
        statsTable.add(livesLabel).left().align(RIGHT);
        statsTable.row();
        // Rounds
        statsTable.add(new Label("Runde: ", infoLabelsStyle)).right().padLeft(10).expandX();
        String roundsLabelText = (gameState.getRoundNumber() + 1) + "/" + gameState.getNumberOfRounds();
        roundsLabel = new Label(roundsLabelText, liveLabelStyle);
        statsTable.add(roundsLabel).left().align(RIGHT);
        statsTable.row();
        // Time
        statsTable.row().pad(10, 0, 10, 0);
//        statsTable.add(new Label("Zeit bis zur nächsten Runde: ", infoLabelsStyle)).right().padLeft(10).expandX();
//        String timeLabelText = ("" + (int) Math.max(0, gameState.getTimeUntilNextRound()));
//        timelabel = new Label(timeLabelText, liveLabelStyle);
//        statsTable.add(timelabel).left().align(RIGHT);

        playerHealth = new ProgressBar(0, localPlayer.getMaxLives(), 1, false, progressBarStyle);
        playerHealth.setValue(localPlayer.getCurrentLives());
        playerHealth.setAnimateDuration(1);

        Drawable menuImage = new TextureRegionDrawable(new Texture(Gdx.files.internal("menuIcon_placeholder.png")));
        Drawable upgradeIcon = new TextureRegionDrawable(new Texture(Gdx.files.internal("upgradeIcon.png")));
        Drawable sellIcon = new TextureRegionDrawable(new Texture(Gdx.files.internal("sellIcon.png")));
        //TextButtonStyle style = new TextButtonStyle();
        towerSelect = new Table();
        //towerSelect.setDebug(true);

        //Die einzelnen Towerbuttons
        logicController.createTowerButtons(this);

        upgrade = new ImageButton(upgradeIcon);
        //sell = new ImageButton(sellIcon);
        //upgrade = new TextButton("^", skin);
        sell = new ImageButton(sellIcon);
        //upgrade.setSize(X_SIZE, Y_SIZE);
        sell.setSize(X_SIZE, Y_SIZE);

        TextButton instaLoose = new TextButton("L", skin);
        instaLoose.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                localPlayer.setCurrentLives(0);
            }
        });

        upgrade.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                logicController.upgradeTower(xCoord, yCoord, localPlayer.getPlayerNumber());
                popUp.setVisible(false);
            }
        });
        sell.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                logicController.sellTower(xCoord, yCoord, localPlayer.getPlayerNumber());
                popUp.setVisible(false);
            }
        });


        upgrade.addListener(new TextTooltip("Upgraden", tooltipManager, skin));
        sell.addListener(new TextTooltip("Verkaufen", tooltipManager, skin));

        //Exit
        final Table exit = new Table();
        ImageButton exitButton = new ImageButton(menuImage);
        exitButton.setSize(10, 10);
        exitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (!logicController.isPause() & !logicController.isMultiplayer()) {
                    System.out.println("Pausiert");
                    logicController.setPause(true);
                    pauseScreen();
                } else if (!logicController.isMultiplayer()) {
                    logicController.setPause(false);
                    pauseScreen();
                }

            }
        });

        popUp = new Group();
        upgradeSell = new Table();
        upgradeSell.add(upgrade).size(X_SIZE, Y_SIZE).row();
        upgradeSell.add(sell).size(X_SIZE, Y_SIZE);
        //upgradeSell.setVisible(false);

        exit.add(exitButton).size(X_SIZE, Y_SIZE);

        messageArea = new Table();

        //Toprow table
        final Table topRow = new Table();
        final Table topLeft = new Table();
        //topRow.setDebug(true);
        if (logicController.isMultiplayer()) {
            //opponent.setBounds(0, 0, 50, 100);
            topLeft.add(opponent).left().padRight(10).padLeft(10);
        }
        topRow.add(exit).top().right();
        topRow.setBounds(0, 50, defaultScreen.getWidth(), defaultScreen.getHeight());

        final Table bottomOfScreen = new Table();
        bottomOfScreen.add(towerSelect).expandX();
        bottomOfScreen.add(sendEnemy).expandX();
        bottomOfScreen.align(MIDDLE);
        //bottomOfScreen.setDebug(true);

        countdown = new Table();
        countdown.add(new Label("Zeit bis zur nächsten Runde: ", infoLabelsStyle)).right().padLeft(10);
        String timeLabelText = ("" + (int) Math.max(0, gameState.getTimeUntilNextRound()));
        timelabel = new Label(timeLabelText, liveLabelStyle);
        countdown.add(timelabel).top().left().align(RIGHT);

        topLeft.add(messageArea).center().top().padLeft(50);
        topLeft.setBounds(0, 50, 100, 100);
        defaultScreen.add(topLeft).left();
        defaultScreen.add(topRow).top().right().expandX();
        defaultScreen.row();
        //defaultScreen.setDebug(true);

        statsTable.add(playerHealth).left().align(RIGHT).expandX().padRight(-40);
        defaultScreen.add(statsTable).top().right().colspan(4).padRight(20).row();
        //defaultScreen.row();
        defaultScreen.add(countdown).top().right().colspan(4).padRight(20).row();
        if (logicController.isMultiplayer()) {
            defaultScreen.add(sendEnemy).top().right().colspan(4).padRight(20).row();
        }
        defaultScreen.add().expand().colspan(3);
        defaultScreen.row();
        defaultScreen.add(bottomOfScreen).bottom().center().colspan(2).expandX();
        mainUiStack.addActor(defaultScreen);
        //mainUiStack.addActor(upgradeSell);

        getUi().addActor(mainUiStack);
        multiplexer.addProcessor(getUi());
    }

    /**
     * Generiert aus einem beobachtbarem Objekt ein neues Turm-Spielobjekt
     *
     * @param observableTower Das hinzuzufügende, beobachtbareObjekt
     */
    @Override
    public void addTower(ObservableTower observableTower) {
        gameObjects.add(0, new TowerObject(observableTower, getAssetManager()));
    }

    /**
     * Generiert aus einem beobachtbarem Objekt ein neues Gegner-Spielobjekt
     *
     * @param observableUnit Das hinzuzufügende, beobachtbareObjekt
     */
    @Override
    public void addEnemy(ObservableEnemy observableUnit) {
        gameObjects.add(new EnemyObject(observableUnit, getAssetManager()));
    }

    /**
     * Generiert aus einem beobachtbarem Objekt ein neues Projektil-Spielobjekt
     *
     * @param observableUnit Das hinzuzufügende, beobachtbareObjekt
     */
    @Override
    public void addProjectile(ObservableUnit observableUnit) {
        gameObjects.add(new ProjectileObject(observableUnit, getAssetManager()));
    }

    /**
     * Zeigt eine Fehlermeldung auf dem Bildschirm an
     *
     * @param message Die anzuzeigende Fehlermeldung
     */
    @Override
    public void displayErrorMessage(String message) {
        Label.LabelStyle messageStyle = new Label.LabelStyle();
        messageStyle.font = getBitmapFont();
        messageLabel = new Label(message, messageStyle);
        messageLabel.setColor(Color.RED);
        messageArea.clear();
        messageArea.add(messageLabel);
        timer = 3;
    }

    /**
     * Assoziiert die GameView mit einem LogicController
     *
     * @param logicController Der zu assoziierende LogicController
     */
    @Override
    public void setLogicController(LogicController logicController) {
        this.logicController = logicController;
    }

    /**
     * Entfernt ein Spielobjekt aus dem Spiel
     * @param gameObject Das Spielobjekt, das entfernt werden soll
     */
    private void removeGameObject(GameObject gameObject) {
        gameObjects.remove(gameObject);
    }

    /**
     * Verhindert, dass die Kamera über die Grenzen der Karte hinaus bewegt werden kann, indem sie in diesem
     * Fall an den Rand der Map zurückgesetzt wird.
     */
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

    /**
     * Setzt den Zustand aller Buttons auf "unchecked".
     */
    private void uncheckAllButtons() {
        towerButtons.forEach(towerButton -> towerButton.setChecked(false));
    }

    /**
     * Kehrt den "checked"-Zustand eines Buttons um
     * @param buttonNumber Die Nummer des Buttons
     */
    private void toggleButton(int buttonNumber) {
        TowerButton towerButton = towerButtons.get(buttonNumber);
        if (towerButton != null) {
            previewTower = null;
            boolean isChecked = towerButton.isChecked();
            uncheckAllButtons();
            towerButton.setChecked(!isChecked);
            updatePreviewTower(mouseXPosition, mouseYPosition);
        }
    }

    /**
     * Erstellt/Zeigt den Pausenbildschirm an. Das Spiel UI wird ausgeblendet wärend das Spiel pausiert ist.
     */
    private void pauseScreen() {
        if (logicController.isPause()) {
            pauseGroup = new Group();
            Image semiTBG = new Image(new Texture(Gdx.files.internal("transparentBG.png")));
            semiTBG.setSize(getStageViewport().getScreenWidth(), getStageViewport().getScreenHeight());

            Table buttonTable = new Table();
            TextButton resume = new TextButton("Resume", skin);
            resume.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    logicController.setPause(false);
                    pauseGroup.setVisible(false);
                    defaultScreen.setVisible(true);
                }
            });
            TextButton save = new TextButton("Save", skin);
            save.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    // TODO: Mögichkeit zum Eingeben von Spielstandnamen hinzufügen
                    logicController.saveGame("Blah.");
                }
            });
            TextButton load = new TextButton("Load", skin);
            load.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    List<SaveState> saveStates = getMainController().getAllSavestates();
                    if (!saveStates.isEmpty()) {
                        SaveState saveState = saveStates.get(saveStates.size() - 1);
                        //TODO: Dropdown-Menü zur Auswahl von Spielständen ergänzen
                        getMainController().loadSinglePlayerGame(saveState.getId());
                    }
                }
            });
            TextButton back2main = new TextButton("Menu", skin);
            back2main.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    logicController.exitGame(true);
                }
            });

            buttonTable.add(resume).top().center().spaceBottom(10).row();
            buttonTable.add(save).top().center().spaceBottom(10).row();
            buttonTable.add(load).top().center().spaceBottom(10).row();
            buttonTable.add(back2main).top().center().row();
            buttonTable.setSize(getStageViewport().getScreenWidth(), getStageViewport().getScreenHeight());

            pauseGroup.addActor(semiTBG);
            pauseGroup.addActor(buttonTable);

            defaultScreen.setVisible(false);
            getUi().addActor(pauseGroup);
        } else {
            pauseGroup.setVisible(false);
        }
    }

    /**
     * Der Bildschirm der am Ende vom Spiel dargestellt wird.
     * Gewonnen/Verloren + Punkte + Highscore des Users
     */
    @Override
    public void endOfGameScreen(boolean victorious, int score, int highscore) {
        Group endScreenGroup = new Group();

        Label resultLabel;
        if (victorious) {
            resultLabel =  new Label("Gewonnen!", skin);
        } else {
            resultLabel =  new Label("Verloren!", skin);
        }

        Label currentScoreLabel = new Label("Erzielte Punkte: " + score, skin);
        Label highscoreLabel = new Label("Highscore: " + highscore, skin);
        // TODO: Zum Assetmanager hinzufügen
        Image background = new Image(new Texture(Gdx.files.internal("transparentBG.png")));

        Table buttonTable = new Table();

        TextButton back2main = new TextButton("Menu", skin);
        back2main.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                logicController.exitGame(false);
            }
        });

        buttonTable.add(resultLabel).center().row();
        buttonTable.add(currentScoreLabel).center().row();
        buttonTable.add(highscoreLabel).center().row();
        buttonTable.add(back2main).top().center().row();
        buttonTable.setSize(getStageViewport().getScreenWidth(), getStageViewport().getScreenHeight() / 2);

        endScreenGroup.addActor(background);
        endScreenGroup.addActor(buttonTable);
        defaultScreen.setVisible(false);
        getUi().addActor(endScreenGroup);
    }

    /**
     * Erstellt das PopUp Menu an der gegebenen Position im Viewport.
     * @param posX Die X-Koordinate wo das Menu erstellt wird
     * @param posY Die Y-Koordinate wo das Menu erstellt wird
     */
    private void popUpButtons(int posX, int posY) {
        int posXAllignment;
        int offset = 75;

        if (posX < getStageViewport().getScreenWidth() / 2) {
            posXAllignment = posX + offset;
        } else {
            posXAllignment = posX - offset;
        }
        popUp.addActor(upgradeSell);
        popUp.setSize(50, 110);
        popUp.setPosition(posXAllignment, posY);
        popUp.setVisible(true);
        tableDecayTimer = 2;
        getUi().addActor(popUp);
    }

    /**
     * Fügt einen Button zur UI hinzu, über den Türme platziert werden können.
     */
    @Override
    public void addTowerButton (ObservableTower observableTower) {
        TowerButton towerButton = new TowerButton(observableTower.getTowerType(), observableTower.getPortraitPath(),
                observableTower.getSelectedPortraitPath(), observableTower.getDescriptionText());
        towerButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                boolean isChecked = towerButton.isChecked();
                towerButtons.forEach(button -> button.setChecked(false));
                towerButton.setChecked(isChecked);
                if (!isChecked) {
                    previewTower = null;
                }
            }
        });
        towerButton.addListener(new TextTooltip(towerButton.getToolTipText(), tooltipManager, skin));
        towerSelect.add(towerButton).size(X_SIZE, Y_SIZE).spaceRight(5);

        towerButtons.add(towerButton);
        previewTowers.add(new TowerObject(observableTower, getAssetManager()));
    }

    /**
     * Legt einen Spielzustand als Attribut der GameView fest
     *
     * @param gameState
     */
    @Override
    public void setGameState(Gamestate gameState) {
        this.gameState = gameState;
    }
}
