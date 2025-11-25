package com.ferpfirstcode.driver;

import com.ferpfirstcode.utils.dataReader.PropertyReader;
import com.ferpfirstcode.utils.logs.LogsManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URI;

public class EdgeFactory extends AbstractDriver {
    static {
    PropertyReader.loadProperties();
}


    private EdgeOptions getEdgeOptions() {
        EdgeOptions options = new EdgeOptions();
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--disable-notifications");
        options.addArguments("--start-maximized");
        // options.addExtensions(blurimageextensions);
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

            return new EdgeDriver(getEdgeOptions());
        } else if (PropertyReader.getProperty("executionType").equalsIgnoreCase("Remote")) {
            try {

                return new RemoteWebDriver(
                        new URI("http://" + remoteHost + ":" + remoteport + "/wd/hub").toURL(), getEdgeOptions()

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
