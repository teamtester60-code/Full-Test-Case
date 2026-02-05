package com.ferpfirstcode;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzer implements IRetryAnalyzer {

    private int retryCount = 0;
    private static final int MAX_RETRY_COUNT = 1; // retry مرة واحدة فقط

    @Override
    public boolean retry(ITestResult result) {
        if (retryCount < MAX_RETRY_COUNT) {
            retryCount++;
            result.setAttribute("retryCount", retryCount);
            result.setAttribute("wasRetried", true);
            return true;
        }
        return false;
    }
}
