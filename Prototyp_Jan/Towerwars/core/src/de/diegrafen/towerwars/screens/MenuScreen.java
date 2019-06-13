package de.diegrafen.towerwars.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import de.diegrafen.towerwars.Assets;
import de.diegrafen.towerwars.Towerwars;
import de.diegrafen.towerwars.gameworld.GameMap;
import de.diegrafen.towerwars.persistence.PlayerDao;

//import static javax.swing.text.html.HTML.Tag.TD;

public class MenuScreen extends AbstractScreen {

    private int currentDifficulty = 0;

    //private BackgroundLevel backgroundLevel;

    Towerwars game;
    private PlayScreen gameScreen;
    private Table mainTable;
    private Stack menuStack;

    private Button newGameButton;
    private Button exitButton;
    private Button highscoreButton;
    private Button loadButton;
    private Label versionLabel;
    private Button creditsButton;
    private Button donateButton;

    private Table difficultySelectionTable;
    private Label difficultyLabel;
    private Label activeGameLabelWarningDifficulty;
    private Label activeGameLabelWarningLevel;

    private Table levelSelectionTable;
    private Label gameTitleLabel;
    private Label levelDifficultyLabel;
    private Label difficultyPointGainLabel;
    private Label levelModeLabel;
    private LabelStyle levelDifficultyStyle;

    private Table highscoreTable;
    //private AchievementListWidget achievementsListWidget;

    private Table creditsTable;

    private Table donationTable;

    private Label levelOfTheDayLabel;
    private int levelIndex = 0;
    //private Level[] levels;
    private Label levelLabel;

    private PlayerDao playerDao;

