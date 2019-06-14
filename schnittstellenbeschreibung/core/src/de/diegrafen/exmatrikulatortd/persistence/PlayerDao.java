package de.diegrafen.exmatrikulatortd.persistence;

import de.diegrafen.exmatrikulatortd.model.Player;

/**
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 14.06.2019 02:50
 */
public class PlayerDao extends BaseDao<Player> {
    @Override
    Class<Player> getClazz() {
        return null;
    }
}
