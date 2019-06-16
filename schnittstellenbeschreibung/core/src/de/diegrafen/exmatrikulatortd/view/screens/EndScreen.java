package de.diegrafen.exmatrikulatortd.view.screens;

import de.diegrafen.exmatrikulatortd.controller.MainController;
import de.diegrafen.exmatrikulatortd.controller.gamelogic.GameLogicController;
import de.diegrafen.exmatrikulatortd.model.Gamestate;

/**
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 15.06.2019 13:56
 *
 * Der EndScreen wird bei Game-Over oder gewinnen des Spiels angezeigt.
 */
public class EndScreen extends BaseScreen {

    /**
     * Der aktuelle Zustand des Spiels.
     */
    private Gamestate gameState;

    /**
     * Der Konstruktor legt den Maincontroller und den aktuellen gameState fest.
     * @param mainController
     * @param gameState
     */
    public EndScreen (MainController mainController, Gamestate gameState) {
        super(mainController);
        this.gameState = gameState;
    }

    /**
     * Der Screen wird Initialisiert.
     */
    @Override
    public void init() {
        //System.out.println("Das hier ist der Endbildschirm!");
        //getMainController().showMenuScreen();
    }


    /**
     * Wird immer nach einem Bestimmten Zeitabstand aufgerufen und die Logik des Spiels berechnet, damit danach in render() neu gezeichnet werden kann.
     * @param deltaTime Die Zeit in Sekunden seit dem letzten Frame.
     */
    @Override
    public void update(float deltaTime) {

    }

    /**
     * Zeigt Statistiken wie Highscore an.
     */
    public void showEndScreenStatistics () {

    }

}
