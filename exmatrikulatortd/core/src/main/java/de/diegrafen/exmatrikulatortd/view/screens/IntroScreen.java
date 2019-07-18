package de.diegrafen.exmatrikulatortd.view.screens;

import com.badlogic.gdx.assets.AssetManager;
import de.diegrafen.exmatrikulatortd.controller.MainController;

/**
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 15.06.2019 12:06
 */
public class IntroScreen extends BaseScreen {

    public IntroScreen (MainController mainController, AssetManager assetManager) {
        super(mainController, assetManager);
    }

    @Override
    public void init () {
        System.out.println("Dies ist der IntroScreen!");
    }

    private void showIntro () {

    }

    /**
     * Wird immer nach einem Bestimmten Zeitabstand aufgerufen und die Logik des Spiels berechnet, damit danach in render() neu gezeichnet werden kann.
     *
     * @param deltaTime Die Zeit in Sekunden seit dem letzten Frame.
     */
    @Override
    public void update(float deltaTime) {
        getMainController().showMenuScreen();
    }
}
