create sequence hibernate_sequence start with 1 increment by 1
create table Auras (id bigint not null, primary key (id))
create table Auras_Buffs (Aura_id bigint not null, buffs_id bigint not null)
create table Auras_Debuffs (Aura_id bigint not null, debuffs_id bigint not null)
create table Buffs (id bigint not null, attackDamageMultiplier float not null, attackSpeedMultiplier float not null, duration float not null, name varchar(255), permanent boolean not null, primary key (id))
create table collision_matrix (tower_id bigint, id bigint not null, primary key (id))
create table Debuffs (id bigint not null, armorBonus float not null, duration float not null, healthBonus float not null, name varchar(255), permanent boolean not null, speedMultiplier float not null, primary key (id))
create table Enemies (id bigint not null, amountOfDamageToPlayer integer not null, armorType integer not null, assetsName varchar(255), baseArmor float not null, baseMaxHitPoints float not null, baseSpeed float not null, bounty integer not null, currentArmor float not null, currentHitPoints float not null, currentMaxHitPoints float not null, currentSpeed float not null, description varchar(255), isAttacking boolean not null, name varchar(255), playerNumber integer not null, pointsGranted integer not null, respawning boolean not null, sendPrice integer not null, targetxPosition float not null, targetyPosition float not null, wayPointIndex integer not null, xPosition float not null, yPosition float not null, primary key (id))
create table Enemies_Debuffs (Enemy_id bigint not null, debuffs_id bigint not null)
create table Gamestate (id bigint not null, active boolean not null, endlessGame boolean not null, gameMode integer not null, gameOver boolean not null, newRound boolean not null, numberOfColumns integer not null, numberOfRounds integer not null, numberOfRows integer not null, roundEnded boolean not null, roundNumber integer not null, tileHeight integer not null, tileWidth integer not null, timeUntilNextRound float not null, primary key (id))
create table Gamestate_Enemies (Gamestate_id bigint not null, enemies_id bigint not null)
create table Gamestate_Players (Gamestate_id bigint not null, players_id bigint not null)
create table Gamestate_Projectiles (Gamestate_id bigint not null, projectiles_id bigint not null)
create table Gamestate_Towers (Gamestate_id bigint not null, towers_id bigint not null)
create table Gamestate_waypoints (Gamestate_id bigint not null, collisionMatrix_id bigint not null)
create table Highscores (id bigint not null, datePlayed timestamp, roundNumberReached integer not null, score integer not null, profile_id bigint, primary key (id))
create table Players (id bigint not null, currentLives integer not null, difficulty integer not null, enemiesSpawned boolean not null, killTracker integer not null, lost boolean not null, maxLives integer not null, playerName varchar(255), playerNumber integer not null, resources integer not null, score integer not null, timeSinceLastSpawn float not null, towerCounter integer not null, victorious boolean not null, primary key (id))
create table Players_Waves (Player_id bigint not null, waves_id bigint not null)
create table Players_waypoints (Player_id bigint not null, wayPoints_id bigint not null)
create table Profiles (id bigint not null, preferredDifficulty integer not null, profileName varchar(255), profilePicturePath varchar(255), primary key (id))
create table Projectiles (id bigint not null, assetsName varchar(255), attackType integer not null, damage float not null, name varchar(255), playerNumber integer not null, speed float not null, splashAmount float not null, splashRadius float not null, targetxPosition float not null, targetyPosition float not null, xPosition float not null, yPosition float not null, target_id bigint, primary key (id))
create table Projectiles_Debuffs (Projectile_id bigint not null, applyingDebuffs_id bigint not null)
create table SaveState (id bigint not null, localPlayerNumber integer not null, mapPath varchar(255), multiplayer boolean not null, saveDate timestamp, saveStateName varchar(255), gamestate_id bigint, profile_id bigint, primary key (id))
create table Towers (id bigint not null, assetsName varchar(255), attackDamageUpgradeBonus float not null, attackDelayTimer float not null, attackRange float not null, attackRangeUpgradeBonus float not null, attackSpeedUpgradeMultiplier float not null, attackStyle varchar(255), attackType integer not null, attacking boolean, auraRange float not null, auraRangeUpgradeBonus float not null, baseAttackDamage float not null, baseAttackDelay float not null, baseAttackSpeed float not null, cooldown float not null, currentAttackDamage float not null, currentAttackDelay float not null, currentAttackSpeed float not null, descriptionText varchar(255), maxUpgradeLevel integer not null, name varchar(255), playerNumber integer not null, portraitPath varchar(255), price integer not null, projectileAssetsName varchar(255), projectileName varchar(255), projectileSpeed float not null, selectedPortraitPath varchar(255), sellPrice integer not null, splashAmount float not null, splashRadius float not null, template boolean not null, timeSinceLastSearch float not null, towerType integer not null, upgradeLevel integer not null, upgradePrice integer not null, xPosition float not null, yPosition float not null, currentTarget_id bigint, primary key (id))
create table Towers_Auras (Tower_id bigint not null, auras_id bigint not null)
create table Towers_Buffs (Tower_id bigint not null, buffs_id bigint not null)
create table Towers_Debuffs (Tower_id bigint not null, attackDebuffs_id bigint not null)
create table Waves (id bigint not null, enemySpawnIndex integer not null, primary key (id))
create table Waves_Enemies (Wave_id bigint not null, enemies_id bigint not null)
create table waypoints (id bigint not null, buildableByPlayer integer not null, height integer not null, playerNumber integer, waypointIndex integer, width integer not null, xCoordinate integer not null, yCoordinate integer not null, primary key (id))
alter table Auras_Buffs add constraint UK_m67a4pcb8c6glo31wm45p545r unique (buffs_id)
alter table Auras_Debuffs add constraint UK_43ayrxu0by9uhuei0j3uhph3r unique (debuffs_id)
alter table Enemies_Debuffs add constraint UK_kworsqvb9c4str4ld3hihbgq unique (debuffs_id)
alter table Gamestate_Enemies add constraint UK_cbjg8jykwmsepdbw9l0nx0e2k unique (enemies_id)
alter table Gamestate_Players add constraint UK_34rmf3msk9s118iovptonfqdk unique (players_id)
alter table Gamestate_Projectiles add constraint UK_8cpqwj9vgxfigox5d3v7uptak unique (projectiles_id)
alter table Gamestate_Towers add constraint UK_mmc8fkonksmiiyo7pii4xhi3t unique (towers_id)
alter table Gamestate_waypoints add constraint UK_qobdvyhudkx1kwletkob1d2l7 unique (collisionMatrix_id)
alter table Players_Waves add constraint UK_6bv7b5hejvrvn2gdhbwi8902m unique (waves_id)
alter table Players_waypoints add constraint UK_e0m8lr4nes9qweryoba6fw9ny unique (wayPoints_id)
alter table Projectiles_Debuffs add constraint UK_sgciurgac1mp3yd31a08ww63m unique (applyingDebuffs_id)
alter table Towers_Auras add constraint UK_ccs0v6br0qq42k77tdkku6jsd unique (auras_id)
alter table Towers_Buffs add constraint UK_odujtqs3goqvtbg5qjyxxubjw unique (buffs_id)
alter table Towers_Debuffs add constraint UK_oi4pq569gnoftmqhu8mv4w152 unique (attackDebuffs_id)
alter table Waves_Enemies add constraint UK_sp87jbqpox6e4qvgywu7o4xrx unique (enemies_id)
alter table Auras_Buffs add constraint FK2dhd0yqlwuflio8qsrk1g7ds4 foreign key (buffs_id) references Buffs
alter table Auras_Buffs add constraint FKdnyywnjna40n8uutrmj93obhk foreign key (Aura_id) references Auras
alter table Auras_Debuffs add constraint FKlu2wbhjrw899ognst18eniftb foreign key (debuffs_id) references Debuffs
alter table Auras_Debuffs add constraint FK1wlylay7nw9335e6aftg2s8kq foreign key (Aura_id) references Auras
alter table collision_matrix add constraint FKi91vt62lgb35fd4x0ahq03wwy foreign key (tower_id) references Towers
alter table collision_matrix add constraint FKajn6nmi3qk77a7dtn9p4ev15p foreign key (id) references waypoints
alter table Enemies_Debuffs add constraint FKd78mtlqmn00d1fpscfhxlaq5r foreign key (debuffs_id) references Debuffs
alter table Enemies_Debuffs add constraint FKox152unif9g3judfxvymcxuad foreign key (Enemy_id) references Enemies
alter table Gamestate_Enemies add constraint FKei20x6cnmab9wr1ey2lhnuwh5 foreign key (enemies_id) references Enemies
alter table Gamestate_Enemies add constraint FKihjulv9o9a1f050dbwkfvn3iy foreign key (Gamestate_id) references Gamestate
alter table Gamestate_Players add constraint FK1ij8retuer729ui39qaf1mh6e foreign key (players_id) references Players
alter table Gamestate_Players add constraint FKhx49kewejkq0jksvpg0yleacb foreign key (Gamestate_id) references Gamestate
alter table Gamestate_Projectiles add constraint FKsr0f0qje5avtd4m1lfd9fyhwl foreign key (projectiles_id) references Projectiles
alter table Gamestate_Projectiles add constraint FKcgsjbfhy6ysg8i222dgsuv6ys foreign key (Gamestate_id) references Gamestate
alter table Gamestate_Towers add constraint FKnsrundee2nmq25ps38t7ltoly foreign key (towers_id) references Towers
alter table Gamestate_Towers add constraint FKciebtgakjoj90dfm0odiasscr foreign key (Gamestate_id) references Gamestate
alter table Gamestate_waypoints add constraint FKmo8euhktm6ftmp40akusppnmk foreign key (collisionMatrix_id) references waypoints
alter table Gamestate_waypoints add constraint FKse6oa93q6b0k650runj5479xb foreign key (Gamestate_id) references Gamestate
alter table Highscores add constraint FKg1u82fa9144lv78wj2eawb53v foreign key (profile_id) references Profiles
alter table Players_Waves add constraint FKeqgnb8jrfmehlnjsshma01lc foreign key (waves_id) references Waves
alter table Players_Waves add constraint FK1vmaa817r73i92te8bdt7pn8b foreign key (Player_id) references Players
alter table Players_waypoints add constraint FKnbriyka2be8cnyl6m45s0gpyx foreign key (wayPoints_id) references waypoints
alter table Players_waypoints add constraint FK7y00hmwnsh3uc2x0niflqhmoo foreign key (Player_id) references Players
alter table Projectiles add constraint FKm2npdnu7kklpu5q687qkqe2jv foreign key (target_id) references Enemies
alter table Projectiles_Debuffs add constraint FKa4m5vwje8uook34u0b7dw4gg2 foreign key (applyingDebuffs_id) references Debuffs
alter table Projectiles_Debuffs add constraint FK9xa7voiw6o93pv9i67tew8auw foreign key (Projectile_id) references Projectiles
alter table SaveState add constraint FKp89i8qyc4vsn0wl6ew6ndubvx foreign key (gamestate_id) references Gamestate
alter table SaveState add constraint FK2vjbl51tnq7ufh776xqt2iuj4 foreign key (profile_id) references Profiles
alter table Towers add constraint FK1tp9iivew89809wmljvy52kh5 foreign key (currentTarget_id) references Enemies
alter table Towers_Auras add constraint FKbfuroltderboqhmvils4d5npw foreign key (auras_id) references Auras
alter table Towers_Auras add constraint FKdtgojl4myvpx18y5nxfq48p4v foreign key (Tower_id) references Towers
alter table Towers_Buffs add constraint FK5ugato2n1nbk3m2k9wyod2kh6 foreign key (buffs_id) references Buffs
alter table Towers_Buffs add constraint FKmax38146id3h52j2q1osayyig foreign key (Tower_id) references Towers
alter table Towers_Debuffs add constraint FK1q214g6s2eg0sgc6mk2tid12d foreign key (attackDebuffs_id) references Debuffs
alter table Towers_Debuffs add constraint FKj7l8vgv3x5p4kwuydij7puidv foreign key (Tower_id) references Towers
alter table Waves_Enemies add constraint FKg730sjfdybwj1qdht8aqhohjx foreign key (enemies_id) references Enemies
alter table Waves_Enemies add constraint FK3xvg4p9r1j0oocwydnglmkp8j foreign key (Wave_id) references Waves