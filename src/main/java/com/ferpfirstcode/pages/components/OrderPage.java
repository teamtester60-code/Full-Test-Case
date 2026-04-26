package com.ferpfirstcode.pages.components;

import com.ferpfirstcode.apis.UserManagmentAPI;
import com.ferpfirstcode.driver.GUIDriver;
import com.ferpfirstcode.media.ScreenShotsManager;
import com.ferpfirstcode.utils.dataReader.PropertyReader;
import com.ferpfirstcode.utils.logs.LogsManager;

import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Random;


public class OrderPage {
    private final GUIDriver driver;
    private List<String> apiProductsList;
    private String currentSearchedProduct;
    private LocalDateTime exactCancelTime;

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
    private final By selectcustomerbutton= By.cssSelector("button.btnPLus.btn-link");
    private final By selectaddressbutton= By.xpath("(//*[@id=\"collapseOne_@i\"]/td[5]/button)[1]");
    private final By closecustomerselectionmodalbutton= By.xpath("//*[@id=\"nav-home\"]/div/div[2]/div[2]/div/button");
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
    private final By paybutton= By.cssSelector("#coact-tab");
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

    @Step("Make A Return Order (Full Return)")
    public OrderPage makeAReturnOrder() throws InterruptedException {
        String orderType = driver.getSelectedOrderType();

        if (orderType == null || orderType.isBlank()) {
            throw new IllegalStateException("Order type is null. Make sure selectOrderTypebyindex() was executed successfully.");
        }

        Allure.step("Using Order Type: " + orderType);

        // 1. التعامل مع إغلاق الطلب (لغير التوصيل)
        if (!orderType.equalsIgnoreCase("delivery") && !orderType.equalsIgnoreCase("توصيل")) {
            driver.element().clickElement(closeorderbutton);
            Thread.sleep(2000);

            // استبدال الـ Sleep بانتظار ذكي للـ Popup
            if(isOrderTypePopupOpen()) {
                driver.element().clickElement(okbuttononordertype);
            }

            if (driver.element().isElementVisible(checktocloseorder)) {
                driver.element().clickElement(confirmorderbutton);
            }
        }

        // 2. التنقل لشاشة المرتجعات بذكاء
        if (!driver.element().isElementVisible(returnordersbutton)) {
            driver.element().clickElement(homebutton);
            driver.element().clickElement(OrderListsButton);
        }

        driver.element().clickElement(returnordersbutton);

        // ملاحظة: الـ Refresh قد يكون حلاً لمشكلة في الـ Angular (Sync issue)، لا بأس بتركه إذا كان يحل مشكلة فعلية.
//        driver.browser().refreshPage();


        // 3. إنشاء المرتجع
        Thread.sleep(4000);
        driver.element().clickElement(createreturnorderbutton);
        driver.element().clickElement(selectordertomakereturnorder);
        driver.element().clickElement(showordertoreturn);
        driver.element().clickElement(selectallproductroreturn);

        // 4. الحسابات (Math & Extractions)
        String pricebeforereturn = waitForNonEmptyText(pricebforereturn);
        double priceBeforeReturn = safeParseDouble(pricebeforereturn, "Price before return");

        String returnedItem = waitForNonEmptyText(returneditem); // تم حذف السطر المكرر هنا

        // 5. حفظ المرتجع والعودة للقائمة
        driver.element().clickElement(savetheReturn);
        ScreenShotsManager.takeFullPageScreenshot(driver.get(), "Before_CreateReturnOrder");
        driver.element().clickElement(returnorderlist);

        // 6. التحقق النهائي (Full Return Assumption)
        String priceafterreturn = waitForNonEmptyText(thepriceofreturnorder);
        double priceAfterReturn = safeParseDouble(priceafterreturn, "Price of Return Invoice");

        double expectedAfterReturn = priceBeforeReturn; // لأننا اخترنا selectallproductroreturn

        if (Math.abs(expectedAfterReturn - priceAfterReturn) > 0.1) {
            throw new AssertionError(
                    "❌ Return calculation mismatch | " +
                            "Original Order Total = " + priceBeforeReturn +
                            " | Returned Items = " + returnedItem +
                            " | Return Invoice Total = " + priceAfterReturn
            );
        }

        ScreenShotsManager.takeFullPageScreenshot(driver.get(), "After_ReturnOrder_Success");
        LogsManager.info("✅ Full Return Successful. Original Price: " + priceBeforeReturn + ", Return Invoice Price: " + priceAfterReturn);
        Allure.step("✅ Full Return Successful. Original Price: " + priceBeforeReturn + ", Return Invoice Price: " + priceAfterReturn);

        return this;
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
            return Double.parseDouble(value.trim());
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
    @Step("Select Order To Pay")
    public PaymentPage selectOrderToPay() throws InterruptedException {
        driver.element().clickElement(selectorderbutton);
        String tabName = driver.element().getElementText(ordertypebutton);
        if (tabName.toLowerCase().contains("توصيل") || tabName.toLowerCase().contains("delivery")) {
            driver.element().clickElement(cashieroperationbutton);
            driver.element().clickElement(followorderbutton);
            driver.element().clickElement(returndriver);
            if (driver.element().isElementVisible(selectreturndriver)) {

                driver.element().clickElement(selectreturndriver);
                
            }
            else{
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

        if (driver.element().isElementVisible(ordertypes)){

            driver.element().clickElement(clickOk);
        }

        driver.element().clickElement(payementbutton);
        return new PaymentPage(driver);
    }
    // @Step("Select table if order type is dine in")
    // public OrderPage selectTable() {
    //     driver.element().clickElement(selecttablebutton);
    //     return this;
    // }


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

                // لو بتستخدم context في GUIDriver
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
    public OrderPage get_All_Product_From_API(){
        UserManagmentAPI userManagmentAPI=new UserManagmentAPI(driver);
        // استلام المنتجات من الـ API وتخزينها في المتغير
        this.apiProductsList = userManagmentAPI.getAllProductNames();

        // تسجيل عدد المنتجات في تقرير Allure لسهولة التتبع
        Allure.step("✅ Successfully fetched " + apiProductsList.size() + " products from API");

        return this; // إرجاع الصفحة الحالية للحفاظ على التسلسل (Fluent Pattern)
    }

    @Step("Search for a random product from API in UI")
    public OrderPage searchRandomAPIProductInUI() {
        Assert.assertNotNull(apiProductsList, "🚨 الـ API لم يقم بجلب المنتجات بعد!");
        Assert.assertTrue(apiProductsList.size() > 0, "🚨 القائمة في الـ API فارغة!");

        // اختيار اسم منتج عشوائي وحفظه في متغير الكلاس لكي تراه الدوال الأخرى
        Random rand = new Random();
        this.currentSearchedProduct = apiProductsList.get(rand.nextInt(apiProductsList.size()));

        // كتابة الاسم في حقل البحث
        driver.element().typeText(searchinput, this.currentSearchedProduct);

        Allure.step("✅ Searched for product: " + this.currentSearchedProduct);
        return this;
    }

    @Step("Select the dynamically searched product from the screen")
    public OrderPage selectSearchedProduct() {

        // التأكد من أننا قمنا بالبحث عن منتج أولاً
        Assert.assertNotNull(this.currentSearchedProduct, "🚨 يجب استدعاء دالة البحث أولاً قبل محاولة النقر!");

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

    //validation
    @Step("Validate that order is sent successfully")
    public OrderPage validateOrderIsSentSuccessfully() throws InterruptedException {
        ScreenShotsManager.takeFullPageScreenshot(driver.get(), "Before Sending Order");
        String priceBeforeSendOrder = driver.element().getElementText(totalpricebeforesendorder);
        Thread.sleep(2000);
        driver.element().clickElement(sendorderbutton);
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
        driver.element().clickElement(paybutton);
        driver.element().clickElement(closeorderbutton);
        return this;
    }
    public void validateToastContains(String expectedText) {

        By toast = By.cssSelector(".toast-info .toast-message");

        driver.element().hoverOverElement(toast);
        driver.element().getElementText(toast);

        String actual = driver.element().getElementText(toast);

        if (!actual.contains(expectedText)) {
            throw new AssertionError(
                    "❌ Expected: " + expectedText + " but found: " + actual
            );
        }

        Allure.step("✅ Toast validated: " + actual);
    }

}



