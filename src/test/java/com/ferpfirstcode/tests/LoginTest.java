package com.ferpfirstcode.tests;

import com.ferpfirstcode.pages.components.LoginPage;
import com.ferpfirstcode.utils.TimeManager;
import com.ferpfirstcode.utils.dataReader.JsonReader;
import com.ferpfirstcode.utils.logs.LogsManager;

import io.qameta.allure.*;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Listeners({com.ferpfirstcode.customlisteners.TestNGListeners.class ,
        com.ferpfirstcode.customlisteners.RetryTransformer.class})
public class LoginTest extends BaseTest {
    protected String timestamp = TimeManager.gettimestamp();
    protected JsonReader testdata;

    @BeforeClass
    public void precondition() {
        testdata = new JsonReader("login-data");
        LogsManager.info("تم تحميل بيانات الاختبار من JSON");
    }




    @Epic("POS System")
    @Feature("Login Management")
    @Story("Login With Valid Data")
    @Severity(SeverityLevel.BLOCKER)
    @Owner("Ahmed Hassan")
    @Test
    public void Valid_Login_TC() throws InterruptedException {
        new LoginPage(guiDriver)
                .navigateToLoginPage()
                .loginwithpin()
                .verifyloggedinsuccess();

        LogsManager.info("تم تنفيذ اختبار تسجيل الدخول بنجاح");
    }

    @Epic("POS System")
    @Feature("Login Management")
    @Story("Login With Valid Data")
    @Severity(SeverityLevel.BLOCKER)
    @Owner("Ahmed Hassan")
    @Test
    public void Invalid_Login_TC_with_Wrong_PIN(){
            new LoginPage(guiDriver)
            .navigateToLoginPage()
            .enterPin("741963")
            .clickLoginButton(); 
             new LoginPage (guiDriver)
            .verifyErrorMessage("Username or Password is incorrect.");



    }


    @Epic("POS System")
    @Feature("Login Management")
    @Story("Login With Empty Fields")
    @Severity(SeverityLevel.NORMAL)
    @Owner("Ahmed Hassan")
    @Test
    public void Login_With_Empty_Fields() {
       new LoginPage(guiDriver)
            .navigateToLoginPage()
            .clickPinField()
            .clickLoginButton();
            new LoginPage(guiDriver)
            .verifyMessageLabel(" كلمة المرور مطلوبة ", " Password Is Required ");//a
    }
}   