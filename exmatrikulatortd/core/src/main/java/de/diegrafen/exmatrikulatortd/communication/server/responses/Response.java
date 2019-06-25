package de.diegrafen.exmatrikulatortd.communication.server.responses;

/**
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 14.06.2019 05:06
 */
public abstract class Response {

    private java.util.Date timeSent;

    Response () {
        timeSent = new java.util.Date();
    }

}
