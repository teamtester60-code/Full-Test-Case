package com.ferpfirstcode.driver;

import com.ferpfirstcode.utils.dataReader.PropertyReader;
import com.ferpfirstcode.utils.logs.LogsManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URI;

public class ChromeFactory extends AbstractDriver {

    static {
        PropertyReader.loadProperties();
    }


    private ChromeOptions getChromeOptions() {
        ChromeOptions options = new ChromeOptions();

        // خيارات عامة
        options.addArguments(
                "--remote-allow-origins=*",
                "--disable-notifications",
                "--window-size=1920,1080" // بدلاً من start-maximized في headless
        );

        // قراءة نوع التنفيذ
        String executionType = PropertyReader.getProperty("executionType");

        switch (executionType) {
            case "localHeadless" -> {
                options.addArguments("--headless=new");
                options.addArguments("--no-sandbox");          // مهم في CI/Docker
                options.addArguments("--disable-dev-shm-usage");
                options.addArguments("--disable-gpu");
                options.addArguments("--remote-debugging-port=9222"); // أحيانًا يساعد في CI
// لتجنب مشاكل shared memory
            }
            case "Remote" -> {
                options.addArguments(
                        "--headless=new",
                        "--disable-gpu",
                        "--disable-extensions",
                        "--no-sandbox",
                        "--disable-dev-shm-usage"
                );
            }
            default -> {
                // يمكن إضافة إعدادات خاصة بأنواع أخرى من التنفيذ
            }
        }

        // إذا كنت تحتاج إضافة Extensions
        // options.addExtensions(blurimageextensions);

        return options;
    }


    @Override
    public WebDriver createDriver() {
        if (PropertyReader.getProperty("executionType").equalsIgnoreCase("local") ||
                PropertyReader.getProperty("executionType").equalsIgnoreCase("localHeadless")) {

            return new ChromeDriver(getChromeOptions());
        } else if (PropertyReader.getProperty("executionType").equalsIgnoreCase("Remote")) {
            try {

                return new RemoteWebDriver(
                        new URI("http://" + remoteHost + ":" + remoteport + "/wd/hub").toURL(), getChromeOptions()

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