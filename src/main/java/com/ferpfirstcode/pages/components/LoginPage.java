//package com.ferpfirstcode.pages.components;
//
//import com.ferpfirstcode.driver.GUIDriver;
//import com.ferpfirstcode.media.ScreenShotsManager;
//import com.ferpfirstcode.utils.dataReader.PropertyReader;
//import io.qameta.allure.Step;
//import org.openqa.selenium.By;
//
//public class LoginPage {
//    private final GUIDriver driver;
//    public LoginPage(GUIDriver driver) {
//        this.driver = driver;
//    }
//
//    // Locators
//    private final By usernameField = By.id("username");
//    private final By passwordField = By.id("pass");
//    private final By loginButton = By.cssSelector("button.submitBtn");
//    private final By numberone= By.xpath("//div[span[text()='1']]");
//    private final By numbertwo= By.xpath("//div[span[text()='2']]");
//    private final By numberthree= By.xpath("//div[span[text()='3']]");
//    private final By numberfour= By.xpath("//div[span[text()='4']]");
//    private final By numberfive= By.xpath("//div[span[text()='5']]");
//    private final By numbersix= By.xpath("//div[span[text()='6']]");
//    private final By numberseven= By.xpath("//div[span[text()='7']]");
//    private final By numbereight= By.xpath("//div[span[text()='8']]");
//    private final By numbernine= By.xpath("//div[span[text()='9']]");
//    private final By numberzero= By.xpath("//div[span[text()='0']]");
//    private final By dot= By.xpath("//div[span[text()='.']]");
//    private final By cardpin= By.cssSelector("a i.fa-id-card");
//    private final By languageDropdown= By.id("navbarDropdown");
//    private final By frenchlanguage= By.xpath("//div[contains(@class,'dropdown-menu-languageLi')]//img[contains(@src,'fr2.png')]");
//    private final By englishlanguage= By.xpath("//div[contains(@class,'dropdown-menu-languageLi')]//img[contains(@src,'en.png')]");
//    private final By turkishlanguage= By.xpath("//div[contains(@class,'dropdown-menu-languageLi')]//img[contains(@src,'tr.png')]");
//    private final By arabiclanguage= By.xpath("//div[contains(@class,'dropdown-menu-languageLi')]//img[contains(@src,'ar.png')]");
//    private final By keyboardicon= By.cssSelector("div.callkeyboard");
//    private final By errorMessage= By.cssSelector("div.toast-error .toast-message");
//    private final By visibleicon= By.cssSelector("img.ms-user-img");
//
//    // Actions
//    @Step("Navigate to Login Page")
//    public  LoginPage navigateToLoginPage() {
//        driver.browser().navigateTo(PropertyReader.getProperty("baseURLweb"));
//        return this;
//    }
//    @Step("Enter Username")
//    public LoginPage enterUsername(String username) {
//        driver.element().typeText(usernameField, username);
//        return this;
//    }
//    @Step("Enter Password")
//    public LoginPage enterPassword(String password) {
//        driver.element().typeText(passwordField, password);
//        return this;
//    }
//    @Step("Click Login Button")
//    public LoginPage clickLoginButton() {
//        driver.element().clickElement(loginButton);
//        return this;
//    }
//    public LoginPage clickoncardpin() {
//        driver.element().clickElement(cardpin);
//        return this;
//    }
//    public LoginPage loginwithpin(String password) {
//        for (char ch : password.toCharArray()) {
//            String charAsString = String.valueOf(ch);
//            switch (charAsString) {
//                case "1" -> driver.element().clickElement(numberone);
//                case "2" -> driver.element().clickElement(numbertwo);
//                case "3" -> driver.element().clickElement(numberthree);
//                case "4" -> driver.element().clickElement(numberfour);
//                case "5" -> driver.element().clickElement(numberfive);
//                case "6" -> driver.element().clickElement(numbersix);
//                case "7" -> driver.element().clickElement(numberseven);
//                case "8" -> driver.element().clickElement(numbereight);
//                case "9" -> driver.element().clickElement(numbernine);
//                case "0" -> driver.element().clickElement(numberzero);
//                case "." -> driver.element().clickElement(dot);
//            }
//        }
//        return this;
//
//    }
//    public LoginPage selectLanguage(String language) {
//        driver.element().clickElement(languageDropdown);
//        switch (language.toLowerCase()) {
//            case "french" -> driver.element().clickElement(frenchlanguage);
//            case "english" -> driver.element().clickElement(englishlanguage);
//            case "turkish" -> driver.element().clickElement(turkishlanguage);
//            case "arabic" -> driver.element().clickElement(arabiclanguage);
//        }
//        return this;
//    }
//    public String getErrorMessage() {
//        return driver.element().getElementText(errorMessage);
//    }
//
//    //validations can be added here
//
//    public LoginPage errorMessageValidation(String expectedMessage) {
//        String actualMessage = getErrorMessage();
//        if (!actualMessage.equals(expectedMessage)) {
//            throw new AssertionError("Expected error message: " + expectedMessage + ", but got: " + actualMessage);
//        }
//        return this;
//    }
//    public LoginPage verifyLoginPageLoaded() {
//        driver.verify().isElementVisible(usernameField);
//        driver.verify().isElementVisible(passwordField);
//        driver.verify().isElementVisible(loginButton);
//        return this;
//    }
//    public LoginPage verifyKeyboardIconVisible() {
//        driver.verify().isElementVisible(keyboardicon);
//        return this;
//    }
//    public LoginPage verifyLanguageDropdownVisible() {
//        driver.verify().isElementVisible(languageDropdown);
//        return this;
//    }
//    public LoginPage verifyCardPinIconVisible() {
//        driver.verify().isElementVisible(cardpin);
//        return this;
//    }
//    @Step("Verify Successful Login")
//    public LoginPage verifyloggedinsuccess() throws InterruptedException {
//        Thread.sleep(4000); // wait for login to complete
//        ScreenShotsManager.takeFullPageScreenshot(driver.get(),"AfterLogin");
//        driver.verify().isElementVisible(visibleicon);
//        return this;
//    }
//}


package com.ferpfirstcode.pages.components;

import com.ferpfirstcode.driver.GUIDriver;
import com.ferpfirstcode.media.ScreenShotsManager;
import com.ferpfirstcode.utils.dataReader.PropertyReader;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

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

    @Step("Click Login Button")
    public LoginPage clickLoginButton() {
        driver.element().clickElement(loginButton);
        return this;
    }

    @Step("Verify Successful Login")
    public LoginPage verifyloggedinsuccess() throws InterruptedException {
        Thread.sleep(4000); // wait for login to complete
        ScreenShotsManager.takeFullPageScreenshot(driver.get(),"AfterLogin");
        driver.verify().isElementVisible(visibleIcon);
        return this;
    }

    public LoginPage verifyErrorMessage(String expectedMessage) {
        String actual = driver.element().getElementText(errorMessage);
        if (!expectedMessage.equals(actual)) {
            throw new AssertionError("Expected: " + expectedMessage + " but found: " + actual);
        }
        return this;
    }
}
