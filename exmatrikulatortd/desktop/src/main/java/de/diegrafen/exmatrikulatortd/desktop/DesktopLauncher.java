package de.diegrafen.exmatrikulatortd.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import de.diegrafen.exmatrikulatortd.ExmatrikulatorTD;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(new ExmatrikulatorTD(), config);
	}
}
