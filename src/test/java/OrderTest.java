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
public class OrderTest extends BaseTest {
    protected String timestamp = com.ferpfirstcode.utils.TimeManager.gettimestamp();
    protected JsonReader testdata;

    @BeforeClass
    public void precondition() {
        testdata = new JsonReader("login-data");
        LogsManager.info("تم تحميل بيانات الاختبار من JSON");
    }

    @Epic("POS System")
    @Feature("Create Order")
    @Story("Create and Pay Order Delivery")
    @Severity(SeverityLevel.CRITICAL)
    @Owner("Ahmed Hassan")

    @Test
    public void shouldCreateDeliveryOrderAndPaySuccessfully() throws InterruptedException {
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
                .validateOrderIsSentSuccessfully();
    }
    
    @Epic("POS System")
    @Feature("Cancel Order")
    @Story("Create and Cancel Order")
    @Severity(SeverityLevel.CRITICAL)
    @Owner("Ahmed Hassan")
    @Test
    public void cancelorderTC() throws InterruptedException{
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
                .cancelOrder();
    }

}
