package com.ferpfirstcode.tests;

import com.ferpfirstcode.driver.GUIDriver;
import com.ferpfirstcode.driver.WebDriverProvider;
import com.ferpfirstcode.utils.dataReader.DataBaseReader;
import com.ferpfirstcode.utils.logs.LogsManager;
import io.qameta.allure.Allure;
import io.qameta.allure.testng.AllureTestNg;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.io.ByteArrayInputStream;

@Listeners({AllureTestNg.class})
public class BaseTest implements WebDriverProvider {

    protected WebDriver webDriver;
    protected GUIDriver guiDriver;

    @BeforeSuite(alwaysRun = true)
    public void setUpSuite() {
        DataBaseReader.connect();
        LogsManager.info("MongoDB connection opened for the suite.");
    }

    @BeforeMethod(alwaysRun = true)
    public void setup() {
        // 1. Database Preconditions (Test Isolation)
        DataBaseReader.configurePOSSettingsForTest();
        DataBaseReader.configureOrderTypesForTest();

        // 2. WebDriver Initialization
        guiDriver = new GUIDriver();
        webDriver = guiDriver.get();

        if (webDriver == null) {
            throw new RuntimeException("WebDriver initialization failed.");
        }

    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        // 1. Capture and attach screenshot to Allure report on failure
        if (webDriver != null && result != null && result.getStatus() == ITestResult.FAILURE) {
            try {
                LogsManager.info("❌ Test Failed: " + result.getName() + " - Taking screenshot...");
                takeScreenshot(result.getName());
            } catch (Exception e) {
                LogsManager.error("Failed to capture screenshot: " + e.getMessage());
            }
        }

        // 2. Safely Close WebDriver
        if (webDriver != null) {
            try {
                webDriver.quit();
                LogsManager.info("WebDriver closed successfully.");
            } catch (Exception e) {
                LogsManager.error("Error while closing WebDriver: " + e.getMessage());
            } finally {
                webDriver = null;
                guiDriver = null;
            }
        }
    }

    @AfterSuite(alwaysRun = true)
    public void tearDownSuite() {
        DataBaseReader.close();
        LogsManager.info("MongoDB connection closed safely.");
    }

    @Override
    public WebDriver getDriver() {
        return webDriver;
    }

    // ==========================================
    // Helper Methods
    // ==========================================

    /**
     * Injects CSS into the DOM to make Toast messages "transparent" to mouse clicks.
     * This prevents ElementClickInterceptedException globally across all tests.
     */

    /**
     * Captures a screenshot and attaches it directly to the Allure Report.
     */
    private void takeScreenshot(String testName) {
        if (webDriver instanceof TakesScreenshot) {
            byte[] screenshot = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.BYTES);
            Allure.addAttachment("Failure Screenshot - " + testName, new ByteArrayInputStream(screenshot));
        }
    }
}