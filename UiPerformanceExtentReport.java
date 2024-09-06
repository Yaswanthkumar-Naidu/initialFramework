package uiperformanceutilities.reports.extentreports;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import testsettings.TestRunSettings;

import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import uiperformanceutilities.constants.UIPerformanceConstants;
import uiperformanceutilities.model.EntryModel;
import uiperformanceutilities.model.UIPerformanceModel;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.FileOutputStream;

public class UiPerformanceExtentReport
{
	public static final Logger logger =LoggerFactory.getLogger(UiPerformanceExtentReport.class.getName());
	public static final ExtentHtmlReporter htmlReporter = null;
	public static final ExtentReports extent = new ExtentReports();

	protected static final Map<String, String> sheetScreenHashMap = new HashMap<>();
	protected static final Map<String, String> screenNameSheetNameMap = new HashMap<>();
	protected static final Map<String, Integer> screenNameTotalTestCases = new HashMap<>();
	protected static final Map<String, ArrayList<String>> screenNameToTestCasesMap = new HashMap<>();
	protected static final Map<String,String> sheets = new HashMap<>();

	static XSSFWorkbook workbook = new XSSFWorkbook();

	public void createExtentReportCategory(String reportPath,String browserName) throws IOException
	{

		try
		{
			fileReportPath(reportPath);
			String reportFilePath = reportPath + "/UIPerformance.html"; 
			int count=1;
			workbook.createSheet("Dashboard");
			ExtentHtmlReporter htmlReporterlcl = new ExtentHtmlReporter(reportFilePath);
			ExtentReports extentlcl = new ExtentReports();
			tryCatchBlock(htmlReporterlcl, extentlcl);

			if (UIPerformanceConstants.getUiPerfData() != null)
			{


				for (String  ScreenName : UIPerformanceConstants.getUiPerfData().keySet())
				{
					String result = "";
					ExtentTest test = extentlcl.createTest(ScreenName);

					test.assignCategory(browserName);

					ArrayList<UIPerformanceModel> uiPerfModels=UIPerformanceConstants.getUiPerfData().get(ScreenName);


					count = extracted(reportPath, count, ScreenName, result, test, uiPerfModels);
				}
				createDashboard(workbook);
				writeworkbook(workbook, reportPath);
			}

			extentlcl.flush();
		}
		catch(Exception e)
		{
			 logger.error("Error'{}'", e.getMessage());
				
		}
	}

	private int extracted(String reportPath, int count, String screenName, String result, ExtentTest test,
			ArrayList<UIPerformanceModel> uiPerfModels) throws IOException {
		for(UIPerformanceModel uiPerfModel:uiPerfModels)
		{
			ExtentTest multiTCNode=test.createNode("Test Case : "+ uiPerfModel.getTestCaseName() + "  :: ModuleName : " + uiPerfModel.getModuleName() + " :: BrowserName : " + uiPerfModel.getBrowserName());	

			if(uiPerfModel.getResponseTimeMillisecond()<=UIPerformanceConstants.EXPECTEDRESPONSETIMEINMILLISECOND)
			{
				result = uiPerfModelEntries(result,uiPerfModel, multiTCNode);
			}
			else
			{
				result = entries(result, uiPerfModel, multiTCNode);
			}
			String sheetName=null;
			String sheetPrefix = sheets.getOrDefault(screenName, screenName);
			int counts = (int) TestRunSettings.getUiperformanceresultsmap().keySet().stream()
			        .filter(s -> s.contains(sheetPrefix))
			        .count() + 1;

			String newScreenName = screenName.length() > 25 ? screenName.substring(0, 25) : screenName;
			sheetName = newScreenName + "_" + counts;

			sheets.put(screenName,sheetName);
			TestRunSettings.getUiperformanceresultsmap().put(sheetName, result);
			screenNameSheetNameMap.put(sheetName, uiPerfModel.getTestCaseName());
			ArrayList<String> s=new ArrayList<>();

			s.add(uiPerfModel.getTestCaseName());
			sheetScreenHashMap.put(sheetName,screenName);
			if(screenNameToTestCasesMap.get(screenName)==null) {
				screenNameToTestCasesMap.put(screenName, s);
			}else {
				screenNameToTestCasesMap.get(screenName).add(uiPerfModel.getTestCaseName());
			}
			writetoExcel(workbook,result,sheetName);
			writeworkbook(workbook, reportPath);

		}
		return count;
	}
	
