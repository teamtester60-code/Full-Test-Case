package com.ferpfirstcode.apis;

import io.qameta.allure.Allure;

public class AuthManager {

    // 🔥 ضع التوكن الثابت الخاص بك هنا (تأكد من وجود كلمة Bearer قبل التوكن)
    private static final String STATIC_TOKEN = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJVc2VySUQiOiJlM2NmNTY5ZC0zMzMyLTQ3NGYtYTdiNS1iNzYyM2U3M2ZjN2UiLCJyb2xlIjoiQWRtaW4iLCJVc2VyTmFtZSI6ImFkbWluIiwibmJmIjoxNzgzMzI0OTQwLCJleHAiOjE3ODM0MTEzNDAsImlhdCI6MTc4MzMyNDk0MH0.Rjfbe4V8dN76ZM83-RVY-rB8iq_QjWB444gJ0o0orwg";

    // رقم نقطة البيع الخاص ببيئة الاختبار
    private static final String ENVIRONMENT_POS_ID = "6a3250d9d4b72754bc18ab73";

    public static void setupAuth(String pin) {
        // لم نعد بحاجة لاستدعاء الـ API، فقط نوثق في التقرير أننا نستخدم توكن ثابت
        Allure.step("🔐 Using Static/Hardcoded Token for Authentication");
    }

    // دالة استدعاء التوكن (الآن ترجع التوكن الثابت دائماً)
    public static String getToken(String pin) {
        setupAuth(pin);
        return STATIC_TOKEN;
    }

    // دالة استدعاء الـ POS ID
    public static String getPOSDocumentId() {
        return ENVIRONMENT_POS_ID;
    }
}