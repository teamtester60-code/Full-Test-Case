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
    public ElementActions clickElementByJS(By locator){
        try {
            ((JavascriptExecutor) driver)
                    .executeScript("arguments[0].click()", driver.findElement(locator));
            LogsManager.info("Clicked element: " + locator);
        }
        catch (Exception e){
            LogsManager.error("Failed to click element: " + locator + " - " + e.getMessage());
            takeScreenshot("clickElementByJS-" + locator.toString());
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
    public boolean isElementSelected(By locator) {
        try {
            WebElement element = driver.findElement(locator);
            boolean selected = element.isSelected();
            System.out.println("Element selected status = " + selected);
            return selected;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public String getAttribute(By locator, String attributeName) {
        try {
            WebElement element = driver.findElement(locator);
            return element.getAttribute(attributeName);
        } catch (Exception e) {
            System.out.println("Failed to get attribute '" + attributeName + "' from locator: " + locator);
            throw new RuntimeException(
                    "Failed to get attribute '" + attributeName + "' from element: " + locator, e
            );
        }
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
            return text == null ? "" : text; // never return null
        });
    } catch (Exception e) {
        LogsManager.error("Failed to get text from element: " + locator + " - " + e.getMessage());
        takeScreenshot("getText-" + locator.toString());
        return ""; // never null
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
    public boolean isElementVisible(By locator) {
        try {
            return waitsManager.fluentWait().until(d -> {
                WebElement element = d.findElement(locator);
                scrollToElement(locator);
                boolean isVisible = element.isDisplayed();
                LogsManager.info("Element visibility for: " + locator + " - " + isVisible);
                return isVisible;
            });
        } catch (Exception e) {
            LogsManager.error("Failed to verify visibility of element: " + locator + " - " + e.getMessage());
            takeScreenshot("isElementVisible-" + locator.toString());
            return false; // instead of throwing, return false
        }
    }
}

