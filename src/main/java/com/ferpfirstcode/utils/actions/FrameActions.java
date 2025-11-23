package com.ferpfirstcode.utils.actions;

import com.ferpfirstcode.utils.WaitsManager;
import com.ferpfirstcode.utils.logs.LogsManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class FrameActions {
    private final WebDriver driver;
    private final WaitsManager waitsManager;

    public FrameActions(WebDriver driver) {
        this.driver = driver;
        this.waitsManager = new WaitsManager(driver);
    }

    public void switchToFrameByIndex(int index) {
        waitsManager.fluentWait().until(d -> {
            try {
                d.switchTo().frame(index);
                LogsManager.info("switched to frame " + index);
                return true;
            } catch (Exception e) {
                return false;
            }
        });
    }

    public void switchToFrameByNameOrId(String nameOrId) {
        waitsManager.fluentWait().until(d -> {
            try {
                d.switchTo().frame(nameOrId);
                LogsManager.info("switched to frame " + nameOrId);
                return true;
            } catch (Exception e) {
                return false;
            }
        });
    }

    public void switchtoframeByWebElement(By frameLocator) {
        waitsManager.fluentWait().until(d -> {
            try {
                d.switchTo().frame(d.findElement(frameLocator));
                LogsManager.info("switched to frame " + frameLocator);
                return true;
            } catch (Exception e) {
                return false;
            }
        });
    }

    public void switchToDefaultContent() {
        waitsManager.fluentWait().until(d -> {
            try {
                d.switchTo().defaultContent();
                LogsManager.info("switched to default content");
                return true;
            } catch (Exception e) {
                return false;
            }
        });
    }
}
