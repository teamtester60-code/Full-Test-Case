package com.ferpfirstcode.validations;

import com.ferpfirstcode.utils.WaitsManager;
import com.ferpfirstcode.utils.actions.ElementActions;
import com.ferpfirstcode.utils.logs.LogsManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public abstract class BaseAssertion {

    protected WebDriver driver;
    protected WaitsManager waitsManager;
    protected ElementActions elementActions;

    protected BaseAssertion() {

    }

    protected BaseAssertion(WebDriver driver) {
        this.driver = driver;
        waitsManager = new WaitsManager(driver);
        elementActions = new ElementActions(driver);
    }

    protected abstract void assertTrue(boolean condition, String message);

    protected abstract void assertFalse(boolean condition, String message);

    protected abstract void assertEquals(Object actual, Object expected, String message);

    protected abstract void assertNotEquals(Object actual, Object expected, String message);

    protected abstract void fail(String message);

    public void Equals(Object actual, Object expected, String message) {
        assertEquals(actual, expected, message);
    }

    public boolean isElementVisible(By locator) {

        boolean flag = waitsManager.fluentWait().until(driver ->
                {
                    try {
                        driver.findElement(locator).isDisplayed();
                        return true;

                    } catch (Exception e) {
                        LogsManager.error("Element not visible: " + locator.toString());
                        return false;
                    }

                }

        );
        assertTrue(flag, "Element is not visible: " + locator.toString());

        return flag;
    }

    //verify page url
    protected void isPageUrl(String expectedUrl) {
        String actualUrl = driver.getCurrentUrl();
        assertEquals(actualUrl, expectedUrl, "Page URL is not as expected. Expected: " + expectedUrl + " Actual: " + actualUrl);
    }

    //verify page title
    protected void isPageTitle(String expectedTitle) {
        String actualTitle = driver.getTitle();
        assertEquals(actualTitle, expectedTitle, "Page title is not as expected. Expected: " + expectedTitle + " Actual: " + actualTitle);
    }

}
