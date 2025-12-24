package com.ferpfirstcode.pages.components;

import com.ferpfirstcode.driver.GUIDriver;
import com.ferpfirstcode.media.ScreenShotsManager;
import com.ferpfirstcode.utils.logs.LogsManager;

import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

import java.util.List;
import java.util.logging.LogManager;



public class OrderPage {
    private final GUIDriver driver;
    public OrderPage(GUIDriver driver) {
        this.driver = driver;
    }
    //Locators
    private final By OrderListButton= By.xpath("//div[contains(@class,'ms-panel-body')]//i[contains(@class,'fa-clipboard-list')]");
    private final By orderpagebutton = By.xpath("//a[@href='/order']");
    private final By volumesPOPUP= By.cssSelector("div.modal-content.modal-contentWidth");
    private final By okbuttonforsidedishmodal = By.cssSelector("button.btn.btn-primary.btn-footer.btnEdit");
    private  final By volumeseditionbutton= By.xpath("(//button[contains(@class,'dropDownBtn')])[1]");
    private  final By sidedisheeditionbutton= By.xpath("(//button[contains(@class,'dropDownBtn')])[2]");
    private  final By noteseditionbutton= By.xpath("(//button[contains(@class,'dropDownBtn')])[3]");
    private  final By productdiscounteditionbutton= By.xpath("(//button[contains(@class,'dropDownBtn')])[4]");
    private  final By insuranceitemseditionbutton= By.xpath("(//button[contains(@class,'dropDownBtn')])[5]");
    private  final By changepriceeditionbutton= By.xpath("(//button[contains(@class,'dropDownBtn')])[6]");
    private final By ordertypebutton= By.id("ordertype-tab");
    private final By newOrderButton= By.id("c-tab");
    private final By numberofpeopleontable= By.cssSelector("img.pull-right.img-thumbnail");
    private final By personscountbar= By.id("PersonsCount");
    private final By okbuttononpersonscountbar= By.id("#modal-Persons div.modal-footer button.btnEdit");
    private final By sendorderbutton= By.xpath("//*[@id=\"ct-tab\"]/div[1]");
    private final By payementbutton= By.xpath("//*[@id=\"coact-tab\"]/div[1]");
    private final By sidedishPOPUP= By.id("modal-NewSideDishes");
    private final By searchbynamefield= By.xpath("(//input[@placeholder='Search By Name'])[1]");
    private final By selectcustomerbutton= By.cssSelector("button.btnPLus.btn-link");
    private final By selectaddressbutton= By.xpath("(//*[@id=\"collapseOne_@i\"]/td[5]/button)[1]");
    private final By closecustomerselectionmodalbutton= By.xpath("//*[@id=\"nav-home\"]/div/div[2]/div[2]/div/button");
    private final By opensentordersbutton= By.xpath("//*[@id=\"OverLayPin\"]/div/div[1]/div[2]/div[3]/div/nav/ul/li[4]/a");
    private final By totalpricebeforesendorder=By.xpath("//li[contains(@class,'bg-maingreen')]//h5[last()]");
    private final By totalpriceaftersendorder=By.xpath("//table[contains(@class,'table')]//tbody//tr[1]/td[3]");
    private final By selectedcostomerindilevery= By.xpath("//*[@id=\"accordion\"]/tr[1]/td[1]");
    private final By tablenumber = By.xpath("(//li[starts-with(@id,'liDrag') and not(contains(@class,'TableBusy'))])[1]");
    private final By selectorderbutton= By.xpath("(//table//tbody//button)[2]");
    private final By employeebutton= By.xpath("//*[@id=\"modal-Waiter\"]/div/div/div[2]/div/div/div/div[3]/perfect-scrollbar/div/div[1]/table/tbody/tr[1]/td/button");
    private final By cashieroperationbutton= By.xpath("//*[@id=\"dropdownMenuLink\"]");
    private final By followorderbutton= By.xpath("//a[contains(@class, \"dropdown-item\") and @href=\"/FollowOrder\"]");
    private final By checklorderbutton=By.xpath("(//table[contains(@class,'e-table')]//tbody//tr[last()]//input[@type='checkbox'])[1]");
    private final By assigntodriverbutton= By.xpath("(//button[contains(@class, \"btn-success\") and contains(@class, \"m-0\")])[2]");
    private final By assignbutton= By.xpath("//button[@data-toggle=\"modal\" and @data-target=\"#modal-1\"]");
    private final By cancelprintbutton= By.xpath("//button[normalize-space()='Cancel']");
    private final By paytheordersbutton= By.xpath("//li[@id=\"tab2\"]/a[@href=\"#tab144\"]\n");
    private final By selcetordertopaybutton= By.xpath("(//tbody/tr[last()]/td[1]//input[contains(@class,'e-checkbox')])[2]");
    private final By paybutton= By.xpath("//button[@type=\"button\" and contains(@class, \"btn-success\") and contains(@class, \"btnEdit\")]");
    private final By ordertypes = By.xpath("//div[@id='v-pills-tab']//a");
    private final By clickOk= By.xpath("//*[@id=\"modal-OrderType\"]/div/div/div[3]/button");
    private final By manageOrdersbutton= By.xpath("//a[@href=\"/manageorderlist\"]");
    private final By showorderbutton= By.xpath("(//tbody/tr)[last()]//button[contains(@class,'btn-info')][2]");
    private final By customerreciptbutton= By.xpath("(//button[contains(@class, \"btn-primary\") and contains(@class, \"rounded\")])[1]");
    private final By cancelproductsbutton=By.xpath("//div[contains(@class, 'fiixedCancel')]");
    private final By cancelbutton=By.xpath("//button[contains(@class, 'btn-danger')]");
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
    private final By returndriver=By.xpath("//button[@data-target='#modal-2']");
    private final By selectreturndriver=By.xpath("//td[@aria-colindex='2']//button[@type='button']");
    private final By closereturndriver=By.xpath("(//div[contains(@class,'modal-content')]//button[@data-dismiss='modal'])[2]");
 

    


