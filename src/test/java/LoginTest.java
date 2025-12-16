import com.ferpfirstcode.driver.GUIDriver;
import com.ferpfirstcode.pages.components.LoginPage;
import com.ferpfirstcode.utils.TimeManager;
import com.ferpfirstcode.utils.dataReader.JsonReader;
import com.ferpfirstcode.utils.logs.LogsManager;
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

    @Test
    public void validLoginTC() throws InterruptedException {
        new LoginPage(guiDriver)
                .navigateToLoginPage()
<<<<<<< HEAD
                .enterPin(testdata.getJsonreader("password"))
=======
                .enterUsername(testdata.getJsonreader("username"))
                .enterPassword(testdata.getJsonreader("password"))
>>>>>>> 6f52845df553065a0249f92ceeed6f22ac3e9a8e
                .clickLoginButton()
                .verifyloggedinsuccess();

        LogsManager.info("تم تنفيذ اختبار تسجيل الدخول بنجاح");
    }
<<<<<<< HEAD


=======
>>>>>>> 6f52845df553065a0249f92ceeed6f22ac3e9a8e
}