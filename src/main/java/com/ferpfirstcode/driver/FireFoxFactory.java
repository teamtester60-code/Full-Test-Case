package com.ferpfirstcode.driver;

import com.ferpfirstcode.utils.dataReader.PropertyReader;
import com.ferpfirstcode.utils.logs.LogsManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URI;

public class FireFoxFactory extends AbstractDriver {

    private FirefoxOptions getfirefoxoption() {
        ;
        FirefoxOptions options = new FirefoxOptions();
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--disable-notifications");
        options.addArguments("--start-maximized");
        FirefoxProfile profile = new FirefoxProfile();
        //    profile.addExtension(blurimageextensions);
        options.setProfile(profile);
        switch (PropertyReader.getProperty("executionType")) {
            case "localHeadless" -> options.addArguments("--headless=new");
            case "Remote" -> {
                options.addArguments("--headless=new");
                options.addArguments("--disable-gpu");
                options.addArguments("--disable-extensions");
            }
        }
        return options;
    }

    @Override
    public WebDriver createDriver() {
        if (PropertyReader.getProperty("executionType").equalsIgnoreCase("local") ||
                PropertyReader.getProperty("executionType").equalsIgnoreCase("localHeadless")) {

            return new FirefoxDriver(getfirefoxoption());
        } else if (PropertyReader.getProperty("executionType").equalsIgnoreCase("Remote")) {
            try {

                return new RemoteWebDriver(
                        new URI("http://" + remoteHost + ":" + remoteport + "/wd/hub").toURL(), getfirefoxoption()

                );
            } catch (Exception e) {
                LogsManager.error("Error Creating RemoteWebDriver:" + e.getMessage());
                throw new RuntimeException("Failed To Create RemoteWebDriver", e);
            }
        } else {
            LogsManager.error("invalid execution type:" + PropertyReader.getProperty("executionType"));
            throw new RuntimeException("Invalid execution type for Edge Driver");
        }

    }
}
