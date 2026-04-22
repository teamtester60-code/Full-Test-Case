package com.ferpfirstcode.pages.components;

import com.ferpfirstcode.apis.UserManagmentAPI;
import com.ferpfirstcode.driver.GUIDriver;
import com.ferpfirstcode.utils.actions.ElementActions;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Random;

public class CustomerOrderPage {
    private final GUIDriver driver;
    public CustomerOrderPage(GUIDriver driver) {
        this.driver = driver;
    }

    //Locators
    private final By addnewcustomerorderbutton = By.cssSelector("#addNewButton");
    private final By editbutton = By.cssSelector("button:has(i.bi-pencil-fill)");
    private final By deletebutton = By.cssSelector("li.trashBtn button");
    private final By savebutton = By.cssSelector("button:has(i.fa-save)");
    private final By printbutton = By.cssSelector("button[data-target=\"#modal-2\"]");
    private final By customertext = By.cssSelector("#CustomerDocumentId_id input");
    private final By ordertypetextsearch= By.cssSelector("#OrderTypeDocument_id input.e-input");
    private final By paymenttypetextsearch= By.cssSelector("#PayTypeDocment_id_hidden + input");
    private final By addproductbutton = By.cssSelector("button[class='btn btn-success mr-3']");
    private final By paymenttypearrow= By.cssSelector("#PayTypeDocment_id .e-ddl-icon");
    private final By activeDropdownOptions = By.cssSelector("#PayTypeDocment_id_options li.e-list-item");
    private final By orderTypeArrow = By.cssSelector("#OrderTypeDocument_id span.e-ddl-icon");
    private final By activeDropdownOptionsordertype = By.cssSelector("#OrderTypeDocument_id_options li.e-list-item");
    private final By productarrowuuid= By.cssSelector("#UUID_id span.e-ddl-icon");
    private final By productsuuid= By.cssSelector("#UUID_id_popup li.e-list-item");
    private final By producttextuuid= By.cssSelector("input[aria-owns='UUID_id_options']");
    private final By productQTY0= By.cssSelector("#ProductQuantity0");
    private final By discount0= By.cssSelector("#Discount0");



    //Actions

    // 1. دالة جلب الاسم
    @Step("Get First Available Customer Name From Network")
    public String getCustomerNameFromNetwork() {

        UserManagmentAPI userManagmentAPI = new UserManagmentAPI(driver);

        // 1. Fetch the first name directly!
        // (The API method already handles lists, assertions, and returning index 0)
        String fetchedName = userManagmentAPI.getFirstAvailableCustomerName();

        // 2. Log the success in Allure
        Allure.step("Customer name fetched successfully for UI usage: " + fetchedName);

        // 3. Return the name to type it in the search bar
        return fetchedName;
    }

    // 2. دالة النقر على الزر
    @Step("Click on add button")
    public void clickOnAddButton() {
        driver.element().clickElement(addnewcustomerorderbutton);
    }

    // 3. دالة الكتابة (لاحظ أننا أضفنا parameter لتستقبل الاسم)
    @Step("Enter the customer name '{customerName}' in textbar")
    public CustomerOrderPage entertextname(String customerName) {
        // استخدمنا المُحدد الخاص بشريط البحث (تأكد أن اسمه مطابق لما لديك)
        driver.element().typeText(customertext, customerName);
        return this;
    }

    @Step("Dynamically open and select the first Payment Type")
    public CustomerOrderPage selectFirstPaymentType() {

        driver.element().clickElement(paymenttypearrow);

        int optionsCount = driver.element().getElementsCount(activeDropdownOptions);
        Assert.assertTrue(optionsCount > 0, "🚨 The Payment Type dropdown popup didn't show any options!");

        By firstOptionLocator = By.cssSelector(".e-popup.e-popup-open li.e-list-item:first-child");
        String selectedText = driver.element().getElementText(firstOptionLocator);

        driver.element().clickElement(firstOptionLocator);

        Allure.step("✅ Successfully clicked Payment Type: " + selectedText);
        return this;
    }

    @Step("Dynamically select a Random Order Type")
    public CustomerOrderPage selectRandomOrderType() {

        driver.element().clickElement(orderTypeArrow);

        int optionsCount = driver.element().getElementsCount(activeDropdownOptionsordertype);
        Assert.assertTrue(optionsCount > 0, "🚨 The Order Type dropdown is empty!");

        Random rand = new Random();
        int randomNum = rand.nextInt(optionsCount) + 1;

        By randomOptionLocator = By.cssSelector(".e-popup.e-popup-open li.e-list-item:nth-child(" + randomNum + ")");

        String selectedText = driver.element().getElementText(randomOptionLocator);
        driver.element().clickElement(randomOptionLocator);

       Allure.step("✅ Dynamically selected Random Order Type: " + selectedText);
        return this;
    }


    @Step("Dynamically select a Random Product from the UUID list")
    public CustomerOrderPage selectRandomProduct() {
        driver.element().clickElement(addproductbutton);

        driver.element().clickElement(productarrowuuid);

        int optionsCount = driver.element().getElementsCount(productsuuid);
        Assert.assertTrue(optionsCount > 0, "🚨 The Product dropdown is empty!");
        Allure.step("products:"+optionsCount);

        Random rand = new Random();
        int randomNum = rand.nextInt(optionsCount) + 1;

        By randomOptionLocator = By.cssSelector("#UUID_id_popup li.e-list-item:nth-child(" + randomNum + ")");


        String selectedProductText = driver.element().getElementText(randomOptionLocator);
        driver.element().clickElement(producttextuuid);
        driver.element().typeText(producttextuuid, selectedProductText);
        driver.element().clickElement(By.cssSelector("#UUID_id_popup li.e-list-item:first-child"));//the selected product

       Allure.step("✅ Successfully selected a Random Product: " + selectedProductText);
       return this;
    }

    @Step("Dynamically type a random quantity between 1 and 1000")
    public CustomerOrderPage enterRandomQuantity() {

        Random rand = new Random();
        int randomQty = rand.nextInt(1000) + 1;

        String quantityText = String.valueOf(randomQty);

        driver.element().typeText(productQTY0, quantityText);

       Allure.step("✅ Successfully typed random quantity: " + quantityText);
       return this;
    }

    @Step("Dynamically type a random Discount Value")
    public CustomerOrderPage enterRandomDiscount() {

        Random rand = new Random();
        int randomQty = rand.nextInt(99) + 1;
        String quantityText = String.valueOf(randomQty);
        driver.element().typeText(discount0, quantityText);
        Allure.step("✅ Successfully typed random quantity: " + quantityText);
        return this;
    }
    @Step("Save The Customer Order")
    public CustomerOrderPage saveCustomerOrder() {
        driver.element().clickElement(savebutton);
        return this;
    }


}
