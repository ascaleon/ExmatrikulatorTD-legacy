package de.diegrafen.exmatrikulatortd.controller.gamelogic;

import de.diegrafen.exmatrikulatortd.model.Coordinates;
import de.diegrafen.exmatrikulatortd.model.enemy.Enemy;
import de.diegrafen.exmatrikulatortd.model.tower.Tower;

/**
 * Interface für Controller der Spiellogik
 *
 *
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 15.06.2019 05:28
 */
public interface LogicController {

    /**
     * Update-Methode
     * @param deltaTime Die Zeit zwischen zwei Frames
     */
    void update(float deltaTime);

    /**
     * Baut einen neuen Turm
     * @param tower Der zu bauende Turm
     * @param coordinates Die Koordinaten des Turmes
     * @return Wenn das Bauen erfolgreich war, true, ansonsten false
     */
    boolean buildTower (Tower tower, Coordinates coordinates);

    /**
     * Verkauft einen Turm
     * @param tower Der zu verkaufende Turm
     * @return Wenn das Verkaufen erfolgreich war, true, ansonsten false
     */
    boolean sellTower (Tower tower);

    /**
     * Rüstet einen Turm auf
     * @param tower Der zu aufzurüstende Turm
     * @return Wenn das Aufrüsten erfolgreich war, true, ansonsten false
     */
    boolean upgradeTower (Tower tower);

    /**
     * Schickt einen Gegner zum gegnerischen Spieler
     * @param enemy Der zu schickende Gegner
     * @return Wenn das Schicken erfolgreich war, true, ansonsten false
     */
    boolean sendEnemy (Enemy enemy);

}
