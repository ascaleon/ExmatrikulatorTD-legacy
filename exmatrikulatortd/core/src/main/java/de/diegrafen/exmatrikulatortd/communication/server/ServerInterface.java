package de.diegrafen.exmatrikulatortd.communication.server;

import de.diegrafen.exmatrikulatortd.communication.ConnectorInterface;

interface ServerInterface extends ConnectorInterface {

    void sendErrorMessage(String errorMessage, int playerNumber);

    void setServerReady();

    void serverFinishedLoading();
}
