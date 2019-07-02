package de.diegrafen.exmatrikulatortd.controller.gamelogic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import de.diegrafen.exmatrikulatortd.controller.MainController;
import de.diegrafen.exmatrikulatortd.model.Coordinates;
import de.diegrafen.exmatrikulatortd.model.Gamestate;
import de.diegrafen.exmatrikulatortd.model.Player;
import de.diegrafen.exmatrikulatortd.model.Profile;
import de.diegrafen.exmatrikulatortd.model.enemy.Debuff;
import de.diegrafen.exmatrikulatortd.model.enemy.Enemy;
import de.diegrafen.exmatrikulatortd.model.enemy.Wave;
import de.diegrafen.exmatrikulatortd.model.tower.*;
import de.diegrafen.exmatrikulatortd.persistence.*;
import de.diegrafen.exmatrikulatortd.util.DistanceComparator;
import de.diegrafen.exmatrikulatortd.view.screens.GameScreen;

import java.awt.geom.Point2D;
import java.util.*;

import static de.diegrafen.exmatrikulatortd.controller.factories.TowerFactory.REGULAR_TOWER;
import static de.diegrafen.exmatrikulatortd.controller.factories.TowerFactory.createNewTower;
import static de.diegrafen.exmatrikulatortd.util.Assets.FIREBALL_ASSETS;
import static de.diegrafen.exmatrikulatortd.util.Constants.*;

/**
 * Der Standard-Spiellogik-Controller.
 *
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 15.06.2019 01:24
 */
public class GameLogicController implements LogicController {

    /**
     * Der Hauptcontroller
     */
    private MainController mainController;

    /**
     * Der Spielzustand
     */
    private Gamestate gamestate;

    /**
     * Der Spielbildschirm
     */
    private GameScreen gameScreen;

    /**
     * Das lokale Spieler-Profil
     */
    private Profile profile;

    /**
     * DAO für CRUD-Operationen mit Spielzustand-Objekten
     */
    private GameStateDao gameStateDao;

    /**
     * DAO für CRUD-Operationen mit Spielstand-Objekten
     */
    private SaveStateDao saveStateDao;


    private float auraRefreshTimer = 0;


    public GameLogicController(MainController mainController, Gamestate gamestate, Profile profile) {
        this.mainController = mainController;
        this.gamestate = gamestate;
        this.profile = profile;
        this.gameStateDao = new GameStateDao();
        this.saveStateDao = new SaveStateDao();
        gamestate.addPlayer(new Player());
        gamestate.setLocalPlayerNumber(0);
        gameStateDao.create(gamestate);
    }

    @Override
    public void update(float deltaTime) {
        // FIXME: Bestimmung, wann das Spiel zuende ist, fixen
        if (!determineGameOver()) {
            if (gamestate.getRoundNumber() < gamestate.getNumberOfRounds()) {
                determineNewRound();
                if (gamestate.isRoundEnded()) {
                    gamestate.setRoundEnded(false);
                    System.out.println("Runde zuende!");
                    startNewRound();
                }
            }
            //applyPlayerDamage();
            spawnWave(deltaTime);
            applyAuras(deltaTime);
            applyMovement(deltaTime);
            makeAttacks(deltaTime);
            moveProjectiles(deltaTime);
            applyBuffsAndDebuffs(deltaTime);
        }
    }

    /**
     * Wendet Auras auf die Objekte des Spiels an
     *
     * @param deltaTime Die Zeit, die seit dem Rendern des letzten Frames vergangen ist
     */
    private void applyAuras(float deltaTime) {

        if (auraRefreshTimer > 0) {
            auraRefreshTimer -= deltaTime;
            return;
        } else {
            auraRefreshTimer = AURA_REFRESH_RATE;
            auraRefreshTimer -= deltaTime;
        }

        for (Tower tower : gamestate.getTowers()) {
            List<Aura> auras = tower.getAuras();
            for (Aura aura : auras) {
                List<Debuff> debuffs = aura.getDebuffs();
                List<Buff> buffs = aura.getBuffs();

                for (Debuff debuff : debuffs) {
                    List<Enemy> enemiesInRange = getEnemiesInRange(tower, aura.getRange());
                    for (Enemy enemyInRange : enemiesInRange) {
                        addDebuffToEnemy(enemyInRange, debuff);
                    }
                }

                for (Buff buff : buffs) {
                    List<Tower> towersInRange = getTowersInRange(tower.getxPosition(), tower.getyPosition(), aura.getRange());
                    for (Tower towerInRange : towersInRange) {
                        boolean buffAlreadyApplied = false;
                        for (Buff buffToCheck : towerInRange.getBuffs()) {
                            if (buff.getName().equals(buffToCheck.getName())) {
                                buffAlreadyApplied = true;
                                break;
                            }
                        }
                        if (!buffAlreadyApplied) {
                            towerInRange.addBuff(new Buff(buff));
                        }
                    }
                }
            }
        }
    }

