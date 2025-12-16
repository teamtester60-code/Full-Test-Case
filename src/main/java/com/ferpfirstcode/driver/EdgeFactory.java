package com.ferpfirstcode.driver;

import com.ferpfirstcode.utils.dataReader.PropertyReader;
import com.ferpfirstcode.utils.logs.LogsManager;
<<<<<<< HEAD
import org.openqa.selenium.JavascriptExecutor;
=======
>>>>>>> 6f52845df553065a0249f92ceeed6f22ac3e9a8e
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URI;

<<<<<<< HEAD

=======
>>>>>>> 6f52845df553065a0249f92ceeed6f22ac3e9a8e
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
<<<<<<< HEAD
            case "localHeadless", "Remote" -> {
                options.addArguments("--headless=new"); // استخدام الوضع Headless الجديد
                options.addArguments("--window-size=1920,1080");
                options.addArguments("--disable-gpu");
                options.addArguments("--no-sandbox");
                options.addArguments("--disable-dev-shm-usage");
=======
            case "localHeadless" -> options.addArguments("--headless=new");
            case "Remote" -> {
                options.addArguments("--headless=new");
                options.addArguments("--disable-gpu");
                options.addArguments("--disable-extensions");
>>>>>>> 6f52845df553065a0249f92ceeed6f22ac3e9a8e
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
