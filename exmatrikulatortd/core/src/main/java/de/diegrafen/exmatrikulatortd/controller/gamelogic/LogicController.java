package de.diegrafen.exmatrikulatortd.controller.gamelogic;

import de.diegrafen.exmatrikulatortd.controller.factories.EnemyFactory;
import de.diegrafen.exmatrikulatortd.controller.factories.TowerFactory;
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
     * @param towerType
     * @param xPosition
     * @param yPosition
     * @param playerNumber
     * @return Wenn das Bauen erfolgreich war, true, ansonsten false
     */
    boolean buildTower(TowerFactory.TowerType towerType, int xPosition, int yPosition, int playerNumber);

    /**
     * Verkauft einen Turm
     * @param tower Der zu verkaufende Turm
     * @return Wenn das Verkaufen erfolgreich war, true, ansonsten false
     */
    boolean sellTower (int xPosition, int yPosition, int playerNumber);

    /**
     * Rüstet einen Turm auf
     * @param tower Der zu aufzurüstende Turm
     * @return Wenn das Aufrüsten erfolgreich war, true, ansonsten false
     */
    boolean upgradeTower (int xPosition, int yPosition, int playerNumber);

    /**
     * Schickt einen Gegner zum gegnerischen Spieler
     * @param enemy Der zu schickende Gegner
     * @return Wenn das Schicken erfolgreich war, true, ansonsten false
     */
    boolean sendEnemy (EnemyFactory.EnemyType enemyType);

    boolean checkIfCoordinatesAreBuildable (int xCoordinate, int yCoordinate, int playerNumber);

    int getXCoordinateByPosition (float xPosition);

    int getYCoordinateByPosition (float yPosition);

    public void buildFailed();

    public void sendFailed();

    public void upgradeFailed();

}
