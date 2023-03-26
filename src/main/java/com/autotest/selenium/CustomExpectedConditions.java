package com.autotest.selenium;

import org.apache.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.autotest.utils.LogUtils;
import com.google.common.base.Function;

public class CustomExpectedConditions {
    private static Logger log = Logger.getLogger(CustomExpectedConditions.class.getName());

    public static Function<WebDriver, Boolean> elementToBeWriteInputable(final WebElement element, final String text) {
        return new Function<WebDriver, Boolean>() {

            public Boolean apply(WebDriver driver) {
                element.click();
                element.clear();
                log.info("Custom ExpectedCondition write input");
                element.sendKeys(text);
                if (text.equals(element.getAttribute("value"))) {
                    return true;
                }
                JavascriptExecutor executor = (JavascriptExecutor) driver;
                executor.executeScript("window.scrollTo(0," + element.getLocation().x + ")");
                executor.executeScript("arguments[0].setAttribute('value',arguments[1])", element, text);
                if (text.equals(element.getAttribute("value"))) {
                    return true;
                }
                return false;
            }
        };
    }

    public static Function<WebDriver, Boolean> pageLoadComplete() {
        return new Function<WebDriver, Boolean>() {

            public Boolean apply(WebDriver driver) {
                try {
                    boolean flagLoadComplete = false;
                    flagLoadComplete = ((JavascriptExecutor) driver).executeScript("return document.readyState")
                            .equals("complete");
                    return flagLoadComplete;
                } catch (Exception e) {
                    e.printStackTrace();
                    LogUtils.infoToFile("ExpectedCondition wait page load complete: " + e);
                    return false;
                }
            }
        };

    }

}
