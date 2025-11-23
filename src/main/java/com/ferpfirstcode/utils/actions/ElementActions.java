package com.ferpfirstcode.utils.actions;

import com.ferpfirstcode.utils.WaitsManager;
import com.ferpfirstcode.utils.logs.LogsManager;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class ElementActions {
    private final WebDriver driver;
    private WaitsManager waitsManager;


    public ElementActions(WebDriver driver) {
        this.driver = driver;
        this.waitsManager = new WaitsManager(driver);
    }

    public ElementActions clickElement(By locator) {
//        waitsManager.fluentWait().until(d ->
//                {
//                    try {
//                        WebElement element = d.findElement(locator);
//                        scrollToElement(locator);
//                        // Wait until the element is stable (not moving)
//                        Point initialLocation = element.getLocation();
//                        LogsManager.info("initialLocation: " + initialLocation);
//                        Point finalLocation = element.getLocation();
//                        LogsManager.info("finalLocation: " + finalLocation);
//                        if (!initialLocation.equals(finalLocation)) {
//                            return false; // still moving, wait longer
//                        }
//                        element.click();
//                        LogsManager.info("Clicked on element: " + locator);
//                        return true;
//                    } catch (Exception e) {
//                        return false;
//                    }
//                }
//        );
//        return this;



//  waitsManager.fluentWait().until(d -> {
//
//     try {
//      WebElement element = d.findElement(locator);
//       scrollToElement(locator);
//        ((JavascriptExecutor) d).executeScript("arguments[0].click();", element);
//         LogsManager.info("Clicked on element using JS: ", locator.toString());
//         return true;
//      }
//      catch (Exception e)
//      { LogsManager.error("JS click failed on element: ", locator.toString());
//       return false;
//     }
//  }
//  );
//  return this;



        waitsManager.fluentWait().until(d -> {
            try {
                WebElement element = d.findElement(locator);
                scrollToElement(locator);

                // تحقق من أن العنصر مستقر
                Point initialLocation = element.getLocation();
                Thread.sleep(100); // تأخير بسيط للسماح بالاستقرار
                Point finalLocation = element.getLocation();

                if (!initialLocation.equals(finalLocation)) {
                    LogsManager.warn("Element is still moving: " + locator);
                    return false;
                }

                // محاولة النقر التقليدي
                try {
                    element.click();
                    LogsManager.info("Clicked on element: " + locator);
                } catch (Exception clickException) {
                    // إذا فشل النقر، استخدم JavaScript
                    ((JavascriptExecutor) d).executeScript("arguments[0].click();", element);
                    LogsManager.info("Clicked on element using JS: " + locator);
                }

                return true;
            } catch (Exception e) {
                LogsManager.error("Failed to click on element: " + locator);
                return false;
            }
        });
        return this;



    }

    public ElementActions typeText(By locator, String text) {
        waitsManager.fluentWait().until(d -> {
                    try {
                        WebElement element = d.findElement(locator);
                        scrollToElement(locator);
                        element.clear();
                        element.sendKeys(text);
                        LogsManager.info("Typed text: ", text, " in element: ", locator.toString());
                        return true;
                    } catch (Exception e) {
                        return false;
                    }
                }
        );
        return this;


    }


    //upload file
    public ElementActions uploadFile(By locator, String filePath) {
        String absolutePath = System.getProperty("user.dir") + File.separator + filePath;
        waitsManager.fluentWait().until(d -> {
                    try {
                        WebElement element = d.findElement(locator);
                        scrollToElement(locator);
                        element.sendKeys(filePath);
                        LogsManager.info("Uploaded file: ", absolutePath, " in element: ", locator.toString());
                        return true;
                    } catch (Exception e) {
                        return false;
                    }
                }
        );
        return this;
    }

    public String getElementText(By locator) {
        return waitsManager.fluentWait().until(d ->
                {
                    try {
                        WebElement element = d.findElement(locator);
                        scrollToElement(locator);
                        String msg = element.getText();
                        LogsManager.info("Retrieved text from element: " + locator + " - Text: " + msg);
                        return !msg.isEmpty() ? msg : null;
                    } catch (Exception e) {
                        return null;
                    }
                }
        );

    }

    public List<String> getElementsText(By locator) {
        return waitsManager.fluentWait().until(d -> {
            try {
                List<WebElement> elements = d.findElements(locator);
                List<String> texts = new ArrayList<>();
                for (WebElement element : elements) {
                    scrollToElement(locator);
                    String msg = element.getText();
                    LogsManager.info("Retrieved text from element: " + locator + " - Text: " + msg);
                    if (!msg.isEmpty()) {
                        texts.add(msg);
                    }
                }
                return texts;
            } catch (Exception e) {
                return Collections.emptyList();
            }
        });
    }


    //function to scroll to element using javascript
    public void scrollToElement(By locator) {
        ((org.openqa.selenium.JavascriptExecutor) driver)
                .executeScript(""" 
                        arguments[0].scrollIntoView({behaviour:"auto",block:"center",inline:"center"})""", findElement(locator));

    }

    // find an element
    public WebElement findElement(By locator) {
        return driver.findElement(locator);
    }

    //hover over element
    public ElementActions hoverOverElement(By locator) {
        waitsManager.fluentWait().until(d ->
                {
                    try {
                        WebElement element = d.findElement(locator);
                        scrollToElement(locator);
                        Actions actions = new Actions(d);
                        actions.moveToElement(element).build().perform();
                        LogsManager.info("Hovered over element: ", locator.toString());
                        return true;
                    } catch (Exception e) {
                        return false;
                    }
                }
        );
        return this;
    }

    //select from dropdown by visible text
    public ElementActions selectFromDropdownByVisibleText(By locator, String visibleText) {
        waitsManager.fluentWait().until(d ->
                {
                    try {
                        WebElement element = d.findElement(locator);
                        scrollToElement(locator);
                        Select select = new Select(element);
                        select.selectByVisibleText(visibleText);
                        LogsManager.info("Selected from dropdown: ", visibleText, " in element: ", locator.toString());
                        return true;
                    } catch (Exception e) {
                        return false;
                    }
                }
        );
        return this;
    }


}

