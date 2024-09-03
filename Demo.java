package reusable;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uitests.testng.common.ExcelUtility;
import common_utilities.Utilities.Util;
import initialize_scripts.InitializeTestSettings;


public class Demo extends InitializeTestSettings{

private static final Logger logger =LoggerFactory.getLogger(Demo.class.getName());

	Util utility = new Util();
	InitializeTestSettings its=new InitializeTestSettings();
	Path currentRelativePath = Paths.get(""); 
	  String prjPath=currentRelativePath.toAbsolutePath().toString();
	  ExcelUtility excelutil=new ExcelUtility();
	  
	  public ArrayList<String> sheets() throws InvalidFormatException, IOException {
		    ArrayList<String> sheetNames = new ArrayList<>();
		    try (Workbook wb = WorkbookFactory.create(new File("/path/to/excel.xls"))) {
		        for (int i = 0; i < wb.getNumberOfSheets(); i++) {
		            sheetNames.add(wb.getSheetName(i));
		        }
		    } catch (InvalidFormatException | IOException e) {
		        throw e;
		    }
		    return sheetNames;
		}

	
	public static XSSFRow test(XSSFSheet sheet, String colName, String textToFind){
	    int colIndex=0;
	    for (int colNum = 0; colNum<=sheet.getRow(0).getLastCellNum();colNum++)
	    {
	        if (sheet.getRow(0).getCell(colNum).toString().equalsIgnoreCase(colName)){
	            colIndex = colNum;
	            break;
	        }
	    }
	    for (int RowNum = 0; RowNum<sheet.getLastRowNum();RowNum++){
	        if(sheet.getRow(RowNum).getCell(colIndex).toString().equalsIgnoreCase(textToFind)){
	            return sheet.getRow(RowNum);
	        }
	    }
	    logger.info("No any row found that contains {}",textToFind);
	    return null;
	}
	
}
