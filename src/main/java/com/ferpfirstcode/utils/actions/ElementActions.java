//package com.ferpfirstcode.utils.actions;
//
//import com.ferpfirstcode.media.ScreenShotsManager;
//import com.ferpfirstcode.utils.WaitsManager;
//import com.ferpfirstcode.utils.logs.LogsManager;
//import org.openqa.selenium.*;
//import org.openqa.selenium.interactions.Actions;
//import org.openqa.selenium.support.ui.Select;
//
//import java.io.File;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//import java.util.Set;
//
//import static java.awt.SystemColor.text;
//
//public class ElementActions {
//    private final WebDriver driver;
//    private WaitsManager waitsManager;
//
//
//    public ElementActions(WebDriver driver) {
//        this.driver = driver;
//        this.waitsManager = new WaitsManager(driver);
//    }
//
//    public ElementActions clickElement(By locator) {
//       waitsManager.fluentWait().until(d -> {
////            try {
////                WebElement element = d.findElement(locator);
////                scrollToElement(locator);
////
////                // تحقق من أن العنصر مستقر
////                Point initialLocation = element.getLocation();
////                Thread.sleep(100); // تأخير بسيط للسماح بالاستقرار
////                Point finalLocation = element.getLocation();
////
////                if (!initialLocation.equals(finalLocation)) {
////                    LogsManager.warn("Element is still moving: " + locator);
////                    return false;
////                }
////
////                // محاولة النقر التقليدي
////                try {
////                    element.click();
////                    LogsManager.info("Clicked on element: " + locator);
////                } catch (Exception clickException) {
////                    // إذا فشل النقر، استخدم JavaScript
////                    ((JavascriptExecutor) d).executeScript("arguments[0].click();", element);
////                    LogsManager.info("Clicked on element using JS: " + locator);
////                }
////
////                return true;
////            } catch (Exception e) {
////                LogsManager.error("Failed to click on element: " + locator);
////                return false;
////            }
////        });
////        return this;
//
//        try {
//            waitsManager.fluentWait().until(d -> {
//                WebElement element = d.findElement(locator);
//                scrollToElement(locator);
//                element.click();
//                LogsManager.info("Clicked on element: " + locator);
//                return true;
//            });
//        } catch (Exception e) {
//            LogsManager.error("Failed to click on element: " + locator + " - " + e.getMessage());
//            takeScreenshot("clickElement-" + locator.toString());
//            throw e;
//        }
//        return this;
//
//    }
//
//    public ElementActions typeText(By locator, String text) {
//       waitsManager.fluentWait().until(d -> {
////                    try {
////                        WebElement element = d.findElement(locator);
////                        scrollToElement(locator);
////                        element.clear();
////                        element.sendKeys(text);
////                        LogsManager.info("Typed text: ", text, " in element: ", locator.toString());
////                        return true;
////                    } catch (Exception e) {
////                        return false;
////                    }
////                }
////        );
////        return this;
//
//        try {
//            waitsManager.fluentWait().until(d -> {
//                WebElement element = d.findElement(locator);
//                scrollToElement(locator);
//                element.clear();
//                element.sendKeys(text);
//                LogsManager.info("Typed text in element: " + locator);
//                return true;
//            });
//        } catch (Exception e) {
//            LogsManager.error("Failed to type text in element: " + locator + " - " + e.getMessage());
//            takeScreenshot("typeText-" + locator.toString());
//            throw e;
//        }
//        return this;
//    }
//
//
//
//    // دالة مساعدة لأخذ Screenshot
//    private void takeScreenshot(String name) {
//        try {
//            if (driver != null) {
//                ScreenShotsManager.takeFullPageScreenshot(driver, name);
//                LogsManager.info("Screenshot captured: " + name);
//            }
//        } catch (Exception ex) {
//            LogsManager.error("Failed to capture screenshot: " + ex.getMessage());
//        }
//    }
//
//    // باقي الدوال بنفس الفكرة: clickElement, typeText, uploadFile, hoverOverElement ...
//}
//
//
////upload file
//    public ElementActions uploadFile(By locator, String filePath) {
//        String absolutePath = System.getProperty("user.dir") + File.separator + filePath;
//        waitsManager.fluentWait().until(d -> {
//                    try {
//                        WebElement element = d.findElement(locator);
//                        scrollToElement(locator);
//                        element.sendKeys(filePath);
//                        LogsManager.info("Uploaded file: ", absolutePath, " in element: ", locator.toString());
//                        return true;
//                    } catch (Exception e) {
//                        return false;
//                    }
//                }
//        );
//        return this;
//    }
//
//    public String getElementText(By locator) {
//        return waitsManager.fluentWait().until(d ->
//                {
//                    try {
//                        WebElement element = d.findElement(locator);
//                        scrollToElement(locator);
//                        String msg = element.getText();
//                        LogsManager.info("Retrieved text from element: " + locator + " - Text: " + msg);
//                        return !msg.isEmpty() ? msg : null;
//                    } catch (Exception e) {
//                        return null;
//                    }
//                }
//        );
//
//    }
//
//    public List<String> getElementsText(By locator) {
//        return waitsManager.fluentWait().until(d -> {
//            try {
//                List<WebElement> elements = d.findElements(locator);
//                List<String> texts = new ArrayList<>();
//                for (WebElement element : elements) {
//                    scrollToElement(locator);
//                    String msg = element.getText();
//                    LogsManager.info("Retrieved text from element: " + locator + " - Text: " + msg);
//                    if (!msg.isEmpty()) {
//                        texts.add(msg);
//                    }
//                }
//                return texts;
//            } catch (Exception e) {
//                return Collections.emptyList();
//            }
//        });
//    }
//
//
//    //function to scroll to element using javascript
//    public void scrollToElement(By locator) {
//        ((org.openqa.selenium.JavascriptExecutor) driver)
//                .executeScript("""
//                        arguments[0].scrollIntoView({behaviour:"auto",block:"center",inline:"center"})""", findElement(locator));
//
//    }
//
//    // find an element
//    public WebElement findElement(By locator) {
//        return driver.findElement(locator);
//    }
//
//    //hover over element
//    public ElementActions hoverOverElement(By locator) {
//        waitsManager.fluentWait().until(d ->
//                {
//                    try {
//                        WebElement element = d.findElement(locator);
//                        scrollToElement(locator);
//                        Actions actions = new Actions(d);
//                        actions.moveToElement(element).build().perform();
//                        LogsManager.info("Hovered over element: ", locator.toString());
//                        return true;
//                    } catch (Exception e) {
//                        return false;
//                    }
//                }
//        );
//        return this;
//    }
//
//    //select from dropdown by visible text
//    public ElementActions selectFromDropdownByVisibleText(By locator, String visibleText) {
//        waitsManager.fluentWait().until(d ->
//                {
//                    try {
//                        WebElement element = d.findElement(locator);
//                        scrollToElement(locator);
//                        Select select = new Select(element);
//                        select.selectByVisibleText(visibleText);
//                        LogsManager.info("Selected from dropdown: ", visibleText, " in element: ", locator.toString());
//                        return true;
//                    } catch (Exception e) {
//                        return false;
//                    }
//                }
//        );
//        return this;
//    }
//
//
//}
//


