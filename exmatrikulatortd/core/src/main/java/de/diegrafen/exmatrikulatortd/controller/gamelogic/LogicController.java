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

    boolean checkIfCoordinatesAreBuildable(int xCoordinate, int yCoordinate, int playerNumber);

    int getXCoordinateByPosition(float xPosition);

    int getYCoordinateByPosition(float yPosition);

    void setGamestate(Gamestate gamestate);

    void displayErrorMessage(String errorMessage, int playerNumber);

    Gamestate getGamestate();

    GameView getGameScreen();

    int getLocalPlayerNumber();

    boolean hasCellTower(int xCoordinate, int yCoordinate);

    void exitGame(boolean saveBeforeExit);

    Player getLocalPlayer();

    void initializeMap(String mapPath);

    boolean isPause();

    void setPause(boolean pause);

    void gameConnectionLost();

    boolean isMultiplayer();

    void saveGame(String saveGameName);

    void loadGame(int id);

    boolean isActiveRound();

    int getNumberOfPlayers();
}
