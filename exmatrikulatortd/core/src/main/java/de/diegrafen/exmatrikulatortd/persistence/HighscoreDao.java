package de.diegrafen.exmatrikulatortd.persistence;

import de.diegrafen.exmatrikulatortd.model.Highscore;
import org.hibernate.Session;

import java.util.List;

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
        return Highscore.class;
    }


    /**
     * Ruft eine bestimmte Anzahl der höchsten Highscores aus der Datenbank ab und gibt sie als Liste in absteigender
     * Reihenfolge zurück.
     *
     * @param limit Die Anzahl der @code{Highscore}s, die aus der Datenbank abgerufen werden sollen
     * @return Die <i>n</i> höchsten @code{Highscore}s aus der Datenbank
     * @author Jan Romann
     */
    public List<Highscore> findHighestScores(int limit) {
        if (limit < 0) {
            return null;
        }

        Session session = openCurrentSessionwithTransaction();
        final List<Highscore> highscores = session.createNamedQuery("Highscore.findHighestScores", getClazz()).setMaxResults(limit).getResultList();
        closeCurrentSessionwithTransaction();
        return highscores;
    }
}
