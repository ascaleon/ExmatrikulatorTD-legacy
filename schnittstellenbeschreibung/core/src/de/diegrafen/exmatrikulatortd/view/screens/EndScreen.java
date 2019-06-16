package de.diegrafen.exmatrikulatortd.view.screens;

import de.diegrafen.exmatrikulatortd.controller.MainController;
import de.diegrafen.exmatrikulatortd.controller.gamelogic.GameLogicController;
import de.diegrafen.exmatrikulatortd.model.Gamestate;

/**
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 15.06.2019 13:56
 */
public class EndScreen extends BaseScreen {

    private Gamestate gameState;

    public EndScreen (MainController mainController, Gamestate gameState) {
        super(mainController);
        this.gameState = gameState;
    }

    @Override
    public void init() {
        //System.out.println("Das hier ist der Endbildschirm!");
        //getMainController().showMenuScreen();
    }


    @Override
    public void update(float deltaTime) {

    }

    public void showEndScreenStatistics () {

    }

}
