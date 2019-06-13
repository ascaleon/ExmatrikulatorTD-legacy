package de.diegrafen.towerwars.screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureAdapter;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import de.diegrafen.towerwars.Assets;
import de.diegrafen.towerwars.gameworld.GameMap;
import de.diegrafen.towerwars.net.MultiplayerClient;
import de.diegrafen.towerwars.persistence.PlayerDao;
import de.diegrafen.towerwars.Towerwars;
import de.diegrafen.towerwars.sprites.Enemy;

import static com.badlogic.gdx.Input.*;


/**
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 14.05.2019 21:51
 */
public class PlayScreen extends AbstractScreen {

    private boolean isMultiPlayer;

    private MultiplayerClient multiplayerClient;


    private TiledMap map;
    private OrthogonalTiledMapRenderer mapRenderer;
    private OrthographicCamera camera;

    private SpriteBatch batch;

    private Enemy enemy;

    // buy tower widgets
    private Table buyTowerSelectionTable;
    //private HorizontalTowersWidget towerListWidget;
    private Label waveLabel;
    private Label waveDescriptionLabel;

    // used for tower information
    private Table towerInfoTable;
    //private TowerWidget towerInfoWidget;
    private boolean isDragging = false;

    // achievement display
    private Table achievementTable;
    private Label achievementTitle;
    private Label achievementDescriptionLabel;

    // gui when active or inactive
    private Table pausedTable;
    private Stack gameStack;

    private Button fastforwardButton;
    private Button pauseButton;

    private Label finishLabel;
    private LabelStyle finishLabelStyle;
    private Label endScoreLabel;
    private Label levelDifficultyBonusLabel;
    private Label mapOfTheDayBonusLabel;
    private Label remainingLivesBonusLabel;
    private Label victoryBonusLabel;
    private Label finalScoreLabel;

    private Table endScreenTable;

    private boolean gameHasEnded = false;

    private PlayerDao playerDao = new PlayerDao();

    Towerwars game;

    private GameMap gameMap;

