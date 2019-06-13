package de.diegrafen.towerwars.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import de.diegrafen.towerwars.Towerwars;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = Towerwars.WIDTH;
        config.height = Towerwars.HEIGHT;
        config.title = Towerwars.TITLE;

		new LwjglApplication(new Towerwars(), config);
	}
}

