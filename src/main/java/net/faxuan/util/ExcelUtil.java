package net.faxuan.util;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.*;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

/**
 * Created by song on 2018/12/25.
 */
public class ExcelUtil {
    private static String SHEET1="接口";
    private static String SHEET2="用例";
    private static String SHEET3="数据库检查";
    private static String SHEET4="系统设置";
    private String host = "";


    @Test
    public void testReadExcel() throws IOException {
        System.out.println(readExcel("D:/test.xlsb.xlsx"));
    }

    /**
     * 读取excel信息 返回excel对象
     * @param excelPath
     * @return
     * @throws Exception
     */
    public Map<String, Map<Integer, List<String>>> readExcel(String excelPath) throws FileNotFoundException,IOException{

//        Excel excel = new Excel();
        //记录所有sheet信息
        Map<String,Map<Integer,List<String>>> sheets = new HashMap<>();
        //读取excel
        if (excelPath.contains(".xlsx")) {
//            System.out.println("新版本excel文件");
            XSSFWorkbook workbook=new XSSFWorkbook(new FileInputStream(new File(excelPath)));
            XSSFSheet sheet=null;
            //记录当前sheet的所有行信息
            Map<Integer,List<String>> she = new HashMap<>();
            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {//获取每个Sheet表
                sheet=workbook.getSheetAt(i);
                for (int j = 0; j < sheet.getLastRowNum(); j++) {//获取每行
                    //记录当前行的信息
                    List<String> line = new ArrayList<>();
                    XSSFRow row=sheet.getRow(j);
                    for (int k = 0; k < row.getLastCellNum(); k++) {//获取每个单元格
                        //蒋当前单元格信息记录
                        line.add(getCellStringValue(row.getCell(k)));
//                        System.out.print(row.getCell(k)+"\t");
                    }
                    she.put(j,line);
                }
                sheets.put(workbook.getSheetAt(i).getSheetName(),she);
            }
        } else if (excelPath.substring(excelPath.length()-4).equals(".xls")) {
//            System.out.println("老版本excel文件");
            HSSFWorkbook workbook=new HSSFWorkbook(new FileInputStream(new File(excelPath)));
            HSSFSheet sheet=null;
            //记录当前sheet的所有行信息
            Map<Integer,List<String>> she = new HashMap<>();
            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {//获取每个Sheet表
                sheet=workbook.getSheetAt(i);
                for (int j = 0; j < sheet.getLastRowNum(); j++) {//获取每行
                    //记录当前行的信息
                    List<String> line = new ArrayList<>();
                    HSSFRow row=sheet.getRow(j);
                    for (int k = 0; k < row.getLastCellNum(); k++) {//获取每个单元格
//                        System.out.print(row.getCell(k)+"\t");
                        //蒋当前单元格信息记录
                        line.add(getCellStringValue(row.getCell(k)));
                    }
                    she.put(j,line);
                }
                sheets.put(workbook.getSheetAt(i).getSheetName(),she);
            }
        } else {
            System.err.println("非excel文件");
        }

//        excel.setSheets(sheets);
        return sheets;

    }


    /**
     * 将获取的所有类型值都转换为String
     * 如果当前字段为空 返回空字符串
     * @param cell
     * @return
     */
    private String getCellStringValue(HSSFCell cell) {
        String cellValue = "";
        try {
            switch (cell.getCellTypeEnum()) {
                case STRING:
                    cellValue = cell.getStringCellValue();
                    if(cellValue.trim().equals("")||cellValue.trim().length()<=0)
                        cellValue="";
                    break;
                case NUMERIC:
                    cellValue = String.valueOf(new Double(cell.getNumericCellValue()).intValue());
                    break;
                case FORMULA:
                    cell.setCellType(CellType.NUMERIC);
                    cellValue = String.valueOf(cell.getNumericCellValue());
                    break;
                case BLANK:
                    break;
                case BOOLEAN:
                    break;
                case ERROR:
                    break;
                default: break;
            }
        } catch (NullPointerException npe) {
            return "";
        }
        return cellValue;
    }

    /**
     * 将获取的所有类型值都转换为String
     * 如果当前字段为空 返回空字符串
     * @param cell
     * @return
     */
    private String getCellStringValue(XSSFCell cell) {
        String cellValue = "";
        try {
            switch (cell.getCellTypeEnum()) {
                case STRING:
                    cellValue = cell.getStringCellValue();
                    if(cellValue.trim().equals("")||cellValue.trim().length()<=0)
                        cellValue="";
                    break;
                case NUMERIC:
                    cellValue = String.valueOf(new Double(cell.getNumericCellValue()).intValue());
                    break;
                case FORMULA:
                    cell.setCellType(CellType.NUMERIC);
                    cellValue = String.valueOf(cell.getNumericCellValue());
                    break;
                case BLANK:
                    break;
                case BOOLEAN:
                    break;
                case ERROR:
                    break;
                default: break;
            }
        } catch (NullPointerException npe) {
            return "";
        }
        return cellValue;
    }


class Excel {

        private Map<String, Map<Integer, List<String>>> sheets;

        private Map<Integer,List<String>> calls;

        public Map<String, Map<Integer, List<String>>> getSheets() {
            return sheets;
        }

        public void setSheets(Map<String, Map<Integer, List<String>>> sheets) {
            this.sheets = sheets;
        }

        @Override
        public String toString() {
            String info = "";
            for (String key:sheets.keySet()) {
                info += "sheet名称:" + key + "\t信息如下\n";
                Map<Integer,List<String>> sheetInfo = sheets.get(key);
                for (int i=0;i<sheetInfo.size();i++) {
                    List<String> lines = sheetInfo.get(i);
                    for (String word:lines) {
                        info += word + "\t";
                    }
                    info += "\n";
                }
            }
            return info;
        }
    }
}

