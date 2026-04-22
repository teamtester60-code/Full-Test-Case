package com.ferpfirstcode.tests;
import com.ferpfirstcode.RetryAnalyzer;
import com.ferpfirstcode.pages.components.CustomerOrderPage;
import com.ferpfirstcode.pages.components.HomePage;
import com.ferpfirstcode.pages.components.PaymentPage;
import com.ferpfirstcode.pages.components.SettingPage;
import io.qameta.allure.*;
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
    public void Create_And_Pay_Delivery_Order() throws InterruptedException {
                 HomePage homePage = new HomePage(guiDriver);
        homePage.gotoSettingPage()
                .disable_Use_Daily_Stock()
                .clickOnSaveButton()
                .clickOnHomeButton();
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
        homePage.gotoSettingPage();
        SettingPage settingPage = new SettingPage(guiDriver);
        settingPage.disable_Use_Daily_Stock()
                .clickOnSaveButton()
                .clickOnHomeButton();

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
        HomePage homePage = new HomePage(guiDriver);
        homePage.gotoSettingPage()
                .disable_Use_Daily_Stock()
                .clickOnSaveButton()
                .clickOnHomeButton();

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
        HomePage homePage = new HomePage(guiDriver);
        homePage.gotoSettingPage()
                .disable_Use_Daily_Stock()
                .clickOnSaveButton()
                .clickOnHomeButton();

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

    @Test
    public void Pay_With_Multiple_Payment() throws InterruptedException {
        HomePage homePage = new HomePage(guiDriver);
        homePage.gotoSettingPage()
                .disable_Use_Daily_Stock()
                .clickOnSaveButton()
                .clickOnHomeButton();
        PaymentPage paymentPage= new PaymentPage(guiDriver);
        new HomePage(guiDriver)
                .gotoorderpage()
                .makeTakeAwayOrder()
                .clickOnProduct()
                .goToPaymentForTakeawayOrder();
        paymentPage
                .pay_With_Multiple_Payment()
                .closeOrder();

    }

    @Test
    public void Make_Discount_With_Fixed_Amount() throws InterruptedException {
        HomePage homePage = new HomePage(guiDriver);
        homePage.gotoSettingPage()
                .disable_Use_Daily_Stock()
                .clickOnSaveButton()
                .clickOnHomeButton();
        PaymentPage paymentPage = new PaymentPage(guiDriver);
        new HomePage(guiDriver)
                .gotoorderpage()
                .makeTakeAwayOrder()
                .clickOnProduct()
                .goToPaymentForTakeawayOrder();
        paymentPage
                .validate_Discount_By_Fixed_Amount()
                .closeOrder();



    }

    @Test
    public void Pay_Less_than_Total_cost() throws InterruptedException {
        HomePage homePage = new HomePage(guiDriver);
        homePage.gotoSettingPage()
                .disable_Use_Daily_Stock()
                .clickOnSaveButton()
                .clickOnHomeButton();
        PaymentPage paymentPage= new PaymentPage(guiDriver);
        new HomePage(guiDriver)
                .gotoorderpage()
                .makeTakeAwayOrder()
                .clickOnProduct()
                .goToPaymentForTakeawayOrder();
        paymentPage
                .payLessThanTotalCost()
                .validate_pay_amount_less_than_total_cost("يجب ان يكون المبلغ المدفوع");


    }

    @Test
    public void Create_Customer_Order_With_Random_Data() throws InterruptedException {
        HomePage homePage = new HomePage(guiDriver);
        homePage.gotocustomerorderpage()
                .clickOnAddButton();

        CustomerOrderPage customerOrderPage = new CustomerOrderPage(guiDriver);
        String dynamicCustomerName = customerOrderPage.getCustomerNameFromNetwork();

        customerOrderPage.entertextname(dynamicCustomerName)
                .selectFirstPaymentType()
                .selectRandomOrderType()
                .selectRandomProduct()
                .enterRandomQuantity()
                .enterRandomDiscount()
                .saveCustomerOrder();


    }






}

