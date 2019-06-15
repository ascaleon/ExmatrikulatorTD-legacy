package de.diegrafen.exmatrikulatortd.persistence;

import de.diegrafen.exmatrikulatortd.model.SaveState;

/**
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 15.06.2019 14:24
 */
public class SaveStateDao extends BaseDao<SaveState> {

    @Override
    Class<SaveState> getClazz() {
        return null;
    }
}