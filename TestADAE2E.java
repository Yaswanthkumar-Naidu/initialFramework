package adautilities;

import com.deque.html.axecore.providers.FileAxeScriptProvider;
import com.deque.html.axecore.results.Node;
import com.deque.html.axecore.results.Results;
import com.deque.html.axecore.results.Rule;
import com.deque.html.axecore.selenium.AxeBuilder;
import testsettings.TestRunSettings;

import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.util.IOUtils;
import org.openqa.selenium.WebDriver;
import java.awt.BorderLayout;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import org.apache.poi.xssf.usermodel.*;
import org.jfree.data.category.DefaultCategoryDataset; 
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.ChartUtils;
import java.util.Iterator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import java.util.Comparator;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class TestADAE2E {
	private static final Logger logger =LoggerFactory.getLogger(TestADAE2E.class.getName());


	public void aDAScan(WebDriver driver, String screenName) {
		try {
			StringBuilder axeResults = new StringBuilder();
			AxeBuilder builder = new AxeBuilder();
			String timeoutFilePath = TestRunSettings.getAdaToolPath();
			axeResults.append("Screen Name,Help,Impact,Description,Help Url,Id,Tags").append(System.lineSeparator());
			FileAxeScriptProvider axeScriptProvider = new FileAxeScriptProvider(timeoutFilePath);
			builder.setAxeScriptProvider(axeScriptProvider);
			builder.include(Arrays.asList("html"));
			Results result = builder.analyze(driver);
			List<Rule> violations = result.getViolations();
			String url = driver.getCurrentUrl();
			String pageID = url.substring(url.lastIndexOf("/") + 1);
			logger.info("Violation of the Screen {} : {} ",screenName,violations.size());
			if (violations.isEmpty()) {
				logger.info("No violations found:{} with PageID: {} " ,screenName,pageID);
			} else {
				logger.info("ADA violations exist on the page:{} with PageID: {}",screenName, pageID);
			}
			for (Rule element : violations) {
				String strHelp = element.getHelp();
				String strImpact = element.getImpact();
				String strDescription = "\"" + element.getDescription() + "\"";
				String strHelpUrl = element.getHelpUrl();
				String strId = element.getId();
				String strTags = "\"" + String.join(",", element.getTags()) + "\"";
				axeResults.append(screenName).append(",").append(strHelp).append(",").append(strImpact).append(",")
				.append(strDescription).append(",").append(strHelpUrl).append(",").append(strId)
				.append(",").append(strTags).append(System.lineSeparator());
				if (element.getNodes() != null && !element.getNodes().isEmpty()) {
					for (Node item : element.getNodes()) {
						if (item.getHtml().trim().length() > 0 && item.getTarget().toString().trim().length() > 0) {
							String htmlContent = item.getHtml().replace(",", "_").replaceAll("\\s", "");
							axeResults.append(screenName).append(",").append(strHelp).append(",").append(strImpact)
							.append(",").append(strDescription).append(",").append(strHelpUrl).append(",")
							.append("\"").append(htmlContent).append("\"").append(",").append("\"")
							.append(item.getTarget()).append("\"").append(System.lineSeparator());
						}
					}
				}
			}
			String[] lines = axeResults.toString().split(System.lineSeparator());
			TestRunSettings.getAdaResultsMap().put(screenName, lines);

		} catch (Exception e) {
			logger.error("Error occurred", e);
		}
	}

	public boolean generateAdaResults(String aDAResult) {
		boolean fileCreated = false;
		String file = null;
		FileOutputStream fileOut = null;

		try {
			if(!TestRunSettings.getAdaResultsMap().isEmpty()){
				String directoryPath = createDirectory(TestRunSettings.getResultsPath() + "/ADAReport/");
				file = directoryPath + aDAResult + ".xlsx";

				XSSFWorkbook workbook = new XSSFWorkbook();
				XSSFCellStyle cellstyle = getDataCellStyle(workbook);
				createDashboard(workbook);
				for (String screenName : TestRunSettings.getAdaResultsMap().keySet()) {
					String[] axeResults = TestRunSettings.getAdaResultsMap().get(screenName);
					Sheet sheet = workbook.createSheet(screenName);
					int rowNum = 0;
					for (String line : axeResults) {
						Row row = sheet.createRow(rowNum++);
						String[] values = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
						
						for (int i = 0; i < values.length; i++) {
							Cell cell = row.createCell(i);
							cell.setCellValue(values[i]);
							cell.setCellStyle(cellstyle);
							sheet.setColumnWidth(i, 30 * 270); 
						}
					}
				}
				createBarChart(workbook);
				fileOut = new FileOutputStream(file);
				workbook.write(fileOut);
				logger.info("Axe results successfully written to Excel file.");
				fileOut.close();

			} 
			else {
				logger.info("No ADA Test Result Available");
			}
		}catch (Exception e) {
			 logger.error("An error occurred", e);
			
		}
		return fileCreated;
	}

	private String createDirectory(String directoryPath) {
		String fullPath = directoryPath;
		File dir = new File(fullPath);
		if (!dir.exists()) dir.mkdirs();
		return fullPath;
	}

	public static String generateTimestamp() {
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH_mm_ss");
		return now.format(formatter);
	}



	public static void createDashboard(XSSFWorkbook workbook) {
		try {
			HashMap<String, Integer> screenNameToTotalIssues = new HashMap<>();
			HashMap<String, Integer> screenNameToTotalseriousIssues = new HashMap<>();
			HashMap<String, Integer> screenNameToTotalmoderateIssues = new HashMap<>();
			HashMap<String, Integer> screenNameToTotallowIssues = new HashMap<>();

			XSSFSheet dashboardSheet = workbook.createSheet("Dashboard");
			workbook.createSheet("Graph");

			CreationHelper createHelper = workbook.getCreationHelper();
			int rowNum = 1;
			
		


			for (String screenName : TestRunSettings.getAdaResultsMap().keySet()) {
				String[] axeResults = TestRunSettings.getAdaResultsMap().get(screenName);
				int seriousCount=0;
				int moderateCount=0;
				int lowCount=0;
				int totalIssues = axeResults.length - 1; // Exclude header
				for(String impact:axeResults) {
					if(impact.toLowerCase().contains("serious")) {
						seriousCount++;
					}else if(impact.toLowerCase().contains("moderate")) {
						moderateCount++;
					}else if(impact.toLowerCase().contains("low")) {
						lowCount++;
					}
				}
				screenNameToTotalIssues.put(screenName, totalIssues);
				screenNameToTotalseriousIssues.put(screenName, seriousCount);
				screenNameToTotalmoderateIssues.put(screenName, moderateCount);
				screenNameToTotallowIssues.put(screenName, lowCount);

			}
			
			 // Create a cell style with borders and color for HEADER

            CellStyle headerStyle = workbook.createCellStyle();
        	Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
           
            headerStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
           
            headerStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
           
            headerStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
           
            headerStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());
			 // Create a cell style with borders and color
            CellStyle style = workbook.createCellStyle();
          
            style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
            
            style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
           
            style.setRightBorderColor(IndexedColors.BLACK.getIndex());
            
            style.setTopBorderColor(IndexedColors.BLACK.getIndex());
		
			Row headerRow = dashboardSheet.createRow(0);
            Cell headerCell1 = headerRow.createCell(0);
            headerCell1.setCellValue("S.No.");
            headerCell1.setCellStyle(headerStyle);
            Cell headerCell2 = headerRow.createCell(1);
            headerCell2.setCellValue("Screename");
            headerCell2.setCellStyle(headerStyle);
            Cell headerCell3 = headerRow.createCell(2);
            headerCell3.setCellValue("TotalIssues");
            headerCell3.setCellStyle(headerStyle);
            Cell headerCell4 = headerRow.createCell(3);
            headerCell4.setCellValue("TotalSeriousIssues");
            headerCell4.setCellStyle(headerStyle);
            Cell headerCell5 = headerRow.createCell(4);
            headerCell5.setCellValue("TotalModerateIssues");
            headerCell5.setCellStyle(headerStyle);
            Cell headerCell6 = headerRow.createCell(5);
            headerCell6.setCellValue("TotalLowIssues");
            headerCell6.setCellStyle(headerStyle);
            Cell headerCell7 = headerRow.createCell(6);
            headerCell7.setCellValue("Status");
            headerCell7.setCellStyle(headerStyle);

            //hyperlink config
            CellStyle hyperlinkStyle = workbook.createCellStyle();
            Font hyperlinkFont = workbook.createFont();
            hyperlinkFont.setUnderline(Font.U_SINGLE);
            hyperlinkFont.setColor(IndexedColors.BLUE.getIndex());
            hyperlinkStyle.setFont(hyperlinkFont);

            hyperlinkStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex());
            hyperlinkStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex());
            hyperlinkStyle.setRightBorderColor(IndexedColors.BLACK.getIndex());
            hyperlinkStyle.setTopBorderColor(IndexedColors.BLACK.getIndex());

            
			List<Map.Entry<String, Integer>> sortedIssuesList = new ArrayList<>(screenNameToTotalIssues.entrySet());
			sortedIssuesList.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));
			for (Map.Entry<String, Integer> entry : sortedIssuesList) {
				Row row = dashboardSheet.createRow(rowNum++);
				Cell cell1=row.createCell(0);
				cell1.setCellValue(rowNum - 1.0);
				cell1.setCellStyle(style);
				String screenName = entry.getKey();
				row.createCell(1).setCellValue(screenName);
				// Add hyperlink to the screen name
				Cell cell = row.getCell(1);
				cell.setCellStyle(hyperlinkStyle);

				Hyperlink link = createHelper.createHyperlink(HyperlinkType.DOCUMENT);
				link.setAddress("'" + screenName + "'!A1"); // Sheet name followed by cell reference
				cell.setHyperlink(link);
				// End of hyperlink addition
				Cell cell2=row.createCell(2);
				cell2.setCellValue(entry.getValue());
				cell2.setCellStyle(style);

				Cell cell3=row.createCell(3);
				cell3.setCellValue(screenNameToTotalseriousIssues.get(entry.getKey()));
				cell3.setCellStyle(style);

				Cell cell4=row.createCell(4);
				cell4.setCellValue(screenNameToTotalmoderateIssues.get(entry.getKey()));
				cell4.setCellStyle(style);
				Cell cell5=row.createCell(5);
				cell5.setCellValue(screenNameToTotallowIssues.get(entry.getKey()));
				cell5.setCellStyle(style);
				String status = entry.getValue() > 0 ? "Failed" : "Passed";
				Cell cell6=row.createCell(6);
				cell6.setCellValue(status);
				cell6.setCellStyle(style);
			}

			// Set column widths
			dashboardSheet.setColumnWidth(0, 10 * 250); // 20 characters width
			dashboardSheet.setColumnWidth(1, 20 * 250); // 200 units width
			dashboardSheet.setColumnWidth(2, 20 * 250); // 20 characters width
			dashboardSheet.setColumnWidth(3, 20 * 250); // 20 characters width
			dashboardSheet.setColumnWidth(4, 20 * 250); // 200 units width
			dashboardSheet.setColumnWidth(5, 20 * 250); // 20 characters width
			dashboardSheet.setColumnWidth(6, 20 * 250); // 20 characters width


		} catch (Exception e) {
			e.printStackTrace();
		}

	}


	public static void createBarChart(XSSFWorkbook workbook) {                

		try { 
			Map<String, Integer> sheetIssueCounts = new HashMap<>();
			XSSFSheet sheet1 = workbook.getSheet("Dashboard");
			XSSFSheet barSheet = workbook.getSheet("Graph");
			
			Iterator<Row> rowIterator1 = sheet1.iterator(); 			
			while(rowIterator1.hasNext()) {				
				Row row = rowIterator1.next();
				if(row.getRowNum()>0) {
					String screenName = row.getCell(1).getStringCellValue();
					double totalIssues = row.getCell(2).getNumericCellValue();		
					sheetIssueCounts.put(screenName, (int) totalIssues);
				}
			}
            List<Map.Entry<String, Integer>> sortedSheets = sortSheetsByIssueCount(sheetIssueCounts);

            DefaultCategoryDataset dataset = new DefaultCategoryDataset();
            
            sortSheet(sortedSheets, dataset);
			createJFreeChart(workbook, barSheet, "Top 5 Screens","Screen Name","Issue Count",3,3,dataset);		

		
				
			Map<String, Integer> dataMap2 = new HashMap<>();
			DefaultCategoryDataset barChartdataset2 = new DefaultCategoryDataset();

			for (int i = 2; i < workbook.getNumberOfSheets(); i++) {
				XSSFSheet sheet2 = workbook.getSheetAt(i);
				logger.info("Readig data from sheet :{} ",sheet2.getSheetName());
				Iterator<Row> iterator2 = sheet2.iterator();
				if(iterator2.hasNext()) {
					iterator2.next();
				}
				while(iterator2.hasNext()) {
					Row row = iterator2.next();
					Cell screenameCell = row.getCell(0);
					Cell impactCell = row.getCell(2);						
					if (screenameCell != null && impactCell != null) {
						String impact = impactCell.getStringCellValue();
						dataMap2.put(impact, dataMap2.getOrDefault(impact, 0)+1);
					}

				}
				for (Entry<String, Integer> entry : dataMap2.entrySet()) {
					Integer impactCounts = entry.getValue();
					String impact = entry.getKey();
					barChartdataset2.addValue(impactCounts, "Overall Impact Status", impact);

				}
								
			}
			createJFreeChart(workbook, barSheet, "Overall Impact On Screens","Severity","Issue Count",3,25,barChartdataset2);		

			
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void sortSheet(List<Map.Entry<String, Integer>> sortedSheets, DefaultCategoryDataset dataset) {
		for (int k = 0; k < Math.min(sortedSheets.size(), 5); k++) {
		    Map.Entry<String, Integer> entry = sortedSheets.get(k);
		    dataset.addValue(entry.getValue(),entry.getKey() , entry.getKey());
		}
	}


    private static List<Map.Entry<String, Integer>> sortSheetsByIssueCount(Map<String, Integer> sheetIssueCounts) {
        List<Map.Entry<String, Integer>> sortedList = new ArrayList<>(sheetIssueCounts.entrySet());
        sortedList.sort((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue())); // Descending order
        return sortedList;
    }
	public static void createJFreeChart(XSSFWorkbook workbook, XSSFSheet sheet, String title, String xaxisTitle, String yaxisTitle, int anchorCol, int anchorRow, DefaultCategoryDataset dataset) throws IOException{ 

		JFreeChart barChart = ChartFactory.createBarChart(title,xaxisTitle,yaxisTitle,dataset);

		SwingUtilities.invokeLater(() -> {
		    JFrame frame = new JFrame(title);
		    frame.setLayout(new BorderLayout());
		    frame.add(new ChartPanel(barChart), BorderLayout.CENTER);
		    frame.pack();
		    frame.setLocationRelativeTo(null);
		    frame.setVisible(true);
		});
		
		createTempFileToStoreChartImage(workbook, sheet, barChart, anchorCol, anchorRow);
	}

	public static void createTempFileToStoreChartImage(XSSFWorkbook workbook, XSSFSheet sheet, JFreeChart barChart,int anchorCol, int anchorRow) throws IOException {

	
		File chartFile = File.createTempFile("bar_Chart", ".png");
		FileOutputStream chartOut = new FileOutputStream(chartFile);


		ChartUtils.writeChartAsPNG(chartOut, barChart, 400, 300);
		chartOut.close();

		FileInputStream chartIn = new FileInputStream(chartFile);
		byte[] chartBytes = IOUtils.toByteArray(chartIn);
		chartIn.close();

		 int pictureIdx = workbook.addPicture(chartBytes, Workbook.PICTURE_TYPE_PNG);
		CreationHelper helper = workbook.getCreationHelper();
		Drawing drawing = sheet.createDrawingPatriarch();
		ClientAnchor anchor = helper.createClientAnchor();
		anchor.setCol1(anchorCol);
		anchor.setRow1(anchorRow);

		Picture pict = drawing.createPicture(anchor, pictureIdx);
		pict.resize();
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
	
}