    private void addDebuffToEnemy(Enemy enemy, Debuff debuff) {
        boolean debuffAlreadyApplied = false;
        for (Debuff debuffToCheck : enemy.getDebuffs()) {
            if (debuff.getName().equals(debuffToCheck.getName())) {
                debuffAlreadyApplied = true;
                break;
            }
        }
        if (!debuffAlreadyApplied) {
            enemy.addDebuff(new Debuff(debuff));
        }
    }

    // TODO: System zum Ermitteln von Türmen und Gegnern verbessern
    private List<Tower> getTowersInRange(float xPosition, float yPosition, float range) {
        List<Tower> towersInRange = new LinkedList<>();

        for (Tower tower : gamestate.getTowers()) {

            double distance = Point2D.distance(xPosition, yPosition, tower.getxPosition(), tower.getyPosition());

            if (distance <= range) {
                towersInRange.add(tower);
            }
        }

        return towersInRange;
    }

    private List<Enemy> getEnemiesInRange(Tower tower, float range) {
        List<Enemy> enemiesInRange = new LinkedList<>();

        for (Enemy enemy : tower.getOwner().getAttackingEnemies()) {

            double distance = Point2D.distance(tower.getxPosition(), tower.getyPosition(), enemy.getxPosition(), enemy.getyPosition());

            if (distance <= range) {
                enemiesInRange.add(enemy);
            }
        }

        return enemiesInRange;
    }

    /**
     * Wendet Buffs und Debuffs auf die Objekte des Spiels an
     *
     * @param deltaTime Die Zeit, die seit dem Rendern des letzten Frames vergangen ist
     */
    private void applyBuffsAndDebuffs(float deltaTime) {

        for (Tower tower : gamestate.getTowers()) {

            List<Buff> buffsToRemove = new LinkedList<>();

            tower.setCurrentAttackDamage(tower.getBaseAttackDamage());
            tower.setCurrentAttackSpeed(tower.getBaseAttackSpeed());

            for (Buff buff : tower.getBuffs()) {
                tower.setCurrentAttackSpeed(tower.getCurrentAttackSpeed() * (1 + buff.getAttackSpeedModifier()));
                tower.setCurrentAttackDamage(tower.getCurrentAttackDamage() * (1 + buff.getAttackDamageModifier()));
                buff.setDuration(buff.getDuration() - deltaTime);
                if (buff.getDuration() < 0) {
                    buffsToRemove.add(buff);
                }
            }

            buffsToRemove.forEach(tower::removeBuff);
        }

        for (Enemy enemy : gamestate.getEnemies()) {

            List<Debuff> debuffsToRemove = new LinkedList<>();

            enemy.setCurrentSpeed(enemy.getBaseSpeed());
            enemy.setCurrentArmor(enemy.getBaseArmor());
            // TODO: Irgendwas mit Leben überlegen. Vielleicht stattdessen eher Damage over Time Debuffs ergänzen.

            for (Debuff debuff : enemy.getDebuffs()) {
                enemy.setCurrentSpeed(enemy.getCurrentSpeed() * (1 + debuff.getSpeedModifier()));
                enemy.setCurrentArmor(enemy.getCurrentArmor() + debuff.getArmorModifier());
                debuff.setDuration(debuff.getDuration() - deltaTime);
                if (debuff.getDuration() < 0) {
                    debuffsToRemove.add(debuff);
                }
            }
            debuffsToRemove.forEach(enemy::removeDebuff);
        }
    }


