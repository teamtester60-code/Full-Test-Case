package com.ferpfirstcode.pages.components;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;


import org.openqa.selenium.*;
import org.openqa.selenium.devtools.v148.network.Network;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.ferpfirstcode.apis.UserManagmentAPI;
import com.ferpfirstcode.driver.GUIDriver;
import com.ferpfirstcode.media.ScreenShotsManager;
import com.ferpfirstcode.pojos.ProductData;
import com.ferpfirstcode.utils.dataReader.DataBaseReader;
import com.ferpfirstcode.utils.dataReader.PropertyReader;
import com.ferpfirstcode.utils.logs.LogsManager;

import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;


import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.HasDevTools;
import org.openqa.selenium.devtools.v148.network.Network; // تأكد من رقم الإصدار لديك
import org.openqa.selenium.devtools.v148.network.model.RequestId;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class OrderPage {
    private final GUIDriver driver;
    private List<ProductData> apiProductsList;
    private String currentSearchedProduct;
    private LocalDateTime exactCancelTime;
    private List<String> apiordertypelist;
    private By currentordertype;
    private String savedOrderNumber = "";

    public OrderPage(GUIDriver driver) {
        this.driver = driver;
    }
    //Locators
    private final By volumesPOPUP= By.cssSelector("div.modal-content.modal-contentWidth");
    private final By okbuttonforsidedishmodal = By.cssSelector("button.btn.btn-primary.btn-footer.btnEdit");
    private final By ordertypebutton= By.id("ordertype-tab");
    private final By newOrderButton= By.id("c-tab");
    private final By numberofpeopleontable= By.cssSelector("img.pull-right.img-thumbnail");
    private final By personscountbar= By.id("PersonsCount");
    private final By okbuttononpersonscountbar= By.id("#modal-Persons div.modal-footer button.btnEdit");
    private final By sendorderbutton= By.xpath("//*[@id=\"ct-tab\"]/div[1]");
    private final By payementbutton= By.xpath("//*[@id=\"coact-tab\"]/div[1]");
    private final By sidedishPOPUP= By.xpath("//div[@id='modal-NewSideDishes' and contains(@class,'show')]");
    private final By sidedishitem= By.xpath("(//div[contains(@class,'row')]//input[@type='checkbox'])[2]");
    private final By searchbynamefield= By.xpath("(//input[@placeholder='Search By Name'])[1]");
    private final By searchbyphonefield= By.xpath("(//input[@placeholder='Phone Number'])[1]");
    private final By selectcustomerbutton= By.cssSelector("button.btnPLus.btn-link");
    private final By selectaddressbutton= By.xpath("(//button[contains(@class, 'btn-success') and (contains(., 'اختر') or contains(., 'Select'))])[1]");
    private final By closecustomerselectionmodalbutton= By.xpath("//div[contains(@class, 'show')]//button[contains(@data-dismiss, 'modal') and (contains(., 'اغلاق') or contains(., 'Close'))]");
    private final By opensentordersbutton= By.xpath("//*[@id=\"OverLayPin\"]/div/div[1]/div[2]/div[3]/div/nav/ul/li[4]/a");
    private final By totalpricebeforesendorder=By.xpath("//li[contains(@class,'bg-maingreen')]//h5[last()]");
    private final By totalpriceaftersendorder=By.xpath("//table[contains(@class,'table')]//tbody//tr[1]/td[3]");
    private final By tablenumber = By.xpath("(//li[starts-with(@id,'liDrag') and not(contains(@class,'TableBusy'))])[1]");
    private final By selectorderbutton= By.xpath("(//table//tbody//button)[2]");
    private final By employeebutton= By.xpath("//*[@id=\"modal-Waiter\"]/div/div/div[2]/div/div/div/div[3]/perfect-scrollbar/div/div[1]/table/tbody/tr[1]/td/button");
    private final By cashieroperationbutton= By.xpath("//*[@id=\"dropdownMenuLink\"]");
    private final By followorderbutton= By.xpath("//a[contains(@class, \"dropdown-item\") and @href=\"/FollowOrder\"]");
    private final By checklorderbutton=By.xpath("(//table[contains(@class,'e-table')]//tbody//tr[last()]//input[@type='checkbox'])[1]");
    private final By assigntodriverbutton= By.xpath("(//button[contains(@class, \"btn-success\") and contains(@class, \"m-0\")])[2]");
    private final By assignbutton= By.xpath("(//button[contains(@class,'btn-info')])[1]");
    private final By cancelprintbutton= By.xpath("//button[normalize-space()='Cancel']");
    private final By paytheordersbutton= By.xpath("//a[@aria-controls='tab144']");
    private final By selcetordertopaybutton= By.xpath("//tbody/tr[last()]/td[1]//input[contains(@class,'e-checkbox')]");
    private final By paybutton= By.xpath("//button[contains(@class, 'btn-success') and contains(@class, 'btnEdit') and (contains(., 'تسديد') or contains(., 'Pay'))]");
    private final By ordertypes = By.xpath("//div[@id='v-pills-tab']//a");
    private final By clickOk= By.xpath("//*[@id=\"modal-OrderType\"]/div/div/div[3]/button");
    private final By cancelproductsbutton=By.xpath("//div[contains(@class, 'fiixedCancel')]");
    private final By checkproducttocancel=By.xpath("(//input[@type='checkbox' and contains(@class, 'form-control')])[2]");
    private final By sendorderaftercancelproduct=By.xpath("//button[contains(@class, 'btnEdit') and contains(text(), 'Send')]");
    private final By customerresoncancelbutton = By.xpath("(//button[contains(@class, 'bg-maingreen')])[1]");
    private final By priceOfProductToCancel= By.xpath("(//p[contains(@class,'digram')]//strong[contains(@class,'textVat')])[1]");
    private final By returnordersbutton=By.xpath("//a[@href='/returnorder']");
    private final By createreturnorderbutton=By.xpath("//i[@class='fa bi bi-plus-lg']");
    private final By selectordertomakereturnorder=By.xpath("(//input[@type='checkbox' and contains(@class,'e-checkbox')])[1]");
    private final By showordertoreturn=By.xpath("(//button[@class='btn btn-view'])[1]");
    private final By selectallproductroreturn=By.xpath("(//input[@name='selectAllIschecked'])[1]");
    private final By savetheReturn=By.xpath("//button[@type='submit' and contains(@class,'btnNav')]");
    private final By returnorderlist=By.xpath("(//a[.//i[contains(@class,'material-icons')]])[4]");
    private final By thepriceofreturnorder=By.xpath("(//td[@class='e-rowcell' and @aria-colindex='3'])[1]");
    private final By homebutton=By.xpath("//*[@id=\"OverLayPin\"]/div/div[1]/div[2]/div[3]/div/nav/ul/li[1]/a/div/i");
    private final By OrderListsButton= By.xpath("//div[contains(@class,'ms-panel-body')]//i[contains(@class,'fa-clipboard-list')]");
    private final By pricebforereturn=By.xpath("//span[i[@class='fas fa-dollar-sign px-2']]");
    private final By returndriver=By.cssSelector("button.btn.custom-btn.btn-success");
    private final By selectreturndriver=By.xpath("//td[@aria-colindex='2']//button[@type='button']");
    private final By closereturndriver=By.xpath("(//div[contains(@class,'modal-content')]//button[@data-dismiss='modal'])[2]");
    private final By cancelassigndriver=By.xpath("//*[@id=\"modal-1\"]/div/div/div/div[1]/button/span");
    private final By takeawayordertype=By.cssSelector("div#v-pills-tab a.nav-link");
    private final By okbuttononordertype=By.xpath("(//div[contains(@class,'modal-footer')]//button[normalize-space()='Ok'])[1]");
    private final By closeorderbutton=By.xpath("(//button[.//i[contains(@class,'fa-times')]])[1]");
    private final By checktocloseorder=By.cssSelector(".modal-content.pos-confirm-close-modal");
    private final By confirmorderbutton=By.cssSelector(".pos-confirm-close-modal .modal-footer button.btn.btn-primary");
    private final By returneditem=By.xpath("//tr[@data-dismiss='modal']/td[3]");
    private final By quantityinput=By.xpath("//input[@id='qty0']");
    private final By  takeawayordertype1=By.xpath("//a[contains(@class,'nav-link') and (     contains(normalize-space(text()),'تيكاوي') or     contains(normalize-space(text()),'تيك اواى') or     contains(translate(normalize-space(text()),         'ABCDEFGHIJKLMNOPQRSTUVWXYZ',         'abcdefghijklmnopqrstuvwxyz'),'takeaway') )]");
    private final By searchinput=By.xpath("//input[@id='searchOrder']");
    private final By volumemodal= By.xpath("//div[@id='modal-Volums' and contains(@class,'show')]");
    private final By ordertypemodal= By.xpath("//div[@id='modal-OrderType']");
    private final By deleveryordertypemodal=By.cssSelector("div.modal.show");
    private final By selectorderlist=By.xpath("(//div[contains(@class,'menu-txt-hld')][.//span[contains(text(),'افتح القائمة')]])[1]");
    private final By employeebuttonselect=By.xpath("(//tbody//tr)[1]//button[contains(.,'اختر') or contains(.,'Select')]");
    private final By captainModal = By.id("modal-Waiter");
    private final By   selectcaptain= By.xpath("//div[@id='modal-Waiter']//tbody//tr[1]//button[contains(.,'اختر') or contains(.,'Select')]");
    private final By expectedReturnTotalLocator = By.cssSelector("div.payment-price");

    private final By selectdriver=By.xpath("//div[contains(@class, 'deliveryCustomer')]//table//tbody//tr[1]//button[contains(., 'Select') or contains(., 'اختر')]");
    private final By drivermodal=By.xpath("//div[contains(@class, 'modal-content') and .//div[contains(@class, 'deliveryCustomer')]]");

    //dynamic locator
    private By productByIndex(int index) {
        return By.xpath("(//div[contains(@class,'product-card')])["+index+"]");
    }
    private By volumeByIndex(int index) {
        return By.xpath("(//button[contains(@class,'volumeSelect')])["+index+"]");
    }
    private By sideDishByIndex(int index) {
        return By.xpath("(//div[@id='modal-NewSideDishes']//li[contains(@class,'liSide')]//button[contains(@class,'btn-success')])["+ index +"]");
    }

    private By getProductCardByName(String productName) {
        // XPath عبقري: يبحث عن كارت المنتج الذي يحتوي بداخله على ديف (productName) يحمل هذا الاسم بالضبط
        // نستخدم normalize-space() لتجاهل أي مسافات زائدة يضعها الـ Angular
        String dynamicXpath = "//div[contains(@class, 'product-card')][.//div[contains(@class, 'productName') and normalize-space(text())='" + productName + "']]";

        return By.xpath(dynamicXpath);
    }
    private By hallByindex(int index) {
        return By.xpath("(//li[contains(@class,'tab')])["+index+"]");
    }
    private final By ordertypenamebyindex =By.xpath("(//*[@id='v-pills-settings-tab'])[1]");
    private String selectedOrderType;
    //Actions
    @Step("Click on Products")
    @Description("Click on Product: {productName} and handle volume and side dishes if popup appears")
    public OrderPage clickOnProduct() throws InterruptedException {
        addProduct(1);
        addProduct(2);
        driver.element().isElementVisible(totalpricebeforesendorder);
        ScreenShotsManager.takeFullPageScreenshot(driver.get(),"After Select Product");
        String productName1 = driver.element().getElementText(productByIndex(1));
        String productName2 = driver.element().getElementText(productByIndex(2));
        Allure.step("Product Name And Price: " + productName1);
        Allure.step("Product Name And Price: " + productName2); 
        
        return this;
    }

    private void addProduct(int index) {
        driver.element().clickElement(productByIndex(index));
        handleVolumeIfShown();
        handleSideDishIfShown();
    }

    private void handleVolumeIfShown() {
        if (driver.element().isElementVisible(volumesPOPUP)) {
            driver.element().clickElement(volumeByIndex(1));
        }
    }

    private void handleSideDishIfShown() {
        if (driver.element().isElementVisible(sidedishPOPUP)) {
            driver.element().clickElement(sideDishByIndex(1));
            driver.element().clickElement(sideDishByIndex(2));
            driver.element().clickElement(sideDishByIndex(2));
            driver.element().clickElement(okbuttonforsidedishmodal);
        }
    }
    @Step("Select Order Type: ")
    public OrderPage selectOrderTypebyindex() {
        if (driver.element().isElementVisible(ordertypes)) {
            if (!driver.element().isElementVisible(ordertypenamebyindex)) {
                Allure.step("Order type element not found at index 1");
                return this;
            }

            String tabName = driver.element().getElementText(ordertypenamebyindex);
            if (tabName == null || tabName.isBlank()) {
                Allure.step("Order type text is null or empty at index 1");
                return this;
            }

            tabName = tabName.trim();
            driver.element().clickElement(ordertypenamebyindex);

            if (tabName.toLowerCase().contains("توصيل") || tabName.toLowerCase().contains("delivery")) {
                driver.element().clickElement(searchbynamefield);
                driver.element().typeText(searchbynamefield, "abdo");
                driver.element().clickElement(selectcustomerbutton);
                driver.element().clickElement(selectaddressbutton);
                driver.element().clickElement(closecustomerselectionmodalbutton);
                Allure.step("Customer selected successfully");
            }

            if (tabName.toLowerCase().contains("صالة") || tabName.toLowerCase().contains("dine")) {
                driver.element().clickElement(hallByindex(1));
                driver.element().clickElement(tablenumber);
                // التحقق من وجود العنصر قبل الحصول على النص
                if (driver.element().isElementVisible(hallByindex(1))) {
                    String hallName = driver.element().getElementText(hallByindex(1));
                    String tableName = driver.element().isElementVisible(tablenumber) ?
                            driver.element().getElementText(tablenumber) : "Table not found";
                    Allure.step("HALL Name:" + hallName + " Table Number: " + tableName);
                } else {
                    Allure.step("Hall element not found");
                }
            }
            if (tabName.toLowerCase().contains("تيك") || tabName.toLowerCase().contains("take")){
                Allure.step("Order Type: " + tabName);
            }
            if (tabName.toLowerCase().contains("موظفين") || tabName.toLowerCase().contains("staff")|| tabName.toLowerCase().contains("employee")) {
                Allure.step("Order Type: Staff Only");
                driver.element().clickElement(employeebutton);
            }
           
            driver.setSelectedOrderType(tabName);
            return this;

        }
        driver.element().clickElement(ordertypebutton);
        if (!driver.element().isElementVisible(ordertypenamebyindex)) {
            Allure.step("Order type element not found at index 1");
            return this;
        }

        String tabName = driver.element().getElementText(ordertypenamebyindex);
        if (tabName == null || tabName.isBlank()) {
            Allure.step("Order type text is null or empty at index 1");
            return this;
        }

        tabName = tabName.trim();
        driver.setSelectedOrderType(tabName);
        Allure.step("Order Type: " + tabName);
        driver.element().clickElement(ordertypenamebyindex);

        if (tabName.toLowerCase().contains("توصيل") || tabName.toLowerCase().contains("delivery")) {
            driver.element().clickElement(searchbynamefield);
            driver.element().typeText(searchbynamefield, "abdo");
            driver.element().clickElement(selectcustomerbutton);
            driver.element().clickElement(selectaddressbutton);
            driver.element().clickElement(closecustomerselectionmodalbutton);
            Allure.step("Customer selected successfully");
        }

        if (tabName.toLowerCase().contains("صالة") || tabName.toLowerCase().contains("dine")) {
            driver.element().clickElement(hallByindex(1));
            driver.element().clickElement(tablenumber);
            // التحقق من وجود العنصر قبل الحصول على النص
            if (driver.element().isElementVisible(hallByindex(1))) {
                String hallName = driver.element().getElementText(hallByindex(1));
                String tableName = driver.element().isElementVisible(tablenumber) ?
                        driver.element().getElementText(tablenumber) : "Table not found";
                Allure.step("HALL Name:" + hallName + " Table Number: " + tableName);
            } else {
                Allure.step("Hall element not found");
            }
        }
        if (tabName.toLowerCase().contains("تيك") || tabName.toLowerCase().contains("take")){
            Allure.step("Order Type: " + tabName);
        }

        if (tabName.toLowerCase().contains("موظفين") || tabName.toLowerCase().contains("staff")|| tabName.toLowerCase().contains("employee")) {
            Allure.step("Order Type: Staff Only");
            driver.element().clickElement(employeebutton);
        }
        return this;
    }

    // داخل كلاس OrderPage
    public OrderPage createReadyOrder() throws InterruptedException {
        Thread.sleep(3000); // Kept your sync wait for stability
        driver.browser().refreshPage();
        Thread.sleep(3000);
        return this.selectRandomOrderType()
                .get_All_Product_From_DB()
                .searchRandomDBProductInUI()
                .selectSearchedProduct()
                .validateOrderIsSentSuccessfully();
    }

    @Step("Execute Full Return Order Process for Document ID: {documentId}")
    public OrderPage makeAReturnOrder(String documentId) throws InterruptedException {

        String orderType = driver.getSelectedOrderType();
        Allure.step("Initiating return for DocID: " + documentId + " | Type: " + orderType);

        // 1. Handle Navigation and Modals
        ensureReturnOrderScreenIsActive();

        // 2. Locate and Select Order
        selectOrderFromGrid(documentId);

        // 3. Product Selection
        driver.element().clickElement(showordertoreturn);
        driver.element().clickElement(selectallproductroreturn);
        String returnedItemInfo = waitForNonEmptyText(returneditem);

        // 4. Calculate Expected Value (Refactored logic)
        double uiTotal = safeParseDouble(waitForNonEmptyText(expectedReturnTotalLocator), "UI Total");
        double expectedReturnAmount = calculateExpectedReturnAmount(uiTotal, orderType);

        // 5. Save and Validate
        driver.element().clickElement(savetheReturn);
        driver.element().clickElement(returnorderlist);
        driver.browser().refreshPage();

        new WebDriverWait(driver.get(), Duration.ofSeconds(10))
                .until(ExpectedConditions.presenceOfElementLocated(thepriceofreturnorder));

        double actualReturnPrice = Math.round(safeParseDouble(waitForNonEmptyText(thepriceofreturnorder), "Actual Return Price") * 100.0) / 100.0;

        // 6. Final Assertion
        Assert.assertEquals(actualReturnPrice, expectedReturnAmount, 0.01, String.format(
                "❌ Return Calculation Mismatch! Expected: %.2f | Actual: %.2f | Items: %s",
                expectedReturnAmount, actualReturnPrice, returnedItemInfo));

        LogsManager.info("✅ Full Return Successful. Final Amount: " + actualReturnPrice);
        return new OrderPage(driver);
    }

    // Helper Method: Handles the calculation logic in one place
    private double calculateExpectedReturnAmount(double uiTotal, String orderType) {
        boolean isDineIn = orderType != null &&
                (orderType.toLowerCase().contains("dine") ||
                        orderType.contains("صالة") ||
                        orderType.contains("صاله"));

        double amount = uiTotal;
        if (isDineIn) {
            // Net = UI_Total / 1.26 | Amount_Without_Service = Net * 1.14
            double net = uiTotal / 1.26;
            amount = net * 1.14;
            LogsManager.info("Dine-In order: Excluding 12% service charge from total.");
        }
        return Math.round(amount * 100.0) / 100.0;
    }
    @Step("Select Order from Grid using Serial/Document ID: {identifier}")
    private void selectOrderFromGrid(String identifier) {
        // نستخدم الـ identifier كمعيار بحث فريد في الجدول
        By targetRow = By.xpath("//tr[td[normalize-space()='" + identifier + "']]");
        By nextButton = By.cssSelector("div.e-next.e-icon-next.e-nextpage");
        JavascriptExecutor js = (JavascriptExecutor) driver.get();
        boolean isOrderFound = false;

        // Loop through pages
        for (int page = 0; page < 10; page++) {
            List<WebElement> matchingRows = driver.get().findElements(targetRow);

            if (!matchingRows.isEmpty()) {
                WebElement rowToClick = matchingRows.get(0);

                // Clicking the checkbox/row selection
                try {
                    WebElement checkbox = rowToClick.findElement(By.xpath(".//input[@type='checkbox']"));
                    js.executeScript("arguments[0].click();", checkbox);
                } catch (Exception e) {
                    js.executeScript("arguments[0].click();", rowToClick.findElement(By.xpath("./td[1]")));
                }

                isOrderFound = true;
                Allure.step("✅ Found and selected Order with Identifier: " + identifier + " on page " + (page + 1));
                break;
            }

            // Move to next page if possible
            List<WebElement> nextBtnList = driver.get().findElements(nextButton);
            if (!nextBtnList.isEmpty() && !nextBtnList.get(0).getAttribute("class").contains("e-disabled")) {
                js.executeScript("arguments[0].click();", nextBtnList.get(0));
                try { Thread.sleep(1500); } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
            } else {
                break; // Last page reached
            }
        }

        if (!isOrderFound) {
            ScreenShotsManager.takeFullPageScreenshot(driver.get(), "Order_Not_Found_In_Grid");
            Assert.fail("❌ Critical Failure: Identifier [" + identifier + "] was NOT found in the grid!");
        }
    }

    // Helper Method: Logic to reach the return screen
    private void ensureReturnOrderScreenIsActive() {
        if (isOrderTypePopupOpen()) driver.element().clickElement(okbuttononordertype);
        if (!driver.element().isElementVisible(returnordersbutton)) {
            driver.element().clickElement(homebutton);
            driver.element().clickElement(OrderListsButton);
        }
        driver.element().clickElement(returnordersbutton);

        WebElement returnBtn = new WebDriverWait(driver.get(), Duration.ofSeconds(10))
                .until(ExpectedConditions.elementToBeClickable(createreturnorderbutton));
        ((JavascriptExecutor) driver.get()).executeScript("arguments[0].click();", returnBtn);
    }

    private String waitForNonEmptyText(By locator) {
        WebDriverWait wait = new WebDriverWait(driver.get(), Duration.ofSeconds(10));

        return wait.until(d -> {
            try {
                String text = d.findElement(locator).getText();
                System.out.println("DEBUG TEXT: [" + text + "]");
                return (text != null && !text.trim().isEmpty()) ? text : null;
            } catch (Exception e) {
                return null;
            }
        });
    }
    private double safeParseDouble(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            throw new RuntimeException("❌ " + fieldName + " is empty or null");
        }

        try {
            // 🔥 السطر السحري: إزالة الفواصل (الخاصة بالآلاف) وأي مسافات قبل التحويل
            String cleanValue = value.replace(",", "").trim();

            return Double.parseDouble(cleanValue);

        } catch (NumberFormatException e) {
            throw new RuntimeException("❌ Invalid number format in " + fieldName + ": " + value);
        }
    }

    @Step("Create New Order for {numberOfPeople} people")
    public OrderPage createNewOrder(int numberOfPeople) {
        driver.element().clickElement(newOrderButton);
        driver.element().clickElement(numberofpeopleontable);
        driver.element().typeText(personscountbar, String.valueOf(numberOfPeople));
        driver.element().clickElement(okbuttononpersonscountbar);
        return this;
    }
    @Step("Select Order to Pay")
    public PaymentPage selectOrderToPay() throws InterruptedException {
        
        driver.element().clickElement(selectorderbutton);
        
        if (driver.element().isElementVisible(ordertypes)) {
            driver.element().clickElement(clickOk);
        }
        
        String tabName = driver.element().getElementText(ordertypebutton).toLowerCase().trim();
        
        boolean isDeliveryOrder = (tabName.contains("توصيل") && !tabName.contains("بدون")) || tabName.contains("delivery");
    
        if (isDeliveryOrder) {
            Allure.step("Processing payment flow for Delivery Order");
            
            driver.element().clickElement(cashieroperationbutton);
            driver.element().clickElement(followorderbutton);
            driver.element().clickElement(returndriver);
            
            if (driver.element().isElementVisible(selectreturndriver)) {
                driver.element().clickElement(selectreturndriver);
            } else {
                driver.element().clickElement(closereturndriver);
            }
            
            driver.element().clickElement(checklorderbutton);
            driver.element().clickElement(assignbutton);
            driver.element().clickElement(assigntodriverbutton);
            
            if (driver.element().isElementVisible(cancelprintbutton)) {
                driver.element().clickElement(cancelprintbutton); 
            }
            if (driver.element().isElementVisible(cancelassigndriver)) {
                driver.element().clickElement(cancelassigndriver);
            }
            
            driver.element().clickElement(paytheordersbutton);
            driver.element().clickElement(selcetordertopaybutton);
            driver.element().clickElement(paybutton);
            
            return new PaymentPage(driver);
        }
    
        Allure.step("Processing payment flow for Non-Delivery Order");
        
        if (driver.element().isElementVisible(ordertypes)) {
            driver.element().clickElement(clickOk);
        }
    
        driver.element().clickElement(payementbutton);
        return new PaymentPage(driver);
    }


    @Step("Select Order To Change Order Type")
    public OrderPage selectOrderToChangeOrderType() {
        driver.element().clickElement(selectorderlist);
        driver.element().clickElement(selectorderbutton);
        return this;
    }

    @Step("Cancel Order")
    public HomePage cancelOrder() throws InterruptedException {
        driver.element().clickElement(sendorderbutton);
        if (driver.element().isElementVisible(okbuttononordertype)) {
            driver.get().switchTo().activeElement().sendKeys(Keys.ESCAPE);

        }
        driver.element().clickElement(opensentordersbutton);
        Thread.sleep(2000);
        driver.element().clickElement(selectorderbutton);
        Thread.sleep(2000);
        driver.element().clickElementByJS(cancelproductsbutton);
        driver.element().clickElement(checkproducttocancel);
        driver.element().clickElement(sendorderaftercancelproduct);

        driver.element().clickElement(customerresoncancelbutton);
        this.exactCancelTime = LocalDateTime.now();
        if (driver.element().isElementVisible(okbuttononordertype)) {
            driver.get().switchTo().activeElement().sendKeys(Keys.ESCAPE);

        }
        driver.element().clickElement(homebutton);
        Thread.sleep(2000);

        return new HomePage(driver);
    }

    public LocalDateTime getExactCancelTime() {
        return this.exactCancelTime;
    }

    @Step("Select first takeaway (تيك اواي / سفري / takeaway)")
    public OrderPage makeTakeAwayOrder() throws InterruptedException {

        // افتح نافذة اختيار النوع
        Thread.sleep(3000);
        if (!isOrderTypePopupOpen()) {
            driver.element().clickElement(ordertypebutton);
        }



        if (!driver.element().isElementVisible(takeawayordertype)) {
            ScreenShotsManager.takeFullPageScreenshot(driver.get(), "order_types_popup_not_visible");
            throw new AssertionError("❌ Order types popup is not visible");
        }

        var types = driver.get().findElements(takeawayordertype);

        for (var el : types) {
            String text = el.getText().trim().toLowerCase();

            if (text.contains("takeaway") || text.contains("take")
                    || text.contains("تيك") || text.contains("تيك اواي")
                    || text.contains("تيكاوي")
                    || text.contains("سفري") || text.contains("سفرى")) {

                Allure.step("✅ Selecting Order Type: " + el.getText().trim());
                el.click();

                driver.setSelectedOrderType(el.getText().trim());

                return this;
            }
        }

        ScreenShotsManager.takeFullPageScreenshot(driver.get(), "takeaway_not_found");
        throw new AssertionError("❌ No takeaway order type found (تيك اواي/سفري/takeaway)");
    }

    private boolean isVisibleAndEnabled(By locator) {
        try {
            if (!driver.element().isElementVisible(locator)) return false;
            return driver.get().findElement(locator).isEnabled();
        } catch (Exception e) {
            return false;
        }
    }
    private boolean isClickable(By locator) {
        try {
            new WebDriverWait(driver.get(), Duration.ofSeconds(5))
                    .until(ExpectedConditions.elementToBeClickable(locator));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isOrderTypePopupOpen() {
        try {
            WebElement popup = driver.get().findElement(By.id("modal-OrderType"));
            return popup.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
    private boolean isDileveryOrderTypePopupOpen() {
        try {
            WebElement popup = driver.get().findElement(By.xpath("//input[contains(@placeholder, 'Customer Balance') or contains(@placeholder, 'رصيد العميل')]"));
            return popup.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
    private boolean isordertypeisdinein() {
        try {
            WebElement popup = driver.get().findElement(By.xpath("(//div[contains(@class, 'pull-left') and contains(text(), 'table1')])[1]"));
            return popup.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
private boolean isordertypeforemployee() {
        try {
            WebElement popup = driver.get().findElement(By.xpath("//perfect-scrollbar[.//th[contains(., 'الموظف') or contains(., 'Employee')]]"));
            return popup.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }


    @Step("Change Order Type After Send Order")
    public OrderPage changeordertypeaftersendorder() throws InterruptedException {

        // 1. Handle initial selection
        if (!isOrderTypePopupOpen()) {
            driver.element().clickElement(ordertypebutton);
        }
        selectRandomOrderType();

        // 2. Wait for UI transition (Implicit sync)
        // Add a wait here to ensure the popup is fully loaded based on selection

        // 3. Handle Delivery Logic
        if (isDileveryOrderTypePopupOpen()) {
            driver.element().typeText(searchbyphonefield, "0111");
            driver.element().clickElement(selectcustomerbutton);

            // Wait for address modal visibility explicitly
            if (driver.element().isElementVisible(selectaddressbutton)) {
                driver.element().clickElement(selectaddressbutton);
            }
            driver.element().clickElement(closecustomerselectionmodalbutton);
        }
        // 4. Handle Dine-in (Using else if to prevent overlapping execution)
        else if (isordertypeisdinein()) {
            selectRandomFreeTable();
            Allure.step("✅ Table selected successfully");
        }
        // 5. Handle Employee
        else if (isordertypeforemployee()) {
            driver.element().clickElement(employeebuttonselect);
            Allure.step("✅ Employee selected successfully");
        }

        Allure.step("✅ Order type changed successfully");
        return this;
    }

    @Step("Go to Payment for Takeaway Order")
    public PaymentPage goToPaymentForTakeawayOrder() {
        driver.element().clickElement(payementbutton);
        return new PaymentPage(driver);
    }


    public class ProductAPI {

        public String getFirstComboProductName() {
            Response response = RestAssured.given()
                    .baseUri(PropertyReader.getProperty("baseURLapi"))
                    .get("/products/preview");

            List<Map<String, Object>> products = response.jsonPath().getList("products");

            return products.stream()
                    .filter(p -> Boolean.TRUE.equals(p.get("isCombo")))
                    .map(p -> p.get("name").toString())
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("No combo product found"));
        }
    }


    @Step("Get product quantity by name from order page")
    public int getProductQuantityByName(String productName) throws InterruptedException {
        Thread.sleep(3000);
        driver.element().typeText(searchinput, productName);
        By productQtyLocator = By.xpath(
                "//div[contains(@class,'product-card')][.//div[contains(@class,'productName') and normalize-space()='" + productName + "']]//*[contains(@class,'productAvalQty')]"
        );

        String quantityText = driver.element().getElementText(productQtyLocator);
        System.out.println("Raw quantity text for product [" + productName + "] = [" + quantityText + "]");

        if (quantityText == null || quantityText.trim().isEmpty()) {
            throw new AssertionError("Available quantity is empty for product: " + productName);
        }

        quantityText = quantityText.replaceAll("[^0-9]", "");

        if (quantityText.isEmpty()) {
            throw new AssertionError("No numeric quantity found for product: " + productName);
        }

        return Integer.parseInt(quantityText);
    }
    @Step("Get All Products From API")
    public OrderPage get_All_Product_From_API() {
        UserManagmentAPI api = new UserManagmentAPI(driver);
        
        // 🚨 السطر القادم هو سبب المشكلة، تأكد أنه مكتوب هكذا بالضبط (بدون كلمة List في البداية)
        // استخدم this.apiProductsList مباشرة!
        this.apiProductsList = api.getAllProductsWithPrices(); 
        
        Allure.step("✅ Successfully fetched " + this.apiProductsList.size() + " products from API");
        return this;
    }

    @Step("Select a Random Employee from the Modal")
    public OrderPage selectRandomEmployee() {

        // 1. محدد (Locator) لجلب كل الصفوف التي تحتوي على زر اختيار (يدعم اللغتين عربي/إنجليزي)
        By allEmployeeRows = By.xpath("//table//tbody//tr[.//button[contains(.,'اختر') or contains(.,'Select')]]");

        // 2. معرفة عدد الموظفين المتاحين في الشاشة
        int totalEmployees = driver.get().findElements(allEmployeeRows).size();

        // Guard Clause: حماية التست من الانهيار إذا كانت القائمة فارغة
        if (totalEmployees == 0) {
            Assert.fail("No employees found to select in the modal!");
        }

        // 3. توليد رقم عشوائي (من 1 إلى إجمالي العدد) لأن XPath يبدأ العد من 1
        Random random = new Random();
        int randomIndex = random.nextInt(totalEmployees) + 1;

        // 4. بناء محددات (Locators) ديناميكية للصف العشوائي الذي تم اختياره
        By randomEmployeeNameLocator = By.xpath("(//table//tbody//tr[.//button[contains(.,'اختر') or contains(.,'Select')]])[" + randomIndex + "]//th");
        By randomEmployeeSelectButton = By.xpath("(//table//tbody//tr[.//button[contains(.,'اختر') or contains(.,'Select')]])[" + randomIndex + "]//button[contains(.,'اختر') or contains(.,'Select')]");

        // 5. استخراج اسم الموظف للتوثيق
        String employeeName = driver.element().getElementText(randomEmployeeNameLocator).trim();

        // 6. النقر على زر الاختيار الخاص بهذا الموظف العشوائي
        driver.element().clickElement(randomEmployeeSelectButton);

        // 7. توثيق الخطوة بنجاح في Allure
        Allure.step("Randomly selected employee: " + employeeName);

        return this;
    }


    @Step("Get all Order Type From Api")
    public OrderPage get_all_Order_Type_From_API(){
         UserManagmentAPI userManagmentAPI= new UserManagmentAPI(driver);
         this.apiordertypelist = userManagmentAPI.getAllOrderTypes();
        Allure.step("✅ Successfully fetched " + apiordertypelist.size() + " order types from API");
        return this;
    }
    @Step("Select A Random Order Type")
    public OrderPage selectRandomOrderType() throws InterruptedException {
        // 1. تأكد من جلب البيانات أولاً إذا كانت القائمة فارغة
        if (this.apiordertypelist == null || this.apiordertypelist.isEmpty()) {
            Allure.step("🔄 القائمة فارغة، يتم جلب أنواع الطلبات من الـ API الآن...");

            UserManagmentAPI api = new UserManagmentAPI(driver);
            this.apiordertypelist = api.getAllOrderTypes(); // استدعاء الدالة التي كتبناها في الكلاس الآخر
        }

        // 2. التحقق بعد المحاولة
        Assert.assertNotNull(apiordertypelist, "🚨 فشل جلب البيانات من الـ API، الكائن ما زال Null!");
        Assert.assertTrue(apiordertypelist.size() > 0, "🚨 القائمة المستلمة من الـ API فارغة!");

        Random rand = new Random();
        String randomTypeName = apiordertypelist.get(rand.nextInt(apiordertypelist.size()));

        // 3. بناء الـ Locator
        By randomOrderTypeLocator = By.xpath("//div[@id='modal-OrderType']//a[contains(normalize-space(text()), '" + randomTypeName + "')]");
        this.currentordertype = randomOrderTypeLocator;

        // فتح الـ Modal إذا كان مغلقاً
        if (!isOrderTypePopupOpen()) {
            driver.element().clickElement(ordertypebutton);
        }

        Thread.sleep(2000);
        driver.element().clickElement(randomOrderTypeLocator);
        Thread.sleep(2000);
        if (isDileveryOrderTypePopupOpen()) {
            driver.element().clickElement(searchbyphonefield);
            driver.element().typeText(searchbyphonefield, "0111");
            driver.element().clickElement(selectcustomerbutton);
            
            if (driver.element().isElementVisible(selectaddressbutton)) {
                driver.element().clickElement(selectaddressbutton);
            }
            driver.element().clickElement(closecustomerselectionmodalbutton);
        }
        if (isordertypeisdinein()) {
            selectRandomFreeTable();
            Allure.step("✅ Table selected successfully");
        }
        if (isordertypeforemployee()) {
            selectRandomEmployee();
            Allure.step("✅ Employee selected successfully");
        }
        
        Allure.step("✅ Selected Order Type: " + randomTypeName);
        driver.setSelectedOrderType(randomTypeName);

        return this;
    }


    @Step("Select a Random Free Table")
    public OrderPage selectRandomFreeTable() {

        // 1. تحديد الطاولات الفارغة
        By freeTablesLocator = By.cssSelector("ul.firstUl li.h:not(.TableBusy)");
        List<WebElement> freeTables = driver.get().findElements(freeTablesLocator);

        // 2. Guard Clause
        if (freeTables.isEmpty()) {
            Assert.fail("❌ No FREE tables found on the screen! All tables might be busy.");
        }

        // 3. اختيار طاولة عشوائية
        Random random = new Random();
        int randomIndex = random.nextInt(freeTables.size());
        WebElement randomFreeTable = freeTables.get(randomIndex);

        // 4. استخراج اسم الطاولة
        String tableName = randomFreeTable.findElement(By.className("pull-left")).getText().trim();
        Allure.step("✅ Random FREE table selected successfully: " + tableName);

        // 🔥 5. الحل السحري: النقر باستخدام الجافاسكريبت لتخطي خطأ Element not interactable
        JavascriptExecutor js = (JavascriptExecutor) driver.get();
        js.executeScript("arguments[0].click();", randomFreeTable);

        return this;
    }

    @Step("Search for a random product (Price > 0) from DB in UI with fallback for stopped products")
    public OrderPage searchRandomDBProductInUI() {
        if (isDileveryOrderTypePopupOpen()) {
            driver.element().clickElement(searchbyphonefield);
            driver.element().typeText(searchbyphonefield, "0111");
            driver.element().clickElement(selectcustomerbutton);

            if (driver.element().isElementVisible(selectaddressbutton)) {
                driver.element().clickElement(selectaddressbutton);
            }
            driver.element().clickElement(closecustomerselectionmodalbutton);
        }

        // 1. التأكد من أن البيانات تم جلبها من الDB
        Assert.assertNotNull(this.apiProductsList, "🚨 قاعدة البيانات لم تقم بجلب المنتجات بعد!");

        // 🔥 إضافة طبقة تشخيصية (Diagnostic Logs) لنرى ماذا أحضرنا من الـ DB قبل الفلترة
        System.out.println("📦 Total products fetched from DB: " + this.apiProductsList.size());
        if(this.apiProductsList.size() > 0) {
            System.out.println("🔍 Sample of first product: Name=" + this.apiProductsList.get(0).name + " | Price=" + this.apiProductsList.get(0).basePrice);
        }

        // 2. فلترة المنتجات الصالحة
        List<ProductData> validProducts = new ArrayList<>(this.apiProductsList.stream()
                .filter(product -> product.basePrice != null && product.basePrice > 0.0)
                .collect(Collectors.toList()));

        // طباعة عدد المنتجات الصالحة بعد الفلتر
        Allure.step("📊 Active products with price > 0 : " + validProducts.size() + " out of " + this.apiProductsList.size());

        Assert.assertTrue(validProducts.size() > 0, "🚨 لا يوجد أي منتج في قاعدة البيانات سعره أكبر من صفر! يرجى مراجعة الـ API Mapping أو بيانات قاعدة البيانات.");

        Random rand = new Random();
        boolean productFoundOnUI = false;

        // 💡 الـ Loop السحرية
        while (!validProducts.isEmpty()) {

            int randomIndex = rand.nextInt(validProducts.size());
            ProductData randomProduct = validProducts.get(randomIndex);
            String productName = randomProduct.name;

            // 3. تنظيف حقل البحث أولاً
            try {
                driver.get().findElement(searchinput).clear();
            } catch (Exception e) {
                driver.element().typeText(searchinput, "");
            }

            // 4. كتابة اسم المنتج المراد اختباره
            driver.element().clickElement(searchinput);
            driver.element().typeText(searchinput, productName);

            try { Thread.sleep(1000); } catch (InterruptedException e) { e.printStackTrace(); }

            // 5. توليد محدد الكارت والتحقق من وجوده
            By targetProductCard = getProductCardByName(productName);
            boolean isDisplayed = !driver.get().findElements(targetProductCard).isEmpty();

            if (isDisplayed) {
                this.currentSearchedProduct = productName;
                productFoundOnUI = true;
                Allure.step("✅ Successfully found active product on UI: " + productName + " | Base Price: " + randomProduct.basePrice);
                break;
            } else {
                System.out.println("⚠️ This Product Is Stopped: " + productName + " -> Fetching another product from DB...");
                validProducts.remove(randomIndex);
            }
        }

        Assert.assertTrue(productFoundOnUI, "🚨 تم تجربة جميع المنتجات النشطة في قاعدة البيانات ولم يظهر أي منها في البحث على الشاشة!");

        return this;
    }

    @Step("Search for a random product (Price > 0) from API in UI")
    public OrderPage searchRandomAPIProductInUI() {
        Assert.assertNotNull(apiProductsList, "🚨 الـ API لم يقم بجلب المنتجات بعد!");
        Assert.assertTrue(apiProductsList.size() > 0, "🚨 القائمة الأصلية في الـ API فارغة!");
    
        // 1. فلترة المنتجات: 💡 استخدمنا basePrice بدلاً من price
        List<ProductData> validProducts = apiProductsList.stream()
                .filter(product -> product.basePrice != null && product.basePrice > 0.0)
                .collect(Collectors.toList());
    
        // 2. التأكد من وجود منتجات بعد الفلترة لتجنب انهيار التست
        Assert.assertTrue(validProducts.size() > 0, "🚨 لا يوجد أي منتج سعره الأساسي أكبر من صفر!");
    
        // 3. اختيار منتج عشوائي من القائمة المفلترة (الصالحة)
        Random rand = new Random();
        ProductData randomProduct = validProducts.get(rand.nextInt(validProducts.size()));
    
        // 4. حفظ اسم المنتج 
        this.currentSearchedProduct = randomProduct.name;
        
        // (اختياري) إذا كنت تحتفظ بالسعر في متغير بالكلاس، استخدم basePrice هكذا:
        // this.currentProductPrice = randomProduct.basePrice;
    
        // 5. كتابة الاسم في حقل البحث
        driver.element().typeText(searchinput, this.currentSearchedProduct);
    
        // توثيق الخطوة في تقرير Allure مع ذكر السعر
        Allure.step("✅ Searched for product: " + this.currentSearchedProduct + " | Base Price: " + randomProduct.basePrice);
        
        return this;
    }

    @Step("Select the dynamically searched product from the screen")
    public OrderPage selectSearchedProduct() {

        // التأكد من أننا قمنا بالبحث عن منتج أولاً
        Assert.assertNotNull(this.currentSearchedProduct, "🚨 يجب استدعاء دالة البحث أولاً قبل محاولة النقر!");
        ScreenShotsManager.takeFullPageScreenshot(driver.get(), "After Searching Product");

        // استدعاء دالة النقر الأساسية مع تمرير الاسم الذي تم حفظه
        return selectSpecificProduct(this.currentSearchedProduct);
    }

    @Step("Select specific product from the screen: {productName}")
    public OrderPage selectSpecificProduct(String productName) {

        // توليد المحدد الخاص بهذا المنتج
        By targetProductCard = getProductCardByName(productName);

        // الانتظار حتى يظهر الكارت على الشاشة
        driver.element().isElementVisible(targetProductCard); // (نصيحة: يفضل استخدام Wait هنا كما اتفقنا سابقاً)

        // النقر على الكارت
        driver.element().clickElement(targetProductCard);

        // التعامل مع البوب أب
        handleVolumeIfShown();
        handleSideDishIfShown();

        Allure.step("✅ Successfully clicked on product: " + productName);

        return this;
    }
    @Step("Send Order")
    public OrderPage sendOrder() {
        driver.element().clickElement(sendorderbutton);
        return this;
    }
    //validation
    @Step("Validate that order is sent successfully")
    public OrderPage validateOrderIsSentSuccessfully() throws InterruptedException {
        ScreenShotsManager.takeFullPageScreenshot(driver.get(), "Before Sending Order");
        String priceBeforeSendOrder = driver.element().getElementText(totalpricebeforesendorder);
        Thread.sleep(2000);
        driver.element().clickElement(sendorderbutton);
        if(driver.element().isElementVisible(drivermodal)){
            driver.element().clickElement(selectdriver);
            driver.element().clickElement(sendorderbutton);

        }

        if(driver.element().isElementVisible(captainModal)){
            driver.element().clickElement(selectcaptain);
        }
        if (driver.element().isElementVisible(ordertypes)){

            driver.element().clickElement(clickOk);
        }
        driver.element().clickElement(opensentordersbutton);
        driver.element().isElementVisible(totalpriceaftersendorder);
        ScreenShotsManager.takeFullPageScreenshot(driver.get(), "After Sending Order");
        String priceAfterSendOrder = driver.element().getElementText(totalpriceaftersendorder);
        
        if (!priceBeforeSendOrder.equals(priceAfterSendOrder)) {
            throw new AssertionError("Order price mismatch: before sending order: " + priceBeforeSendOrder + ", after sending order: " + priceAfterSendOrder);
        }
        return this;
    }


    @Step("Close Order and Catch Serial from Request Payload")
    public String closeOrderAndCatchSerial() {

        // 1. استخراج الـ Driver في متغير محلي لتفادي مشاكل الـ ThreadLocal مع الـ Async Listener
        WebDriver localDriver = driver.get();
        DevTools devTools = ((HasDevTools) localDriver).getDevTools();
        devTools.createSession();
        devTools.send(Network.enable(Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty()));

        // 2. استخدام AtomicReference لأنه آمن (Thread-Safe) للتعديل من داخل الـ Lambda
        AtomicReference<String> caughtSerial = new AtomicReference<>(null);

        devTools.addListener(Network.requestWillBeSent(), request -> {
            String url = request.getRequest().getUrl().toLowerCase();

            // 3. التحقق من الرابط المستهدف
            if (url.contains("closeorder") || url.contains("updatepaidorderasync") || url.contains("updateorder")) {

                LogsManager.info("🌐 [Network Sniffer] Intercepted Target URL: " + url);

                Optional<String> postData = request.getRequest().getPostData();

                if (postData.isPresent()) {
                    String payload = postData.get();
                    LogsManager.info("📦 [Network Sniffer] Raw Payload: " + payload);

                    // 4. Regex محسّن: يدعم كلمة Serial أو SerialNumber، ويلتقط القيمة سواء كانت نص "123" أو رقم 123
                    Pattern pattern = Pattern.compile("(?i)\"?(?:serial|serialnumber)\"?\\s*:\\s*\"?([^\",}\\s]+)\"?");
                    Matcher matcher = pattern.matcher(payload);

                    if (matcher.find()) {
                        caughtSerial.set(matcher.group(1)); // حفظ القيمة بأمان
                        LogsManager.info("🎯 [Network Sniffer] SUCCESS! Caught Serial: " + caughtSerial.get());
                    } else {
                        LogsManager.error("⚠️ Payload caught, but Regex couldn't find the Serial!");
                    }
                } else {
                    LogsManager.error("⚠️ Request caught, but PostData (Body) is empty!");
                }
            }
        });

        try {
            // 5. الضغط على الزر (يجب أن يحدث "بعد" تفعيل الـ Listener)
            driver.element().clickElementRaw(closeorderbutton);

            // 6. الانتظار حتى تتغير قيمة الـ AtomicReference
            new WebDriverWait(localDriver, Duration.ofSeconds(10))
                    .until(d -> caughtSerial.get() != null);

            return caughtSerial.get();

        } catch (Exception e) {
            throw new RuntimeException("❌ Timeout: Did not catch the Serial within 10 seconds! Check logs to see if the request was actually sent.");
        } finally {
            // 7. إغلاق الـ Sniffer فور الانتهاء لتنظيف الذاكرة
            devTools.send(Network.disable());
            devTools.close();
        }
    }
    @Step("Extract Latest Order Number from Orders Grid")
    public String getLatestOrderNumber() throws InterruptedException {

        By latestOrderNumberLocator = By.xpath("(//tbody//tr[1]//th[@scope='row'])[1]");
        String rawText = "";

        for (int i = 0; i < 10; i++) {
            if (!driver.get().findElements(latestOrderNumberLocator).isEmpty()) {
                rawText = driver.element().getElementText(latestOrderNumberLocator).trim();
                if (!rawText.isEmpty()) break;
            }
            Thread.sleep(500);
        }

        if (rawText.isEmpty()) {
            Assert.fail("❌ لم يتمكن السيلينيوم من قراءة رقم الطلب من القائمة!");
        }

        String cleanOrderNumber = rawText.replaceAll("[^0-9]", "");
        Allure.step("✅ Extracted Latest Order Number cleanly: " + cleanOrderNumber);

        return cleanOrderNumber;
    }

    @Step("Validate available quantity matches daily stock")
    public OrderPage validateAvailableQuantityMatchesDailyStock(String productName, int expectedQuantity) throws InterruptedException {
        Thread.sleep(3000);
        if(isOrderTypePopupOpen())
        {
            Thread.sleep(2000);
            driver.element().clickElement(takeawayordertype1);
        }

        int actualQuantity = getProductQuantityByName(productName);

        Assert.assertEquals(
                actualQuantity,
                expectedQuantity,
                "Quantity mismatch for product: " + productName
        );
        LogsManager.info("Actual Quantity ="+ actualQuantity ,"Expected Quantity ="+ expectedQuantity);
        Allure.step("✅ Available quantity matches daily stock for product: " + productName + " (Actual: " + actualQuantity + ", Expected: " + expectedQuantity + ")");

        return this;
    }
    @Step("Create Order With A Product is Out Of Stock")
    public OrderPage
    createOrderWithProductIsOutOfStock(String productName, int expectedQuantity) throws InterruptedException {
        Thread.sleep(3000);
        if (isOrderTypePopupOpen()){
            driver.element().clickElement(takeawayordertype1);
        }
        driver.element().typeText(searchinput, productName);


        driver.element().clickElement(By.xpath("//div[contains(@class,'productName') and normalize-space(text())='" + productName + "']"));
        if (driver.element().isElementVisible(volumeByIndex(1))) {
            driver.element().clickElement(volumeByIndex(1));
        }
        if (driver.element().isElementVisible(sidedishPOPUP)){
            driver.element().clickElement(sidedishitem);
            driver.element().clickElement(By.xpath("//div[contains(@class,'modal-NewSideDishes')]//button[contains(@class,'btn-primary') and contains(@class,'btnEdit')]"));
        }
        int actualQuantity= Integer.parseInt(driver.element().getElementText(By.xpath("//div[contains(@class,'productName') and normalize-space(text())='" + productName + "'] /ancestor::div[contains(@class,'product-card')] //div[contains(@class,'productAvalQty')]//span")));

        Allure.step("✅ Available quantity for product: " + productName + " is: " + actualQuantity);


        int quantityToOrder = actualQuantity+1;
        driver.element().clickElement(quantityinput);
        driver.element().typeText(quantityinput, String.valueOf(quantityToOrder));
        driver.element().clickElement(payementbutton);
        driver.element().clickElementRaw(closeorderbutton);
        return this;
    }



    @Step("Validate Toast Message Contains: {expectedText}")
    public OrderPage validateToastContains(String expectedText) {

        // 1. الدالة المركزية تقوم بكل العمل الشاق: (Hover + قراءة + تدمير)
        String actualText = driver.element().getToastMessageAndDestroyIt();

        // 2. التحقق من النتيجة (Assertion)
        if (!actualText.contains(expectedText)) {
            throw new AssertionError(
                    "❌ Expected: [" + expectedText + "] but found: [" + actualText + "]"
            );
        }

        Allure.step("✅ Toast validated successfully: " + actualText);

        return this;
    }


    @Step("Get All Products with Volumes directly from API")
public OrderPage get_All_Product_From_API_With_Volumes_From_API() {
    UserManagmentAPI api = new UserManagmentAPI(driver);
    
    // 1. جلب المنتجات وأحجامها من الـ API مباشرة وتخزينها في متغير الكلاس
    this.apiProductsList = api.getAllProductsWithVolumesFromAPI();
    
    // 2. التحقق من أن القائمة ليست فارغة
    Assert.assertTrue(this.apiProductsList.size() > 0, "🚨 فشل في تكوين قائمة المنتجات المدمجة!");
    
    return this;
}
@Step("Get All Products directly from Database")
public OrderPage get_All_Product_From_DB() {
    
    // سطر واحد فقط يجلب لك كل شيء (أسماء + أسعار + أحجام) من الداتا بيز!
    this.apiProductsList = DataBaseReader.getCompleteProductsFromDB();
    
    Assert.assertTrue(this.apiProductsList.size() > 0, "🚨 فشل في تكوين قائمة المنتجات من قاعدة البيانات!");
    
    return this;
}



}



