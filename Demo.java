package reusable;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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


public class Demo extends InitializeTestSettings
{
	private static final Logger logger =LoggerFactory.getLogger(Demo.class.getName());
	
	Util utilobj = new Util();
	InitializeTestSettings iTS=new InitializeTestSettings();
	Path currentRelativePath = Paths.get(""); 
	  String prjPath=currentRelativePath.toAbsolutePath().toString();
	  ExcelUtility excelutil=new ExcelUtility();
	  
	  
	  public List<String> sheets(String filePath) throws InvalidFormatException, IOException {
		    try (Workbook wb = WorkbookFactory.create(new File(filePath))) {
		        List<String> sheetNames = new ArrayList<>();
		        for (int i = 0; i < wb.getNumberOfSheets(); i++) {
		            sheetNames.add(wb.getSheetName(i));
		        }
		        return sheetNames;
		    } catch (Exception e) {
		        logger.error("Error retrieving sheet names: {}", e.getMessage());
		        return Collections.emptyList();
		    }
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
	    
	    return null;
	}
	
}
