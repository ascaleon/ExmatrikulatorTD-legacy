package de.diegrafen.exmatrikulatortd.controller.gamelogic;

import de.diegrafen.exmatrikulatortd.model.Coordinates;
import de.diegrafen.exmatrikulatortd.model.Gamestate;
import de.diegrafen.exmatrikulatortd.model.Player;
import de.diegrafen.exmatrikulatortd.model.enemy.Debuff;
import de.diegrafen.exmatrikulatortd.model.enemy.Enemy;
import de.diegrafen.exmatrikulatortd.model.enemy.Wave;
import de.diegrafen.exmatrikulatortd.model.tower.Aura;
import de.diegrafen.exmatrikulatortd.model.tower.Buff;
import de.diegrafen.exmatrikulatortd.model.tower.Projectile;
import de.diegrafen.exmatrikulatortd.model.tower.Tower;
import de.diegrafen.exmatrikulatortd.util.DistanceComparator;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static de.diegrafen.exmatrikulatortd.util.Constants.*;
import static java.awt.geom.Point2D.distance;

/**
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 20.06.2019 01:21
 */
class GameLogicUnit {

    private float auraRefreshTimer = 0;

    private final LogicController logicController;

    private final Gamestate gamestate;

    private List<Tower> attackingTowers = new LinkedList<>();

    GameLogicUnit(LogicController logicController) {
        this.logicController = logicController;
        this.gamestate = logicController.getGamestate();
    }

    /**
     * Spawnt die nächste Angriffswelle
     *
     * @param deltaTime Die Zeit seit dem letzten Rendern.
     */
    void spawnWave(float deltaTime) {

        if (logicController.isActiveRound()) {
            int roundNumber = gamestate.getRoundNumber();

            for (Player player : gamestate.getPlayers()) {

                List<Wave> waves = player.getWaves();
                Wave currentWave = waves.get(roundNumber);

                if (currentWave.getEnemies().size() <= currentWave.getEnemySpawnIndex()) {
                    currentWave.setEnemySpawnIndex(0);
                    player.setEnemiesSpawned(true);
                    gamestate.setNewRound(false);
                    if (gamestate.isEndlessGame()) {
                        waves.add(new Wave(waves.get(roundNumber)));
                    }
                    break;
                }

                if (!player.isEnemiesSpawned() && player.getTimeSinceLastSpawn() > TIME_BETWEEN_SPAWNS) {
                    Enemy enemy = currentWave.getEnemies().get(currentWave.getEnemySpawnIndex());
                    currentWave.setEnemySpawnIndex(currentWave.getEnemySpawnIndex() + 1);
                    addEnemy(enemy, player.getPlayerNumber());
                    player.setTimeSinceLastSpawn(0);
                } else {
                    player.setTimeSinceLastSpawn(player.getTimeSinceLastSpawn() + deltaTime);
                }
            }
        }
        gamestate.setTimeUntilNextRound(gamestate.getTimeUntilNextRound() - deltaTime);
        gamestate.notifyObserver();
    }

    /**
     * Wendet Auras auf die Objekte des Spiels an
     *
     * @param deltaTime Die Zeit, die seit dem Rendern des letzten Frames vergangen ist
     */
    void applyAuras(float deltaTime, Gamestate gamestate) {

        if (auraRefreshTimer > 0) {
            auraRefreshTimer -= deltaTime;
            return;
        } else {
            auraRefreshTimer = AURA_REFRESH_RATE - deltaTime;
        }

        for (Tower tower : gamestate.getTowers()) {
            List<Aura> auras = tower.getAuras();
            for (Aura aura : auras) {
                List<Debuff> debuffs = aura.getDebuffs();
                List<Buff> buffs = aura.getBuffs();

                for (Debuff debuff : debuffs) {
                    List<Enemy> enemiesInRange = getEnemiesInTowerRange(tower, calculateRangeFromTilesize(tower.getAuraRange()));
                    for (Enemy enemyInRange : enemiesInRange) {
                        addDebuffToEnemy(enemyInRange, debuff);
                    }
                }

                for (Buff buff : buffs) {
                    List<Tower> towersInRange = getTowersInRange(gamestate, tower.getxPosition(), tower.getyPosition(), calculateRangeFromTilesize(tower.getAuraRange()));
                    for (Tower towerInRange : towersInRange) {
                        addBuffToTower(towerInRange, buff);
                    }
                }
            }
        }
    }

