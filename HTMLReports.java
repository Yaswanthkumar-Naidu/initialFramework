package report_utilities.htmlreport;

import com.aventstack.extentreports.ExtentTest;

import report_utilities.Constants.HTMLReportContants;
import report_utilities.Constants.ReportContants;
import report_utilities.Model.ExtentModel.TestStepDetails;
import report_utilities.Model.html.HTMLTCLiveModel;
import report_utilities.TestResultModel.BrowserResult;
import report_utilities.TestResultModel.ModuleResult;
import report_utilities.TestResultModel.TestCaseResult;
import report_utilities.common.ReportCommon;
import report_utilities.Model.TestCaseParam;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HTMLReports {

	private static final Logger logger =LoggerFactory.getLogger(HTMLReports.class.getName());
	
	protected static final Map<String, String> TCHTMLMapping = new HashMap<>();	


	public void generateReport(String reportPath)
	{
		try {
			
			logger.info("Initial ReportPath: {}", reportPath);

			reportPath = reportPath + HTMLReportContants.FILE_HTML_SUMMARY;
			String reportPath2 = reportPath + File.separator + ".." + File.separator + ".." + File.separator + ".." + HTMLReportContants.FILE_HTML_SUMMARY;

			logger.info("Final ReportPath: {}", reportPath);
			logger.info("Alternate ReportPath2: {}", reportPath2);
			
			StringBuilder sb = new StringBuilder();
			String moduleResults = "";
			int i = 1;
			for (ModuleResult moduleResult : ReportContants.moduleResults) {
				String modResult = HTMLReportContants.PlaceHolder_HTMLSummary_ModuleRow;

				modResult = modResult.replaceAll(HTMLReportContants.SUMMARY_MODULE_BROWSER_SNO, String.valueOf(i));
				modResult = modResult.replaceAll(HTMLReportContants.SUMMARY_MODULE_BROWSER_MODULE, String.valueOf(moduleResult.Module));
				modResult = modResult.replaceAll(HTMLReportContants.SUMMARY_MODULE_BROWSER_TOTAL_TC, String.valueOf(moduleResult.TCTotalCount));
				modResult = modResult.replaceAll(HTMLReportContants.SUMMARY_MODULE_BROWSER_PASSED_TC, String.valueOf(moduleResult.TCPassCount));
				modResult = modResult.replaceAll(HTMLReportContants.SUMMARY_MODULE_BROWSER_FAILED_TC, String.valueOf(moduleResult.TCFailCount));
				modResult = modResult.replaceAll(HTMLReportContants.SUMMARY_MODULE_BROWSER_SKIPPED_TC, String.valueOf(moduleResult.TCSkippedCount));
				sb.append(moduleResults);
				sb.append(modResult);
				moduleResults = sb.toString();
				Thread.sleep(0);
				i++;
			}
			StringBuilder sbbr = new StringBuilder();
			String browserResults = "";
			i = 1;
			for (BrowserResult browserResult : ReportContants.browserResults) {
				String brResult = HTMLReportContants.PlaceHolder_HTMLSummary_BrowserRow;

				brResult = brResult.replaceAll(HTMLReportContants.SUMMARY_MODULE_BROWSER_SNO, String.valueOf(i));
				brResult = brResult.replaceAll(HTMLReportContants.SUMMARY_MODULE_BROWSER_MODULE, String.valueOf(browserResult.Browser));
				brResult = brResult.replaceAll(HTMLReportContants.SUMMARY_MODULE_BROWSER_TOTAL_TC, String.valueOf(browserResult.TCTotalCount));
				brResult = brResult.replaceAll(HTMLReportContants.SUMMARY_MODULE_BROWSER_PASSED_TC, String.valueOf(browserResult.TCPassCount));
				brResult = brResult.replaceAll(HTMLReportContants.SUMMARY_MODULE_BROWSER_FAILED_TC, String.valueOf(browserResult.TCFailCount));
				brResult = brResult.replaceAll(HTMLReportContants.SUMMARY_MODULE_BROWSER_SKIPPED_TC, String.valueOf(browserResult.TCSkippedCount));
				sbbr.append(browserResults);
				sbbr.append(brResult);
				browserResults = sbbr.toString();
				i++;
			}

			ReportCommon reportCommon = new ReportCommon();

			StringBuilder sbtcr = new StringBuilder();
			String testCaseResults = "";
			i = 1;
			for (TestCaseResult testCaseResult : ReportContants.testcaseResults) {
				String tcResult = HTMLReportContants.PlaceHolder_HTMLSummary_TCRow;

				tcResult = tcResult.replaceAll(HTMLReportContants.SUMMARY_TC_SNO, String.valueOf(i));
				tcResult = tcResult.replaceAll(HTMLReportContants.SUMMARY_TC_TC_NAME, testCaseResult.TestCaseName);
				tcResult = tcResult.replaceAll(HTMLReportContants.SUMMARY_TC_TC_DESC, testCaseResult.TestCaseDescription);
				tcResult = tcResult.replaceAll(HTMLReportContants.SUMMARY_TC_MODULE, testCaseResult.Module);
				tcResult = tcResult.replaceAll(HTMLReportContants.SUMMARY_TC_BROWSER, testCaseResult.Browser);
				tcResult = tcResult.replaceAll(HTMLReportContants.SUMMARY_TC_STATUS, testCaseResult.TestCaseStatus);
				tcResult = tcResult.replaceAll(HTMLReportContants.SUMMARY_TC_START_TIME, reportCommon.convertLocalDateTimetoSQLDateTime(testCaseResult.StartTime));
				tcResult = tcResult.replaceAll(HTMLReportContants.SUMMARY_TC_END_TIME, reportCommon.convertLocalDateTimetoSQLDateTime(testCaseResult.EndTime));
				tcResult = tcResult.replaceAll(HTMLReportContants.SUMMARY_TC_DURATION, reportCommon.getTimeDifference(testCaseResult.StartTime, testCaseResult.EndTime));
				sbtcr.append(testCaseResults);
				sbtcr.append(tcResult);
				testCaseResults = sbtcr.toString();
				i++;
			}

			String hTMLResultsSummary = HTMLReportContants.PlaceHolder_HTMLSummary;
			hTMLResultsSummary = hTMLResultsSummary.replaceAll(HTMLReportContants.SUMMARY_DATA_ROW_MODULE, moduleResults);
			hTMLResultsSummary = hTMLResultsSummary.replaceAll(HTMLReportContants.SUMMARY_DATA_ROW_BROWSER, browserResults);
			hTMLResultsSummary = hTMLResultsSummary.replaceAll(HTMLReportContants.SUMMARY_DATA_ROW_TC, testCaseResults);

			writeDataToTextFile(reportPath, hTMLResultsSummary);
			writeDataToTextFile(reportPath2, hTMLResultsSummary);

			writeDataToTextFile(HTMLReportContants.htmlreportsdir + File.separator + HTMLReportContants.SUMMARY_DASHBOARD, HTMLReportContants.SummaryData_HTML);
			writeFileLineByLine(HTMLReportContants.htmlreportsdir + File.separator + HTMLReportContants.SUMMARY_CSS, HTMLReportContants.SummaryData_CSS);
			writeFileLineByLine(HTMLReportContants.htmlreportsdir + File.separator + HTMLReportContants.SUMMARY_JS, HTMLReportContants.HTMLSummaryData_JS);

		} catch (InterruptedException e) {
		    Thread.currentThread().interrupt(); // Restore the interrupted status
		    logger.error("Unable to create the HTML report due to interruption");
		} catch (Exception e) {
		    logger.error("Unable to create the HTML report: {}", e.getMessage());
		}
	}


	public String createTCHTML(String testCase,String moduleName, String browser)
	{
		String tCHTMLPath="";
		String tCHTMLIndex="";
		try
		{
		

		String tcDir=HTMLReportContants.htmlreportsdir +testCase+"_"+ moduleName+ "_" + browser;
		tCHTMLIndex=tcDir+HTMLReportContants.FILE_HTML_INDEX;
		tCHTMLPath=tcDir+ HTMLReportContants.FILE_HTML_TEST_CASE;
		String tCHTMLData=HTMLReportContants.PlaceHolder_HTMLTestCase;
		
		String tSHTMLPath=tcDir+ HTMLReportContants.FILE_TEST_STEP;
		String tSHTMLData=HTMLReportContants.PlaceHolder_TestStepPre;

		String tcIndexPath=tcDir+ HTMLReportContants.FILE_HTML_INDEX;
		
		File dir = new File(tcDir);
        if (!dir.exists()) dir.mkdirs();

        tCHTMLData=tCHTMLData.replaceAll(HTMLReportContants.TC_NAME,testCase);
        tCHTMLData=tCHTMLData.replaceAll(HTMLReportContants.TC_MODULE,moduleName);
        tCHTMLData=tCHTMLData.replaceAll(HTMLReportContants.TC_BROWSER,browser);
        tCHTMLData=tCHTMLData.replaceAll(HTMLReportContants.TC_STATUS,"InProgress");
        tCHTMLData=tCHTMLData.replaceAll(HTMLReportContants.PAGE_REFRESH_TIME_IN_SECONDS_VAR,HTMLReportContants.PageRefreshTimeInSeconds);
        tSHTMLData=tSHTMLData.replaceAll(HTMLReportContants.PAGE_REFRESH_TIME_IN_SECONDS_VAR,HTMLReportContants.PageRefreshTimeInSeconds);
        
        
        writeDataToTextFile(tCHTMLPath, tCHTMLData);
        
        writeDataToTextFile(tSHTMLPath, tSHTMLData);
        
        writeDataToTextFile(tcIndexPath, HTMLReportContants.PlaceHolder_HTMLIndex);
                
        logger.info("<a href='file:///{}'>{}</a>", tSHTMLPath, testCase);

  		}
		catch(Exception e)
		{
		logger.error("{} Unable to initialize the HTML Test Case Reports",e.getMessage());
		}

		return tCHTMLIndex;
	
	}

	
	
	public String createTCHTMLLiveSummary()
	{


		String hTMLTCLiveReportFile=HTMLReportContants.htmlreportsdir +HTMLReportContants.HTML_TC_LIVE_REPORT;

		try
		{
			String tCHTMLLiveReport=HTMLReportContants.PlaceHolder_HTMLTCLiveTemplate;
			writeDataToTextFile(hTMLTCLiveReportFile, tCHTMLLiveReport);

			logger.info("<a href='file:///{}'> Test Cases Details</a>", hTMLTCLiveReportFile);

		}
		catch(Exception e)
		{
			logger.error("{} Unable to initialize the HTML Live Report",e.getMessage());
		}

		return hTMLTCLiveReportFile;

	}
	public String createDashboardHTMLLiveSummary()
	{

		String hTMLTCLiveReportFile=HTMLReportContants.htmlreportsdir +"/" + HTMLReportContants.SUMMARY_DASHBOARD;
		try
		{
			writeDataToTextFile(HTMLReportContants.htmlreportsdir +"/" + HTMLReportContants.SUMMARY_DASHBOARD, HTMLReportContants.SummaryData_HTML);
			writeFileLineByLine(HTMLReportContants.htmlreportsdir +"/" + HTMLReportContants.SUMMARY_CSS, HTMLReportContants.SummaryData_CSS);
			writeFileLineByLine(HTMLReportContants.htmlreportsdir +"/" + HTMLReportContants.SUMMARY_JS, HTMLReportContants.HTMLSummaryData_JS);

			logger.info("<a href='file:///{}'> Live Dashboard</a>", hTMLTCLiveReportFile);
		}
		catch(Exception e)
		{
			logger.error("{} Unable to initialize the HTML Live Report",e.getMessage());
		}

		return hTMLTCLiveReportFile;
	}

	public void createTCHTMLLive(String testCase,String moduleName, String browser, String status,String tcFilePath)
	{
		try
		{
		
	    StringBuilder sb = new StringBuilder();
		String hTMLTCLiveReportFile=HTMLReportContants.htmlreportsdir +HTMLReportContants.HTML_TC_LIVE_REPORT;
		
		String tCHTMLLiveReport=HTMLReportContants.PlaceHolder_HTMLTCLiveTemplate;

		tCHTMLLiveReport=tCHTMLLiveReport.replaceAll(HTMLReportContants.PAGE_REFRESH_TIME_IN_SECONDS_VAR,HTMLReportContants.PageRefreshTimeInSeconds);
		tCHTMLLiveReport=tCHTMLLiveReport.replaceAll(HTMLReportContants.PAGE_REFRESH_TIME_IN_SECONDS_VAR,HTMLReportContants.PageRefreshTimeInSeconds);

		
		ReportContants.htmlTCLiveModels.add(new HTMLTCLiveModel().AddData_HTMLTCLiveModel(testCase, moduleName, browser, status, tcFilePath));


		
		for(HTMLTCLiveModel htmltcLiveModel: ReportContants.htmlTCLiveModels)
		{
			String hTMLTCLiveRow=HTMLReportContants.PlaceHolder_HTMLTCLiveRow;
			hTMLTCLiveRow=hTMLTCLiveRow.replace(HTMLReportContants.TC_LIVE_TC_NAME, htmltcLiveModel.TestCase);
			hTMLTCLiveRow=hTMLTCLiveRow.replace(HTMLReportContants.TC_LIVE_MODULE, htmltcLiveModel.Module);
			hTMLTCLiveRow=hTMLTCLiveRow.replace(HTMLReportContants.TC_LIVE_BROWSER, htmltcLiveModel.Browser);
			hTMLTCLiveRow=hTMLTCLiveRow.replace(HTMLReportContants.TC_LIVE_STATUS, htmltcLiveModel.Status);
			htmltcLiveModel.FilePath = (htmltcLiveModel.FilePath.split("HTMLReports")[1]).substring(1);
			hTMLTCLiveRow=hTMLTCLiveRow.replace(HTMLReportContants.TC_LIVE_FILE_PATH, htmltcLiveModel.FilePath);
			sb.append(tCHTMLLiveReport);
			sb.append(hTMLTCLiveRow);
			tCHTMLLiveReport = sb.toString();	
		}
		
        writeDataToTextFile(hTMLTCLiveReportFile, tCHTMLLiveReport);
        
		}
		catch(Exception e)
		{
		logger.error("{} Unable to initialize the HTML Test Case Report",e.getMessage());
		}
	
		
	
	}


	public void updateTCHTMLLive(String testCase,String moduleName, String browser, String status)
	{
		try
		{
		
	    StringBuilder sb = new StringBuilder();
		String hTMLTCLiveReportFile=HTMLReportContants.htmlreportsdir +HTMLReportContants.HTML_TC_LIVE_REPORT;
		
		String tCHTMLLiveReport=HTMLReportContants.PlaceHolder_HTMLTCLiveTemplate;
		tCHTMLLiveReport=tCHTMLLiveReport.replaceAll(HTMLReportContants.PAGE_REFRESH_TIME_IN_SECONDS_VAR,HTMLReportContants.PageRefreshTimeInSeconds);
		tCHTMLLiveReport=tCHTMLLiveReport.replaceAll(HTMLReportContants.PAGE_REFRESH_TIME_IN_SECONDS_VAR,HTMLReportContants.PageRefreshTimeInSeconds);


		
		ReportContants.htmlTCLiveModels = ReportContants.htmlTCLiveModels.stream()
                .map(row -> {
                    if (row.getTestCase().equals(testCase) && row.getModule().equals(moduleName) && row.getBrowser().equals(browser)) {
                    	row.setStatus(status);
                    }
                    return row;
                })
                .collect(Collectors.toList());

		
		for(HTMLTCLiveModel htmltcLiveModel: ReportContants.htmlTCLiveModels)
		{
			String hTMLTCLiveRow=HTMLReportContants.PlaceHolder_HTMLTCLiveRow;
			hTMLTCLiveRow=hTMLTCLiveRow.replace(HTMLReportContants.TC_LIVE_TC_NAME, htmltcLiveModel.TestCase);
			hTMLTCLiveRow=hTMLTCLiveRow.replace(HTMLReportContants.TC_LIVE_MODULE, htmltcLiveModel.Module);
			hTMLTCLiveRow=hTMLTCLiveRow.replace(HTMLReportContants.TC_LIVE_BROWSER, htmltcLiveModel.Browser);
			hTMLTCLiveRow=hTMLTCLiveRow.replace(HTMLReportContants.TC_LIVE_STATUS, htmltcLiveModel.Status);
			hTMLTCLiveRow=hTMLTCLiveRow.replace(HTMLReportContants.TC_LIVE_FILE_PATH, htmltcLiveModel.FilePath);
			sb.append(tCHTMLLiveReport);
			sb.append(hTMLTCLiveRow);
			tCHTMLLiveReport = sb.toString();
		}
		
		
        writeDataToTextFile(hTMLTCLiveReportFile, tCHTMLLiveReport);
        
		}
		catch(Exception e)
		{
		logger.error("{} Unable to initialize the HTML Test Case Report",e.getMessage());
		}
	
	}

	

	public void addTestStepsLogs(TestCaseParam testCaseParam, TestStepDetails tsDetails)
	{

		try
		{
			String tCDir=HTMLReportContants.htmlreportsdir +testCaseParam.TestCaseName +"_"+ testCaseParam.ModuleName+ "_" + testCaseParam.Browser;
			String sCHTMLPath=tCDir+ HTMLReportContants.FILE_SCREEN_SHOT;
			String sCHTMLData=HTMLReportContants.PlaceHolder_ScreenShot;
			
			String tSHTMLPath=tCDir+ HTMLReportContants.FILE_TEST_STEP;
			String tSHTMLData=HTMLReportContants.PlaceHolder_CurrentTestStep;
		

			sCHTMLData=sCHTMLData.replaceAll(HTMLReportContants.SCR_STEP_NAME,tsDetails.StepName);
			sCHTMLData=sCHTMLData.replaceAll(HTMLReportContants.PAGE_REFRESH_TIME_IN_SECONDS_VAR,HTMLReportContants.PageRefreshTimeInSeconds);

			
	        writeDataToTextFile(sCHTMLPath, sCHTMLData);
			
	        tSHTMLData=tSHTMLData.replaceAll(HTMLReportContants.TS_STEP_NAME,tsDetails.StepName);
	        tSHTMLData=tSHTMLData.replaceAll(HTMLReportContants.TS_STEP_DESC,tsDetails.StepDescription);
	        tSHTMLData=tSHTMLData.replaceAll(HTMLReportContants.TS_STATUS,tsDetails.testCaseStatus);
	        if(tsDetails.ScreenShotData.equals(""))
	        {
	        tSHTMLData=tSHTMLData.replaceAll(HTMLReportContants.tsScreenshot,"N/A");
	        }
	        else
	        {
	        	
	        	String scrLink=writeImageToReport(tsDetails.ScreenShotData,"Screenshot");
	        	
		        tSHTMLData=tSHTMLData.replaceAll(HTMLReportContants.tsScreenshot,scrLink);
		        String dele = "/";
		        String screenshotPath=tCDir+ dele + HTMLReportContants.FILE_SCREEN_SHOT_IMAGE;
		        copyFileUsingStream(tsDetails.ScreenShotData, screenshotPath);
		        
	        }
	        
	        ReportCommon reportCommon =new ReportCommon();
	        
	        tSHTMLData=tSHTMLData.replace(HTMLReportContants.TS_START_TIME,reportCommon.convertLocalDateTimetoSQLDateTime(tsDetails.StartTime));
	        tSHTMLData=tSHTMLData.replace(HTMLReportContants.TS_END_TIME,reportCommon.convertLocalDateTimetoSQLDateTime(tsDetails.EndTime));
	        
	        tSHTMLData=tSHTMLData.replaceAll(HTMLReportContants.TS_DURATION,reportCommon.getTimeDifference(tsDetails.StartTime,tsDetails.EndTime));
			
	        appendDataToTextFile(tSHTMLPath, tSHTMLData);
		}
		catch(Exception e)
		{		
		   logger.error("{} Unable to update the Extent Test Step",e.getMessage());
		}
	}

	public void addScreenShotDetails(TestStepDetails ts,ExtentTest node )
	{
		if (ts.ScreenShotData.equals(""))
		{
			node.log(ts.ExtentStatus, ts.StepName);
		}
		else
		{
			node.log(ts.ExtentStatus,writeImageToReport(ts.ScreenShotData, ts.StepName));
		}		
	}



	public String readDataFromTextFile(String filePath) {
	    StringBuilder data = new StringBuilder();
	    try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
	        String strCurrentLine;
	        while ((strCurrentLine = reader.readLine()) != null) {
	            data.append(strCurrentLine);
	        }
	        return data.toString();
	    } catch (IOException e) {
	        logger.error("Error reading file: {}", e.getMessage());
	        return data.toString();
	    }
	}



	public List<String> readFileLineByLine(String filePath) {
	    ArrayList<String> data = new ArrayList<>();
	    try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
	        String strCurrentLine;
	        while ((strCurrentLine = reader.readLine()) != null) {
	            data.add(strCurrentLine);
	        }
	        return data;
	    } catch (IOException e) {
	        logger.error("Error reading file: {}", e.getMessage());
	        return data;
	    }
	}
		
	public boolean writeDataToTextFile(String filePath, String fileContent) {
	    try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
	        writer.write(fileContent);
	        return true; // File written successfully
	    } catch (IOException e) {
	        logger.error("Error writing data to file: {}", e.getMessage());
	        return false; // File write failed
	    }
	}



	
	public void writeFileLineByLine(String filePath,List<String> summaryDataCSS)
	{

		try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
	        for (String line : summaryDataCSS) {
	            writer.write(line);
	            writer.newLine(); // To add a newline after each line
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}

	public boolean appendDataToTextFile(String filePath, String fileContent) {
	    try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
	        writer.write(fileContent);
	        return true; // File appended successfully
	    } catch (IOException e) {
	        logger.error("Error appending data to file: {}", e.getMessage());
	        return false; // File append failed
	    }
	}



	public String writeImageToReport(String filePath, String fileName)
	{
	
		try
		{
			filePath = filePath.replace("\\", "/");
			filePath = "<a href = 'file:///" + filePath + "'>" + fileName + "</a>";
			return filePath;
		}

		catch (Exception e)
		{
			return filePath;
		}
	}


	
	public void copyFileUsingStream(String sourcePath, String destPath) throws IOException {
	    File source = new File(sourcePath);
	    File dest = new File(destPath);

	    try (InputStream is = new FileInputStream(source);
	         OutputStream os = new FileOutputStream(dest)) {
	        byte[] buffer = new byte[1024];
	        int length;
	        while ((length = is.read(buffer)) > 0) {
	            os.write(buffer, 0, length);
	        }
	    } catch (Exception e) {
	        logger.error("Unable to copy the files: {}", e.getMessage());
	    }
	}
}