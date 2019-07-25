package de.diegrafen.exmatrikulatortd.communication.server.responses;

public class StartGameResponse extends Response {

    private boolean finishedLoading;

    public StartGameResponse() {
        super();
        this.finishedLoading = false;
    }

    public StartGameResponse(boolean finishedLoading) {
        this.finishedLoading = finishedLoading;
    }

    public boolean isFinishedLoading() {
        return finishedLoading;
    }

    public void setFinishedLoading(boolean finishedLoading) {
        this.finishedLoading = finishedLoading;
    }
}
