/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */ 

package testapp;

import java.util.Properties;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
/**
 *
 * @author user
 */
class Settings {
    private String sftpHost;
    private Short sftpPort;
    private String sftpUser;
    private String sftpPassword;
    private String sftpRemoteDir;
    private String localDir;
    private String sqlUser;
    private String sqlPassword;
    private String sqlDatabase;
    Settings () {
        /*this.sftpHost = "127.0.0.1";
        this.sftpPort = 22;
        this.sftpPassword = "test";
        this.sftpUser = "user";
        this.sftpRemoteDir = "some/dir";
        this.localDir = "C:/Temp/";
        this.sqlUser = "user";
        this.sqlPassword = "password";
        this.sqlDatabase = "D:\\testdb.s3db"; */
        this.sftpHost = null;
        this.sftpPort = null;
        this.sftpPassword = null;
        this.sftpUser = null;
        this.sftpRemoteDir = null;
        this.localDir = null;
        this.sqlUser = null;
        this.sqlPassword = null;
        this.sqlDatabase = null;
    }
    
    private boolean checkSettings() {
        if(getSftpHost() != null
                && getSftpPort() != null
                && getSftpPassword() != null
                && getSftpUser() != null
                && getSftpRemoteDir() != null
                && getLocalDir() != null
                && getSqlUser() != null
                && getSqlPassword() != null
                && getSqlDatabase() != null
                ) {
            return true;
        }
        else {
            return false;
        }
    }
    