    /**
     * Bewegt die Einheiten
     *
     * @param deltaTime Die Zeit, die seit dem Rendern des letzten Frames vergangen ist
     */
    void applyMovement(float deltaTime, Gamestate gamestate) {
        for (Enemy enemy : gamestate.getEnemies()) {
            if (Math.floor(getDistanceToNextPoint(enemy)) <= DISTANCE_TOLERANCE) {
                enemy.incrementWayPointIndex();
                setTargetToNextWayPoint(enemy);
            }
            Player attackedPlayer = gamestate.getPlayerByNumber(enemy.getPlayerNumber());
            if (attackedPlayer == null) {
                return;
            }

            if (enemy.getWayPointIndex() >= attackedPlayer.getWayPoints().size()) {
                applyDamageToPlayer(enemy);
                if (enemy.isRespawning()) {
                    addEnemy(new Enemy(enemy), attackedPlayer.getPlayerNumber());
                }
                break;
            }

            moveInTargetDirection(enemy, deltaTime);
            enemy.notifyObserver();
        }
    }

    /**
     * Lässt die Türme angreifen
     *
     * @param deltaTime Die Zeit, die seit dem Rendern des letzten Frames vergangen ist
     */
    void makeAttacks(float deltaTime) {

        for (Tower tower : gamestate.getTowers()) {

            tower.setCurrentAttackDelay(Math.min(tower.getCurrentAttackSpeed() / 2, tower.getBaseAttackDelay()));

            if (!isTargetStillInTowerRange(tower)) {
                findTargetforTower(tower, deltaTime);
            }

            if (tower.getCooldown() > 0) {
                tower.setCooldown(tower.getCooldown() - deltaTime);
            } else if (tower.getCurrentTarget() != null) {
                    tower.setAttacking(true);
                    tower.notifyObserver();
                    attackingTowers.add(tower);
                    tower.setCooldown(tower.getCurrentAttackSpeed());
            }
        }
    }

    /**
     * Wendet die Buffs aller Türme auf diese an.
     *
     * @param deltaTime Die Zeit, die seit dem letzten Rendern vergangen ist
     */
    void applyBuffsToTowers(float deltaTime) {
        for (Tower tower : gamestate.getTowers()) {

            List<Buff> buffsToRemove = new LinkedList<>();

            tower.setCurrentAttackDamage(tower.getBaseAttackDamage());
            tower.setCurrentAttackSpeed(tower.getBaseAttackSpeed());

            for (Buff buff : tower.getBuffs()) {
                tower.setCurrentAttackSpeed(tower.getCurrentAttackSpeed() * (1 / buff.getAttackSpeedMultiplier()));
                tower.setCurrentAttackDamage(tower.getCurrentAttackDamage() * buff.getAttackDamageMultiplier());
                buff.setDuration(buff.getDuration() - deltaTime);
                if (buff.getDuration() < 0) {
                    buffsToRemove.add(buff);
                }
            }
            buffsToRemove.forEach(tower::removeBuff);
        }
    }

