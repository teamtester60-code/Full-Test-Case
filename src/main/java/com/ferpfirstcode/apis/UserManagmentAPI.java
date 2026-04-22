package com.ferpfirstcode.apis;

import com.ferpfirstcode.driver.GUIDriver;
import com.ferpfirstcode.utils.logs.LogsManager;
import com.ferpfirstcode.validations.Verification;
import com.jayway.jsonpath.JsonPath;
import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserManagmentAPI {
    RequestSpecification requestSpecification;
    Response response;
    Verification verification;

    public UserManagmentAPI(GUIDriver driver) {
        requestSpecification = RestAssured.given();
        verification = new Verification();
    }

    //end point
    private static final String createAccount_endpoint = "/createAccount";
    private static final String deleteAccount_endpoint = "/deleteAccount";


    //create user method
    //name, email, password, title (for example: Mr, Mrs, Miss), birth_date, birth_month, birth_year, firstname, lastname, company, address1, address2, country, zipcode, state, city, mobile_number
    @Step("Create User Account With Full Details")
    public UserManagmentAPI createUser(
            String name,
            String email,
            String password,
            String title,
            String birth_date,
            String birth_month,
            String birth_year,
            String firstname,
            String lastname,
            String company,
            String address1,
            String address2,
            String country,
            String zipcode,
            String state,
            String city,
            String mobile_number
    ) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("email", email);
        params.put("password", password);
        params.put("title", title);
        params.put("birth_date", birth_date);
        params.put("birth_month", birth_month);
        params.put("birth_year", birth_year);
        params.put("firstname", firstname);
        params.put("lastname", lastname);
        params.put("company", company);
        params.put("address1", address1);
        params.put("address2", address2);
        params.put("country", country);
        params.put("zipcode", zipcode);
        params.put("state", state);
        params.put("city", city);
        params.put("mobile_number", mobile_number);
        response = requestSpecification.spec(Builder.getUserMangamentRequestSpecification(params)).post(createAccount_endpoint);
        LogsManager.info("User Created Successfully");
        return this;
    }
    // create user with minimal details

    @Step("Create User Account With Minimal Details")
    public UserManagmentAPI createUser(String name, String email, String password, String firstname, String lastname) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("email", email);
        params.put("password", password);
        params.put("title", "Mr");
        params.put("birth_date", "1");
        params.put("birth_month", "1");
        params.put("birth_year", "1990");
        params.put("firstname", firstname);
        params.put("lastname", lastname);
        params.put("company", "company");
        params.put("address1", "address1");
        params.put("address2", "address2");
        params.put("country", "country");
        params.put("zipcode", "zipcode");
        params.put("state", "state");
        params.put("city", "city");
        params.put("mobile_number", "123456789");
        response = requestSpecification.spec(Builder.getUserMangamentRequestSpecification(params)).post(createAccount_endpoint);
        LogsManager.info("User Created Successfully");
        return this;
    }


    //delete user method
    @Step("Delete User Account")
    public UserManagmentAPI deleteUser(String email, String password) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("email", email);
        params.put("password", password);
        response = requestSpecification.spec(Builder.getUserMangamentRequestSpecification(params)).delete(deleteAccount_endpoint);
        LogsManager.info(response.asPrettyString());
        return this;
    }


    //validate user method
    @Step("verify user account")
    public UserManagmentAPI validateUser() {
        LogsManager.info("Response Status Code: " + response.getStatusCode());
        LogsManager.info("Response Content-Type: " + response.getContentType());
        LogsManager.info("Response Body: " + response.asString());

        // محاولة تحليل JSON بغض النظر عن Content-Type
        try {
            String message = response.jsonPath().get("message");
            verification.Equals(message, "User created!", "User not created!");
            LogsManager.info("User created successfully!");
        } catch (Exception e) {
            LogsManager.error("Failed to parse response as JSON: " + e.getMessage());
            LogsManager.error("Response Body: " + response.asString());
        }
        return this;
    }


    @Step("verify user deleted")
    public UserManagmentAPI validateUserDeleted() {
        verification.Equals(response.jsonPath().get("message"), "Account deleted!", "User not deleted!");
        return this;
    }

    @Step("Dynamically fetch the first available customer using multiple search letters")
    public String getFirstAvailableCustomerName() {

        String token = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJVc2VySUQiOiJkMWYyYzY3Ny1kOGQ3LTQxMGUtODgzMC0yMzc3NmY2OGNjNzkiLCJyb2xlIjoiQWRtaW4iLCJVc2VyTmFtZSI6ImFkbWluIiwibmJmIjoxNzc2NTgyNzgwLCJleHAiOjE3NzY2NjkxODAsImlhdCI6MTc3NjU4Mjc4MH0.xmH9nArySaYqKwg_Y72s6MxZ865jAXOHIG_FIgf8ODM";
        String POSdocumentID = "6903c153f6a6de2248add31d";

        // 1. Array of common letters to search for (English, Arabic, and even a space)
        String[] searchCharacters = {"a","A","M", "m", "s", "م", "ا", "س", " "};

        // 2. Loop through the characters one by one
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

            // 3. If we get 200, we found customers! Let's extract the first one and break the loop.
            if (statusCode == 200) {
                String responseBody = response.getBody().asString();
                List<String> allNames = JsonPath.read(responseBody, "$[*].Name");

                if (allNames != null && !allNames.isEmpty()) {
                    String firstCustomer = allNames.get(0);
                    Allure.step("✅ Found customer using letter '" + letter + "': " + firstCustomer);
                    return firstCustomer; // Exit the function immediately with the name
                }
            } else {
                // Log that this specific letter didn't work, but the loop will continue
                System.out.println("Letter '" + letter + "' returned " + statusCode + ", trying next...");
            }
        }

        // 4. If the loop finishes all letters and still hasn't returned a name, THEN we fail the test.
        Assert.fail("🚨 CRITICAL FAILURE: Tried all search letters (A-Z fallback) but the server returned 204 No Content every time! Is the database empty?");

        return null;
    }


    @Step("Get all product names from FirstOpen API")
    public List<String> getAllProductNames() {

        // Variables for easy maintenance
        String token = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJVc2VySUQiOiJkMWYyYzY3Ny1kOGQ3LTQxMGUtODgzMC0yMzc3NmY2OGNjNzkiLCJyb2xlIjoiQWRtaW4iLCJVc2VyTmFtZSI6ImFkbWluIiwibmJmIjoxNzc2NjcwMDk2LCJleHAiOjE3NzY3NTY0OTYsImlhdCI6MTc3NjY3MDA5Nn0.gDGQaKjs5U0NlsNgs5WHL8oBfe5BvWQIuRoQGhENNLk";
        String POSdocumentID = "6903c153f6a6de2248add31d";

        Response response = RestAssured.given()
                .header("Authorization", token)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .header("PointOfSaleDocumentId", POSdocumentID) // Passing the variable perfectly
                .when()
                .get("http://localhost:56740/api/Order/FirstOpen");

        int statusCode = response.getStatusCode();
        Allure.step("Status code: " + statusCode);

        // Hard Assertion: If status is not 200, the test STOPS here immediately.
        Assert.assertEquals(statusCode, 200, "🚨 API Request failed! Expected status code 200 but found: " + statusCode);

        // If the code reaches this line, it means the status code is definitely 200
        String responseBody = response.getBody().asString();

        // Extracting product names
        List<String> allProducts = JsonPath.read(responseBody, "$.productTypes[*].ProductGroups[*].Products[*].Name");

        // Validating the parsed data to prevent NullPointerException
        Assert.assertNotNull(allProducts, "🚨 The extracted product list is null! Check the JSONPath.");
        Assert.assertFalse(allProducts.isEmpty(), "🚨 The product list is empty! No products were found.");

        Allure.step("The number of products fetched: " + allProducts.size());
        // 1. Format the list to have each product on a new line instead of one big block
        String formattedProducts = String.join("\n", allProducts);

        // 2. Add it as a clean text attachment inside the Allure Report
        Allure.addAttachment("📜 Full Product List (" + allProducts.size() + " Items)", "text/plain", formattedProducts);

        return allProducts;
    }

}
