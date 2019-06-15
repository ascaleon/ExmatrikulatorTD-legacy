package de.diegrafen.exmatrikulatortd;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.diegrafen.exmatrikulatortd.controller.MainController;

public class ExmatrikulatorTD extends Game {

	private MainController mainController;

	@Override
	public void create () {
        mainController = new MainController(this);
        mainController.setSplashScreen();
	}

}