    /**
     * Wendet die Buffs aller Gegner auf diese an.
     *
     * @param deltaTime Die Zeit, die seit dem letzten Rendern vergangen ist
     */
    void applyDebuffsToEnemies(float deltaTime) {
        for (Enemy enemy : gamestate.getEnemies()) {

            List<Debuff> debuffsToRemove = new LinkedList<>();

            float oldMaxHitPoints = enemy.getCurrentMaxHitPoints();

            enemy.setCurrentSpeed(enemy.getBaseSpeed());
            enemy.setCurrentArmor(enemy.getBaseArmor());
            enemy.setCurrentMaxHitPoints(enemy.getBaseMaxHitPoints());

            for (Debuff debuff : enemy.getDebuffs()) {
                enemy.setCurrentSpeed(enemy.getCurrentSpeed() * debuff.getSpeedMultiplier());
                enemy.setCurrentArmor(enemy.getCurrentArmor() + debuff.getArmorBonus());
                enemy.setCurrentMaxHitPoints(enemy.getCurrentMaxHitPoints() + debuff.getHealthBonus());

                if (enemy.getCurrentMaxHitPoints() < 0) {
                    enemy.setCurrentMaxHitPoints(1);
                }

                if (!debuff.isPermanent()) {
                    debuff.setDuration(debuff.getDuration() - deltaTime);
                }

                if (debuff.getDuration() < 0) {
                    debuffsToRemove.add(debuff);
                }
            }

            enemy.setCurrentHitPoints(enemy.getCurrentHitPoints() * enemy.getCurrentMaxHitPoints() / oldMaxHitPoints);

            for (Debuff debuff : debuffsToRemove) {
                enemy.removeDebuff(debuff);
            }
        }
    }

    private void addProjectile(Tower tower) {
        Projectile projectile = new Projectile(tower);

        gamestate.addProjectile(projectile);
        logicController.getGameScreen().addProjectile(projectile);
    }

    /**
     * Lässt einen Turm angreifen
     * @param tower Der angreifende Turm
     */
    private void letTowerAttack(Tower tower) {
        switch (tower.getAttackStyle()) {
            case PROJECTILE:
                addProjectile(tower);
                break;
            case IMMEDIATE:
                applyDamageToTarget(tower);
                break;
        }
    }

    public void applyAttackDelay(float deltaTime) {
        List<Tower> towersToRemove = new LinkedList<>();
        for (Tower tower : attackingTowers) {
            float attackdelay = tower.getAttackDelayTimer();
            if (attackdelay <= 0) {
                towersToRemove.add(tower);
                letTowerAttack(tower);
                tower.setAttackDelayTimer(tower.getCurrentAttackDelay());
            } else {
                tower.setAttackDelayTimer(attackdelay - deltaTime);
            }
        }
        towersToRemove.forEach(tower -> attackingTowers.remove(tower));

    }

    private void moveInTargetDirection(Enemy enemy, float deltaTime) {
        float xPosition = enemy.getxPosition();
        float yPosition = enemy.getyPosition();
        float currentSpeed = enemy.getCurrentSpeed();

        float angle = (float) Math.atan2(enemy.getTargetyPosition() - yPosition, enemy.getTargetxPosition() - xPosition);

        enemy.setxPosition(xPosition + (float) Math.cos(angle) * currentSpeed * deltaTime);
        enemy.setyPosition(yPosition + (float) Math.sin(angle) * currentSpeed * deltaTime);
    }

    private void addEnemy(Enemy enemy, int playerNumber) {
        enemy.setPlayerNumber(playerNumber);
        gamestate.addEnemy(enemy);
        Player attackedPlayer = gamestate.getPlayerByNumber(enemy.getPlayerNumber());
        enemy.addDebuff(generateDifficultyDebuff(attackedPlayer.getDifficulty()));
        enemy.addDebuff(generateRoundNumberDebuff());
        setEnemyToStartPosition(enemy);
        logicController.getGameScreen().addEnemy(enemy);
        enemy.notifyObserver();
    }

    private void applyDamageToPlayer(Enemy enemy) {
        Player attackedPlayer = gamestate.getPlayerByNumber(enemy.getPlayerNumber());
        attackedPlayer.setCurrentLives(attackedPlayer.getCurrentLives() - enemy.getAmountOfDamageToPlayer());
        removeEnemy(enemy);
    }

    private void removeEnemy(Enemy enemy) {
        enemy.clearDebuffs();
        gamestate.removeEnemy(enemy);
        enemy.setRemoved(true);
        enemy.notifyObserver();
    }

