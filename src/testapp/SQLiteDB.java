/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testapp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
/**
 *
 * @author user
 */
public class SQLiteDB implements FileDownloadDB {
    protected Connection conn;
    protected String dbPath; //path to database
    SQLiteDB() {
        conn = null;
        dbPath = null;
    }
    SQLiteDB(String dbPath) {
        this.conn = null;
        this.dbPath = dbPath;
    }
    protected String createTableQuery() {
        return "CREATE TABLE IF NOT EXISTS SFTPDownloads(\n"
                + "	id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                + "	filename TEXT NOT NULL,\n"
                + "	date TEXT\n"
                + ");";
    }
    protected String insertDataQuery() {
        return "INSERT INTO SFTPDownloads(filename,date) VALUES(?,datetime(\'now\'))";
    }
    @Override
    public boolean connect() {
        boolean result = false;
        if(this.conn == null && this.dbPath!=null) {
            try {
                this.conn = DriverManager.getConnection("jdbc:sqlite:"+this.dbPath);
                result = true;
            }
            catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        return result;
    }
    @Override
    public void disconnect() {
        //close db connection
        if(this.conn!=null) {
            try{
                this.conn.close();
                this.conn = null;
            }
            catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }
    @Override
    public void createTable() {
        // create table
        Statement statem;
        if(this.conn!=null) {
            try {
                statem = this.conn.createStatement();
                String sqlExec = createTableQuery();
                statem.execute(sqlExec);
            }
            catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }
    @Override
    public void insertData(String filename) {
        //insert table record
        String sqlExec = insertDataQuery();
        if(this.conn!=null) {
            try {
                PreparedStatement prepStatem = this.conn.prepareStatement(sqlExec);
                prepStatem.setString(1, filename);
                prepStatem.executeUpdate();
            }
            catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}