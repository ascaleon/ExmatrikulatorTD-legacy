package de.diegrafen.exmatrikulatortd.view.screens;

import com.badlogic.gdx.Screen;
import de.diegrafen.exmatrikulatortd.controller.gamelogic.LogicController;
import de.diegrafen.exmatrikulatortd.model.Gamestate;
import de.diegrafen.exmatrikulatortd.model.ObservableUnit;
import de.diegrafen.exmatrikulatortd.model.enemy.ObservableEnemy;
import de.diegrafen.exmatrikulatortd.model.tower.ObservableTower;
import de.diegrafen.exmatrikulatortd.view.Observer;

/**
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 15.06.2019 04:20
 */
public interface GameView extends Observer, Screen {

    /**
     * Generiert aus einem beobachtbarem Objekt ein neues Turm-Spielobjekt
     *
     * @param observable Das hinzuzufügende, beobachtbareObjekt
     */
    void addTower(ObservableTower observable);

    /**
     * Generiert aus einem beobachtbarem Objekt ein neues Gegner-Spielobjekt
     *
     * @param observable Das hinzuzufügende, beobachtbareObjekt
     */
    void addEnemy(ObservableEnemy observable);

    /**
     * Generiert aus einem beobachtbarem Objekt ein neues Projektil-Spielobjekt
     *
     * @param observable Das hinzuzufügende, beobachtbareObjekt
     */
    void addProjectile(ObservableUnit observable);

    /**
     * Zeigt eine Fehlermeldung auf dem Bildschirm an
     * @param message Die anzuzeigende Fehlermeldung
     */
    void displayErrorMessage(String message);

    /**
     * Assoziiert die GameView mit einem LogicController
     * @param logicController Der zu assoziierende LogicController
     */
    void setLogicController(LogicController logicController);

    /**
     * Lädt eine Karte
     * @param mapPath Der Dateipfad der Karte
     */
    void loadMap(String mapPath);

    /**
     * Legt einen Spielzustand als Attribut der GameView fest
     * @param gameState
     */
    void setGameState(Gamestate gameState);

    /**
     * Zeigt den Spielendbildschirm an
     */
    void endOfGameScreen(boolean victorious, int score, int highscore);

    void addTowerButton(ObservableTower observableTower);

    void clearGameObjects();
}
