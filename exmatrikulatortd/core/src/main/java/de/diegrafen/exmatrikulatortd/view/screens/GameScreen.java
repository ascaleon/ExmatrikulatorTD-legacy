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
import static com.badlogic.gdx.Input.Buttons.RIGHT;
import static de.diegrafen.exmatrikulatortd.controller.factories.EnemyFactory.HEAVY_ENEMY;
import static de.diegrafen.exmatrikulatortd.controller.factories.EnemyFactory.REGULAR_ENEMY;
import static de.diegrafen.exmatrikulatortd.util.Assets.*;
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

    /**
     *
     */
    private boolean keyDownDown = false;

    /**
     *
     */
    private boolean keyUpDown = false;

    /**
     *
     */
    private boolean keyRightDown = false;

    /**
     *
     */
    private boolean keyLeftDown = false;

    /**
     * Label für den Punktestand des lokalen Spielers
     */
    private Label scoreLabel;

    /**
     * Label für den Punktestand des Gegenspielers
     */
    private Label opponentScore;

    /**
     * Label für die Anzeige der Resourcen
     */
    private Label resourcesLabel;

    /**
     * Label für das Leben des Spielers
     */
    private Label livesLabel;

    /**
     * Label für die Anzahl der Runden
     */
    private Label roundsLabel;

    /**
     * Label für die Zeit bis zur nächsten Runde
     */
    private Label timelabel;

    /**
     * Style der Towerinformation
     */
    private Label.LabelStyle towerinfoLabelsStyle = new Label.LabelStyle(getBitmapFont(), Color.WHITE);
    /**
     * Die Towerinformation
     */
    private Label towerinfoLabel = new Label(null, towerinfoLabelsStyle);

    /**
     *
     */
    private float touchDownX;

    /**
     *
     */
    private float touchDownY;

    /**
     * Verwaltet Inputs von Maus und Tastatur und von den UI Elementen
     */
    private InputMultiplexer multiplexer;

    /**
     * Der Container für die UI Elemnte des Pause/Menubildschirm
     */
    private Group pauseGroup;

    /**
     * Der Container für die UI Elemente des Endbildschirm
     */
    private Group endScreenGroup;

    private Skin skin;

    /**
     * Der Style der Lebensanzeigen
     */
    private ProgressBar.ProgressBarStyle healthBarStyle;

    /**
     * Eine Liste der Buttons für die Auswahl eines Turmes
     */
    private List<TowerButton> towerButtons;

    /**
     * Eine Liste der einzelnen Vorschautürme
     */
    private List<TowerObject> previewTowers;

    /**
     * Der Vorschauturm
     */
    private TowerObject previewTower;

    /**
     * Das Interface der Spiellogik
     */
    private LogicController logicController;

    /**
     * Die Lebensleiste des Lokalenspielers
     */
    private ProgressBar playerHealth;
    /**
     * Die Lebensleiste des Gegenspielers
     */
    private ProgressBar opponentHealth;

    /**
     * UI Bereich in dem die Benachrichtigungen angezeigt werden
     */
    private Table messageArea;
    /**
     * UI Bereich im Menu in dem die Benachrichtigungen angezeigt werden
     */
    private Table menuMessageArea;

    /**
     * UI Bereich in dem sich die Buttons zum Upgraden und Verkaufen befinden
     */
    private Table upgradeSell;
    /**
     * UI bereich in dem der Rundencountdown angezeigt wird
     */
    private Table countdown;
    /**
     * UI Bereich der die Informationen über den Gegenspieler beinhaltet
     */
    private Table opponent;
    /**
     * UI Bereich für die Buttons zum Senden von Einheiten an den Gegenspieler
     */
    private Table sendEnemy;

    /**
     * Container für die Elemente des Pop Up Menu
     */
    private Group popUpButtons;

    /**
     * Die Nachricht die dem Spieler angezeigt wird
     */
    private Label messageLabel;

    /**
     * Timer für die Benachrichtigungen
     */
    private float timer;

    /**
     * Timer für das Pop Up Menu
     */
    private float tableDecayTimer;

    /**
     * Koordinaten für die Position eines Turmes
     */
    private int xCoord, yCoord;

    /**
     * Die
     */
    private static final int X_SIZE = 100;

    /**
     *
     */
    private static final int Y_SIZE = 100;

    /**
     *
     */
    private static final int NUM_KEY_OFFSET = 8;

    /**
     * Der Tooltipmanager für die Infoboxen der Buttons
     */
    private TooltipManager tooltipManager;

    /**
     * Die Tabelle in die die einzelnen Towerbuttons hinzugefügt werden
     */
    private Table towerSelect;

    /**
     * Die X-Position der Maus
     */
    private float mouseXPosition;

    /**
     * Die Y-Position der Maus
     */
    private float mouseYPosition;

    /**
     * Die Anzahl der Spieler
     */
    private int numberOfPlayers;

    /**
     * Das Hintergrundbild für die Menus
     */
    private Image background;

    /**
     * Der Gegenspieler
     */
    private Player opposingPlayer;

    /**
     * Der lokale Spieler
     */
    private Player localPlayer;

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
     * Die Initialisierung erzeugt die Inputprocessoren, initialisiert benötigte Variablen
     * und erstellt die Spielerobjekte welche für die UI benötigt werden.
     */
    @Override
    public void init() {
        float width = Gdx.graphics.getWidth();
        float height = Gdx.graphics.getHeight();
        skin = getAssetManager().get(SKIN, Skin.class);

        getCamera().setToOrtho(false, width, height);

        multiplexer = new InputMultiplexer();

        towerButtons = new LinkedList<>();

        previewTowers = new LinkedList<>();

        tooltipManager = new TooltipManager();
        tooltipManager.instant();

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
                    int numberOfPlayers = logicController.getNumberOfPlayers();
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
                    if (towerButtons.size() > buttonNumber) {
                        toggleButton(buttonNumber);
                    }
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
                        if(gameState.getMapCellByListIndex(xCoordinate + yCoordinate* gameState.getNumberOfColumns()).getTower().getPlayerNumber() == localPlayer.getPlayerNumber()) {
                            Vector3 clickCoordinates2 = new Vector3(screenX, screenY, 0);
                            Vector3 position2 = getStageViewport().unproject(clickCoordinates2);
                            xCoord = xCoordinate;
                            yCoord = yCoordinate;
                            popUpButtons(Math.round(position2.x), Math.round(position2.y));
                            return true;
                        }
                    }
                } else if (button == LEFT) {
                    if (logicController.checkIfCoordinatesAreBuildable(xCoordinate, yCoordinate, localPlayerNumber)) {
                        for (TowerButton towerButton : towerButtons) {
                            if (towerButton.isChecked()) {
                                logicController.buildTower(towerButton.getTowerNumber(), xCoordinate, yCoordinate, localPlayerNumber);
                                return true;
                            }
                        }
                    }
                }
                return false;
            }

            @Override
            public boolean touchDragged(int screenX, int screenY, int pointer) {

                Vector3 clickCoordinates = new Vector3(screenX, screenY, 0);
                Vector3 position = getCamera().unproject(clickCoordinates);

                getCamera().position.x += touchDownX - position.x;
                getCamera().position.y += touchDownY - position.y;

                resetCameraToBorders();

                if(popUpButtons.isVisible()){
                    popUpButtons.setVisible(false);
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
                int xCoordinate = logicController.getXCoordinateByPosition(position.x);
                int yCoordinate = logicController.getYCoordinateByPosition(position.y);
                Coordinates coordinates = gameState.getMapCellByListIndex(xCoordinate + yCoordinate * gameState.getNumberOfColumns());

                if(logicController.hasCellTower(xCoordinate, yCoordinate)){
                    towerinfoLabel.setText(
                                    "Name: " + coordinates.getTower().getName() + "\n" +
                                    "Level: " + coordinates.getTower().getUpgradeLevel() + "\n" +
                                    "Angriff: " + coordinates.getTower().getBaseAttackDamage() + "\n" +
                                    "Speed: " + coordinates.getTower().getBaseAttackSpeed() + "\n" +
                                    "eff. Speed: " + coordinates.getTower().getCurrentAttackSpeed() + "\n" +
                                    "Reichweite: " + coordinates.getTower().getAttackRange() + "\n" +
                                    "Aura-Reichweite: " + coordinates.getTower().getAuraRange() + "\n" +
                                    "Upgradekosten: " + coordinates.getTower().getUpgradePrice() + "\n" +
                                    "Verkaufen: " + coordinates.getTower().getSellPrice()
                    );
                }else{
                    towerinfoLabel.setText(null);
                }

                return updatePreviewTower(position.x, position.y);
            }

            @Override
            public boolean scrolled(int amount) {
                return false;
            }
        };

        numberOfPlayers = gameState.getPlayers().size();

        background = new Image(getAssetManager().get(TRANSPARENT_BACKGROUND_IMAGE, Texture.class));

        localPlayer = logicController.getLocalPlayer();

        if(logicController.isMultiplayer()){
            int opposingPlayerNumber;
            if (logicController.getLocalPlayerNumber() == 0) {
                opposingPlayerNumber = 1;
            } else {
                opposingPlayerNumber = 0;
            }

            opposingPlayer = gameState.getPlayerByNumber(opposingPlayerNumber);
        }
        initializeUserInterface();
        initPauseScreen();

        multiplexer.addProcessor(inputProcessorCam);
        Gdx.input.setInputProcessor(multiplexer);
    }

    /**
     *
     * @param xPosition
     * @param yPosition
     * @return
     */
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
            popUpButtons.setVisible(false);
        } else {
            tableDecayTimer = tableDecayTimer - deltaTime;
        }
        if (messageLabel != null) {
            if (timer <= 0) {
                if(defaultScreen.isVisible()) {
                    messageArea.removeActor(messageLabel);
                }
                else{
                    menuMessageArea.removeActor(messageLabel);
                }
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

    /**
     * Aktuallisiert die UI Elemente
     */
    @Override
    public void update() {

        if (logicController.isMultiplayer()) {
            if (opposingPlayer != null) {
                opponentHealth.setValue(opposingPlayer.getCurrentLives());
                opponentScore.setText(opposingPlayer.getScore());
            }
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

        popUpButtons.setPosition(popUpButtons.getX() + cameraDeltaX, popUpButtons.getY() + cameraDeltaY);

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
            MapProperties layerProperties = tiledMap.getLayers().get(i).getProperties();
            if ((int) layerProperties.get("zIndex") < 1) {
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

        opponent = new Table();
        sendEnemy = new Table();

        final Stack mainUiStack = new Stack();
        mainUiStack.setFillParent(true);

        defaultScreen.setFillParent(true);

        initProgressbarStyle();

        if (logicController.isMultiplayer()) {
            initMultiplayerUiComponents();
        }

        final Table towerinfoTable = new Table();
        towerinfoTable.add(towerinfoLabel).left().align(RIGHT);

        final Table statsTable = new Table();
        Label.LabelStyle infoLabelsStyle = new Label.LabelStyle();
        infoLabelsStyle.font = getBitmapFont();

        // score
        statsTable.add(new Label("Punkte: ", infoLabelsStyle)).right().padLeft(10).expandX();
        scoreLabel = new Label(Integer.toString(localPlayer.getScore()), infoLabelsStyle);
        statsTable.add(scoreLabel).left().align(RIGHT);
        statsTable.row();
        // money
        statsTable.add(new Label("Geld: ", infoLabelsStyle)).right().padLeft(10).expandX();
        resourcesLabel = new Label(Integer.toString(localPlayer.getResources()), infoLabelsStyle);
        statsTable.add(resourcesLabel).left().align(RIGHT);
        statsTable.row();
        // lives
        statsTable.add(new Label("Leben: ", infoLabelsStyle)).right().padLeft(10).expandX();
        livesLabel = new Label(localPlayer.getCurrentLives() + "/" + localPlayer.getMaxLives(), infoLabelsStyle);
        statsTable.add(livesLabel).left().align(RIGHT);
        statsTable.row();
        // Rounds
        statsTable.add(new Label("Runde: ", infoLabelsStyle)).right().padLeft(10).expandX();
        String roundsLabelText = (gameState.getRoundNumber() + 1) + "/" + gameState.getNumberOfRounds();
        roundsLabel = new Label(roundsLabelText, infoLabelsStyle);
        statsTable.add(roundsLabel).left().align(RIGHT);
        statsTable.row();
        // Time
        statsTable.row().pad(10, 0, 10, 0);

        playerHealth = new ProgressBar(0, localPlayer.getMaxLives(), 1, false, healthBarStyle);
        playerHealth.setValue(localPlayer.getCurrentLives());
        playerHealth.setAnimateDuration(1);

        Drawable menuImage = new TextureRegionDrawable(getAssetManager().get(MENU_ICON_PLACEHOLDER, Texture.class));

        towerSelect = new Table();

        //Erstellen der Towerbuttons
        logicController.createTowerButtons(this);

        //Exit
        final Table menu = new Table();
        ImageButton menuButton = new ImageButton(menuImage);
        menuButton.setSize(10, 10);
        menuButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (!logicController.isPause() & !logicController.isMultiplayer()) {
                    logicController.setPause(true);
                    //pauseScreen();
                } else if (logicController.isMultiplayer()) {
                    logicController.setPause(false);
                }
                showPauseScreen();
            }
        });

        menu.add(menuButton).size(X_SIZE, Y_SIZE);

        initPopUpContent();

        messageArea = new Table();

        //Toprow table
        final Table topRow = new Table();
        final Table topLeft = new Table();
        if (logicController.isMultiplayer()) {
            topLeft.add(opponent).left().padRight(10).padLeft(10);
        }
        topRow.add(menu).top().right();
        topRow.setBounds(0, 50, defaultScreen.getWidth(), defaultScreen.getHeight());

        final Table bottomOfScreen = new Table();
        bottomOfScreen.add(towerSelect).expandX();

        countdown = new Table();
        countdown.add(new Label("Zeit bis zur nächsten Runde: ", infoLabelsStyle)).right().padLeft(10);
        String timeLabelText = ("" + (int) Math.max(0, gameState.getTimeUntilNextRound()));
        timelabel = new Label(timeLabelText, infoLabelsStyle);
        countdown.add(timelabel).top().left().align(RIGHT);

        topLeft.add(messageArea).center().top().padLeft(50).colspan(2);
        topLeft.setBounds(0, 50, 100, 100);
        defaultScreen.add(topLeft).left();
        defaultScreen.add(topRow).top().right().expandX().colspan(2);
        defaultScreen.row();

        statsTable.add(playerHealth).left().align(RIGHT).expandX().padRight(-40);
        defaultScreen.add(statsTable).top().right().colspan(4).padRight(20).row();
        defaultScreen.add(countdown).top().right().colspan(4).padRight(20).row();
        if (logicController.isMultiplayer()) {
            defaultScreen.add(sendEnemy).top().right().colspan(4).padRight(20).row();
        }
        defaultScreen.add(towerinfoTable).left().colspan(1).padLeft(10);
        defaultScreen.add().expand().colspan(4);
        defaultScreen.row();
        defaultScreen.add(bottomOfScreen).bottom().center().colspan(4).expandX();
        mainUiStack.addActor(defaultScreen);

        getUi().addActor(mainUiStack);
        multiplexer.addProcessor(getUi());
    }

    private void showPauseScreen() {
        defaultScreen.setVisible(false);
        pauseGroup.setVisible(true);
    }

    /**
     * Initialisiert die Komponenten des Userinterfaces die nur für den Multiplayer benötigt werden.
     */
    private void initMultiplayerUiComponents(){
        opponentHealth = new ProgressBar(0, opposingPlayer.getMaxLives(), 1, false, healthBarStyle);
        opponentHealth.setScale(1 / 2);
        opponentHealth.setValue(opposingPlayer.getCurrentLives());
        opponentHealth.setAnimateDuration(1);
        Label.LabelStyle scoreLabelStyle = new Label.LabelStyle();
        scoreLabelStyle.font = getBitmapFont();
        opponentScore = new Label("Punkte " + opposingPlayer.getScore(), scoreLabelStyle);
        opponent.setBounds(0, 50, 100, 100);
        opponent.add(new Label(opposingPlayer.getPlayerName(), scoreLabelStyle)).left().row();
        opponent.add(opponentScore).left().row();
        opponent.add(opponentHealth).left();

        Drawable sendRegEnemyIcon = new TextureRegionDrawable(getAssetManager().get(SEND_REGULAR_ENEMY_ICON, Texture.class));
        Drawable sendHvyEnemyIcon = new TextureRegionDrawable(getAssetManager().get(SEND_HEAVY_ENEMY_ICON, Texture.class));

        ImageButton sendRegEnemy = new ImageButton(sendRegEnemyIcon);
        ImageButton sendHvyEnemy = new ImageButton(sendHvyEnemyIcon);

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
        sendRegEnemy.addListener(new TextTooltip("Sende deinem Gegenspieler einen zusätzlichen leichten Gegner \n" + "Kosten: 50 Gold", tooltipManager, skin));
        sendHvyEnemy.addListener(new TextTooltip("Sende deinem Gegenspieler einen zusätzlichen schweren Gegner \n" + "Kosten: 100 Gold", tooltipManager, skin));
        sendEnemy.add(sendRegEnemy).size(X_SIZE, Y_SIZE).padBottom(5).row();
        sendEnemy.add(sendHvyEnemy).size(X_SIZE, Y_SIZE);
    }

    /**
     * Initialisiert die Inhalte des Turmmanagement Pop Up menu
     */
    private void initPopUpContent(Player localPlayer) {
        Drawable upgradeIcon = new TextureRegionDrawable(getAssetManager().get(UPRADE_ICON, Texture.class));
        Drawable sellIcon = new TextureRegionDrawable(getAssetManager().get(SELL_ICON, Texture.class));

        ImageButton upgradeButton = new ImageButton(upgradeIcon);
        ImageButton sellButton = new ImageButton(sellIcon);

        upgradeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                logicController.upgradeTower(xCoord, yCoord, localPlayer.getPlayerNumber());
                popUpButtons.setVisible(false);
            }
        });
        sellButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                logicController.sellTower(xCoord, yCoord, localPlayer.getPlayerNumber());
                popUpButtons.setVisible(false);
            }
        });

        upgradeButton.addListener(new TextTooltip("Upgraden", tooltipManager, skin));
        sellButton.addListener(new TextTooltip("Verkaufen", tooltipManager, skin));

        popUpButtons = new Group();
        upgradeSell = new Table();
        upgradeSell.add(upgradeButton).size(X_SIZE, Y_SIZE).row();
        upgradeSell.add(sellButton).size(X_SIZE, Y_SIZE);
    }

    /**
     * Initialisiert die Stylekomponenten der Progressbar welche für die Lebensanzeigen der Spieler verwendet wird
     */
    private void initProgressbarStyle(){
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
        healthBarStyle = new ProgressBar.ProgressBarStyle();
        healthBarStyle.background = redBG;
        healthBarStyle.knob = hiddenBar;
        healthBarStyle.knobBefore = greenBar;
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
        if(defaultScreen.isVisible()) {
            messageArea.clear();
            messageArea.add(messageLabel);
        }
        else{
            menuMessageArea.clear();
            menuMessageArea.add(messageLabel);
        }
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
     * Kehrt den "checked"-Zustand eines TowerButtons um
     * @param buttonNumber Die Nummer des TowerButtons
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
     * Erstellt/Zeigt den Pausenbildschirm an. Das Spiel UI wird ausgeblendet, während das Spiel pausiert ist.
     */
    private void initPauseScreen() {
        pauseGroup = new Group();
        background.setSize(getStageViewport().getScreenWidth(), getStageViewport().getScreenHeight());

        Table buttonTable = new Table();
        menuMessageArea = new Table();
        TextButton resume = new TextButton("Resume", skin);
        resume.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (!logicController.isMultiplayer()) {
                    logicController.setPause(false);
                }
                pauseGroup.setVisible(false);
                defaultScreen.setVisible(true);
            }
        });

        TextButton save = new TextButton("Save", skin);
        save.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                logicController.saveGame("Spielstand");
            }
        });

        TextButton back2main = new TextButton("Menu", skin);

            TextButton load = new TextButton("Quickload", skin);
            load.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    getMainController().loadLastSaveGame();
                }
            });
            back2main.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    logicController.exitGame(logicController.isServer());
                }
            });

            buttonTable.add(resume).top().center().spaceBottom(10).row();
            if (!logicController.isMultiplayer()) {
                buttonTable.add(save).top().center().spaceBottom(10).row();
                buttonTable.add(load).top().center().spaceBottom(10).row();
            }
            buttonTable.add(back2main).top().center().row();
            buttonTable.setSize(getStageViewport().getScreenWidth(), getStageViewport().getScreenHeight());

            menuMessageArea.setSize(getStageViewport().getScreenWidth(), getStageViewport().getScreenHeight()/buttonTable.getRows());

            pauseGroup.addActor(background);
            pauseGroup.addActor(buttonTable);
            pauseGroup.addActor(menuMessageArea);

            getUi().addActor(pauseGroup);

        if (logicController.isMultiplayer()) {
            back2main.addListener(new TextTooltip("Warnung: Diese Aktion trennt die Verbindung und beendet die Partie", tooltipManager, skin));
            buttonTable.add(resume).top().center().spaceBottom(10).row();
            if(logicController.isServer()){
                buttonTable.add(save).top().center().spaceBottom(10).row();
            }
            buttonTable.add(back2main).top().center().row();
            buttonTable.setSize(getStageViewport().getScreenWidth(), getStageViewport().getScreenHeight());

            pauseGroup.addActor(background);
            pauseGroup.addActor(buttonTable);
            pauseGroup.addActor(menuMessageArea);

            getUi().addActor(pauseGroup);
        }
        pauseGroup.setVisible(false);
    }

    /**
     * Der Bildschirm der am Ende vom Spiel dargestellt wird.
     * Gewonnen/Verloren + Punkte + Highscore des Users
     *
     * @param victorious Gibt an, ob der lokale Spieler siegreich war
     * @param score Die Punkte des lokalen Spielers
     * @param highscore Der Highscore des lokalen Spielers
     */
    @Override
    public void endOfGameScreen(boolean victorious, int score, int highscore) {
        endScreenGroup = new Group();

        Image resultImage;
        if (victorious) {
            resultImage = new Image(getAssetManager().get(WIN_IMAGE, Texture.class));
        } else {
            resultImage = new Image(getAssetManager().get(LOSE_IMAGE, Texture.class));
        }

        Label currentScoreLabel = new Label("Erzielte Punkte: " + score, skin);
        Label highscoreLabel = new Label("Highscore: " + highscore, skin);

        Table buttonTable = new Table();

        TextButton back2main = new TextButton("Menu", skin);
        back2main.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                logicController.exitGame(false);
            }
        });
        TextButton stats = new TextButton("Statistiken", skin);
        stats.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y){
                endScreenGroup.setVisible(false);
                gameStatsScreen();
            }
        });

        buttonTable.add(currentScoreLabel).center().row();
        buttonTable.add(highscoreLabel).center().row();
        buttonTable.add(stats).top().center().spaceRight(10);
        buttonTable.add(back2main).top().center().row();
        buttonTable.setSize(getStageViewport().getScreenWidth(), getStageViewport().getScreenHeight() / 2);

        endScreenGroup.addActor(background);
        endScreenGroup.addActor(resultImage);
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
        popUpButtons.addActor(upgradeSell);
        popUpButtons.setSize(50, 110);
        popUpButtons.setPosition(posXAllignment, posY);
        popUpButtons.setVisible(true);
        tableDecayTimer = 2;
        getUi().addActor(popUpButtons);
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
     * Erstellt einen Bildschirm der am Ende des Spiels aufgerufen werden kann.
     * Zeigt diverse Statistiken über die abgeschlossene Partie an.
     */
    private void gameStatsScreen() {
        Group statScreen = new Group();

        Table firstColum = new Table();
        Table secondColum = new Table();
        Table thirdColum = new Table();
        firstColum.add(new Label("Statistiken:", skin)).row();
        secondColum.add(new Label(localPlayer.getPlayerName(), skin)).row();
        firstColum.add(new Label("Score", skin)).row();
        secondColum.add(new Label(Integer.toString(localPlayer.getScore()), skin)).row();
        firstColum.add(new Label("Besiegte Gegner", skin)).row();
        secondColum.add(new Label(Integer.toString(localPlayer.getKillTracker()), skin)).row();
        firstColum.add(new Label("Abgeschlossene Wellen", skin)).row();
        secondColum.add(new Label(Integer.toString(logicController.getGamestate().getRoundNumber()), skin)).row();
        firstColum.add(new Label("Erbaute Tuerme", skin)).row();
        secondColum.add(new Label(Integer.toString(localPlayer.getTowerCounter()), skin)).row();
        firstColum.add(new Label("Verdientes Gold", skin)).row();
        secondColum.add(new Label(Integer.toString(localPlayer.getResources() - 1000), skin)).row();

        if(logicController.isMultiplayer()){
            thirdColum.add(new Label(opposingPlayer.getPlayerName(), skin)).row();
            thirdColum.add(new Label(Integer.toString(opposingPlayer.getScore()), skin)).row();
            thirdColum.add(new Label(Integer.toString(opposingPlayer.getKillTracker()), skin)).row();
            thirdColum.add(new Label(Integer.toString(logicController.getGamestate().getRoundNumber()), skin)).row();
            thirdColum.add(new Label(Integer.toString(opposingPlayer.getTowerCounter()), skin)).row();
            thirdColum.add(new Label(Integer.toString(opposingPlayer.getResources() -1000), skin)).row();
        }

        TextButton back = new TextButton("Zurueck", skin);
        back.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                statScreen.setVisible(false);
                endScreenGroup.setVisible(true);
            }
        });

        Table statTable = new Table();
        statTable.add(firstColum).center().expandX();
        statTable.add(secondColum).center().expandX();
        if(logicController.isMultiplayer()){
            statTable.add(thirdColum).center().expandX();
            statTable.row();
            statTable.add(back).center().colspan(3);
        }
        else {
            statTable.row();
            statTable.add(back).center().colspan(2);
        }
        statTable.setSize(getStageViewport().getScreenWidth(), getStageViewport().getScreenHeight());
        statScreen.addActor(background);
        statScreen.addActor(statTable);

        getUi().addActor(statScreen);
    }
    /**
     * Legt einen Spielzustand als Attribut der GameView fest
     *
     * @param gameState Der als Attribut festzulegende Spielzustand
     */
    @Override
    public void setGameState(Gamestate gameState) {
        this.gameState = gameState;
    }

    /**
     *
     */
    @Override
    public void clearGameObjects() {
        List<GameObject> objectsToRemove = new LinkedList<>();
        gameObjects.forEach(gameObject -> {
            if (gameObject instanceof TowerObject) {
                objectsToRemove.add(gameObject);
            }
        });
        objectsToRemove.forEach(gameObjects::remove);
    }

    /**
     * Nimmt Anpasssungen bei Änderung der Fenstergröße vor
     *
     * @param width Die neue Breite.
     * @param height Die neue Höhe.
     */
    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        background.setSize(getStageViewport().getScreenWidth(), getStageViewport().getScreenHeight());

    }
}
