import com.ferpfirstcode.utils.logs.LogsManager;
import org.testng.annotations.Test;

public class FirstTest extends BaseTest {
    @Test
    public void verifyLoginPageTitle() {
        LogsManager.info("Opening the page...");
        webDriver.get("https://www.google.com");
        String title = webDriver.getTitle();
        LogsManager.info("Page title: " + title);
        //aaaaa
    }
}