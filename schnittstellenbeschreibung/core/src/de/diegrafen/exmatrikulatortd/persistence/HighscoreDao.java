package de.diegrafen.exmatrikulatortd.persistence;

import de.diegrafen.exmatrikulatortd.model.Highscore;

/**
 *
 * DAO-Klasse für Highscore-Objekte
 *
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 15.06.2019 14:31
 */
public class HighscoreDao extends BaseDao<Highscore> {


    /**
     * Gibt die Highscore-Klasse zurück
     * @return Der Klassen-Typ
     */
    @Override
    Class<Highscore> getClazz() {
        return null;
    }

}
