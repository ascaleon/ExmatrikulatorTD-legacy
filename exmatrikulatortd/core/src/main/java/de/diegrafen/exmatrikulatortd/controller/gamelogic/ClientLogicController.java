package de.diegrafen.exmatrikulatortd.controller.gamelogic;

import de.diegrafen.exmatrikulatortd.model.Gamestate;
import de.diegrafen.exmatrikulatortd.model.Player;
import de.diegrafen.exmatrikulatortd.model.tower.Tower;

import java.util.List;

/**
 * Interface für einen Spiellogik-Controller, über den ein Spielclient Befehle vom Gameserver
 * lokal ausführen kann
 *
 * @author Jan Romann <jan.romann@uni-bremen.de>
 */
public interface ClientLogicController extends LogicController {

    /**
     * Fügt dem Spiel einen Turm hinzu. Da angenommen wird, dass die Prüfung des Spielzuges bereits durch den Server
     * erfolgt ist, wird nicht überprüft, ob der Spielzug legal ist.
     *
     * @param towerType    Die Nummer des Turmtyps
     * @param xCoordinate  Die x-Koordinate, an der der Turm gebaut werden soll
     * @param yCoordinate  Die y-Koordinate, an der der Turm gebaut werden soll
     * @param playerNumber Die Nummer der Spielerin, die den Turm baut
     */
    void addTowerFromServer(int towerType, int xCoordinate, int yCoordinate, int playerNumber);

    /**
     * Verkauft einen Turm. Da angenommen wird, dass die Prüfung des Spielzuges bereits durch den Server
     * erfolgt ist, wird nicht überprüft, ob der Spielzug legal ist.
     *
     * @param xCoordinate  Die x-Koordinate, an der sich der zu verkaufende Turm befindet
     * @param yCoordinate  Die y-Koordinate, an der sich der zu verkaufende Turm befindet
     * @param playerNumber Die Spielernummer der Turm-Eigentümerin
     */
    void sellTowerFromServer(int xCoordinate, int yCoordinate, int playerNumber);

    /**
     * Sendet einen Gegner von einer Spielerin an eine andere Spielerin. Da angenommen wird, dass die Prüfung des
     * Spielzuges bereits durch den Server erfolgt ist, wird nicht überprüft, ob der Spielzug legal ist.
     *
     * @param enemyType            Die Nummer des Gegnertyps
     * @param playerToSendToNumber Die Nummer der Spielerin, an die der Gegner geschickt wird
     * @param sendingPlayerNumber  Die Nummer der Spielerin, die den Turm geschickt hat
     */
    void sendEnemyFromServer(int enemyType, int playerToSendToNumber, int sendingPlayerNumber);

    /**
     * Rüstet einen Turm auf. Da angenommen wird, dass die Prüfung des
     * Spielzuges bereits durch den Server erfolgt ist, wird nicht überprüft, ob der Spielzug legal ist.
     *
     * @param xCoordinate  Die x-Koordinate, an der sich der aufzurüstende Turm befindet
     * @param yCoordinate  Die y-Koordinate, an der sich der aufzurüstende Turm befindet
     * @param playerNumber Die Nummer der Spielerin, der der Turm gehört
     */
    void upgradeTowerFromServer(int xCoordinate, int yCoordinate, int playerNumber);

    void setGamestateFromServer(List<Tower> towers, List<Player> players, float timeUntilNextRound);
}
