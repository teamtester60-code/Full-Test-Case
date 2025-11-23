package com.ferpfirstcode.utils;


import com.ferpfirstcode.utils.dataReader.PropertyReader;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.FluentWait;

import java.util.ArrayList;

public class WaitsManager {

    private static WebDriver driver;

    public WaitsManager(WebDriver driver) {
        this.driver = driver;
    }

    public FluentWait<WebDriver> fluentWait() {
        return new FluentWait<>(driver)
                .withTimeout(java.time.Duration.ofSeconds(Long.parseLong(PropertyReader.getProperty("DEFAULT_WAIT"))))
                .pollingEvery(java.time.Duration.ofMillis(500))
                .ignoreAll(getexceptions());
    }

    private static ArrayList<Class<? extends Exception>> getexceptions() {

        ArrayList<Class<? extends Exception>> exceptions = new ArrayList<>();
        exceptions.add(NoSuchElementException.class);
        exceptions.add(IndexOutOfBoundsException.class);
        exceptions.add(NullPointerException.class);
        exceptions.add(StaleElementReferenceException.class);
        exceptions.add(ElementClickInterceptedException.class);
        exceptions.add(ElementNotInteractableException.class);
        return exceptions;
    }

}
