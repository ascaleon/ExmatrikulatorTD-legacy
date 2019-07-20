package de.diegrafen.exmatrikulatortd.communication.client.requests;

public class ClientReadyRequest extends Request {

    private boolean finishedLoading;

    public ClientReadyRequest() {
        super();
        finishedLoading = false;
    }

    public ClientReadyRequest(boolean finishedLoading) {
        this.finishedLoading = finishedLoading;
    }

    public boolean isFinishedLoading() {
        return finishedLoading;
    }

    public void setFinishedLoading(boolean finishedLoading) {
        this.finishedLoading = finishedLoading;
    }
}
