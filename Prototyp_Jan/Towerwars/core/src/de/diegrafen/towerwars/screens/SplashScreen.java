package de.diegrafen.towerwars.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import de.diegrafen.towerwars.Assets;
import de.diegrafen.towerwars.Towerwars;
import org.hibernate.SessionFactory;

import static de.diegrafen.towerwars.util.HibernateUtils.getSessionFactory;

/**
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 14.05.2019 23:55
 */
public class SplashScreen extends AbstractScreen {

    private SpriteBatch spriteBatch;
    private Sprite splash;

    private final Label loadingLabel;

    private Button continueButton;

    private CheckBox rememberCheckbox;

    public SplashScreen(final Towerwars game) {
        final Table mainTable = new Table();
        mainTable.setFillParent(true);

        final Label.LabelStyle loadingLabelStyle = new Label.LabelStyle();
        loadingLabelStyle.fontColor = Color.WHITE;
        loadingLabelStyle.font = Assets.TITLE_FONT_38;

        loadingLabel = new Label("Exmatrikulator TD", loadingLabelStyle);
        mainTable.add(loadingLabel).expand().bottom().colspan(2);
        mainTable.row();

        final Label.LabelStyle tippOfTheDayLabelStyle = new Label.LabelStyle();
        tippOfTheDayLabelStyle.fontColor = Color.WHITE;
        tippOfTheDayLabelStyle.font = Assets.TEXT_FONT;

        final Label tippOfTheDay = new Label(
                "Loading...",//Rand.selectRandom(TD.getPlayed() >= TIP_2_TRESHOLD ? TIPPS_OF_THE_DAY_2 : TIPPS_OF_THE_DAY),
                tippOfTheDayLabelStyle);
        //tippOfTheDay.setWrap(true);
        tippOfTheDay.setAlignment(Align.center);

        mainTable.add(tippOfTheDay).expandX().minWidth(Value.percentWidth(0.9f)).colspan(2);
        mainTable.row();

        final CheckBox.CheckBoxStyle checkBoxStyle = new CheckBox.CheckBoxStyle();
        checkBoxStyle.font = Assets.TEXT_FONT;
        checkBoxStyle.fontColor = Color.WHITE;
        checkBoxStyle.checkboxOn = new TextureRegionDrawable(new Texture(Gdx.files.internal("sprites/hud/checkboxchecked.png")));
        checkBoxStyle.checkboxOff = new TextureRegionDrawable(new Texture(Gdx.files.internal("sprites/hud/checkbox.png")));
        checkBoxStyle.checkboxOver = new TextureRegionDrawable(new Texture(Gdx.files.internal("sprites/hud/checkboxpressed.png")));

        rememberCheckbox = new CheckBox("Always Skip Tip", checkBoxStyle);
        final Preferences preferences = Gdx.app.getPreferences(Assets.GLOBAL_PREF_NAME);
        rememberCheckbox.setChecked(true); //preferences.contains(REMEMBER_KEY) ? preferences.getBoolean(REMEMBER_KEY) : false);
        rememberCheckbox.addListener(new ChangeListener() {

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (!rememberCheckbox.isChecked()) {
                    //game.disableSkipTipp();
                }
            }
        });

        mainTable.add(rememberCheckbox).expand().bottom().left();

        final Button.ButtonStyle continueButtonStyle = new Button.ButtonStyle();
        continueButtonStyle.up = new TextureRegionDrawable(new TextureRegion(new TextureRegion(new Texture(Gdx.files.internal("sprites/hud/continuebuttonsmall.png")))));
        continueButtonStyle.down = new TextureRegionDrawable(new TextureRegion(new TextureRegion(new Texture(Gdx.files.internal("sprites/hud/continuebuttonsmallpressed.png")))));
        continueButtonStyle.disabled = new TextureRegionDrawable(new TextureRegion(
                new TextureRegion(new Texture(Gdx.files.internal("sprites/hud/continuebuttonsmalldisabled.png")))));

        continueButton = new Button(continueButtonStyle);
        continueButton.setDisabled(true);
        continueButton.addListener(new ChangeListener() {

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("Blah8");
                game.switchToMenu();
                //persistRemember();
            }
        });

        mainTable.add(continueButton).bottom().right().padRight(3).padBottom(3);

        ui.addActor(mainTable);

        System.out.println("Blargh!");
    }

    public void setLoadingPercentage(final float percentage) {
        continueButton.setDisabled(percentage < 1);
        loadingLabel.setText("Loading (" + (int) (percentage * 100) + "%)");
    }

    /**
     * Called when this screen becomes the current screen for a {@link Game}.
     */
    //@Override
    //public void show() {
        //spriteBatch = new SpriteBatch();

        //Texture splashTexture = new Texture("background.jpg");
        //Texture splashTexture = new Texture("background.png");
        //splash = new Sprite(splashTexture);
        //splash.setSize(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());

    //}

    /**
     * Called when the screen should render itself.
     *
     * @param delta The time in seconds since the last render.
     */
    //@Override
    //public void render(float delta) {

        //Gdx.gl.glClearColor(0, 0, 0, 1);
        //Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        //spriteBatch.begin();
        //splash.draw(spriteBatch);
        //spriteBatch.end();

    //}

}
