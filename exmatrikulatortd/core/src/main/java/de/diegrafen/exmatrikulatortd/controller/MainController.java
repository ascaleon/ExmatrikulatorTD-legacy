package de.diegrafen.exmatrikulatortd.controller;

import com.badlogic.gdx.Screen;
import de.diegrafen.exmatrikulatortd.ExmatrikulatorTD;
import de.diegrafen.exmatrikulatortd.communication.client.GameClient;
import de.diegrafen.exmatrikulatortd.communication.server.GameServer;
import de.diegrafen.exmatrikulatortd.controller.gamelogic.ClientGameLogicController;
import de.diegrafen.exmatrikulatortd.controller.gamelogic.GameLogicController;
import de.diegrafen.exmatrikulatortd.model.*;
import de.diegrafen.exmatrikulatortd.persistence.HighscoreDao;
import de.diegrafen.exmatrikulatortd.persistence.ProfileDao;
import de.diegrafen.exmatrikulatortd.persistence.SaveStateDao;
import de.diegrafen.exmatrikulatortd.view.screens.*;

import javax.persistence.PersistenceException;
import java.net.InetAddress;
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
    private final HighscoreDao highScoreDao;

    /**
     * DAO-Objekt für CRUD-Operationen mit Profil-Objekte
     */
    private final ProfileDao profileDao;

    /**
     * DAO-Objekt für CRUD-Operationen mit Savestate-Objekten
     */
    private final SaveStateDao saveStateDao;

    /**
     * Das aktuelle Profil
     */
    private Profile currentProfile;

    /**
     * Splashscreen
     */
    private final SplashScreen splashScreen;

    /**
     * Das Spiel-Objekt, das unter anderem das Rendern übernimmt
     */
    private final ExmatrikulatorTD game;

    /**
     * Gameserver für den Multiplayer-ModusGame
     */
    private GameServer gameServer;

    /**
     * Gameclient für den Multiplayer-Modus
     */
    private GameClient gameClient;

    /**
     * Der Introbildschirm
     */
    private final Screen introScreen;

    /**
     * Gibt an, ob die Datenbank geladen ist
     */
    private boolean databaseLoaded;

    /**
     * Die aktuelle Hostadresse im Multiplayer
     */
    private String hostAdress = "";

    /**
     * Gibt an, ob die aktuelle Spielinstanz Host im Multiplayer-Modus ist
     */
    private boolean host;

    /**
     * Erzeugt einen neuen Maincontroller, das ein Game-Objekt verwaltet
     *
     * @param game Das mit dem Game-Controller zu assoziierende Game-Objekt
     */
    public MainController(ExmatrikulatorTD game) {
        this.game = game;
        System.out.println(game.getAssetManager());
        this.splashScreen = new SplashScreen(this, game.getAssetManager());
        this.introScreen = new IntroScreen(this, game.getAssetManager());
        this.profileDao = new ProfileDao();
        this.highScoreDao = new HighscoreDao();
        this.saveStateDao = new SaveStateDao();
        //
    }

    /**
     * Zeigt den Splash-Screen an
     */
    public void showSplashScreen() {
        game.setScreen(splashScreen);
    }

    /**
     * Zeigt das Hauptmenü an
     */
    public void showMenuScreen() {
        long profileID = game.getPreferences().getLong("profileID", -1L);
        if (profileID != -1L) {
            currentProfile = profileDao.retrieve(profileID);
        }
        if (currentProfile == null) {
            game.getPreferences().putLong("profileID", -1L);
            game.getPreferences().flush();
        }
        game.setScreen(new MenuScreen(this, game.getAssetManager()));
    }


    /**
     * Zeigt den Endbildschirm einer Spielpartie an
     *
     * @param gamestate Der Spielzustand, dessen Informationen angezeigt werden sollen
     */
    public void setEndScreen(Gamestate gamestate) {
        //game.setScreen(new EndScreen(this, game.getAssetManager(), gamestate));
        showMenuScreen();
    }

    public Profile getCurrentProfile() {
        return currentProfile;
    }

    public void setCurrentProfile(long profileId) {
        game.getPreferences().putLong("profileID", profileId);
        game.getPreferences().flush();
        this.currentProfile = profileDao.retrieve(profileId);
    }

    /**
     * Erzeugt ein neues Profil
     *
     * @param profileName    Der Name des Profils
     * @param profilePicture Das Bild des Profils
     */
    public void createNewProfile(String profileName, int preferredDifficulty, String profilePicture) {
        Profile profile = new Profile(profileName, preferredDifficulty, profilePicture);
        profileDao.create(profile);
        currentProfile = profile;
    }

    /**
     * Aktualisiert das aktuell ausgewählte Profil
     *
     * @param newProfileName Der neue Profilname
     * @param newDifficulty Der neue Schwierigkeitsgrad
     * @param newProfilePicturePath Das neue Profilbild
     */
    public void updateProfile(final String newProfileName, final int newDifficulty, final String newProfilePicturePath) {
        currentProfile.setProfileName(newProfileName);
        currentProfile.setPreferredDifficulty(newDifficulty);
        currentProfile.setProfilePicturePath(newProfilePicturePath);
        profileDao.update(currentProfile);
    }

    /**
     *
     * Löscht das Profil mit der angegebenen ID
     *
     * @param profileId Die ID des zu löschenden Profils
     */
    public void deleteProfile(final long profileId) {
        try{
            Profile profile = profileDao.retrieve(profileId);
            profileDao.delete(profile);
        } catch (final PersistenceException e){
            e.printStackTrace();
        }
    }

    /**
     * Erzeugt einen neuen GameServer
     */
    public void createServer() {
        this.gameServer = new GameServer();
        this.gameServer.setMainController(this);
        this.host = true;
    }

    /**
     * Erzeugt einen neuen GameClient
     */
    public void createClient() {
        if (gameClient == null) {
            this.gameClient = new GameClient();
            this.gameClient.setMainController(this);
        }
        host = false;
    }

    /**
     *
     * Holt Informationen über lokale Spielserver vom GameClient, enkodiert in einen String
     *
     * @return Die Informationen über lokale Spielserver als String
     */
    public List<String> getLocalGameServers() {
        gameClient.discoverLocalServers();
        return gameClient.getReceivedSessionInfo();
    }

    /**
     * Schließt alle Verbindungen mit GameClients oder GameServern
     */
    public void shutdownConnections() {
        if (gameClient != null) {
            gameClient.shutdown();
            gameClient = null;
        }
        if (gameServer != null) {
            gameServer.shutdown();
            gameServer = null;
        }

    }

    /**
     * Startet den GameServer
     */
    public void startServer() {
        gameServer.startServer(2, currentProfile.getPreferredDifficulty());
    }

    /**
     * Verbindet den GameClient mit einem GameServer
     *
     * @param host Die IP-Adresse des Servers
     */
    public boolean connectToServer(String host) {
        return gameClient.connect(host);
    }

    /**
     * Holt alle Spielstände aus der Datenbank
     *
     * @return Eine Liste aller in der Datenbank gespeicherten Spielstände
     */
    public List<SaveState> getAllSavestates() {
        return saveStateDao.findAllSaveStates();
    }

    /**
     * Holt alle Spielstände für das aktuell ausgewählte Profil aus der Datenbank
     *
     * @return Eine Liste aller in der Datenbank gespeicherten Spielstände für das aktuell ausgewählte Profil
     */
    public List<SaveState> getSaveStatesForCurrentProfile() {
        return saveStateDao.findSaveStatesForProfile(currentProfile);
    }

    /**
     * Erstellt ein neues Einzelspieler-Spiel
     */
    public void createNewSinglePlayerGame(int gamemode, int difficulty, String mapPath) {
        GameView gameScreen = new GameScreen(this, game.getAssetManager());
        String[] names = { currentProfile.getProfileName() };
        new GameLogicController(this, difficulty, 1, 0, gamemode, gameScreen, mapPath, names);
        showScreen(gameScreen);
    }

    /**
     * Lädt ein Einzelspieler-Spiel
     *
     * @param idToLoad Die Datenbank-ID des Spielzustandes, der geladen werden soll
     */
    public void loadSinglePlayerGame(Long idToLoad) {
        SaveState saveState = saveStateDao.retrieve(idToLoad);
        GameView gameScreen = new GameScreen(this, game.getAssetManager());
        new GameLogicController(this, saveState, gameScreen);
        showScreen(gameScreen);
    }

    /**
     * Erzeugt ein neues Multiplayer-Spiel als Client
     */
    public void createNewMultiplayerClientGame(int numberOfPlayers, int allocatedPlayerNumber, int difficulty, int gamemode, String mapPath, String[] names) {
        GameView gameScreen = new GameScreen(this, game.getAssetManager());
        new ClientGameLogicController(this, difficulty, numberOfPlayers, allocatedPlayerNumber, gamemode, gameScreen, mapPath, gameClient, names);
    }

    /**
     * Lädt ein Multiplayerspiel als Client
     */
    public void loadMultiPlayerClientGame(Gamestate gamestate, int allocatedPlayerNumber, String mapPath) {
        GameView gameScreen = new GameScreen(this, game.getAssetManager());
        new ClientGameLogicController(this, gamestate, allocatedPlayerNumber, gameScreen, mapPath, gameClient);
        //showScreen(gameScreen);
    }

    /**
     * Erzeugt ein Multiplayer-Spiel als Server
     */
    public void createNewMultiplayerServerGame(int numberOfPlayers, int difficulty, int allocatedPlayerNumber, int gamemode, String mapPath, String[] names) {
        GameView gameScreen = new GameScreen(this, game.getAssetManager());
        new GameLogicController(this, difficulty, numberOfPlayers, allocatedPlayerNumber, gamemode, gameScreen, mapPath, gameServer, names);
    }

    /**
     * Lädt ein Multiplayerspiel als Server
     *
     * @param saveState Der Spielzustand, der geladen werden soll
     */
    public void loadMultiPlayerServerGame(Gamestate gamestate, int allocatedPlayerNumber, String mapPath) {
        GameView gameScreen = new GameScreen(this, game.getAssetManager());
        new GameLogicController(this, gamestate, allocatedPlayerNumber, gameScreen, mapPath, gameServer);
    }

    private List<Profile> retrieveProfiles() {
        return profileDao.findAllProfiles();
    }

    public Profile retrieveProfile(long id) {
        return profileDao.retrieve(id);
    }

    public boolean noProfilesYet() {
        return retrieveProfiles().isEmpty();
    }

    /**
     * Holt eine bestimmte Anzahl der höchsten HighScores aus der Datenbank
     *
     * @param limit Die Anzahl der HighScores, die aus der Datenbank geholt werden sollen
     * @return Die Liste der HighScores
     */
    public List<Highscore> retrieveHighscores(int limit) {
        return highScoreDao.findHighestScores(limit);
    }

    /**
     * Zeigt einen Screen an
     *
     * @param screen Der anzuzeigende Screen
     */
    public void showScreen(Screen screen) {
        game.setScreen(screen);
    }

    /**
     * Zeigt den Introbildschirm an
     */
    public void showIntroScreen() {
        game.setScreen(introScreen);
    }

    /**
     * Gibt Auskunft darüber, ob die Datenbank geladen ist
     *
     * @return true, wenn die Datenbank geladen ist, ansonsten false
     */
    public boolean isDatabaseLoaded() {
        return databaseLoaded;
    }

    /**
     * Setzt die Variable, die Auskunft darüber gibt, ob die Datenbank geladen ist, auf true oder false
     *
     * @param databaseLoaded Der neue Wahrheitswert
     */
    public void setDatabaseLoaded(boolean databaseLoaded) {
        this.databaseLoaded = databaseLoaded;
    }

    /**
     * Gibt die aktuelle HostAdresse zurück
     * @return Die Hostadresse
     */
    public String getHostAdress() {
        return hostAdress;
    }

    public void setHostAdress(String hostAdress) {
        this.hostAdress = hostAdress;
    }

    /**
     * Ändert den Bereitschaftszustand der Spielinstanz im Multiplayer und schickt als Client eine entsprechende
     * Nachricht an den Server
     */
    public void toggleReady() {
        if (host) {
            gameServer.setServerReady();
        } else {
            gameClient.reportReadiness();
        }
    }

    /**
     * Gibt das HighScoreDao des Controllers zurück
     *
     * @return Das HighScoreDao
     */
    public HighscoreDao getHighScoreDao() {
        return highScoreDao;
    }

    /**
     * Aktualisiert die Profilauswahl-Buttons eines MenuScreens
     * @param menuScreen Der MenuScreen, dessen Profilauswahl-Buttons aktualisiert werden sollen
     */
    public void updateProfileButtons(MenuScreen menuScreen) {
        for (final Profile profile : profileDao.findAllProfiles()) {

            boolean isCurrentProfile = false;

            if (currentProfile != null) {
                if (currentProfile.getId().equals(profile.getId())) {
                    isCurrentProfile = true;
                }
            }

            menuScreen.addProfileButton(profile.getProfileName(), profile.getId(), isCurrentProfile);
        }
    }

    public boolean hasCurrentProfile() {

        return currentProfile != null;
    }

    public String getCurrentProfileName() {
        return currentProfile.getProfileName();
    }

    public int getCurrentProfilePreferredDifficulty() {
        return currentProfile.getPreferredDifficulty();
    }

    public String getCurrentProfilePicturePath() {
        return currentProfile.getProfilePicturePath();
    }

    public void updateSaveStateButtons(MenuScreen menuScreen) {

        for (SaveState saveState : saveStateDao.findAllSaveStates()) {
            menuScreen.addSaveStateButton("Player name:" + saveState.getProfile().getProfileName() + "\n" + saveState.getSaveStateName() + "\nSaved: " + saveState.getSaveDate().toString(),
                    saveState.getId());
        }
    }

    public void deleteSaveState(Long idToLoad) {
        SaveState saveState = saveStateDao.retrieve(idToLoad);
        saveStateDao.delete(saveState);
    }
}
