package com.autotest.dto;

public class ElementDto {
    private String elementName;
    private String locatorType;
    private String locatorValue;
    
    public String getLocatorType() {
        return locatorType;
    }
    
    public void setLocatorType(String locatorType) {
        this.locatorType = locatorType;
    }
    
    public String getLocatorValue() {
        return locatorValue;
    }
    
    public void setLocatorValue(String locatorValue) {
        this.locatorValue = locatorValue;
    }
    
    public String getElementName() {
        return elementName;
    }
    
    public void setElementName(String elementName) {
        this.elementName = elementName;
    }
}
