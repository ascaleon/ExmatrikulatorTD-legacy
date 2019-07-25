package de.diegrafen.exmatrikulatortd.communication;

import com.esotericsoftware.kryo.Kryo;
import de.diegrafen.exmatrikulatortd.communication.client.requests.*;
import de.diegrafen.exmatrikulatortd.communication.server.responses.*;

/**
 *
 * Basisklasse für GameClient und GameServer. Dient vor allem der Registrierung von Requests und Responses
 *
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 15.06.2019 13:49
 */
public abstract class Connector implements ConnectorInterface {

    /**
     * Registriert die Requests und Responses
     * @param kryo Das Kryo-Objekt, auf dem die Requests und Responses registriert werden
     */
    protected void registerObjects (Kryo kryo) {
        kryo.register(BuildRequest.class);
        kryo.register(BuildResponse.class);
        kryo.register(GetServerStateRequest.class);
        kryo.register(GetServerStateResponse.class);
        kryo.register(SellRequest.class);
        kryo.register(SellResponse.class);
        kryo.register(SendEnemyRequest.class);
        kryo.register(SendEnemyResponse.class);
        kryo.register(UpgradeRequest.class);
        kryo.register(UpgradeResponse.class);
        kryo.register(GetGameInfoRequest.class);
        kryo.register(GetGameInfoResponse.class);
        kryo.register(StartGameResponse.class);
        kryo.register(ClientReadyRequest.class);

        kryo.register(ErrorResponse.class);
        kryo.register(AllPlayersReadyResponse.class);
        kryo.register(FinishedLoadingRequest.class);

        kryo.register(java.util.LinkedList.class);
        kryo.register(java.util.Date.class);
    }
}
