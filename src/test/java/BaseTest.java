import com.ferpfirstcode.driver.GUIDriver;
import com.ferpfirstcode.driver.WebDriverProvider;
import com.ferpfirstcode.media.ScreenShotsManager;
import com.ferpfirstcode.utils.dataReader.DataBaseReader;
import com.ferpfirstcode.utils.logs.LogsManager;
import io.qameta.allure.testng.AllureTestNg;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.*;

@Listeners({AllureTestNg.class})
public class BaseTest implements WebDriverProvider {

    protected WebDriver webDriver;
    protected GUIDriver guiDriver;

    @BeforeSuite(alwaysRun = true)
    public void setUpSuite() {
        DataBaseReader.connect();
        LogsManager.info("MongoDB connection opened.");
    }

    @BeforeMethod(alwaysRun = true)
    public void setup() {
        guiDriver = new GUIDriver();
        webDriver = guiDriver.get();

        if (webDriver == null) {
            throw new RuntimeException("WebDriver initialization failed.");
        }
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {

        // (اختياري) Screenshot عند الفشل - قبل ما نقفل driver
        if (webDriver != null && result != null && result.getStatus() == ITestResult.FAILURE) {
            try {
                ScreenShotsManager.takeFullPageScreenshot(webDriver, result.getName());
                LogsManager.info("Screenshot captured for failed test: " + result.getName());
            } catch (Exception e) {
                LogsManager.error("Failed to capture screenshot: " + e.getMessage());
            }
        }

        // إغلاق الـ WebDriver بأمان
        if (webDriver != null) {
            try {
                webDriver.quit();
                LogsManager.info("WebDriver closed successfully.");
            } catch (Exception e) {
                LogsManager.error("Error while closing WebDriver: " + e.getMessage());
            } finally {
                webDriver = null;
                guiDriver = null;
            }
        }
    }

    @AfterSuite(alwaysRun = true)
    public void tearDownSuite() {
        DataBaseReader.close();
        LogsManager.info("MongoDB connection closed.");
    }

    @Override
    public WebDriver getDriver() {
        return webDriver;
    }
}
