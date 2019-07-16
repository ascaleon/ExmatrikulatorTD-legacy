package de.diegrafen.exmatrikulatortd;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import de.diegrafen.exmatrikulatortd.controller.MainController;
import de.diegrafen.exmatrikulatortd.util.Assets;
import org.hibernate.SessionFactory;

import static de.diegrafen.exmatrikulatortd.util.HibernateUtils.getSessionFactory;


/**
 *
 */
public class ExmatrikulatorTD extends Game implements GameInterface {

	/**
	 * Verwaltet die Assets des Spiels
	 */
	private AssetManager assetManager;

	/**
	 * Der Haupt-Controller des Spiels, der die verschiedenen Bildschirme sowie Zugriffe auf die Datenbank und das
	 * Netzwerk verwaltet
	 */
	private MainController mainController;

	/**
	 * Konstruktor für ein neues Spiel-Objekt
	 */
	public ExmatrikulatorTD() {

	}

	/**
	 * Initialisiert das Spielobjekt, den {@code AssetManager}, den {@code MainController} sowie die Datenbankverbindung.
	 * Zeigt zudem einen {@code SplashScreen} an, der als Ladebildschirm dient.
	 */
	@Override
	public void create () {
		assetManager = new AssetManager();
		Assets.queueAssets(assetManager);
		startDatabaseInitialization();
        mainController = new MainController(this);
        mainController.showSplashScreen();
	}

	/**
	 * Gibt den assetManager zurück.
	 * @return Der assetManager
	 */
	public AssetManager getAssetManager() {
		return assetManager;
	}

	/**
	 * Gibt den Speicher der verwendeten Spielressourcen wieder frei
	 */
	@Override
	public void dispose() {
		super.dispose();
		assetManager.dispose();
	}

	/**
	 * Initialisiert die Datenbank, indem auf die Session-Factory zugegriffen wird. Läuft parallel zum Render-Thread
	 * und setzt nach der Initialisierung das {@code databaseLoaded}-Attribut des {@link MainController}
	 * auf {@code true}
	 */
	private void startDatabaseInitialization() {
		new Thread(() -> {
			getSessionFactory(); // Initialisiert die Datenbank
			mainController.setDatabaseLoaded(true);
		}).start();
	}
}
