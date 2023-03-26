package com.autotest.selenium;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.autotest.utils.Constants;
import com.autotest.utils.LogUtils;

public class WaitUtils {

    private static WaitUtils waiUtils;
    private static WebDriver driver = null;

    /** Default wait time for an element. 7 seconds. */
    public static final int DEFAULT_WAIT_4_ELEMENT = 7;
    /**
     * Default wait time for a page to be displayed. 12 seconds. The average webpage
     * load time is 6 seconds in 2018. Based on your tests, please set this value.
     * "0" will nullify implicitlyWait and speed up a test.
     */
    private int timeOut = 10;

    public WaitUtils(WebDriver driver) {
        super();
        WaitUtils.driver = driver;
    }

    /**
     * Initialize only one instance of Waiter to follow Singleton design pattern
     */
    public static synchronized WaitUtils getWaitUtils(WebDriver driver) {
        if (waiUtils == null) {
            waiUtils = new WaitUtils(driver);
        }
        WaitUtils.driver = driver;
        return waiUtils;
    }

    /**
     * Wait for the element to be present in the DOM, and displayed on the page. And
     * returns the first WebElement using the given method.
     * 
     * @param By  selector to find the element
     * @param int The time in seconds to wait until returning a failure
     * 
     * @return WebElement the first WebElement using the given method, or null (if
     *         the timeout is reached)
     */
    public WebElement waitForElement(final By by, int timeOutInSeconds) {
        WebElement element;
        try {
            // To use WebDriverWait(), we would have to nullify
            // implicitlyWait().
            // Because implicitlyWait time also set "driver.findElement()" wait
            // time.
            // info from:
            // https://groups.google.com/forum/?fromgroups=#!topic/selenium-users/6VO_7IXylgY
            driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS); // nullify
                                                                            // implicitlyWait()

            WebDriverWait wait = new WebDriverWait(driver, timeOutInSeconds);
            element = wait.until(ExpectedConditions.visibilityOfElementLocated(by));

            driver.manage().timeouts().implicitlyWait(timeOut, TimeUnit.SECONDS); // reset
                                                                                  // implicitlyWait
            return element; // return the element
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Wait for the element to be present in the DOM, regardless of being displayed
     * or not. And returns the first WebElement using the given method.
     * 
     * @param By  selector to find the element
     * @param int The time in seconds to wait until returning a failure
     * 
     * @return WebElement the first WebElement using the given method, or null (if
     *         the timeout is reached)
     */
    public WebElement waitForElementPresent(final By by, int timeOutInSeconds) {
        WebElement element;
        try {
            driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS); // nullify
                                                                            // implicitlyWait()

            WebDriverWait wait = new WebDriverWait(driver, timeOutInSeconds);
            element = wait.until(ExpectedConditions.presenceOfElementLocated(by));

            driver.manage().timeouts().implicitlyWait(timeOut, TimeUnit.SECONDS); // reset
                                                                                  // implicitlyWait
            return element; // return the element
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Wait for the List<WebElement> to be present in the DOM, regardless of being
     * displayed or not. Returns all elements within the current page DOM.
     * 
     * @param By  selector to find the element
     * @param int The time in seconds to wait until returning a failure
     * 
     * @return List<WebElement> all elements within the current page DOM, or null
     *         (if the timeout is reached)
     */
    public List<WebElement> waitForListElementsPresent(final By by, int timeOutInSeconds) {
        List<WebElement> elements;
        try {
            driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS); // nullify
                                                                            // implicitlyWait()

            WebDriverWait wait = new WebDriverWait(driver, timeOutInSeconds);
            wait.until((new ExpectedCondition<Boolean>() {
                public Boolean apply(WebDriver driverObject) {
                    return areElementsPresent(by);
                }
            }));

            elements = driver.findElements(by);
            driver.manage().timeouts().implicitlyWait(timeOut, TimeUnit.SECONDS); // reset
                                                                                  // implicitlyWait
            return elements; // return the element
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Wait until an element disappear on the web page, return true when done
     * 
     * This method is to deal with loading image, wait until loading image is
     * disappear
     * 
     * @param locator selector to find the element (loading image)
     * 
     * @param int     The time in seconds to wait until returning a failure
     * 
     * @return true if element disappear during wait time period, or false if
     *         element still appear
     * 
     * @author Thuy Cao
     */

    public boolean waitUntilElementDisappear(final By by, int timeOutInSeconds) {
        // Wait for element present

        try {
            driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS); // nullify
                                                                            // implicitlyWait()

            WebDriverWait wait = new WebDriverWait(driver, timeOutInSeconds);
            wait.until(ExpectedConditions.presenceOfElementLocated(by));

            driver.manage().timeouts().implicitlyWait(timeOut, TimeUnit.SECONDS); // reset

            // Wait for element disappear
            long ctime = System.currentTimeMillis();
            while ((timeOutInSeconds * 1000 > System.currentTimeMillis() - ctime)) {
                List<WebElement> elementList = driver.findElements(by);
                if ((!elementList.isEmpty())) {
                    Thread.sleep(150);
                }
                // element is Disappear within timeout
                else
                    return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Wait for an element to appear on the refreshed web-page. And returns the
     * first WebElement using the given method.
     * 
     * This method is to deal with dynamic pages.
     * 
     * Some sites I (Mark) have tested have required a page refresh to add
     * additional elements to the DOM. Generally you (Chon) wouldn't need to do this
     * in a typical AJAX scenario.
     * 
     * @param locator selector to find the element
     * @param int     The time in seconds to wait until returning a failure
     * 
     * @return WebElement the first WebElement using the given method, or null(if
     *         the timeout is reached)
     * 
     * @author Mark Collin
     */
    public WebElement waitForElementRefresh(final By by, int timeOutInSeconds) {
        WebElement element;
        try {
            driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS); // nullify
                                                                            // implicitlyWait()
            new WebDriverWait(driver, timeOutInSeconds) {
            }.until(new ExpectedCondition<Boolean>() {

                public Boolean apply(WebDriver driverObject) {
                    driverObject.navigate().refresh(); // refresh the page
                                                       // ****************
                    return isElementPresentAndDisplay(by);
                }
            });
            element = driver.findElement(by);
            driver.manage().timeouts().implicitlyWait(timeOut, TimeUnit.SECONDS); // reset
                                                                                  // implicitlyWait
            return element; // return the element
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Wait for the Text to be present in the given element, regardless of being
     * displayed or not.
     * 
     * @param locator selector of the given element, which should contain the text
     * @param String  The text we are looking
     * @param int     The time in seconds to wait until returning a failure
     * 
     * @return boolean
     */
    public boolean waitForTextPresent(final By by, final String text, int timeOutInSeconds) {
        boolean isPresent = false;
        try {
            driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS); // nullify
                                                                            // implicitlyWait()
            new WebDriverWait(driver, timeOutInSeconds) {
            }.until(new ExpectedCondition<Boolean>() {

                public Boolean apply(WebDriver driverObject) {
                    return isTextPresent(by, text); // is the Text
                                                    // in the
                                                    // DOM
                }
            });
            isPresent = isTextPresent(by, text);
            driver.manage().timeouts().implicitlyWait(timeOut, TimeUnit.SECONDS); // reset
                                                                                  // implicitlyWait
            return isPresent;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Waits for the Condition of JavaScript.
     * 
     * @param String The javaScript condition we are waiting. e.g. "return
     *               (xmlhttp.readyState >= 2 && xmlhttp.status == 200)"
     * @param int    The time in seconds to wait until returning a failure
     * 
     * @return boolean true or false(condition fail, or if the timeout is reached)
     **/
    public boolean waitForJavaScriptCondition(final String javaScript, int timeOutInSeconds) {
        boolean jscondition = false;
        try {
            driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS); // nullify
                                                                            // implicitlyWait()
            new WebDriverWait(driver, timeOutInSeconds) {
            }.until(new ExpectedCondition<Boolean>() {

                public Boolean apply(WebDriver driverObject) {
                    return (Boolean) ((JavascriptExecutor) driverObject).executeScript(javaScript);
                }
            });
            jscondition = (Boolean) ((JavascriptExecutor) driver).executeScript(javaScript);
            driver.manage().timeouts().implicitlyWait(timeOut, TimeUnit.SECONDS); // reset
                                                                                  // implicitlyWait
            return jscondition;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Waits for the completion of Ajax jQuery processing by checking "return
     * jQuery.active == 0" condition.
     * 
     * @param int - The time in seconds to wait until returning a failure
     * 
     * @return boolean true or false(condition fail, or if the timeout is reached)
     */
    public boolean waitForJQueryProcessing(int timeOutInSeconds) {
        boolean jQcondition = false;
        try {
            driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS); // nullify
                                                                            // implicitlyWait()
            new WebDriverWait(driver, timeOutInSeconds) {
            }.until(new ExpectedCondition<Boolean>() {
                public Boolean apply(WebDriver driverObject) {
                    return (Boolean) ((JavascriptExecutor) driverObject).executeScript("return jQuery.active == 0");
                }
            });
            jQcondition = (Boolean) ((JavascriptExecutor) driver).executeScript("return jQuery.active == 0");
            driver.manage().timeouts().implicitlyWait(timeOut, TimeUnit.SECONDS); // reset
                                                                                  // implicitlyWait
            return jQcondition;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jQcondition;
    }

    /**
     * Coming to implicit wait, If you have set it once then you would have to
     * explicitly set it to zero to nullify it -
     */
    public void nullifyImplicitWait() {
        driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS); // nullify
                                                                        // implicitlyWait()
    }

    /**
     * Set driver implicitlyWait() time.
     */
    public void setImplicitWait(int waitTime_InSeconds) {
        driver.manage().timeouts().implicitlyWait(waitTime_InSeconds, TimeUnit.SECONDS);
    }

    /**
     * Reset ImplicitWait. To reset ImplicitWait time you would have to explicitly
     * set it to zero to nullify it before setting it with a new time value.
     */
    public void resetImplicitWait() {
        driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS); // nullify
                                                                        // implicitlyWait()
        driver.manage().timeouts().implicitlyWait(timeOut, TimeUnit.SECONDS); // reset
                                                                              // implicitlyWait
    }

    /**
     * Reset ImplicitWait.
     * 
     * @param int - a new wait time in seconds
     */
    public void resetImplicitWait(int newWaittime_InSeconds) {
        driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS); // nullify
                                                                        // implicitlyWait()
        driver.manage().timeouts().implicitlyWait(newWaittime_InSeconds, TimeUnit.SECONDS); // reset
                                                                                            // implicitlyWait
    }

    /**
     * Checks if the text is present in the element.
     * 
     * @param by   - selector to find the element that should contain text
     * @param text - The Text element you are looking for
     * @return true or false
     */
    private boolean isTextPresent(By by, String text) {
        try {
            return driver.findElement(by).getText().contains(text);
        } catch (NullPointerException e) {
            return false;
        }
    }

    /**
     * Checks if the element is in the DOM, regardless of being displayed or not.
     * 
     * @param by - selector to find the element
     * @return boolean
     */
    @SuppressWarnings("unused")
    private static boolean isElementPresent(By by) {
        try {
            driver.findElement(by);// if it does not find the element throw
                                   // NoSuchElementException, which calls
                                   // "catch(Exception)" and returns false;
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    /**
     * Checks if the List<WebElement> are in the DOM, regardless of being displayed
     * or not.
     * 
     * @param by - selector to find the element
     * @return boolean
     */
    private boolean areElementsPresent(By by) {
        try {
            driver.findElements(by);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    /**
     * Checks if the element is in the DOM and displayed.
     * 
     * @param driver - The driver object to use to perform this element search
     * @param by     - selector to find the element
     * @return boolean
     */
    private boolean isElementPresentAndDisplay(By by) {
        try {
            return driver.findElement(by).isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public boolean waitForLoad() {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Constants.TIME_OUT_TESTSCRIPT);
            wait.until(CustomExpectedConditions.pageLoadComplete());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.infoToFile("Exception wait for page load " + e);
            return false;
        }
    }

    public WebElement waitForElementClickable(final By by, int timeOutInSeconds) {
        WebElement element;
        try {
            driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
            WebDriverWait wait = new WebDriverWait(driver, timeOutInSeconds);
            element = wait.until(ExpectedConditions.elementToBeClickable(by));
            driver.manage().timeouts().implicitlyWait(timeOut, TimeUnit.SECONDS);
            return element;
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.infoToFile("Exception wait element " + e);
        }
        return null;
    }

}