    private float getDistanceToNextPoint(Enemy enemy) {
        float x1 = enemy.getxPosition();
        float x2 = enemy.getTargetxPosition();
        float y1 = enemy.getyPosition();
        float y2 = enemy.getTargetyPosition();
        return (float) distance(x1, y1, x2, y2);
    }

    private void setTargetToNextWayPoint(Enemy enemy) {
        Player attackedPlayer = gamestate.getPlayerByNumber(enemy.getPlayerNumber());
        if (enemy.getWayPointIndex() < attackedPlayer.getWayPoints().size()) {
            Coordinates nextWayPoint = attackedPlayer.getWayPoints().get(enemy.getWayPointIndex());
            enemy.setTargetxPosition(nextWayPoint.getXCoordinate() * gamestate.getTileWidth());
            enemy.setTargetyPosition(nextWayPoint.getYCoordinate() * gamestate.getTileHeight());
        }
    }

    /**
     * Ermittelt eine Liste von Türmen in einem Umkreis um einen Punkt.
     *
     * @param xPosition Die x-Position des Umkreises, in dem gesucht wird
     * @param yPosition Die y-Position des Umkreises, in dem gesucht wird
     * @param range     Der Radius, in dem gesucht werden soll
     * @return Die Liste der gefundenen Türme
     */
    private List<Tower> getTowersInRange(Gamestate gamestate, float xPosition, float yPosition, float range) {
        List<Tower> towersInRange = new LinkedList<>();

        for (Tower tower : gamestate.getTowers()) {

            double distance = distance(xPosition, yPosition, tower.getxPosition(), tower.getyPosition());

            if (distance <= range) {
                towersInRange.add(tower);
            }
        }

        return towersInRange;
    }

    /**
     * Gibt alle Gegner zurück, die sich in einem angegebenen Umkreis eines Turms befinden.
     *
     * @param tower Der Turm, der als Referenzpunkt genutzt wird
     * @param range Der Radius um den Turm, in dem gesucht wird
     * @return Die Liste der gefundenen Gegner
     */
    private List<Enemy> getEnemiesInTowerRange(Tower tower, float range) {
        List<Enemy> enemiesInRange = new LinkedList<>();

        for (Enemy enemy : gamestate.getEnemies()) {
            if (enemy.getPlayerNumber() == tower.getPlayerNumber() & isEnemyInRangeOfTower(enemy, tower, range)) {
                enemiesInRange.add(enemy);
            }
        }

        return enemiesInRange;
    }

    /**
     * Überprüft, ob sich ein Gegner in Reichweite eines Turmes befindet.
     *
     * @param enemy Der Gegner, der geprüft werden soll.
     * @param tower Der Turm, dessen Reichweite geprüft werden soll
     * @param range Die Reichweite des Turms
     * @return {@code true}, wenn sich der Gegner im Radius befindet. Ansonsten {@code false}
     */
    private boolean isEnemyInRangeOfTower(Enemy enemy, Tower tower, float range) {
        double distance = distance(tower.getxPosition(), tower.getyPosition(), enemy.getxPosition(), enemy.getyPosition());
        return distance <= range;
    }

    /**
     *
     */
    private void addDebuffToEnemy(Enemy enemy, Debuff debuff) {
        for (Debuff debuffToCheck : enemy.getDebuffs()) {
            if (debuff.getName().equals(debuffToCheck.getName())) {
                return;
            }
        }

        enemy.addDebuff(new Debuff(debuff));
    }

    /**
     *
     */
    private void addBuffToTower(Tower tower, Buff buff) {
        for (Buff buffToCheck : tower.getBuffs()) {
            if (buff.getName().equals(buffToCheck.getName())) {
                return;
            }
        }

        tower.addBuff(new Buff(buff));
    }

