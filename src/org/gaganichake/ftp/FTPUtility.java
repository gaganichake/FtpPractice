package org.gaganichake.ftp;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FTPUtility {

    public static String HOST = "localhost";
    //public static String HOST = "127.0.0.1";

    public static int PORT = 21;

    // Anonymous User's credential
    //public static String USERID = "anonymous";
    //public static String PASSWORD = "";
    // Administrator's credential
    public static String USERID = "myusername";
    public static String PASSWORD = "myuserpassword";

    /**
     * This method will send files on FTP server
     *
     * @param fileNames String
     * @return String
     */
    public static boolean sendFiles(List<String> fileNames) throws Throwable {
        System.out.println("sendFiles() : START");
        FTPClient ftpClient = new FTPClient();
        if (loginToFtpServer(ftpClient)) {
            File file;
            FileInputStream fis = null;
            try {
                final List<String> failedFiles = new ArrayList<String>();
                String fileName;

                for (String name : fileNames) {
                    file = new File(name);
                    fis = new FileInputStream(file);
                    fileName = file.getName();
                    System.out.println("Sending file: " + fileName);
                    boolean success = ftpClient.storeFile(fileName, fis);
                    if (!success) {
                        failedFiles.add(fileName);
                    }
                    fis.close();
                }

                if (failedFiles.size() > 0) {
                    System.out.println("Failed to send files on FTP server. Files: " + failedFiles);
                } else {
                    System.out.println("FTP operation successful");
                }

                if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
                    showServerReply(ftpClient);
                    return false;
                }
            } catch (Throwable e) {
                e.printStackTrace();
                throw e;
            } finally {
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        System.out.println("Could not close FTP Client");
                        e.printStackTrace();
                    }
                }
                logoutToFtpServer(ftpClient);
            }
        }
        System.out.println("sendFiles() : END");
        return false;
    }

    /**
     * This method will delete files on FTP server
     */
    public static void removeFiles(List<String> fileNames) throws Throwable {
        System.out.println("removeFiles() : START");
        FTPClient ftpClient = new FTPClient();
        if (loginToFtpServer(ftpClient)) {
            try {
                final List<String> failedFiles = new ArrayList<String>();
                for (String fileName : fileNames) {
                    System.out.println("Removing file: " + fileName);
                    boolean success = ftpClient.deleteFile(fileName);
                    if (!success) {
                        failedFiles.add(fileName);
                    }
                }

                if (failedFiles.size() > 0) {
                    System.out.println("Failed to remove files from FTP server. Files: " + failedFiles);
                } else {
                    System.out.println("FTP operation successful");
                }
            } catch (Throwable e) {
                e.printStackTrace();
                throw e;
            } finally {
                logoutToFtpServer(ftpClient);
            }
        }
        System.out.println("removeFiles() : END");
    }

    private static boolean loginToFtpServer(FTPClient ftpClient) throws Throwable {
        try {
            System.out.println("Connecting to FTP server " + HOST);
            ftpClient.connect(HOST, PORT);
            showServerReply(ftpClient);

            if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
                showServerReply(ftpClient);
                return false;
            }

            if (ftpClient.isConnected()) {
                System.out.println("Connected to " + HOST);
            } else {
                return false;
            }

            System.out.println("Trying to login to FTP server...");
            boolean success = ftpClient.login(USERID, PASSWORD);
            showServerReply(ftpClient);

            if (!success) {
                System.out.println("Could not login to FTP server");
                return false;
            } else {
                System.out.println("Logged in to " + HOST);
            }

            if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
                showServerReply(ftpClient);
                return false;
            }

            return true;
        } catch (Throwable e) {
            e.printStackTrace();
            throw e;
        }
    }

    private static void logoutToFtpServer(FTPClient ftpClient) {
        if (ftpClient != null) {
            try {
                ftpClient.logout();
                showServerReply(ftpClient);
                ftpClient.disconnect();
            } catch (Throwable e) {
                System.out.println("Could not logout/disconnect FTP server!");
                e.printStackTrace();
            }
        }
    }

    private static void showServerReply(FTPClient ftpClient) {
        System.out.println("SERVER: " + ftpClient.getReplyString());
        String[] replies = ftpClient.getReplyStrings();
        if (replies != null && replies.length > 0) {
            for (String reply : replies) {
                if (reply != null)
                    System.out.println("SERVER: " + reply);
            }
        }
    }

}
