package de.diegrafen.exmatrikulatortd.persistence;

import de.diegrafen.exmatrikulatortd.model.Profile;

/**
 *
 * DAO-Klasse für Profile-Objekte
 *
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 15.06.2019 14:32
 */
public class ProfileDao extends BaseDao<Profile> {

    /**
     * Gibt die Profile-Klasse zurück
     * @return Der Klassen-Typ
     */
    @Override
    Class<Profile> getClazz() {
        return Profile.class;
    }


}
