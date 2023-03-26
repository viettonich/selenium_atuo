/*
* TestStepDto
* Version 1.0
* Copyright @FPT Software
*/

package com.autotest.dto;

public class TestStepDto {
    private String id;
    private String description;
    private ElementDto element;
    private String inputData;
    private String keyword;
    private String testStepResult;
    private String snapShot;

    public TestStepDto() {
        super();
    }

    public TestStepDto(String id, String description, String screenId, String screenName, ElementDto element,
            String inputData, String keyword) {
        super();
        this.id = id;
        this.description = description;
        this.element = element;
        this.inputData = inputData;
        this.keyword = keyword;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ElementDto getElement() {
        return element;
    }

    public void setElement(ElementDto element) {
        this.element = element;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getTestStepResult() {
        return testStepResult;
    }

    public void setTestStepResult(String testStepResult) {
        this.testStepResult = testStepResult;
    }

    public String getSnapShot() {
        return snapShot;
    }

    public void setSnapShot(String snapShot) {
        this.snapShot = snapShot;
    }

    public String getInputData() {
        return inputData;
    }

    public void setInputData(String inputData) {
        this.inputData = inputData;
    }
}
