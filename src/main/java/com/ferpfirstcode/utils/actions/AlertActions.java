package com.ferpfirstcode.utils.actions;

import com.ferpfirstcode.utils.WaitsManager;
import com.ferpfirstcode.utils.logs.LogsManager;
import org.openqa.selenium.WebDriver;

public class AlertActions {
    private final WebDriver driver;
    private final WaitsManager waitsManager;

    public AlertActions(WebDriver driver) {
        this.driver = driver;
        this.waitsManager = new WaitsManager(driver);
    }

    public void acceptAlert() {
        waitsManager.fluentWait().until(d -> {
            try {
                d.switchTo().alert().accept();
                return true;
            } catch (Exception e) {
                LogsManager.error("Failed to accept.", e.getMessage());
                return false;
            }
        });
    }

    public void dismissAlert() {
        waitsManager.fluentWait().until(d -> {
            try {
                d.switchTo().alert().dismiss();
                return true;
            } catch (Exception e) {
                LogsManager.error("Failed to dismiss.", e.getMessage());
                return false;
            }
        });
    }

    public String getAlertText() {
        return waitsManager.fluentWait().until(d -> {
            try {
                String text = d.switchTo().alert().getText();
                return !text.isEmpty() ? text : null;
            } catch (Exception e) {
                LogsManager.error("Failed to get text from.", e.getMessage());
                return null;
            }
        });

    }

    public void typeInAlert(String text) {
        waitsManager.fluentWait().until(d -> {
            try {
                d.switchTo().alert().sendKeys(text);
                return true;
            } catch (Exception e) {
                LogsManager.error("Failed to type in.", e.getMessage());
                return false;
            }
        });
    }
}
