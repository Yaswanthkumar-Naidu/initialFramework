package common_utilities.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;


public class PoiReadExcel
    {
	
    	private static final Logger logger = Logger.getLogger(PoiReadExcel.class.getName());

    	public static Map<String, ArrayList<String>> fetchWithCondition(String sheetPath, String sheetName, List<String> whereClause) {
    		Map<String, ArrayList<String>> excelMap = coreListToMap(sheetPath, sheetName);
    		for (String clause : whereClause) {
    			HashMap<String, ArrayList<String>> finalMap = new HashMap<>();
    			ArrayList<Integer> addIndex = new ArrayList<>();
    			for (Map.Entry<String, ArrayList<String>> entry : excelMap.entrySet()) {
    				int k = 0;
    				if (entry.getKey().equalsIgnoreCase(clause.split("::")[0])) {
    					ArrayList<String> vals = new ArrayList<>();
    					for (String val : new ArrayList<String>(entry.getValue())) {
    						if (val.equalsIgnoreCase(clause.split("::")[1])) {
    							vals.add(val);
    							addIndex.add(k);
    						}
    						k++;
    					}
    					finalMap.put(entry.getKey(), vals);
    				}
    			}
    			for (Map.Entry<String, ArrayList<String>> entry : excelMap.entrySet()) {
    				ArrayList<String> vals = new ArrayList<>();
    				if (!entry.getKey().equalsIgnoreCase(clause.split("::")[0])) {
    					for (int add : addIndex)
    						vals.add(entry.getValue().get(add));
    					finalMap.put(entry.getKey(), vals);
    				}
    			}
    			excelMap = finalMap;
    		}
    		return excelMap;
    	}
    	
    	public static Map<String, ArrayList<String>> coreListToMap(String sheetPath, String sheetName) {
    		List<ArrayList<String>> tempStorage = coreFetch(sheetPath, sheetName);

			if (tempStorage == null || tempStorage.isEmpty()) {
				return new HashMap<>();
			}

    		HashMap<String, ArrayList<String>> excelMap = new HashMap<>();
    		
    		ArrayList<ArrayList<String>> tempList = new ArrayList<>();

    		for(int j=0; j<tempStorage.get(0).size() ; j++){
    			ArrayList<String> eachCol = new ArrayList<>();
    			for(int i=1; i<tempStorage.size(); i++){
    				try{
    					eachCol.add(tempStorage.get(i).get(j));
    				}catch(IndexOutOfBoundsException e){
    					eachCol.add("");
    				}
    				
    			}
    			tempList.add(eachCol);
    		}
    		
    		for(int i=0; i<tempList.size(); i++){
    			excelMap.put(tempStorage.get(0).get(i), tempList.get(i));
    		}
    		return excelMap;
    	}
    	
    	@SuppressWarnings("deprecation")
		public static List<ArrayList<String>> coreFetch(String sheetPath, String sheetName) {
    		ArrayList<ArrayList<String>> tempStorage = new ArrayList<>();
    		FileInputStream file = null;
    		XSSFWorkbook workbook = null;		
    		try {
    			if(!(new File(sheetPath).exists()))
    			{
    				JOptionPane.showConfirmDialog(null, "File NOT found: "+sheetPath , "Warning", 2);
    			}
    			file = new FileInputStream(new File(sheetPath));
    			workbook = new XSSFWorkbook(file);
    			XSSFSheet sheet = workbook.getSheet(sheetName);

    			Iterator<Row> rowIterator = sheet.iterator();
    			int numOfHeaders = 0;

    			if(rowIterator.hasNext()) {
    				ArrayList<String> rowWise = new ArrayList<>();

    				Row row = rowIterator.next();
    				Iterator<Cell> cellIterator = row.cellIterator();

    				int i = 0;
    				while (cellIterator.hasNext()) {
    					Cell cell = cellIterator.next();
    					
    					if(i != cell.getColumnIndex()){
    						rowWise.add("");
    					}
    					
    					i = cell.getColumnIndex()+1;
    					
    					switch (cell.getCellType()) {
    					case Cell.CELL_TYPE_STRING:
    						rowWise.add(cell.getStringCellValue());
    						break;
    					case Cell.CELL_TYPE_NUMERIC:
    						rowWise.add(Integer.toString((int) (cell.getNumericCellValue())));
    						break;
    					 default:
    					}
    					
    				}
    				numOfHeaders = rowWise.size();
    				tempStorage.add(rowWise);				
    			}
    			
    			while (rowIterator.hasNext()) {
    				ArrayList<String> rowWise = new ArrayList<>();
    				
    				Row row = rowIterator.next();
    				for(int cellNumber=0; cellNumber < numOfHeaders; cellNumber++){
    					Cell  cell = row.getCell(cellNumber);
    					String dataValue = "";
    					if(cell != null){						
    						switch (cell.getCellType()) {
    							case Cell.CELL_TYPE_STRING:
    								String cellValue = cell.getStringCellValue();
   
    								rowWise.add(cellValue);
    								break;
    							case Cell.CELL_TYPE_NUMERIC:
    								rowWise.add(Long.toString((long) (cell.getNumericCellValue())));
    								break;
    							default: rowWise.add("");
    							  break;
    						}												
    					}else{
    						rowWise.add(dataValue);
    					}
    				}
    				tempStorage.add(rowWise);
    			}
    		} catch (Exception e) {
    			logger.log(Level.WARNING,"Error while fetching details from TestCase file",e);
    		} finally {
    		    try {
    		        if (workbook != null) {
    		            workbook.close();
    		        }
    		        if (file != null) {
    		            file.close();
    		        }
    		    } catch (IOException e) {
    		        logger.log(Level.SEVERE, "Error closing resources", e);
    		    }
    		}
    		return tempStorage;
    	}    	
    }
