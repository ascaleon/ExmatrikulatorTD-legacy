package de.diegrafen.exmatrikulatortd.controller;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Server;
import de.diegrafen.exmatrikulatortd.communication.client.GameClient;
import de.diegrafen.exmatrikulatortd.communication.server.GameServer;
import de.diegrafen.exmatrikulatortd.controller.gamelogic.ClientGameLogicController;
import de.diegrafen.exmatrikulatortd.model.Difficulty;
import de.diegrafen.exmatrikulatortd.model.Gamestate;
import de.diegrafen.exmatrikulatortd.model.Highscore;
import de.diegrafen.exmatrikulatortd.model.Profile;
import de.diegrafen.exmatrikulatortd.persistence.HighscoreDao;
import de.diegrafen.exmatrikulatortd.persistence.ProfileDao;
import de.diegrafen.exmatrikulatortd.persistence.SaveStateDao;
import de.diegrafen.exmatrikulatortd.view.screens.EndScreen;
import de.diegrafen.exmatrikulatortd.view.screens.GameScreen;
import de.diegrafen.exmatrikulatortd.view.screens.MenuScreen;
import de.diegrafen.exmatrikulatortd.view.screens.SplashScreen;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
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
     *
     * @param game Das mit dem Game-Controller zu assoziierende Game-Objekt
     */
    public MainController(Game game) {
        this.game = game;
        this.splashScreen = new SplashScreen(this, game);
        this.menuScreen = new MenuScreen(this, game);
        this.assetManager = new AssetManager();
        this.profileDao = new ProfileDao();
        this.highScoreDao = new HighscoreDao();
    }

    /**
     * Zeigt den Splash-Screen an
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
     *
     * @return True, wenn die Assets geladen sind, ansonsten false
     */
    public boolean areAssetsLoaded() {
        return true;
    }

    /**
     * Zeigt das Hauptmenü an
     */
    public void showMenuScreen() {
        game.setScreen(menuScreen);
    }


    /**
     * Zeigt den Endbildschirm einer Spielpartie an
     *
     * @param gamestate Der Spielzustand, dessen Informationen angezeigt werden sollen
     */
    public void setEndScreen(Gamestate gamestate) {
        game.setScreen(new EndScreen(this, game, gamestate));
    }

    /**
     * Erzeugt ein neues Profil
     *
     * @param profileName    Der Name des Profils
     * @param profilePicture Das Bild des Profils
     */
    public void createNewProfile(String profileName, String profilePicture) {

    }

    /**
     * Gibt das aktuelle Profil zurücl
     *
     * @return Das aktuelle Profil
     */
    public Profile getCurrentProfile() {
        return currentProfile;
    }

    /**
     * Legt ein Profil als aktuelles Profil fest
     *
     * @return Das festzulegende Profil
     */
    public void setCurrentProfile(Profile currentProfile) {
        this.currentProfile = currentProfile;
    }

    /**
     * Erzeugt einen neuen GameServer
     */
    public void createServer() {
        this.gameServer = new GameServer();
    }

    /**
     * Erzeugt einen neuen GameClient
     */
    public void createClient() {
        if (gameClient == null) {
            this.gameClient = new GameClient();
        } else {
            /*
            // Code, um Server-Funktionalität zu testen.
            List<InetAddress> servers  = gameClient.discoverLocalServers();


            // TODO: Empfangene Informationen müssen geparst werden
            for (InetAddress inetAddress : servers) {
                System.out.println(inetAddress.getHostAddress());
            }

            for (String string : gameClient.getReceivedSessionInfo()) {
                System.out.println(string);
            }

            if (!servers.isEmpty()) {
                gameClient.connect(servers.get(0).getHostName());
            } else {
                System.out.println("Keine Server gefunden!");
            }
             */
        }
    }

    public List<String> getLocalGameServers() {

        List<String> serverList = new ArrayList<>();

        // Code, um Server-Funktionalität zu testen.
        List<InetAddress> servers  = gameClient.discoverLocalServers();


        // TODO: Empfangene Informationen müssen geparst werden
        //for (InetAddress inetAddress : servers) {
            //System.out.println(inetAddress.getHostAddress());
            //serverList.add(inetAddress.getHostAddress());
        //}

        serverList = gameClient.getReceivedSessionInfo();

        for (String string : serverList) {
            System.out.println(string);
        }

        if (!servers.isEmpty()) {
            //gameClient.connect(servers.get(0).getHostName());
        } else {
            System.out.println("Keine Server gefunden!");
        }

        return serverList;
    }

    public void shutdownClient() {
        this.gameClient.shutdown();
        //this.gameClient = null;
    }

    /**
     * Startet den GameServer
     */
    public void startServer() {
        gameServer.startServer();
    }

    /**
     * Verbindet den GameClient mit einem GameServer
     *
     * @param host Die IP-Adresse des Servers
     */
    public void connectToServer(String host) {
        gameClient.connect(host);
    }

    /**
     * Erstellt ein neues Einzelspieler-Spiel
     */
    public void createNewSinglePlayerGame() {
        game.setScreen(new GameScreen(this, game, currentProfile));
    }

    /**
     * Lädt ein Einzelspieler-Spiel
     *
     * @param gamestate Der Spielzustand, der geladen werden soll
     */
    public void loadSinglePlayerGame(Gamestate gamestate) {
        game.setScreen(new GameScreen(this, game, currentProfile, gamestate));
    }

    /**
     * Erzeugt ein neues Multiplayer-Spiel als Client
     */
    public void createNewMultiplayerClientGame() {
        new GameScreen(this, game, currentProfile, gameClient);
    }

    /**
     * Lädt ein Multiplayerspiel als Client
     */
    public void loadMultiPlayerClientGame() {
        game.setScreen(new GameScreen(this, game, currentProfile, new Gamestate()));
    }

    /**
     * Erzeugt ein Multiplayer-Spiel als Server
     */
    public void createNewMultiplayerServerGame() {
        new GameScreen(this, game, currentProfile, gameServer);
    }

    /**
     * Lädt ein Multiplayerspiel als Server
     *
     * @param gamestate Der Spielzustand, der geladen werden soll
     */
    public void loadMultiPlayerServerGame(Gamestate gamestate) {
        game.setScreen(new GameScreen(this, game, currentProfile, gameServer, gamestate));
    }

    public List<Highscore> retrieveHighscores(int limit) {

        //Profile profile = new Profile("Sherlock Holmes", Difficulty.EASY);

        //profileDao.create(profile);

        //Highscore highscore1 = new Highscore(profile, 9000, 25, new Date());
        //Highscore highscore2 = new Highscore(profile, 5012, 691, new Date());
        //Highscore highscore3 = new Highscore(profile, 1337, 42, new Date());

        //highScoreDao.create(highscore1);
        //highScoreDao.create(highscore2);
        //highScoreDao.create(highscore3);

        return highScoreDao.findHighestScores(limit);
    }

}
