import com.ferpfirstcode.utils.SnagitUtils;
import com.ferpfirstcode.utils.dataReader.DataBaseReader;
import com.ferpfirstcode.utils.dataReader.JsonReader;
import com.ferpfirstcode.utils.logs.LogsManager;
import com.ferpfirstcode.utils.report.AllureAttachmentManger;

import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;

import java.io.File;

import org.testng.Assert;
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
    @Epic("POS System")
    @Feature("Order Management")
    @Story("Create and Pay Order")
    @Severity(SeverityLevel.CRITICAL)
    @Owner("Ahmed Hassan")
    @Test
    public void e2e() throws InterruptedException {
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
                .gotoorderpage()
                .selectOrderTypebyindex()
                .clickOnProduct()
                .validateOrderIsSentSuccessfully()
                .selectOrderToPay()
                .validateDiscountCalculation(50)
                .validateDeliveryOrder();
    }

    @Epic("POS System")
    @Feature("Return Order")
    @Story("Create and Return Order")
    @Severity(SeverityLevel.NORMAL)
    @Owner("Ahmed Hassan")
    @Test
    public void createreturnorderTC() throws InterruptedException {
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
                .gotoorderpage()
                .selectOrderTypebyindex()
                .clickOnProduct()
                .validateOrderIsSentSuccessfully()
                .selectOrderToPay()
                .validateDiscountCalculation(50)
                .validateDeliveryOrder()
                .makeAReturnOrder();
    }
    
    @Test
    public void overpaymenttestwithdatabase() throws InterruptedException {
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
                .gotoorderpage()
                .makeTakeAwayOrder()
                .clickOnProduct()
                .goToPaymentForTakeawayOrder()
                .payOverPrice()
                .closeOrder();
                new com.ferpfirstcode.pages.components.PaymentPage(guiDriver) 
                .getLastPaymentAmountFromDB();
                new com.ferpfirstcode.pages.components.PaymentPage(guiDriver) 
                .validatePaymentAmountMatchesDB();       
    }

}
