package com.ferpfirstcode.pages.components;

import com.ferpfirstcode.driver.GUIDriver;
import com.ferpfirstcode.media.ScreenShotsManager;
import com.ferpfirstcode.utils.dataReader.DataBaseReader;
import com.ferpfirstcode.utils.dataReader.PropertyReader;
import com.ferpfirstcode.utils.logs.LogsManager;

import io.qameta.allure.Step;

import java.util.Arrays;

import org.openqa.selenium.By;
import org.testng.Assert;

public class LoginPage {
    private final GUIDriver driver;

    public LoginPage(GUIDriver driver) {
        this.driver = driver;
    }

    // Locators
    private final By usernameField = By.id("username");
    private final By passwordField = By.id("pass");
    private final By loginButton = By.cssSelector("button.submitBtn");
    private final By visibleIcon = By.cssSelector("img.ms-user-img");
    private final By errorMessage = By.cssSelector("div.toast-error .toast-message");
    private final By pinicon = By.xpath("//i[@class='fas fa-id-card menu-icon']");
    private final By messageLabel = By.xpath("//div[contains(@class,'alert-danger')]");

    @Step("Navigate to Login Page")
    public LoginPage navigateToLoginPage() {
        driver.browser().navigateTo(PropertyReader.getProperty("baseURLweb"));
        return this;
    }

    @Step("Enter Username")
    public LoginPage enterUsername(String username) {
        driver.element().typeText(usernameField, username);
        return this;
    }

    @Step("Enter Password")
    public LoginPage enterPassword(String password) {
        driver.element().typeText(passwordField, password);
        return this;
    }
    @Step("Enter PIN")
    public LoginPage enterPin(String pin) {
        ScreenShotsManager.takeFullPageScreenshot(driver.get(),"BeforePin");
        driver.element().clickElement(pinicon);
        ScreenShotsManager.takeFullPageScreenshot(driver.get(),"AfterPin");
        driver.element().typeText(passwordField, pin);
        ScreenShotsManager.takeFullPageScreenshot(driver.get(),"Aftertyping");
        return this;
    }
    @Step("Click PIN field")
    public LoginPage clickPinField() {
        driver.element().clickElement(pinicon);
        return this;
    }

    @Step("login with pin that readed from database")
    public PosPage loginwithpin() {
    // 1. جلب الـ Pin من MongoDB
    String dbPin = DataBaseReader.getAdminPin();
    LogsManager.info("الرمز المستخرج من القاعدة هو: " + dbPin);

    // التحقق من أن القيمة ليست فارغة (Assertion)
    Assert.assertNotNull(dbPin, "فشل جلب الـ Pin من قاعدة البيانات!");

    // 2. استخدام السيلينيوم لإدخال الـ Pin
    LoginPage loginPage = new LoginPage(driver);
    loginPage.enterPin(dbPin);
    loginPage.clickLoginButton();
    return new PosPage(driver);

    // 3. التأكد من نجاح الدخول
    // Assert.assertTrue(...)
}



    @Step("Click Login Button")
    public PosPage clickLoginButton() {
        driver.element().clickElement(loginButton);
        return new PosPage(driver);
    }

//    @Step("Verify Successful Login")
//    public HomePage verifyloggedinsuccess() throws InterruptedException {
//        Thread.sleep(4000); // wait for login to complete
//        ScreenShotsManager.takeFullPageScreenshot(driver.get(),"AfterLogin");
//        driver.verify().isElementVisible(visibleIcon);
//        return new HomePage(driver);
//    }
    @Step("Verify Error Message")
    public LoginPage verifyErrorMessage(String expectedMessage) {
        String actual = driver.element().getElementText(errorMessage);
        if (!expectedMessage.equals(actual)) {
            throw new AssertionError("Expected: " + expectedMessage + " but found: " + actual);
        }
        return this;
    }

    @Step("Verify Message Label")
public LoginPage verifyMessageLabel(String... expectedMessages) {
    String actual = driver.element().getElementText(messageLabel).trim();

    for (String expected : expectedMessages) {
        if (actual.equals(expected.trim())) {
            return this; // ✅ match found
        }
    }

    throw new AssertionError(
        "Expected one of: " + Arrays.toString(expectedMessages) +
        " but found: " + actual
    );
}

}
