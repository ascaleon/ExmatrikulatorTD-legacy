package de.diegrafen.exmatrikulatortd.view.screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import de.diegrafen.exmatrikulatortd.ExmatrikulatorTD;
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
import static de.diegrafen.exmatrikulatortd.controller.factories.TowerFactory.*;

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

    private final Table defaultScreen = new Table();

    private Group pauseGroup;
    // TODO: Variablen sinnvollere Bezeichnungen geben bzw. ersetzen

    private boolean t1 = false;

    private boolean t2 = false;

    private boolean t3 = false;

    private boolean t4 = false;

    private boolean u = false;

    private boolean s = false;

    private Skin skin = new Skin(Gdx.files.internal("ui-skin/glassy-ui.json"));
    private ImageButton tower1;
    private ImageButton tower2;
    private ImageButton tower3;
    private TextButton tower4;
    private TextButton upgrade;
    private TextButton sell;

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
                if (keycode == Input.Keys.Q) {
                    buttonManager(tower1);
                }
                if (keycode == Input.Keys.W) {
                    buttonManager(tower2);
                }
                if (keycode == Input.Keys.E) {
                    buttonManager(tower3);
                }
                if (keycode == Input.Keys.R) {
                    buttonManager(tower4);
                }
                if (keycode == Input.Keys.D) {
                    buttonManager(upgrade);
                }
                if (keycode == Input.Keys.S) {
                    buttonManager(sell);
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

//        final Table defaultScreen = new Table();
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
        //Skin skin = new Skin(Gdx.files.internal("ui-skin/glassy-ui.json"));
        Drawable towerImage1 = new TextureRegionDrawable(new Texture(Gdx.files.internal("sprites/objects/towers/WanderingEye1.png")));
        Drawable towerImage1_selected = new TextureRegionDrawable(new Texture(Gdx.files.internal("sprites/objects/towers/WanderingEye1_selected.png")));
        Drawable towerImage2 = new TextureRegionDrawable(new Texture(Gdx.files.internal("sprites/objects/towers/WanderingEye2.png")));
        Drawable towerImage2_selected = new TextureRegionDrawable(new Texture(Gdx.files.internal("sprites/objects/towers/WanderingEye2_selected.png")));
        Drawable towerImage3 = new TextureRegionDrawable(new Texture(Gdx.files.internal("sprites/objects/towers/WanderingEye3.png")));
        Drawable towerImage3_selected = new TextureRegionDrawable(new Texture(Gdx.files.internal("sprites/objects/towers/WanderingEye3_selected.png")));
        TextButtonStyle style = new TextButtonStyle();
        final Table towerSelect = new Table();
        //towerSelect.setDebug(true);

        //Die einzelnen Towerbuttons
        tower1 = new ImageButton(towerImage1, towerImage1, towerImage1_selected);
        tower2 = new ImageButton(towerImage2, towerImage2, towerImage2_selected);
        tower3 = new ImageButton(towerImage3, towerImage3, towerImage3_selected);
        tower4 = new TextButton("T4", skin);
        upgrade = new TextButton("^", skin);
        sell = new TextButton("$$$", skin);

        //InputListener für die Buttons
        tower1.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                buttonManager(tower1);
            }
        });
        tower2.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                buttonManager(tower2);
            }
        });
        tower3.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                buttonManager(tower3);
            }
        });
        tower4.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                buttonManager(tower4);
            }
        });
        upgrade.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                buttonManager(upgrade);
            }
        });
        sell.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                buttonManager(sell);
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
                if(isPause()== false) {
                    System.out.println("Pausiert");
                    //exitButton.setColor(255,0,0,255);
                    //exitButton.setText(">");
                    //Gdx.files.internal("ui-skin/glassy-ui.png");
                    //Gdx.app.exit();
                    setPause(!isPause());
                    pauseScreen();
                }
                else{
                    //exitButton.setColor(Color.valueOf("ffffffff"));
                    //exitButton.setText("| |");
                    setPause(!isPause());
                    pauseScreen();
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

    private void buttonManager(Actor a){
        tower1.setChecked(false);
        tower2.setChecked(false);
        tower3.setChecked(false);
        tower4.setColor(Color.valueOf("ffffffff"));
        upgrade.setColor(Color.valueOf("ffffffff"));
        sell.setColor(Color.valueOf("ffffffff"));
        if(a == tower1){
            if(!t1) {
                tower1.setChecked(true);
                t1 = true;
                t2 = false;
                t3 = false;
                t4 = false;
                u = false;
                s = false;
            }
            else{
                t1 = false;
            }
        }
        else if(a == tower2){
            if(!t2) {
                tower2.setChecked(true);
                t1 = false;
                t2 = true;
                t3 = false;
                t4 = false;
                u = false;
                s = false;
            }
            else {
                t2 = false;
            }
        }
        else if(a == tower3){
            if(!t3) {
                tower3.setChecked(true);
                t1 = false;
                t2 = false;
                t3 = true;
                t4 = false;
                u = false;
                s = false;
            }
            else{
                t3 = false;
            }
        }
        else if(a == tower4){
            if(!t4) {
                tower4.setColor(Color.GREEN);
                t1 = false;
                t2 = false;
                t3 = false;
                t4 = true;
                u = false;
                s = false;
            }
            else{
                t4 = false;
            }
        }
        else if(a == upgrade){
            if(!u) {
                upgrade.setColor(Color.YELLOW);
                t1 = false;
                t2 = false;
                t3 = false;
                t4 = false;
                u = true;
                s = false;
            }
            else{
                u = false;
            }
        }
        else if(a == sell){
            if(!s) {
                sell.setColor(Color.YELLOW);
                t1 = false;
                t2 = false;
                t3 = false;
                t4 = false;
                u = false;
                s = true;
            }
            else{
                s = false;
            }
        }
    }

    private void pauseScreen() {
        Skin skin = new Skin(Gdx.files.internal("ui-skin/glassy-ui.json"));
        if(isPause() == true){
            pauseGroup = new Group();
            Image semiTBG = new Image(new Texture(Gdx.files.internal("transparentBG.png")));
            //semiTBG.setSize(500,500);
            semiTBG.setSize(getStageViewport().getScreenWidth(), getStageViewport().getScreenHeight());
            Table buttonTable = new Table();
            TextButton resume = new TextButton("Resume", skin);
            resume.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    setPause(!isPause());
                    pauseGroup.setVisible(false);
                    defaultScreen.setVisible(true);
                }
            });
            buttonTable.add(resume).top().center().spaceBottom(10).row();
            TextButton back2main = new TextButton("Menu", skin);
            back2main.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    logicController.exitGame(false);
                    //System.out.println("Menu nicht gefunden");
                }
            });
            buttonTable.add(back2main).top().center().row();
            buttonTable.setSize(getStageViewport().getScreenWidth(), getStageViewport().getScreenHeight());
            pauseGroup.addActor(semiTBG);
            //semiTBG.setAlign(MIDDLE);
            pauseGroup.addActor(buttonTable);
            //pauseGroup.
            defaultScreen.setVisible(false);
            getUi().addActor(pauseGroup);
        }
        else{
            pauseGroup.setVisible(false);
        }
    }
}