    public MenuScreen(final Towerwars game) {

        this.game = game;



        menuStack = new Stack();
        menuStack.setFillParent(true);

        mainTable = new Table();
        mainTable.setFillParent(true);

        menuStack.add(mainTable);

        System.out.println("Test1");

        // main menu
        {
            final LabelStyle gameTitleLabelStyle = new LabelStyle();
            gameTitleLabelStyle.font = Assets.TITLE_FONT_64;
            gameTitleLabelStyle.fontColor = Color.BLACK;
            gameTitleLabelStyle.background = new TextureRegionDrawable(Assets.getTexture(Assets.BACKGROUND));


            gameTitleLabel = new Label("Towerdefense", gameTitleLabelStyle);
            gameTitleLabel.setAlignment(Align.center);

            final TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
            style.font = Assets.TITLE_FONT_38;
            style.overFontColor = Color.WHITE;
            style.downFontColor = Color.WHITE;
            style.fontColor = Color.BLACK;
            style.up = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("background.png"))));

            final ButtonStyle loadButtonPressed = new ButtonStyle();
            loadButtonPressed.up = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("sprites/hud/continuebutton.png"))));
            loadButtonPressed.down = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("sprites/hud/continuebuttonpressed.png"))));

            loadButton = new Button(loadButtonPressed);
            loadButton.addListener(new ClickListener() {
                @Override
                public void clicked(final InputEvent event, final float x, final float y) {
                    gameScreen.setPaused(true);
                    game.setScreen(gameScreen);
                }
            });

            final ButtonStyle newGameButtonStyle = new ButtonStyle();
            newGameButtonStyle.up = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("sprites/hud/newgamebutton.png"))));
            newGameButtonStyle.down = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("sprites/hud/newgamebuttonpressed.png"))));

            newGameButton = new Button(newGameButtonStyle);
            newGameButton.addListener(new ClickListener() {
                @Override
                public void clicked(final InputEvent event, final float x, final float y) {
                    game.setScreen(gameScreen);

                    //showDifficultySelection();
                }
            });

            final ButtonStyle highscoreButtonStyle = new ButtonStyle();
            highscoreButtonStyle.up = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("sprites/hud/achievementsbutton.png"))));
            highscoreButtonStyle.down = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("sprites/hud/achievementsbuttonpressed.png"))));

            highscoreButton = new Button(highscoreButtonStyle);
            highscoreButton.addListener(new ClickListener() {
                @Override
                public void clicked(final InputEvent event, final float x, final float y) {
                    highscoreTable.setVisible(true);
                    //achievementsListWidget.update();
                    setMainMenuVisible(false);
                    //TD.tracking.sendScreen("AchievementsScreen");
                }
            });

            final ButtonStyle exitButtonStyle = new ButtonStyle();
            exitButtonStyle.up = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("sprites/hud/exitbutton.png"))));
            exitButtonStyle.down = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("sprites/hud/exitbuttonpressed.png"))));

            exitButton = new Button(exitButtonStyle);
            exitButton.addListener(new ClickListener() {
                @Override
                public void clicked(final InputEvent event, final float x, final float y) {
                    Gdx.app.exit();
                }
            });

            final LabelStyle versionLabelStyle = new LabelStyle();
            versionLabelStyle.font = Assets.TEXT_FONT;
            versionLabelStyle.fontColor = Color.BLACK;

            //versionLabel = new Label("Version " + TD.appVersion, versionLabelStyle);

            final ButtonStyle creditsButtonStyle = new ButtonStyle();
            creditsButtonStyle.up = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("sprites/hud/creditsbutton.png"))));
            creditsButtonStyle.down = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("sprites/hud/creditsbuttonpressed.png"))));

            creditsButton = new Button(creditsButtonStyle);
            creditsButton.addListener(new ClickListener() {
                @Override
                public void clicked(final InputEvent event, final float x, final float y) {
                    creditsTable.setVisible(true);
                    setMainMenuVisible(false);
                    //TD.tracking.sendScreen("CreditsScreen");

                }
            });

            final ButtonStyle donateButtonStyle = new ButtonStyle();
            donateButtonStyle.up = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("sprites/hud/donatebutton.png"))));
            donateButtonStyle.down = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("sprites/hud/donatebuttonpressed.png"))));
            donateButton = new Button(donateButtonStyle);
            donateButton.addListener(new ClickListener() {
                @Override
                public void clicked(final InputEvent event, final float x, final float y) {
                    donationTable.setVisible(true);
                    setMainMenuVisible(false);
                    //TD.tracking.sendScreen("DonateScreen");
                }
            });

        }

        // highscores
        addHighscoreTable(menuStack);

        // difficulty selection
        {
            difficultySelectionTable = new Table();
            difficultySelectionTable.setFillParent(true);
            difficultySelectionTable.setBackground(new TextureRegionDrawable(Assets.getTexture(Assets.BACKGROUND)));
            difficultySelectionTable.setVisible(false);

            // Title
            final LabelStyle difficultyTitleLabelStyle = new LabelStyle();
            difficultyTitleLabelStyle.fontColor = Color.BLACK;
            difficultyTitleLabelStyle.font = Assets.TITLE_FONT_64;
            final Label difficultyTitleLabel = new Label("Difficulty", difficultyTitleLabelStyle);

            difficultySelectionTable.add(difficultyTitleLabel).top().expandX().colspan(2).spaceBottom(5);
            difficultySelectionTable.row();

            // difficulty selection with tooltip
            {
                final Table difficultyTable = new Table();

                final LabelStyle difficultyTooltipStyle = new LabelStyle();
                difficultyTooltipStyle.fontColor = Color.BLACK;
                difficultyTooltipStyle.font = Assets.TEXT_FONT;
                final Label difficultyTooltipLabel = new Label("Increased difficulty will increase enemy health, but also increases the amount of points you gain.",
                        difficultyTooltipStyle);
                difficultyTooltipLabel.setWrap(true);
                difficultyTooltipLabel.setAlignment(Align.center);

                difficultyTable.add(difficultyTooltipLabel).expandX().center().minWidth(Value.percentWidth(0.9f));
                difficultyTable.row();

                final Table difficultyChooserTable = new Table();
                final ButtonStyle decreaseDifficultyStyle = new ButtonStyle();
                decreaseDifficultyStyle.up = new TextureRegionDrawable(Assets.getTexture(Assets.DECREASE_BUTTON));
                decreaseDifficultyStyle.down = new TextureRegionDrawable(Assets.getTexture(Assets.DECREASE_BUTTON_PRESSED));

                //final Button decreaseDifficultyButton = new HoldableButton(decreaseDifficultyStyle);
                final Button decreaseDifficultyButton = new Button(decreaseDifficultyStyle);// new HoldableButton(decreaseDifficultyStyle);
                decreaseDifficultyButton.addListener(new ChangeListener() {

                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        currentDifficulty--;
                        currentDifficulty = Math.max(currentDifficulty, 0);
                        difficultyLabel.setText(String.valueOf(currentDifficulty) + "/100");
                    }

                });
                difficultyChooserTable.add(decreaseDifficultyButton).padRight(5);

                final LabelStyle difficultyLabelStyle = new LabelStyle();
                difficultyLabelStyle.fontColor = Color.BLACK;
                difficultyLabelStyle.font = Assets.TEXT_FONT;
                difficultyLabel = new Label(String.valueOf(currentDifficulty) + "/100", difficultyLabelStyle);
                difficultyLabel.setAlignment(Align.center);
                difficultyChooserTable.add(difficultyLabel).minWidth(75).top().padTop(4);

                final ButtonStyle increaseDifficultyStyle = new ButtonStyle();
                increaseDifficultyStyle.up = new TextureRegionDrawable(Assets.getTexture(Assets.INCREASE_BUTTON));
                increaseDifficultyStyle.down = new TextureRegionDrawable(Assets.getTexture(Assets.INCREASE_BUTTON_PRESSED));

                //final Button increaseDifficultyButton = new HoldableButton(increaseDifficultyStyle);
                final Button increaseDifficultyButton = new Button(increaseDifficultyStyle); //new HoldableButton(increaseDifficultyStyle);
                increaseDifficultyButton.addListener(new ChangeListener() {

                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        currentDifficulty++;
                        currentDifficulty = Math.min(currentDifficulty, 100);
                        difficultyLabel.setText(String.valueOf(currentDifficulty) + "/100");
                    }

                });
                difficultyChooserTable.add(increaseDifficultyButton).padLeft(5);
                difficultyTable.add(difficultyChooserTable);

                difficultySelectionTable.add(difficultyTable).expandX().center().colspan(2).minWidth(Value.percentWidth(1.0f));
                difficultySelectionTable.row();
            }

            final LabelStyle activeGameWarningStyle = new LabelStyle();
            activeGameWarningStyle.fontColor = Assets.COLOR_RED;
            activeGameWarningStyle.font = Assets.TEXT_FONT;
            activeGameLabelWarningDifficulty = new Label("current game will be discarded if you start a new game!", activeGameWarningStyle);
            activeGameLabelWarningDifficulty.setWrap(true);
            activeGameLabelWarningDifficulty.setAlignment(Align.center);

            difficultySelectionTable.add(activeGameLabelWarningDifficulty).expand().bottom().minWidth(Value.percentWidth(0.95f)).colspan(2).spaceBottom(5);
            difficultySelectionTable.row();

            final ButtonStyle backButtonStyle = new ButtonStyle();
            backButtonStyle.up = new TextureRegionDrawable(Assets.getTexture(Assets.BACK_BUTTON));
            backButtonStyle.down = new TextureRegionDrawable(Assets.getTexture(Assets.BACK_BUTTON_PRESSED));

            final Button backButton = new Button(backButtonStyle);
            backButton.addListener(new ChangeListener() {

                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    showMenu();

                    //persistDifficulty();
                }

            });

            difficultySelectionTable.add(backButton).expandX().bottom().left();

            final ButtonStyle selecttLevelButtonStyle = new ButtonStyle();
            selecttLevelButtonStyle.up = new TextureRegionDrawable(new Texture(Gdx.files.internal("sprites/hud/selectlevelbutton.png")));
            selecttLevelButtonStyle.down = new TextureRegionDrawable(new Texture(Gdx.files.internal("sprites/hud/selectlevelbuttonpressed.png")));

            final Button selectLevelButton = new Button(selecttLevelButtonStyle);
            selectLevelButton.addListener(new ChangeListener() {

                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    //showLevelSelection();

                    //persistDifficulty();
                }
            });

            difficultySelectionTable.add(selectLevelButton).bottom().right();

            menuStack.add(difficultySelectionTable);

        }

        // level selection
        {
            levelSelectionTable = new Table();
            levelSelectionTable.setFillParent(true);
            levelSelectionTable.setBackground(new TextureRegionDrawable(Assets.getTexture(Assets.BACKGROUND)));
            levelSelectionTable.setVisible(false);

            final LabelStyle selectLevelStyle = new LabelStyle();
            selectLevelStyle.fontColor = Color.BLACK;
            selectLevelStyle.font = Assets.TITLE_FONT_64;
            final Label selectLevelLabel = new Label("Select Level", selectLevelStyle);

            levelSelectionTable.add(selectLevelLabel).top().expandX().colspan(2);
            levelSelectionTable.row();

            {
                final Table difficultyChooserTable = new Table();
                final ButtonStyle previousLevelButtonStyle = new ButtonStyle();
                previousLevelButtonStyle.up = new TextureRegionDrawable(Assets.getTexture(Assets.DECREASE_BUTTON));
                previousLevelButtonStyle.down = new TextureRegionDrawable(Assets.getTexture(Assets.DECREASE_BUTTON_PRESSED));

                final Button previousLevelButton = new Button(previousLevelButtonStyle);
                previousLevelButton.addListener(new ChangeListener() {

                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        //levelIndex--;
                        //if (levelIndex < 0) {
                            //levelIndex = levels.length - 1;
                        //}

                        //updateBackgroundLevel();
                    }

                });
                difficultyChooserTable.add(previousLevelButton).padRight(5);

                final LabelStyle levelLabelStyle = new LabelStyle();
                levelLabelStyle.fontColor = Color.BLACK;
                levelLabelStyle.font = Assets.TEXT_FONT;
                //levelLabel = new Label(levels[0].getName(), levelLabelStyle);
                levelLabel = new Label("Blah.", levelLabelStyle);
                levelLabel.setAlignment(Align.center);
                difficultyChooserTable.add(levelLabel).minWidth(160).top().padTop(4);

                final ButtonStyle nextLevelButtonStyle = new ButtonStyle();
                nextLevelButtonStyle.up = new TextureRegionDrawable(Assets.getTexture(Assets.INCREASE_BUTTON));
                nextLevelButtonStyle.down = new TextureRegionDrawable(Assets.getTexture(Assets.INCREASE_BUTTON_PRESSED));

                final Button nextLevelButton = new Button(nextLevelButtonStyle);
                nextLevelButton.addListener(new ChangeListener() {

                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
//                        levelIndex++;
//                        if (levelIndex == levels.length) {
//                            levelIndex = 0;
//                        }

                        //updateBackgroundLevel();
                    }

                });
                difficultyChooserTable.add(nextLevelButton).padLeft(5);

                levelSelectionTable.add(difficultyChooserTable).expandX().center().colspan(2).minWidth(Value.percentWidth(1.0f)).spaceTop(5f);
                levelSelectionTable.row();
            }

            // difficulty indicator
            final Table levelDifficultyTable = new Table();

            final LabelStyle defaultTextStyle = new LabelStyle();
            defaultTextStyle.fontColor = Color.BLACK;
            defaultTextStyle.font = Assets.TEXT_FONT;

            levelDifficultyStyle = new LabelStyle();
            levelDifficultyStyle.fontColor = Assets.COLOR_YELLOW;
            levelDifficultyStyle.font = Assets.TEXT_FONT;

            levelDifficultyTable.add(new Label("Difficulty: ", defaultTextStyle));
            levelDifficultyLabel = new Label("Medium", levelDifficultyStyle);
            levelDifficultyTable.add(levelDifficultyLabel);
            difficultyPointGainLabel = new Label(" (+0% Points gain)", defaultTextStyle);
            levelDifficultyTable.add(difficultyPointGainLabel);

            levelSelectionTable.add(levelDifficultyTable).center().top().expandX().colspan(2).spaceTop(5f);
            levelSelectionTable.row();

            final Table modeTable = new Table();

            final LabelStyle modeTextStyle = new LabelStyle();
            modeTextStyle.fontColor = Assets.COLOR_YELLOW;
            modeTextStyle.font = Assets.TEXT_FONT;
            levelModeLabel = new Label("XXX", modeTextStyle);

            modeTable.add(new Label("Mode: ", defaultTextStyle)).center();
            modeTable.add(levelModeLabel).center();

            levelSelectionTable.add(modeTable).center().top().expandX().colspan(2).spaceTop(5f);
            levelSelectionTable.row();

            // level of the day
            final LabelStyle levelOfTheDayStyle = new LabelStyle();
            levelOfTheDayStyle.fontColor = Assets.COLOR_GREEN;
            levelOfTheDayStyle.font = Assets.TEXT_FONT;

            levelOfTheDayLabel = new Label("Level of the Day!", levelOfTheDayStyle);
            levelSelectionTable.add(levelOfTheDayLabel).center().top().expandX().colspan(2).spaceTop(5f);
            levelSelectionTable.row();

            // back button
            final ButtonStyle backButtonStyle = new ButtonStyle();
            backButtonStyle.up = new TextureRegionDrawable(Assets.getTexture(Assets.BACK_BUTTON));
            backButtonStyle.down = new TextureRegionDrawable(Assets.getTexture(Assets.BACK_BUTTON_PRESSED));

            final Button backButton = new Button(backButtonStyle);
            backButton.addListener(new ChangeListener() {

                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    //showDifficultySelection();
                }
            });

            final LabelStyle activeGameWarningStyle = new LabelStyle();
            activeGameWarningStyle.fontColor = Assets.COLOR_RED;
            activeGameWarningStyle.font = Assets.TEXT_FONT;

            activeGameLabelWarningLevel = new Label("Current game will be discarded if you start a new game!", activeGameWarningStyle);
            activeGameLabelWarningLevel.setWrap(true);
            activeGameLabelWarningLevel.setAlignment(Align.center);

            levelSelectionTable.add(activeGameLabelWarningLevel).expand().bottom().minWidth(Value.percentWidth(0.95f)).colspan(2).spaceBottom(5);
            levelSelectionTable.row();

            levelSelectionTable.add(backButton).expandX().bottom().left();

            final ButtonStyle startButtonStyle = new ButtonStyle();
            startButtonStyle.up = new TextureRegionDrawable(new Texture(Gdx.files.internal("sprites/hud/startbutton.png")));
            startButtonStyle.down = new TextureRegionDrawable(new Texture(Gdx.files.internal("sprites/hud/startbuttonpressed.png")));
            final Button startButton = new Button(startButtonStyle);
            startButton.addListener(new ChangeListener() {

                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    showMenu();

//                    final Level level = levels[levelIndex];
//
//                    final GameWorld abstractWorld = level.createWorld();
//                    abstractWorld.setWaveSpawner(new WaveSpawner(abstractWorld, 5, 100, currentDifficulty));
//                    abstractWorld.setDifficulty(currentDifficulty);
//                    abstractWorld.setIsMapOfTheDay(level.isMapOfTheDay());
//                    abstractWorld.setLevelDifficulyt(level.getDifficulty());

                    // dispose old resources
                    if (gameScreen != null) {
                        gameScreen.dispose();
                    }

                    //gameScreen = new PlayScreen(game, abstractWorld);

                    game.setScreen(gameScreen);

                }
            });

            levelSelectionTable.add(startButton).bottom().right();

            menuStack.add(levelSelectionTable);

            //updateBackgroundLevel();
        }

        // credits screen
        {
            creditsTable = new Table();
            creditsTable.setFillParent(true);
            creditsTable.setBackground(new TextureRegionDrawable(Assets.getTexture(Assets.BACKGROUND)));
            creditsTable.setVisible(false);

            final LabelStyle creditsTitleStyle = new LabelStyle();
            creditsTitleStyle.fontColor = Color.BLACK;
            creditsTitleStyle.font = Assets.TITLE_FONT_64;

            final Label titleLabel = new Label("Credits", creditsTitleStyle);
            titleLabel.setAlignment(Align.center);

            creditsTable.add(titleLabel).expandX().center().top();
            creditsTable.row();

            final LabelStyle textStyle = new LabelStyle();
            textStyle.fontColor = Color.BLACK;
            textStyle.font = Assets.TEXT_FONT;
            final Label creditsLabel = new Label("\"Hard Vacuum\" art by Daniel Cook:", textStyle);
            creditsLabel.setWrap(true);
            creditsLabel.setAlignment(Align.center);
            creditsTable.add(creditsLabel).expandX().center().minWidth(Value.percentWidth(0.9f));
            creditsTable.row();

            // link to lostgarden
            final ButtonStyle lostGardenButtonLinkStyle = new ButtonStyle();
            lostGardenButtonLinkStyle.up = new TextureRegionDrawable(new Texture(Gdx.files.internal("sprites/hud/lostgardenlink.png")));
            lostGardenButtonLinkStyle.down = new TextureRegionDrawable(new Texture(Gdx.files.internal("sprites/hud/lostgardenlinkpressed.png")));

            final Button lostGardenLinkButton = new Button(lostGardenButtonLinkStyle);
            lostGardenLinkButton.addListener(new ClickListener() {
                @Override
                public void clicked(final InputEvent event, final float x, final float y) {
                    Gdx.net.openURI("http://www.lostgarden.com/");
                }
            });

            creditsTable.add(lostGardenLinkButton).expandX().center();
            creditsTable.row();

            final Label programmingLabel = new Label("My Website:", textStyle);
            programmingLabel.setWrap(true);
            programmingLabel.setAlignment(Align.center);
            creditsTable.add(programmingLabel).expandX().center().minWidth(Value.percentWidth(0.9f)).spaceTop(20);
            creditsTable.row();

            // link to my website
            final ButtonStyle heavydefenseLinkStyle = new ButtonStyle();
            heavydefenseLinkStyle.up = new TextureRegionDrawable(new Texture(Gdx.files.internal("sprites/hud/websitelink.png")));
            heavydefenseLinkStyle.down = new TextureRegionDrawable(new Texture(Gdx.files.internal("sprites/hud/websitelinkpressed.png")));

            final Button heavyDefenseLinkButton = new Button(heavydefenseLinkStyle);
            heavyDefenseLinkButton.addListener(new ClickListener() {
                @Override
                public void clicked(final InputEvent event, final float x, final float y) {
                    Gdx.net.openURI("http://www.benjaminneukom.net");
                }
            });

            creditsTable.add(heavyDefenseLinkButton).expandX().center();
            creditsTable.row();

            final ButtonStyle backButtonStyle = new ButtonStyle();
            backButtonStyle.up = new TextureRegionDrawable(Assets.getTexture(Assets.BACK_BUTTON));
            backButtonStyle.down = new TextureRegionDrawable(Assets.getTexture(Assets.BACK_BUTTON_PRESSED));

            final Button backButton = new Button(backButtonStyle);
            backButton.addListener(new ClickListener() {
                @Override
                public void clicked(final InputEvent event, final float x, final float y) {
                    showMenu();
                }
            });

            creditsTable.add(backButton).expand().bottom().left();

            menuStack.add(creditsTable);
        }

        // donation screen
        {
            donationTable = new Table();
            donationTable.setFillParent(true);
            donationTable.setBackground(new TextureRegionDrawable(Assets.getTexture(Assets.BACKGROUND)));
            donationTable.setVisible(false);

            if (true) { //(TD.billing.isAvailable()) {
                final LabelStyle descriptionLabelStyle = new LabelStyle();
                descriptionLabelStyle.fontColor = Color.BLACK;
                descriptionLabelStyle.font = Assets.TEXT_FONT;

                final Label descriptionLabel = new Label(
                        "Thank you for considering a donation. If you like the game and want to support me please choose one of the given donations.",
                        descriptionLabelStyle);
                descriptionLabel.setWrap(true);
                descriptionLabel.setAlignment(Align.center);

                donationTable.add(descriptionLabel).expandX().center().top().minWidth(Value.percentWidth(1f)).colspan(2);
                donationTable.row();

                // small
                {
                    final ButtonStyle smallDonationStyle = new ButtonStyle();
                    smallDonationStyle.up = new TextureRegionDrawable(new TextureRegionDrawable(new Texture(Gdx.files.internal("sprites/hud/smalldonationbutton.png"))));
                    smallDonationStyle.down = new TextureRegionDrawable(new TextureRegionDrawable(new Texture(Gdx.files.internal("sprites/hud/smalldonationbuttonpressed.png"))));

                    final Button smallDonationButton = new Button(smallDonationStyle);
                    smallDonationButton.addListener(new ClickListener() {
                        @Override
                        public void clicked(final InputEvent event, final float x, final float y) {
                            //TD.billing.initiateDonate(Billing.DONATE_SMALL);
                        }
                    });

                    final Table smallDonationTable = new Table();
                    smallDonationTable.add(smallDonationButton).center();
                    smallDonationTable.add(new Label(" Of 2$.", descriptionLabelStyle)).left();

                    donationTable.add(smallDonationTable).expandX().center().spaceTop(20);
                    donationTable.row();
                }

                // medium
                {
                    final ButtonStyle mediumDonationStyle = new ButtonStyle();
                    mediumDonationStyle.up = new TextureRegionDrawable(new TextureRegionDrawable(new Texture(Gdx.files.internal("sprites/hud/mediumdonationbutton.png"))));
                    mediumDonationStyle.down = new TextureRegionDrawable(new TextureRegionDrawable(new Texture(Gdx.files.internal("sprites/hud/mediumdonationbuttonpressed.png"))));

                    final Button mediumDonationButton = new Button(mediumDonationStyle);
                    mediumDonationButton.addListener(new ClickListener() {
                        @Override
                        public void clicked(final InputEvent event, final float x, final float y) {
                            //TD.billing.initiateDonate(Billing.DONATE_MEDIUM);
                        }
                    });

                    final Table mediumDonationTable = new Table();
                    mediumDonationTable.add(mediumDonationButton).center();
                    mediumDonationTable.add(new Label(" Of 5$.", descriptionLabelStyle)).left();

                    donationTable.add(mediumDonationTable).expandX().center().spaceTop(10);
                    donationTable.row();
                }

                // big
                {
                    final ButtonStyle bigDonationStyle = new ButtonStyle();
                    bigDonationStyle.up = new TextureRegionDrawable(new TextureRegionDrawable(new Texture(Gdx.files.internal("sprites/hud/bigdonationbutton.png"))));
                    bigDonationStyle.down = new TextureRegionDrawable(new TextureRegionDrawable(new Texture(Gdx.files.internal("sprites/hud/bigdonationbuttonpressed.png"))));

                    final Button bigDonationButton = new Button(bigDonationStyle);
                    bigDonationButton.addListener(new ClickListener() {
                        @Override
                        public void clicked(final InputEvent event, final float x, final float y) {
                            //TD.billing.initiateDonate(Billing.DONATE_BIG);
                        }
                    });

                    final Table bigDonationTable = new Table();
                    bigDonationTable.add(bigDonationButton).center();
                    bigDonationTable.add(new Label(" Of 10$.", descriptionLabelStyle)).left();

                    donationTable.add(bigDonationTable).expandX().center().spaceTop(10);
                    donationTable.row();

                }
            } else {
                // billing not running (probably old google play version)
                final LabelStyle messageLabelStyle = new LabelStyle();
                messageLabelStyle.fontColor = Assets.COLOR_RED;
                messageLabelStyle.font = Assets.TEXT_FONT;

                final Label messageLabel = new Label(
                        "It seems In-App donations don't work on your device :( Please visit my website for a Paypal donation if you'd like:",
                        messageLabelStyle);
                messageLabel.setWrap(true);
                messageLabel.setAlignment(Align.center);

                donationTable.add(messageLabel).expand().bottom().minWidth(Value.percentWidth(1f)).colspan(2);
                donationTable.row();

                // link to my website
                final ButtonStyle heavydefenseLinkStyle = new ButtonStyle();
                heavydefenseLinkStyle.up = new TextureRegionDrawable(new Texture(Gdx.files.internal("sprites/hud/websitelink.png")));
                heavydefenseLinkStyle.down = new TextureRegionDrawable(new Texture(Gdx.files.internal("sprites/hud/websitelinkpressed.png")));

                final Button heavyDefenseLinkButton = new Button(heavydefenseLinkStyle);
                heavyDefenseLinkButton.addListener(new ClickListener() {
                    @Override
                    public void clicked(final InputEvent event, final float x, final float y) {
                        Gdx.net.openURI("http://benjaminneukom.net/?page_id=88");
                    }
                });

                donationTable.add(heavyDefenseLinkButton).expandX().center();
                donationTable.row();

            }

            final ButtonStyle backButtonStyle = new ButtonStyle();
            backButtonStyle.up = new TextureRegionDrawable(Assets.getTexture(Assets.BACK_BUTTON));
            backButtonStyle.down = new TextureRegionDrawable(Assets.getTexture(Assets.BACK_BUTTON_PRESSED));

            final Button backButton = new Button(backButtonStyle);
            backButton.addListener(new ClickListener() {
                @Override
                public void clicked(final InputEvent event, final float x, final float y) {
                    showMenu();
                }
            });

            donationTable.add(backButton).expand().bottom().left();

            //menuStack.add(donationTable);
        }

        ui.addActor(menuStack);


    }



    private void showMenu() {
        System.out.println("Test!!1");
        //TD.tracking.sendScreen("MainMenuScreen");
        difficultySelectionTable.setVisible(false);
        levelSelectionTable.setVisible(false);
        creditsTable.setVisible(false);
        highscoreTable.setVisible(false);
        donationTable.setVisible(false);

        //versionLabel.setVisible(true);
        creditsButton.setVisible(true);
        setMainMenuVisible(true);
        System.out.println("Test!!!!!1121");
    }

    private void setMainMenuVisible(boolean visible) {
        System.out.println("Test!!1");
        gameTitleLabel.setVisible(visible);
        loadButton.setVisible(visible);
        newGameButton.setVisible(visible);
        highscoreButton.setVisible(visible);
        exitButton.setVisible(visible);
        creditsButton.setVisible(visible);
        donateButton.setVisible(visible);
        System.out.println("Test!!!!!1121");
    }

    public void validateGameScreen() {
        System.out.println("Validating game...");
        if (gameScreen != null && gameScreen.gameHasEnded()) {
            gameScreen = null;
        }
    }

    @Override
    public void show() {
        super.show();

        showMenu();

        //playerDao = new PlayerDao();

        //List<Profile> playerList = playerDao.findAll();

        mainTable.clear();
        mainTable.setFillParent(true);

        final Table menuTable = new Table();

        if (gameScreen != null) {
            menuTable.add(loadButton).spaceBottom(15).expandX();
            menuTable.row();
        } else {

            gameScreen = new PlayScreen(game, new GameMap());
            /*try {
                final GameWorld world = laodWorld();

                if (world != null) {
                    world.rebuildScene();



                    gameScreen = new GameScreen(towerdefenseGame, world);

                    menuTable.add(loadButton).spaceBottom(15).expandX();
                    menuTable.row();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }*/
        }
        menuTable.add(newGameButton).spaceBottom(15).expandX();
        menuTable.row();
        menuTable.add(highscoreButton).spaceBottom(15).expandX();
        menuTable.row();
        menuTable.add(exitButton).expandX();

        mainTable.add(menuTable).expand().colspan(3);
        mainTable.row();

        mainTable.add(creditsButton).bottom().left();
        mainTable.add(donateButton).bottom().left().expandX();
        mainTable.add(versionLabel).bottom();

    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);

        //backgroundLevel.applyCamera(stageCamera);
    }


    private void addHighscoreTable (Stack menuStack) {

            highscoreTable = new Table();
            highscoreTable.setFillParent(true);
            highscoreTable.setBackground(new TextureRegionDrawable(Assets.getTexture(Assets.BACKGROUND)));
            highscoreTable.setVisible(false);

            final LabelStyle scoreTextLabelStyle = new LabelStyle();
            scoreTextLabelStyle.font = Assets.TEXT_FONT;
            scoreTextLabelStyle.fontColor = Color.BLACK;

            final LabelStyle scoreLabelStyle = new LabelStyle();
            scoreLabelStyle.font = Assets.TEXT_FONT;
            scoreLabelStyle.fontColor = new Color(212f / 255f, 207f / 255f, 17f / 255f, 1);

            //final Label scoreLabel = new Label(StringUtil.numberSepeartor((int) TD.getTotalScore()), scoreLabelStyle);
            final Label scoreLabel = new Label("9001", scoreLabelStyle);

//            TD.addScoreChangedListener(new ScoreListener() {
//
//                @Override
//                public void totalScoreChanged(float newScore) {
//                    scoreLabel.setText(StringUtil.numberSepeartor((int) newScore));
//                }
//
//            });

            final Table scoreTable = new Table();

            scoreTable.add(new Label("Lifetime points: ", scoreTextLabelStyle)).left().top();
            scoreTable.add(scoreLabel).left().top();

            highscoreTable.add(scoreTable).expandX();
            highscoreTable.row();

            final ButtonStyle backButtonStyle = new ButtonStyle();
            backButtonStyle.up = new TextureRegionDrawable(Assets.getTexture(Assets.BACK_BUTTON));
            backButtonStyle.down = new TextureRegionDrawable(Assets.getTexture(Assets.BACK_BUTTON_PRESSED));

            final Button backButton = new Button(backButtonStyle);
            backButton.addListener(new ClickListener() {
                @Override
                public void clicked(final InputEvent event, final float x, final float y) {
                    showMenu();
                    //TD.persistAchievements();
                }
            });

            //achievementsListWidget = new AchievementListWidget(TD.getAchievementCollection());

            final ScrollPane.ScrollPaneStyle scrollPaneStyle = new ScrollPane.ScrollPaneStyle();
            scrollPaneStyle.vScroll = new TextureRegionDrawable(Assets.getTexture(Assets.V_SCROLL));
            scrollPaneStyle.vScrollKnob = new TextureRegionDrawable(Assets.getTexture(Assets.V_SCROLL_KNOB));
            final ScrollPane highscoreScrollPane = new ScrollPane(new Table(), scrollPaneStyle); //new ScrollPane(achievementsListWidget, scrollPaneStyle);
            highscoreScrollPane.setFadeScrollBars(false);
            highscoreScrollPane.setScrollingDisabled(true, false);

            highscoreTable.add(highscoreScrollPane).expandX().center().minWidth(Value.percentWidth(1.0f));
            highscoreTable.row();

            highscoreTable.add(backButton).expandX().bottom().left();

            menuStack.add(highscoreTable);
    }

    private void addNewSinglePlayerGameTable (Stack menuStack) {
        
    }

    private void addNewMultiPlayerGameTable (Stack menuStack) {

    }

    private void addSelectProfileTable (Stack menuStack) {

    }

}