    private Debuff generateDifficultyDebuff(int difficulty) {

        Debuff difficultyDebuff = new Debuff();

        difficultyDebuff.setPermanent(true);

        switch (difficulty) {
            case TESTMODE:
                difficultyDebuff.setArmorBonus(-100);
                difficultyDebuff.setHealthBonus(-1000);
                difficultyDebuff.setSpeedMultiplier(0.5f);
                break;
            case EASY:
                difficultyDebuff.setArmorBonus(-6);
                difficultyDebuff.setSpeedMultiplier(0.7f);
                break;
            case HARD:
                difficultyDebuff.setArmorBonus(6);
                difficultyDebuff.setSpeedMultiplier(1.3f);
                break;
        }

        return difficultyDebuff;
    }

    private Debuff generateRoundNumberDebuff() {

        Debuff roundNumberDebuff = new Debuff();

        roundNumberDebuff.setArmorBonus(gamestate.getRoundNumber() * ARMOR_INCREASE_PER_LEVEL);
        roundNumberDebuff.setSpeedMultiplier(1 + ((float) gamestate.getRoundNumber()) / 100 * SPEED_INCREASE_PER_LEVEL);

        return roundNumberDebuff;
    }

    /**
     * Setzt einen Gegner an seine Startposition (zurück)
     *
     * @param enemy Der Gegner, der an die Startposition (zurück)gesetzt werden soll
     */
    private void setEnemyToStartPosition(Enemy enemy) {
        Player attackedPlayer = gamestate.getPlayerByNumber(enemy.getPlayerNumber());
        Coordinates startCoordinates = attackedPlayer.getWayPoints().get(0);
        enemy.setxPosition(startCoordinates.getXCoordinate() * gamestate.getTileWidth());
        enemy.setyPosition(startCoordinates.getYCoordinate() * gamestate.getTileHeight());
        setTargetToNextWayPoint(enemy);
    }
    private void calculateDamageImpact(Enemy enemy) {
        if (enemy.getCurrentHitPoints() <= 0) {
            Player attackedPlayer = gamestate.getPlayerByNumber(enemy.getPlayerNumber());
            if (attackedPlayer != null) {
                attackedPlayer.addToResources(enemy.getBounty());
                attackedPlayer.addToScore(enemy.getPointsGranted());
                attackedPlayer.incrementBodyTracker();
                attackedPlayer.notifyObserver();
            }
            if (!enemy.isRemoved()) {
                removeEnemy(enemy);
            }
        }
    }

    private void applyDamageToTarget(Projectile projectile) {
        List<Enemy> enemiesHit = new LinkedList<>();
        Enemy mainTarget = projectile.getTarget();
        if (mainTarget == null) {
            removeProjectile(projectile);
            return;
        }
        mainTarget.setCurrentHitPoints(mainTarget.getCurrentHitPoints() - projectile.getDamage() * calculateDamageMultiplier(mainTarget, projectile.getAttackType()));
        enemiesHit.add(mainTarget);

        if (projectile.getSplashRadius() > 0) {
            List<Enemy> enemiesInSplashRadius = getEnemiesInSplashRadius(projectile);
            for (Enemy enemyInSplashRadius : enemiesInSplashRadius) {
                float damageMultiplier = calculateDamageMultiplier(enemyInSplashRadius, projectile.getAttackType());
                enemyInSplashRadius.setCurrentHitPoints(enemyInSplashRadius.getCurrentHitPoints() - projectile.getDamage() * projectile.getsplashAmount() * damageMultiplier);
                enemiesHit.add(enemyInSplashRadius);
            }
        }

        for (Enemy enemyHit : enemiesHit) {
            for (Debuff debuff : projectile.getApplyingDebuffs()) {
                addDebuffToEnemy(enemyHit, debuff);
            }
            calculateDamageImpact(enemyHit);
        }
        removeProjectile(projectile);
    }