    //dynamic locator
    private By productByIndex(int index) {
        return By.xpath("(//div[contains(@class,'product-card')])["+index+"]");
    }
    private By volumeByIndex(int index) {
        return By.xpath("(//div[@id='modal-Volums']//button[contains(@class,'volumeSelect')])["+index+"]");
    }
    private By sideDishByIndex(int index) {
        return By.xpath("(//div[@id='modal-NewSideDishes']//li[contains(@class,'liSide')]//button[contains(@class,'btn-success')])["+ index +"]");
    }
    private By hallByindex(int index) {
        return By.xpath("(//li[contains(@class,'tab')])["+index+"]");
    }
    private final By ordertypenamebyindex =By.xpath("(//*[@id='v-pills-settings-tab'])[1]");




    //Actions
    @Step("Click on Products")
    @Description("Click on Product: {productName} and handle volume and side dishes if popup appears")
    public OrderPage clickOnProduct() throws InterruptedException {
        addProduct(1);
        addProduct(2);
        driver.element().isElementVisible(totalpricebeforesendorder);
        ScreenShotsManager.takeFullPageScreenshot(driver.get(),"AfterClickOnProduct");
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
    @Step("Select Order Type: {tabName}")
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

    @Step("Make A Return Order")
    public OrderPage makeAReturnOrder() {
        if (!driver.element().isElementVisible(returnordersbutton)) {
                driver.element().clickElement(homebutton);
                driver.element().clickElement(OrderListsButton);
        }
    
        driver.element().clickElement(returnordersbutton);
        driver.browser().refreshPage();
        driver.element().clickElement(createreturnorderbutton); 
        driver.element().clickElement(selectordertomakereturnorder);
        driver.element().clickElement(showordertoreturn);
        driver.element().clickElement(selectallproductroreturn);
        String pricebeforereturn = driver.element().getElementText(pricebforereturn);
        double priceBeforeReturn = Double.parseDouble(pricebeforereturn);
        driver.element().clickElement(savetheReturn);
        driver.element().clickElement(returnorderlist);
        String priceafterreturn = driver.element().getElementText(thepriceofreturnorder);
        double priceAfterReturn = Double.parseDouble(priceafterreturn);
        if (priceBeforeReturn != priceAfterReturn) {
            throw new AssertionError("Price before return: " + priceBeforeReturn + ", Price after return: " + priceAfterReturn);
        }
        ScreenShotsManager.takeFullPageScreenshot(driver.get(), "ReturnOrder");
        LogsManager.info("Price before return: " + priceBeforeReturn + ", Price after return: " + priceAfterReturn);
        Allure.step("Price before return: " + priceBeforeReturn + ", Price after return: " + priceAfterReturn);
        return this;
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
            driver.element().isElementVisible(cancelprintbutton);
            driver.element().clickElement(cancelprintbutton);
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


    @Step("Cancel Order")
    public OrderPage cancelOrder() {
        driver.element().clickElement(opensentordersbutton);
        driver.element().clickElement(selectorderbutton);
        String priceOfProductTocancel = driver.element().getElementText(priceOfProductToCancel);
        String totalpriceBeforderbeforecancel = driver.element().getElementText(totalpricebeforesendorder);
        double priceOfProductToCancel = Double.parseDouble(priceOfProductTocancel);
        double totalPriceBeforeCancel = Double.parseDouble(totalpriceBeforderbeforecancel);
        driver.element().clickElementByJS(cancelproductsbutton);
        driver.element().clickElement(checkproducttocancel);
        driver.element().clickElement(sendorderaftercancelproduct);
        driver.element().clickElement(customerresoncancelbutton);
        driver.element().clickElement(opensentordersbutton);
        String totalpriceoforderaftercancel = driver.element().getElementText(totalpriceaftersendorder);
        double totalPriceAfterCancel = Double.parseDouble(totalpriceoforderaftercancel);
        if (totalPriceBeforeCancel != totalPriceAfterCancel + priceOfProductToCancel) {
            throw new AssertionError("Total price mismatch: before cancel: " + totalPriceBeforeCancel + ", after cancel: " + totalPriceAfterCancel + ", price of product to cancel: " + priceOfProductToCancel);
        }
    
        LogsManager.info("Total price before cancel: " + totalPriceBeforeCancel + ", Total price after cancel: " + totalPriceAfterCancel + ", price of product to cancel: " + priceOfProductToCancel);
        Allure.step("Total price before cancel: " + totalPriceBeforeCancel + ", Total price after cancel: " + totalPriceAfterCancel + ", price of product to cancel: " + priceOfProductToCancel);

        return this;
    }

    //validation
    @Step("Validate that order is sent successfully")
    public OrderPage validateOrderIsSentSuccessfully() throws InterruptedException {
        ScreenShotsManager.takeFullPageScreenshot(driver.get(), "Before Sending Order");
        String priceBeforeSendOrder = driver.element().getElementText(totalpricebeforesendorder);
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

}
