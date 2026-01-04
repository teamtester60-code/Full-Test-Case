package com.ferpfirstcode.utils;

import java.io.File;

public class SnagitUtils {

     public static File waitForLatestFile(
            String directoryPath,
            long timeoutSeconds
    ) {
        File dir = new File(directoryPath);
        long endTime = System.currentTimeMillis() + timeoutSeconds * 1000;

        File latestFile = null;
        long lastModified = 0;

        while (System.currentTimeMillis() < endTime) {
            File[] files = dir.listFiles((d, name) ->
                    name.endsWith(".png") || name.endsWith(".jpg"));

            if (files != null && files.length > 0) {
                for (File file : files) {
                    if (file.lastModified() > lastModified) {
                        latestFile = file;
                        lastModified = file.lastModified();
                    }
                }
                if (latestFile != null) {
                    return latestFile;
                }
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException ignored) {}
        }

        throw new RuntimeException("Snagit capture file not found within timeout");
    }
}
