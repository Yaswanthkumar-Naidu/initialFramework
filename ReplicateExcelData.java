package reusable;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReplicateExcelData 
{
	private static final Logger logger =LoggerFactory.getLogger(ReplicateExcelData.class.getName());
	   
	
	public List<String> sheetsName() throws IOException {
	    ArrayList<String> sheetnames = new ArrayList<>();
	    
	    try (FileInputStream fileInputStream = new FileInputStream("C:/Users/abhishekkumar879/Desktop/KY_POM_V7/KY_POM/POM_Test/src/main/java/Artifacts/TestData/WP/DataCollection.xls")) {
	        HSSFWorkbook workbook = new HSSFWorkbook(fileInputStream);

	        // for each sheet in the workbook
	        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
	            sheetnames.add(workbook.getSheetName(i));
	        }
	    } catch (IOException e) {
	        logger.error("Error while getting sheet names: {}", e.getMessage(), e);
	    }
	    
	    return sheetnames;
	}

	
	public static String[] readExcelRow(int iteration, int numOfIterations, String filePath, String excelFileName, String sheetName) throws IOException {
	    String[] tempRow = new String[1000];

	    try (FileInputStream fileInputStream = new FileInputStream(new File(filePath + excelFileName + ".xlsx"));
	         XSSFWorkbook workBook = new XSSFWorkbook(fileInputStream)) {

	        int sheetIndex = workBook.getSheetIndex(sheetName);
	        XSSFSheet sheet = workBook.getSheetAt(sheetIndex);

	        FormulaEvaluator formulaEvaluator = workBook.getCreationHelper().createFormulaEvaluator();

	        Row row = sheet.getRow(iteration + numOfIterations);

	        for (int cn = 0; cn < row.getLastCellNum(); cn++) {
	            Cell cell = row.getCell(cn);
	            try {
	                if (cell == null || cell.toString().isEmpty()) {
	                    tempRow[cn] = "";
	                } else {
	                    switch (formulaEvaluator.evaluateInCell(cell).getCellType()) {
	                        case Cell.CELL_TYPE_NUMERIC:
	                            tempRow[cn] = Integer.toString((int) Math.round((cell.getNumericCellValue())));
	                            break;
	                        case Cell.CELL_TYPE_STRING:
	                            tempRow[cn] = cell.getStringCellValue();
	                            break;
	                        default:      
	                    }
	                }
	            } catch (Exception e) {
	                logger.error("Failed to copy column: {}", e.getMessage(), e);
	                tempRow[cn] = "";
	            }
	        }
	    } catch (IOException e) {
	        logger.error("Error while reading Excel row: {}", e.getMessage(), e);
	    }

	    return tempRow;
	}


}
