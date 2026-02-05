package com.ferpfirstcode.tests;
import com.ferpfirstcode.pages.components.LoginPage;
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

@Listeners({
    com.ferpfirstcode.customlisteners.TestNGListeners.class,
    io.qameta.allure.testng.AllureTestNg.class
})

public class PosSelectTest extends BaseTest {
    protected String timestamp = com.ferpfirstcode.utils.TimeManager.gettimestamp();
    protected JsonReader testdata;

    @BeforeClass
    public void precondition() {
        testdata = new JsonReader("login-data");
        LogsManager.info("تم تحميل بيانات الاختبار من JSON");
    }
    @Epic("POS System")
    @Feature("Login")
    @Story("Login and Select POS")
    @Severity(SeverityLevel.CRITICAL)
    @Owner("Ahmed Hassan")
    @Test
    public void posSelectTC() throws InterruptedException {
        new LoginPage(guiDriver)
                .navigateToLoginPage()
                .loginwithpin()
                .verifyloggedinsuccess()
                .clickEditPosButton()
                .clickAuthorAndUnAuthorButton()
                .clickSavePosButton()
                .clickHomeButton()
                .verifysuccessfulgotohomepage();

        LogsManager.info("تم تنفيذ اختبار اختيار نقطة البيع بنجاح");
    }

}
