/*
* InitValue
* Version 1.0
* Copyright @FPT Software
*/

package com.autotest.commander;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.apache.commons.lang.exception.ExceptionUtils;

import com.autotest.dto.TestStepDto;
import com.autotest.selenium.Browser;
import com.autotest.utils.Constants;
import com.autotest.utils.LogUtils;

public class Executor {

    private TestActions testActions = new TestActions();

    private String rootPath = System.getProperty("user.dir");

    public Executor() {
    }

    public void start(List<TestStepDto> testStepDtoList) {
        for (int i = 0; i < testStepDtoList.size(); i++) {
            executeKeyword(testStepDtoList.get(i));
        }
    }

    public void executeKeyword(TestStepDto testStep) {
        Method[] method = testActions.getClass().getMethods();
        for (int index = 0; index < method.length; index++) {
            if (method[index].getName().equalsIgnoreCase(testStep.getKeyword())) {
                try {
                    LogUtils.infoToFile("[STAT TEST STEP][" + testStep.getId() + "][" + testStep.getKeyword() + "]");
                    if (testStep.getKeyword().equals("snapshot")) {
                        String path = rootPath + "//" + testStep.getId() + "_" + getCurrentDate() + ".png";
                        testStep.setInputData(path);
                        testStep.setSnapShot(path);
                    }
                    String result = (String) method[index].invoke(testActions, testStep.getElement(),
                            testStep.getInputData());
                    if (result.contains(Constants.FAILED)) {
                        testStep.setTestStepResult(Constants.FAILED);
                        String path = rootPath + "//" + testStep.getId() + "_FAIL_" + getCurrentDate() + ".png";
                        Browser.getBrowser().takeScreenshot(path);
                        testStep.setSnapShot(path);
                    } else {
                        testStep.setTestStepResult(Constants.PASS);
                    }
                    LogUtils.infoToFile(
                            "[END TEST STEP][" + testStep.getId() + "][" + testStep.getTestStepResult() + "]");
                    Thread.sleep(1000L);
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                    testStep.setTestStepResult(Constants.FAILED);
                    LogUtils.infoToFile(ExceptionUtils.getStackTrace(e));
                    LogUtils.infoToFile(
                            "[END TEST STEP][" + testStep.getId() + "][" + testStep.getTestStepResult() + "]");
                } catch (Exception e) {
                    e.printStackTrace();
                    LogUtils.infoToFile(ExceptionUtils.getStackTrace(e));
                    LogUtils.infoToFile("END TEST STEP: " + testStep.getId());
                }
                break;
            }
        }
    }

    private String getCurrentDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmm");
        return LocalDateTime.now().format(formatter);
    }
}
