package com.ferpfirstcode.utils;

import com.ferpfirstcode.utils.dataReader.PropertyReader;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;

public class WaitsManager {

    private final WebDriver driver;

    public WaitsManager(WebDriver driver) {
        this.driver = driver;
    }

    public FluentWait<WebDriver> fluentWait() {

        long timeout = 10; // fallback default
        try {
            String v = PropertyReader.getProperty("DEFAULT_WAIT");
            if (v != null && !v.isBlank()) timeout = Long.parseLong(v);
        } catch (Exception ignored) {}

        return new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(timeout))
                .pollingEvery(Duration.ofMillis(500))
                .ignoreAll(getexceptions());
    }

    public void waitVisible(By locator, int seconds) {
        new WebDriverWait(driver, Duration.ofSeconds(seconds))
                .until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public void waitClickable(By locator, int seconds) {
        new WebDriverWait(driver, Duration.ofSeconds(seconds))
                .until(ExpectedConditions.elementToBeClickable(locator));
    }

    public void waitInvisible(By locator, int seconds) {
        new WebDriverWait(driver, Duration.ofSeconds(seconds))
                .until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    private static ArrayList<Class<? extends Exception>> getexceptions() {
        ArrayList<Class<? extends Exception>> exceptions = new ArrayList<>();
        exceptions.add(NoSuchElementException.class);
        exceptions.add(StaleElementReferenceException.class);
        exceptions.add(ElementClickInterceptedException.class);
        exceptions.add(ElementNotInteractableException.class);
        return exceptions;
    }
}
