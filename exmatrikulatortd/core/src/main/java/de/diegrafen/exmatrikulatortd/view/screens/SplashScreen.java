package de.diegrafen.exmatrikulatortd.view.screens;

import com.badlogic.gdx.assets.AssetManager;
import de.diegrafen.exmatrikulatortd.controller.MainController;

/**
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 15.06.2019 04:54
 */
public class SplashScreen extends BaseScreen {

    public SplashScreen (MainController mainController, AssetManager assetManager) {
        super(mainController, assetManager);
    }

    @Override
    public void init() {
        System.out.println("Dies ist der SplashScreen!");
    }

    /**
     * Called when the screen should render itself.
     *
     * @param delta The time in seconds since the last render.
     */
    @Override
    public void update(float delta) {
        if (getAssetManager().update()) {
            if (getMainController().isDatabaseLoaded()) {
                getMainController().showIntroScreen();
            } else {
                System.out.println("Warte auf Datenbank...");
            }
        } else {
            System.out.println("Current Progress: " + getAssetManager().getProgress());
        }

    }
}
