package com.ferpfirstcode.pages.components;

import com.ferpfirstcode.driver.GUIDriver;
import com.ferpfirstcode.media.ScreenShotsManager;
import com.ferpfirstcode.utils.logs.LogsManager;
import io.qameta.allure.Step;
import org.openqa.selenium.By;


public class HomePage {
    private final GUIDriver driver;

    public HomePage(GUIDriver driver) {
        this.driver = driver;
    }

    // Locators
    private final By visibleUsername = By.xpath("//span[@class='ng-star-inserted']/h6");
    private final By opendaybutton=By.xpath("//button[@data-target='#openDaymodal']");
    private final By shiftOpenbutton=By.xpath("//button[@data-target='#openshiftmodal']");
    private final By savebuttonatopendaymodal=By.xpath("//div[@id='openDaymodal']//button[@type='button' and contains(@class,'btn-primary')]");
    private final By savebuttonatopenshiftmodal=By.xpath("//div[@id='openshiftmodal']//button[@type='button' and contains(@class,'btn-primary')]");
    private final By OrderListButton= By.xpath("//div[contains(@class,'ms-panel-body')]//i[contains(@class,'fa-clipboard-list')]");
    private final By orderpagebutton = By.xpath("//a[@href='/order']");
    private final By manageorderbutton = By.cssSelector("a[href='/manageorderlist']");
    private final By generalsettingbutton = By.cssSelector("div.ms-widget.widgetEditing i.mainIcon.fa-cogs");
    private final By settingbtn=By.xpath("//a[@href='/POSsettings' and contains(@class,'d-flex')]");
    private final By transactionbutton = By.xpath("//i[contains(@class,'fa-donate')]/ancestor::div[contains(@class,'ms-panel-body')]");
    private final By dailystockbutton = By.cssSelector("a.d-flex[href='/dailystock']");
    private final By customerorderbutton = By.xpath("//a[@href='/CustomerOrders']");
    private final By reportbutton = By.xpath("//div[contains(@class, 'ms-widget')][.//h5[normalize-space(text())='التقارير' or normalize-space(text())='Report']]");
    private final By salesReport = By.xpath("//a[contains(normalize-space(.), 'Sales report') or contains(normalize-space(.), 'تقرير المبيعات')]");


    //Actions
    @Step("Click Open Day Button if it is enabled")
    public HomePage clickOpenDayButton() throws InterruptedException {
        try {
            // Check if the element is present and enabled
            if (driver.element().isElementVisible(opendaybutton)
                    && driver.get().findElement(opendaybutton).isEnabled()) {

                driver.element().clickElement(opendaybutton);

                // Optional: wait for modal to appear before clicking Save
                driver.element().clickElement(savebuttonatopendaymodal);
            } else {
                LogsManager.info("Open Day button is not enabled, skipping click.");
            }
        } catch (Exception e) {
            LogsManager.info("Open Day button not found, skipping click.");
        }
        Thread.sleep(4000);
        return this;
    }
    @Step("Go To Sales Report Page")
    public SalesReport gotoSalesReportPage() {
        driver.element().clickElement(reportbutton);
        driver.element().clickElement(salesReport);
        return new SalesReport(driver);
    }

    @Step("Click Shift Open Button if it is enabled")
    public HomePage clickShiftOpenButton() {
        try {
            // Check if the element exists and is enabled
            if (driver.element().isElementVisible(shiftOpenbutton)
                    && driver.get().findElement(shiftOpenbutton).isEnabled()) {

                driver.element().clickElement(shiftOpenbutton);
                // Optional: wait for modal to appear before clicking Save
                driver.element().clickElement(savebuttonatopenshiftmodal);
                LogsManager.info("Clicked Shift Open button successfully.");

            } else {
                LogsManager.info("Shift Open button is not enabled, skipping click.");
            }
        } catch (Exception e) {
            LogsManager.error("Shift Open button not found or not clickable: " + e.getMessage());
            ScreenShotsManager.takeFullPageScreenshot(driver.get(), "ShiftOpenButtonError");
        }
        return this;
    }

    @Step ("Go to Setting Page")
    public SettingPage gotoSettingPage() {
        driver.element().clickElement(generalsettingbutton);
        driver.element().clickElement(settingbtn);
        return new SettingPage(driver);
    }

    @Step("Go to Order Page")
    public OrderPage gotoorderpage() throws InterruptedException {
        Thread.sleep(4000);
        driver.element().clickElement(OrderListButton);
        driver.element().clickElement(orderpagebutton);
        return new OrderPage(driver);
    }
    @Step("Go to Customer Order Page")
    public CustomerOrderPage gotocustomerorderpage() {
        driver.element().clickElement(transactionbutton);
        driver.element().clickElement(customerorderbutton);
        return new CustomerOrderPage(driver);
    }

    @Step("Go to Manage Page")
    public ManageOrderPage gotomanagepage() {
        driver.element().clickElement(OrderListButton); 
        driver.element().clickElement(manageorderbutton);
        return new ManageOrderPage(driver);
    }
    @Step("Go to Daily Stock Page")
    public DailyStockPage gotoDailyStockPage() {
        driver.element().clickElement(transactionbutton);
        driver.element().clickElement(dailystockbutton);
        return new DailyStockPage(driver);
    }

    //Verification
    @Step("verify successful go to homepage" )
    public HomePage verifysuccessfulgotohomepage() throws InterruptedException {
        Thread.sleep(4000); // wait for login to complete
        ScreenShotsManager.takeFullPageScreenshot(driver.get(),"AfterGoToHomePage");
        driver.verify().isElementVisible(visibleUsername);
        return this;
    }

    @Step("Take Screenshot of Home Page After Shift Open" )
    public HomePage takeScreenshotOfHomePageAfterShiftOpen() {
        ScreenShotsManager.takeFullPageScreenshot(driver.get(), "HomePageAfterShiftOpen");
        return this;
    }

}





