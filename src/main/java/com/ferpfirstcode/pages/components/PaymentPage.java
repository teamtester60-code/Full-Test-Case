package com.ferpfirstcode.pages.components;

import com.ferpfirstcode.driver.GUIDriver;
import com.ferpfirstcode.media.ScreenShotsManager;
import com.ferpfirstcode.utils.dataReader.DataBaseReader;
import com.ferpfirstcode.utils.logs.LogsManager;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PaymentPage {
    public final GUIDriver driver;
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
    private final By ordercost=By.xpath("//*[@id=\"OverLayPin\"]/div/div[2]/div/app-payment/div[1]/div/div[1]/div/div[4]/div/div/div[1]/div/div[2]/table/tbody/tr[1]/td");
    private final By totaldiscountamount=By.xpath("//*[@id=\"OverLayPin\"]/div/div[2]/div/app-payment/div[1]/div/div[1]/div/div[4]/div/div/div[1]/div/div[2]/table/tbody/tr[6]/td");
    private final By manageOrdersbutton= By.xpath("//a[@href=\"/manageorderlist\"]");
    private final By totalprice= By.xpath("(//div[contains(@class,'col-4') and contains(@class,'text-right')])[1]");
    private final By paymentamountfield = By.id("PayAmount0");
    private final By orderNumberLabel= By.xpath("(//div[contains(@class,'col-12') and contains(@class,'mt-1')]  //table[contains(@class,'table-bordered')])[last()]//tr[1]/td");

    private long orderNumber;
    private Double lastPaidAmount;
    private Double orderTotalAmount;

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
    public OrderPage closeOrder() {
        driver.element().clickElement(closeOrderbutton);
        return new OrderPage(driver);
    }



    @Step("pay the order overprice")
    public PaymentPage payOverPrice() {

        double totalAmount = readMoneyOrFail(totalprice, "totalprice");
        double overPrice = totalAmount * 2;

        driver.element().typeText(paymentamountfield, String.valueOf(overPrice));

        this.orderTotalAmount = totalAmount;
        this.lastPaidAmount = overPrice;

        LogsManager.info("Total amount: " + totalAmount);
        LogsManager.info("Paid amount: " + overPrice);
        ScreenShotsManager.takeFullPageScreenshot(driver.get(), "overpayment");

        return this;
    }




    //validation
    @Step("Validate Discount Calculation")
    public PaymentPage validateDiscountCalculation(double discountPercentage) {
        if (driver.element().isElementVisible(manageOrdersbutton)) {
            return this;
        }
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

    @Step("Validate the order if order type is delivery")
    public OrderPage validateDeliveryOrder() throws InterruptedException {
        if (driver.element().isElementVisible(manageOrdersbutton)) {

            driver.element().clickElement(manageOrdersbutton);
            Thread.sleep(6000);
            ScreenShotsManager.takeFullPageScreenshot(driver.get(), "After Paid Order");
        }

        return new OrderPage(driver);
    }
    
    public PaymentPage setOrderNumber(long orderNumber) {
    this.orderNumber = orderNumber;
    return this;
}


    public long getOrderNumberFromUI() {
        new WebDriverWait(driver.get(), Duration.ofSeconds(10))
                .until(d -> !d.findElement(orderNumberLabel).getText().trim().isEmpty());
        String raw = driver.element().getElementText(orderNumberLabel); // عدّل حسب طريقتك في القراءة
        if (raw == null) raw = "";

        raw = raw.trim();

        if (raw.isEmpty()) {
            throw new RuntimeException("Order number is empty on UI. Check locator/wait/flow before parsing.");
        }

        // استخراج أول رقم موجود داخل النص
        Matcher m = Pattern.compile("(\\d+)").matcher(raw);
        if (!m.find()) {
            throw new RuntimeException("Order number text does not contain digits. Raw text: [" + raw + "]");
        }

        return Long.parseLong(m.group(1));
    }



    private Double waitForPaymentAmountFromDB(double expectedMin, int timeoutSeconds) {

        long endTime = System.currentTimeMillis() + timeoutSeconds * 1000L;
        Double last = null;

        while (System.currentTimeMillis() < endTime) {

            Double amount = DataBaseReader.getPayAmountByOrderNumber(this.orderNumber);
            last = amount;

            LogsManager.info("Polling DB | OrderNumber=" + this.orderNumber + " | PayAmount=" + amount);

            if (amount != null && amount >= expectedMin) {
                return amount;
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }

        ScreenShotsManager.takeFullPageScreenshot(driver.get(), "DB_PayAmount_Not_Reached");
        throw new AssertionError(
                "❌ DB PayAmount did not reach expectedMin=" + expectedMin +
                        " | last=" + last +
                        " | OrderNumber=" + this.orderNumber
        );
    }




    private double readMoneyOrFail(By locator, String nameForLog) {
    String text = null;
    try {
        // حاول تتأكد إنه ظاهر قبل القراءة (لو عندك verify)
        driver.verify().isElementVisible(locator);

        text = driver.element().getElementText(locator);
        if (text == null || text.isBlank()) {
            ScreenShotsManager.takeFullPageScreenshot(driver.get(), "NULL_TEXT_" + nameForLog);
            throw new AssertionError("❌ Text is null/blank for: " + nameForLog + " locator=" + locator);
        }

        return Double.parseDouble(text.replaceAll("[^0-9.]", "").trim());

    } catch (Exception e) {
        ScreenShotsManager.takeFullPageScreenshot(driver.get(), "READ_FAIL_" + nameForLog);
        throw new AssertionError(
                "❌ Failed to read money value for: " + nameForLog +
                " | rawText=" + text +
                " | locator=" + locator +
                " | error=" + e.getMessage(), e
        );
    }
}


    @Step("Validate DB PayAmount equals Total*2 (timeout={timeoutSeconds}s, delta={delta})")
    public PaymentPage validateDBPayAmountIsDoubleTotal(int timeoutSeconds, double delta) {

        if (this.orderNumber == 0) {
            throw new IllegalStateException("OrderNumber is not set before DB validation");
        }
        if (this.orderTotalAmount == null) {
            throw new IllegalStateException("orderTotalAmount is not set. Call payOverPrice() before DB validation");
        }

        double expected = this.orderTotalAmount * 2;

        // ننتظر لحد ما DB توصل تقريبًا للـ expected
        Double dbPayAmount = waitForPaymentAmountFromDB(expected - delta, timeoutSeconds);

        if (Math.abs(dbPayAmount - expected) > delta) {
            ScreenShotsManager.takeFullPageScreenshot(driver.get(), "DB_PayAmount_Mismatch");
            throw new AssertionError(
                    "❌ DB PayAmount mismatch | Expected (Total*2)=" + expected +
                            " | DB PayAmount=" + dbPayAmount +
                            " | OrderNumber=" + this.orderNumber
            );
        }

        LogsManager.info("✅ DB PayAmount matches Total*2 | Expected=" + expected + " | DB=" + dbPayAmount);
        return this;
    }








}
