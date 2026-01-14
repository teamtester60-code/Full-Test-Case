import com.ferpfirstcode.pages.components.HomePage;
import com.ferpfirstcode.pages.components.OrderPage;
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
public class OrderTest extends AuthenticatedBaseTest {
    

    @Epic("POS System")
    @Feature("Create Order")
    @Story("Create and Pay Order Delivery")
    @Severity(SeverityLevel.CRITICAL)
    @Owner("Ahmed Hassan")

    @Test
    public void shouldCreateDeliveryOrderAndPaySuccessfully() throws InterruptedException {
             HomePage homePage = new HomePage(guiDriver);
             homePage.gotoorderpage()
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
            HomePage homePage = new HomePage(guiDriver);
            homePage.gotoorderpage()
            .selectOrderTypebyindex()
            .clickOnProduct()
            .cancelOrder();
    }

}
