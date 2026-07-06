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
                // 🔥 1. تدمير التنبيهات قبل البحث عن العنصر والنزول إليه
                destroyObstacles();

                WebElement element = d.findElement(locator);
                scrollToElement(locator);

                // 🔥 2. تدمير التنبيهات مرة أخرى (تحسباً لظهور تنبيه أثناء عمل Scroll)
                destroyObstacles();

                element.click();
                LogsManager.info("Clicked element: " + locator);
                return true;
            });
        } catch (Exception e) {
            LogsManager.error("Failed to normal click element: " + locator + " - " + e.getMessage());

            // 🛡️ خط الدفاع الأخير: إذا فشل الضغط العادي (بسبب Angular Animations مثلاً)
            // سنستخدم دالة JS Click الموجودة لديك بالفعل كمنقذ أخير قبل إعلان فشل التست!
            try {
                LogsManager.info("🔄 Attempting Fallback JS Click for: " + locator);
                clickElementByJS(locator);
            } catch (Exception jsException) {
                // إذا فشل كلاهما، نأخذ سكرين شوت ونرمي الخطأ
                takeScreenshot("clickElement-" + locator.toString());
                throw e; // نرمي الخطأ الأصلي
            }
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
    public int getElementsCount(By locator) {
        try {
            // Using driver.findElements directly. It returns an empty list (size 0)
            // if no elements are found, which is exactly what we want when counting.
            List<WebElement> elements = driver.findElements(locator);
            int count = elements.size();

            LogsManager.info("Counted " + count + " elements for locator: " + locator);
            return count;

        } catch (Exception e) {
            // Catching any unexpected WebDriver errors, logging them, and returning 0 safely
            LogsManager.error("Failed to count elements for: " + locator + " - " + e.getMessage());
            takeScreenshot("getElementsCount-" + locator.toString());
            return 0;
        }
    }

    public void destroyObstacles() {
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            // ❌ الكود القديم: كان يمسح الـ Container بالكامل
            // js.executeScript("var toast = document.getElementById('toast-container'); if(toast) { toast.remove(); }");

            // ✅ الكود الجديد: يمسح التنبيهات الفردية المعروضة فقط، ويترك الـ Container حياً
            js.executeScript("var toasts = document.querySelectorAll('.ngx-toastr'); toasts.forEach(function(toast) { toast.remove(); });");
        } catch (Exception e) {
            // صمت تام
        }
    }
    // ==========================================
    // 🍞 Toast Validation Methods
    // ==========================================
    public String getToastMessageAndDestroyIt() {
        By toastLocator = By.cssSelector(".toast-message");
        try {
            // 🔥 إضافة الـ Hover هنا لمنع التنبيه من الاختفاء قبل قراءته
            hoverOverElement(toastLocator);

            String toastText = waitsManager.fluentWait().until(d -> {
                WebElement element = d.findElement(toastLocator);
                String text = element.getText();
                if (text != null && !text.isEmpty()) {
                    return text;
                }
                return null;
            });

            LogsManager.info("✅ Toast message captured: " + toastText);

            // 🔥 تدمير التنبيه بعد قراءته لتنظيف الشاشة
            destroyObstacles();

            return toastText;

        } catch (Exception e) {
            LogsManager.error("❌ Failed to capture toast message: " + e.getMessage());
            takeScreenshot("ToastCaptureFailure");
            return "";
        }
    }
    // ==========================================
    // 🎯 Raw Click (Without Toast Destruction)
    // ==========================================
    public ElementActions clickElementRaw(By locator) {
        try {
            waitsManager.fluentWait().until(d -> {
                WebElement element = d.findElement(locator);
                scrollToElement(locator);

                // ضغط طبيعي بدون استدعاء destroyObstacles()
                element.click();

                LogsManager.info("Raw Clicked element (Kept Toasts Alive): " + locator);
                return true;
            });
        } catch (Exception e) {
            LogsManager.error("Failed to raw click element: " + locator + " - " + e.getMessage());
            takeScreenshot("rawClickElement-" + locator.toString());

            // محاولة أخيرة بالـ JS إذا فشل الضغط العادي
            try {
                LogsManager.info("🔄 Attempting Fallback JS Raw Click for: " + locator);
                ((JavascriptExecutor) driver).executeScript("arguments[0].click()", driver.findElement(locator));
            } catch (Exception jsException) {
                throw e; // رمي الخطأ الأصلي
            }
        }
        return this;
    }
//
//    public String catchSerialFromNetwork(By buttonToClick) {
//        // 1. تفعيل الـ DevTools بطريقة مختصرة
//        org.openqa.selenium.devtools.DevTools devTools = ((org.openqa.selenium.devtools.HasDevTools) driver.get()).getDevTools();
//        devTools.createSession();
//
//        // 🔥 بدلاً من .enable() الطويلة، سنستخدم الأسلوب المباشر
//        devTools.send(org.openqa.selenium.devtools.v147.network.Network.enable(java.util.Optional.empty(),
//                java.util.Optional.empty(),
//                java.util.Optional.empty(),
//                java.util.Optional.empty(),
//                java.util.Optional.empty()));
//        final String[] caughtSerial = new String[1];
//
//        // 2. استخدام الإصدار المطابق v148 الخاص بمشروعك
//        devTools.addListener(org.openqa.selenium.devtools.v147.network.Network.requestWillBeSent(), request -> {
//            String url = request.getRequest().getUrl().toLowerCase();
//
//            if (url.contains("closeorder") || url.contains("updateorder") || url.contains("updatepaidorderasync")) {
//                if (request.getRequest().getPostData().isPresent()) {
//                    String payload = request.getRequest().getPostData().get();
//                    java.util.regex.Matcher matcher = java.util.regex.Pattern.compile("(?i)\"?serial\"?\\s*:\\s*\"([^\"]+)\"").matcher(payload);
//                    if (matcher.find()) {
//                        caughtSerial[0] = matcher.group(1);
//                        LogsManager.info("🎯 Captured Serial: " + caughtSerial[0]);
//                    }
//                }
//            }
//        });
//
//        // 3. الضغط على الزر
//        clickElementRaw(buttonToClick);
//
//        // 4. الانتظار
//        try {
//            new org.openqa.selenium.support.ui.WebDriverWait(driver.get(), java.time.Duration.ofSeconds(10))
//                    .until(d -> caughtSerial[0] != null);
//        } catch (Exception e) {
//            throw new RuntimeException("❌ Timeout: Serial was not captured.");
//        } finally {
//            devTools.send(org.openqa.selenium.devtools.v148.network.Network.disable());
//            devTools.close();
//        }
//
//        return caughtSerial[0];
//    }
}

