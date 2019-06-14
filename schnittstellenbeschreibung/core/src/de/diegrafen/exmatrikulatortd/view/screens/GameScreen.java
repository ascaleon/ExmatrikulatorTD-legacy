package de.diegrafen.exmatrikulatortd.view.screens;

import de.diegrafen.exmatrikulatortd.controller.GameController;
import de.diegrafen.exmatrikulatortd.controller.MultiPlayerGameController;
import de.diegrafen.exmatrikulatortd.controller.SinglePlayerGameController;
import de.diegrafen.exmatrikulatortd.model.Gamestate;

/**
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 14.06.2019 02:12
 */
public class GameScreen extends BaseScreen {

    private boolean multiPlayer;

    public GameScreen(SinglePlayerGameController singlePlayerGameController, Gamestate gameState) {
        super(singlePlayerGameController, gameState);
        this.multiPlayer = false;
    }

    public GameScreen(MultiPlayerGameController multiPlayerGameController, Gamestate gameState) {
        super(multiPlayerGameController, gameState);
        this.multiPlayer = true;
    }



}