    /**
     * Bewegt die Einheiten
     *
     * @param deltaTime Die Zeit, die seit dem Rendern des letzten Frames vergangen ist
     */
    private void applyMovement(float deltaTime) {
        for (Enemy enemy : gamestate.getEnemies()) {
            if (Math.floor(getDistanceToNextPoint(enemy)) <= 3) {
                enemy.incrementWayPointIndex();
            }
            if (enemy.getWayPointIndex() >= enemy.getAttackedPlayer().getWayPoints().size()) {
                applyDamageToPlayer(enemy);
                if (enemy.isRespawning()) {
                    addEnemy(new Enemy(enemy));
                }
                break;
            }

            moveInTargetDirection(enemy, deltaTime);
            enemy.notifyObserver();

//            if (enemy.getCurrentMapCell() != null) {
//                enemy.getCurrentMapCell().removeFromEnemiesOnCell(enemy);
//            }
//            Coordinates newCell = getMapCellByXandY((int) enemy.getxPosition(), (int) enemy.getyPosition());
//            enemy.setCurrentMapCell(newCell);
//            newCell.addToEnemiesOnCell(enemy);
        }
    }

    /**
     * Bewegt die Geschosse
     *
     * @param deltaTime Die Zeit, die seit dem Rendern des letzten Frames vergangen ist
     */
    private void moveProjectiles(float deltaTime) {
        List<Projectile> projectilesThatHit = new ArrayList<>();
        List<Projectile> projectilesWithoutTarget = new LinkedList<>();

        for (Projectile projectile : gamestate.getProjectiles()) {
            //if (projectile.getTarget().isRemoved()) {
            //projectilesWithoutTarget.add(projectile);
            //continue;
            //}
            if (Math.floor(getDistanceToTarget(projectile)) <= 3) {
                projectilesThatHit.add(projectile);
                continue;
            }
            moveInTargetDirection(projectile, deltaTime);
        }

        //for (Projectile projectile : projectilesWithoutTarget) {
        //removeProjectile(projectile);
        //}

        for (Projectile projectile : projectilesThatHit) {
            applyDamageToTarget(projectile);
        }
    }


    private void moveInTargetDirection(Enemy enemy, float deltaTime) {
        Coordinates nextWayPoint = enemy.getAttackedPlayer().getWayPoints().get(enemy.getWayPointIndex());
        int tileSize = gamestate.getTileSize();

        float xPosition = enemy.getxPosition();
        float yPosition = enemy.getyPosition();
        float targetxPosition = nextWayPoint.getXCoordinate() * tileSize;
        float targetyPosition = nextWayPoint.getYCoordinate() * tileSize;
        float currentSpeed = enemy.getCurrentSpeed();

        float angle = (float) Math.atan2(targetyPosition - yPosition, targetxPosition - xPosition);

        enemy.setTargetxPosition(targetxPosition);// + tileSize / 2;
        enemy.setTargetyPosition(targetyPosition);// + tileSize / 2;
        enemy.setxPosition(xPosition + (float) Math.cos(angle) * currentSpeed * deltaTime);
        enemy.setyPosition(yPosition + (float) Math.sin(angle) * currentSpeed * deltaTime);
    }

    private void moveInTargetDirection(Projectile projectile, float deltaTime) {

        float xPosition = projectile.getxPosition();
        float yPosition = projectile.getyPosition();
        float targetxPosition = projectile.getTarget().getxPosition();
        float targetyPosition = projectile.getTarget().getyPosition();

        float speed = projectile.getSpeed();

        float angle = (float) Math.atan2(targetyPosition - yPosition, targetxPosition - xPosition);
        projectile.setxPosition(xPosition + (float) Math.cos(angle) * speed * deltaTime);
        projectile.setyPosition(yPosition + (float) Math.sin(angle) * speed * deltaTime);
        projectile.setTargetxPosition(targetxPosition);
        projectile.setTargetyPosition(targetyPosition);
        projectile.notifyObserver();
    }

    private void applyDamageToTarget(Projectile projectile) {
        List<Enemy> enemiesHit = new LinkedList<>();
        Enemy mainTarget = projectile.getTarget();
        mainTarget.setCurrentHitPoints(mainTarget.getCurrentHitPoints() - projectile.getDamage() * calculateDamageMultiplier(mainTarget, projectile.getAttackType()));
        enemiesHit.add(mainTarget);

        if (projectile.getSplashRadius() > 0) {
            List<Enemy> enemiesInSplashRadius = getEnemiesInRange(projectile.getTowerThatShot(), projectile.getSplashRadius());
            for (Enemy enemyInSplashRadius : enemiesInSplashRadius) {
                float damageMultiplier = calculateDamageMultiplier(enemyInSplashRadius, projectile.getAttackType());
                // TODO: Splash-Percentage ist keine gute Bezeichnung.
                enemyInSplashRadius.setCurrentHitPoints(enemyInSplashRadius.getCurrentHitPoints() - projectile.getDamage() * projectile.getSplashPercentage() * damageMultiplier);
                enemiesHit.add(enemyInSplashRadius);
            }
        }

        for (Enemy enemyHit : enemiesHit) {
            for (Debuff debuff : projectile.getApplyingDebuffs()) {
                addDebuffToEnemy(enemyHit, debuff);
            }
            // TODO: In eigene Funktion auslagern
            if (enemyHit.getCurrentHitPoints() <= 0) {
                Player attackedPlayer = enemyHit.getAttackedPlayer();
                if (attackedPlayer != null) {
                    attackedPlayer.addToResources(enemyHit.getBounty());
                    attackedPlayer.addToScore(enemyHit.getPointsGranted());
                    attackedPlayer.notifyObserver();
                    removeEnemy(enemyHit);
                }
            }
        }
        removeProjectile(projectile);
    }

