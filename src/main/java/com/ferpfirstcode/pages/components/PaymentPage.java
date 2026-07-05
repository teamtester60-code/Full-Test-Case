package com.ferpfirstcode.pages.components;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.ferpfirstcode.driver.GUIDriver;
import com.ferpfirstcode.media.ScreenShotsManager;
import com.ferpfirstcode.utils.dataReader.DataBaseReader;
import com.ferpfirstcode.utils.logs.LogsManager;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.testng.Assert;

public class PaymentPage {
    public final GUIDriver driver;

    public PaymentPage(GUIDriver driver) {
        this.driver = driver;
    }

    //Locators
    private final By customersbutton = By.xpath("(//button[contains(@class,'btn bg-maingreen cairo-font w-100 mb-1')])[1]");
    private final By discountbutton = By.xpath("(//button[contains(@class,'btn') and contains(@class,'bg-maingreen')])[5]");
    private final By closeOrderbutton = By.xpath("//button[contains(@class, 'btn-success') and (contains(., 'إغلاق فاتورة') or contains(., 'close Order') or contains(., 'Close Order'))]");
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


    private final By discountmodal= By.xpath("//div[contains(@class, 'show')]//h5[contains(., 'الخصم') or contains(., 'Discount')]");

    private long orderNumber;
    private Double lastPaidAmount;
    private Double orderTotalAmount;
    private String currentOrderType = "";

    //Actions

    @Step("Set Current Order Type to: {orderType}")
    public PaymentPage setOrderType(String orderType) {
        this.currentOrderType = orderType;
        return this;
    }
    public String getOrderType() {
        return this.currentOrderType;
    }
    @Step("Select Customer")
    public PaymentPage selectCustomer() {
        driver.element().clickElement(customersbutton);
        driver.element().clickElement(searchbynamefield).typeText(searchbynamefield, "abdo");
        driver.element().clickElement(selectcustomerbutton);
        driver.element().clickElement(selectaddressbutton);
        driver.element().clickElement(closecustomerselectionmodalbutton);
        return this;
    }

