package de.diegrafen.exmatrikulatortd.view.screens;

import com.badlogic.gdx.Game;
import de.diegrafen.exmatrikulatortd.controller.MainController;

/**
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 15.06.2019 04:54
 */
public class SplashScreen extends BaseScreen {

    public SplashScreen (MainController mainController, Game game) {
        super(mainController, game);
    }

    @Override
    public void init() {
        System.out.println("Dies ist der SplashScreen!");
        getMainController().loadAssets();
    }

    /**
     * Called when the screen should render itself.
     *
     * @param delta The time in seconds since the last render.
     */
    @Override
    public void update(float delta) {
        if (getMainController().areAssetsLoaded()) {
            getMainController().showMenuScreen();
        }
    }


}
