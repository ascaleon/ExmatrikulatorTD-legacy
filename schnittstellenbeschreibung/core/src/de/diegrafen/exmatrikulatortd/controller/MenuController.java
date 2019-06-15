package de.diegrafen.exmatrikulatortd.controller;

import de.diegrafen.exmatrikulatortd.model.Gamestate;
import de.diegrafen.exmatrikulatortd.persistence.*;

/**
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 15.06.2019 12:06
 */
public class MenuController {

    private SinglePlayerSaveFileDao singlePlayerSaveFileDao;

    private MultiPlayerSaveFileDao multiPlayerSaveFileDao;

    private ProfileDao profileDao;

    private HighscoreDao highscoreDao;

    public Gamestate savedGames;

}
