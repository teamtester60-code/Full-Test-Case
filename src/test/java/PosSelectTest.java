import com.ferpfirstcode.pages.components.LoginPage;
import com.ferpfirstcode.utils.dataReader.JsonReader;
import com.ferpfirstcode.utils.logs.LogsManager;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Listeners(com.ferpfirstcode.customlisteners.TestNGListeners.class)
public class PosSelectTest extends BaseTest {
    protected String timestamp = com.ferpfirstcode.utils.TimeManager.gettimestamp();
    protected JsonReader testdata;

    @BeforeClass
    public void precondition() {
        testdata = new JsonReader("login-data");
        LogsManager.info("تم تحميل بيانات الاختبار من JSON");
    }

    @Test
    public void posSelectTC() throws InterruptedException {
        new LoginPage(guiDriver)
                .navigateToLoginPage()
                .enterUsername(testdata.getJsonreader("username"))
                .enterPassword(testdata.getJsonreader("password"))
                .clickLoginButton()
                .verifyloggedinsuccess()
                .clickEditPosButton()
                .clickAuthorAndUnAuthorButton()
                .clickSavePosButton()
                .clickHomeButton()
                .verifysuccessfulgotohomepage();

        LogsManager.info("تم تنفيذ اختبار اختيار نقطة البيع بنجاح");
    }
}
