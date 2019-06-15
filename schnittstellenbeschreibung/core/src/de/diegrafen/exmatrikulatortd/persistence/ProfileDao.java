package de.diegrafen.exmatrikulatortd.persistence;

import de.diegrafen.exmatrikulatortd.model.Profile;

/**
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 15.06.2019 14:32
 */
public class ProfileDao extends BaseDao<Profile> {


    @Override
    Class<Profile> getClazz() {
        return null;
    }


}
