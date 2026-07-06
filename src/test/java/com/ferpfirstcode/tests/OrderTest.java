package com.ferpfirstcode.tests;
import java.time.LocalDateTime;

import com.ferpfirstcode.pages.components.*;
import com.ferpfirstcode.utils.dataReader.DataBaseReader;
import com.ferpfirstcode.utils.logs.LogsManager;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import java.util.ArrayList;

import com.ferpfirstcode.apis.UserManagmentAPI;

import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;

@Listeners(com.ferpfirstcode.customlisteners.TestNGListeners.class)
public class OrderTest extends AuthenticatedBaseTest {




    @Epic("POS System")
    @Feature("Create Order")
    @Story("Create and Pay Order Delivery")
    @Severity(SeverityLevel.CRITICAL)
    @Owner("Ahmed Hassan")
    @Test
    public void Create_Order_And_Pay_Successfully() throws InterruptedException {
             HomePage homePage = new HomePage(guiDriver);
             homePage.gotoorderpage()
                     .selectRandomOrderType()
                     .get_All_Product_From_DB()
                     .searchRandomDBProductInUI()
                     .selectSearchedProduct()
                     .validateOrderIsSentSuccessfully();
    }

    
    @Epic("POS System")
    @Feature("Cancel Order")
    @Story("Create and Cancel Order")
    @Severity(SeverityLevel.CRITICAL)
    @Owner("Ahmed Hassan")
    @Test
    public void Cancel_order_TC() throws InterruptedException{

        UserManagmentAPI reportsAPI = new UserManagmentAPI (guiDriver);
            HomePage homePage = new HomePage(guiDriver);
            homePage.gotoorderpage()
            .selectRandomOrderType()
             .get_All_Product_From_DB()
             .searchRandomDBProductInUI()
             .selectSearchedProduct()
                    .cancelOrder();
        LocalDateTime exactTimeFromAPI = reportsAPI.getLatestCanceledOrderTimeFromAPI();
            homePage.gotoSalesReportPage();
            SalesReport salesReport = new SalesReport(guiDriver);
            salesReport.navigateToCancelledOrdersandgetlatestorder();
            salesReport.validateTimeMatchesAPI(exactTimeFromAPI);
    }
    @Test
    public void get_all_Order_Type_From_API() throws InterruptedException {
        UserManagmentAPI reportsAPI = new UserManagmentAPI (guiDriver);
        OrderPage orderPage = new OrderPage(guiDriver);
        HomePage homePage = new HomePage(guiDriver);
        homePage.gotoorderpage();
        orderPage.get_all_Order_Type_From_API()
                .selectRandomOrderType();
    }

    @Epic("Order Management")
    @Feature("Daily Stock")
    @Story("Validate available quantity in order page matches daily stock quantity")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that the available quantity shown in the order page is equal to the last quantity from daily stock for the same product")
    @Owner("Ahmed Hassan")
    @Test(description = "Validate order available quantity matches daily stock")
    public void validateAvailableQuantityMatchesDailyStock() throws InterruptedException {

        // 1. Database Setup: Enable daily stock settings
        DataBaseReader.enableDailyStockSettings();

//        // 2. Session Refresh: Logout and login to apply new DB settings
        HomePage homePage = new HomePage(guiDriver);
        homePage.clickOnLogoutButton();

        LoginPage loginPage = new LoginPage(guiDriver);
        loginPage.loginwithpin();

        // 3. Fetch Expected Data from Daily Stock
        DailyStockPage dailyStockPage = new DailyStockPage(guiDriver);

        homePage.gotoDailyStockPage()
                .gotoDailyStockListPage()
                .openTodayDailyStockOrCreateNew();

        ProductStockData stockData = dailyStockPage.getProductNameAndLastQuantity();
        String productName = stockData.getProductName();
        int expectedQuantity = stockData.getLastQuantity();

        LogsManager.info(String.format("Fetched from Daily Stock -> Product: %s | Expected Quantity: %d", productName, expectedQuantity));

        // 4. Validate Actual Quantity in Order Page
        dailyStockPage.clickOnHomeButton()
                .gotoorderpage()
                .validateAvailableQuantityMatchesDailyStock(productName, expectedQuantity);
    }
    @Epic("Order Management")
    @Feature("Daily Stock")
    @Story("Validate available quantity in order page matches daily stock quantity")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that the available quantity shown in the order page is equal to the last quantity from daily stock for the same product")
    @Owner("Ahmed Hassan")
    @Test
    public void check_Product_Out_Of_Stock() throws InterruptedException {

        // 1. Inject settings into the database
        DataBaseReader.enableDailyStockSettings();
        HomePage homePage = new HomePage(guiDriver);
        DailyStockPage dailyStockPage = new DailyStockPage(guiDriver);
        homePage.clickOnLogoutButton();
        LoginPage loginPage = new LoginPage(guiDriver);
        loginPage.loginwithpin();
        // 3.Navigate directly to execute the Business Logic
        homePage.gotoDailyStockPage()
                .gotoDailyStockListPage()
                .openTodayDailyStockOrCreateNew();

        ProductStockData stockData = dailyStockPage.getProductNameAndLastQuantity();
        String productName = stockData.getProductName();
        int expectedQuantity = stockData.getLastQuantity();

        dailyStockPage.clickOnHomeButton()
                .gotoorderpage()
                .createOrderWithProductIsOutOfStock(productName, expectedQuantity)
               .validateToastContains("لا يوجد كميه متاحه من الوجبه");
    }

    @Test
    public void Get_First_Combo_Product_Name() throws InterruptedException {
        HomePage homePage = new HomePage(guiDriver);
        homePage.gotoorderpage();
        OrderAPI orderAPI = new OrderAPI();
        String productName = orderAPI.getFirstComboProductName();
        Allure.step("First combo product name: " + productName);
    }

    @Test
    public void Get_Products_Name_From_API() throws InterruptedException {
        HomePage homePage = new HomePage(guiDriver);
        homePage.gotoorderpage();
        OrderPage orderPage = new OrderPage(guiDriver);
        orderPage.get_All_Product_From_API();



    }

    @Test
    public void Change_Order_Type_After_Send_Order() throws InterruptedException {

        // 1.Inject the required setting into the database in milliseconds
        DataBaseReader.enableChangeOrderTypeAfterSaveSetting();

        // 2.Refresh the page so the system reads the new configuration
        guiDriver.get().navigate().refresh();

        HomePage homePage = new HomePage(guiDriver);
        OrderPage orderPage = new OrderPage(guiDriver);

        homePage.gotoorderpage();

        Thread.sleep(3000); // Kept your sync wait for stability

        orderPage.selectRandomOrderType()
                .get_All_Product_From_DB()
                .searchRandomDBProductInUI()
                .selectSearchedProduct()
                .sendOrder()
                .selectOrderToChangeOrderType()
                .changeordertypeaftersendorder();
    }

    
    @Test
    public void get_All_Product_From_API_And_DB() throws InterruptedException {
        HomePage homePage =new HomePage(guiDriver);
        OrderPage orderPage=new OrderPage(guiDriver);
        homePage.gotoorderpage();
        Thread.sleep(3000);
               orderPage.get_All_Product_From_DB();

}}
