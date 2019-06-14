package de.diegrafen.exmatrikulatortd.persistence;

import de.diegrafen.exmatrikulatortd.model.ServerState;

/**
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 14.06.2019 02:43
 */
public class MultiplayerStateDao extends BaseDao<ServerState> {

    @Override
    Class<ServerState> getClazz() {
        return null;
    }


}
