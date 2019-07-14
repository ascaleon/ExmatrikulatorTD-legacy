package de.diegrafen.exmatrikulatortd.controller.gamelogic;

import com.badlogic.gdx.Game;
import de.diegrafen.exmatrikulatortd.communication.client.GameClient;
import de.diegrafen.exmatrikulatortd.communication.server.GameServer;
import de.diegrafen.exmatrikulatortd.controller.MainController;
import de.diegrafen.exmatrikulatortd.controller.factories.EnemyFactory;
import de.diegrafen.exmatrikulatortd.controller.factories.TowerFactory;
import de.diegrafen.exmatrikulatortd.model.Gamestate;
import de.diegrafen.exmatrikulatortd.model.Profile;
import de.diegrafen.exmatrikulatortd.view.screens.GameView;

/**
 * Spiellogik-Controller für Multiplayer-Spiele als Server
 *
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 15.06.2019 20:43
 * @deprecated
 */

public class ServerGameLogicController extends GameLogicController {

    /**
     * Der GameClient, über den die Netzwerkkommuikation abläuft
     */
    private GameServer gameServer;

    /**
     * Konstruktor für den Spiellogik-Controller
     *
     * @param mainController Der Haupt-Controller der Anwendung
     * @param profile        Das Spieler-Profil
     * @param gameServer     Der GameClient, über den die Netzwerkkommunikation abläuft
     */
    public ServerGameLogicController(MainController mainController, Profile profile, int numberOfPlayers, int localPlayerNumber,
                                     int gamemode, GameView gameView, String mapPath, GameServer gameServer) {
        super(mainController, profile, numberOfPlayers, localPlayerNumber, gamemode, gameView, mapPath);
        this.gameServer = gameServer;
        gameServer.attachRequestListeners(this);
    }

    /**
     * Baut einen neuen Turm
     *
     * @param towerType    Der Typ des zu bauenden Turms
     * @param xPosition    Die x-Koordinate des Turms
     * @param yPosition    Die y-Koordinate des Turms
     * @param playerNumber Die Nummer der Spielerin, für die der Turm gebaut werden soll
     */
    @Override
    public void buildTower(int towerType, int xPosition, int yPosition, int playerNumber) {
        super.buildTower(towerType, xPosition, yPosition, playerNumber);

        super.buildTower(towerType,xPosition,yPosition,playerNumber);

        gameServer.buildTower(towerType, xPosition, yPosition, playerNumber);

    }

    @Override
    public void sellTower(int xCoordinate, int yCoordinate, int playerNumber) {
        // TODO: Methode nach Muster von buildTower implementieren
        super.sellTower(xCoordinate, yCoordinate, playerNumber);
    }

    /**
     * Rüstet einen Turm auf
     *
     */
    @Override
    public void upgradeTower(int xPosition, int yPosition, int playerNumber) {
        // TODO: Methode nach Muster von buildTower implementieren
        super.upgradeTower(xPosition, yPosition, playerNumber);
    }

    /**
     * Schickt einen Gegner zum gegnerischen Spieler
     *
     * @param enemyType            Der Typ des zu schickenden Gegners
     */
    @Override
    public void sendEnemy(int enemyType, int playerToSendToNumber, int sendingPlayerNumber) {
        super.sendEnemy(enemyType, playerToSendToNumber, sendingPlayerNumber);
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
    public void sendServerStateToClient() {

    }
}