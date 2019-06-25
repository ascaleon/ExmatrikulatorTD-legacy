package de.diegrafen.exmatrikulatortd.communication;

import de.diegrafen.exmatrikulatortd.controller.factories.EnemyFactory;
import de.diegrafen.exmatrikulatortd.controller.factories.TowerFactory;
import de.diegrafen.exmatrikulatortd.model.enemy.Enemy;
import de.diegrafen.exmatrikulatortd.model.tower.Tower;

public interface ConnectorInterface {

    /**
     * Baut einen neuen Turm
     * @param towerType
     * @param xPosition
     * @param yPosition
     * @param playerNumber
     * @return Wenn das Bauen erfolgreich war, true, ansonsten false
     */
    void buildTower(TowerFactory.TowerType towerType, int xPosition, int yPosition, int playerNumber);

    /**
     * Verkauft einen Turm
     * @param tower Der zu verkaufende Turm
     * @return Wenn das Verkaufen erfolgreich war, true, ansonsten false
     */
    void sellTower (int xPosition, int yPosition, int playerNumber);

    /**
     * Rüstet einen Turm auf
     * @param tower Der zu aufzurüstende Turm
     * @return Wenn das Aufrüsten erfolgreich war, true, ansonsten false
     */
    void upgradeTower (int xPosition, int yPosition, int playerNumber);

    /**
     * Schickt einen Gegner zum gegnerischen Spieler
     * @param enemy Der zu schickende Gegner
     * @return Wenn das Schicken erfolgreich war, true, ansonsten false
     */
    void sendEnemy (EnemyFactory.EnemyType enemyType);

    void shutdown ();
}
