package com.ferpfirstcode.validations;

import org.openqa.selenium.WebDriver;
import org.testng.Assert;

public class Verification extends BaseAssertion {
    public Verification() {

    }

    public Verification(WebDriver driver) {
        super(driver);
    }

    @Override
    protected void assertTrue(boolean condition, String message) {
        Assert.assertTrue(condition, message);

    }

    @Override
    protected void assertFalse(boolean condition, String message) {
        Assert.assertFalse(condition, message);

    }

    @Override
    public void assertEquals(Object actual, Object expected, String message) {
        Assert.assertEquals(actual, expected, message);

    }

    @Override
    protected void assertNotEquals(Object actual, Object expected, String message) {
        Assert.assertNotEquals(actual, expected, message);

    }

    @Override
    protected void fail(String message) {
        Assert.fail(message);

    }
}
