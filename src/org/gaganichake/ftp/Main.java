package org.gaganichake.ftp;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws Throwable {

        List<String> fileNamesToSend = new ArrayList<String>();
        fileNamesToSend.add("C:\\Users\\Gagan\\Downloads\\IDBI Salary Overdraft Facility.pdf");
        fileNamesToSend.add("C:\\Users\\Gagan\\Downloads\\IMG_1182.JPG");
        FTPUtility.sendFiles(fileNamesToSend);

        List<String> fileNamesToRemove = new ArrayList<String>();
        fileNamesToRemove.add("IDBI Salary Overdraft Facility.pdf");
        fileNamesToRemove.add("MG_1182.JPG");
        FTPUtility.removeFiles(fileNamesToRemove);
    }

}