    public PlayScreen(Towerwars game, GameMap gameMap) {
        this.game = game;
        this.gameMap = gameMap;
        this.gameMap.setCamera(stageCamera);

        gameMap.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!isDragging && !isPaused()) {
                    handleClick(gameMap, x, y);

                }
            }
        });

        createUI();

        stage.addActor(gameMap);
    }

    /**
     * Called when the screen should render itself.
     *
     * @param delta The time in seconds since the last render.
     */
    @Override
    public void render(float delta) {


//        mapRenderer.getBatch().setProjectionMatrix(camera.combined);
//        Gdx.gl.glClearColor(0, 0, 0, 1);
//        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
//        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//        camera.update();
//        mapRenderer.setView(camera);
//        mapRenderer.render();

        super.render(delta);

    }


    @Override
    public void hide() {
        super.hide();

        //persistGame();
    }

    public boolean gameHasEnded() {
        return gameHasEnded;
    }

    @Override
    public void setPaused(boolean paused) {
        // pausing not enabled while showing the initial tooltip
        //if (isShowingInitialTooltip) return;

        super.setPaused(paused);

        pausedTable.setVisible(paused);

        //hideTowerInfoDialog();
        //hideBuyTowerDialog();

        fastforwardButton.setDisabled(paused);
        pauseButton.setVisible(!paused);

        setSpeed(1);
    }

    private void createUI() {
        // separation between paused and normal mode
        final Stack mainUiStack = new Stack();
        mainUiStack.setFillParent(true);

        // create game screen
        {
            gameStack = new Stack();
            gameStack.setFillParent(true);

            endScreenTable = new Table();
            endScreenTable.setFillParent(true);
            endScreenTable.setVisible(false);

            finishLabelStyle = new LabelStyle();
            finishLabelStyle.font = Assets.TITLE_FONT_64;
            finishLabel = new Label("", finishLabelStyle);

            endScreenTable.add(finishLabel).expandX().top().padTop(20).colspan(4);
            endScreenTable.row();

            final ButtonStyle exitButtonStyle = new ButtonStyle();
            exitButtonStyle.up = new TextureRegionDrawable(new TextureRegion(new TextureRegion(new Texture(Gdx.files.internal("sprites/hud/exittotitlebutton.png")))));
            exitButtonStyle.down = new TextureRegionDrawable(new TextureRegion(new TextureRegion(new Texture(Gdx.files.internal("sprites/hud/exittotitlebuttonpressed.png")))));
            Button exitToTitleButton = new Button(exitButtonStyle);

            exitToTitleButton.addListener(new ChangeListener() {

                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    game.switchToMenu();
                }

            });

            final Table scoreTable = new Table();
            scoreTable.setBackground(new TextureRegionDrawable(Assets.getTexture(Assets.BACKGROUND)));

            final LabelStyle endScoreLabelStyle = new LabelStyle();
            endScoreLabelStyle.font = Assets.TEXT_FONT;
            endScoreLabelStyle.fontColor = Color.BLACK;

            final LabelStyle endScoreValueLabelStyle = new LabelStyle();
            endScoreValueLabelStyle.font = Assets.TEXT_FONT;
            endScoreValueLabelStyle.fontColor = Assets.COLOR_YELLOW;
            //			endScoreValueLabelStyle.fontColor = new Color(160 / 255f, 155 / 255f, 16 / 255f, 1);

            endScoreLabel = new Label("", endScoreValueLabelStyle);
            levelDifficultyBonusLabel = new Label("", endScoreValueLabelStyle);
            mapOfTheDayBonusLabel = new Label("", endScoreValueLabelStyle);
            remainingLivesBonusLabel = new Label("", endScoreValueLabelStyle);
            victoryBonusLabel = new Label("", endScoreValueLabelStyle);
            finalScoreLabel = new Label("", endScoreValueLabelStyle);

            scoreTable.add().expandX();
            scoreTable.add(new Label("Score:", endScoreLabelStyle)).left();
            scoreTable.add(endScoreLabel).left().padLeft(3);
            scoreTable.add().expandX();
            scoreTable.row();

            scoreTable.add().expandX();
            scoreTable.add(new Label("Map of the Day Bonus:", endScoreLabelStyle)).left();
            scoreTable.add(mapOfTheDayBonusLabel).left().padLeft(3);
            scoreTable.add().expandX();
            scoreTable.row();

            scoreTable.add().expandX();
            scoreTable.add(new Label("Level Difficulty Bonus:", endScoreLabelStyle)).left();
            scoreTable.add(levelDifficultyBonusLabel).left().padLeft(3);
            scoreTable.add().expandX();
            scoreTable.row();

            scoreTable.add().expandX();
            scoreTable.add(new Label("Remaining Lives Bonus:", endScoreLabelStyle)).left();
            scoreTable.add(remainingLivesBonusLabel).left().padLeft(3);
            scoreTable.add().expandX();
            scoreTable.row();

            scoreTable.add().expandX();
            scoreTable.add(new Label("Victory Bonus:", endScoreLabelStyle)).left();
            scoreTable.add(victoryBonusLabel).left().padLeft(3);
            scoreTable.add().expandX();
            scoreTable.row();

            scoreTable.add().expandX();
            scoreTable.add(new Label("Final Score:", endScoreLabelStyle)).left();
            scoreTable.add(finalScoreLabel).left().padLeft(3);
            scoreTable.add().expandX();
            scoreTable.row();

            endScreenTable.add(scoreTable).expand().center();
            endScreenTable.row();

            endScreenTable.add(exitToTitleButton).colspan(4).expand().bottom().padBottom(3);

            gameStack.add(endScreenTable);

            final TextureRegionDrawable background = new TextureRegionDrawable(new TextureRegion(Assets.getTexture(Assets.BACKGROUND_SMALL)));

            // active when the game is in progress
            final Table defaultScreen = new Table();
            defaultScreen.setFillParent(true);

            // info labels
            final LabelStyle infoLabelsStyle = new LabelStyle();
            infoLabelsStyle.font = Assets.TEXT_FONT;
            infoLabelsStyle.fontColor = Color.BLACK;

            final LabelStyle scoreLabelStyle = new LabelStyle();
            scoreLabelStyle.font = Assets.TEXT_FONT;
            scoreLabelStyle.fontColor = Assets.COLOR_YELLOW;

            final LabelStyle liveLabelStyle = new LabelStyle();
            liveLabelStyle.font = Assets.TEXT_FONT;
            liveLabelStyle.fontColor = Assets.COLOR_GREEN;

            final Table statsTable = new Table();
            statsTable.setBackground(background);

            // score
            statsTable.add(new Label("Score: ", infoLabelsStyle)).left().padLeft(10).expandX();
            final Label scoreLabel = new Label("9001"//numberSepeartor((int) gameWorld.getPlayer().getScore())
                    , scoreLabelStyle);
            statsTable.add(scoreLabel).left().align(Align.right); //BaseTableLayout.RIGHT
            // );

            // money
            statsTable.add(new Label("Money: ", infoLabelsStyle)).left().padLeft(10).expandX();
            final Label resourcesLabel = new Label("Blah"//numberSepeartor((int) gameWorld.getPlayer().getMoney())
                    , scoreLabelStyle);
            statsTable.add(resourcesLabel).left().align(Align.right); //BaseTableLayout.RIGHT

            // lives
            statsTable.add(new Label("Lives: ", infoLabelsStyle)).left().padLeft(10).expandX();
            final Label livesLabel = new Label("9001"//numberSepeartor((int) gameWorld.getPlayer().getScore())
                    , liveLabelStyle);
            statsTable.add(livesLabel).left().align(Align.right); //BaseTableLayout.RIGHT

            defaultScreen.add(statsTable).top().center().expandX().colspan(4);
            defaultScreen.row();

            defaultScreen.add().expand().colspan(3);
            defaultScreen.row();

//            gameWorld.getPlayer().addStatChangedListener(new StatChangedListener() {
//
//                @Override
//                public void statsChanged(Profile player, String stat) {
//                    scoreLabel.setText(numberSepeartor((int) player.getScore()));
//                    resourcesLabel.setText(numberSepeartor((int) player.getMoney()));
//                    livesLabel.setText(numberSepeartor(player.getLives()));
//                }
//            });

            // achievements
            achievementTable = new Table();
            achievementTable.setVisible(false);
            achievementTable.setBackground(new TextureRegionDrawable(Assets.getTexture(Assets.BACKGROUND)));

            final Image achievementIcon = new Image(new TextureRegionDrawable(Assets.getTexture(Assets.ACHIEVEMENT_UNLOCKED_ICON)));
            achievementTable.add(achievementIcon).left();

            final Table achievementInfo = new Table();
            achievementInfo.setFillParent(true);

            final LabelStyle achievementTitleStyle = new LabelStyle();
            achievementTitleStyle.font = Assets.TEXT_FONT;
            achievementTitleStyle.fontColor = Color.BLACK;
            achievementTitle = new Label("", achievementTitleStyle);
            achievementInfo.add(achievementTitle).left().expandX();
            achievementInfo.row();

            final LabelStyle achievementDescriptionStyle = new LabelStyle();
            achievementDescriptionStyle.font = Assets.TEXT_FONT;
            achievementDescriptionStyle.fontColor = Color.BLACK;
            achievementDescriptionLabel = new Label("", achievementTitleStyle);
            achievementDescriptionLabel.setWrap(true);
            achievementInfo.add(achievementDescriptionLabel).left().expandX().minWidth(Value.percentWidth(0.75f));

            achievementTable.add(achievementInfo).left();

            defaultScreen.add(achievementTable).expandX().colspan(3).minWidth(Value.percentWidth(0.8f));
            defaultScreen.row();

            final Table pauseFastForwardTable = new Table();

            // paused button
            final TextureRegionDrawable pauseUp = new TextureRegionDrawable(new TextureRegion(Assets.getTexture(Assets.PAUSE_BUTTON_UP)));
            final TextureRegionDrawable pauseDown = new TextureRegionDrawable(new TextureRegion(Assets.getTexture(Assets.PAUSE_BUTTON_DOWN)));
            pauseButton = new Button(pauseUp, pauseDown);
            pauseButton.addListener(new ChangeListener() {

                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    setPaused(true);
                }
            });
            pauseFastForwardTable.add(pauseButton).left();

            // fastforward button
            final ButtonStyle fastForwardButtonStyle = new ButtonStyle();
            fastForwardButtonStyle.up = new TextureRegionDrawable(new TextureRegion(Assets.getTexture(Assets.FAST_FORWARD_UP)));
            fastForwardButtonStyle.down = new TextureRegionDrawable(new TextureRegion(Assets.getTexture(Assets.FAST_FORWARD_DOWN)));
            fastForwardButtonStyle.checked = new TextureRegionDrawable(new TextureRegion(Assets.getTexture(Assets.FAST_FORWARD_CHECKED)));

            fastforwardButton = new Button(fastForwardButtonStyle);
            fastforwardButton.addListener(new ChangeListener() {

                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    //toggleFastForward();
                }

            });

            pauseFastForwardTable.add(fastforwardButton).left();

            defaultScreen.add(pauseFastForwardTable).expandX().left();

            // wave
            final LabelStyle waveStyle = new LabelStyle();
            waveStyle.font = Assets.TEXT_FONT;
            waveStyle.fontColor = Color.BLACK;

            waveDescriptionLabel = new Label("Wave: ", waveStyle);
            defaultScreen.add(waveDescriptionLabel).expandX().right();
            waveLabel = new Label("2/25",//"String.valueOf(gameWorld.getWaveSpawner().getWave()) + "/" + String.valueOf(gameWorld.getWaveSpawner().getMaxWaves()),
                    waveStyle);
            defaultScreen.add(waveLabel).right().padRight(5);
