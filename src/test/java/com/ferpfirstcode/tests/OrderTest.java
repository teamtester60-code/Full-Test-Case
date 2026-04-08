package com.ferpfirstcode.tests;
import com.ferpfirstcode.pages.components.*;

import io.qameta.allure.*;

import org.testng.Assert;
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
    public void Should_Create_Order_And_Pay_Successfully() throws InterruptedException {
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
    public void Cancel_order_TC() throws InterruptedException{
            HomePage homePage = new HomePage(guiDriver);
            homePage.gotoorderpage()
            .selectOrderTypebyindex()
            .clickOnProduct()
            .cancelOrder();
    }

    @Epic("Order Management")
    @Feature("Daily Stock")
    @Story("Validate available quantity in order page matches daily stock quantity")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that the available quantity shown in the order page is equal to the last quantity from daily stock for the same product")
    @Owner("Ahmed Hassan")
    @Test
    public void Check_Available_Quantity_From_Daily_Stock_That_Equals_To_The_Quantity_in_Order() throws InterruptedException {
        HomePage homePage = new HomePage(guiDriver);
        DailyStockPage dailyStockPage = new DailyStockPage(guiDriver);

        homePage.gotoSettingPage()
                .enable_ShowProducts_AvaliableQuantity_Settings()
                .enable_Use_Daily_Stock()
                .clickOnSaveButton()
                .clickOnHomeButton()
                .gotoDailyStockPage()
                .gotoDailyStockListPage()
                .openTodayDailyStockOrCreateNew();

        ProductStockData stockData = dailyStockPage.getProductNameAndLastQuantity();

        String productName = stockData.getProductName();
        int expectedQuantity = stockData.getLastQuantity();

        dailyStockPage.clickOnHomeButton()
                .gotoorderpage()
                .validateAvailableQuantityMatchesDailyStock(
                        productName,
                        expectedQuantity
                );

    }

    @Test
    public void Get_First_Combo_Product_Name() throws InterruptedException {
        HomePage homePage = new HomePage(guiDriver);
        homePage.gotoorderpage();
        OrderAPI orderAPI = new OrderAPI();
        String productName = orderAPI.getFirstComboProductName();
        Allure.step("First combo product name: " + productName);
    }

}
