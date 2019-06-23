package de.diegrafen.exmatrikulatortd.view.screens;

import de.diegrafen.exmatrikulatortd.model.Coordinates;
import de.diegrafen.exmatrikulatortd.model.Observable;
import de.diegrafen.exmatrikulatortd.model.ObservableUnit;
import de.diegrafen.exmatrikulatortd.model.enemy.Enemy;
import de.diegrafen.exmatrikulatortd.model.tower.Tower;
import de.diegrafen.exmatrikulatortd.view.Observer;
import de.diegrafen.exmatrikulatortd.view.gameobjects.EnemyObject;
import de.diegrafen.exmatrikulatortd.view.gameobjects.GameObject;
import de.diegrafen.exmatrikulatortd.view.gameobjects.TowerObject;

/**
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 15.06.2019 04:20
 */
public interface GameView extends Observer {

    /**
     * Generiert aus einem beobachtbarem Objekt ein neues Turm-Spielobjekt
     * @param observable Das hinzuzufügende, beobachtbareObjekt
     */
    void addTower (ObservableUnit observable);

    /**
     * Generiert aus einem beobachtbarem Objekt ein neues Gegner-Spielobjekt
     * @param observable Das hinzuzufügende, beobachtbareObjekt
     */
    void addEnemy (ObservableUnit observable);

    /**
     * Generiert aus einem beobachtbarem Objekt ein neues Projektil-Spielobjekt
     * @param observable Das hinzuzufügende, beobachtbareObjekt
     */
    void addProjectile (ObservableUnit observable);

}
