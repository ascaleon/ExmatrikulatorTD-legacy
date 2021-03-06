package de.diegrafen.exmatrikulatortd.controller;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Server;
import de.diegrafen.exmatrikulatortd.communication.client.GameClient;
import de.diegrafen.exmatrikulatortd.communication.server.GameServer;
import de.diegrafen.exmatrikulatortd.model.Gamestate;
import de.diegrafen.exmatrikulatortd.model.Profile;
import de.diegrafen.exmatrikulatortd.persistence.HighscoreDao;
import de.diegrafen.exmatrikulatortd.persistence.ProfileDao;
import de.diegrafen.exmatrikulatortd.persistence.SaveStateDao;
import de.diegrafen.exmatrikulatortd.view.screens.EndScreen;
import de.diegrafen.exmatrikulatortd.view.screens.GameScreen;
import de.diegrafen.exmatrikulatortd.view.screens.MenuScreen;
import de.diegrafen.exmatrikulatortd.view.screens.SplashScreen;

/**
 *
 * Haupt-Controller. Dient der Verwaltung der Bildschirme und der Interaktion mit der Kommunikations- und Persistenz-
 * Komponente
 *
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 15.06.2019 18:08
 */
public class MainController {

    /**
     * DAO-Objekt für CRUD-Operationen mit Highscore-Objekten
     */
    private HighscoreDao highScoreDao;

    /**
     * DAO-Objekt für CRUD-Operationen mit Profil-Objekte
     */
    private ProfileDao profileDao;

    /**
     * DAO-Objekt für CRUD-Operationen mit Savestate-Objekten
     */
    private SaveStateDao saveStateDao;

    /**
     * Das aktuelle Profil
     */
    private Profile currentProfile;

    /**
     * Menü-Bildschirm
     */
    private final MenuScreen menuScreen;

    /**
     * Splashscreen
     */
    private SplashScreen splashScreen;

    /**
     * Das Spiel-Objekt, das unter anderem das Rendern übernimmt
     */
    private Game game;

    /**
     * Assetmanager zur Verwaltung von Spielressourcen
     */
    private AssetManager assetManager;

    /**
     * Gameserver für den Multiplayer-Modus
     */
    private GameServer gameServer;

    /**
     * Gameclient für den Multiplayer-Modus
     */
    private GameClient gameClient;

    /**
     * Erzeugt einen neuen Maincontroller, das ein Game-Objekt verwaltet
     * @param game Das mit dem Game-Controller zu assoziierende Game-Objekt
     */
    public MainController (Game game) {
        this.game = game;
        this.splashScreen = new SplashScreen(this);
        this.menuScreen = new MenuScreen(this);
        this.assetManager = new AssetManager();
        this.profileDao = new ProfileDao();
        this.highScoreDao = new HighscoreDao();
    }

    /**
        Zeigt den Splash-Screen an
     */
    public void showSplashScreen() {
        game.setScreen(splashScreen);
    }

    /**
     * Lädt Assets
     */
    public void loadAssets() {
    }

    /**
     * Gibt an, ob die Assets vom Assetmanager bereits geladen sind
     * @return True, wenn die Assets geladen sind, ansonsten false
     */
    public boolean areAssetsLoaded() {
        return false;
    }

    /**
     * Zeigt das Hauptmenü an
     */
    public void showMenuScreen() {
        game.setScreen(menuScreen);
    }


    /**
     * Zeigt den Endbildschirm einer Spielpartie an
     * @param gamestate Der Spielzustand, dessen Informationen angezeigt werden sollen
     */
    public void setEndScreen(Gamestate gamestate) {
        game.setScreen(new EndScreen(this, gamestate));
    }

    /**
     * Erzeugt ein neues Profil
     * @param profileName Der Name des Profils
     * @param profilePicture Das Bild des Profils
     */
    public void createNewProfile (String profileName, String profilePicture) {

    }

    /**
     * Gibt das aktuelle Profil zurücl
     * @return Das aktuelle Profil
     */
    public Profile getCurrentProfile() {
        return currentProfile;
    }

    /**
     * Legt ein Profil als aktuelles Profil fest
     * @return Das festzulegende Profil
     */
    public void setCurrentProfile(Profile currentProfile) {
        this.currentProfile = currentProfile;
    }

    /**
     * Erzeugt einen neuen GameServer
     */
    public void createServer() {

    }

    /**
     * Erzeugt einen neuen GameClient
     */
    public void createClient() {

    }

    /**
     * Startet den GameServer
     */
    public void startServer() {

    }

    /**
     * Verbindet den GameClient mit einem GameServer
     * @param host Die IP-Adresse des Servers
     */
    public void connectToServer (String host) {

    }

    /**
     * Erstellt ein neues Einzelspieler-Spiel
     */
    public void createNewSinglePlayerGame () {
        game.setScreen(new GameScreen(this, currentProfile));
    }

    /**
     * Lädt ein Einzelspieler-Spiel
     * @param gamestate Der Spielzustand, der geladen werden soll
     */
    public void loadSinglePlayerGame (Gamestate gamestate) {
        game.setScreen(new GameScreen(this, currentProfile, gamestate));
    }

    /**
     * Erzeugt ein neues Multiplayer-Spiel als Client
     */
    public void createNewMultiplayerClientGame() {
        new GameScreen(this, currentProfile, gameClient);
    }

    /**
     * Lädt ein Multiplayerspiel als Client
     */
    public void loadMultiPlayerClientGame () {
        game.setScreen(new GameScreen(this, currentProfile, new Gamestate()));
    }

    /**
     * Erzeugt ein Multiplayer-Spiel als Server
     */
    public void createNewMultiplayerServerGame() {
        new GameScreen(this, currentProfile, gameServer);
    }

    /**
     * Lädt ein Multiplayerspiel als Server
     * @param gamestate Der Spielzustand, der geladen werden soll
     */
    public void loadMultiPlayerServerGame (Gamestate gamestate) {
        game.setScreen(new GameScreen(this, currentProfile, gameServer, gamestate));
    }

}