    private void applyDamageToTarget(Tower tower){
        List<Enemy> enemiesHit = new LinkedList<>();
        Enemy mainTarget = tower.getCurrentTarget();
        if (mainTarget != null) {
            mainTarget.setCurrentHitPoints(mainTarget.getCurrentHitPoints()
                    - tower.getCurrentAttackDamage() * calculateDamageMultiplier(mainTarget, tower.getAttackType()));
            enemiesHit.add(mainTarget);
        }


        if (tower.getSplashRadius() > 0) {
            List<Enemy> enemiesInSplashRadius = getEnemiesInSplashRadius(tower);
            for (Enemy enemyInSplashRadius : enemiesInSplashRadius) {
                float damageMultiplier = calculateDamageMultiplier(enemyInSplashRadius, tower.getAttackType());
                enemyInSplashRadius.setCurrentHitPoints(enemyInSplashRadius.getCurrentHitPoints() - tower.getCurrentAttackDamage() * tower.getSplashAmount() * damageMultiplier);
                enemiesHit.add(enemyInSplashRadius);
            }
        }

        for (Enemy enemyHit : enemiesHit) {
            for (Debuff debuff : tower.getAttackDebuffs()) {
                addDebuffToEnemy(enemyHit, debuff);
            }
            calculateDamageImpact(enemyHit);
        }
    }

    private void removeProjectile(Projectile projectileToRemove) {
        projectileToRemove.setRemoved(true);
        projectileToRemove.notifyObserver();
        gamestate.removeProjectile(projectileToRemove);
    }

    private float calculateDamageMultiplier(Enemy enemy, int attackType) {
        float armor = enemy.getCurrentArmor();
        float damageModifier = 1;

        float armorEffect = ATTACK_DEFENSE_MATRIX[attackType][enemy.getArmorType()];

        if (armor > 0) {
            damageModifier = 1 - (armor * DAMAGE_REDUCTION_FACTOR) / (1 + DAMAGE_REDUCTION_FACTOR * armor);
        } else if (armor < 0) {
            damageModifier = 2 - (float) Math.pow(0.94, -armor);
        }

        return armorEffect * damageModifier;
    }

    /**
     * Bewegt die Geschosse. Trifft ein Geschoss sein Ziel, wird diesem Schaden zugefügt.
     *
     * @param deltaTime Die Zeit, die seit dem Rendern des letzten Frames vergangen ist
     */
    void moveProjectiles(float deltaTime) {
        List<Projectile> projectilesThatHit = new LinkedList<>();

        for (Projectile projectile : gamestate.getProjectiles()) {

            if (projectileHasHit(projectile)) {
                projectilesThatHit.add(projectile);
                continue;
            }
            moveInTargetDirection(projectile, deltaTime);
        }

        for (Projectile projectile : projectilesThatHit) {
            applyDamageToTarget(projectile);
        }
    }

    private boolean projectileHasHit(Projectile projectile) {
        return Math.floor(getDistanceToTarget(projectile)) <= DISTANCE_TOLERANCE;
    }