package com.ferpfirstcode.utils.actions;

import com.ferpfirstcode.media.ScreenShotsManager;
import com.ferpfirstcode.utils.WaitsManager;
import com.ferpfirstcode.utils.logs.LogsManager;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ElementActions {
    private final WebDriver driver;
    private final WaitsManager waitsManager;

    public ElementActions(WebDriver driver) {
        this.driver = driver;
        this.waitsManager = new WaitsManager(driver);
    }

    public ElementActions clickElement(By locator) {
        try {
            waitsManager.fluentWait().until(d -> {
                WebElement element = d.findElement(locator);
                scrollToElement(locator);
                element.click();
                LogsManager.info("Clicked element: " + locator);
                return true;
            });
        } catch (Exception e) {
            LogsManager.error("Failed to click element: " + locator + " - " + e.getMessage());
            takeScreenshot("clickElement-" + locator.toString());
            throw e;
        }
        return this;
    }

    public ElementActions typeText(By locator, String text) {
        try {
            waitsManager.fluentWait().until(d -> {
                WebElement element = d.findElement(locator);
                scrollToElement(locator);
                element.clear();
                element.sendKeys(text);
                LogsManager.info("Typed text in element: " + locator);
                return true;
            });
        } catch (Exception e) {
            LogsManager.error("Failed to type text in element: " + locator + " - " + e.getMessage());
            takeScreenshot("typeText-" + locator.toString());
            throw e;
        }
        return this;
    }

    public ElementActions uploadFile(By locator, String filePath) {
        try {
            waitsManager.fluentWait().until(d -> {
                WebElement element = d.findElement(locator);
                scrollToElement(locator);
                String absolutePath = System.getProperty("user.dir") + File.separator + filePath;
                element.sendKeys(absolutePath);
                LogsManager.info("Uploaded file: " + absolutePath + " in element: " + locator);
                return true;
            });
        } catch (Exception e) {
            LogsManager.error("Failed to upload file: " + locator + " - " + e.getMessage());
            takeScreenshot("uploadFile-" + locator.toString());
            throw e;
        }
        return this;
    }

    public String getElementText(By locator) {
        try {
            return waitsManager.fluentWait().until(d -> {
                WebElement element = d.findElement(locator);
                scrollToElement(locator);
                String text = element.getText();
                LogsManager.info("Retrieved text from element: " + locator + " - " + text);
                return text.isEmpty() ? null : text;
            });
        } catch (Exception e) {
            LogsManager.error("Failed to get text from element: " + locator + " - " + e.getMessage());
            takeScreenshot("getText-" + locator.toString());
            return null;
        }
    }

    public List<String> getElementsText(By locator) {
        try {
            return waitsManager.fluentWait().until(d -> {
                List<WebElement> elements = d.findElements(locator);
                List<String> texts = new ArrayList<>();
                for (WebElement element : elements) {
                    scrollToElement(locator);
                    String text = element.getText();
                    if (!text.isEmpty()) {
                        texts.add(text);
                    }
                }
                return texts;
            });
        } catch (Exception e) {
            LogsManager.error("Failed to get texts from elements: " + locator + " - " + e.getMessage());
            takeScreenshot("getTexts-" + locator.toString());
            return Collections.emptyList();
        }
    }

    public ElementActions hoverOverElement(By locator) {
        try {
            waitsManager.fluentWait().until(d -> {
                WebElement element = d.findElement(locator);
                scrollToElement(locator);
                new Actions(d).moveToElement(element).perform();
                LogsManager.info("Hovered over element: " + locator);
                return true;
            });
        } catch (Exception e) {
            LogsManager.error("Failed to hover over element: " + locator + " - " + e.getMessage());
            takeScreenshot("hover-" + locator.toString());
            throw e;
        }
        return this;
    }

    public ElementActions selectFromDropdownByVisibleText(By locator, String visibleText) {
        try {
            waitsManager.fluentWait().until(d -> {
                WebElement element = d.findElement(locator);
                scrollToElement(locator);
                new Select(element).selectByVisibleText(visibleText);
                LogsManager.info("Selected '" + visibleText + "' from dropdown: " + locator);
                return true;
            });
        } catch (Exception e) {
            LogsManager.error("Failed to select dropdown: " + locator + " - " + e.getMessage());
            takeScreenshot("selectDropdown-" + locator.toString());
            throw e;
        }
        return this;
    }

    public void scrollToElement(By locator) {
        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].scrollIntoView({behavior:'auto',block:'center',inline:'center'})",
                        driver.findElement(locator));
    }

    private void takeScreenshot(String name) {
        try {
            if (driver != null) {
                ScreenShotsManager.takeFullPageScreenshot(driver, name);
                LogsManager.info("Screenshot captured: " + name);
            }
        } catch (Exception ex) {
            LogsManager.error("Failed to capture screenshot: " + ex.getMessage());
        }
    }
}

