package de.diegrafen.exmatrikulatortd.controller.gamelogic;

import de.diegrafen.exmatrikulatortd.communication.client.GameClient;
import de.diegrafen.exmatrikulatortd.communication.server.GameServer;
import de.diegrafen.exmatrikulatortd.controller.MainController;
import de.diegrafen.exmatrikulatortd.controller.factories.EnemyFactory;
import de.diegrafen.exmatrikulatortd.controller.factories.TowerFactory;
import de.diegrafen.exmatrikulatortd.model.Gamestate;
import de.diegrafen.exmatrikulatortd.model.Profile;

/**
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
     *
     * @param mainController Der Haupt-Controller der Anwendung
     * @param gamestate      Der Spielzustand, mit dem der Controller initialisiert wird
     * @param profile        Das Spieler-Profil
     * @param gameServer     Der GameClient, über den die Netzwerkkommunikation abläuft
     */
    public ServerGameLogicController(MainController mainController, Profile profile, int numberOfPlayers, int localPlayerNumber,
                                     int gamemode, GameServer gameServer) {
        super(mainController, profile, numberOfPlayers, localPlayerNumber, gamemode);
        System.out.println("Ohai?");
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
     * @return Wenn das Bauen erfolgreich war, true, ansonsten false
     */
    @Override
    public boolean buildTower(int towerType, int xPosition, int yPosition, int playerNumber) {

        boolean successful = super.buildTower(towerType, xPosition, yPosition, playerNumber);

        if (successful) {
            gameServer.buildTower(towerType, xPosition, yPosition, playerNumber);
        }

        return successful;

    }

    @Override
    public boolean sellTower(int xCoordinate, int yCoordinate, int playerNumber) {
        // TODO: Methode nach Muster von buildTower implementieren
        return super.sellTower(xCoordinate, yCoordinate, playerNumber);
    }

    /**
     * Rüstet einen Turm auf
     *
     * @param xPosition
     * @param yPosition
     * @param playerNumber
     * @return Wenn das Aufrüsten erfolgreich war, true, ansonsten false
     */
    @Override
    public boolean upgradeTower(int xPosition, int yPosition, int playerNumber) {
        // TODO: Methode nach Muster von buildTower implementieren
        return super.upgradeTower(xPosition, yPosition, playerNumber);
    }

    /**
     * Schickt einen Gegner zum gegnerischen Spieler
     *
     * @param enemyType            Der Typ des zu schickenden Gegners
     * @param playerToSendToNumber
     * @param sendingPlayerNumber
     * @return Wenn das Schicken erfolgreich war, true, ansonsten false
     */
    @Override
    public boolean sendEnemy(int enemyType, int playerToSendToNumber, int sendingPlayerNumber) {
        return super.sendEnemy(enemyType, playerToSendToNumber, sendingPlayerNumber);
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