package de.diegrafen.exmatrikulatortd.controller.gamelogic;

public interface ClientLogicController extends LogicController {

    void addTowerByServer(int towerType, int xCoordinate, int yCoordinate, int playerNumber);
}