//            gameWorld.getWaveSpawner().addWaveListener(new WaveListener() {
//
//                @Override
//                public void nextWave(int wave) {
//                    //waveLabel.setText(String.valueOf(wave) + "/" + String.valueOf(gameWorld.getWaveSpawner().getMaxWaves()));
//                }
//            });

            waveLabel.setText(String.valueOf(2) + "/" + String.valueOf(25));

            gameStack.add(defaultScreen);

            // tower info
            towerInfoTable = new Table();
            towerInfoTable.setVisible(false);
            towerInfoTable.setFillParent(true);

            //towerInfoWidget = new TowerWidget();
            //towerInfoWidget.addSellClickListener(new ClickListener() {
            //@Override
            //public void clicked(InputEvent event, float x, float y) {
            //super.clicked(event, x, y);

            //AbstractTower currentTower = towerInfoWidget.getCurrentTower();
            //gameWorld.removeTower(currentTower);
            //gameWorld.getPlayer().increaseMoney(currentTower.getSellValue());

            //hideTowerInfoDialog();

            //}
            //});

/*            towerInfoWidget.addUpgradeClickListener(new ChangeListener() {

                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    final AbstractTower currentTower = towerInfoWidget.getCurrentTower();

                    currentTower.upgrade();
                    gameWorld.getPlayer().increaseMoney(-currentTower.getUpgradeCost());

                    if (currentTower.getLevel() == 4) {
                        TD.unlockAchievement(AchievementType.LEVEL_4);
                    }

                    towerInfoWidget.refresh();
                }
            });*/

            //towerInfoTable.add(towerInfoWidget);

            gameStack.add(towerInfoTable);

