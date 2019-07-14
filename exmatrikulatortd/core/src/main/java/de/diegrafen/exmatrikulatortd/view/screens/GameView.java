package de.diegrafen.exmatrikulatortd.view.screens;

import com.badlogic.gdx.Screen;
import de.diegrafen.exmatrikulatortd.controller.gamelogic.LogicController;
import de.diegrafen.exmatrikulatortd.model.Gamestate;
import de.diegrafen.exmatrikulatortd.model.ObservableUnit;
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
    void addTower(ObservableUnit observable);

    /**
     * Generiert aus einem beobachtbarem Objekt ein neues Gegner-Spielobjekt
     *
     * @param observable Das hinzuzufügende, beobachtbareObjekt
     */
    void addEnemy(ObservableUnit observable);

    /**
     * Generiert aus einem beobachtbarem Objekt ein neues Projektil-Spielobjekt
     *
     * @param observable Das hinzuzufügende, beobachtbareObjekt
     */
    void addProjectile(ObservableUnit observable);

    void displayErrorMessage(String message);

    void setLogicController(LogicController logicController);

    void loadMap(String mapPath);

    void setGameState(Gamestate gameState);

}
