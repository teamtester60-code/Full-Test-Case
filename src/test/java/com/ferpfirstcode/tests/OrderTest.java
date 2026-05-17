package com.ferpfirstcode.tests;
import java.time.LocalDateTime;

import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.ferpfirstcode.apis.UserManagmentAPI;
import com.ferpfirstcode.pages.components.DailyStockPage;
import com.ferpfirstcode.pages.components.HomePage;
import com.ferpfirstcode.pages.components.OrderAPI;
import com.ferpfirstcode.pages.components.OrderPage;
import com.ferpfirstcode.pages.components.ProductStockData;
import com.ferpfirstcode.pages.components.SalesReport;
import com.ferpfirstcode.pages.components.SettingPage;

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
             homePage.gotoSettingPage()
                     .disable_Use_Daily_Stock()
                     .clickOnSaveButton()
                     .clickOnHomeButton();
             homePage.gotoorderpage()
                     .selectOrderTypebyindex()
                     .get_All_Product_From_API()
                     .searchRandomAPIProductInUI()
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
            homePage.gotoSettingPage()
                    .disable_Use_Daily_Stock()
                    .clickOnSaveButton()
                    .clickOnHomeButton();

            homePage.gotoorderpage()
            .selectOrderTypebyindex()
             .get_All_Product_From_API()
             .searchRandomAPIProductInUI()
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
        homePage.gotoSettingPage()
                .disable_Use_Daily_Stock()
                .clickOnSaveButton()
                .clickOnHomeButton();

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
        int expectedQuantity  = stockData.getLastQuantity();

        dailyStockPage.clickOnHomeButton()
                .gotoorderpage()
                .validateAvailableQuantityMatchesDailyStock(
                        productName,
                        expectedQuantity
                );

    }


    @Epic("Order Management")
    @Feature("Daily Stock")
    @Story("Validate available quantity in order page matches daily stock quantity")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that the available quantity shown in the order page is equal to the last quantity from daily stock for the same product")
    @Owner("Ahmed Hassan")
    @Test
    public void check_Product_Out_Of_Stock() throws InterruptedException {
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
        HomePage homePage =new HomePage(guiDriver);
        SettingPage settingPage=new SettingPage(guiDriver);
        OrderPage orderPage=new OrderPage(guiDriver);
        homePage.gotoSettingPage()
                .changeordertypeaftersend()
                .disable_Allow_Sale_With_No_Quantity_Available()
                .disable_Use_Daily_Stock()
                .clickOnSaveButton()
                .clickOnHomeButton()
                .gotoOrderTypeSettingPage()
                .enablePaymentByAnotherUserForAllTypes()
                .clickOnHomeButton()
                .gotoorderpage();
        Thread.sleep(3000);
               orderPage.get_All_Product_From_DB()
                .get_All_Product_From_API ()
               .searchRandomAPIProductInUI()
               .selectSearchedProduct()
               .sendOrder()
               .selectOrderToChangeOrderType()
               .changeordertypeaftersendorder();

    }

    
    @Test
    public void get_All_Product_From_API_And_DB() throws InterruptedException {
        HomePage homePage =new HomePage(guiDriver);
        SettingPage settingPage=new SettingPage(guiDriver);
        OrderPage orderPage=new OrderPage(guiDriver);
        homePage.gotoSettingPage()
                .changeordertypeaftersend()
                .disable_Allow_Sale_With_No_Quantity_Available()
                .disable_Use_Daily_Stock()
                .clickOnSaveButton()
                .clickOnHomeButton()
                .gotoOrderTypeSettingPage()
                .enablePaymentByAnotherUserForAllTypes()
                .clickOnHomeButton()
                .gotoorderpage();
        Thread.sleep(3000);
               orderPage.get_All_Product_From_DB();

}}
