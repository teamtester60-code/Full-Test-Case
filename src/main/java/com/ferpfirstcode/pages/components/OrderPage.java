package com.ferpfirstcode.pages.components;

import com.ferpfirstcode.driver.GUIDriver;
import com.ferpfirstcode.media.ScreenShotsManager;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.TakesScreenshot;
import org.testng.annotations.Test;

public class OrderPage {
    private final GUIDriver driver;
    public OrderPage(GUIDriver driver) {
        this.driver = driver;
    }
    //Locators
    private final By OrderListButton= By.xpath("//div[contains(@class,'ms-panel-body')]//i[contains(@class,'fa-clipboard-list')]");
    private final By orderpagebutton = By.xpath("//a[@href='/order']");
    private final By volumesPOPUP= By.cssSelector("div.modal-content.modal-contentWidth");
    private final By okbuttonforsidedishmodal = By.xpath("button.btn.btn-primary.btn-footer.btnEdit");
    private  final By volumeseditionbutton= By.xpath("(//button[contains(@class,'dropDownBtn')])[1]");
    private  final By sidedisheeditionbutton= By.xpath("(//button[contains(@class,'dropDownBtn')])[2]");
    private  final By noteseditionbutton= By.xpath("(//button[contains(@class,'dropDownBtn')])[3]");
    private  final By productdiscounteditionbutton= By.xpath("(//button[contains(@class,'dropDownBtn')])[4]");
    private  final By insuranceitemseditionbutton= By.xpath("(//button[contains(@class,'dropDownBtn')])[5]");
    private  final By changepriceeditionbutton= By.xpath("(//button[contains(@class,'dropDownBtn')])[6]");
    private final By ordertypebutton= By.id("ordertype-tab");
    private final By newOrderButton= By.id("c-tab");
    private final By numberofpeopleontable= By.cssSelector("img.pull-right.img-thumbnail");
    private final By personscountbar= By.id("PersonsCount");
    private final By okbuttononpersonscountbar= By.id("#modal-Persons div.modal-footer button.btnEdit");
    private final By sendorderbutton= By.xpath("//button[@id='ct-tab']//div[normalize-space(text())='Send']");
    private final By payementbutton= By.xpath("//button[@id='coact-tab']//div[normalize-space(text())='Payment']");
    private final By sidedishPOPUP= By.id("modal-NewSideDishes");
    private final By searchbynamefield= By.xpath("(//input[@placeholder='Search By Name'])[1]");
    private final By selectcustomerbutton= By.cssSelector("button.btnPLus.btn-link");
    private final By selectaddressbutton= By.xpath("(//tr[@id='collapseOne_@i']//button[normalize-space(text())='Select'])[1]");
    private final By closecustomerselectionmodalbutton= By.xpath("//*[@id=\"nav-home\"]/div/div[2]/div[2]/div/button");
    private final By opensentordersbutton= By.xpath("//*[@id=\"OverLayPin\"]/div/div[1]/div[3]/div[3]/div/nav/ul/li[4]/a/div");
    private final By totalpricebeforesendorder=By.xpath("//li[contains(@class,'bg-maingreen')]//h5[last()]");
    private final By totalpriceaftersendorder=By.xpath("//table[contains(@class,'table')]//tbody//tr[1]/td[3]");


    //dynamic locator
    private By productByName(String productName) {
        return By.xpath("//div[contains(@class,'product-card')]//div[@class='productName' and normalize-space(text())='" + productName + "']");
    }
    private By volumeByIndex(int index) {
        return By.xpath("(//div[@id='modal-Volums']//button[contains(@class,'volumeSelect')])[" + index + "]");
    }
    private By sideDishByIndex(int index) {
        return By.xpath("(//div[@id='modal-NewSideDishes']//li[contains(@class,'liSide')]//button[contains(@class,'btn-success')])[" + index + "]");
    }
    private By ordertypename(String tabName) {
        return By.xpath("//a[contains(@class,'nav-link') and normalize-space(text())='" + tabName + "']");
    }
    private By hallByName(String hallName) {
        return By.xpath("//li[contains(@class,'tab')]//span[normalize-space(text())='"+ hallName +"']");
    }
    private By tableByNumber(String tableNumber) {
        return By.xpath("//li[contains(@class,'h')][.//div[@class='pull-left' and normalize-space(text())='" + tableNumber + "']]");
    }



    //Actions
    @Step("Click on Product: {productName}")
    @Description("Click on Product: {productName} and handle volume and side dishes if popup appears")
    public OrderPage clickOnProduct(String productName) {
        driver.element().clickElement(productByName(productName));
        if (driver.element().isElementVisible(volumesPOPUP)) {
            driver.element().clickElement(volumeByIndex(1));
        }
        if (driver.element().isElementVisible(sidedishPOPUP)) {
            driver.element().clickElement(sideDishByIndex(1));
            driver.element().clickElement(sideDishByIndex(2));
            driver.element().clickElement(sideDishByIndex(2));
            driver.element().clickElement(okbuttonforsidedishmodal);
        }
        return this;
    }
    @Step("Select Order Type: {tabName}")
    public OrderPage selectOrderType(String tabName) {
        driver.element().clickElement(ordertypebutton);
        driver.element().clickElement(ordertypename(tabName));

        if (tabName.equals("توصيل")) {
            driver.element().clickElement(searchbynamefield);
            driver.element().typeText(searchbynamefield, "abdo");
            driver.element().clickElement(selectcustomerbutton);
            driver.element().clickElement(selectaddressbutton);
            driver.element().clickElement(closecustomerselectionmodalbutton);
        }

        if (tabName.equals("صالة")) {
            driver.element().clickElement(hallByName("vip"));
            driver.element().clickElement(tableByNumber("1"));
        }

        return this;
    }

    @Step("Create New Order for {numberOfPeople} people")
    public OrderPage createNewOrder(int numberOfPeople) {
        driver.element().clickElement(newOrderButton);
        driver.element().clickElement(numberofpeopleontable);
        driver.element().typeText(personscountbar, String.valueOf(numberOfPeople));
        driver.element().clickElement(okbuttononpersonscountbar);
        return this;
    }

    //validation
    @Step("Validate that order is sent successfully")
    public OrderPage validateOrderIsSentSuccessfully() {
        ScreenShotsManager.takeFullPageScreenshot(driver.get(), "Before Sending Order");
        String priceBeforeSendOrder = driver.element().getElementText(totalpricebeforesendorder);
        driver.element().clickElement(sendorderbutton);
        driver.element().clickElement(opensentordersbutton);
        ScreenShotsManager.takeFullPageScreenshot(driver.get(), "After Sending Order");
        String priceAfterSendOrder = driver.element().getElementText(totalpriceaftersendorder);
        if (!priceBeforeSendOrder.equals(priceAfterSendOrder)) {
            throw new AssertionError("Order price mismatch: before sending order: " + priceBeforeSendOrder + ", after sending order: " + priceAfterSendOrder);
        }
        return this;
    }

}
