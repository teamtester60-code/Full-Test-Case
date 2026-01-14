import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import com.ferpfirstcode.pages.components.HomePage;
import com.ferpfirstcode.pages.components.LoginPage;
import com.ferpfirstcode.pages.components.PosPage;
import com.ferpfirstcode.utils.dataReader.JsonReader;
import com.ferpfirstcode.utils.logs.LogsManager;

public class AuthenticatedBaseTest extends BaseTest {


    protected String timestamp = com.ferpfirstcode.utils.TimeManager.gettimestamp();
    protected JsonReader testdata;

    @BeforeClass(alwaysRun = true)
    public void precondition() {
        testdata = new JsonReader("login-data");
        LogsManager.info("تم تحميل بيانات الاختبار من JSON");
    }

    @BeforeMethod(alwaysRun = true)
    public void authenticate() throws InterruptedException {
        loginSelectPosAndOpenShift();
    }
    

    protected void loginSelectPosAndOpenShift() throws InterruptedException {

        LoginPage loginPage = new LoginPage(guiDriver);
        loginPage.navigateToLoginPage()
                .loginwithpin();

        PosPage posPage = new PosPage(guiDriver);
        posPage.clickEditPosButton()
                .clickAuthorAndUnAuthorButton()
                .clickSavePosButton()
                .clickHomeButton()
                .verifysuccessfulgotohomepage();

        HomePage homePage = new HomePage(guiDriver);
        homePage.clickOpenDayButton()
                .clickShiftOpenButton();
    }
}
