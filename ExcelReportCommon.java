package report_utilities.ExcelReport;


import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import report_utilities.Model.ExtentModel.TestStepDetails;
import report_utilities.Model.ExtentModel.TestStepDetails.TestStepType;
import report_utilities.TestResultModel.BrowserResult;
import report_utilities.TestResultModel.ModuleResult;
import report_utilities.TestResultModel.TestCaseResult;
import report_utilities.common.ReportCommon;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class ExcelReportCommon {

	private static final Logger logger =LoggerFactory.getLogger(ExcelReportCommon.class.getName());
	public Workbook createWorkbook() throws Exception
	{
		
        Workbook workbook = new XSSFWorkbook();     // new HSSFWorkbook() for generating `.xls` file

        return workbook;
	}
	
	public CreationHelper createExcelHelper(Workbook workbook)throws Exception
	{
		CreationHelper createHelper = workbook.getCreationHelper();
		return createHelper;
		
	}
	
	/**
	 * @param workbook
	 * @param sheetName
	 */
	public Sheet createSheet(Workbook workbook,String sheetName) {
		  Sheet sheet = workbook.createSheet(sheetName);
		  return sheet;
	}
	
	public void createHeader(Workbook workbook, Sheet sheet,ArrayList<String> columns,ArrayList<Integer> columnSize ,int startRow)
	{
	        // Create a Row
	        Row headerRow = sheet.createRow(startRow);
	        CellStyle headerStyle= getHeaderCellStyle(workbook);
	        // Creating cells
	        for(int i = 0; i < columns.size(); i++) {
	            Cell cell = headerRow.createCell(i);
	            cell.setCellValue(columns.get(i));
	            cell.setCellStyle(headerStyle);
	            sheet.setColumnWidth(i, columnSize.get(i));
	        }

	}
	
	public void addTestStepDetails(Workbook workbook, Sheet sheet,CreationHelper createHelper,ArrayList<TestStepDetails> rowdata)
	{
	
        int rowNum = 1;
        int sNumber = 1;
        for(TestStepDetails teststepmodel : rowdata) {
            Row row = sheet.createRow(rowNum++);


            if((teststepmodel.testStepType== TestStepType.Module) || 
            	(teststepmodel.testStepType== TestStepType.Screen)|| 
            	(teststepmodel.testStepType== TestStepType.Module_Screen) )
            {
            	String stepDetails="";
            
            	if(teststepmodel.testStepType== TestStepType.Module)
            	{
            		stepDetails="Module : "+ teststepmodel.ModuleName;
            	}

            	if(teststepmodel.testStepType== TestStepType.Screen)
            	{
            		stepDetails="Screen : "+ teststepmodel.ScreenName;
            	}

            	if(teststepmodel.testStepType== TestStepType.Module_Screen)
            	{
            		stepDetails="Module : "+ teststepmodel.ModuleName
            				+ " ===> Screen : "+ teststepmodel.ScreenName;
            	}

    	        Cell cell1 = row.createCell(0);
    	        cell1.setCellStyle(getLeftBorderStyle(workbook));

    	        Cell cell2 = row.createCell(1);
    	        cell2.setCellStyle(getNoSideBorderStyle(workbook));

    	        Cell cell3 = row.createCell(2);
    	        cell3.setCellValue(stepDetails);
    	        cell3.setCellStyle(getNoSideBorderStyle(workbook));

    	        Cell cell4 = row.createCell(3);
    	        cell4.setCellStyle(getNoSideBorderStyle(workbook));

    	        Cell cell5 = row.createCell(4);
    	        cell5.setCellStyle(getNoSideBorderStyle(workbook));


    	        Cell cell6 = row.createCell(5);
    	        cell6.setCellStyle(getNoSideBorderStyle(workbook));

    	        Cell cell7 = row.createCell(6);
    	        cell7.setCellStyle(getNoSideBorderStyle(workbook));

    	        Cell cell8 = row.createCell(7);
    	        cell8.setCellStyle(getNoSideBorderStyle(workbook));


    	        Cell cell9 = row.createCell(8);
    	        cell9.setCellStyle(getNoSideBorderStyle(workbook));

    	        Cell cell10 = row.createCell(9);
    	        cell10.setCellStyle(getRightBorderStyle(workbook));

            }
            else
            {
            
            CellStyle rowCellStyle =getGenericCellStyle(workbook);
            
	        Cell sNo = row.createCell(0);
            sNo.setCellValue(sNumber++);
            sNo.setCellStyle(getGenericCellStyleMiddle(workbook));


	        Cell stepName = row.createCell(1);
	        stepName.setCellValue(teststepmodel.StepName);
	        stepName.setCellStyle(rowCellStyle);

            
            row.createCell(2)
            .setCellValue(teststepmodel.StepDescription);
            
            Cell stepDesc = row.createCell(2); 
            stepDesc.setCellValue(teststepmodel.StepDescription);
            stepDesc.setCellStyle(rowCellStyle);


            if(teststepmodel.ExtentStatus.name().toUpperCase().contains("PASS"))
            {
            	   Cell passStatus = row.createCell(3);
            	   passStatus.setCellValue(teststepmodel.ExtentStatus.name());
            	   passStatus.setCellStyle(getPassCellStyle(workbook));
            	
            }

            else if(teststepmodel.ExtentStatus.name().toUpperCase().contains("FAIL"))
            {
            	   Cell failStatus = row.createCell(3);
            	   failStatus.setCellValue(teststepmodel.ExtentStatus.name());
            	   failStatus.setCellStyle(getFailCellStyle(workbook));
            	
            }
            else if(teststepmodel.ExtentStatus.name().toUpperCase().contains("INFO"))
            {
         	   Cell passStatus = row.createCell(3);
         	   passStatus.setCellValue("DONE");
         	   passStatus.setCellStyle(getGenericCellStyleMiddle(workbook));
         	
         }
            else
            {
                
                
                Cell status = row.createCell(3);
                status.setCellValue(teststepmodel.ExtentStatus.name());
                status.setCellStyle(getGenericCellStyleMiddle(workbook));


            }
             
            ReportCommon rcommon= new ReportCommon();
            
            Cell startTime = row.createCell(4);
            startTime.setCellValue(rcommon.ConvertLocalDateTimetoSQLDateTime(teststepmodel.StartTime));
            startTime.setCellStyle(getDateTimeCellStyle(workbook, createHelper));

            Cell endTime = row.createCell(5);
            endTime.setCellValue(rcommon.ConvertLocalDateTimetoSQLDateTime(teststepmodel.EndTime));
            endTime.setCellStyle(getDateTimeCellStyle(workbook, createHelper));

            
            Cell duration = row.createCell(6);
            duration.setCellValue(rcommon.getDuration(teststepmodel.StartTime, teststepmodel.EndTime));
            duration.setCellStyle(getGenericCellStyleMiddle(workbook));
            

            if(!teststepmodel.ScreenShotData.equals(""))
            {
            	
                Cell screenshot = row.createCell(7);
                
                try
                {
                screenshot.setCellValue("Screenshot");
                screenshot.setHyperlink(getHyperLinkScreenshot(workbook, createHelper, teststepmodel.ScreenShotData));
                screenshot.setCellStyle(getHyperLinkCellStyle(workbook));
                }
                catch(Exception e)
                {
                    screenshot.setCellValue("Missing Screenshot");
                  
                    screenshot.setCellStyle(rowCellStyle);
                	
                }
                
            }
            else
            {
            	
            	
                Cell screenshot = row.createCell(7);
                screenshot.setCellValue("N\\A");
                screenshot.setCellStyle(getGenericCellStyleMiddle(workbook));

            }
            
            if(!teststepmodel.ErrorMessage.equals(""))
            {
            	
                Cell errorMessage = row.createCell(8);
                errorMessage.setCellValue(teststepmodel.ErrorMessage);
                errorMessage.setCellStyle(rowCellStyle);
            }
            else
            {
                Cell errorMessage = row.createCell(8);
                errorMessage.setCellValue("N\\A");
                errorMessage.setCellStyle(getGenericCellStyleMiddle(workbook));

            }
            
            if(!teststepmodel.ErrorDetails.equals(""))
            {
                Cell errorDetails = row.createCell(9);
                errorDetails.setCellValue(teststepmodel.ErrorDetails);
                errorDetails.setCellStyle(rowCellStyle);

            }
            else
            {
            	 Cell errorDetails = row.createCell(9);
                 errorDetails.setCellValue("N\\A");
                 errorDetails.setCellStyle(getGenericCellStyleMiddle(workbook));           }
            
        }
        }
	}
	
	public void addTestCaseDetails(Workbook workbook, Sheet sheet,CreationHelper createHelper,ArrayList<TestCaseResult> testCaseResults)
	{
        int rowNum = 1;
        for(TestCaseResult testCaseResult : testCaseResults) {
            Row row = sheet.createRow(rowNum++);

            row.createCell(0)
            .setCellValue(rowNum-1);

            Cell sNO = row.createCell(0);
            sNO.setCellValue(rowNum-1);
            sNO.setCellStyle(getGenericCellStyleMiddle(workbook));
            
     	   Cell tcName = row.createCell(1);
     	  tcName.setCellValue(testCaseResult.TestCaseName);
		try
		{
			tcName.setCellStyle(getHyperLinkCellStyle(workbook));
			Hyperlink link = createHelper.createHyperlink(HyperlinkType.DOCUMENT);
			link.setAddress("'" + testCaseResult.TestCaseName+"'!A1");
			tcName.setHyperlink(link);
			

		}
		catch(Exception e)
		{
			logger.info(e.getMessage());
			logger.info(Arrays.toString(e.getStackTrace()));

		}
		
		  Cell tcDesc = row.createCell(2);
		  tcDesc.setCellValue(testCaseResult.TestCaseDescription);
		  tcDesc.setCellStyle(getGenericCellStyle(workbook));
     	 

		  Cell module = row.createCell(3);
		  module.setCellValue(testCaseResult.Module);
		  module.setCellStyle(getGenericCellStyleMiddle(workbook));
		  
		  
		  Cell testcaseCategory = row.createCell(4);
		  testcaseCategory.setCellValue(testCaseResult.TestCaseCategory);
		  testcaseCategory.setCellStyle(getGenericCellStyleMiddle(workbook));
		  
		  Cell caseNumber = row.createCell(5);
		  caseNumber.setCellValue(testCaseResult.CaseNumber);
		  caseNumber.setCellStyle(getGenericCellStyleMiddle(workbook));
		  
		  Cell applicationNumber = row.createCell(6);
		  applicationNumber.setCellValue(testCaseResult.ApplicationNumber);
		  applicationNumber.setCellStyle(getGenericCellStyleMiddle(workbook));

		  Cell browser = row.createCell(7);
		  browser.setCellValue(testCaseResult.Browser);
		  browser.setCellStyle(getGenericCellStyleMiddle(workbook));

		  
	
            if(testCaseResult.TestCaseStatus.equalsIgnoreCase("PASS"))
            {
            	   Cell passStatus = row.createCell(8);
            	   passStatus.setCellValue(testCaseResult.TestCaseStatus);
            	   passStatus.setCellStyle(getPassCellStyle(workbook));
            	
            }

            else if(testCaseResult.TestCaseStatus.equalsIgnoreCase("FAIL"))
            {
            	   Cell failStatus = row.createCell(8);
            	   failStatus.setCellValue(testCaseResult.TestCaseStatus);
            	   failStatus.setCellStyle(getFailCellStyle(workbook));
            	
            }
            else
            {

      		  Cell testcaseStatus = row.createCell(8);
      		testcaseStatus.setCellValue(testCaseResult.TestCaseStatus);
      		testcaseStatus.setCellStyle(getGenericCellStyleMiddle(workbook));

                

            }
            ReportCommon rcommon=new ReportCommon();
            
            Cell startTime = row.createCell(9);
            startTime.setCellValue(rcommon.ConvertLocalDateTimetoSQLDateTime(testCaseResult.StartTime));
            startTime.setCellStyle(getDateTimeCellStyle(workbook, createHelper));

            Cell endTime = row.createCell(10);
            endTime.setCellValue(rcommon.ConvertLocalDateTimetoSQLDateTime(testCaseResult.EndTime));
            endTime.setCellStyle(getDateTimeCellStyle(workbook, createHelper));
 

    		  Cell duration = row.createCell(11);
    		  duration.setCellValue(testCaseResult.Duration);
    		  duration.setCellStyle(getGenericCellStyleMiddle(workbook));
    		  
    		
    		  
        
        }

	}

	public void addTestModuleDetails(Workbook workbook, Sheet sheet,CreationHelper createHelper,ArrayList<ModuleResult> moduleResults)
	{
        int rowNum = 1;
        for(ModuleResult moduleResult : moduleResults) {
            Row row = sheet.createRow(rowNum++);
            
            ExcelRunSettings.dashboardRowCounter=rowNum;

            Cell sNo = row.createCell(0);
            sNo.setCellValue(rowNum-1);
            sNo.setCellStyle(getGenericCellStyleMiddle(workbook));
            

            Cell module = row.createCell(1);
            module.setCellValue(moduleResult.Module);
            module.setCellStyle(getGenericCellStyleMiddle(workbook));
            

            Cell tcTotalCount = row.createCell(2);
            tcTotalCount.setCellValue(moduleResult.TCTotalCount);
            tcTotalCount.setCellStyle(getGenericCellStyleMiddle(workbook));

            

            Cell tcPassCount = row.createCell(3);
            tcPassCount.setCellValue(moduleResult.TCPassCount);
            tcPassCount.setCellStyle(getGenericCellStyleMiddle(workbook));

            

            Cell tcFailCount = row.createCell(4);
            tcFailCount.setCellValue(moduleResult.TCFailCount);
            tcFailCount.setCellStyle(getGenericCellStyleMiddle(workbook));
            
            

            Cell tcSkippedCount = row.createCell(5);
            tcSkippedCount.setCellValue(moduleResult.TCSkippedCount);
            tcSkippedCount.setCellStyle(getGenericCellStyleMiddle(workbook));

        }
	}

	public void addTestBrowserDetails(Workbook workbook, Sheet sheet,CreationHelper createHelper,ArrayList<BrowserResult> browserResults)
	{
		ExcelRunSettings.dashboardRowCounter=ExcelRunSettings.dashboardRowCounter+1;
		int rowNum = ExcelRunSettings.dashboardRowCounter;
		int browserRowNo=1;
        for(BrowserResult browserResult : browserResults) {
            Row row = sheet.createRow(rowNum++);
            
       
            Cell sNo = row.createCell(0);
            sNo.setCellValue(browserRowNo++);
            sNo.setCellStyle(getGenericCellStyleMiddle(workbook));
            

            Cell module = row.createCell(1);
            module.setCellValue(browserResult.Browser);
            module.setCellStyle(getGenericCellStyleMiddle(workbook));
            

            Cell tcTotalCount = row.createCell(2);
            tcTotalCount.setCellValue(browserResult.TCTotalCount);
            tcTotalCount.setCellStyle(getGenericCellStyleMiddle(workbook));

            

            Cell tcPassCount = row.createCell(3);
            tcPassCount.setCellValue(browserResult.TCPassCount);
            tcPassCount.setCellStyle(getGenericCellStyleMiddle(workbook));

            

            Cell tcFailCount = row.createCell(4);
            tcFailCount.setCellValue(browserResult.TCFailCount);
            tcFailCount.setCellStyle(getGenericCellStyleMiddle(workbook));
            
            

            Cell tcSkippedCount = row.createCell(5);
            tcSkippedCount.setCellValue(browserResult.TCSkippedCount);
            tcSkippedCount.setCellStyle(getGenericCellStyleMiddle(workbook));

        
        
        }

	}

	public CellStyle getDateCellStyle(Workbook workbook,CreationHelper createHelper)
	{
        CellStyle dateCellStyle = workbook.createCellStyle();
        dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd-MM-yyyy"));
        dateCellStyle.setBorderTop(BorderStyle.THICK);
        dateCellStyle.setBorderLeft(BorderStyle.THICK);
        dateCellStyle.setBorderRight(BorderStyle.THICK);
        dateCellStyle.setBorderBottom(BorderStyle.THICK);
        dateCellStyle.setAlignment(HorizontalAlignment.CENTER);
        return dateCellStyle;
	}

	
	
	public CellStyle getDateTimeCellStyle(Workbook workbook,CreationHelper createHelper)
	{
        CellStyle dateCellStyle = workbook.createCellStyle();
        dateCellStyle.setDataFormat(createHelper.createDataFormat().getFormat("dd-MM-yyyy HH:MM:SS.SSS"));
        dateCellStyle.setBorderTop(BorderStyle.THICK);
        dateCellStyle.setBorderLeft(BorderStyle.THICK);
        dateCellStyle.setBorderRight(BorderStyle.THICK);
        dateCellStyle.setBorderBottom(BorderStyle.THICK);
        dateCellStyle.setAlignment(HorizontalAlignment.CENTER);
        return dateCellStyle;
	}

	public CellStyle getPassCellStyle(Workbook workbook)
	{
	     Font passFont = workbook.createFont();
	     passFont.setBold(false);
	     passFont.setFontHeightInPoints((short) 9);
	     passFont.setFontName("Verdana");
	     passFont.setColor(IndexedColors.DARK_GREEN.getIndex());

	        // Create a CellStyle with the font
	        CellStyle passCellStyle = workbook.createCellStyle();
	        passCellStyle.setFont(passFont);
	        passCellStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
	        
	        passCellStyle.setFillBackgroundColor(IndexedColors.LIGHT_GREEN.getIndex());
	        passCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
	        passCellStyle.setAlignment(HorizontalAlignment.CENTER);
	        passCellStyle.setBorderTop(BorderStyle.THICK);
	        passCellStyle.setBorderLeft(BorderStyle.THICK);
	        passCellStyle.setBorderRight(BorderStyle.THICK);
	        passCellStyle.setBorderBottom(BorderStyle.THICK);
	        passCellStyle.setAlignment(HorizontalAlignment.CENTER);
	return passCellStyle;
	}

	public CellStyle getFailCellStyle(Workbook workbook)
	{
	     Font failFont = workbook.createFont();
	     failFont.setBold(false);
	     failFont.setFontHeightInPoints((short) 9);
	     failFont.setFontName("Verdana");
	     failFont.setColor(IndexedColors.DARK_RED.getIndex());

	        // Create a CellStyle with the font
	        CellStyle failCellStyle = workbook.createCellStyle();
	        failCellStyle.setFont(failFont);
	        failCellStyle.setFillBackgroundColor(IndexedColors.RED.getIndex());
	        failCellStyle.setFillForegroundColor(IndexedColors.RED.getIndex());
	        failCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

	        failCellStyle.setBorderTop(BorderStyle.THICK);
	        failCellStyle.setBorderLeft(BorderStyle.THICK);
	        failCellStyle.setBorderRight(BorderStyle.THICK);
	        failCellStyle.setBorderBottom(BorderStyle.THICK);
	        failCellStyle.setAlignment(HorizontalAlignment.CENTER);
	        return failCellStyle;
	}

	
	public CellStyle getHyperLinkCellStyle(Workbook workbook)
	{
	     Font hLinkFont = workbook.createFont();
	     hLinkFont.setBold(false);
	     hLinkFont.setFontHeightInPoints((short) 9);
	     hLinkFont.setFontName("Verdana");
	     hLinkFont.setColor(IndexedColors.BLUE.getIndex());
	     
	     hLinkFont.setUnderline(Font.U_SINGLE);

	        // Create a CellStyle with the font
	        CellStyle hlinkCellStyle = workbook.createCellStyle();
	        hlinkCellStyle.setFont(hLinkFont);
	        hlinkCellStyle.setBorderTop(BorderStyle.THICK);
	        hlinkCellStyle.setBorderLeft(BorderStyle.THICK);
	        hlinkCellStyle.setBorderRight(BorderStyle.THICK);
	        hlinkCellStyle.setBorderBottom(BorderStyle.THICK);
	        hlinkCellStyle.setAlignment(HorizontalAlignment.CENTER);
	        return hlinkCellStyle;
	}

	
	public CellStyle getGenericCellStyle(Workbook workbook)
	{
		
        Font rowFont = workbook.createFont();
        rowFont.setBold(false);
        rowFont.setFontHeightInPoints((short) 9);
        rowFont.setFontName("Verdana");
        rowFont.setColor(IndexedColors.BLACK.getIndex());

        CellStyle rowCellStyle = workbook.createCellStyle();
        rowCellStyle.setFont(rowFont);
        rowCellStyle.setBorderTop(BorderStyle.THICK);
        rowCellStyle.setBorderLeft(BorderStyle.THICK);
        rowCellStyle.setBorderRight(BorderStyle.THICK);
        rowCellStyle.setBorderBottom(BorderStyle.THICK);
        rowCellStyle.setAlignment(HorizontalAlignment.LEFT);
        rowCellStyle.setWrapText(true);
		return rowCellStyle;
    

	}

	public CellStyle getGenericCellStyleMiddle(Workbook workbook)
	{
		
        Font rowFont = workbook.createFont();
        rowFont.setBold(false);
        rowFont.setFontHeightInPoints((short) 9);
        rowFont.setFontName("Verdana");
        rowFont.setColor(IndexedColors.BLACK.getIndex());

        CellStyle rowCellStyle = workbook.createCellStyle();
        rowCellStyle.setFont(rowFont);
        rowCellStyle.setBorderTop(BorderStyle.THICK);
        rowCellStyle.setBorderLeft(BorderStyle.THICK);
        rowCellStyle.setBorderRight(BorderStyle.THICK);
        rowCellStyle.setBorderBottom(BorderStyle.THICK);
        rowCellStyle.setWrapText(true);
        rowCellStyle.setAlignment(HorizontalAlignment.CENTER);
		return rowCellStyle;
    

	}

	
	public CellStyle getLeftBorderStyle(Workbook workbook)
	{
		
        Font rowFont = workbook.createFont();
        rowFont.setBold(false);
        rowFont.setFontHeightInPoints((short) 9);
        rowFont.setFontName("Verdana");
        rowFont.setColor(IndexedColors.BLACK.getIndex());

        CellStyle rowCellStyle = workbook.createCellStyle();
        rowCellStyle.setFillBackgroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
        rowCellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        rowCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        rowCellStyle.setFont(rowFont);
        rowCellStyle.setBorderTop(BorderStyle.THICK);
        rowCellStyle.setBorderLeft(BorderStyle.THICK);
        rowCellStyle.setBorderRight(BorderStyle.NONE);
        rowCellStyle.setBorderBottom(BorderStyle.THICK);
        rowCellStyle.setWrapText(false);
        rowCellStyle.setAlignment(HorizontalAlignment.CENTER);
		return rowCellStyle;
    

	}

	
	public CellStyle getRightBorderStyle(Workbook workbook)
	{
		
        Font rowFont = workbook.createFont();
        rowFont.setBold(false);
        rowFont.setFontHeightInPoints((short) 9);
        rowFont.setFontName("Verdana");
        rowFont.setColor(IndexedColors.BLACK.getIndex());

        CellStyle rowCellStyle = workbook.createCellStyle();
        rowCellStyle.setFillBackgroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
        rowCellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        rowCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        rowCellStyle.setFont(rowFont);
        rowCellStyle.setBorderTop(BorderStyle.THICK);
        rowCellStyle.setBorderLeft(BorderStyle.NONE);
        rowCellStyle.setBorderRight(BorderStyle.THICK);
        rowCellStyle.setBorderBottom(BorderStyle.THICK);
        rowCellStyle.setWrapText(false);
        rowCellStyle.setAlignment(HorizontalAlignment.CENTER);
		return rowCellStyle;
  

	}

	
	
	public CellStyle getNoSideBorderStyle(Workbook workbook)
	{
		
        Font rowFont = workbook.createFont();
        rowFont.setBold(true);
        rowFont.setFontHeightInPoints((short) 9);
        rowFont.setFontName("Verdana");
        rowFont.setColor(IndexedColors.BLACK.getIndex());

        
        CellStyle rowCellStyle = workbook.createCellStyle();
        rowCellStyle.setFillBackgroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
        rowCellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        rowCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        rowCellStyle.setFont(rowFont);
        rowCellStyle.setBorderTop(BorderStyle.THICK);
        rowCellStyle.setBorderLeft(BorderStyle.NONE);
        rowCellStyle.setBorderRight(BorderStyle.NONE);
        rowCellStyle.setBorderBottom(BorderStyle.THICK);
        rowCellStyle.setWrapText(false);
        rowCellStyle.setAlignment(HorizontalAlignment.CENTER);
		return rowCellStyle;
  

	}

	public CellStyle getHeaderCellStyle(Workbook workbook)
	{
		try {
	     Font headerFont = workbook.createFont();
	        headerFont.setBold(true);
	        headerFont.setFontHeightInPoints((short) 9);
	        headerFont.setFontName("Verdana");
	        headerFont.setColor(IndexedColors.BLACK.getIndex());

	        // Create a CellStyle with the font
	        CellStyle headerCellStyle = workbook.createCellStyle();
	        headerCellStyle.setFont(headerFont);
	        headerCellStyle.setFillBackgroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
	        headerCellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
	        headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
	        headerCellStyle.setBorderTop(BorderStyle.THICK);
	        headerCellStyle.setBorderLeft(BorderStyle.THICK);
	        headerCellStyle.setBorderRight(BorderStyle.THICK);
	        headerCellStyle.setBorderBottom(BorderStyle.THICK);
	        headerCellStyle.setAlignment(HorizontalAlignment.CENTER);

	        return headerCellStyle;
		}
		catch(Exception e) 
		{
			logger.info(e.getMessage());
			logger.info(Arrays.toString(e.getStackTrace()));

			throw e;
		}
	        

	}
	public Hyperlink getHyperLinkSheet(Workbook workbook,CreationHelper createHelper,String sheetName)
	{
		Hyperlink link = createHelper.createHyperlink(HyperlinkType.DOCUMENT);
		link.setAddress("'" + sheetName+"'!A1");
		return link;
	}

	

	public Hyperlink getHyperLinkScreenshot(Workbook workbook,CreationHelper createHelper,String path)
	{
		Hyperlink link = createHelper.createHyperlink(HyperlinkType.FILE);
		path= path.replace("\\", "/");
		logger.info(path);
		link.setAddress(path);
		return link;
	}

	
	public void closeWorkBook(Workbook workbook, String filePath) throws Exception
	{
		try 
		{
			FileOutputStream fileOut = new FileOutputStream(filePath);
	        workbook.write(fileOut);
	        fileOut.close();
	        workbook.close();
		}
		catch(Exception e) 
		{
			logger.info(e.getMessage());
			logger.info(Arrays.toString(e.getStackTrace()));

			throw e;
			
		}
        
	}
	
}
