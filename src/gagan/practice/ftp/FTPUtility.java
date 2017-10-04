package gagan.practice.ftp;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

public class FTPUtility {
	
	public static String HOST = "localhost";
	// OR
	//public static String HOST = "127.0.0.1";
	
	public static int PORT = 21;
	
	// Anonymous User's credential
	//public static String USERID = "anonymous";
	//public static String PASSWORD = "";
	
	// OR
	// Administrator's credential
	public static String USERID = "gagan";
	public static String PASSWORD = "gagan";
    	
    /**
     * This method will send files on FTP server
     * 
     * @param fileName String
     * @return String
     * @throws Throwable 
     */
    public static boolean sendFiles(List<String> fileNames) throws Throwable {
    	System.out.println("sendFiles() : START");
    	FTPClient ftpClient = new FTPClient();
    	if(loginToFtpServer(ftpClient)){
    		File file = null;
        	FileInputStream fis = null;
    		try {
        		final List<String> failedFiles = new ArrayList<String>();
        		String fileName = null;
        		
	             for (int i = 0; i < fileNames.size(); i++) {
	             	file = new File(fileNames.get(i));
	             	fis = new FileInputStream(file);
	             	fileName = file.getName();
	             	System.out.println("Sending file: " + fileName);
	             	boolean success = ftpClient.storeFile(fileName, fis);
	             	if(!success){
	             		failedFiles.add(fileName);
	             	} 
	             	fis.close();
	 			   }
             
	             if(failedFiles.size() > 0){
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
	            		throw e;
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
     * 
     * @throws Throwable 
     */
	public static void removeFiles(List<String> fileNames) throws Throwable {
		System.out.println("removeFiles() : START");
		FTPClient ftpClient = new FTPClient();
		if(loginToFtpServer(ftpClient)){
			try {
				final List<String> failedFiles = new ArrayList<String>();
				for(String fileName : fileNames){
					System.out.println("Removing file: " + fileName);
					boolean success = ftpClient.deleteFile(fileName);
					if(!success){
	             		failedFiles.add(fileName);
	             	} 
				}
				
				if(failedFiles.size() > 0){
	         		System.out.println("Failed to remove files from FTP server. Files: " + failedFiles);
	             } else {
	             	System.out.println("FTP operation successful");
	             }
			}catch (Throwable e) {
				e.printStackTrace();
				throw e;
			}finally{
				logoutToFtpServer(ftpClient); 
			}
		}
		System.out.println("removeFiles() : END");
	}
    
    private static boolean loginToFtpServer(FTPClient ftpClient) throws Throwable{
    	try {
    		System.out.println("Connecting to FTP server "+HOST);
            ftpClient.connect(HOST, PORT);            
            showServerReply(ftpClient);
            
            if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
            	showServerReply(ftpClient);
                return false;
            }
            
            if(ftpClient.isConnected()) {
            	System.out.println("Connected to "+HOST);
            } else {
            	return false;
            }
            
            System.out.println("Trying to login to FTP server...");
            boolean success = ftpClient.login(USERID, PASSWORD);
            showServerReply(ftpClient);
            
            if (success == false) {
                System.out.println("Could not login to FTP server");
                return false;
            } else {
                System.out.println("Logged in to "+HOST);
            }
            
            if (!FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
            	showServerReply(ftpClient);
                return false;
            }
            
            return success;
        } catch (Throwable e) {
        	e.printStackTrace();
        	throw e;
        }
    }
    
    private static void logoutToFtpServer(FTPClient ftpClient) throws Throwable{
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
            	if(reply != null)            	
            		System.out.println("SERVER: " + reply);
            }
        }
    }
    
 }
