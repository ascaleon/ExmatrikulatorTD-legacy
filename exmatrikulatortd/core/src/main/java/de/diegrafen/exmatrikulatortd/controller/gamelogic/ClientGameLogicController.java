package de.diegrafen.exmatrikulatortd.controller.gamelogic;

import de.diegrafen.exmatrikulatortd.communication.client.GameClient;
import de.diegrafen.exmatrikulatortd.controller.MainController;
import de.diegrafen.exmatrikulatortd.model.Coordinates;
import de.diegrafen.exmatrikulatortd.model.Gamestate;
import de.diegrafen.exmatrikulatortd.model.Profile;
import de.diegrafen.exmatrikulatortd.model.enemy.Enemy;
import de.diegrafen.exmatrikulatortd.model.tower.Tower;

/**
 *
 * Spiellogik-Controller für Multiplayer-Spiele als Client
 *
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 15.06.2019 05:29
 */
public class ClientGameLogicController extends GameLogicController {

    /**
     * Der GameClient, über den die Netzwerkkommuikation abläuft
     */
    private GameClient gameClient;

    /**
     * Konstruktor für den Spiellogik-Controller
     * @param mainController Der Haupt-Controller der Anwendung
     * @param gamestate Der Spielzustand, mit dem der Controller initialisiert wird
     * @param profile Das Spieler-Profil
     * @param gameClient Der GameClient, über den die Netzwerkkommunikation abläuft
     */
    public ClientGameLogicController(MainController mainController, Gamestate gamestate, Profile profile, GameClient gameClient) {
        super(mainController, gamestate, profile);
        this.gameClient = gameClient;
    }


    /**
     * Baut einen neuen Turm
     *
     * @param tower       Der zu bauende Turm
     * @param coordinates Die Koordinaten des Turmes
     * @return Wenn das Bauen erfolgreich war, true, ansonsten false
     */
    @Override
    public boolean buildTower(Tower tower, Coordinates coordinates) {
        return super.buildTower(tower, coordinates);
    }

    /**
     * Verkauft einen Turm
     *
     * @param tower Der zu verkaufende Turm
     * @return Wenn das Verkaufen erfolgreich war, true, ansonsten false
     */
    @Override
    public boolean sellTower(Tower tower) {
        return super.sellTower(tower);
    }

    /**
     * Rüstet einen Turm auf
     *
     * @param tower Der zu aufzurüstende Turm
     * @return Wenn das Aufrüsten erfolgreich war, true, ansonsten false
     */
    @Override
    public boolean upgradeTower(Tower tower) {
        return super.upgradeTower(tower);
    }

    /**
     * Schickt einen Gegner zum gegnerischen Spieler
     *
     * @param enemy Der zu schickende Gegner
     * @return Wenn das Schicken erfolgreich war, true, ansonsten false
     */
    @Override
    public boolean sendEnemy(Enemy enemy) {
        return super.sendEnemy(enemy);
    }

    /**
     * Holt den aktuellen Spielzustand vom Server
     */
    public void refreshLocalGameState () {

    }

    /**
     * Beendet das Spiel
     *
     */
    public void exitGame () {
        gameClient.shutdown();
        super.exitGame(false);

    }
}
