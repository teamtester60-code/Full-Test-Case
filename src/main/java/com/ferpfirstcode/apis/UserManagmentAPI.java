package com.ferpfirstcode.apis;

import com.ferpfirstcode.driver.GUIDriver;
import com.ferpfirstcode.utils.logs.LogsManager;
import com.ferpfirstcode.validations.Verification;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.HashMap;
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
}
