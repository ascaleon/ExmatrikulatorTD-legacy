package de.diegrafen.exmatrikulatortd.communication;

import com.esotericsoftware.kryo.Kryo;
import de.diegrafen.exmatrikulatortd.communication.client.requests.*;
import de.diegrafen.exmatrikulatortd.communication.server.responses.*;

/**
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 15.06.2019 13:49
 */
public abstract class Connector {

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
    }
}
