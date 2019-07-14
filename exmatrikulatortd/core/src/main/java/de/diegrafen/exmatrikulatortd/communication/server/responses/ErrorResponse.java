package de.diegrafen.exmatrikulatortd.communication.server.responses;

public class ErrorResponse extends Response {

    private String errorMessage;

    private int playerNumber;

    public ErrorResponse() {

    }

    public ErrorResponse(String errorMessage, int playerNumber) {
        super();
        this.errorMessage = errorMessage;
        this.playerNumber = playerNumber;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public int getPlayerNumber() {
        return playerNumber;
    }
}
