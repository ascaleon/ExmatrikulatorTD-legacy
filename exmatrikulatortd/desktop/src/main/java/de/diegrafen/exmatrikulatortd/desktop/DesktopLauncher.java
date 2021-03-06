package de.diegrafen.exmatrikulatortd.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import de.diegrafen.exmatrikulatortd.ExmatrikulatorTD;

import static de.diegrafen.exmatrikulatortd.util.Constants.GAME_TITLE;

class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = GAME_TITLE;
		config.height = 720;
		config.width = 1280;
		//config.resizable = false;
		//config.fullscreen = true;
		new LwjglApplication(new ExmatrikulatorTD(), config);
	}
}
