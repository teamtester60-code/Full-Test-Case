package com.ferpfirstcode.utils.actions;

import com.ferpfirstcode.utils.logs.LogsManager;
import org.openqa.selenium.WebDriver;

import java.util.Set;

public class BrowserActions {
    private final WebDriver driver;

    public BrowserActions(WebDriver driver) {
        this.driver = driver;
    }

    public void navigateTo(String url) {
        driver.navigate().to(url);
        LogsManager.info("Navigated to URL: ", url);
    }

    public void maximizeWindow() {
        driver.manage().window().maximize();
    }

    public void refreshPage() {
        driver.navigate().refresh();
    }

    public String getcurrentUrl() {
        String currentUrl = driver.getCurrentUrl();
        LogsManager.info("Current URL: ", currentUrl);
        return currentUrl;

    }

    public void closeBrowser() {
        driver.close();
    }

    //open a new window
    public void openNewWindow() {
        driver.switchTo().newWindow(org.openqa.selenium.WindowType.WINDOW);
    }

    public void closeextensionTabs() {
        String originalHandle = driver.getWindowHandle();
        Set<String> allHandles = driver.getWindowHandles();

        for (String handle : allHandles) {
            if (!handle.equals(originalHandle)) {
                driver.switchTo().window(handle);
                driver.close();
            }
        }

        driver.switchTo().window(originalHandle);
    }
}
