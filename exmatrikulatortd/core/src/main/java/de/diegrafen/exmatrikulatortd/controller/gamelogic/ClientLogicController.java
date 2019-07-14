package de.diegrafen.exmatrikulatortd.controller.gamelogic;

public interface ClientLogicController extends LogicController {

    void addTowerByServer(int towerType, int xCoordinate, int yCoordinate, int playerNumber);

    void sellTowerByServer(int xCoordinate, int yCoordinate, int playerNumber);

    void sendEnemyFromServer(int enemyType, int playerToSendTo, int sendingPlayer);

    void upgradeTowerFromServer(int xCoordinate, int yCoordinate, int playerNumber);
}
