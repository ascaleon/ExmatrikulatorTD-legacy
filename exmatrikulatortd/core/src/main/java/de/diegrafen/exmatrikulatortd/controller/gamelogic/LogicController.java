package de.diegrafen.exmatrikulatortd.controller.gamelogic;

import de.diegrafen.exmatrikulatortd.controller.factories.EnemyFactory;
import de.diegrafen.exmatrikulatortd.controller.factories.TowerFactory;
import de.diegrafen.exmatrikulatortd.model.Coordinates;
import de.diegrafen.exmatrikulatortd.model.Gamestate;
import de.diegrafen.exmatrikulatortd.model.enemy.Enemy;
import de.diegrafen.exmatrikulatortd.model.tower.Tower;

/**
 * Interface für Controller der Spiellogik
 *
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 15.06.2019 05:28
 */
public interface LogicController {

    /**
     * Update-Methode, die eine Aktualisierung des Spielzustandes auslöst
     *
     * @param deltaTime Die Zeit, die seit dem Rendern des letzten Frames vergangen ist
     */
    void update(float deltaTime);


    /**
     * Baut einen neuen Turm an den angegebenen Koordinaten auf der Karte
     *
     * @param towerType    Der Typ des zu bauenden Turms
     * @param xCoordinate  Die x-Koordinate der Stelle, an der der Turm gebaut werden soll
     * @param yCoordinate  Die y-Koordinate der Stelle, an der der Turm gebaut werden soll
     * @param playerNumber Die Nummer der Spielerin, die den Turm bauen will
     * @return Wenn das Bauen erfolgreich war, true, ansonsten false
     */
    boolean buildTower(int towerType, int xCoordinate, int yCoordinate, int playerNumber);

    /**
     * Verkauft einen Turm
     *
     * @param xCoordinate  Die x-Koordinate des Turms
     * @param yCoordinate  Die y-Koordinate des Turms
     * @param playerNumber Die Nummer der Spielerin, der der Turm gehört
     * @return Wenn das Verkaufen erfolgreich war, true, ansonsten false
     */
    boolean sellTower(int xCoordinate, int yCoordinate, int playerNumber);

    /**
     * Rüstet einen Turm auf
     *
     * @param xCoordinate  Die x-Koordinate des Turms
     * @param yCoordinate  Die y-Koordinate des Turms
     * @param playerNumber Die Nummer der Spielerin, der der Turm gehört
     * @return Wenn das Aufrüsten erfolgreich war, true, ansonsten false
     */
    boolean upgradeTower(int xCoordinate, int yCoordinate, int playerNumber);

    /**
     * Schickt einen Gegner zum gegnerischen Spieler
     *
     * @param enemyType Der Typ des zu schickenden Gegners
     * @return Wenn das Schicken erfolgreich war, true, ansonsten false
     */
    boolean sendEnemy(int enemyType, int playerToSendTo, int sendingPlayer);

    boolean checkIfCoordinatesAreBuildable(int xCoordinate, int yCoordinate, int playerNumber);

    int getXCoordinateByPosition(float xPosition);

    int getYCoordinateByPosition(float yPosition);

    void buildFailed();

    void sendFailed();

    void upgradeFailed();

    void setGamestate(Gamestate gamestate);

    Gamestate getGamestate();

}
