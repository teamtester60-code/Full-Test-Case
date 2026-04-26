package com.ferpfirstcode.pages.components;

import com.ferpfirstcode.driver.GUIDriver;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.openqa.selenium.By;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SalesReport {
    private final GUIDriver driver;

    public SalesReport(GUIDriver driver) {
        this.driver = driver;
    }
    //Locators
    private final By cancelledOrders = By.xpath("//button[contains(normalize-space(.), 'Cancelled Orders') or contains(normalize-space(.), 'الطلبات الملغي')]");
    private final By cancelTime=By.xpath("//th[.//span[normalize-space()='وقت الالغاء' or normalize-space()='Canceled time']]");
    private final By firstRowcanceltime = By.xpath("(//div[contains(@class, 'e-gridcontent')]//tbody/tr[1]/td[count(//th[.//span[contains(normalize-space(text()), 'وقت الالغاء') or contains(normalize-space(text()), 'Canceled time')]]/preceding-sibling::th) + 1])[1]");


    @Step("Navigate to Cancelled Orders")
    public SalesReport navigateToCancelledOrdersandgetlatestorder() {
        driver.element().clickElement(cancelledOrders);
        driver.element().clickElement(cancelTime);
        driver.element().clickElement(cancelTime);
        driver.element().getElementText(firstRowcanceltime);
        return this;
    }



    @Step("Validate UI Canceled Time matches API Canceled Time (Max 5s Delta)")
    public SalesReport validateTimeMatchesAPI (LocalDateTime exactTimeFromAPI) {

        org.openqa.selenium.By timeCellInGrid = org.openqa.selenium.By.xpath("//div[contains(@class, 'e-gridcontent')]//tbody/tr[1]/td[count(//th[.//span[contains(normalize-space(text()), 'وقت الالغاء') or contains(normalize-space(text()), 'Canceled time')]]/preceding-sibling::th) + 1]");

        // 1. قراءة النص من الشاشة
        String reportTimeText = driver.element().getElementText(timeCellInGrid).trim();

        // 2. 🚨 المحاولة الذكية (Retry Loop): إذا كان النص فارغاً (الجدول يحمل البيانات)، انتظر وحاول مجدداً
        int retries = 0;
        while (reportTimeText.isEmpty() && retries < 5) {
            try {
                Allure.step("⏳ Grid is still loading data, waiting 1 second...");
                Thread.sleep(3000); // ننتظر ثانية واحدة
                reportTimeText = driver.element().getElementText(timeCellInGrid).trim();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            retries++;
        }

        // تأمين أخير: إذا ظلت فارغة بعد 5 ثوانٍ، ارمِ خطأ واضحاً بدلاً من الانهيار
        if (reportTimeText.isEmpty()) {
            throw new AssertionError("❌ The Time cell in the grid is completely empty after waiting! Check the Grid Locator or Page loading.");
        }

        Allure.step("⏱️ Time read from UI Grid: " + reportTimeText);

        // 3. التحويل والمقارنة (الكود السابق كما هو)
        java.time.format.DateTimeFormatter timeFormatter = java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss");
        java.time.LocalTime uiTime = java.time.LocalTime.parse(reportTimeText, timeFormatter);
        java.time.LocalTime apiTime = exactTimeFromAPI.toLocalTime();

        long differenceInSeconds = Math.abs(java.time.Duration.between(apiTime, uiTime).getSeconds());

        if (differenceInSeconds <= 5) {
            Allure.step("✅ Time match! API: " + apiTime + " | UI: " + uiTime);
            io.qameta.allure.Allure.step("✅ Time Match! API: " + apiTime + " | UI: " + uiTime);
        } else {
            throw new AssertionError("❌ Time mismatch! API: " + apiTime + " | UI: " + uiTime);
        }

        return this;
    }
}
