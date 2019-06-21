package de.diegrafen.exmatrikulatortd.persistence;

import de.diegrafen.exmatrikulatortd.model.Gamestate;

/**
 *
 * DAO-Klasse für Gamestate-Objekte
 *
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 14.06.2019 02:47
 */
public class GameStateDao extends BaseDao<Gamestate> {

    /**
     * Gibt die Gamestate-Klasse zurück
     * @return Der Klassen-Typ
     */
    @Override
    Class<Gamestate> getClazz() {
        return null;
    }
}