    @Step("Extract Order Number from Payment Screen")
    public String getOrderNumber() throws InterruptedException {

        By orderNumberLocator = By.xpath("//tr[th[contains(., 'Order Number') or contains(., 'رقم الطلب') or contains(., 'Invoice Number') or contains(., 'رقم الفاتورة')]]//td");

        String orderNumber = "";

        // 💡 حلقة انتظار ذكية (Polling): يحاول السيلينيوم قراءة النص، وإذا كان فارغاً ينتظر نصف ثانية ويحاول مجدداً (بحد أقصى 10 محاولات = 5 ثوانٍ)
        for (int i = 0; i < 10; i++) {
            orderNumber = driver.element().getElementText(orderNumberLocator).trim();

            if (!orderNumber.isEmpty()) {
                break; // بمجرد أن يجد الرقم، يخرج من الحلقة فوراً
            }
            Thread.sleep(500); // انتظار نصف ثانية حتى يكتمل الـ Angular Rendering
        }

        // Guard Clause للتأكد أننا التقطنا الرقم فعلياً قبل إكمال التست
        if (orderNumber.isEmpty()) {
            Assert.fail("❌ لم يتمكن السيلينيوم من قراءة رقم الطلب! الشاشة لم تعرض الرقم في الوقت المحدد.");
        }

        Allure.step("✅ Extracted Order Number: " + orderNumber);

        return orderNumber;
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
        if (driver.element().isElementVisible(closeOrderbutton)) {


        // احفظ total قبل الإغلاق لأن الإغلاق = الدفع
        double totalAmount = readMoneyOrFail(totalprice, "totalprice");
        this.orderTotalAmount = totalAmount;
        this.lastPaidAmount = totalAmount;

        LogsManager.info("Closing order with total amount: " + totalAmount);
        ScreenShotsManager.takeFullPageScreenshot(driver.get(), "before_close_order");

        driver.element().clickElement(closeOrderbutton);

        return new OrderPage(driver);

        }
         else{
//            By backbutton = By.xpath("//button[contains(@class, 'bg-maingreen') and contains(.,'عودة')]");
//            driver.element().clickElement(backbutton);

            Allure.step("Order Type is  delivery");
            return new OrderPage(driver);
        }

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
    // Helper method to handle the customer selection popup gracefully
    private void handleCustomerSelectionPopupIfVisible(String customerName) {
        if (driver.element().isElementVisible(searchbynamefield)) {
            driver.element().clickElement(searchbynamefield).typeText(searchbynamefield, customerName);
            driver.element().clickElement(selectcustomerbutton);
            driver.element().clickElement(selectaddressbutton);
            driver.element().clickElement(closecustomerselectionmodalbutton);
        }
    }

    @Step("Pay the order with multiple payment types")
    public PaymentPage pay_With_Multiple_Payment() {

        // 1. Fetch the stored order type from GUIDriver
        String orderType = driver.getSelectedOrderType();

        // 2. Guard Clause (Early Exit) for Delivery Orders
        if (orderType != null && !orderType.isBlank() &&
                (orderType.equalsIgnoreCase("delivery") || orderType.contains("دليفر") || orderType.contains("توصيل"))) {

            Allure.step("✅ Order Type = Delivery. Skipping multiple payments gracefully.");
            return this; // Skip the rest of the method and pass the test
        }

        // 3. Normal execution for non-delivery orders (Dine-in, Takeaway, etc.)
        double totalAmount = readMoneyOrFail(totalprice, "totalprice");

        // Format the amount to 2 decimal places to prevent UI input errors (e.g., 33.33 instead of 33.3333333)
        String paymentAmountStr = String.format(java.util.Locale.US, "%.2f", totalAmount / 3);

        // --- First Payment ---
        driver.element().clickElement(paymenttype1);
        String payType1Name = driver.element().getElementText(paymenttype1);
        handleCustomerSelectionPopupIfVisible("abdo");
        driver.element().typeText(paymentamountfield, paymentAmountStr);

        // --- Second Payment ---
        driver.element().clickElement(paymenttype2);
        String payType2Name = driver.element().getElementText(paymenttype2);
        handleCustomerSelectionPopupIfVisible("abdo");
        driver.element().typeText(paymentamountfield2, paymentAmountStr);

        // --- Third Payment ---
        driver.element().clickElement(paymenttype3);
        String payType3Name = driver.element().getElementText(paymenttype3);
        handleCustomerSelectionPopupIfVisible("abdo");
        driver.element().typeText(paymentamountfield3, paymentAmountStr);

        // --- Logging ---
        Allure.step("✅ Paid successfully using 3 split payments:");
        Allure.step("1. " + payType1Name + " | Amount: " + paymentAmountStr);
        Allure.step("2. " + payType2Name + " | Amount: " + paymentAmountStr);
        Allure.step("3. " + payType3Name + " | Amount: " + paymentAmountStr);

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
        if ( driver.element().isElementVisible(discountmodal)) {
            driver.element().typeText(discountbypercentagebutton, String.valueOf(discountPercentage));

            // 3. اضغط حفظ أو تأكيد
            driver.element().clickElement(okbuttonofdiscountmodal);
        }

    }

    @Step("Validate Random Discount Calculation")
    public PaymentPage validateRandomDiscountCalculation() {
        if (driver.element().isElementVisible(manageOrdersbutton)) {
            return this;
        }
        String orderType = driver.getSelectedOrderType();
        if (orderType.contains("هالك واعدامات" ) || orderType.contains("تذوق العملاء")) {
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
            Allure.step("✅ Discount applied successfully: " + randomDiscountPercentage + "%");
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
    @Step("Get current total from UI")
    public double getOrderTotalFromUI() {
        // هذه الدالة تقرأ القيمة من الشاشة وتُرجعها فوراً
        return readMoneyOrFail(totalprice, "totalprice");
    }

//    @Step("Close Order and catch Serial dynamically")
//    public String closeOrderAndCatchSerial() {
//        // استخدم Locator زر الدفع الفعلي الخاص بهذه الصفحة
//        // تأكد أن payButtonLocator معرف في أعلى كلاس PaymentPage
//        return driver.element().catchSerialFromNetwork(closeOrderbutton);
//    }

    @Step("Validate DB PayAmount equals Total*2 using exact Serial")
    public PaymentPage validateDBPayAmountIsDoubleTotal(String exactSerial, int timeoutSeconds, double delta) throws InterruptedException {

        // --- Early validation (Guard Clauses) ---
        if (exactSerial == null || exactSerial.trim().isEmpty()) {
            throw new IllegalStateException("❌ Serial is not set before DB validation. Ensure closeOrderAndCatchSerial() was executed successfully.");
        }
        if (this.orderTotalAmount == null) {
            throw new IllegalStateException("❌ orderTotalAmount is not set. Call payOverPrice() before DB validation.");
        }

        // --- Expected DB value is double the order total ---
        double expectedPayAmount = this.orderTotalAmount * 2;

        // --- Wait for DB to reach expected value ---
        // تم إضافة تأخير بسيط للسماح للسيرفر بإنهاء المعالجة
        Thread.sleep(3000);

        // --- Fetch from DB ---
        Double dbPayAmount = DataBaseReader.waitForPaymentAmountFromDBBySerial(exactSerial, expectedPayAmount, delta, timeoutSeconds);

        // --- Validation with Screenshot on Failure ---
        if (Math.abs(dbPayAmount - expectedPayAmount) > delta) {
            // 🔥 هذه هي الخطوة المهمة: التقاط شاشة عند حدوث الخطأ
            ScreenShotsManager.takeFullPageScreenshot(driver.get(), "DB_PayAmount_Mismatch");

            throw new AssertionError(String.format(
                    "❌ DB PayAmount mismatch | Serial=%s | Expected=%.2f | DB Actual=%.2f",
                    exactSerial, expectedPayAmount, dbPayAmount
            ));
        }

        // --- Success Reporting ---
        String successMessage = String.format(
                "✅ DB PayAmount matches Total*2 | Serial=%s | Expected=%.2f | DB=%.2f",
                exactSerial, expectedPayAmount, dbPayAmount
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
    private Double waitForOrderTotalFromDB(String exactSerial, int timeoutSeconds, String orderType) {
        long endTime = System.currentTimeMillis() + timeoutSeconds * 1000L;
        Double lastValue = null;

        while (System.currentTimeMillis() < endTime) {

            // Fetch the 'Total' field directly using the unique Serial
            Double total = DataBaseReader.getOrderTotalBySerial(exactSerial);

            if (total != null && total > 0.0) {

                // ====================================================================
                // SERVICE CHARGE LOGIC (12% for Dine-In applied to DB Total)
                // ====================================================================
                boolean isDineIn = orderType != null && (orderType.toLowerCase().contains("dine") || orderType.contains("صالة") || orderType.contains("صاله"));

                if (isDineIn) {
                    // 1. Extract the Net amount (Since the current total includes 14% VAT)
                    // Math: Net = Total / 1.14
                    double netAmount = total / 1.14;

                    // 2. Calculate the 12% Service Charge strictly from the Net amount
                    double serviceCharge = netAmount * 0.12;

                    // 3. Add the exact service charge to the original total
                    total = total + serviceCharge;

                    // 4. Precision fix for Java floating-point rounding (e.g., converts 55.26315 to 55.26)
                    total = Math.round(total * 100.0) / 100.0;

                    LogsManager.info("Dine-In order detected. Extracted Net amount and applied 12% service charge. Final Calculated Total: " + total);
                }
                // ====================================================================

                LogsManager.info("Polling DB Total | Serial=" + exactSerial + " | Final DB Total=" + total);
                return total;
            }

            lastValue = total;
            LogsManager.info("Polling DB Total | Serial=" + exactSerial + " | Current DB Total=" + total);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }

        ScreenShotsManager.takeFullPageScreenshot(driver.get(), "DB_Total_Not_Reached");
        throw new AssertionError(
                "Timeout: Order Total did not appear in the Database within the specified timeframe." +
                        " | Last Value=" + lastValue +
                        " | Serial=" + exactSerial
        );
    }
    @Step("Pay less than total cost")
    public PaymentPage payLessThanTotalCost() {

        String type = getOrderType(); // قراءة النوع المحفوظ

        // Guard Clause (Early Exit)
        if (type != null && !type.isBlank() &&
                (type.equalsIgnoreCase("delivery") || type.contains("دليفر") || type.contains("توصيل"))) {

            Allure.step("✅ Order Type = Delivery. Skipping payment logic gracefully.");
            return this; // يخرج بنجاح دون تنفيذ الدفع
        }

        // الكود العادي لباقي أنواع الطلبات
        driver.element().typeText(paymentamountfield, "1");
        driver.element().clickElementRaw(closeOrderbutton);

        return this;
    }


    @Step("Validate the Message that appear when pay less than total cost")
    public PaymentPage validate_pay_amount_less_than_total_cost(String expectedText) {

        String orderType = driver.getSelectedOrderType();

        if (orderType != null && !orderType.isBlank() &&
                (orderType.equalsIgnoreCase("delivery") || orderType.contains("دليفر") || orderType.contains("توصيل"))) {

            Allure.step("✅ Order Type = Delivery. Skipping toast validation gracefully.");
            return this; // إنهاء الدالة بنجاح فوراً
        }
//
//        driver.element().clickElement(closeOrderbutton);

        By toast = By.cssSelector(".toast-warning .toast-message");

        String actual = driver.element().getElementText(toast);

        if (actual == null || !actual.contains(expectedText)) {
            throw new AssertionError(
                    "❌ Expected to contain: " + expectedText + "\nActual message was: " + actual
            );
        }

        Allure.step("✅ Toast validated successfully: " + actual);
        return this;
    }

    // تأكد من تمرير الـ uiTotalAmount كـ Parameter
    @Step("Validate DB Total equals UI Total using Serial: {exactSerial} | Type: {orderType}")
    public PaymentPage validateDBTotalEqualsUITotal(String exactSerial, double uiTotalAmount, double delta, String orderType) {

        if (exactSerial == null || exactSerial.trim().isEmpty()) {
            throw new IllegalStateException("❌ Serial is empty. Ensure the serial was captured successfully before calling this method.");
        }

        // 1. Fetch the total directly from the database using the unique serial
        // Note: The 12% Dine-in Service Charge logic is handled inside this DB fetching method.
        double dbTotal = waitForOrderTotalFromDB(exactSerial, 20, orderType);

        // 2. Comparison Evaluation
        if (Math.abs(dbTotal - uiTotalAmount) > delta) {
            ScreenShotsManager.takeFullPageScreenshot(driver.get(), "DB_Total_Mismatch");
            throw new AssertionError(String.format(
                    "❌ Match Failed! | Serial: %s | Expected UI Total: %.2f | Actual DB Total: %.2f",
                    exactSerial, uiTotalAmount, dbTotal
            ));
        }

        LogsManager.info(String.format("✅ Match Successful! | Serial: %s | Matched Total: %.2f", exactSerial, dbTotal));
        return this;
    }
}
