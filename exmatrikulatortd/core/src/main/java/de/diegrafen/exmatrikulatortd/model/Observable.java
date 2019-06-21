package de.diegrafen.exmatrikulatortd.model;

import de.diegrafen.exmatrikulatortd.view.gameobjects.GameObject;

/**
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 20.06.2019 16:11
 */
public interface Observable {

    void registerObserver (GameObject gameObject);

    void removeObserver (GameObject gameObject);

    void notifyObserver ();

    String getName ();

    String getAssetsName ();

    float getxPosition ();

    float getyPosition ();

    float getTargetxPosition ();

    float getTargetyPosition ();

    boolean isRemoved ();

}