    /**
     * Berechnet die Distanz eines Geschosses zu seinem Ziel
     *
     * @param projectile Das Geschoss, für das die Distanz zum Zielobjekt berechnet werden soll
     * @return Die Distanz zum Zielobjekt des Projektils
     */
    private float getDistanceToTarget(Projectile projectile) {
        if (projectile.getTarget() != null) {
            float x1 = projectile.getxPosition();
            float x2 = projectile.getTarget().getxPosition();
            float y1 = projectile.getyPosition();
            float y2 = projectile.getTarget().getyPosition();
            return (float) distance(x1, y1, x2, y2);
        } else {
            return 0;
        }
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

    /**
     * Ermittelt, ob das Ziel eines Turms immer noch in Reichweite ist
     *
     * @param tower Der Turm, für den die Ermittlung durchgeführt werden soll.
     * @return true, wenn das Ziel immer noch in Reichweite ist, ansonsten false
     */
    private boolean isTargetStillInTowerRange(Tower tower) {

        Enemy target = tower.getCurrentTarget();

        return target != null && !target.isRemoved() & isEnemyInRangeOfTower(target, tower, calculateRangeFromTilesize(tower.getAttackRange()));
    }

    /**
     * Gibt alle Gegner zurück, die sich Flächenschaden-Radius eines Projektils befinden
     *
     * @param projectile Das Projektil, das den Flächenschaden verursacht
     * @return Die Liste der ermittelten Gegner
     */
    private List<Enemy> getEnemiesInSplashRadius(Projectile projectile) {
        List<Enemy> enemiesInRange = new LinkedList<>();

        for (Enemy enemy : gamestate.getEnemies()) {
            if (isEnemyInSplashRadius(enemy, projectile)) {
                enemiesInRange.add(enemy);
            }
        }

        return enemiesInRange;
    }

    private List<Enemy> getEnemiesInSplashRadius(Tower tower) {
        List<Enemy> enemiesInRange = new LinkedList<>();

        for (Enemy enemy : gamestate.getEnemies()) {
            if (isEnemyInSplashRadius(enemy, tower)) {
                enemiesInRange.add(enemy);
            }
        }

        return enemiesInRange;
    }

    /**
     * Überprüft, ob sich ein Gegner im Flächenschaden-Radius eines Turms befindet.
     *
     * @param enemy      Der Gegner, der geprüft werden soll.
     * @return {@code true}, wenn sich der Gegner im Radius befindet. Ansonsten {@code false}
     */
    private boolean isEnemyInSplashRadius(Enemy enemy, Tower tower) {
        return isEnemyInSplashRadius(enemy, tower.getxPosition(), tower.getyPosition(), tower.getSplashRadius(), tower.getPlayerNumber());
    }

    /**
     * Überprüft, ob sich ein Gegner im Flächenschaden-Radius eines Projektils befindet.
     *
     * @param enemy      Der Gegner, der geprüft werden soll.
     * @return {@code true}, wenn sich der Gegner im Radius befindet. Ansonsten {@code false}
     */
    private boolean isEnemyInSplashRadius(Enemy enemy, Projectile projectile) {
        return isEnemyInSplashRadius(enemy, projectile.getxPosition(), projectile.getyPosition(), projectile.getSplashRadius(), projectile.getPlayerNumber());
    }

    /**
     * Überprüft, ob sich ein Gegner im Flächenschaden-Radius eines Projektils befindet.
     *
     * @param enemy      Der Gegner, der geprüft werden soll.
     * @return {@code true}, wenn sich der Gegner im Radius befindet. Ansonsten {@code false}
     */
    private boolean isEnemyInSplashRadius(Enemy enemy, float xPosition, float yPosition, float range, int playerNumber) {
        if (enemy.getPlayerNumber() != playerNumber) {
            return false;
        }
        double distance = distance(xPosition, yPosition, enemy.getxPosition(), enemy.getyPosition());
        return distance <= calculateRangeFromTilesize(range);
    }

    private void findTargetforTower(Tower tower, float deltaTime) {

        float timeSinceLastSearch = tower.getTimeSinceLastSearch();

        if (timeSinceLastSearch >= SEARCH_TARGET_INTERVAL) {
            tower.setCurrentTarget(findClosestEnemy(tower));
            tower.setTimeSinceLastSearch(0);
        } else {
            tower.setTimeSinceLastSearch(timeSinceLastSearch + deltaTime);
        }
    }

    private Enemy findClosestEnemy(Tower tower) {
        List<Enemy> enemiesInRange = getEnemiesInTowerRange(tower, calculateRangeFromTilesize(tower.getAttackRange()));
        enemiesInRange.sort(new DistanceComparator(tower.getxPosition(), tower.getyPosition()));
        return !enemiesInRange.isEmpty() ? enemiesInRange.get(0) : null;
    }

    /**
     * Wandelt eine relativ zur Größe der Kartenfelder gegebene Reicheweite in absolute Werte um.
     *
     * Dabei wird der Durchschnitt von Höhe und Breite als Größe der Kartenfelder herangezogen, um (theoretisch) auch
     * rechteckige Felder berücksichtigen zu können.
     *
     * @param range Die in Kartenfeldern ausgedrückte Reichweite
     * @return Die in absoluten Werten ausgedrückte Reichweite
     */
    private float calculateRangeFromTilesize(float range) {
        range *= ((gamestate.getTileHeight() + gamestate.getTileWidth()) / 2.0f);
        return range;
    }

}
