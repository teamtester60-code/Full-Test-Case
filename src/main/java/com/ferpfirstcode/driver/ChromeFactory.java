package com.ferpfirstcode.driver;

import com.ferpfirstcode.utils.dataReader.PropertyReader;
import com.ferpfirstcode.utils.logs.LogsManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;


public class ChromeFactory extends AbstractDriver {
    String executionType = PropertyReader.getPropertyOrDefault("executionType", "local");


    static { PropertyReader.loadProperties(); }

    private ChromeOptions getChromeOptions(String executionType) {
        ChromeOptions options = new ChromeOptions();

        options.addArguments(
                "--remote-allow-origins=*",
                "--disable-notifications",
                "--window-size=1920,1080"
        );

        switch (executionType.toLowerCase()) {
            case "localheadless" -> {
                options.addArguments("--headless=new", "--no-sandbox", "--disable-dev-shm-usage", "--disable-gpu",
                        "--remote-debugging-port=9222");
            }
            case "remote" -> {
                options.addArguments("--headless=new", "--disable-gpu", "--disable-extensions", "--no-sandbox",
                        "--disable-dev-shm-usage");
            }
            default -> {
                // local (normal) أو أي future types
            }
        }

        return options;
    }

    @Override
public WebDriver createDriver() {
    String executionType = PropertyReader.getPropertyOrDefault("executionType", "local");
    LogsManager.info("Chrome executionType = " + executionType);

    if (executionType.equalsIgnoreCase("local") || executionType.equalsIgnoreCase("localHeadless")) {
        return new ChromeDriver(getChromeOptions(executionType));
    } else if (executionType.equalsIgnoreCase("Remote")) {
        LogsManager.error("Remote execution type is not supported yet");
        throw new RuntimeException("Remote execution type is not supported yet");
    } else {
        LogsManager.error("invalid execution type: " + executionType);
        throw new RuntimeException("Invalid execution type for Chrome Driver: " + executionType);
    }
}

}
