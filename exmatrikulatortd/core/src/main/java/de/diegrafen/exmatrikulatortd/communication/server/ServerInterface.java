package de.diegrafen.exmatrikulatortd.communication.server;

import de.diegrafen.exmatrikulatortd.communication.ConnectorInterface;
import de.diegrafen.exmatrikulatortd.model.Gamestate;
import de.diegrafen.exmatrikulatortd.model.tower.Tower;

import java.util.List;

interface ServerInterface extends ConnectorInterface {

    void sendErrorMessage(String errorMessage, int playerNumber);

    void setServerReady();

    void serverFinishedLoading();

    void sendServerGameState(List<Tower> towers);
}
