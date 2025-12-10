package com.ferpfirstcode.pages.components;

import com.ferpfirstcode.driver.GUIDriver;
import com.ferpfirstcode.media.ScreenShotsManager;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class PaymentPage {
    private final GUIDriver driver;
    public PaymentPage(GUIDriver driver) {
        this.driver=driver;
    }
    //Locators
    private final By customersbutton=By.xpath("(//button[contains(@class,'btn bg-maingreen cairo-font w-100 mb-1')])[1]");
    private final By discountbutton=By.xpath("(//button[contains(@class,'btn') and contains(@class,'bg-maingreen')])[5]");
    private final By closeOrderbutton=By.cssSelector("button.btn.btn-success.ng-star-inserted");
    private final By searchbynamefield= By.xpath("(//input[@placeholder='Search By Name'])[1]");
    private final By selectcustomerbutton= By.cssSelector("button.btnPLus.btn-link");
    private final By selectaddressbutton= By.xpath("(//*[@id=\"collapseOne_@i\"]/td[5]/button)[1]");
    private final By closecustomerselectionmodalbutton= By.xpath("(//*[@id=\"nav-home\"]/div/div[2]/div[2]/div/button)[1]");
    private final By discountbypercentagebutton= By.xpath("//*[@id=\"modal-DetailDiscount\"]/div/div/div[2]/div[1]/ul/li[8]/a");
    private final By okbuttonofdiscountmodal= By.xpath("(//*[@id=\"modal-DetailDiscount\"]/div/div/div[3]/button)[1]");
    private final By totalamount=By.xpath("//*[@id=\"OverLayPin\"]/div/div[2]/div/app-payment/div[1]/div/div[1]/div/div[4]/div/div/div[1]/div/div[2]/table/tbody/tr[7]/td");
    private final By ordercost=By.xpath("//*[@id=\"OverLayPin\"]/div/div[2]/div/app-payment/div[1]/div/div[1]/div/div[4]/div/div/div[1]/div/div[2]/table/tbody/tr[1]/td");
    private final By totaldiscountamount=By.xpath("//*[@id=\"OverLayPin\"]/div/div[2]/div/app-payment/div[1]/div/div[1]/div/div[4]/div/div/div[1]/div/div[2]/table/tbody/tr[6]/td");
    //Actions
    @Step("Select Customer")
    public PaymentPage selectCustomer() {
        driver.element().clickElement(customersbutton);
        driver.element().clickElement(searchbynamefield).typeText(searchbynamefield,"abdo");
        driver.element().clickElement(selectcustomerbutton);
        driver.element().clickElement(selectaddressbutton);
        driver.element().clickElement(closecustomerselectionmodalbutton);
        return this;
    }
    @Step("Select Discount")
    public PaymentPage selectDiscount() {
        driver.element().clickElement(discountbutton);
        driver.element().clickElement(discountbypercentagebutton);
        driver.element().clickElement(okbuttonofdiscountmodal);
        return this;
    }
    @Step("Close Order")
    public PaymentPage closeOrder() {
        driver.element().clickElement(closeOrderbutton);
        return this;
    }


    //validation
    @Step("Validate Discount Calculation")
    public PaymentPage validateDiscountCalculation(double discountPercentage) {
        // 1. Read the total price before discount
        String beforeText = driver.element().getElementText(ordercost).replace(",", "").trim();
        double totalBefore = Double.parseDouble(beforeText);

        // Take screenshot for documentation
        ScreenShotsManager.takeFullPageScreenshot(driver.get(), "Total Price Before Discount");

        // 2. Calculate the expected price after discount
        double expectedAfter = totalBefore - (totalBefore * discountPercentage / 100);

        // 3. Apply discount steps from the UI
        selectDiscount();

        // 4. Read the total price after discount
        String afterText = driver.element().getElementText(totaldiscountamount).replace(",", "").trim();
        double totalAfter = Double.parseDouble(afterText);

        // Take screenshot for documentation
        ScreenShotsManager.takeFullPageScreenshot(driver.get(), "Total Price After Discount");

        // 5. Assert the calculation
        if (Math.abs(expectedAfter - totalAfter) < 0.01) {
            System.out.println("✅ Discount is correct: Expected = " + expectedAfter + " | Actual = " + totalAfter);
        } else {
            throw new AssertionError("❌ Discount is incorrect: Expected = " + expectedAfter + " | Actual = " + totalAfter);
        }

        return this;
    }







}