/*            // create initial tooltip if needed
            if (TD.getPlayed() == 1 && TD.getGamesPlayed() == 1) {
                setPaused(true, false);
                isShowingInitialTooltip = true;

                fastforwardButton.setDisabled(true);
                pauseButton.setDisabled(true);

                final Table startInfoTable = new Table();
                startInfoTable.setFillParent(true);
                startInfoTable.setBackground(new TextureRegionDrawable(Assets.getTexture(Assets.BACKGROUND)));

                final Label.LabelStyle infoTableLabelStyle = new Label.LabelStyle();
                infoTableLabelStyle.font = Assets.TEXT_FONT;
                infoTableLabelStyle.fontColor = Color.BLACK;

                final Label infoLabel = new Label(
                        INITIAL_TOOLTIP,
                        infoTableLabelStyle);
                infoLabel.setAlignment(Align.center);
                infoLabel.setWrap(true);

                startInfoTable.add().expand();
                startInfoTable.row();

                startInfoTable.add(infoLabel).expandX().center().minWidth(Value.percentWidth(0.95f));
                startInfoTable.row();

                ButtonStyle continueButtonStyle = new ButtonStyle();
                continueButtonStyle.up = new TextureDrawable(new Texture(Gdx.files.internal("sprites/hud/continuebuttonsmall.png")));
                continueButtonStyle.down = new TextureDrawable(new Texture(Gdx.files.internal("sprites/hud/continuebuttonsmallpressed.png")));

                final Button continueButton = new Button(continueButtonStyle);
                continueButton.addListener(new ChangeListener() {
                    @Override
                    public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                        startInfoTable.setVisible(false);
                        isShowingInitialTooltip = false;

                        fastforwardButton.setDisabled(false);
                        pauseButton.setDisabled(false);

                        setPaused(false);
                    }
                });

                startInfoTable.add(continueButton).center().spaceTop(10).expandX();
                startInfoTable.row();

                startInfoTable.add().expand();
                startInfoTable.row();

                gameStack.add(startInfoTable);
            }*/

            mainUiStack.add(gameStack);
        }

        ui.addActor(mainUiStack);
    }

    public void addInputFunctionality(Stage stage) {
        // dragging
        stage.addListener(new InputListener() {

            private float x;
            private float y;
            private float distance;

            @Override
            public boolean keyUp(InputEvent event, int keycode) {
                if (keycode == Keys.ESCAPE) {
                    Gdx.app.exit();
                    return true;
                }

                return false;
            }

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                this.x = x;
                this.y = y;

                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                isDragging = false;
                distance = 0;
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                if (buyTowerSelectionTable.isVisible())
                    return;

                float offsetX = this.x - x;
                float offsetY = this.y - y;

                distance += Math.hypot(offsetX, offsetY);

                if (distance < 1.0)//MIN_DRAG_DISTANCE)
                    return;

                isDragging = true;

                stageCamera.translate(offsetX, offsetY);

                if (stageCamera.position.x - stageCamera.viewportWidth / 2 <= 0)
                    stageCamera.position.x = stageCamera.viewportWidth / 2;
                if (stageCamera.position.x + stageCamera.viewportWidth / 2 >= gameMap.getWidth())
                    stageCamera.position.x = gameMap.getWidth() - stageCamera.viewportWidth / 2;

                if (stageCamera.position.y - stageCamera.viewportHeight / 2 <= 0)
                    stageCamera.position.y = stageCamera.viewportHeight / 2;
                if (stageCamera.position.y + stageCamera.viewportHeight / 2 >= gameMap.getHeight())
                    stageCamera.position.y = gameMap.getHeight() - stageCamera.viewportHeight / 2;

                stageCamera.update();

                this.x = x + offsetX;
                this.y = y + offsetY;

            }

        });

    }

    @Override
    protected InputMultiplexer createStageMultiplexer() {
        // TODO implement zoom
        GestureDetector gesture = new GestureDetector(new GestureAdapter() {

        });
        return new InputMultiplexer(gesture, stage);
    }

    private void handleClick(final GameMap gameWorld, float x, float y) {
        // set possible open dialog invisible
//        if (buyTowerSelectionTable.isVisible()) {
//            hideBuyTowerDialog();
//            return;
//        }
//        if (towerInfoTable.isVisible()) {
//            hideTowerInfoDialog();
//            return;
//        }

        final Actor hit = stage.hit(x, y, true);

        if (hit instanceof Object) {
            // show tower info
            //showTowerInfoDialg((AbstractTower) hit);
        } else {
            // place tower
            final int tileWidth = Towerwars.GRID_SIZE;
            final int tileHeight = Towerwars.GRID_SIZE;
            final float xGrid = x - x % tileWidth;
            final float yGrid = y - y % tileHeight;
            final boolean canBuildTower = gameWorld.canBuildTower(xGrid, yGrid);

            if (canBuildTower) {

                //gameWorld.setIsBuyingTower(xGrid, yGrid);

                //showBuyTowerDialog();
            } else {
                //gameWorld.showInvalidClick((int) xGrid, (int) yGrid);
            }
        }
    }

}




