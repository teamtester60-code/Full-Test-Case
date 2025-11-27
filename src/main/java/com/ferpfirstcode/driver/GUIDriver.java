//package com.ferpfirstcode.driver;
//
//import com.ferpfirstcode.utils.actions.AlertActions;
//import com.ferpfirstcode.utils.actions.BrowserActions;
//import com.ferpfirstcode.utils.actions.ElementActions;
//import com.ferpfirstcode.utils.actions.FrameActions;
//import com.ferpfirstcode.utils.dataReader.PropertyReader;
//import com.ferpfirstcode.utils.logs.LogsManager;
//import com.ferpfirstcode.validations.Validation;
//import com.ferpfirstcode.validations.Verification;
//import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.support.ThreadGuard;
//
//public class GUIDriver {
//
//    private final String browser = PropertyReader.getProperty("browserType");
//    private ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();
//
//    public GUIDriver() {
//        LogsManager.info("Initializing GUIDriver...");
//
//        String browserName = browser != null ? browser.trim().toUpperCase() : "";
//        LogsManager.info("قيمة المتصفح من الخصائص: " + browserName);
//
//        Browser browserType;
//        try {
//            browserType = Browser.valueOf(browserName);
//        } catch (IllegalArgumentException e) {
//            LogsManager.error("نوع المتصفح غير معروف: " + browserName + ". سيتم استخدام CHROME كافتراضي.");
//            browserType = Browser.CHROME;
//        }
//
//
//        LogsManager.info("Starting driver for browser: " + browserType);
//
//        AbstractDriver abstractDriver = browserType.getDriverFactory();
//        WebDriver driver = ThreadGuard.protect(abstractDriver.createDriver());
//        driverThreadLocal.set(driver);
//    }
//
//
//    public ElementActions element() {
//        return new ElementActions(get());
//    }
//
//    public BrowserActions browser() {
//        return new BrowserActions(get());
//    }
//
//    public FrameActions frame() {
//        return new FrameActions(get());
//    }
//
//    public AlertActions alert() {
//        return new AlertActions(get());
//    }
//
//    //soft assertions
//    public Validation validation() {
//        return new Validation(get());
//    }
//
//    // hard assertions
//    public Verification verify() {
//        return new Verification(get());
//    }
//
//    public WebDriver get() {
//        return driverThreadLocal.get();
//    }
//
//    public void quitDriver() {
//        driverThreadLocal.get().quit();
//    }
//}


package com.ferpfirstcode.driver;

import com.ferpfirstcode.utils.actions.AlertActions;
import com.ferpfirstcode.utils.actions.BrowserActions;
import com.ferpfirstcode.utils.actions.ElementActions;
import com.ferpfirstcode.utils.actions.FrameActions;
import com.ferpfirstcode.utils.dataReader.PropertyReader;
import com.ferpfirstcode.utils.logs.LogsManager;
import com.ferpfirstcode.validations.Validation;
import com.ferpfirstcode.validations.Verification;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ThreadGuard;

public class GUIDriver {

    private final String browser = PropertyReader.getProperty("browserType");
    private final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();

    public GUIDriver() {
        LogsManager.info("Initializing GUIDriver...");

        String browserName = browser != null ? browser.trim().toUpperCase() : "";
        LogsManager.info("Browser from properties: " + browserName);

        Browser browserType;
        try {
            browserType = Browser.valueOf(browserName);
        } catch (IllegalArgumentException e) {
            LogsManager.error("Unknown browser: " + browserName + ". Using CHROME as default.");
            browserType = Browser.CHROME;
        }

        LogsManager.info("Starting driver for browser: " + browserType);
        AbstractDriver abstractDriver = browserType.getDriverFactory();
        WebDriver driver = ThreadGuard.protect(abstractDriver.createDriver());
        driverThreadLocal.set(driver);
    }

    public ElementActions element() {
        return new ElementActions(get());
    }

    public BrowserActions browser() {
        return new BrowserActions(get());
    }

    public FrameActions frame() {
        return new FrameActions(get());
    }

    public AlertActions alert() {
        return new AlertActions(get());
    }

    public Validation validation() {
        return new Validation(get());
    }

    public Verification verify() {
        return new Verification(get());
    }

    public WebDriver get() {
        return driverThreadLocal.get();
    }

    public void quitDriver() {
        if (driverThreadLocal.get() != null) {
            driverThreadLocal.get().quit();
        }
    }
}
