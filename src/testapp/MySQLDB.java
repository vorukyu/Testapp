/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testapp;

import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author user
 */
public class MySQLDB extends SQLiteDB {
    String user;
    String password;
    MySQLDB(String database, String user, String password) {
        super(database);
        this.user = user;
        this.password = password;
    }
    @Override
    public boolean connect() {
        boolean result = false;
        if(this.conn == null && this.dbPath!=null) {
            try {
                this.conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/"+this.dbPath,this.user,this.password);
                result = true;
            }
            catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        return result;
    }
    @Override
    protected String createTableQuery() {
        return "CREATE TABLE IF NOT EXISTS SFTPDownloads(\n"
                + "	id INTEGER PRIMARY KEY AUTO_INCREMENT,\n"
                + "	filename TEXT NOT NULL,\n"
                + "	dldate datetime\n"
                + ");";
    }
    @Override
    protected String insertDataQuery() {
        return "INSERT INTO SFTPDownloads(filename,dldate) VALUES(?,(now()))";
    }
}
