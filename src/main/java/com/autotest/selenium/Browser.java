package com.autotest.selenium;

import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.Select;

import com.autotest.dto.ElementDto;
import com.autotest.utils.Constants;
import com.autotest.utils.LogUtils;

import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

public class Browser {
    private static Browser instance = null;

    // Common Initialization
    private WebDriver driver = null;
    private String rememberedUrl = null;
    private static final int WAIT_TIME_OUT = 10;

    private static final Log log = LogFactory.getLog(Browser.class);

    public static synchronized Browser getBrowser() {
        if (instance == null) {
            instance = new Browser();
        }
        return instance;
    }

    public WebDriver getDriver() {
        return driver;
    }

    @SuppressWarnings("deprecation")
    public boolean launch(int loadTimeOut, String driverLocation) throws InterruptedException {
        if (!new File(driverLocation).exists()) {
            LogUtils.infoToFile("path driver not exist.");
            return false;
        } else {
            if (driver != null) {
                driver = null;
            }
            LogUtils.infoToFile("open browser ");

            System.setProperty("webdriver.chrome.driver", driverLocation);
            ChromeOptions options = new ChromeOptions();
            HashMap<String, Object> chromePrefs = new HashMap<>();
            chromePrefs.put("profile.default_content_settings.popups", 0);
            options.setExperimentalOption("prefs", chromePrefs);
            options.addArguments("--disable-extensions");
            options.addArguments("--start-maximized");
            DesiredCapabilities cap = DesiredCapabilities.chrome();
            cap.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
            cap.setCapability(ChromeOptions.CAPABILITY, options);
            driver = new ChromeDriver(cap);
            driver.manage().window().maximize();
            driver.manage().timeouts().pageLoadTimeout(loadTimeOut, TimeUnit.SECONDS);
            return ((RemoteWebDriver) driver).getSessionId() != null;
        }
    }

    public boolean takeScreenshot(String path) {
//        TakesScreenshot view = TakesScreenshot.class.cast(driver);
//        File screenshot = view.getScreenshotAs(OutputType.FILE);
//        File destination = new File(name + ".png");
//        try {
//            FileUtils.copyFile(screenshot, destination);
//            log.info("Screenshot saved to " + destination.getAbsolutePath());
//        } catch (IOException e) {
//            log.error("Failed to write screenshot to " + destination.getAbsolutePath(), e);
//        }

        Screenshot screenshot = new AShot().shootingStrategy(ShootingStrategies.viewportPasting(1000))
                .takeScreenshot(driver);
        try {
            ImageIO.write(screenshot.getImage(), "png", new File(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    public boolean goToUrl(String url) {
        try {
            LogUtils.infoToFile("navigate to url");
            driver.get(url);
            WaitUtils waitUtils = WaitUtils.getWaitUtils(driver);
            return waitUtils.waitForLoad();
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.infoToFile("Exception navigate " + e);
        }
        return false;

    }

    public void rememberLocation() {
        rememberedUrl = driver.getCurrentUrl();
    }

    public void recallLocation() {
        if (rememberedUrl != null) {
            driver.get(rememberedUrl);
        }
    }

    public boolean shutdown() throws InterruptedException {
        if (driver == null) {
            LogUtils.infoToFile("driver not exist.");
            return false;
        } else {
            try {
                driver.quit();
                int count = 0;
                while (count < WAIT_TIME_OUT) {
                    if (((RemoteWebDriver) driver).getSessionId() == null) {
                        driver = null;
                        return true;
                    } else {
                        Thread.sleep(1000);
                        count++;
                    }
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
                LogUtils.infoToFile("Exception close browser " + e);
            }
            return false;
        }
    }

    public boolean writeInput(ElementDto elementDto, String data) {
        WaitUtils wait = WaitUtils.getWaitUtils(driver);
        WebElement element;
        if (wait.waitForLoad()) {
            By location = Common.getLocation(elementDto);
            try {
                // wait element display on page
                element = wait.waitForElementClickable(location, WAIT_TIME_OUT);
                if (element == null) {
                    return false;
                } else {
                    //element.click();
                    element.clear();
                    // write input by selenium.
                    element.sendKeys(data);
                    Thread.sleep(100);
                    if (Common.verifyWriteInput(element, data)) {
                        return true;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                LogUtils.infoToFile("Exception write input " + e);
            }
            return false;
        } else {
            return false;
        }
    }

    public boolean click(By location) throws InterruptedException {
        WaitUtils wait = new WaitUtils(driver);
        WebElement element = wait.waitForElementClickable(location, Constants.TIME_OUT_TESTSCRIPT);
        if (element == null) {
            return false;
        }
        element = wait.waitForElementClickable(location, Constants.TIME_OUT_TESTSCRIPT);
        JavascriptExecutor executor = (JavascriptExecutor) driver;
        executor.executeScript("window.scrollTo(0, " + element.getLocation().y + ");");
        executor.executeScript("arguments[0].click();", element);
        return true;
    }

    public boolean selectDropdown(ElementDto elementDto, String data) {
        WaitUtils waitUtils = WaitUtils.getWaitUtils(driver);
        if (waitUtils.waitForLoad()) {
            By location = Common.getLocation(elementDto);
            WebElement element = waitUtils.waitForElementClickable(location, Constants.TIME_OUT_TESTSCRIPT);

            Select drpCountry = new Select(element);
            drpCountry.selectByVisibleText(data);
            String text = (String) ((JavascriptExecutor) driver)
                    .executeScript("return arguments[0].options[arguments[0].selectedIndex].text;", element);
            return data.equals(text);
        }
        return false;
    }

    public boolean uploadFile(ElementDto elementDto, String data) {
        try {
            WaitUtils waitUtils = WaitUtils.getWaitUtils(driver);
            if (waitUtils.waitForLoad()) {
                File file = new File(data);
                if (!file.exists()) {
                    LogUtils.infoToFile(" File not found:" + data);
                    return false;
                } else {
                    data = file.getAbsolutePath();
                    By location = Common.getLocation(elementDto);
                    WebElement uploadElement = waitUtils.waitForElementClickable(location,
                            Constants.TIME_OUT_TESTSCRIPT);
                    uploadElement.sendKeys(data);
                    Thread.sleep(1000);
                    Robot robot = new Robot();
                    robot.keyPress(KeyEvent.VK_ESCAPE);
                    robot.keyRelease(KeyEvent.VK_ESCAPE);
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;

    }

    public boolean clearTextInput(ElementDto elementDto, String data) {
        try {
            WaitUtils waitUtils = WaitUtils.getWaitUtils(driver);
            if (waitUtils.waitForLoad()) {
                By location = Common.getLocation(elementDto);
                WebElement element = waitUtils.waitForElementClickable(location, Constants.TIME_OUT_TESTSCRIPT);
                element.clear();
                String value = element.getAttribute("value");
                return value == null || value.equals("");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
