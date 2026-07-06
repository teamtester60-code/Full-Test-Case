package com.ferpfirstcode.pages.components;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.ferpfirstcode.driver.GUIDriver;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;

public class OrderTypeSettingPage {
    private final GUIDriver driver;
    public OrderTypeSettingPage(GUIDriver driver) { 
        this.driver = driver;
    }
private final By ordertypelist=By.xpath("//li[contains(@class,'breadcrumb-item')]//a[ contains(.,'قائمة انواع الفواتير') or contains(translate(normalize-space(.), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), 'order type list') ]");
private final By ordertypebutton=By.xpath("//button[contains(@class,'btn-primary')]");
private final By homebutton =By.xpath("//li[contains(@class,'menu-item')]//span[contains(.,'الرئيسية') or contains(.,'home')]");


@Step("Enable 'Payment By Another User' for ALL Order Types")
public OrderTypeSettingPage enablePaymentByAnotherUserForAllTypes() throws InterruptedException {

    // 1. تحديد محددات الأزرار الثابتة بناءً على المعطيات الخاصة بك
    By editYellowButton = By.xpath("//button[@id='Edit']");
    By saveButton = By.xpath("//button[contains(@class,'btn-outline-success')]");
    By paymentByAnotherUserCheckbox = By.xpath("//input[@type='checkbox' and @name='PaymentByAnotherUser']");
    driver.element().clickElement(ordertypelist);
    // 2. معرفة عدد الصفوف الإجمالي في الجدول
    // نستخدم المحدد العام (بدون أرقام) لجلب كل الصفوف وحساب عددها
    Thread.sleep(2000);
    By allRowsLocator = By.xpath("//table[@id='Grid_content_table']//tr");
    int totalOrderTypes = driver.get().findElements(allRowsLocator).size();

    Allure.step("📊 Found " + totalOrderTypes + " Order Types to update.");

    // 3. حلقة تكرارية (Loop) تبدأ من 1 (لأن XPath يبدأ من 1)
    for (int i = 1; i <= totalOrderTypes; i++) {
        Allure.step("🔄 Processing Order Type Row #" + i);

        Thread.sleep(2000);

        // 💡 هنا يكمن السحر: جعلنا الرقم (Index) ديناميكياً يتغير مع كل لفة
        By dynamicRow = By.xpath("(//table[@id='Grid_content_table']//tr)[" + i + "]");
        // أ. الضغط على الصف لتحديده
        driver.get().findElements(dynamicRow);
        driver.element().clickElement(dynamicRow);


        driver.element().clickElement(editYellowButton);

        // انتظار حتى تفتح الشاشة (يفضل استبدالها لاحقاً بـ Explicit Wait)
        Thread.sleep(2000);


        // ج. جلب خيار "Payment By Another User"
        WebElement paymentCheckbox = driver.findElement(paymentByAnotherUserCheckbox);

        // د. فحص حالة الـ Checkbox وتفعيله إذا كان مغلقاً
        if (!paymentCheckbox.isSelected()) {
            
            // ⚠️ تنبيه: في بعض الإطارات الحديثة (Angular)، قد يكون الـ input مخفياً برمجياً 
            // إذا واجهت خطأ (Element Not Interactable)، استخدم كود الجافاسكريبت أسفله بدلاً من الكليك العادي:
            // ((JavascriptExecutor) driver).executeScript("arguments[0].click();", paymentCheckbox);
            
            paymentCheckbox.click();
            Allure.step("✅ Enabled option for Order Type #" + i);
        } else {
            Allure.step("⚡ Option was already enabled.");
        }

        // هـ. حفظ التعديلات
        driver.get().findElements(saveButton);
        driver.element().clickElement(saveButton);

        // و. انتظار حتى يغلق المودال ويتم تحديث الجدول وتجنب الـ StaleElement
        Thread.sleep(2000);
        driver.element().clickElement(ordertypelist);
        Thread.sleep(2000);
    }

    Allure.step("🎉 Successfully updated all Order Types!");
    return this; // لدعم الـ Fluent Design Pattern
}


    @Step("Enable 'Is Has Discount' for ALL Order Types")
    public OrderTypeSettingPage enableHasDiscountForAllTypes() throws InterruptedException {

        // 1. تحديد محددات الأزرار الثابتة
        By editYellowButton = By.xpath("//button[@id='Edit']");
        By saveButton = By.xpath("//button[contains(@class,'btn-outline-success')]");

        // 🔥 تم تحديث هذا المحدد ليعمل على زر الخصم (تأكد أن الخاصية name أو id في الـ HTML هي IsHasDiscount)
        By hasDiscountCheckbox = By.xpath("//input[@type='checkbox' and @name='IsHasDiscount']");
        Thread.sleep(2000);

        driver.element().clickElement(ordertypelist);

        // 2. معرفة عدد الصفوف الإجمالي في الجدول
        Thread.sleep(2000);
        By allRowsLocator = By.xpath("//table[@id='Grid_content_table']//tr");
        int totalOrderTypes = driver.get().findElements(allRowsLocator).size();

        Allure.step("Found " + totalOrderTypes + " Order Types to update for Discount.");

        // 3. حلقة تكرارية لتعديل كل صف
        for (int i = 1; i <= totalOrderTypes; i++) {
            Allure.step("Processing Order Type Row #" + i);

            // جعلنا الرقم (Index) ديناميكياً
            By dynamicRow = By.xpath("(//table[@id='Grid_content_table']//tr)[" + i + "]");

            // أ. الضغط على الصف لتحديده
            driver.element().clickElement(dynamicRow);

            // ب. الضغط على زر التعديل
            driver.element().clickElement(editYellowButton);

            Thread.sleep(2000);

            // ج. جلب خيار "IsHasDiscount"
            WebElement discountCheckbox = driver.findElement(hasDiscountCheckbox);

            // د. فحص حالة الـ Checkbox وتفعيله إذا كان مغلقاً
            if (discountCheckbox.isSelected()) {

                discountCheckbox.click();
                Allure.step("Disable 'Is Has Discount' option for Order Type #" + i);

            } else {
                Allure.step("'Is Has Discount' Option was already Disabled.");
            }

            // هـ. حفظ التعديلات
            driver.element().clickElement(saveButton);

            // و. انتظار حتى يغلق المودال ويتم تحديث الجدول
            Thread.sleep(2000);
            driver.element().clickElement(ordertypelist);
            Thread.sleep(2000);
        }

        Allure.step("Successfully updated all Order Types for Discount!");
        return this;
    }

@Step("Click on Home Button")
public HomePage clickOnHomeButton() {
    driver.element().clickElement(homebutton);
    return new HomePage(driver);
}

}