	private String uiPerfModelEntries(String result, UIPerformanceModel uiPerfModel, ExtentTest multiTCNode) {
	    StringBuilder sb = new StringBuilder(result);

	    for (String entry : uiPerfModel.getEntries()) {
	        sb.append("\n ").append(entry);
	    }
		multiTCNode.pass(" SourceScreen : " + uiPerfModel.getSourceScreen()+ "<br>  "
				+ " DestinationScreen : " + uiPerfModel.getDestinationScreen()+ "<br>  "
				+ " Source_DestinationScreen : " + uiPerfModel.getSourceDestinationScreen()+ "<br>  "
				+ " ResponseTime : " + uiPerfModel.getResponseTime()+ "<br>  "
				+ " ResponseTimeMillisecond : " + uiPerfModel.getResponseTimeMillisecond()+ "<br>  "
				+ " TestCaseName : " + uiPerfModel.getTestCaseName()+ "<br>  "
				+ " ModuleName : " + uiPerfModel.getModuleName()+ "<br>  "
				+ " BrowserName : " + uiPerfModel.getBrowserName()+ "<br>  "
				+ " StartTime : " + uiPerfModel.getStartTime()+ "<br>  "
				+ " EndTime : " + uiPerfModel.getEndtime()+ "<br>  "
				+ " PageLoadTime : " + uiPerfModel.getPageLoadTime()+ "<br> "
				+ " TTFB : " +uiPerfModel.getTtfb()+ "<br> "
				+ " EndToEndResponseTime : "+uiPerfModel.getEndtoendRespTime()+ "<br> "
				);
		 return sb.toString();
	}
	
	private String entries(String result, UIPerformanceModel uiPerfModel, ExtentTest multiTCNode) {
	    StringBuilder sb = new StringBuilder(result);

	    for (String entry : uiPerfModel.getEntries()) {
	        sb.append("\n ").append(entry);
	    }
	    
		multiTCNode.fail(" SourceScreen : " + uiPerfModel.getSourceScreen()+ "<br>  "
				+ " DestinationScreen : " + uiPerfModel.getDestinationScreen()+ "<br>  "
				+ " Source_DestinationScreen : " + uiPerfModel.getSourceDestinationScreen()+ "<br>  "
				+ " ResponseTime : " + uiPerfModel.getResponseTime()+ "<br>  "
				+ " ResponseTimeMillisecond : " + uiPerfModel.getResponseTimeMillisecond()+ "<br>  "
				+ " TestCaseName : " + uiPerfModel.getTestCaseName()+ "<br>  "
				+ " ModuleName : " + uiPerfModel.getModuleName()+ "<br>  "
				+ " BrowserName : " + uiPerfModel.getBrowserName()+ "<br>  "
				+ " StartTime : " + uiPerfModel.getStartTime()+ "<br>  "
				+ " EndTime : " + uiPerfModel.getEndtime()+ "<br>  "
				+ " PageLoadTime : " + uiPerfModel.getPageLoadTime()+ "<br> "
				+ " TTFB : " +uiPerfModel.getTtfb()+ "<br> "
				+ " EndToEndResponseTime : "+uiPerfModel.getEndtoendRespTime()+ "<br> "
				);

		 resourceType(uiPerfModel, multiTCNode);
		 return sb.toString();
	}
	