    private void removeProjectile(Projectile projectileToRemove) {
        projectileToRemove.setRemoved(true);
        projectileToRemove.notifyObserver();
        gamestate.removeProjectile(projectileToRemove);
    }

    private float calculateDamageMultiplier(Enemy enemy, int attackType) {
        float armor = enemy.getCurrentArmor();
        float damageModifier = 1;

        // TODO: Eindeutigeren Namen hinzufügen
        float armorEffect = ATTACK_DEFENSE_MATRIX[attackType][enemy.getArmorType()];

        if (armor > 0) {
            damageModifier = 1 - (armor * DAMAGE_REDUCTION_FACTOR) / (1 + DAMAGE_REDUCTION_FACTOR * armor);
        } else if (armor < 0) {
            damageModifier = 2 - (float) Math.pow(0.94, -armor);
        }

        return armorEffect * damageModifier;
    }

    private float getDistanceToTarget(Projectile projectile) {
        float x1 = projectile.getxPosition();
        float x2 = projectile.getTarget().getxPosition();
        float y1 = projectile.getyPosition();
        float y2 = projectile.getTarget().getyPosition();
        return (float) Point2D.distance(x1, y1, x2, y2);
    }

    /**
     * Lässt die Türme angreifen
     *
     * @param deltaTime Die Zeit, die seit dem Rendern des letzten Frames vergangen ist
     */
    private void makeAttacks(float deltaTime) {

        for (Tower tower : gamestate.getTowers()) {

            if (tower.getCooldown() > 0) {
                tower.setCooldown(tower.getCooldown() - deltaTime);
            }

            Enemy newTarget = null;

            if (!targetInRange(tower)) {
                tower.setCurrentTarget(null);
                float timeSinceLastSearch = tower.getTimeSinceLastSearch();
                if (timeSinceLastSearch >= SEARCH_TARGET_INTERVAL) {
                    List<Enemy> enemiesInRange = getEnemiesInRange(tower, tower.getAttackRange());
                    enemiesInRange.sort(new DistanceComparator(tower.getxPosition(), tower.getyPosition()));
                    if (!enemiesInRange.isEmpty()) {
                        newTarget = enemiesInRange.get(0);
                    }
                    tower.setCurrentTarget(newTarget);
                    tower.setTimeSinceLastSearch(0);
                } else {
                    tower.setTimeSinceLastSearch(timeSinceLastSearch + deltaTime);
                }
            }

            if (tower.getCurrentTarget() != null) {
                letTowerAttack(tower);
            }
        }
    }

    private void letTowerAttack(Tower tower) {
        Enemy enemy = tower.getCurrentTarget();
        if (tower.getCooldown() <= 0) {
            switch (tower.getAttackStyle()) {
                case PROJECTILE:
                    Projectile projectile = new Projectile("Feuerball", FIREBALL_ASSETS, EXPLOSIVE, tower.getCurrentAttackDamage(),
                            0.5f, 100, 300);
                    projectile.addDebuff(new Debuff("Frost-Debuff", 3, -5, -0.5f, -0.5f));
                    addProjectile(projectile, tower);
                    break;
                case IMMEDIATE: //TODO: Animationen wie Blitze oder Ähnliches triggern lassen.
                    enemy.setCurrentHitPoints(enemy.getCurrentHitPoints() - tower.getCurrentAttackDamage());
                    if (enemy.getCurrentHitPoints() <= 0) {
                        tower.getOwner().addToResources(enemy.getBounty());
                        tower.getOwner().addToScore(enemy.getPointsGranted());
                        tower.getOwner().notifyObserver();
                        removeEnemy(enemy);
                        tower.setCurrentTarget(null);
                    }
                    break;
            }

            tower.setCooldown(tower.getCurrentAttackSpeed());
        }
    }

