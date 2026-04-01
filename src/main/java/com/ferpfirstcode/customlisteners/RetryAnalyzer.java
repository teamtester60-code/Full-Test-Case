package com.ferpfirstcode.customlisteners;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzer implements IRetryAnalyzer {

    private int retryCount = 0;
    private static final int maxRetry = 2;

    @Override
    public boolean retry(ITestResult result) {

        System.out.println("🔥 RETRY METHOD CALLED");

        Throwable error = result.getThrowable();

        // ❌ مؤقتًا شيل الشرط ده للتجربة
        // لو سيبته، Assert.fail مش هيعمل retry
//         if (error instanceof AssertionError) {
//             return false;
//         }

        if (retryCount < maxRetry) {
            retryCount++;

            result.setAttribute("wasRetried", true);
            result.setAttribute("retryCount", retryCount);

            System.out.println("🔁 Retry " + retryCount + " for " + result.getName());

            return true;
        }

        return false;
    }
}