	private void resourceType(UIPerformanceModel uiPerfModel, ExtentTest multiTCNode) {
		for(String resourceType: uiPerfModel.getMapEntryModel().keySet())
		{
			ExtentTest resourceTypeNode=multiTCNode.createNode("Resource  Type : "+ resourceType);	
			ArrayList<EntryModel> lstEntryModel=uiPerfModel.getMapEntryModel().get(resourceType);
			for (EntryModel entryModel :lstEntryModel)
			{

				resourceTypeNode.log(Status.FAIL,
						"EntryName : "+ entryModel.getEntryName()
						+"<br>EntryType : "+ entryModel.getEntryType()
						+"<br>InitiatorType : "+ entryModel.getInitiatorType()
						+"<br>StartTime : "+ entryModel.getStartTime()
						+"<br>EndTime : "+ entryModel.getEndTime()
						+"<br>Duration : "+ entryModel.getDuration()
						+"<br>TransferSize : "+ entryModel.getTransferSize());
			}
		}
	}
	private void fileReportPath(String reportPath) throws DirectoryCreationException {
	    try {
	        File dir = new File(reportPath);
	        if (!dir.exists()) {
	            boolean created = dir.mkdirs();
	            if (!created) {
	                throw new DirectoryCreationException("Failed to create directory: " + reportPath);
	            }
	        }
	        logger.error("{} Directory Created ===>", reportPath);
	    } catch (SecurityException e) {
	        throw new DirectoryCreationException("Permission denied to create directory: " + reportPath, e);
	    }
	}
	
	class DirectoryCreationException extends Exception {
	    public DirectoryCreationException(String message) {
	        super(message);
	    }

	    public DirectoryCreationException(String message, Throwable cause) {
	        super(message, cause);
	    }
	}
	
	private void tryCatchBlock(ExtentHtmlReporter htmlReporterlcl, ExtentReports extentlcl) {
		try
		{
			extentlcl.attachReporter(htmlReporterlcl);

		}
		catch(Exception e)
		{
			logger.error("{}", e.getMessage());
		}
	}
	private static XSSFCellStyle getDataCellStyle(XSSFWorkbook workbook){
		XSSFCellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setWrapText(true);		
		BorderStyle borderStyle = BorderStyle.THIN;
		cellStyle.setBorderLeft(borderStyle);
		cellStyle.setBorderRight(borderStyle);
		cellStyle.setBorderBottom(borderStyle);
		cellStyle.setBorderTop(borderStyle);
		cellStyle.setAlignment(HorizontalAlignment.LEFT);
		cellStyle.setVerticalAlignment(VerticalAlignment.TOP);		
		return cellStyle;
	}	

	private static XSSFCellStyle headerCellStyle(XSSFWorkbook workbook){
		XSSFCellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setWrapText(true);		
		BorderStyle borderStyle = BorderStyle.THIN;
		cellStyle.setBorderLeft(borderStyle);
		cellStyle.setBorderRight(borderStyle);
		cellStyle.setBorderBottom(borderStyle);
		cellStyle.setBorderTop(borderStyle);
		cellStyle.setAlignment(HorizontalAlignment.LEFT);
		cellStyle.setVerticalAlignment(VerticalAlignment.TOP);	
		Font headerFont = workbook.createFont();
		headerFont.setBold(true);
		cellStyle.setFont(headerFont);
		cellStyle.setFont(headerFont);
		cellStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
		cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

		return cellStyle;
	}	

	public static void writetoExcel(XSSFWorkbook workbook,String data,String sheetName){

		Sheet sheet = workbook.createSheet(sheetName);
		XSSFCellStyle cellstyle = getDataCellStyle(workbook);
		XSSFCellStyle headerStyle = headerCellStyle(workbook);

		String[] lines = data.split("\n");

		String headerLine=lines[1].trim(); 
		String[] headers = headerLine.split("\\|"); 
		int colIndex = 0;
		Row headerRow = sheet.createRow(0);

		for (String header : headers) {
			Cell headerCell = headerRow.createCell(colIndex++);
			headerCell.setCellValue(header);
			headerCell.setCellStyle(headerStyle);

		}
		sheet.setColumnWidth(0, 30 * 270); 
		for (int rowIndex = 1; rowIndex < lines.length-2; rowIndex++) {
			String[] values = lines[rowIndex+2].split("\\|");
			colIndex = 0;
			Row dataRow = sheet.createRow(rowIndex);
			for (String value : values) {
				Cell dataCell = dataRow.createCell(colIndex++);
				dataCell.setCellValue(value);
				dataCell.setCellStyle(cellstyle);
				sheet.setColumnWidth(rowIndex, 30 * 270); 
			}
		}
	}
	
