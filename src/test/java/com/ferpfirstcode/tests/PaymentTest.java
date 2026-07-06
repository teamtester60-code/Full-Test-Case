package com.ferpfirstcode.tests;

import com.ferpfirstcode.pages.components.*;
import com.ferpfirstcode.utils.dataReader.DataBaseReader;
import com.ferpfirstcode.utils.logs.LogsManager;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Listeners(com.ferpfirstcode.customlisteners.TestNGListeners.class)
public class PaymentTest extends AuthenticatedBaseTest {
    private boolean isDelivery(String orderType) {
        return orderType != null && (orderType.equalsIgnoreCase("delivery") ||
                orderType.contains("توصيل") || orderType.contains("دليفر"));
    }

    @Epic("POS System")
    @Feature("Return Order")
    @Story("Create and Return Order with Exact Serial Tracking")
    @Severity(SeverityLevel.CRITICAL)
    @Owner("Ahmed Hassan")
    @Test(description = "Verify successful full return order process")
    public void Create_Return_Order_TC() throws InterruptedException {

        HomePage homePage = new HomePage(guiDriver);
        OrderPage orderPage = homePage.gotoorderpage().createReadyOrder();

        orderPage.selectOrderToPay();

        String currentOrderType = guiDriver.getSelectedOrderType();
        if (currentOrderType != null && isDelivery(currentOrderType)) {
            throw new SkipException("Skipping Return process: Delivery Order.");
        }

        // 1. Close the order and catch the Serial
        String exactSerial = orderPage.closeOrderAndCatchSerial();

        // 2. CRITICAL FIX: Fetch DocumentId (MongoDB _id) instead of OrderNumber
        // Note: Ensure you create 'getDocumentIdBySerial' in DataBaseReader
        String exactDocumentId = DataBaseReader.getDocumentIdBySerial(exactSerial);

        // 3. Start Return Process using the unique DocumentId
        orderPage.makeAReturnOrder(exactDocumentId);
    }



    @Test
    public void Over_Payment_Test_With_Database() throws InterruptedException {

        HomePage homePage = new HomePage(guiDriver);
        OrderPage orderPage = homePage.gotoorderpage().createReadyOrder();

        orderPage.selectOrderToPay();

        PaymentPage paymentPage = new PaymentPage(guiDriver);

      paymentPage.payOverPrice();
        String exactSerial = orderPage.closeOrderAndCatchSerial();
        String exactOrderNumber = DataBaseReader.getOrderNumberBySerial(exactSerial);

        paymentPage.validateDBPayAmountIsDoubleTotal(exactSerial, 15, 0.01);
    }



    @Test(description = "Verify that the DB total matches the UI total after closing the order")
    public void DB_Matches_UI() throws InterruptedException {

        HomePage homePage = new HomePage(guiDriver);
        OrderPage orderPage = homePage.gotoorderpage().createReadyOrder();
        orderPage.selectOrderToPay();

        String currentOrderType = guiDriver.getSelectedOrderType();
        if (currentOrderType != null && isDelivery(currentOrderType)) {
            throw new SkipException("Skipping test: Delivery Order.");
        }

        PaymentPage paymentPage = new PaymentPage(guiDriver);
        double uiTotal = paymentPage.getOrderTotalFromUI();

        String exactSerial = orderPage.closeOrderAndCatchSerial();

        paymentPage.validateDBTotalEqualsUITotal(exactSerial, uiTotal, 0.01, currentOrderType);
    }

    @Test
    public void Pay_With_Multiple_Payment() throws InterruptedException {
        PaymentPage paymentPage = new PaymentPage(guiDriver);
        HomePage homePage = new HomePage(guiDriver);
        OrderPage orderPage = homePage.gotoorderpage().createReadyOrder(); // سطر واحد بدلاً من 6!

        orderPage.selectOrderToPay();

        paymentPage
                .pay_With_Multiple_Payment()
                .closeOrder();
    }

    @Test
    public void Make_Random_Discount_Amount() throws InterruptedException {
        PaymentPage paymentPage = new PaymentPage(guiDriver);
        HomePage homePage = new HomePage(guiDriver);
        OrderPage orderPage = homePage.gotoorderpage().createReadyOrder();

        orderPage.selectOrderToPay();

        paymentPage
                .validateRandomDiscountCalculation()
                .closeOrder();
    }

    @Test
    public void Pay_Less_than_Total_cost() throws InterruptedException {

        HomePage homePage = new HomePage(guiDriver);
        OrderPage orderPage = homePage.gotoorderpage().createReadyOrder(); // سطر واحد بدلاً من 6!

        orderPage.selectOrderToPay();





        String currentOrderType = guiDriver.getSelectedOrderType();
        if (currentOrderType != null && isDelivery(currentOrderType)) {
            throw new SkipException("Skipping Return process: Delivery Order.");
        }
        PaymentPage paymentPage = new PaymentPage(guiDriver);


        // Note: Kept the Arabic string here because it is a validation assertion parameter, not a comment.
        paymentPage.payLessThanTotalCost()
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


