package com.ferpfirstcode.validations;

import com.ferpfirstcode.utils.logs.LogsManager;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.asserts.SoftAssert;

public class Validation extends BaseAssertion {
    private static SoftAssert softAssert = new SoftAssert();
    private static boolean used = false;

    public Validation() {

    }

    public Validation(WebDriver driver) {
        super(driver);
    }

    @Override
    protected void assertTrue(boolean condition, String message) {
        used = true;
        softAssert.assertTrue(condition, message);

    }

    @Override
    protected void assertFalse(boolean condition, String message) {
        used = true;
        softAssert.assertFalse(condition, message);

    }

    @Override
    protected void assertEquals(Object actual, Object expected, String message) {
        used = true;
        softAssert.assertEquals(actual, expected, message);

    }

    @Override
    protected void assertNotEquals(Object actual, Object expected, String message) {
        used = true;
        softAssert.assertNotEquals(actual, expected, message);

    }

    @Override
    protected void fail(String message) {
        used = true;
        softAssert.fail(message);

    }

    public static void assertAll(ITestResult result) {
        if (!used) return;
        try {
            softAssert.assertAll();
        } catch (AssertionError e) {
            LogsManager.error("Soft assertion failed: " + e.getMessage());
            result.setStatus(ITestResult.FAILURE);
            result.setThrowable(e);
        } finally {
            softAssert = new SoftAssert(); // Reset for future use
        }
    }
}