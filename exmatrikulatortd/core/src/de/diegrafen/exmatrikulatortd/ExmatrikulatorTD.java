package de.diegrafen.exmatrikulatortd;

import com.badlogic.gdx.Game;
import de.diegrafen.exmatrikulatortd.controller.MainController;


/**
 *
 */
public class ExmatrikulatorTD extends Game {

	/**
	 *
	 */
	private MainController mainController;


	@Override
	public void create () {
        mainController = new MainController(this);
        mainController.showSplashScreen();
	}

}
