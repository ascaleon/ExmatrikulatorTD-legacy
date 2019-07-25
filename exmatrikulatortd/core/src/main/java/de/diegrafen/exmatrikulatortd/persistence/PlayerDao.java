package de.diegrafen.exmatrikulatortd.persistence;

import de.diegrafen.exmatrikulatortd.model.Player;

/**
 *
 * DAO-Klasse für Player-Objekte
 *
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 14.06.2019 02:50
 */
public class PlayerDao extends BaseDao<Player> {

    /**
     * Gibt die Player-Klasse zurück
     * @return Der Klassen-Typ
     */
    @Override
    Class<Player> getClazz() {
        return Player.class;
    }
}
