import com.ferpfirstcode.pages.components.LoginPage;
import com.ferpfirstcode.utils.TimeManager;
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
    @Severity(SeverityLevel.CRITICAL)
    @Owner("Ahmed Hassan")


    @Test
    public void validLoginTC() throws InterruptedException {
        new LoginPage(guiDriver)
                .navigateToLoginPage()
                .loginwithpin()
                .verifyloggedinsuccess();

        LogsManager.info("تم تنفيذ اختبار تسجيل الدخول بنجاح");
    }
}   