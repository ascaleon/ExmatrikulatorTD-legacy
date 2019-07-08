package de.diegrafen.exmatrikulatortd.communication;

import de.diegrafen.exmatrikulatortd.controller.factories.EnemyFactory;
import de.diegrafen.exmatrikulatortd.controller.factories.TowerFactory;
import de.diegrafen.exmatrikulatortd.model.enemy.Enemy;
import de.diegrafen.exmatrikulatortd.model.tower.Tower;

import static de.diegrafen.exmatrikulatortd.controller.factories.EnemyFactory.*;
import static de.diegrafen.exmatrikulatortd.controller.factories.TowerFactory.*;

public interface ConnectorInterface {

    /**
     * Baut einen neuen Turm an den angegebenen Koordinaten auf der Karte
     *
     * @param towerType    Der Typ des zu bauenden Turms
     * @param xCoordinate  Die x-Koordinate der Stelle, an der der Turm gebaut werden soll
     * @param yCoordinate  Die y-Koordinate der Stelle, an der der Turm gebaut werden soll
     * @param playerNumber Die Nummer der Spielerin, die den Turm bauen will
     */
    void buildTower(int towerType, int xCoordinate, int yCoordinate, int playerNumber);

    /**
     * Verkauft einen Turm
     *
     * @param xCoordinate  Die x-Koordinate des Turms
     * @param yCoordinate  Die y-Koordinate des Turms
     * @param playerNumber Die Nummer der Spielerin, der der Turm gehört
     */
    void sellTower(int xCoordinate, int yCoordinate, int playerNumber);

    /**
     * Rüstet einen Turm auf
     *
     * @param xCoordinate  Die x-Koordinate des Turms
     * @param yCoordinate  Die y-Koordinate des Turms
     * @param playerNumber Die Nummer der Spielerin, der der Turm gehört
     */
    void upgradeTower(int xCoordinate, int yCoordinate, int playerNumber);

    /**
     * Schickt einen Gegner zum gegnerischen Spieler
     *
     * @param enemyType Der Typ des zu schickenden Gegners
     */
    void sendEnemy(int enemyType, int playerToSendTo, int sendingPlayer);

    void shutdown ();
}
