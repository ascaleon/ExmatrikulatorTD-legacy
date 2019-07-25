package de.diegrafen.exmatrikulatortd.communication.server.responses;

import java.util.Date;

/**
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 25.06.2019 21:17
 */
abstract class Response {

    private final java.util.Date timeSent;

    Response() {
        timeSent = new java.util.Date();
    }

    public Date getTimeSent() {
        return timeSent;
    }
}
