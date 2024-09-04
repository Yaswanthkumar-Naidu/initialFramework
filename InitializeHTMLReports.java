package report_utilities.htmlreport;

import report_utilities.Constants.HTMLReportContants;

public class InitializeHTMLReports {

	
	
	public void initHTMLReports(String prjPath,String reportPath) throws InterruptedException
	{
		HTMLReportContants.htmlreportsdir =reportPath+HTMLReportContants.HTML_REPORTS_DIR_NAME;
		HTMLReports htmlReports= new HTMLReports();
		String fileLocation=prjPath+"/"+ HTMLReportContants.HTML_FILE_LOCATION;
		HTMLReportContants.PlaceHolder_HTMLIndex=htmlReports.readDataFromTextFile(fileLocation + HTMLReportContants.TEMPLATE_HTML_INDEX);
		HTMLReportContants.PlaceHolder_ScreenShot=htmlReports.readDataFromTextFile(fileLocation + HTMLReportContants.TEMPLATE_SCREEN_SHOT);
		HTMLReportContants.PlaceHolder_HTMLTestCase=htmlReports.readDataFromTextFile(fileLocation + HTMLReportContants.TEMPLATE_HTML_TEST_CASE);
		HTMLReportContants.PlaceHolder_TestStepPre=htmlReports.readDataFromTextFile(fileLocation + HTMLReportContants.TEMPLATE_TEST_STEP_PRE);
		HTMLReportContants.PlaceHolder_CurrentTestStep=htmlReports.readDataFromTextFile(fileLocation + HTMLReportContants.TEMPLATE_CURRENT_TEST_STEP);
		HTMLReportContants.PlaceHolder_HTMLSummary=htmlReports.readDataFromTextFile(fileLocation + HTMLReportContants.TEMPLATE_HTML_SUMMARY);
		HTMLReportContants.PlaceHolder_HTMLSummary_BrowserRow=htmlReports.readDataFromTextFile(fileLocation + HTMLReportContants.TEMPLATE_HTML_SUMMARY_MODULE_BROWSER_ROW);
		HTMLReportContants.PlaceHolder_HTMLSummary_ModuleRow=htmlReports.readDataFromTextFile(fileLocation + HTMLReportContants.TEMPLATE_HTML_SUMMARY_MODULE_BROWSER_ROW);
		HTMLReportContants.PlaceHolder_HTMLSummary_TCRow=htmlReports.readDataFromTextFile(fileLocation + HTMLReportContants.TEMPLATE_HTML_SUMMARY_TC_ROW);
Thread.sleep(0);
		HTMLReportContants.PlaceHolder_HTMLTCLiveRow=htmlReports.readDataFromTextFile(fileLocation + HTMLReportContants.TEMPLATE_HTMLTC_LIVE_ROW);
		HTMLReportContants.PlaceHolder_HTMLTCLiveTemplate=htmlReports.readDataFromTextFile(fileLocation + HTMLReportContants.TEMPLATE_HTMLTC_LIVE_TEMPLATE);

		
		HTMLReportContants.SummaryData_HTML=htmlReports.readDataFromTextFile(fileLocation + HTMLReportContants.SUMMARY_DASHBOARD);
		HTMLReportContants.SummaryData_CSS=htmlReports.readFileLineByLine(fileLocation + HTMLReportContants.SUMMARY_CSS);
		HTMLReportContants.HTMLSummaryData_JS=htmlReports.readFileLineByLine(fileLocation + HTMLReportContants.SUMMARY_JS);
		
		HTMLReportContants.TestCaseData_HTML=htmlReports.readDataFromTextFile(fileLocation + HTMLReportContants.TEST_CASE_HTML);
		HTMLReportContants.TestCaseData_CSS=htmlReports.readFileLineByLine(fileLocation + HTMLReportContants.TEST_CASE_CSS);
		HTMLReportContants.testCaseDataJS =htmlReports.readFileLineByLine(fileLocation + HTMLReportContants.TCSCRIPT_JS);

		HTMLReportContants.SummaryJSONFilePath= HTMLReportContants.htmlreportsdir + HTMLReportContants.SUMMARY_JSON;

		
	}
}