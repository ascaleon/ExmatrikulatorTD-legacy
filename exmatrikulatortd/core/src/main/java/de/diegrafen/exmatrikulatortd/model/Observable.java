package de.diegrafen.exmatrikulatortd.model;

import de.diegrafen.exmatrikulatortd.view.Observer;

/**
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 20.06.2019 16:11
 */
public interface Observable {

    void registerObserver (Observer observer);

    void removeObserver (Observer observer);

    void notifyObserver ();

}
