package de.diegrafen.towerwars.net;

import com.esotericsoftware.kryo.Kryo;
import de.diegrafen.towerwars.sprites.Tower;

/**
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 16.05.2019 00:11
 */
public abstract class Connector {

    void registerObjects (Kryo kryo) {
        kryo.register(GameState.class);
        kryo.register(PlayerMove.class);
    }
}
