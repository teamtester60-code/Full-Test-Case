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
    public void tearDown() {
        if (webDriver != null) {
            webDriver.quit();
        }
    }
    @AfterMethod
    public void takeScreenshotOnFailure(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE && webDriver != null) {
            ScreenShotsManager.takeFullPageScreenshot(webDriver, result.getMethod().getConstructorOrMethod().getName());
         
            LogsManager.info("Screenshot taken for failed test: " + result.getMethod().getConstructorOrMethod().getName());
        }
    }
}