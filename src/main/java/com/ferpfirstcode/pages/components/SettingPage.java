package com.ferpfirstcode.pages.components;

import com.ferpfirstcode.driver.GUIDriver;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class SettingPage {
    private final GUIDriver driver;

    public SettingPage(GUIDriver driver) {
        this.driver = driver;
    }
    // Locators
    private final By ShowProductsAvaliableQuantity = By.cssSelector("input[name='ShowProductsAvalQty']");
    private final By allowsalewithnoquantityavailable = By.cssSelector("input[name='AllowUsingProductsWithNoAvalQty']");
    private final By UseDailyStock = By.cssSelector("input[name='UseDailyStock']");
    private final By savebutton = By.cssSelector("button.btn.btn-danger.mt-0");
    private final By homebutton = By.cssSelector("a.mainTitle.padNone i.material-icons");


    @Step("Enable show products available quantity settings")
    public SettingPage enable_ShowProducts_AvaliableQuantity_Settings() throws InterruptedException {
        Thread.sleep(3000);

        WebElement checkbox = driver.get().findElement(ShowProductsAvaliableQuantity);

        if (!checkbox.isSelected()) {
            checkbox.click();
        }

        return this;
    }

    @Step("Disable show products available quantity settings")
    public SettingPage disable_ShowProducts_AvaliableQuantity_Settings() throws InterruptedException {
        Thread.sleep(3000);

        WebElement checkbox = driver.get().findElement(ShowProductsAvaliableQuantity);

        if (checkbox.isSelected()) {
            checkbox.click();
        }

        return this;
    }


    @Step("disable show products avaliable quantity settings")
    public SettingPage disable_ShowProducts_Avaliable_Quantity_Settings() {
        boolean isChecked = driver.element().isElementSelected(ShowProductsAvaliableQuantity);

        if (isChecked) {
            driver.element().clickElement(ShowProductsAvaliableQuantity);
        }

        return this;
    }
    @Step("enable allow sale with no quantity available")
    public SettingPage enable_Allow_Sale_With_No_Quantity_Available(){

        boolean isChecked = driver.element().isElementSelected(allowsalewithnoquantityavailable);
        if (!isChecked){
            driver.element().clickElement(allowsalewithnoquantityavailable);

        }
        return this;
    }

    @Step("disable allow sale with no quantity available")
    public SettingPage disable_Allow_Sale_With_No_Quantity_Available(){

        boolean isChecked = driver.element().isElementSelected(allowsalewithnoquantityavailable);
        if (isChecked){
            driver.element().clickElement(allowsalewithnoquantityavailable);

        }
        return this;
    }

    @Step("Enable Use Daily Stock")
    public SettingPage enable_Use_Daily_Stock(){

        boolean isChecked = driver.element().isElementSelected(UseDailyStock);
        if (!isChecked){
            driver.element().clickElement(UseDailyStock);
        }
        return this;


    }

    @Step("click on save button")
    public SettingPage clickOnSaveButton(){
        driver.element().clickElement(savebutton);
        return this;
    }

    @Step("click on home button")
    public HomePage clickOnHomeButton(){
        driver.element().clickElement(homebutton);
        return new HomePage(driver);
    }

}


