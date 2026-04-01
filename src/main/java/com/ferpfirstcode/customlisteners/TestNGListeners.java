package com.ferpfirstcode.customlisteners;

import com.ferpfirstcode.FileUtils;
import com.ferpfirstcode.driver.WebDriverProvider;
import com.ferpfirstcode.media.ScreenRecordManager;
import com.ferpfirstcode.media.ScreenShotsManager;
import com.ferpfirstcode.utils.dataReader.PropertyReader;
import com.ferpfirstcode.utils.logs.LogsManager;
import com.ferpfirstcode.utils.report.AllureAttachmentManger;
import com.ferpfirstcode.utils.report.AllureConstants;
import com.ferpfirstcode.utils.report.AllureEnviromentManger;
import com.ferpfirstcode.utils.report.AllureReportGenerator;
import com.ferpfirstcode.validations.Validation;
import org.openqa.selenium.WebDriver;
import org.testng.*;
import io.qameta.allure.Allure;
import io.qameta.allure.model.Label;
import static io.qameta.allure.Allure.getLifecycle;

import java.io.File;
import java.nio.file.Path;

public class TestNGListeners implements IExecutionListener, IInvokedMethodListener, ITestListener {
    public void onExecutionStart() {

        LogsManager.info("======== Starting Test Execution ========");
        org.apache.logging.log4j.LogManager.shutdown();
        cleantestdirs();
        LogsManager.info("✅ Test Directories Cleaned Successfully");
        createtestdirs();
        LogsManager.info("✅ Test Directories Created Successfully");
        PropertyReader.loadProperties();
        LogsManager.info("✅ Test Properties Loaded Successfully");
        AllureEnviromentManger.setEnvironmentVariables();
        LogsManager.info("✅ Allure Environment Variables Set Successfully");
    }

    /**
     * Invoked once all the suites have been run.
     */
    public void onExecutionFinish() {

        AllureReportGenerator.generateReports(false);
        AllureReportGenerator.copyHistory();
        AllureReportGenerator.generateReports(true);

        // اسم الملف بعد إعادة التسمية
        String reportFileName = AllureReportGenerator.renameReport();

        // المسار الكامل للتقرير
        Path fullReportPath = AllureConstants.REPORT_PATH.resolve(reportFileName);
        LogsManager.info("REPORT PATH = " + AllureConstants.REPORT_PATH);
        LogsManager.info("FULL FILE PATH = " + AllureConstants.REPORT_PATH.resolve(reportFileName));

        File testFile = AllureConstants.REPORT_PATH.resolve(reportFileName).toFile();
        LogsManager.info("FILE EXISTS? " + testFile.exists());

        try {
            AllureReportGenerator.openReport(fullReportPath.toString());
            LogsManager.info("🔥 Final Allure HTML report opened successfully");

        } catch (Exception e) {
            LogsManager.error("❌ Could not open final HTML report");
            e.printStackTrace();
        }

        LogsManager.info("✅ Allure Report Generated Successfully");
        LogsManager.info("======== Test Execution Finished ========");
    }


    // public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {
    //     if (method.isTestMethod()) {
    //         ScreenRecordManager.startRecording();
    //         LogsManager.info("======== Starting Test: " + testResult.getMethod().getMethodName() + " ========");
    //     }

    // }

    public void afterInvocation(IInvokedMethod method, ITestResult testResult) {
        WebDriver driver = null;
        if (method.isTestMethod()) {
            ScreenRecordManager.stopRecording(testResult.getMethod().getMethodName());
            Validation.assertAll(testResult);
            if (testResult.getInstance() instanceof WebDriverProvider provider)
                driver = provider.getDriver();
            switch (testResult.getStatus()) {
                case ITestResult.SUCCESS ->
                        ScreenShotsManager.takeFullPageScreenshot(driver, "passed-" + testResult.getName());
                case ITestResult.FAILURE ->
                        ScreenShotsManager.takeFullPageScreenshot(driver, "failed-" + testResult.getName());
                case ITestResult.SKIP ->
                        ScreenShotsManager.takeFullPageScreenshot(driver, "skipped-" + testResult.getName());
            }
            AllureAttachmentManger.attachLogs();
            if (testResult.getStatus() == ITestResult.FAILURE || testResult.getStatus() == ITestResult.SKIP) {
                AllureAttachmentManger.attachRecords(testResult.getName());
            }
        }

    }


    public void onTestStart(ITestResult result) {
        // not implemented
    }

    /**
     * Invoked each time a test succeeds.
     *
     * @param result <code>ITestResult</code> containing information about the run test
     * @see ITestResult#SUCCESS
     */
    public void onTestSuccess(ITestResult result) {
        markFlakyIfRetried(result);
        LogsManager.info("======== Test: " + result.getMethod().getMethodName() + " Finished Successfully ========");
    }

    /**
     * Invoked each time a test fails.
     *
     * @param result <code>ITestResult</code> containing information about the run test
     * @see ITestResult#FAILURE
     */
    public void onTestFailure(ITestResult result) {
        
        LogsManager.error("======== Test: " + result.getMethod().getMethodName() + " Failed ========");
    }

    /**
     * Invoked each time a test is skipped.
     *
     * @param result <code>ITestResult</code> containing information about the run test
     * @see ITestResult#SKIP
     */
    public void onTestSkipped(ITestResult result) {
        markFlakyIfRetried(result);
        LogsManager.warn("======== Test: " + result.getMethod().getMethodName() + " Skipped ========");
    }


    //cleanning and creating dirs

    private void cleantestdirs() {
        FileUtils.cleanDirectory(AllureConstants.RESULTS_FOLDER.toFile());
        FileUtils.cleanDirectory(new File(ScreenShotsManager.SCREENSHOTS_PATH));
        FileUtils.cleanDirectory(new File(ScreenRecordManager.RECORDINGS_PATH));
        FileUtils.deleteLogFile(LogsManager.LOGS_PATH + File.separator + "logs.log");

    }

    private void createtestdirs() {
        FileUtils.createDirectory(ScreenShotsManager.SCREENSHOTS_PATH);
        FileUtils.createDirectory(ScreenRecordManager.RECORDINGS_PATH);
    }

    private void markFlakyIfRetried(ITestResult result) {
    Object wasRetried = result.getAttribute("wasRetried");
    Object retryCount = result.getAttribute("retryCount");

    if (wasRetried != null && (boolean) wasRetried) {
        getLifecycle().updateTestCase(tc -> {
            tc.getLabels().add(new Label().setName("flaky").setValue("true"));
            tc.getLabels().add(new Label().setName("retryCount").setValue(String.valueOf(retryCount)));
        });

        Allure.addAttachment(
                "Retry Info",
                "Test was retried once and marked as flaky."
        );
    }
}





}
