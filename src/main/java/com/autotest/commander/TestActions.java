package com.autotest.commander;

import org.openqa.selenium.By;

import com.autotest.dto.ElementDto;
import com.autotest.selenium.Browser;
import com.autotest.selenium.Common;
import com.autotest.utils.Constants;
import com.autotest.utils.LogUtils;

public class TestActions {

    public String openBrowser(ElementDto element, String data) {
        try {
            if (Browser.getBrowser().launch(Constants.WAIT_TIME_OUT, data)) {
                return Constants.PASS;
            } else {
                return Constants.FAILED;
            }
        } catch (Exception e) {
            LogUtils.infoToFile("Exception open browser " + e);
            return Constants.FAILED;
        }
    }

    public String closeBrowser(ElementDto element, String data) {
        try {
            if (Browser.getBrowser().shutdown()) {
                return Constants.PASS;
            } else {
                return Constants.FAILED;
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.infoToFile("Exception open browser " + e);
            return Constants.FAILED;
        }

    }

    public String navigate(ElementDto element, String data) {
        if (Browser.getBrowser().goToUrl(data)) {
            return Constants.PASS;
        } else {
            return Constants.FAILED;
        }
    }

    public String click(ElementDto elementDto, String data) {
        try {
            By location = Common.getLocation(elementDto);
            boolean flag;
            flag = Browser.getBrowser().click(location);
            if (flag) {
                return Constants.PASS;
            } else {
                return Constants.FAILED;
            }
        } catch (Exception e) {
            return Constants.FAILED;
        }

    }

    public String writeInput(ElementDto elementDto, String data) {
        if (Browser.getBrowser().writeInput(elementDto, data)) {
            return Constants.PASS;
        } else {
            return Constants.FAILED;
        }
    }

    public String selectDropdown(ElementDto elementDto, String data) {
        try {
            if (Browser.getBrowser().selectDropdown(elementDto, data)) {
                return Constants.PASS;
            } else {
                return Constants.FAILED;
            }
        } catch (Exception e) {
            LogUtils.infoToFile(" Exception: " + e.getMessage());
            return Constants.FAILED;
        }
    }

    public String uploadFile(ElementDto elementDto, String data) {
        try {
            if (Browser.getBrowser().uploadFile(elementDto, data)) {
                return Constants.PASS;
            } else {
                return Constants.FAILED;
            }
        } catch (Exception e) {
            LogUtils.infoToFile("Exception: " + e.getMessage());
            return Constants.FAILED;
        }
    }

    public String clearTextInput(ElementDto elementDto, String data) {
        try {
            if (Browser.getBrowser().clearTextInput(elementDto, data)) {
                return Constants.PASS;
            } else {
                return Constants.FAILED;
            }
        } catch (Exception e) {
            LogUtils.infoToFile("Exception: " + e.getMessage());
            return Constants.FAILED;
        }
    }

    public String snapshot(ElementDto elementDto, String data) {
        if (Browser.getBrowser().takeScreenshot(data)) {
            return Constants.PASS;
        } else {
            return Constants.FAILED;
        }
    }

}
