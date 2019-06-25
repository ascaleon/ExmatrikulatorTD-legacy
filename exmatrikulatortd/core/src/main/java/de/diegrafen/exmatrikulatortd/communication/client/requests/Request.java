package de.diegrafen.exmatrikulatortd.communication.client.requests;

import java.util.Date;

/**
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 14.06.2019 05:07
 */
public abstract class Request {


    private java.util.Date timeSent;

    Request () {
        this.timeSent = new Date();
    }

    public Date getTimeSent() {
        return timeSent;
    }
}
