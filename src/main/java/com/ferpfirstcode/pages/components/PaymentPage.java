package com.ferpfirstcode.pages.components;

import com.ferpfirstcode.driver.GUIDriver;
import com.ferpfirstcode.media.ScreenShotsManager;
import com.ferpfirstcode.utils.dataReader.DataBaseReader;
import com.ferpfirstcode.utils.logs.LogsManager;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PaymentPage {
    public final GUIDriver driver;

    public PaymentPage(GUIDriver driver) {
        this.driver = driver;
    }

    //Locators
    private final By customersbutton = By.xpath("(//button[contains(@class,'btn bg-maingreen cairo-font w-100 mb-1')])[1]");
    private final By discountbutton = By.xpath("(//button[contains(@class,'btn') and contains(@class,'bg-maingreen')])[5]");
    private final By closeOrderbutton = By.cssSelector("button.btn.btn-success.ng-star-inserted");
    private final By searchbynamefield = By.xpath("(//input[@placeholder='Search By Name'])[1]");
    private final By selectcustomerbutton = By.cssSelector("button.btnPLus.btn-link");
    private final By selectaddressbutton = By.xpath("(//*[@id=\"collapseOne_@i\"]/td[5]/button)[1]");
    private final By closecustomerselectionmodalbutton = By.xpath("(//*[@id=\"nav-home\"]/div/div[2]/div[2]/div/button)[1]");
    private final By discountbypercentagebutton = By.xpath("(//input[starts-with(@id, 'DiscountPercentage')])[1]");
    private final By okbuttonofdiscountmodal = By.xpath("(//*[@id=\"modal-DetailDiscount\"]/div/div/div[3]/button)[1]");
    private final By ordercost = By.xpath("//th[normalize-space(text())='Grand Total' or contains(text(), 'الاجمالي النهائي') or contains(text(), 'الإجمالي النهائي')]/preceding-sibling::td");
    private final By totaldiscountamount = By.xpath("//*[@id=\"OverLayPin\"]/div/div[2]/div/app-payment/div[1]/div/div[1]/div/div[4]/div/div/div[1]/div/div[2]/table/tbody/tr[6]/td");
    private final By manageOrdersbutton = By.xpath("//a[@href=\"/manageorderlist\"]");
    private final By totalprice = By.xpath("(//div[contains(@class,'col-4') and contains(@class,'text-right')])[1]");
    private final By paymentamountfield = By.id("PayAmount0");
    private final By paymentamountfield2 = By.id("PayAmount1");
    private final By paymentamountfield3 = By.id("PayAmount2");
    private final By orderNumberLabel = By.xpath("(//div[contains(@class,'col-12') and contains(@class,'mt-1')]  //table[contains(@class,'table-bordered')])[last()]//tr[1]/td");
    private final By paymenttype1=By.xpath("(//div[@id='navbarSupportedContentt3']//a)[1]");
    private final By paymenttype2=By.xpath("(//div[@id='navbarSupportedContentt3']//a)[2]");
    private final By paymenttype3=By.xpath("(//div[@id='navbarSupportedContentt3']//a)[3]");
    private final By discountvalue=By.xpath("(//input[@id='DiscountAmount11'])[1]");
    private final By discountpercentage=By.xpath("(//input[contains(@id,'DiscountPercentage')])[1]");


    private long orderNumber;
    private Double lastPaidAmount;
    private Double orderTotalAmount;

    //Actions
    @Step("Select Customer")
    public PaymentPage selectCustomer() {
        driver.element().clickElement(customersbutton);
        driver.element().clickElement(searchbynamefield).typeText(searchbynamefield, "abdo");
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

        // احفظ total قبل الإغلاق لأن الإغلاق = الدفع
        double totalAmount = readMoneyOrFail(totalprice, "totalprice");
        this.orderTotalAmount = totalAmount;
        this.lastPaidAmount = totalAmount;

        LogsManager.info("Closing order with total amount: " + totalAmount);
        ScreenShotsManager.takeFullPageScreenshot(driver.get(), "before_close_order");

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

    @Step("pay the order with multiple payment")
    public PaymentPage pay_With_Multiple_Payment() {
        double totalAmount = readMoneyOrFail(totalprice, "totalprice");
        double firstPayment = totalAmount / 3;
        double secondPayment = totalAmount / 3;
        double thirdPayment = totalAmount / 3;
        driver.element().clickElement(paymenttype1);
        String namepaytype1 = driver.element().getElementText(paymenttype1);
        if (driver.element().isElementVisible(searchbynamefield)) {
            driver.element().clickElement(searchbynamefield).typeText(searchbynamefield, "abdo");
            driver.element().clickElement(selectcustomerbutton);
            driver.element().clickElement(selectaddressbutton);
            driver.element().clickElement(closecustomerselectionmodalbutton);
        }
        driver.element().typeText(paymentamountfield, String.valueOf(firstPayment));
        driver.element().clickElement(paymenttype2);
        String namepaytype2 = driver.element().getElementText(paymenttype2);
        if (driver.element().isElementVisible(searchbynamefield)) {
            driver.element().clickElement(searchbynamefield).typeText(searchbynamefield, "abdo");
            driver.element().clickElement(selectcustomerbutton);
            driver.element().clickElement(selectaddressbutton);
            driver.element().clickElement(closecustomerselectionmodalbutton);
        }
        driver.element().typeText(paymentamountfield2, String.valueOf(secondPayment));
        driver.element().clickElement(paymenttype3);
        if (driver.element().isElementVisible(searchbynamefield)) {
            driver.element().clickElement(searchbynamefield).typeText(searchbynamefield, "abdo");
            driver.element().clickElement(selectcustomerbutton);
            driver.element().clickElement(selectaddressbutton);
            driver.element().clickElement(closecustomerselectionmodalbutton);
        }
        driver.element().typeText(paymentamountfield3, String.valueOf(thirdPayment));
        String namepaytype3 = driver.element().getElementText(paymenttype3);

        Allure.step("Pay with multiple payment");
        Allure.step("Pay Type 1:"+namepaytype1);
        Allure.step("Pay Type 2:"+namepaytype2);
        Allure.step("Pay Type 3:"+namepaytype3);
        return this;
    }
    @Step("Validate Discount By Fixed Amount")
    public PaymentPage validate_Discount_By_Fixed_Amount() {
        double totalAmount = readMoneyOrFail(totalprice, "totalprice");
        double discountAmount = totalAmount / 4;
        driver.element().clickElement(discountbutton);
        driver.element().typeText(discountvalue, String.valueOf(discountAmount));
        driver.element().clickElement(okbuttonofdiscountmodal);
        Allure.step(" Discount Amount Value:" + discountAmount);
        return this;
    }



    //validation
    @Step("Apply discount percentage in UI: {discountPercentage}%")
    private void applyDiscountInUI(int discountPercentage) {
        // 1. اضغط على زر إضافة خصم
        driver.element().clickElement(discountbutton);

        // 2. اكتب الرقم العشوائي في حقل الخصم
        driver.element().typeText(discountbypercentagebutton, String.valueOf(discountPercentage));

        // 3. اضغط حفظ أو تأكيد
        driver.element().clickElement(okbuttonofdiscountmodal);
    }

    @Step("Validate Random Discount Calculation")
    public PaymentPage validateRandomDiscountCalculation() {
        if (driver.element().isElementVisible(manageOrdersbutton)) {
            return this;
        }

        // 1. استخراج السعر قبل الخصم
        String beforeText = driver.element().getElementText(ordercost).replace(",", "").trim();
        double totalBefore = Double.parseDouble(beforeText);
        ScreenShotsManager.takeFullPageScreenshot(driver.get(), "Total Price Before Discount");

        // 2. توليد نسبة خصم عشوائية (مثلاً بين 1% و 99%)
        Random rand = new Random();
        int randomDiscountPercentage = rand.nextInt(99) + 1; // +1 لضمان عدم اختيار 0%
        Allure.step("🎲 Generated Random Discount: " + randomDiscountPercentage + "%");

        // 3. حساب الإجمالي المتوقع
        // ملاحظة برمجية: نستخدم 100.0 بدلاً من 100 لتجنب مشكلة قسمة الأعداد الصحيحة في الجافا
        double expectedAfter = totalBefore - (totalBefore * randomDiscountPercentage / 100.0);

        // 4. تطبيق الخصم العشوائي في واجهة المستخدم (UI)
        // ⚠️ انتبه: يجب أن تقوم بتحديث دالتك القديمة لتستقبل هذا الرقم وتكتبه في الشاشة
        applyDiscountInUI(randomDiscountPercentage);

        // 5. استخراج السعر بعد الخصم من الشاشة
        String afterText = driver.element().getElementText(ordercost).replace(",", "").trim();
        double totalAfter = Double.parseDouble(afterText);
        ScreenShotsManager.takeFullPageScreenshot(driver.get(), "Total Price After Discount");

        // 6. التحقق (Assertion)
        if (Math.abs(expectedAfter - totalAfter) < 0.01) {
            Allure.step("✅ Discount is correct: Expected = " + expectedAfter + " | Actual = " + totalAfter);
            System.out.println("✅ Discount applied successfully: " + randomDiscountPercentage + "%");
        } else {
            throw new AssertionError("❌ Discount calculation failed! " +
                    "Applied: " + randomDiscountPercentage + "% | " +
                    "Expected = " + expectedAfter + " | Actual = " + totalAfter);
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

    private Double waitForPaymentAmountFromDB(double expected, double delta, int timeoutSeconds) {

        long endTime = System.currentTimeMillis() + timeoutSeconds * 1000L;
        Double lastAmount = null;
        Double lastPayAmount = null;

        while (System.currentTimeMillis() < endTime) {

            // 👇 You MUST fetch both values
            Double amount = DataBaseReader.getPaymentAmountFieldByOrderNumber(this.orderNumber);       // 380
            Double payAmount = DataBaseReader.getPayAmountByOrderNumber(this.orderNumber); // 760

            lastAmount = amount;
            lastPayAmount = payAmount;

            LogsManager.info(
                    "Polling DB | OrderNumber=" + this.orderNumber +
                            " | Amount=" + amount +
                            " | PayAmount=" + payAmount
            );

            // ✅ Your new rule: PayAmount = Amount * 2
            if (amount != null && payAmount != null &&
                    Math.abs(payAmount - (amount * 2)) <= delta) {
                return payAmount;
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }

        throw new AssertionError(
                "❌ DB condition failed | Expected PayAmount = Amount*2" +
                        " | Last Amount=" + lastAmount +
                        " | Last PayAmount=" + lastPayAmount +
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


    @Step("Validate DB PayAmount equals Total*2 ")
    public PaymentPage validateDBPayAmountIsDoubleTotal(int timeoutSeconds, double delta) throws InterruptedException {

        // --- Early validation ---
        if (this.orderNumber == 0) {
            throw new IllegalStateException("OrderNumber is not set before DB validation");
        }
        if (this.orderTotalAmount == null) {
            throw new IllegalStateException("orderTotalAmount is not set. Call payOverPrice() before DB validation");
        }

        // --- Expected DB value is double the order total ---
        double expectedPayAmount = this.orderTotalAmount * 2;

        // --- Wait for DB to reach expected value ---
        Thread.sleep(3000);
        Double dbPayAmount = waitForPaymentAmountFromDB(expectedPayAmount, delta, timeoutSeconds);

        // --- Logging for info ---
        LogsManager.info(String.format(
                "Checking DB PayAmount | OrderNumber=%d | Expected=%.2f | Actual=%.2f",
                this.orderNumber, expectedPayAmount, dbPayAmount
        ));

        // --- Validation with screenshot on mismatch ---
        if (Math.abs(dbPayAmount - expectedPayAmount) > delta) {
            throw new AssertionError(String.format(
                    "❌ DB PayAmount mismatch | Expected=%.2f | DB PayAmount=%.2f | OrderNumber=%d",
                    expectedPayAmount, dbPayAmount, this.orderNumber
            ));
        }

        // --- Allure step & logging for success ---
        String successMessage = String.format(
                "✅ DB PayAmount matches Total*2 | Expected=%.2f | DB=%.2f",
                expectedPayAmount, dbPayAmount
        );
        Allure.step(successMessage);
        LogsManager.info(successMessage);

        return this;
    }


    @Step("Validate order created within last {seconds} seconds")
    public PaymentPage validateOrderCreatedRecently(int seconds) {

        if (this.orderNumber == 0) {
            throw new IllegalStateException("OrderNumber is not set before DB validation");
        }

        LocalDateTime dbTime =
                DataBaseReader.getOrderCreationDateTime(this.orderNumber);

        if (dbTime == null) {
            throw new AssertionError("❌ CreationTime is null in DB");
        }

        LocalDateTime now = LocalDateTime.now();

        long diffSeconds =
                java.time.Duration.between(dbTime, now).getSeconds();

        if (diffSeconds > seconds) {
            throw new AssertionError(
                    "❌ Order not created recently | DB Time=" + dbTime +
                            " | Now=" + now +
                            " | DiffSeconds=" + diffSeconds +
                            " | OrderNumber=" + this.orderNumber
            );
        }

        LogsManager.info(
                "✅ Order created recently | DiffSeconds=" + diffSeconds +
                        " | OrderNumber=" + this.orderNumber
        );

        return this;
    }


    @Step("Validate latest DB order matches UI order")
    public PaymentPage validateLatestOrderFromDB() {

        if (this.orderTotalAmount == null) {
            throw new IllegalStateException("orderTotalAmount not set");
        }

        Order order =
                DataBaseReader.getLatestOrderByFilter(
                        "Admin Admin",
                        this.orderTotalAmount,
                        30
                );

        if (order == null) {
            throw new AssertionError("❌ No matching order found in DB");
        }

        if (Math.abs(order.getTotal() - this.orderTotalAmount) > 0.01) {

            throw new AssertionError(
                    "❌ DB Total mismatch | UI=" + this.orderTotalAmount +
                            " | DB=" + order.getTotal()
            );
        }

        this.orderNumber = order.getOrderNumber();

        LogsManager.info(
                "✅ Order validated from DB | OrderNumber=" + order.getOrderNumber() +
                        " | Total=" + order.getTotal() +
                        " | Created=" + order.getCreationTime()
        );

        return this;
    }

    private Double waitForOrderTotalFromDB(double expectedMin, int timeoutSeconds) {

        long endTime = System.currentTimeMillis() + timeoutSeconds * 1000L;
        Double last = null;

        while (System.currentTimeMillis() < endTime) {

            Double total = DataBaseReader.getOrderTotalByOrderNumber(this.orderNumber);
            last = total;

            LogsManager.info("Polling DB Total | OrderNumber=" + this.orderNumber + " | DB Total=" + total);

            if (total != null && total >= expectedMin) {
                return total;
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }

        ScreenShotsManager.takeFullPageScreenshot(driver.get(), "DB_Total_Not_Reached");

        throw new AssertionError(
                "❌ DB Total did not reach expectedMin=" + expectedMin +
                        " | last=" + last +
                        " | OrderNumber=" + this.orderNumber
        );
    }

    @Step("pay less than total cost")
    public PaymentPage payLessThanTotalCost() {
        driver.element().typeText(paymentamountfield,"1");
        driver.element().clickElement(closeOrderbutton);
        return this;
    }


    @Step("Validate the Message that appear when pay less than total cost")
    public PaymentPage validate_pay_amount_less_than_total_cost(String expectedText) {
        driver.element().clickElement(closeOrderbutton);

        By toast = By.cssSelector(".toast-warning .toast-message");

        driver.element().getElementText(toast);

        String actual = driver.element().getElementText(toast);

        if (!actual.contains(expectedText)) {
            throw new AssertionError(
                    "❌ Expected: " + expectedText + "\nActual: " + actual
            );
        }

        System.out.println("✅ Toast validated: " + actual);
        return this;
    }

    @Step("Validate DB Total equals UI Total")
    public PaymentPage validateDBTotalEqualsUITotal(double delta) {

        if (this.orderNumber == 0) {
            throw new IllegalStateException("OrderNumber is not set before DB validation");
        }

        if (this.orderTotalAmount == null) {
            throw new IllegalStateException("orderTotalAmount is not set before DB validation");
        }

        double dbTotal = waitForOrderTotalFromDB(this.orderTotalAmount - delta, 20);

        if (Math.abs(dbTotal - this.orderTotalAmount) > delta) {
            ScreenShotsManager.takeFullPageScreenshot(driver.get(), "DB_Total_Mismatch");

            throw new AssertionError(
                    "❌ DB Total mismatch | UI Total=" + this.orderTotalAmount +
                            " | DB Total=" + dbTotal +
                            " | OrderNumber=" + this.orderNumber
            );
        }
        Allure.step("✅ DB Total matches UI Total | UI=" + this.orderTotalAmount + " | DB=" + dbTotal);

        LogsManager.info("✅ DB Total matches UI Total | UI=" + this.orderTotalAmount + " | DB=" + dbTotal);
        return this;
    }









}
