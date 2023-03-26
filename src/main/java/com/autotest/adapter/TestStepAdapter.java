/*
* TestStepAdapter
* Version info
* Copyright @FPT Software
*/

package com.autotest.adapter;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Sheet;

import com.autotest.dto.ElementDto;
import com.autotest.dto.TestStepDto;
import com.autotest.utils.Constants;
import com.autotest.utils.ExcelUtils;

public class TestStepAdapter {

    private ElementDto getElement(String location) {
        if (location == null || location.equals("")) {
            return null;
        }
        ElementDto element = new ElementDto();
        String[] data = location.split("=", 2);
        if (data[0].trim().equals(Constants.LOCATOR_BY_ID)) {
            element.setLocatorType(Constants.LOCATOR_BY_ID);
        } else if (data[0].trim().equals(Constants.LOCATOR_BY_XPATH)) {
            element.setLocatorType(Constants.LOCATOR_BY_XPATH);
        }
        element.setLocatorValue(data[1].trim());
        return element;
    }

    public List<TestStepDto> readTestStep(ExcelUtils excel, String sheetName) {
        TestStepDto testStep = null;
        ElementDto element = null;
        List<TestStepDto> testSteps = new ArrayList<>();
        Sheet stepSheet = excel.getSheetByName(sheetName);
        excel.setCurrentWorkSheet(stepSheet);
        int numberOfRows = stepSheet.getPhysicalNumberOfRows();
        for (int i = 2; i <= numberOfRows; i++) {
            testStep = new TestStepDto();
            testStep.setId(excel.getCellValueAsString("A" + i));
            testStep.setKeyword(excel.getCellValueAsString("B" + i));
            testStep.setInputData(excel.getCellValueAsString("D" + i));
            String location = excel.getCellValueAsString("E" + i);
            testStep.setDescription(excel.getCellValueAsString("C" + i));
            element = getElement(location);
            testStep.setElement(element);
            testSteps.add(testStep);
        }
        return testSteps;
    }
}