	public static void writeworkbook(XSSFWorkbook workbook, String reportPath) throws IOException {
		String delem = "/";
		String filepath = reportPath+delem+"UIPerformanceReport"+".xlsx";
		FileOutputStream outputStream = new FileOutputStream(filepath);
		workbook.write(outputStream);
		outputStream.close();
	}

	public String writeDataToTextFile(String filePath, String fileName,String fileContent,String fileFormat)
	{
		String del = "/";
		filePath = filePath + del + fileName + fileFormat;
		
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
			writer.write(fileContent);
			
			filePath= filePath.replace("\\","/");

			filePath = "<a href = 'file:///"+ filePath + "'>"+ fileName + "</a>";
			return filePath;
		}

		catch (Exception e)
		{
			return filePath;
		}
	}

	public static void createDashboard(XSSFWorkbook workbook) {
		try {
			HashMap<String, Double> screenNameToMedian = new HashMap<>();
			HashMap<String, Double> screenNameToTotalMean = new HashMap<>();
			HashMap<String, Double> screenNameToMinimum = new HashMap<>();
			HashMap<String, Double> screenNameToMaximun = new HashMap<>();
			Map<String, Double> screenNameToTotalMeanNew;
			
			XSSFSheet dashboardSheet = workbook.getSheet("Dashboard");

			CreationHelper createHelper = workbook.getCreationHelper();
			int rowNum = 1;

			for (String screenName : TestRunSettings.getUiperformanceresultsmap().keySet()) {
						
				double mean=calculateMean(screenName);
				screenNameToTotalMean.put(screenName, mean);
				double median=calculateMedian(screenName);
				screenNameToMedian.put(screenName, median);
				double min=calculateMinimunDuration(screenName);
				screenNameToMinimum.put(screenName, min);
				double max=calculateMaximunDuration(screenName);
				screenNameToMaximun.put(screenName, max);

			}
			screenNameToTotalMeanNew=maximumValueForDashboard(screenNameToTotalMean);

			CellStyle headerStyle = workbook.createCellStyle();
			Font headerFont = workbook.createFont();
			headerFont.setBold(true);
			headerStyle.setFont(headerFont);
			headerStyle.setFont(headerFont);
			headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
			
			headerStyle.setBorderBottom(BorderStyle.THICK);
			headerStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
			headerStyle.setBorderLeft(BorderStyle.THICK);
			headerStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
			headerStyle.setBorderRight(BorderStyle.THICK);
			headerStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
			headerStyle.setBorderTop(BorderStyle.THICK);
			headerStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
			
			CellStyle style = workbook.createCellStyle();
			style.setBorderBottom(BorderStyle.THIN);
			style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
			style.setBorderLeft(BorderStyle.THIN);
			style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
			style.setBorderRight(BorderStyle.THIN);
			style.setRightBorderColor(IndexedColors.BLACK.getIndex());
			style.setBorderTop(BorderStyle.THIN);
			style.setTopBorderColor(IndexedColors.BLACK.getIndex());

			Row headerRow = dashboardSheet.createRow(0);
			Cell headerCell1 = headerRow.createCell(0);
			headerCell1.setCellValue("S.No.");
			headerCell1.setCellStyle(headerStyle);
			Cell headerCell2 = headerRow.createCell(1);
			headerCell2.setCellValue("Screename");
			headerCell2.setCellStyle(headerStyle);
			Cell headerCell3 = headerRow.createCell(2);
			headerCell3.setCellValue("Average Duration (ms)");
			headerCell3.setCellStyle(headerStyle);
			Cell headerCell4 = headerRow.createCell(3);
			headerCell4.setCellValue("Median Duration (ms)");
			headerCell4.setCellStyle(headerStyle);
			Cell headerCell5 = headerRow.createCell(4);
			headerCell5.setCellValue("Min Duration (ms)");
			headerCell5.setCellStyle(headerStyle);
			Cell headerCell6 = headerRow.createCell(5);
			headerCell6.setCellValue("Max Duration (ms)");
			headerCell6.setCellStyle(headerStyle);
			Cell headerCell7 = headerRow.createCell(6);
			headerCell7.setCellValue("Count of TestCase");
			headerCell7.setCellStyle(headerStyle);
			Cell headerCell8 = headerRow.createCell(7);
			headerCell8.setCellValue("All TestCases");
			headerCell8.setCellStyle(headerStyle);
			Cell headerCell9 = headerRow.createCell(8);
			headerCell9.setCellValue("Max Avrg Duration TestCase");
			headerCell9.setCellStyle(headerStyle);

			CellStyle hyperlinkStyle = workbook.createCellStyle();
			Font hyperlinkFont = workbook.createFont();
			hyperlinkFont.setUnderline(Font.U_SINGLE);
			hyperlinkFont.setColor(IndexedColors.BLUE.getIndex());
			hyperlinkStyle.setFont(hyperlinkFont);
			hyperlinkStyle.setBorderBottom(BorderStyle.THIN);
			hyperlinkStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
			hyperlinkStyle.setBorderLeft(BorderStyle.THIN);
			hyperlinkStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
			hyperlinkStyle.setBorderRight(BorderStyle.THIN);
			hyperlinkStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
			hyperlinkStyle.setBorderTop(BorderStyle.THIN);
			hyperlinkStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());

			for (Map.Entry<String, Double> entry : screenNameToTotalMeanNew.entrySet()) {
			    String screenName = entry.getKey();
			    Double totalMean = entry.getValue();

			    Row row = dashboardSheet.createRow(rowNum++);
			    Cell cell1 = row.createCell(0);
			    cell1.setCellValue((double) rowNum - 1);
			    cell1.setCellStyle(style);

			    row.createCell(1).setCellValue(screenName);
			    Cell cell = row.getCell(1);
			    cell.setCellStyle(style);

			    Cell cell2 = row.createCell(2);
			    cell2.setCellValue(totalMean);
			    cell2.setCellStyle(style);

			    Cell cell3 = row.createCell(3);
			    cell3.setCellValue(screenNameToMedian.get(screenName));
			    cell3.setCellStyle(style);

			    Cell cell4 = row.createCell(4);
			    cell4.setCellValue(screenNameToMinimum.get(screenName));
			    cell4.setCellStyle(style);

			    Cell cell5 = row.createCell(5);
			    cell5.setCellValue(screenNameToMaximun.get(screenName));
			    cell5.setCellStyle(style);

			    StringBuilder arrayData = new StringBuilder();
			    String keyDataForSheetName = sheetScreenHashMap.get(screenName);
			    List<String> testCases = screenNameToTestCasesMap.get(keyDataForSheetName);
			    for (int i = 0; i < testCases.size(); i++) {
			        if (i > 0) {
			            arrayData.append(":");
			        }
			        arrayData.append(testCases.get(i));
			    }

			    Cell cell6 = row.createCell(6);
			    cell6.setCellValue(testCases.size());
			    cell6.setCellStyle(style);

			    Cell cell7 = row.createCell(7);
			    cell7.setCellValue(arrayData.toString());
			    cell7.setCellStyle(style);

			    Cell cell8 = row.createCell(8);
			    cell8.setCellValue(screenNameSheetNameMap.get(screenName));
			    cell8.setCellStyle(hyperlinkStyle);
			    Hyperlink link = createHelper.createHyperlink(HyperlinkType.DOCUMENT);
			    link.setAddress("'" + screenName + "'!A1");
			    cell8.setHyperlink(link);
			}
			
			dashboardSheet.setColumnWidth(0, 10 * 250); 
			dashboardSheet.setColumnWidth(1, 20 * 250);
			dashboardSheet.setColumnWidth(2, 20 * 250);
			dashboardSheet.setColumnWidth(3, 20 * 250);
			dashboardSheet.setColumnWidth(4, 20 * 250);
			dashboardSheet.setColumnWidth(5, 20 * 250);
			dashboardSheet.setColumnWidth(6, 20 * 250);
			dashboardSheet.setColumnWidth(7, 20 * 250);
			dashboardSheet.setColumnWidth(8, 20 * 250);


		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static double calculateMean(String screenName) {

		int columnIndex = 2;
		List<Double> numbers = new ArrayList<>();
		XSSFSheet sheet = workbook.getSheet(screenName);

		for (Row row : sheet) {
			Cell cell = row.getCell(columnIndex);
			if (cell != null ) {
				String cellValue = cell.getStringCellValue();
				try {
					double number = Double.parseDouble(cellValue);
					numbers.add(number);
				} catch (NumberFormatException e) {
					logger.error("{}", e.getMessage());
				}
			}
		}

		double sum = 0;
		for (double number : numbers) {
			sum += number;
		}
		return numbers.isEmpty() ? 0 : sum / numbers.size();

	}

	private static double calculateMedian(String screenName) {

		XSSFSheet sheet = workbook.getSheet(screenName);

		int columnIndex = 2;
		List<Double> numbers = new ArrayList<>();

		for (Row row : sheet) {
			Cell cell = row.getCell(columnIndex);
			if (cell != null) {
				String cellValue = cell.getStringCellValue();
				try {
					double number = Double.parseDouble(cellValue);
					numbers.add(number);
				} catch (NumberFormatException e) {
					 logger.error("Error parsing cell value '{}' to a number: {}", cellValue, e.getMessage());
				}
			}
		}

		Collections.sort(numbers);

		double median;
		int size = numbers.size();
		if (size % 2 == 0) {
			median = (numbers.get(size / 2 - 1) + numbers.get(size / 2)) / 2.0;
		} else {
			median = numbers.get(size / 2);
		}

		return median;
	}

	private static double calculateMinimunDuration(String screenName) {

		XSSFSheet sheet = workbook.getSheet(screenName);

		int columnIndex = 2;
		List<Double> numbers = new ArrayList<>();

		for (Row row : sheet) {
			Cell cell = row.getCell(columnIndex);
			if (cell != null) {
				String cellValue = cell.getStringCellValue();
				try {
					double number = Double.parseDouble(cellValue);
					numbers.add(number);
				} catch (NumberFormatException e) {
					logger.error("{}", e.getMessage());
				}
			}
		}

		return Collections.min(numbers);
	}
	
	private static double calculateMaximunDuration(String screenName) {

		XSSFSheet sheet = workbook.getSheet(screenName);

		int columnIndex = 2;
		List<Double> numbers = new ArrayList<>();

		for (Row row : sheet) {
			Cell cell = row.getCell(columnIndex);
			if (cell != null) {
				String cellValue = cell.getStringCellValue();
				try {
					double number = Double.parseDouble(cellValue);
					numbers.add(number);
				} catch (NumberFormatException e) {
					logger.error("{}", e.getMessage());
				}
			}
		}
		return Collections.max(numbers);
	}


	public static Map<String, Double> maximumValueForDashboard(Map<String,Double> screenNameToTotalMean) {
		HashMap<String, Double> screenNameResult = new HashMap<>();

		for(String k:TestRunSettings.getUiperformanceresultsmap().keySet()) {

			double maxAccountValue = Double.MIN_VALUE;
			String latestKey=null;

			String keyTrimmed=k.split("_")[0];

			for (Map.Entry<String, Double> entry : screenNameToTotalMean.entrySet()) {
				String key = entry.getKey();

				Double value = entry.getValue();
				if (key.startsWith(keyTrimmed) && value>maxAccountValue) {
						latestKey=key;
						maxAccountValue=value;
					}
			}
			screenNameResult.put(latestKey, maxAccountValue);
		}
		return screenNameResult;
	}
}