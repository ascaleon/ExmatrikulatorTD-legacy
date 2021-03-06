package de.diegrafen.exmatrikulatortd.controller.gamelogic;

import de.diegrafen.exmatrikulatortd.model.Gamestate;
import de.diegrafen.exmatrikulatortd.model.Player;
import de.diegrafen.exmatrikulatortd.view.screens.GameView;

/**
 * Interface für Controller der Spiellogik
 *
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 15.06.2019 05:28
 */
public interface LogicController {

    void createTowerButtons(GameView gameView);

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

    /**
     * Überprüft, ob ausgewählte Koordinaten vom angegebenen Spieler bebaut werden dürfen.
     *
     * @param xCoordinate Die x-Koordinate des betreffenden Feldes
     * @param yCoordinate Die y-Koordinate des betreffenden Feldes
     * @param playerNumber Die Nummer des betreffenden Spielers
     * @return true, wenn die betreffende Map-Zelle bebaut werden darf, ansonsten false
     */
    boolean checkIfCoordinatesAreBuildable(int xCoordinate, int yCoordinate, int playerNumber);

    /**
     * Gibt die x-Koordinate zurück, die der angegebenen x-Position auf der Karte entspricht
     *
     * @param xPosition Die x-Position, für die die x-Koordinate ermittelt werden soll
     * @return Die passende x-Koordinate
     */
    int getXCoordinateByPosition(float xPosition);

    /**
     * Gibt die y-Koordinate zurück, die der angegebenen y-Position auf der Karte entspricht
     *
     * @param yPosition Die y-Position, für die die y-Koordinate ermittelt werden soll
     * @return Die passende y-Koordinate
     */
    int getYCoordinateByPosition(float yPosition);

    /**
     * Legt den Spielzustand fest
     *
     * @param gamestate Der festzulegende Spielzustand
     */
    void setGamestate(Gamestate gamestate);

    /**
     * Zeigt eine Fehlermeldung für einen Spieler an. Sendet im Multiplayer-Medus die Fehlernachricht an den betreffenden
     * Spieler
     *
     * @param errorMessage Die Fehlermeldung
     * @param playerNumber Die Nummer des Spielers, bei dem der Fehler aufgetreten ist
     */
    void displayErrorMessage(String errorMessage, int playerNumber);

    /**
     * Gibt den Spielzustand zurück
     *
     * @return Der aktuelle Spielzustand
     */
    Gamestate getGamestate();

    /**
     * Gibt den Spielbildschirm zurück
     * @return Der Spielbildschirm
     */
    GameView getGameScreen();

    /**
     * Gibt die Nummer des lokalen Spielers zurück
     *
     * @return Die Nummer des lokalen Spielers
     */
    int getLocalPlayerNumber();

    boolean hasCellTower(int xCoordinate, int yCoordinate);

    void exitGame(boolean saveBeforeExit);

    Player getLocalPlayer();

    void initializeMap(String mapPath);

    boolean isPause();

    boolean isServer();

    void setPause(boolean pause);

    void gameConnectionLost();

    boolean isMultiplayer();

    void saveGame(String saveGameName);

    boolean isActiveRound();

    int getNumberOfPlayers();
}
