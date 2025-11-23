package com.ferpfirstcode.utils;


import com.ferpfirstcode.utils.dataReader.PropertyReader;

public class OSUtils {
    public enum OSType {
        WINDOWS,
        MAC,
        LINUX,
        OTHER
    }

    public static OSType getOperatingSystemType() {
        String osName = PropertyReader.getProperty("os.name").toLowerCase();
        if (osName.contains("win")) {
            return OSType.WINDOWS;
        } else if (osName.contains("mac")) {
            return OSType.MAC;
        } else if (osName.contains("nix") || osName.contains("nux") || osName.contains("aix")) {
            return OSType.LINUX;
        } else {
            return OSType.OTHER;
        }
    }
}
