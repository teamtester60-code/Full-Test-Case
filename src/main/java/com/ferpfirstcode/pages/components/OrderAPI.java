package com.ferpfirstcode.pages.components;

import com.ferpfirstcode.utils.dataReader.PropertyReader;
import io.qameta.allure.Allure;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class OrderAPI {

    // ===================== CONSTANTS =====================
    private static final String FIRST_OPEN_ENDPOINT = "/api/Order/FirstOpen";

    // ===================== CONFIG =====================
    private final String baseUrl = PropertyReader.getProperty("baseURLapi");
    private final String token = PropertyReader.getProperty("token");
    private final String posId = PropertyReader.getProperty("pointOfSaleDocumentId");

    // ===================== PUBLIC METHOD =====================
    public String getFirstComboProductName() {
        Response response = sendFirstOpenRequest();
        validateResponse(response);
        return extractFirstComboProductName(response);
    }

    // ===================== REQUEST =====================
    private Response sendFirstOpenRequest() {

        logRequestDetails();

        return RestAssured.given()
                .baseUri(baseUrl)
                .headers(
                        "Authorization", "Bearer " + token,
                        "PointOfSaleDocumentId", posId,
                        "lang", "en",
                        "langs", "ar",
                        "Accept", "application/json",
                        "Content-Type", "application/json"
                )
                .when()
                .get(FIRST_OPEN_ENDPOINT)
                .then()
                .extract()
                .response();
    }

    // ===================== VALIDATION =====================
    private void validateResponse(Response response) {

        int statusCode = response.getStatusCode();
        String body = response.asString();
        String contentType = response.getContentType();

        logResponse(statusCode, body);

        if (statusCode != 200) {
            throw new RuntimeException(
                    "❌ API FAILED\n" +
                            "Status Code: " + statusCode + "\n" +
                            "Response:\n" + body
            );
        }

        if (contentType == null || !contentType.contains("application/json")) {
            throw new RuntimeException(
                    "❌ INVALID CONTENT TYPE\n" +
                            "Expected: application/json\n" +
                            "Actual: " + contentType
            );
        }
    }

    // ===================== PARSING =====================
    private String extractFirstComboProductName(Response response) {

        List<Map<String, Object>> productTypes =
                response.jsonPath().getList("productTypes");

        for (Map<String, Object> type : productTypes) {

            List<Map<String, Object>> groups =
                    (List<Map<String, Object>>) type.get("ProductGroups");

            if (groups == null) continue;

            for (Map<String, Object> group : groups) {

                List<Map<String, Object>> products =
                        (List<Map<String, Object>>) group.get("Products");

                if (products == null) continue;

                for (Map<String, Object> product : products) {

                    if (hasCombo(product)) {
                        return String.valueOf(product.get("Name"));
                    }
                }
            }
        }
        Allure.step("No Combo Product Found");
        return null;
    }

    private boolean hasCombo(Map<String, Object> product) {
        Object combosObj = product.get("Combos");

        if (!(combosObj instanceof List<?> combos)) {
            return false;
        }

        return !combos.isEmpty();
    }

    // ===================== LOGGING =====================
    private void logRequestDetails() {
        System.out.println("========== REQUEST ==========");
        System.out.println("Base URL: " + baseUrl);
        System.out.println("Endpoint: " + FIRST_OPEN_ENDPOINT);
        System.out.println("POS ID: " + posId);
        System.out.println("=============================");
    }

    private void logResponse(int statusCode, String body) {
        System.out.println("========== RESPONSE ==========");
        System.out.println("Status Code: " + statusCode);
        System.out.println("Body: " + body);
        System.out.println("==============================");

        Allure.addAttachment("API Response", body);
    }
}