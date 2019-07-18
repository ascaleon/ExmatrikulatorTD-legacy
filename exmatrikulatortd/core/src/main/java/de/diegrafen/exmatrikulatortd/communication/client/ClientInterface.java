package de.diegrafen.exmatrikulatortd.communication.client;

import de.diegrafen.exmatrikulatortd.communication.ConnectorInterface;

/**
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 15.06.2019 05:37
 */
interface ClientInterface extends ConnectorInterface {

    /**
     * Fragt eine aktuelle Kopie des Server-Spielzustandes an
     */
    void refreshLocalGameState ();

    /**
     * Stellt die Verbindung zu einem Server her.
     * @param host Die Hostadresse
     * @return @code{true}, wenn die Verbindung erfolgreich hergestellt wurde. Ansonsten @code{false}
     */
    boolean connect (String host);

    void reportReadiness();

    void reportFinishedLoading();
}
