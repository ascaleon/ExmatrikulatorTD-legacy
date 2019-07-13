package de.diegrafen.exmatrikulatortd.communication.server;

import de.diegrafen.exmatrikulatortd.communication.ConnectorInterface;

public interface ServerInterface extends ConnectorInterface {

    void sendErrorMessage(String errorMessage, int playerNumber);
}
