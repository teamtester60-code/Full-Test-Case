package com.ferpfirstcode.apis;

import com.ferpfirstcode.driver.GUIDriver;
import com.ferpfirstcode.utils.dataReader.DataBaseReader;
import com.ferpfirstcode.utils.logs.LogsManager;
import com.ferpfirstcode.validations.Verification;
import com.jayway.jsonpath.JsonPath;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserManagmentAPI {
    RequestSpecification requestSpecification;
    Response response;
    Verification verification;
    private JavascriptExecutor JavascriptExecutor;
    private WebDriver driver;

    public UserManagmentAPI(GUIDriver driver) {
        requestSpecification = RestAssured.given();
        verification = new Verification();
    }

    //end point
    private static final String createAccount_endpoint = "/createAccount";
    private static final String deleteAccount_endpoint = "/deleteAccount";


    // 🎯 1. الدالة المساعدة الأهم (الآن هي ديناميكية 100% وتستدعي AuthManager بشكل صحيح)
    private Response getFirstOpenApiResponse() {
        String dynamicPin = DataBaseReader.getAdminPin();
        String token = AuthManager.getToken(dynamicPin);
        String POSdocumentID = AuthManager.getPOSDocumentId();

        System.out.println("🚀 Calling FirstOpen API...");

        return RestAssured.given()
                .header("Authorization", token)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .header("PointOfSaleDocumentId", POSdocumentID) // 👈 أعدنا الهيدر لكي لا ينهار السيرفر
                .when()
                .get("http://localhost:56740/api/Order/FirstOpen");
    }

    @Step("Get all product names from FirstOpen API")
    public List<String> getAllProductNames() {
        // الاستدعاء أصبح بسطر واحد نظيف جداً!
        Response response = getFirstOpenApiResponse();

        int statusCode = response.getStatusCode();
        Allure.step("Status code: " + statusCode);
        Assert.assertEquals(statusCode, 200, "🚨 API Request failed! Expected status code 200 but found: " + statusCode);

        String responseBody = response.getBody().asString();
        List<String> allProducts = JsonPath.read(responseBody, "$.productTypes[*].ProductGroups[*].Products[*].Name");

        Assert.assertNotNull(allProducts, "🚨 The extracted product list is null! Check the JSONPath.");
        Assert.assertFalse(allProducts.isEmpty(), "🚨 The product list is empty! No products were found.");

        Allure.step("The number of products fetched: " + allProducts.size());
        String formattedProducts = String.join("\n", allProducts);
        Allure.addAttachment("📜 Full Product List (" + allProducts.size() + " Items)", "text/plain", formattedProducts);

        return allProducts;
    }

    @Step("Get all order types from FirstOpen API")
    public List<String> getAllOrderTypes() {
        // تخلصنا من التوكن الثابت هنا أيضاً
        Response response = getFirstOpenApiResponse();

        int statusCode = response.getStatusCode();
        Allure.step("Status code: " + statusCode);
        Assert.assertEquals(statusCode, 200, "🚨 API Request failed! Expected status code 200 but found: " + statusCode);

        String responseBody = response.getBody().asString();
        List<String> allOrderTypes = JsonPath.read(responseBody, "$.ordertypes[*].Name");

        Assert.assertNotNull(allOrderTypes, "🚨 The extracted order types list is null! Check the JSONPath.");
        Assert.assertFalse(allOrderTypes.isEmpty(), "🚨 The order types list is empty! No order types were found.");

        Allure.step("The number of order types fetched: " + allOrderTypes.size());
        String formattedOrderTypes = String.join("\n", allOrderTypes);
        Allure.addAttachment("📜 Full Order Types List (" + allOrderTypes.size() + " Items)", "text/plain", formattedOrderTypes);

        return allOrderTypes;
    }

    @Step("Get Latest Canceled Order Time from API")
    public LocalDateTime getLatestCanceledOrderTimeFromAPI() {
        // تنظيف الدالة واستخدام AuthManager بدلاً من التوكن الثابت
        String dynamicPin = DataBaseReader.getAdminPin();
        String token = AuthManager.getToken(dynamicPin);
        String POSdocumentID = AuthManager.getPOSDocumentId();

        java.time.format.DateTimeFormatter payloadFormatter = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm a", java.util.Locale.ENGLISH);
        String currentDate = java.time.LocalDateTime.now().format(payloadFormatter);
        String myJsonPayload = "{\n" +
                "  \"FromDate\": \"" + currentDate + "\",\n" +
                "  \"ProductwithoutsideDish\": false,\n" +
                "  \"ToDate\": \"" + currentDate + "\"\n" +
                "}";

        Response response = RestAssured.given()
                .baseUri("http://localhost:56740")
                .contentType(ContentType.JSON)
                .body(myJsonPayload)
                .header("Authorization", token)
                .header("PointOfSaleDocumentId", POSdocumentID)
                .when()
                .post("/api/SalesReport/GetCanceledProductsReport")
                .then()
                .statusCode(200)
                .extract().response();

        String canceledDate = response.jsonPath().getString("[-1].Canceleddate");
        String canceledTime = response.jsonPath().getString("[-1].Canceledtime");

        String fullDateTimeStr = canceledDate + " " + canceledTime;
        Allure.step("🕒 Latest Canceled Time from API: " + fullDateTimeStr);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return LocalDateTime.parse(fullDateTimeStr, formatter);
    }

    @Step("Dynamically fetch the first available customer using multiple search letters")
    public String getFirstAvailableCustomerName() {
        // تنظيف الدالة واستخدام AuthManager
        String dynamicPin = DataBaseReader.getAdminPin();
        String token = AuthManager.getToken(dynamicPin);
        String POSdocumentID = AuthManager.getPOSDocumentId();

        String[] searchCharacters = {"a", "A", "M", "m", "s", "م", "ا", "س", " "};

        for (String letter : searchCharacters) {
            String payload = "{\"Name\": \"" + letter + "\", \"UseCredit\": true}";

            Response response = RestAssured.given()
                    .header("Authorization", token)
                    .header("Accept", "application/json")
                    .header("Content-Type", "application/json")
                    .header("PointOfSaleDocumentId", POSdocumentID)
                    .body(payload)
                    .when()
                    .post("http://localhost:56740/api/Order/GetCustomerByMobileOrNameAsync/");

            int statusCode = response.getStatusCode();

            if (statusCode == 200) {
                String responseBody = response.getBody().asString();
                List<String> allNames = JsonPath.read(responseBody, "$[*].Name");

                if (allNames != null && !allNames.isEmpty()) {
                    String firstCustomer = allNames.get(0);
                    Allure.step("✅ Found customer using letter '" + letter + "': " + firstCustomer);
                    return firstCustomer;
                }
            } else {
                System.out.println("Letter '" + letter + "' returned " + statusCode + ", trying next...");
            }
        }

        Assert.fail("🚨 CRITICAL FAILURE: Tried all search letters but the server returned 204 No Content every time! Is the database empty?");
        return null;
    }

    // دوال إنشاء وحذف المستخدم تم تركها كما هي لأنها لا تعتمد على التوكن
    @Step("Create User Account With Full Details")
    public UserManagmentAPI createUser(String name, String email, String password, String title, String birth_date, String birth_month, String birth_year, String firstname, String lastname, String company, String address1, String address2, String country, String zipcode, String state, String city, String mobile_number) {
        // ... (تم إخفاء الكود هنا للاختصار، وهو موجود في كودك الأصلي)
        return this;
    }

    // ... باقي دوال الإنشاء والحذف كما هي
}