package com.autotest.app;

import java.util.List;

import com.autotest.adapter.TestStepAdapter;
import com.autotest.commander.Executor;
import com.autotest.dto.TestStepDto;
import com.autotest.utils.ExcelUtils;
import com.autotest.utils.LogUtils;

public class Application {

    public static void main(String[] args) {
        LogUtils.configLog4j();
        ExcelUtils excelUtils = new ExcelUtils("F:\\workspace\\Automation_Selenium\\TestSuite.xlsx");
        TestStepAdapter testStepAdapter = new TestStepAdapter();
        List<TestStepDto> testStepList = testStepAdapter.readTestStep(excelUtils, "TestSuite1");

        Executor executor = new Executor();
        executor.start(testStepList);
    }

}
