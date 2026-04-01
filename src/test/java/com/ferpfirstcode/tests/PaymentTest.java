package com.ferpfirstcode.tests;
import com.ferpfirstcode.RetryAnalyzer;
import com.ferpfirstcode.pages.components.HomePage;
import com.ferpfirstcode.pages.components.PaymentPage;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.annotations.Listeners;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;



@Listeners(com.ferpfirstcode.customlisteners.TestNGListeners.class)
public class PaymentTest extends AuthenticatedBaseTest {
 
    @Epic("POS System")
    @Feature("Order Management")
    @Story("Create and Pay Order")
    @Severity(SeverityLevel.CRITICAL)
    @Owner("Ahmed Hassan")
    @Test
    public void Should_Create_And_Pay_Delivery_Order() throws InterruptedException {
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
    public void Create_Return_Order_TC() throws InterruptedException {
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
public void Over_Payment_Test_With_Database() throws InterruptedException {

    PaymentPage paymentPage =
            new HomePage(guiDriver)
                    .gotoorderpage()
                    .makeTakeAwayOrder()
                    .clickOnProduct()
                    .goToPaymentForTakeawayOrder();

    long orderNumber = paymentPage.getOrderNumberFromUI();

    paymentPage
            .setOrderNumber(orderNumber)
            .payOverPrice()
            .closeOrder();

    paymentPage.validateDBPayAmountIsDoubleTotal(40, 0.01);
}
    @Test
 public void DB_Matches_UI() throws InterruptedException {

        PaymentPage paymentPage = new PaymentPage(guiDriver);
         new HomePage(guiDriver)
                .gotoorderpage()
                 .makeTakeAwayOrder()
                 .clickOnProduct()
                 .goToPaymentForTakeawayOrder();
            long orderNumber = paymentPage.getOrderNumberFromUI();
            paymentPage.setOrderNumber(orderNumber)
                 .closeOrder();
            paymentPage.validateDBTotalEqualsUITotal(0.2);

 }



}

