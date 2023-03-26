package com.autotest.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtils {
    private static Logger log = Logger.getLogger(ExcelUtils.class.getName());
    
    private Workbook wb;
    private Sheet sheet;
    private String fileName;
    
    public ExcelUtils(String excel_file_name) {
        fileName = excel_file_name;
        FileInputStream fis;
        try {
            log.info("Reading file: " + excel_file_name);
            fis = new FileInputStream(excel_file_name);
            wb = new XSSFWorkbook(fis);
        } catch (FileNotFoundException e1) {
            log.debug("File " + excel_file_name + " not found");
        } catch (IOException e) {
            log.debug("Cannot open file: " + excel_file_name);
            e.printStackTrace();
        }
        
    }
    
    public ExcelUtils() {
    };
    
    public String getCurrentFileName() {
        return fileName;
    }
    
    public void setCurrentWorkSheet(Sheet sheet) {
        this.sheet = sheet;
    }
    
    // This method must be call first if ExcelHelper is initialized using empty
    // constructor
    public void parseFile(String excel_file_name) {
        fileName = excel_file_name;
        FileInputStream fis;
        try {
            log.info("Reading file: " + excel_file_name);
            fis = new FileInputStream(excel_file_name);
            wb = new XSSFWorkbook(fis);
        } catch (FileNotFoundException e1) {
            System.out.println("Excel file not found:" + excel_file_name);
            log.debug("File " + excel_file_name + " not found");
        } catch (IOException e) {
            e.printStackTrace();
            log.debug("Cannot open file: " + excel_file_name);
        }
        
    }
    
    public Sheet getSheetByName(String sheetName) {
        return wb.getSheet(sheetName);
    }
    
    public List<String> getColumnValues(String sheetName, String columnIndex,
            boolean hasHeader) {
        List<String> values = new ArrayList<String>();
        sheet = getSheetByName(sheetName);
        int numberOfRows = sheet.getPhysicalNumberOfRows();
        int startRow = 1;
        if (hasHeader) {
            startRow = 2;
        }
        
        for (int i = startRow; i <= numberOfRows; i++) {
            String tempStr = getCellValueAsString(sheetName, columnIndex + i);
            values.add(tempStr);
        }
        
        return values;
    }
    
    // Get a cell value and pass into a string
    public String getCellValueAsString(String sheet_name,
            String cell_reference) {
        String ret = "";
        sheet = wb.getSheet(sheet_name);
        ret = getCellValueAsString(cell_reference);
        return ret;
    }
    
    public String getCellValueAsString(String cell_reference) {
        String ret = "";
        CellReference cellReference = new CellReference(cell_reference);
        Row row = sheet.getRow(cellReference.getRow());
        Cell cell = row.getCell(cellReference.getCol());
        log.info("Reading cell " + cell_reference + " in the sheet "
                + this.sheet.getSheetName());
        if (cell != null) {
            switch (cell.getCellType()) {
                case Cell.CELL_TYPE_BOOLEAN:
                    ret = String.valueOf(cell.getBooleanCellValue());
                    break;
                case Cell.CELL_TYPE_NUMERIC:
                    ret = String.valueOf(cell.getNumericCellValue());
                    break;
                case Cell.CELL_TYPE_STRING:
                    ret = cell.getStringCellValue();
                    break;
                case Cell.CELL_TYPE_BLANK:
                    break;
                case Cell.CELL_TYPE_ERROR:
                    ret = "Error cell, please recheck " + cell_reference
                            + " on Microsoft Excel";
                    break;
                default:
                    break;
            }
        }
        log.info("Cell " + cell_reference + " value is " + ret);
        return ret;
    }
}
