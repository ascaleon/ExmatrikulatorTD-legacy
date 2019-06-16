package de.diegrafen.exmatrikulatortd.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 *
 * Utility-Klsse für die Nutzung von Hibernate. Dient insbesondere der Bereitstellung einer Session-Factory für
 * die Erzeugung von Sessions zur Interaktion mit der Datenbank.
 *
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 14.06.2019 02:23
 */
public class HibernateUtils {

    /**
     * Privater Konstruktor
     */
    private HibernateUtils() {}

    /**
     * Die Session-Factory
     */
    private static final SessionFactory sessionFactory;

    static {
        try {
            sessionFactory = new Configuration().configure().buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }


    /**
     * Gibt die Session-Factory zurück
     * @return Die Session-Factory
     */
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    /**
     * Schließt die aktuelle Session-Factory
     */
    public static void shutdown() {

        getSessionFactory().close();
    }
}
