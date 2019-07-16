package de.diegrafen.exmatrikulatortd;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import de.diegrafen.exmatrikulatortd.controller.MainController;
import org.hibernate.SessionFactory;

import static de.diegrafen.exmatrikulatortd.util.HibernateUtils.getSessionFactory;


/**
 *
 */
public class ExmatrikulatorTD extends Game {

	/**
	 *
	 */
	private MainController mainController;


	/**
	 *
	 */
	@Override
	public void create () {
        mainController = new MainController(this);
		new Thread(new Runnable() {
			@Override
			public void run() {
				// do something important here, asynchronously to the rendering thread
				//final SessionFactory sessionFactory = getSessionFactory();
				mainController.setDatabaseLoaded(true);
				// post a Runnable to the rendering thread that processes the result
			}
		}).start();
        mainController.showSplashScreen();
	}


}
