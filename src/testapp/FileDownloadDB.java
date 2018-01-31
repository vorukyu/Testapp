/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testapp;

/**
 *
 * @author user
 */
public interface FileDownloadDB {
    public boolean connect();
    public void createTable();
    public void disconnect();
    public void insertData(String filename);
}
