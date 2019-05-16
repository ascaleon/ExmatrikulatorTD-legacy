package de.diegrafen.towerwars.persistance;

import java.io.File;
import java.sql.*;

/**
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 16.05.2019 02:21
 */
public class DataBase {

    protected static String dataBaseName = "towerwars_db";
    protected static DataBase instance = null;
    protected static int version = 1;

    protected Connection dataBaseConnection;
    protected Statement statement;
    protected boolean noDataBase = false;

    public DataBase() {
        loadDatabase();
        if (isNewDatabase()) {
            onCreate();
            upgradeVersion();
        } else if (isVersionDifferent()) {
            onUpgrade();
            upgradeVersion();
        }

    }

    private void onCreate() {
        //Example of Highscore table code (You should change this for your own DB code creation)

        execute("CREATE TABLE highscore(highscoreid INTEGER PRIMARY KEY, score INTEGER);)");
        execute("CREATE TABLE player(playerid INTEGER, name TEXT, score INTEGER, FOREIGN KEY(score) REFERENCES highscore(highscoreid));");



//        execute("CREATE TABLE Highscores (" +
//                "Id INT PRIMARY KEY NOT NULL," +
//                "FOREIGN KEY name REFERENCES Players (Name)," +
//                "SCORE INTEGER NOT NULL);");
//        execute("CREATE TABLE Players " +
//                "(Id INTEGER PRIMARY KEY NOT NULL," +
//                "Name VARCHAR NOT NULL);");
        //execute("CREATE TABLE 'players' ('_id' INTEGER PRIMARY KEY  NOT NULL , 'name' VARCHAR NOT NULL);");
        execute("INSERT INTO highscore (highscoreid, score) VALUES (0, 9001);");
        execute("INSERT INTO player (playerid, name, score) VALUES (0, 'Rainer', 0);");
        //Example of query to get DB data of Highscore table
        Result query = query("SELECT * FROM player");
        if (!query.isEmpty()) {
            query.moveToNext();
            System.out.println("Highscore of " + query.getString(query.getColumnIndex("name")) + ": "
                    + query.getString(query.getColumnIndex("score")));
        }
    }

    private void onUpgrade() {
        //Example code (You should change this for your own DB code)
        execute("DROP TABLE IF EXISTS 'highscores';");
        onCreate();
        System.out.println("DB Upgrade made because I changed DataBase.version on code");
    }

    //Runs a sql query like "create".
    public void execute(String sql) {
        try {
            statement.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Identical to execute but returns the number of rows affected (useful for updates)
    public int executeUpdate(String sql) {
        try {
            return statement.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    //Runs a query and returns an Object with all the results of the query.
    public Result query(String sql) {
        try {
            return new Result(statement.executeQuery(sql));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void loadDatabase() {
        File file = new File(dataBaseName + ".db");
        if (!file.exists())
            noDataBase = true;
        try {
            Class.forName("org.sqlite.JDBC");
            dataBaseConnection = DriverManager.getConnection("jdbc:sqlite:" + dataBaseName + ".db");
            statement = dataBaseConnection.createStatement();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void upgradeVersion() {
        execute("PRAGMA user_version=" + version);
    }

    private boolean isNewDatabase() {
        return noDataBase;
    }

    private boolean isVersionDifferent() {
        Result query = query("PRAGMA user_version");
        return query.isEmpty() || (query.getInt(1) != version);
    }
}