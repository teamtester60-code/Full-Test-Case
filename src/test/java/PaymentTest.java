import com.ferpfirstcode.utils.dataReader.JsonReader;
import com.ferpfirstcode.utils.logs.LogsManager;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;



@Listeners(com.ferpfirstcode.customlisteners.TestNGListeners.class)
public class PaymentTest extends BaseTest {
    protected String timestamp = com.ferpfirstcode.utils.TimeManager.gettimestamp();
    protected JsonReader testdata;

    @BeforeClass
    public void precondition() {
        testdata = new JsonReader("login-data");
        LogsManager.info("تم تحميل بيانات الاختبار من JSON");
    }
    @Test
    public void paymentPageTC() throws InterruptedException {
        new com.ferpfirstcode.pages.components.LoginPage(guiDriver)
                .navigateToLoginPage()
                .enterPin(testdata.getJsonreader("password"))
                .clickLoginButton()
                .verifyloggedinsuccess()
                .clickEditPosButton()
                .clickAuthorAndUnAuthorButton()
                .clickSavePosButton()
                .clickHomeButton()
                .clickOpenDayButton()
                .clickShiftOpenButton()
                .gotoorderpage()
                .selectOrderTypebyindex()
                .clickOnProduct()
                .validateOrderIsSentSuccessfully()
                .selectOrderToPay()
                .validateDiscountCalculation(50)
                .validateDeliveryOrder();
    }
    //visca Barca
}
