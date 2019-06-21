package de.diegrafen.exmatrikulatortd.controller.gamelogic;

import de.diegrafen.exmatrikulatortd.communication.client.GameClient;
import de.diegrafen.exmatrikulatortd.communication.server.GameServer;
import de.diegrafen.exmatrikulatortd.controller.MainController;
import de.diegrafen.exmatrikulatortd.model.Gamestate;
import de.diegrafen.exmatrikulatortd.model.Profile;

/**
 *
 * Spiellogik-Controller für Multiplayer-Spiele als Server
 *
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 15.06.2019 20:43
 */

public class ServerGameLogicController extends GameLogicController {

    /**
     * Der GameClient, über den die Netzwerkkommuikation abläuft
     */
    private GameServer gameServer;

    /**
     * Konstruktor für den Spiellogik-Controller
     * @param mainController Der Haupt-Controller der Anwendung
     * @param gamestate Der Spielzustand, mit dem der Controller initialisiert wird
     * @param profile Das Spieler-Profil
     * @param gameServer Der GameClient, über den die Netzwerkkommunikation abläuft
     */
    public ServerGameLogicController(MainController mainController, Gamestate gamestate, Profile profile, GameServer gameServer) {
        super(mainController, gamestate, profile);
        this.gameServer = gameServer;
        gameServer.setLogicController(this);
    }

    /**
     * Beendet das Spiel
     *
     * @param saveBeforeExit Gibt an, ob das Spiel vorher gespeichert werden soll
     */
    public void exitGame(boolean saveBeforeExit) {
        this.gameServer.shutdown();
        super.exitGame(saveBeforeExit);
    }

    /**
     * Sendet den aktuellen Spielzustand zum Client
     */
    public void sendServerStateToClient () {

    }

}