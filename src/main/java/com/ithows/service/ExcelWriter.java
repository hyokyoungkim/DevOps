/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ithows.service;

import com.ithows.util.ExcelUtils;

import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFFormulaEvaluator;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;

/**
 *
 * @author ksyuser
 */
public class ExcelWriter {
 
    public static boolean DumpExcel(List dataList, Map groupInfo, String fileName ) {

        boolean result = false;
        
        try{

            // 1. 템플릿 준비
            String templatePath = "bap_template.xls"; 
            Workbook wkb = ExcelUtils.createTemplateWorkBook(templatePath);
            
            // 2. 데이터 쓰기
            setData(wkb, dataList, groupInfo, fileName);
            result = true;
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return result;
    }
    
     /**
     * 엑셀에 검색된 데이터를 채우는 함수 
     * 
     * @param workbook
     * @param workList
     * @param groupInfo
     * @param fileName 
     */
    public static void setData(Workbook workbook, List workList, Map groupInfo, String fileName) 
    {
        
        Map rec;
       
        createBlankRow(workbook, "Data", workList.size(), 2);
        
        // Data 입력
        for (int i = 0; i < workList.size(); i++) {
            
            rec = (Map) workList.get(i);;

            ExcelUtils.setCellValue(workbook, "Data", 2+i, 0, rec.get("workNo").toString() );
            ExcelUtils.setCellValue(workbook, "Data", 2+i, 1, rec.get("workManNo").toString() );
            ExcelUtils.setCellFormular(workbook, "Data", 2+i, 2, "IF(ISBLANK(K" +(3+i) +"),0, (K"+ (3+i) +"-G"+ (3+i)+")*1440)" );
            ExcelUtils.setCellFormular(workbook, "Data", 2+i, 3, "IF(ISBLANK(M" +(3+i) +"),0, (H"+ (3+i) +"-M"+ (3+i)+")*1440)" );
            ExcelUtils.setCellValue(workbook, "Data", 2+i, 4, rec.get("ES").toString() );
        }

        
        // Meta Data 입력 
        createBlankRow(workbook, "MetaData",1 , 1);
        ExcelUtils.setCellValue(workbook, "MetaData", 1, 0, Integer.parseInt(groupInfo.get("ManCount").toString()));
        
        HSSFFormulaEvaluator.evaluateAllFormulaCells((HSSFWorkbook)workbook);
        ExcelUtils.saveExcel(workbook, fileName);
    }
    
    public static void createBlankRow(Workbook workbook, String sheetName, int size, int srcRow) {
        for (int i = 0; i < size; i++) {
            ExcelUtils.copyRow(workbook, sheetName, srcRow, srcRow+1);
        }
    }
}