    private void applyDamageToPlayer(Enemy enemy) {
        Player attackedPlayer = enemy.getAttackedPlayer();
        attackedPlayer.setCurrentLives(attackedPlayer.getCurrentLives() - enemy.getAmountOfDamageToPlayer());
        removeEnemy(enemy);
    }

    private void removeEnemy(Enemy enemy) {
        enemy.getAttackedPlayer().removeEnemy(enemy);
        enemy.setAttackedPlayer(null);
        //enemy.getCurrentMapCell().removeFromEnemiesOnCell(enemy);
        //enemy.setCurrentMapCell(null);
        enemy.setDebuffs(new LinkedList<>());
        gamestate.removeEnemy(enemy);
        enemy.setGameState(null);
        enemy.setRemoved(true);
        enemy.notifyObserver();
    }

    private float getDistanceToNextPoint(Enemy enemy) {
        float x1 = enemy.getxPosition();
        float x2 = enemy.getTargetxPosition();
        float y1 = enemy.getyPosition();
        float y2 = enemy.getTargetyPosition();
        return (float) Point2D.distance(x1, y1, x2, y2);
    }

    private boolean isCellInRangeOfTower(Tower tower, Coordinates coordinates) {

        if (coordinates == null) {
            return false;
        }

        Coordinates towerCoordinates = tower.getPosition();

        float x1 = towerCoordinates.getXCoordinate() * TILE_SIZE;
        float x2 = coordinates.getXCoordinate() * TILE_SIZE;
        float y1 = towerCoordinates.getXCoordinate() * TILE_SIZE;
        float y2 = coordinates.getYCoordinate() * TILE_SIZE;
        float distance = (float) Point2D.distance(x1, y1, x2, y2);
        return tower.getAttackRange() >= distance;
    }

    private boolean targetInRange(Tower tower) {

        Enemy target = tower.getCurrentTarget();

        return target != null && !target.isRemoved() && isCellInRangeOfTower(tower, target.getCurrentMapCell());
    }

    /**
     * Spawnt die nächste Angriffswelle
     */
    private void spawnWave(float deltaTime) {
        float timeUntilNextRound = gamestate.getTimeUntilNextRound();
        if (timeUntilNextRound <= 0) {

            int roundNumber = gamestate.getRoundNumber();

            for (Player player : gamestate.getPlayers()) {
                List<Wave> waves = player.getWaves();
                if (waves.size() <= roundNumber) {
                    // TODO: Mehrspieler*innen-Szenario berücksichtigen
                    gamestate.setGameOver(true);
                    break;
                } else if (waves.get(roundNumber).getEnemies().isEmpty()) {
                    player.setEnemiesSpawned(true);
                    gamestate.setNewRound(false);
                    break;
                }

                if (!player.isEnemiesSpawned() && player.getTimeSinceLastSpawn() > TIME_BETWEEN_SPAWNS) {
                    Enemy enemy = player.getWaves().get(roundNumber).getEnemies().get(0);
                    // TODO: Replace with .pop()
                    player.getWaves().get(roundNumber).getEnemies().remove(0);
                    addEnemy(enemy);
                    player.setTimeSinceLastSpawn(0);
                } else {
                    player.setTimeSinceLastSpawn(player.getTimeSinceLastSpawn() + deltaTime);
                }
            }
        }

        gamestate.setTimeUntilNextRound(timeUntilNextRound - deltaTime);
    }

    /**
     * Stellt fest, ob die Runde zuende ist
     */
    private void determineNewRound() {
        if (gamestate.getEnemies().isEmpty() && !gamestate.isNewRound()) {
            gamestate.setRoundEnded(true);
            gamestate.setRoundNumber(gamestate.getRoundNumber() + 1);


            List<Projectile> remainingProjectiles = new LinkedList<>(gamestate.getProjectiles());
            remainingProjectiles.forEach(this::removeProjectile);

            gamestate.notifyObserver();
            gameStateDao.update(gamestate);
        }
    }

    private boolean determineGameOver() {
        boolean gameOver = true;
        for (Player player : gamestate.getPlayers()) {
            if (!(player.getWaves().size() <= gamestate.getRoundNumber())) {
                gameOver = false;
            }
        }
        gamestate.setGameOver(gameOver);
        return gameOver;
    }

