package com.ferpfirstcode.pages.components;

import com.ferpfirstcode.driver.GUIDriver;
import com.ferpfirstcode.media.ScreenShotsManager;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

public class PosPage {

    private final GUIDriver driver;

    public PosPage(GUIDriver driver) {this.driver = driver;}

    // Locators
    private final By editposbutton = By.cssSelector("button.btn.btn-outline-warning.btnNav");
    private final By authorandUnAthor= By.cssSelector("button.btn.m-0.p-2");
    private final By saveposbutton= By.xpath("//button[.//i[contains(@class,'fa-save')]]");
    private final By homebutton= By.xpath("(//a[.//i[text()='home']])[1]");
    private final By visibleIcon = By.cssSelector("img.ms-user-img");


    //Actions
    @Step("Click Edit POS Button")
    public PosPage clickEditPosButton() {
        driver.element().clickElement(editposbutton);
        return this;
    }
    @Step("Click Authorize/UnAuthorize Button")
    public PosPage clickAuthorAndUnAuthorButton() {
        driver.element().clickElement(authorandUnAthor);
        return this;
    }
    @Step("Click Save POS Button")
    public PosPage clickSavePosButton() {
        driver.element().clickElement(saveposbutton);
        return this;
    }
    @Step("Click Home Button")
    public HomePage clickHomeButton() {
        driver.element().clickElement(homebutton);
        return new HomePage(driver);
    }


    //Verification
    @Step("Verify Successful Login")
    public PosPage verifyloggedinsuccess() throws InterruptedException {
        Thread.sleep(4000); // wait for login to complete
        ScreenShotsManager.takeFullPageScreenshot(driver.get(),"AfterLogin");
        driver.verify().isElementVisible(visibleIcon);
        return this;
    }

}
