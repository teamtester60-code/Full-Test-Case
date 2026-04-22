package com.ferpfirstcode.pages.components;

import com.ferpfirstcode.driver.GUIDriver;
import com.ferpfirstcode.utils.logs.LogsManager;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.apache.poi.ss.formula.functions.T;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class DailyStockPage {
    private final GUIDriver driver;

    public DailyStockPage(GUIDriver driver) {
        this.driver = driver;
    }

    //Locators
    private final By editButton = By.cssSelector("button#Edit");
    private final By createNewButton = By.cssSelector("button#Add");
    private final By dailystocklistbutton = By.xpath("(//li[contains(@class,'breadcrumb-item')]//a[contains(@class,'styleAll')])[3]");
    private final By dateindailystock=By.xpath("//tr[contains(@class,'e-row')]/td[@aria-colindex='1']");
    private final By lastquantityindailystock=By.xpath("//input[starts-with(@id,'LastQuantity')]");
    private final By nameoffirstproduct=By.xpath("(//input[starts-with(@id,'product')])[1]");
    private final By selectallproduct=By.cssSelector("button#SelectAllProducts_id");
    private final By insertItemQuantityButton=By.xpath("(//input[starts-with(@id,'CurrentQuantity1')])[1]");
    private final By savebutton=By.cssSelector("li.nav-item button.btnNav.btn-outline-success");
    private final By homebutton=By.xpath("//i[text()='home']/ancestor::li[contains(@class,'menu-item')]");
    private final By orderbutton=By.cssSelector("button#Order");





    @Step(" go to daily stock List Page")
    public DailyStockPage gotoDailyStockListPage() {
    driver.element().clickElement(dailystocklistbutton);
    return this;
}


    @Step("Open today's daily stock if exists, otherwise create a new one")
    public HomePage openTodayDailyStockOrCreateNew0() throws InterruptedException {

        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        Thread.sleep(3000);
        List<WebElement> rows = driver.get().findElements(dateindailystock);
        for (WebElement row : rows) {
            String rowText = row.getText().trim();
            if (rowText.contains(today)) {
                row.click();
                driver.element().clickElement(editButton);
                return openTodayDailyStockOrCreateNew();
            }
            else{
                driver.element().clickElement(createNewButton);
                Thread.sleep(3000);
                driver.element().clickElement(selectallproduct);
            }
        }
        Thread.sleep(3000);
        driver.element().clickElement(insertItemQuantityButton);
        driver.element().typeText(insertItemQuantityButton, "10");
        driver.element().clickElement(savebutton);
        driver.browser().refreshPage();
        return new HomePage(driver);

    }


    @Step("Open today's daily stock if exists, otherwise create a new one")
    public HomePage openTodayDailyStockOrCreateNew() throws InterruptedException {

        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        Thread.sleep(3000);

        List<WebElement> rows = driver.get().findElements(dateindailystock);

        // 🔍 ندور الأول هل موجود ولا لا
        for (WebElement row : rows) {
            String rowText = row.getText().trim();

            if (rowText.contains(today)) {
                System.out.println("✅ Today's stock found");

                row.click();
                Thread.sleep(3000);
                driver.element().clickElement(editButton);

                return new HomePage(driver); // ✅ بدون recursion
            }
        }

        // ❌ لو مش موجود → نعمل create جديد
        Allure.step("⚠️ Today's stock not found → creating new");
        Thread.sleep(3000);

        driver.element().clickElement(createNewButton);

        driver.element().clickElement(selectallproduct);

        Thread.sleep(3000);
        driver.element().typeText(insertItemQuantityButton, "10");

        driver.element().clickElement(savebutton);

        driver.browser().refreshPage();

        Thread.sleep(3000);
        return new HomePage(driver);
    }




    @Step("Get Product Name And Last Quantity Of It")
    public ProductStockData getProductNameAndLastQuantity() {

        driver.element().isElementVisible(nameoffirstproduct);
        driver.element().isElementVisible(lastquantityindailystock);

        String productName = driver.element().getAttribute(nameoffirstproduct, "value");
        String lastQuantityText = driver.element().getAttribute(lastquantityindailystock, "value");

        System.out.println("Raw Product Name = [" + productName + "]");
        System.out.println("Raw Last Quantity = [" + lastQuantityText + "]");

        if (productName == null || productName.trim().isEmpty()) {
            throw new AssertionError("Product name is empty. Check how the name is stored in the element.");
        }

        if (lastQuantityText == null || lastQuantityText.trim().isEmpty()) {
            throw new AssertionError("Last quantity is empty. Check locator of lastquantityindailystock.");
        }

        return new ProductStockData(
                productName.trim(),
                Integer.parseInt(lastQuantityText.trim())
        );
    }

    @Step("Go To Home Page")
    public HomePage clickOnHomeButton() {
        driver.element().clickElement(homebutton);
        return new HomePage(driver);
    }

}