    /**
     * Startet eine neue Runde
     */
    private void startNewRound() {
        System.out.println("New round started!");
        gamestate.setNewRound(true);
        gamestate.setRoundEnded(false);
        for (Player player : gamestate.getPlayers()) {
            player.setEnemiesSpawned(false);
        }
        gamestate.setTimeUntilNextRound(TIME_BETWEEN_ROUNDS);
    }

    /**
     * Initialisiert die Kollisionsmatrix der Karte
     *
     * @param mapPath Der Dateipfad der zu ladenden Karte
     */
    public void initializeCollisionMap(String mapPath) {

        TiledMap tiledMap = new TmxMapLoader().load(mapPath);

        gameScreen.loadMap(mapPath);

        TiledMapTileLayer tiledMapTileLayer = (TiledMapTileLayer) tiledMap.getLayers().get("Kollisionsmatrix");

        int numberOfColumns = tiledMapTileLayer.getWidth();
        int numberOfRows = tiledMapTileLayer.getHeight();

        gamestate.setNumberOfColumns(numberOfColumns);
        gamestate.setNumberOfRows(numberOfRows);

        for (int i = 0; i < numberOfColumns; i++) {
            for (int j = 0; j < numberOfRows; j++) {
                int buildableByPlayer = (int) tiledMapTileLayer.getCell(i, j).getTile().getProperties().get("buildableByPlayer");
                addGameMapTile(i, j, buildableByPlayer);
            }
        }

        for (Coordinates mapCell : gamestate.getCollisionMatrix()) {

            int mapCellXCoordinate = mapCell.getxCoordinate();
            int mapCellYCoordinate = mapCell.getyCoordinate();

            int numberOfCols = gamestate.getNumberOfColumns();
            if (mapCellXCoordinate > 0) {
                mapCell.addNeighbour(gamestate.getMapCellByListIndex(numberOfCols * (mapCellXCoordinate - 1) + mapCellYCoordinate));
            }
            if (mapCellXCoordinate < numberOfColumns - 1) {
                mapCell.addNeighbour(gamestate.getMapCellByListIndex(numberOfCols * (mapCellXCoordinate + 1) + mapCellYCoordinate));
            }
            if (mapCellYCoordinate > 0) {
                mapCell.addNeighbour(gamestate.getMapCellByListIndex(numberOfCols * mapCellXCoordinate + mapCellYCoordinate - 1));
            }
            if (mapCellYCoordinate < numberOfColumns - 1) {
                mapCell.addNeighbour(gamestate.getMapCellByListIndex(numberOfCols * mapCellXCoordinate + mapCellYCoordinate + 1));
            }
        }
    }

    /**
     * Fügt der im Spielzustand vorhandenen Karte ein neues Feld hinzu
     *
     * @param xCoordinate       Die x-Koordinate des Feldes auf der Karte
     * @param yCoordinate       Die y-Koordinate des Feldes auf der Karte
     * @param buildableByPlayer Die Nummer der Spielerin, die auf dem Feld bauen darf. -1, wenn das Feld nicht bebaubar ist
     */
    private void addGameMapTile(int xCoordinate, int yCoordinate, int buildableByPlayer) {
        Coordinates coordinates = new Coordinates(xCoordinate, yCoordinate, buildableByPlayer);
        gamestate.addCoordinatesToCollisionMatrix(coordinates);
    }

    /**
     * Gibt die y-Koordinate zurück, die der angegebenen x-Position auf der Karte entspricht
     *
     * @param xPosition Die x-Position, für die die x-Koordinate ermittelt werden soll
     * @return Die passende x-Koordinate
     */
    public int getXCoordinateByPosition(float xPosition) {
        return (int) xPosition / gamestate.getTileSize();
    }

    /**
     * Gibt die y-Koordinate zurück, die der angegebenen y-Position auf der Karte entspricht
     *
     * @param yPosition Die y-Position, für die die y-Koordinate ermittelt werden soll
     * @return Die passende y-Koordinate
     */
    public int getYCoordinateByPosition(float yPosition) {
        return (int) yPosition / gamestate.getTileSize();
    }

    @Override
    public void buildFailed() {
        gameScreen.displayErrorMessage("Bauen fehlgeschlagen.");
    }

    @Override
    public void sendFailed() {
        gameScreen.displayErrorMessage("Gegner senden fehlgeschlagen.");
    }

