package de.diegrafen.exmatrikulatortd.view.screens;

import com.badlogic.gdx.Game;
import de.diegrafen.exmatrikulatortd.controller.MainController;
import de.diegrafen.exmatrikulatortd.persistence.GameStateDao;

/**
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 14.06.2019 04:34
 */
public class MenuScreen extends BaseScreen {

    public MenuScreen (MainController mainController, Game game) {
        super(mainController, game);
    }

    @Override
    public void init () {
        System.out.println("Dies ist der MenuScreen!");
        getMainController().createNewSinglePlayerGame();
    }

    private void createMainMenu () {

    }

    private void createSaveGameMenu () {

    }

    private void createNewSinglePlayerGameMenu () {

    }

}
