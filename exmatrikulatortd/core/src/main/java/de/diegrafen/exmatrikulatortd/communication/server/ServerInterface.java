package de.diegrafen.exmatrikulatortd.communication.server;

import de.diegrafen.exmatrikulatortd.communication.ConnectorInterface;

public interface ServerInterface extends ConnectorInterface {

    void sendErrorMessage(String errorMessage, int playerNumber);
<<<<<<< HEAD
=======

    void setServerReady();
>>>>>>> 3825ea28ca601436345c1a8e7ff4c546a034b642
}
