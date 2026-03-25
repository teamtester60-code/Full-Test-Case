package com.ferpfirstcode.tests;
import com.ferpfirstcode.utils.dataReader.JsonReader;
import com.ferpfirstcode.utils.logs.LogsManager;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Listeners(com.ferpfirstcode.customlisteners.TestNGListeners.class)
public class HomePageTest extends BaseTest {

    protected String timestamp = com.ferpfirstcode.utils.TimeManager.gettimestamp();
    protected JsonReader testdata;

    @BeforeClass
    public void precondition() {
        testdata = new JsonReader("login-data");
        LogsManager.info("تم تحميل بيانات الاختبار من JSON");
    }

    @Epic("POS System")
    @Feature("Home Page")
    @Story("Login And Go To Home Page")
    @Severity(SeverityLevel.NORMAL)
    @Owner("Ahmed Hassan")
    @Test
    public void Home_Page_TC() throws InterruptedException {
        new com.ferpfirstcode.pages.components.LoginPage(guiDriver)
                .navigateToLoginPage()
                .loginwithpin()
                .verifyloggedinsuccess()
                .clickEditPosButton()
                .clickAuthorAndUnAuthorButton()
                .clickSavePosButton()
                .clickHomeButton()
                .clickOpenDayButton()
                .clickShiftOpenButton()
                .takeScreenshotOfHomePageAfterShiftOpen();
    }

}
