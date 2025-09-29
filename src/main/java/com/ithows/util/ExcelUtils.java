package com.ithows.util;

import com.ithows.BaseDebug;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.GeneralPath;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import javax.imageio.ImageIO;
import net.sf.jxls.util.Util;

import org.apache.commons.io.IOUtils;

import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.json.JSONArray;


/**
 *
 * @author ksyuser
 */
public class ExcelUtils {


    public static Workbook createTemplateWorkBook(String fileName) {
        HSSFWorkbook wkb = null;
        InputStream bs = null;
        File f = new File(fileName);
        try {
            bs = new BufferedInputStream(new FileInputStream(f.getAbsolutePath()));
            //wkb = WorkbookFactory.create(bs);
            wkb = new HSSFWorkbook(bs);
        } catch (Exception e) {
            BaseDebug.log(e, "엑셀로딩 에러", fileName);
        } finally {
            try {
                bs.close();
            } catch (IOException e2) {
                BaseDebug.log(e2);
            }
        }
        return wkb;
    }


    public static String saveExcel(Workbook workbook, String fileName) {
        try {
            FileOutputStream fileOut = new FileOutputStream(fileName);
            workbook.write(fileOut);
            fileOut.close();
        } catch (IOException e) {
            BaseDebug.log(e, "엑셀 파일 생성 에러");
            fileName = null;
        }
        if (fileName != null) {
            return new File(fileName).getName();
        } else {
            return null;
        }
    }
    
    public static void setCellValue(Workbook workbook, String sheetName, int irow, int icol, String value) {
        
        Sheet worksheet = workbook.getSheet(sheetName);
        
        if (value == null || value.isEmpty()) {
            return;//데이터가 없다면 빠져라
        }
        Row row = worksheet.getRow(irow);
        Cell cell = row.getCell(icol);
        cell.setCellValue(value);
    }
    

    public static void setCellBgColor(Workbook workbook, String sheetName, int irow, int icol, short color) { //셀 색상 변경 함수
        
        HashMap<String, CellStyle> map = new HashMap<String, CellStyle>();
        Sheet worksheet = workbook.getSheet(sheetName); // String sheetName,
        Row row = worksheet.getRow(irow);
        Cell cell = row.getCell(icol);
        CellStyle oldStyle = cell.getCellStyle(); //원래 있던 셀의 스타일을 얻어서 oldStyle에 저장한다.

        String key = icol + "_" + color; //몇번째 열의 색상이 몇번인지를 가지고 key만들기

        if (map.containsKey(key)) { //이셀의 key값이 map에 들어있다면
            cell.setCellStyle(map.get(key));  //그대로 사용한다.
        } else {   //그렇지 않다면
            CellStyle newStyle = workbook.createCellStyle(); //엑셀에 새로운 스타일로 셀 만들기
            newStyle.cloneStyleFrom(oldStyle); //oldStyle의 스타일을 복사하여 새 스타일 셀 만든다.
            newStyle.setFillForegroundColor(color); // 1. 1~3을 같이 사용하여야 셀의 배경색을 보여줄 수 있다.
            newStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            newStyle.setBorderBottom(BorderStyle.THIN);  //셀의 아래쪽 테두리가 없어지는 현상을 방지하기 위해 아래쪽 테두리를 다시 그려준다.
            cell.setCellStyle(newStyle); //새로운 스타일의 셀을 세팅해준다.
            map.put(key, newStyle); // map에 key와  newStyle을 넣어준다.
        }
    }

    public static void setCellStyle(Workbook workbook, String sheetName, int irow, int icol, CellStyle newStyle) { //셀 색상 변경 함수
        
        Sheet worksheet = workbook.getSheet(sheetName); // String sheetName,
        Row row = worksheet.getRow(irow);
        Cell cell = row.getCell(icol);
        cell.setCellStyle(newStyle);
        /*
         * CellStyle oldStyle = cell.getCellStyle(); //원래 있던 셀의 스타일을 얻어서
         * oldStyle에 저장한다.
         * oldStyle.setFillBackgroundColor(newStyle.getFillBackgroundColor());
         * oldStyle.setFillForegroundColor(newStyle.getFillForegroundColor());
         * oldStyle.setFillPattern(newStyle.getFillPattern());
         */
    }

    public static void setCellValue(Workbook workbook, String sheetName, int irow, int icol, int value) {
        Sheet worksheet = workbook.getSheet(sheetName); // 
        Row row = worksheet.getRow(irow);
        Cell cell = row.getCell(icol);
        cell.setCellValue((double) value);
    }

    public static void setCellValue(Workbook workbook, String sheetName, int irow, int icol, float value) {
        Sheet worksheet = workbook.getSheet(sheetName); // 
        Row row = worksheet.getRow(irow);
        Cell cell = row.getCell(icol);
        cell.setCellValue((double) value);
    }

    public static void setCellFormular(Workbook workbook,  String sheetName, int irow, int icol, String formula) {
        Sheet worksheet = workbook.getSheet(sheetName); //
        Row row = worksheet.getRow(irow);
        Cell newCell = row.getCell(icol);
        newCell.setCellType(CellType.FORMULA);
        newCell.setCellFormula(formula);
    }

    public static void setStampImage(Workbook workbook, String stampFileName, int sheetNumber, int x1, int y1, int x2, int y2, int col1, int row1, int col2, int row2) throws FileNotFoundException, IOException {
        //도장이미지
        File f = new File(stampFileName);
        if (f.exists()) {
            HSSFWorkbook wrk = (HSSFWorkbook) workbook;
            HSSFSheet sheet = wrk.getSheetAt(sheetNumber);
            InputStream is = new FileInputStream(stampFileName);
            byte[] bytes = IOUtils.toByteArray(is);
            int index = wrk.addPicture(bytes, Workbook.PICTURE_TYPE_JPEG);
            is.close();

            HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
            HSSFClientAnchor anchor = new HSSFClientAnchor(x1, y1, x2, y2, (short) col1, row1, (short) col2, row2);

            //HSSFClientAnchor anchor = new HSSFClientAnchor(568, 132, 991, 105, (short) 34, 2, (short) 34, 4);
            //HSSFClientAnchor anchor = new HSSFClientAnchor(634, 17, 1009, 47, (short) 5, 2, (short) 5, 5);
            anchor.setAnchorType(ClientAnchor.AnchorType.DONT_MOVE_DO_RESIZE );
            patriarch.createPicture(anchor, index);
        }
    }

  
    public static void copyRow(Workbook workbook, String sheetName, int oldRowNum, int newRowNum) {
        
        Sheet worksheet = workbook.getSheet(sheetName); // 
        // Get the source / new row
        Row newRow = worksheet.getRow(newRowNum);
        Row oldRow = worksheet.getRow(oldRowNum);//복사할 원본줄
        if (newRow != null) {
            worksheet.shiftRows(newRowNum, worksheet.getLastRowNum(), 1);//********************
        } else {
            newRow = worksheet.createRow(newRowNum);
        }
        Util.copyRow(worksheet, oldRow, newRow);
    }
}
