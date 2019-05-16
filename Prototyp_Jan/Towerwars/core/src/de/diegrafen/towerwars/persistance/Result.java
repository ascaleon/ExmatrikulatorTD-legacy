package de.diegrafen.towerwars.persistance;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Jan Romann <jan.romann@uni-bremen.de>
 * @version 16.05.2019 02:46
 */

public class Result {

    private ResultSet res;
    private boolean called_is_empty = false;

    Result(ResultSet res) {
        this.res = res;
    }

    public boolean isEmpty() {
        try {
            if (res.getRow() == 0) {
                called_is_empty = true;
                return !res.next();
            }
            return res.getRow() == 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean moveToNext() {
        try {
            if (called_is_empty) {
                called_is_empty = false;
                return true;
            } else
                return res.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public int getColumnIndex(String name) {
        try {
            return res.findColumn(name);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public float getFloat(int columnIndex) {
        try {
            return res.getFloat(columnIndex);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getInt(int columnIndex) {
        try {
            return res.getInt(columnIndex);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public String getString(int columnIndex) {
        try {
            return res.getString(columnIndex);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "";
    }
}
