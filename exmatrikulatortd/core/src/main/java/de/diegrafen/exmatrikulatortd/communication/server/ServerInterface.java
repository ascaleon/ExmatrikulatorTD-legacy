package de.diegrafen.exmatrikulatortd.communication.server;

import de.diegrafen.exmatrikulatortd.communication.ConnectorInterface;
import de.diegrafen.exmatrikulatortd.model.Gamestate;

interface ServerInterface extends ConnectorInterface {

    void sendErrorMessage(String errorMessage, int playerNumber);

    void setServerReady();

    void serverFinishedLoading();

    void sendServerGameState(Gamestate gamestate);
}
