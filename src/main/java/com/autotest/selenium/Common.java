package com.autotest.selenium;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.autotest.dto.ElementDto;
import com.autotest.utils.Constants;
import com.autotest.utils.LogUtils;

public class Common {

    public static By getLocation(ElementDto element) {
        By by = null;
        String locator = element.getLocatorType();
        switch (locator) {
        case Constants.LOCATOR_BY_ID:
            by = By.id(element.getLocatorValue());
            break;
        case Constants.LOCATOR_BY_NAME:
            by = By.name(element.getLocatorValue());
            break;
        case Constants.LOCATOR_BY_CLASS:
            by = By.className(element.getLocatorValue());
            break;
        case Constants.LOCATOR_BY_XPATH:
            by = By.xpath(element.getLocatorValue());
            break;
        default:
            break;
        }
        return by;
    }

    public static boolean verifyWriteInput(WebElement element, String data) {
        LogUtils.infoToFile("element.getText(): " + element.getAttribute("value") + ", data from param: " + data);
        return data.equals(element.getAttribute("value").trim());
    }

}