    @Override
    public void upgradeFailed() {
        gameScreen.displayErrorMessage("Turmausbau fehlgeschlagen.");
    }

    private Coordinates getMapCellByXandYCoordinates(int xCoordinate, int yCoordinate) {

        xCoordinate *= gamestate.getNumberOfColumns();

        return gamestate.getMapCellByListIndex(xCoordinate + yCoordinate);
    }

    private Coordinates getMapCellByXandY(float xPosition, float yPosition) {
        int xCoordinate = (int) xPosition / TILE_SIZE;
        int yCoordinate = (int) yPosition / TILE_SIZE;

        int xIndex = gamestate.getNumberOfColumns() * xCoordinate;

        return gamestate.getMapCellByListIndex(xIndex + yCoordinate);
    }

    private void addEnemy(Enemy enemy) {
        Player attackedPlayer = gamestate.getPlayerByNumber(0);
        attackedPlayer.addEnemy(enemy);
        enemy.setAttackedPlayer(attackedPlayer);
        gamestate.addEnemy(enemy);
        enemy.setGameState(gamestate);
        setEnemyToStartPosition(enemy);
        gameScreen.addEnemy(enemy);
        enemy.notifyObserver();
    }

    /**
     * Setzt einen Gegner an seine Startposition (zurück)
     *
     * @param enemy
     */
    private void setEnemyToStartPosition(Enemy enemy) {
        Coordinates startCoordinates = enemy.getAttackedPlayer().getWayPoints().get(0);
        int tileSize = gamestate.getTileSize();
        enemy.setxPosition(startCoordinates.getXCoordinate() * tileSize);// + tileSize / 2;
        enemy.setyPosition(startCoordinates.getYCoordinate() * tileSize);// + tileSize / 2;
    }

    private void addTower(Tower tower, int xPosition, int yPosition) {
        Player owningPlayer = gamestate.getPlayerByNumber(0);
        tower.setOwner(owningPlayer);
        owningPlayer.addTower(tower);

        Coordinates coordinates = getMapCellByXandYCoordinates(xPosition, yPosition);
        tower.setPosition(coordinates);
        coordinates.setTower(tower);

        tower.setGamestate(gamestate);
        gamestate.addTower(tower);
        gameScreen.addTower(tower);
    }

    private void addProjectile(Projectile projectile, Tower tower) {
        projectile.setxPosition(tower.getxPosition());
        projectile.setyPosition(tower.getyPosition());
        projectile.setTarget(tower.getCurrentTarget());
        projectile.setTowerThatShot(tower);
        projectile.setTargetxPosition(tower.getCurrentTarget().getxPosition());
        projectile.setTargetyPosition(tower.getCurrentTarget().getyPosition());

        gamestate.addProjectile(projectile);
        gameScreen.addProjectile(projectile);
    }

    /**
     * Baut einen neuen Turm an den angegebenen Koordinaten auf der Karte
     *
     * @param towerType    Der Typ des zu bauenden Turms
     * @param xCoordinate  Die x-Koordinate der Stelle, an der der Turm gebaut werden soll
     * @param yCoordinate  Die y-Koordinate der Stelle, an der der Turm gebaut werden soll
     * @param playerNumber Die Nummer der Spielerin, die den Turm bauen will
     * @return Wenn das Bauen erfolgreich war, true, ansonsten false
     */
    @Override
    public boolean buildTower(int towerType, int xCoordinate, int yCoordinate, int playerNumber) {

        boolean wasSuccessful = false;

        if (checkIfCoordinatesAreBuildable(xCoordinate, yCoordinate, playerNumber)) {
            Tower tower = createNewTower(towerType);
            int towerPrice = tower.getPrice();
            Player player = gamestate.getPlayerByNumber(playerNumber);
            int playerResources = player.getResources();
            if (playerResources >= towerPrice) {
                player.setResources(playerResources - towerPrice);
                addTower(tower, xCoordinate, yCoordinate);
                player.notifyObserver();
                wasSuccessful = true;
            }
        }

        return wasSuccessful;
    }

    public void buildRegularTower(final int mapX, final int mapY) {
        buildTower(REGULAR_TOWER, mapX, mapY, 0);
    }

