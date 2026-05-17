package com.ferpfirstcode.apis;

import org.testng.Assert;

import io.qameta.allure.Allure;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class AuthManager {

    private static String dynamicToken;
    private static String currentPin;

    // وضعنا رقم نقطة البيع الخاص ببيئة الاختبار هنا مباشرة (لأنه يمثل الجهاز وليس المستخدم)
    private static final String ENVIRONMENT_POS_ID = "683310db8a28e71b98b320a8";

    public static void setupAuth(String pin) {
        // إذا كان التوكن موجوداً لنفس المستخدم، لا تُعد تسجيل الدخول
        if (dynamicToken != null && pin.equals(currentPin)) {
            return;
        }

        Allure.step("🔐 Authenticating via Login API to get Dynamic Token...");

        // الـ Payload الصحيح الذي اكتشفناه معاً!
        String loginPayload = "{\n" +
                "  \"LoginUserName\": 1,\n" +
                "  \"LoginPassword\": \"" + pin + "\",\n" +
                "  \"LoginWithCard\": false\n" +
                "}";

        Response loginResponse = RestAssured.given()
                .baseUri("http://localhost:56740")
                .contentType(ContentType.JSON)
                // 💡 إضافة الهيدر هنا اختياري، ولكن بعض الأنظمة تفضله لربط الجلسة
                .header("PointOfSaleDocumentId", ENVIRONMENT_POS_ID)
                .body(loginPayload)
                .when()
                .post("api/User/Login");

        Assert.assertEquals(loginResponse.getStatusCode(), 200, "🚨 Login API failed!");

        // استخراج التوكن بنجاح
        String extractedToken = loginResponse.jsonPath().getString("token");
        Assert.assertNotNull(extractedToken, "🚨 التوكن المستخرج قيمته Null!");

        dynamicToken = "Bearer " + extractedToken;
        currentPin = pin;

        Allure.step("✅ Token Generated Successfully!" + dynamicToken);
    }

    // دالة استدعاء التوكن (ديناميكية)
    public static String getToken(String pin) {
        setupAuth(pin);
        return dynamicToken;
    }

    // دالة استدعاء الـ POS ID (تُرجع إعدادات البيئة)
    public static String getPOSDocumentId() {
        return ENVIRONMENT_POS_ID;
    }
}