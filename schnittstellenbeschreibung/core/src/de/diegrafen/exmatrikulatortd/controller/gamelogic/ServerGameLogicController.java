package de.diegrafen.exmatrikulatortd.controller.gamelogic;

import de.diegrafen.exmatrikulatortd.communication.server.GameServer;
import de.diegrafen.exmatrikulatortd.controller.MainController;
import de.diegrafen.exmatrikulatortd.model.Gamestate;
import de.diegrafen.exmatrikulatortd.model.Profile;

/**
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 15.06.2019 20:43
 */

public class ServerGameLogicController extends GameLogicController {
    private GameServer gameServer;

    public ServerGameLogicController(MainController mainController, Gamestate gamestate, Profile profile, GameServer gameServer) {
        super(mainController, gamestate, profile);
        this.gameServer = gameServer;
        gameServer.setLogicController(this);
    }

    public void exitGame(boolean saveBeforeExit) {
        this.gameServer.shutdown();
        super.exitGame(saveBeforeExit);
    }

    public void sendServerStateToClient () {

    }

}