package com.ferpfirstcode.driver;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ThreadGuard;

import com.ferpfirstcode.utils.actions.AlertActions;
import com.ferpfirstcode.utils.actions.BrowserActions;
import com.ferpfirstcode.utils.actions.ElementActions;
import com.ferpfirstcode.utils.actions.FrameActions;
import com.ferpfirstcode.utils.dataReader.PropertyReader;
import com.ferpfirstcode.utils.logs.LogsManager;
import com.ferpfirstcode.validations.Validation;
import com.ferpfirstcode.validations.Verification;

public class GUIDriver {

    private static final String DEFAULT_BROWSER = "EDGE";
    private static final String DEFAULT_EXECUTION_TYPE = "local"; // خليها local كـ default
    // ووقت التشغيل انت هتعمل override بـ -DexecutionType=localHeadless

    private final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();

    public GUIDriver() {
        LogsManager.info("Initializing GUIDriver...");

        // اقرأ من System أولاً ثم properties ثم default
        String browserTypeRaw = firstNonBlank(
                System.getProperty("browserType"),
                PropertyReader.getPropertyOrNull("browserType"),
                DEFAULT_BROWSER
        );

        String executionTypeRaw = firstNonBlank(
                System.getProperty("executionType"),
                PropertyReader.getPropertyOrNull("executionType"),
                DEFAULT_EXECUTION_TYPE
        );

        String browserName = browserTypeRaw.trim().toUpperCase();
        String executionType = executionTypeRaw.trim(); // خليه كما هو (localHeadless)
        // (ولو عايز normalizing خليها في Factory)

        LogsManager.info("SYS browserType = " + System.getProperty("browserType"));
        LogsManager.info("SYS executionType = " + System.getProperty("executionType"));
        LogsManager.info("Resolved browserType = " + browserName);
        LogsManager.info("Resolved executionType = " + executionType);

        // ثبّت في System عشان كل الـ Factories تشوف نفس القيمة
        System.setProperty("browserType", browserName);
        System.setProperty("executionType", executionType);

        Browser browserType;
        try {
            browserType = Browser.valueOf(browserName);
        } catch (IllegalArgumentException e) {
            LogsManager.error("Unknown browser: " + browserName + ". Using EDGE as default.");
            browserType = Browser.EDGE;
        }

        LogsManager.info("Starting driver for browser: " + browserType);

        AbstractDriver abstractDriver = browserType.getDriverFactory();
        WebDriver driver = ThreadGuard.protect(abstractDriver.createDriver());

        if (driver == null) {
            throw new RuntimeException("WebDriver initialization failed. driver is null.");
        }

        driverThreadLocal.set(driver);
    }

    private static String firstNonBlank(String... values) {
        for (String v : values) {
            if (v != null && !v.trim().isEmpty()) return v;
        }
        return null;
    }

    public ElementActions element() { return new ElementActions(get()); }
    public BrowserActions browser() { return new BrowserActions(get()); }
    public FrameActions frame() { return new FrameActions(get()); }
    public AlertActions alert() { return new AlertActions(get()); }
    public Validation validation() { return new Validation(get()); }
    public Verification verify() { return new Verification(get()); }

    public WebDriver get() { return driverThreadLocal.get(); }

    public void quitDriver() {
        WebDriver d = driverThreadLocal.get();
        if (d != null) {
            try { d.quit(); }
            finally { driverThreadLocal.remove(); }
        }
    }

    private final ThreadLocal<String> selectedOrderType = new ThreadLocal<>();

    public void setSelectedOrderType(String value) {
        selectedOrderType.set(value);
    }

    public String getSelectedOrderType() {
        return selectedOrderType.get();
    }

    public void clearSelectedOrderType() {
        selectedOrderType.remove();
    }

    public WebElement findElement(By locator) {
        return get().findElement(locator);
    }

}