    public boolean checkIfCoordinatesAreBuildable(int xCoordinate, int yCoordinate, int playerNumber) {

        boolean buildable = true;

        String errormessage = "";

        if (xCoordinate < 0 || xCoordinate >= gamestate.getNumberOfColumns()) {
            errormessage = "Es nicht erlaubt, außerhalb des Spielfelds zu bauen!";
            buildable = false;
        } else if (yCoordinate < 0 || yCoordinate >= gamestate.getNumberOfRows()) {
            errormessage = "Es nicht erlaubt, außerhalb des Spielfelds zu bauen!";
            buildable = false;
        } else if (playerNumber < 0 || gamestate.getPlayers().size() <= playerNumber) {
            errormessage = "Die Spielernummer ist nicht bekannt!";
            buildable = false;
        } else {
            Coordinates mapCell = getMapCellByXandYCoordinates(xCoordinate, yCoordinate);
            if (mapCell.getTower() != null) {
                errormessage = "Auf diesem feld gibt es bereits einen Turm!";
                buildable = false;
            } else if (mapCell.getBuildableByPlayer() != playerNumber) {
                errormessage = "Du darfst hier nicht bauen!";
                buildable = false;
            }
        }

        if (!buildable) {
            gameScreen.displayErrorMessage(errormessage);
            System.err.println(errormessage);
        }

        return buildable;
    }

    public boolean hasCellTower(int xCoordinate, int yCoordinate) {
        Coordinates coordinates = getMapCellByXandYCoordinates(xCoordinate, yCoordinate);

        return coordinates.getTower() != null;
    }

    /**
     * Verkauft einen Turm
     *
     * @param xCoordinate  Die x-Koordinate des Turms
     * @param yCoordinate  Die y-Koordinate des Turms
     * @param playerNumber Die Nummer der Spielerin, der der Turm gehört
     * @return Wenn das Verkaufen erfolgreich war, true, ansonsten false
     */
    @Override
    public boolean sellTower(int xCoordinate, int yCoordinate, int playerNumber) {

        boolean wasSuccessful = false;

        String errormessage = "";

        Coordinates mapCell = getMapCellByXandYCoordinates(xCoordinate, yCoordinate);

        Tower tower = mapCell.getTower();

        if (tower == null) {
            errormessage = "Hier gibt es keinen Turm zum Verkaufen!";
            System.err.println("Hier gibt es keinen Turm zum Verkaufen!");
        } else if (tower.getOwner().getPlayerNumber() != playerNumber) {
            errormessage = "Du darfst diesen Turm nicht verkaufen!";
        } else {
            tower.getOwner().addToResources(tower.getSellPrice());
            tower.getOwner().notifyObserver();
            removeTower(tower);
            wasSuccessful = true;
        }

        if (!wasSuccessful) {
            gameScreen.displayErrorMessage(errormessage);
            System.err.println(errormessage);
        }

        return wasSuccessful;
    }

    /**
     * Entfernt einen Turm aus dem Spiel
     *
     * @param tower Der zu entfernende Turm
     */
    private void removeTower(Tower tower) {
        tower.setRemoved(true);
        tower.getOwner().removeTower(tower);
        tower.getPosition().setTower(null);
        gamestate.removeTower(tower);
        tower.setRemoved(true);
        tower.notifyObserver();
    }


    /**
     * Rüstet einen Turm auf
     *
     * @param xCoordinate  Die x-Koordinate des Turms
     * @param yCoordinate  Die y-Koordinate des Turms
     * @param playerNumber Die Nummer der Spielerin, der der Turm gehört
     * @return Wenn das Aufrüsten erfolgreich war, true, ansonsten false
     */
    @Override
    public boolean upgradeTower(int xCoordinate, int yCoordinate, int playerNumber) {
        return false;
    }

    /**
     * Schickt einen Gegner zum gegnerischen Spieler
     *
     * @param enemyType Der Typ des zu schickenden Gegners
     * @return Wenn das Schicken erfolgreich war, true, ansonsten false
     */
    @Override
    public boolean sendEnemy(int enemyType) {
        return false;
    }

    /**
     * Gibt den Spielzustand zurück
     *
     * @return Der aktuelle Spielzustand
     */
    public Gamestate getGamestate() {
        return gamestate;
    }

    /**
     * Legt den Spielzustand fest
     *
     * @param gamestate Der festzulegende Spielzustand
     */
    public void setGamestate(Gamestate gamestate) {
        this.gamestate = gamestate;
    }

    public void setGameScreen(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
    }

    /**
     * Beendet das Spiel
     *
     * @param saveBeforeExit Gibt an, ob das Spiel vorher gespeichert werden soll
     */
    public void exitGame(boolean saveBeforeExit) {
        mainController.setEndScreen(gamestate);
    }
}
