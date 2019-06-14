package de.diegrafen.exmatrikulatortd.persistence;

import de.diegrafen.exmatrikulatortd.model.Gamestate;

/**
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 14.06.2019 02:47
 */
public class GameStateDao extends BaseDao<Gamestate> {

    @Override
    Class<Gamestate> getClazz() {
        return null;
    }
}
