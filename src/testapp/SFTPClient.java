/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testapp;

import com.jcraft.jsch.*;
import java.util.Vector;

/**
 *
 * @author user
 */

public class SFTPClient {

    private static JSch jsch = null;
    private Session session = null;
    private ChannelSftp channelSftp = null;

    public SFTPClient() {
        if (jsch == null) {
            try {
                jsch = new JSch();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public boolean isConnected() {
        if (channelSftp != null) {
            return channelSftp.isConnected();
        } else {
            return false;
        }
    }

    public boolean connect(String host, Short port, String user, String password) {
        if (jsch == null) {
            return false;
        }
        boolean result = true;
        try {
            //create session and connect to sftp server
            session = jsch.getSession(user, host, port);
            session.setPassword(password);
            session.setConfig("StrictHostKeyChecking", "no"); // no key check
            session.connect();
            Channel channel = session.openChannel("sftp");
            channel.connect();
            this.channelSftp = (ChannelSftp) channel;
        } catch (JSchException e) {
            System.out.println(e.getMessage());
            result = false;
        }
        /*catch (SftpException e) {
          System.out.println(e.getMessage());
          result = false;
      }*/
        return result;
    }

    public String[] getFileList(String dir) {
        Vector<String> filenames = new Vector<String>();
        try {
            Vector lsEntryVect = this.channelSftp.ls(dir); //get all files in directories
            if (lsEntryVect != null) {
                for (int i = 0; i < lsEntryVect.size(); i++) {
                    Object obj = lsEntryVect.elementAt(i); //get full file entry
                    if (obj instanceof ChannelSftp.LsEntry) {
                        SftpATTRS fileAttrs = ((ChannelSftp.LsEntry) obj).getAttrs(); // get file attributes
                        if (!fileAttrs.isDir() && !fileAttrs.isLink()) { // get no directories or symlinks
                            filenames.add(((ChannelSftp.LsEntry) obj).getFilename()); 
                        }
                    }
                }
            }
        } catch (SftpException e) {
            System.out.println(e.toString());
        }
        return filenames.toArray(new String[0]);
    }

    public boolean getFile(String path, String dest) {
        boolean result = true;
        if (!isConnected()) {
            return false;
        }
        try {
            SftpProgressMonitor monitor = new MyProgressMonitor(); //to get download progress
            int mode = ChannelSftp.OVERWRITE;
            this.channelSftp.get(path, dest, monitor, mode);
        } catch (SftpException e) {
            System.out.println(e.toString());
            result = false;
        }
        return result;
    }

    public void disconnect() {
        //close all
        try {
            if (channelSftp != null) {
                channelSftp.quit();
                ((Channel) channelSftp).disconnect();
            }
            if (session != null) {
                session.disconnect();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        /*catch (SftpException e) {
        System.out.println(e.getMessage());
     }*/
    }

    public static class MyProgressMonitor implements SftpProgressMonitor { //to show download progress

        long count = 0;
        long max = 0;
        String filePath;

        @Override
        public void init(int op, String src, String dest, long max) {
            this.max = max;
            count = 0;
            percent = -1;
            this.filePath = src;
            System.out.print(this.filePath + " starting...");
        }
        private long percent = -1;

        @Override
        public boolean count(long count) {
            this.count += count;
            if (percent >= this.count * 100 / max) {
                return true;
            }
            percent = this.count * 100 / max;
            System.out.print("\r" + this.filePath + " " + this.count + "/" + max + " " + percent + "%");

            return true; //false to interrupt
        }

        @Override
        public void end() {
            if (this.count < this.max) {
                System.out.print(" Error\n");
            }
            else System.out.println();
        }
    }
}