    public boolean retrieveFromFile(String filename, String keySeparator, String settingSeparator) {
        boolean fileLoaded = false;
        //file read
        File file = new File(filename);
        String settingsString = "";
        FileInputStream inpStream = null;
        try {
           inpStream = new FileInputStream(file);
           fileLoaded = true;
           int curChar;
           StringBuilder sbSettingsString = new StringBuilder();
           while((curChar = inpStream.read()) != -1) {
               sbSettingsString.append((char)curChar);
           }
           settingsString = sbSettingsString.toString();
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
        finally {
            try {
                if(inpStream != null) {
                    inpStream.close();
                }
            }
            catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
        if(!fileLoaded) {
            return false;
        }
        //get key-value and add to hashmap
        HashMap<String, String> settingsHashmap = new HashMap<String, String>();
        String[] settingsArray = settingsString.split(settingSeparator);
        for(String setting : settingsArray) {
            String[] keyValueArray = setting.split(keySeparator);
            if(keyValueArray.length < 2) {
                continue;
            }
            if(keyValueArray.length > 2) {
                for(int i=2; i < keyValueArray.length; i++) {
                    keyValueArray[1] += keySeparator + keyValueArray[i];
                }
            }
            //System.out.println("Setting: "+keyValueArray[0] + " = " + keyValueArray[1]);
            settingsHashmap.put(keyValueArray[0], keyValueArray[1]);
        }
        //get settings from hashmap
        setSftpHost(settingsHashmap.getOrDefault("sftp_host", null));
        try {
            Short tmp = settingsHashmap.getOrDefault("sftp_port",null) != null ? Short.parseShort(settingsHashmap.get("sftp_port")) : null;
            setSftpPort(tmp);
        }
        catch (NumberFormatException e) {
            System.out.println(e.getMessage());
            setSftpPort(null);
        }
        setSftpUser(settingsHashmap.getOrDefault("sftp_user", null));
        setSftpPassword(settingsHashmap.getOrDefault("sftp_password", null));
        setSftpRemoteDir(settingsHashmap.getOrDefault("sftp_remote_dir", null));
        setLocalDir(settingsHashmap.getOrDefault("local_dir", null));
        setSqlUser(settingsHashmap.getOrDefault("sql_user", ""));
        setSqlPassword(settingsHashmap.getOrDefault("sql_password", ""));
        setSqlDatabase(settingsHashmap.getOrDefault("sql_database", null));
        return checkSettings();
    }
    
    public boolean retrieveFromFile(String filename) { //test
        //boolean result = false;
        boolean fileLoaded = false;
        Properties props = new Properties();
        //file read
        File file = new File(filename);
        FileInputStream inpStream = null;
        try {
           inpStream = new FileInputStream(file);
           props.load(inpStream);
           fileLoaded = true;
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
        catch (IllegalArgumentException argEx) {
            System.out.println(argEx.getMessage());
        }
        finally {
            try {
                if(inpStream != null) {
                    inpStream.close();
                }
            }
            catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
        if(!fileLoaded) {
            return false;
        }
        //get properties
        setSftpHost(props.getProperty("sftp_host", null));
        try {
            Short tmp = props.getProperty("sftp_port",null) != null ? Short.parseShort(props.getProperty("sftp_port",null)) : null;
            setSftpPort(tmp);
        }
        catch (NumberFormatException e) {
            System.out.println(e.getMessage());
            setSftpPort(null);
        }
        setSftpUser(props.getProperty("sftp_user", null));
        setSftpPassword(props.getProperty("sftp_password", null));
        setSftpRemoteDir(props.getProperty("sftp_remote_dir", null));
        setLocalDir(props.getProperty("local_dir", null));
        setSqlUser(props.getProperty("sql_user", ""));
        setSqlPassword(props.getProperty("sql_password", ""));
        setSqlDatabase(props.getProperty("sql_database", null));
        // check if all settings has been read
        return checkSettings();
    }
    
    public String getSftpHost() {
        return sftpHost;
    }

    public Short getSftpPort() {
        return sftpPort;
    }

    public String getSftpUser() {
        return sftpUser;
    }

    public String getSftpPassword() {
        return sftpPassword;
    }

    public String getSftpRemoteDir() {
        return sftpRemoteDir;
    }

    public String getLocalDir() {
        return localDir;
    }

    public String getSqlUser() {
        return sqlUser;
    }

    public String getSqlPassword() {
        return sqlPassword;
    }

    public String getSqlDatabase() {
        return sqlDatabase;
    }

    private void setSftpHost(String sftpHost) {
        this.sftpHost = sftpHost;
    }

    private void setSftpPort(Short sftpPort) {
        this.sftpPort = sftpPort;
    }

    private void setSftpUser(String sftpUser) {
        this.sftpUser = sftpUser;
    }

    private void setSftpPassword(String sftpPassword) {
        this.sftpPassword = sftpPassword;
    }

    private void setSftpRemoteDir(String sftpRemoteDir) {
        this.sftpRemoteDir = sftpRemoteDir;
    }

    private void setLocalDir(String localDir) {
        this.localDir = localDir;
    }

    private void setSqlUser(String sqlUser) {
        this.sqlUser = sqlUser;
    }

    private void setSqlPassword(String sqlPassword) {
        this.sqlPassword = sqlPassword;
    }

    private void setSqlDatabase(String sqlDatabase) {
        this.sqlDatabase = sqlDatabase;
    }
    
}

public class Testapp {

    static Settings appSet;
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        if(args.length>0) appSet = new Settings();
        else {
            System.out.println("Error: No config file specified");
            return;
        }

        //if(!appSet.retrieveFromFile(args[0],"=","\r\n")) {
        if(!appSet.retrieveFromFile(args[0])) {
            System.out.println("Error: wrong config file");
            return;
        }
        //System.out.println(appSet.getSqlUser() + " Length: " + appSet.getSqlUser().length());
        FileDownloadDB filedb;
        if(!appSet.getSqlUser().equals("")) { // use mysql if sql_user specified
            System.out.println("Using MySQL DB");
            filedb = new MySQLDB(appSet.getSqlDatabase(),appSet.getSqlUser(),appSet.getSqlPassword());
        }
        else {
            System.out.println("Using SQLite DB"); // use sqlite if sql_user not specified
            filedb = new SQLiteDB(appSet.getSqlDatabase());
        }
        if(!filedb.connect()) {
            System.out.println("DB connection error");
        }
        filedb.createTable();
        SFTPClient sftpClient = new SFTPClient();
        sftpClient.connect(appSet.getSftpHost(), appSet.getSftpPort(), appSet.getSftpUser(), appSet.getSftpPassword()); // connecting to sftp server
        if(sftpClient.isConnected()) {
            String[] files = sftpClient.getFileList(appSet.getSftpRemoteDir()); //get file list at remotedir
            //System.out.println("File count = " +files.length);
            for(String f : files) {
             //System.out.println(f);
             if(sftpClient.getFile(appSet.getSftpRemoteDir()+f, appSet.getLocalDir())) { //download file
                 filedb.insertData(f); // insert data into db if downloaded correctly
             }
             else {
                 if(!sftpClient.isConnected()) {
                     System.out.println("SFTP Connection aborted");
                     break;
                 }
             }
            }
        }
        else {
            System.out.println("SFTP Connection error");
        }
        sftpClient.disconnect(); // close sftp connection
        filedb.disconnect(); // close db connection
    }   
}