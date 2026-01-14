import com.ferpfirstcode.pages.components.HomePage;
import com.ferpfirstcode.pages.components.PaymentPage;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;



@Listeners(com.ferpfirstcode.customlisteners.TestNGListeners.class)
public class PaymentTest extends AuthenticatedBaseTest {
 
    @Epic("POS System")
    @Feature("Order Management")
    @Story("Create and Pay Order")
    @Severity(SeverityLevel.CRITICAL)
    @Owner("Ahmed Hassan")
    @Test
    public void shouldCreateAndPayDeliveryOrder() throws InterruptedException {
                 HomePage homePage = new HomePage(guiDriver);
                homePage.gotoorderpage()
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
         HomePage homePage = new HomePage(guiDriver);
             homePage.gotoorderpage()
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
           HomePage homePage = new HomePage(guiDriver);
             homePage.gotoorderpage()
             .makeTakeAwayOrder()
             .clickOnProduct()
             .goToPaymentForTakeawayOrder()
             .payOverPrice()
             .closeOrder();
             PaymentPage paymentPage = new PaymentPage(guiDriver); 
             paymentPage.getLastPaymentAmountFromDB();
             paymentPage.validatePaymentAmountMatchesDB();       
    }

}
