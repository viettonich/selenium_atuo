/*
* LogUtils
* Version 1.1
* Copyright @FPT Software
*/

package com.autotest.utils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;

public class LogUtils {

    // Initialize Log4j logs
    private static Logger log = Logger.getLogger(LogUtils.class.getName());

    public static void shutdown() {
        LogManager.shutdown();
        log.removeAllAppenders();
    }

    /**
     * @param inputString
     * @param args
     */
    public static void infoToFile(String inputString, String... args) {
        log.info(inputString);
    }

    public static void logStart(String name, Logger log) {
        String message = "<<<<< " + name + " - START >>>>>";
        log.info(message);
    }

    public static void logEnd(String name, Logger log) {
        String message = "<<<<< " + name + " - END >>>>>";
        log.info(message);
    }

    public static void logStopAtRow(int row, Logger log) {
        String message = "Stop at row " + row;
        log.info(message);
    }

    public static void configLog4j() {
        // Set name of log file
        Date today = new Date();
        SimpleDateFormat dateFormaterTemp = new SimpleDateFormat("yyyyMMddhhmmssS");

        String fileLogTemp;

        fileLogTemp = System.getProperty("user.dir") + "/LOG/log_" + dateFormaterTemp.format(today) + ".log";

        // This is the root logger provided by log4j
        Logger rootLogger = Logger.getRootLogger();
        rootLogger.setLevel(Level.DEBUG);

        // Define log pattern layout
        PatternLayout layout = new PatternLayout("%d{ISO8601} [%t] %-5p %c %x - %m%n");

        // Add console appender to root logger
        rootLogger.addAppender(new ConsoleAppender(layout));
        try {
            // Define file appender with layout and output log file name
            RollingFileAppender fileAppender = new RollingFileAppender(layout, fileLogTemp);

            // Add the appender to root logger
            rootLogger.addAppender(fileAppender);
        } catch (IOException e) {
            System.out.println("Failed to add appender !!");
        }
    }

    public static void copyFileLog(boolean isCompare) {
//        // Set name of log file
//        Date today = new Date();
//        SimpleDateFormat dateFormaterNew = new SimpleDateFormat(
//                Constants.LOG_FILE_NAME_FORMAT);
//        
//        String folderReport = Constants.PATH_FOLDER_OUTPUT;
//        folderReport = folderReport.replace("\\", "/");
//        
//        String fileLogNew = Constants.BLANK;
//        
//        fileLogNew = folderReport + "/" + Constants.LOG_FOLDER + "/"
//                + dateFormaterNew.format(today) + Constants.LOG_FILE_EXTENSION;
//        
//        File newFile = null;
//        File tempFile = null;
//        
//        try {
//            newFile = new File(fileLogNew);
//            tempFile = new File(Constants.PATH_LOG_FILE);
//            
//            if (isCompare) {
//                Common.joinFile(fileLogNew, Constants.PATH_LOG_FILE);
//            } else {
//                Files.copy(tempFile, newFile);
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        // Set file log temporary
        configLog4j();
    }
}
