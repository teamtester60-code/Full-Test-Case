package com.ferpfirstcode.driver;

import com.ferpfirstcode.utils.dataReader.PropertyReader;
import com.ferpfirstcode.utils.logs.LogsManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;

public class EdgeFactory extends AbstractDriver {

    static { PropertyReader.loadProperties(); }

    private static final String DEFAULT_EXECUTION = "local";

    private EdgeOptions buildOptions(String executionType) {
        EdgeOptions options = new EdgeOptions();

        // Base options (always)
        options.addArguments(
                "--remote-allow-origins=*",
                "--disable-notifications",
                "--start-maximized"
        );

        // Normalize
        String exec = executionType == null ? DEFAULT_EXECUTION : executionType.trim().toLowerCase();

        if ("localheadless".equals(exec)) {
            options.addArguments(
                    "--headless=new",
                    "--disable-gpu",
                    "--disable-extensions"
            );

            // على Windows غالبًا غير ضرورية، لكن لا تضر
            options.addArguments("--no-sandbox", "--disable-dev-shm-usage");

            // ⚠️ لو عندك تعارض بورت أو parallel شيلها
            // options.addArguments("--remote-debugging-port=9222");

        } else if ("remote".equals(exec)) {
            // أنت قلت remote غير مدعوم عندك حاليا
            // لو هتدعمه لاحقًا هنرجع نضيف RemoteWebDriver هنا
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
