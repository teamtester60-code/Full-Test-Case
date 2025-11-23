package com.ferpfirstcode;

import com.ferpfirstcode.utils.dataReader.PropertyReader;
import com.ferpfirstcode.utils.logs.LogsManager;

import java.io.File;
import java.io.IOException;

import static org.apache.commons.io.FileUtils.copyFile;

public class FileUtils {
    private static final String USER_DIR = PropertyReader.getProperty("user.dir") + File.separator;

    private FileUtils() {
        // Private constructor to prevent instantiation
    }

    //rename method
    public static void renameFile(String oldName, String newName) {
        try {
            var targetFile = new File(oldName);
            String targetDirectory = targetFile.getParentFile().getAbsolutePath();
            File newFile = new File(targetDirectory + File.separator + newName);
            if (!targetFile.getPath().equals(newFile.getPath())) {
                copyFile(targetFile, newFile);
                org.apache.commons.io.FileUtils.deleteQuietly(targetFile);
                LogsManager.info("Target File Path: \"" + oldName + "\", file was renamed to \"" + newName + "\".");
            } else {
                LogsManager.info(("Target File Path: \"" + oldName + "\", already has the desired name \"" + newName + "\"."));
            }
        } catch (IOException e) {
            LogsManager.error(e.getMessage());
        }
    }


    //create directory method
    public static void createDirectory(String path) {
        try {
            File file = new File(USER_DIR + path);
            if (!file.exists()) {
                boolean created = file.mkdirs();
                if (created) {
                    LogsManager.info("Directory created: " + path);
                } else {
                    LogsManager.warn("Failed to create directory: " + path);
                }
            } else {
                LogsManager.info("Directory already exists: " + path);
            }
        } catch (Exception e) {
            LogsManager.error("Error creating directory: " + path, e.getMessage());
        }
    }


    //force delete directory method
    public static void forceDeleteDirectory(File file) {
        try {
            org.apache.commons.io.FileUtils.deleteDirectory(file);
        } catch (Exception e) {
            LogsManager.error("Error force deleting directory: " + file.getAbsolutePath(), e.getMessage());
        }
    }


    //clean directory method
    public static void cleanDirectory(File file) {
        try {
            org.apache.commons.io.FileUtils.deleteQuietly(file);
        } catch (Exception e) {
            LogsManager.error("Error cleaning directory: " + file.getAbsolutePath(), e.getMessage());
        }

    }

    public static void cleanLogs(String logDirectoryPath) {
        File logDir = new File(logDirectoryPath);
        if (logDir.exists() && logDir.isDirectory()) {
            try {
                FileUtils.cleanDirectory(logDir);
                System.out.println("✅ Logs cleaned successfully from: " + logDirectoryPath);
            } catch (Exception e) {
                System.err.println("❌ Failed to clean logs: " + e.getMessage());
            }
        } else {
            System.out.println("⚠️ Log directory does not exist or is not a directory: " + logDirectoryPath);
        }
    }

    public static void deleteLogFile(String logFilePath) {
        File logFile = new File(logFilePath);
        if (logFile.exists() && logFile.isFile()) {
            try {
                FileUtils.forceDelete(logFile);
                System.out.println("✅ Log file deleted: " + logFilePath);
            } catch (Exception e) {
                System.err.println("❌ Failed to delete log file: " + e.getMessage());
            }
        } else {
            System.out.println("⚠️ Log file does not exist: " + logFilePath);
        }
    }

    private static void forceDelete(File logFile) {
    }
}
