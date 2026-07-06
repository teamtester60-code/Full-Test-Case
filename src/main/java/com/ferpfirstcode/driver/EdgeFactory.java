package com.ferpfirstcode.driver;

import com.ferpfirstcode.utils.dataReader.PropertyReader;
import com.ferpfirstcode.utils.logs.LogsManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class EdgeFactory extends AbstractDriver {

    static { PropertyReader.loadProperties(); }

    private static final String DEFAULT_EXECUTION = "local";

    private EdgeOptions buildOptions(String executionType) {
        EdgeOptions options = new EdgeOptions();

        // 1. Disable Password Manager and Save Password prompts
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);
        options.setExperimentalOption("prefs", prefs);

        // 2. Hide automation bar and disable automation extension
        options.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
        options.setExperimentalOption("useAutomationExtension", false);

        // 3. Base options
        options.addArguments(
                "--remote-allow-origins=*",
                "--disable-notifications",
                "--window-size=1920,1080",
                "--start-maximized"
        );

        // 4. Handle Execution Types
        String exec = executionType == null ? DEFAULT_EXECUTION : executionType.trim().toLowerCase();

        if ("localheadless".equals(exec)) {
            options.addArguments(
                    "--headless=new",
                    "--disable-gpu",
                    "--disable-extensions",
                    "--no-sandbox",
                    "--disable-dev-shm-usage"
            );
        } else if ("remote".equals(exec)) {
            options.addArguments("--headless=new", "--disable-gpu", "--disable-extensions");
        }

        return options;
    }

    @Override
    public WebDriver createDriver() {
        String executionType = PropertyReader.getPropertyOrDefault("executionType", DEFAULT_EXECUTION);
        LogsManager.info("Edge executionType = " + executionType);

        if (executionType.equalsIgnoreCase("local") || executionType.equalsIgnoreCase("localHeadless")) {
            return new EdgeDriver(buildOptions(executionType));
        }

        if (executionType.equalsIgnoreCase("remote")) {
            LogsManager.error("Remote execution type is not supported yet");
            throw new RuntimeException("Remote execution type is not supported yet");
        }

        LogsManager.error("Invalid execution type: " + executionType);
        throw new RuntimeException("Invalid execution type for Edge Driver: " + executionType);
    }
}