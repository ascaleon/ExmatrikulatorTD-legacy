package de.diegrafen.towerwars.net;

import com.esotericsoftware.kryo.Kryo;
import de.diegrafen.towerwars.sprites.Tower;

/**
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 16.05.2019 00:11
 */
abstract class Connector {

    public Connector (Kryo kryo) {
        registerObjects(kryo);
    }

    private void registerObjects (Kryo kryo) {
        kryo.register(GameState.class);
        kryo.register(PlayerMove.class);
    }
}
