import com.ferpfirstcode.driver.GUIDriver;
import com.ferpfirstcode.media.ScreenShotsManager;
import com.ferpfirstcode.utils.logs.LogsManager;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public class BaseTest {
    protected WebDriver webDriver;
    protected GUIDriver guiDriver;

    @BeforeMethod
    public void setup() {
        guiDriver = new GUIDriver();
        webDriver = guiDriver.get();
        if (webDriver == null) {
            throw new RuntimeException("WebDriver initialization failed.");
        }
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        // إذا فشل الاختبار، التقط Screenshot
        if (result.getStatus() == ITestResult.FAILURE && webDriver != null) {
            ScreenShotsManager.takeFullPageScreenshot(webDriver,
                    "failed-" + result.getMethod().getConstructorOrMethod().getName());
            LogsManager.info("Screenshot taken for failed test: " + result.getMethod().getConstructorOrMethod().getName());
        }

        // إغلاق الـ WebDriver
        if (webDriver != null) {
            webDriver.quit();
            LogsManager.info("WebDriver closed successfully.");
        }
    }

}