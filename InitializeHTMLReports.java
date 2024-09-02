package report_utilities.htmlreport;

import report_utilities.Constants.HTMLReportContants;

public class InitializeHTMLReports {

	
	
	public void initHTMLReports(String prjPath,String reportPath) throws InterruptedException
	{
		HTMLReportContants.HTMLReportsDir=reportPath+HTMLReportContants.HTMLReportsDirName;
		HTMLReports htmlReports= new HTMLReports();
		String fileLocation=prjPath+"/"+ HTMLReportContants.HTMLFileLocation;
		HTMLReportContants.PlaceHolder_HTMLIndex=htmlReports.readDataFromTextFile(fileLocation + HTMLReportContants.Template_HTMLIndex);
		HTMLReportContants.PlaceHolder_ScreenShot=htmlReports.readDataFromTextFile(fileLocation + HTMLReportContants.Template_ScreenShot);
		HTMLReportContants.PlaceHolder_HTMLTestCase=htmlReports.readDataFromTextFile(fileLocation + HTMLReportContants.Template_HTMLTestCase);
		HTMLReportContants.PlaceHolder_TestStepPre=htmlReports.readDataFromTextFile(fileLocation + HTMLReportContants.Template_TestStepPre);
		HTMLReportContants.PlaceHolder_CurrentTestStep=htmlReports.readDataFromTextFile(fileLocation + HTMLReportContants.Template_CurrentTestStep);
		HTMLReportContants.PlaceHolder_HTMLSummary=htmlReports.readDataFromTextFile(fileLocation + HTMLReportContants.Template_HTMLSummary);
		HTMLReportContants.PlaceHolder_HTMLSummary_BrowserRow=htmlReports.readDataFromTextFile(fileLocation + HTMLReportContants.Template_HTMLSummary_ModuleBrowserRow);
		HTMLReportContants.PlaceHolder_HTMLSummary_ModuleRow=htmlReports.readDataFromTextFile(fileLocation + HTMLReportContants.Template_HTMLSummary_ModuleBrowserRow);
		HTMLReportContants.PlaceHolder_HTMLSummary_TCRow=htmlReports.readDataFromTextFile(fileLocation + HTMLReportContants.Template_HTMLSummary_TCRow);
Thread.sleep(0);
		HTMLReportContants.PlaceHolder_HTMLTCLiveRow=htmlReports.readDataFromTextFile(fileLocation + HTMLReportContants.Template_HTMLTCLiveRow);
		HTMLReportContants.PlaceHolder_HTMLTCLiveTemplate=htmlReports.readDataFromTextFile(fileLocation + HTMLReportContants.Template_HTMLTCLiveTemplate);

		
		HTMLReportContants.SummaryData_HTML=htmlReports.readDataFromTextFile(fileLocation + HTMLReportContants.SummaryDashboard);
		HTMLReportContants.SummaryData_CSS=htmlReports.readFileLineByLine(fileLocation + HTMLReportContants.SummaryCSS);
		HTMLReportContants.HTMLSummaryData_JS=htmlReports.readFileLineByLine(fileLocation + HTMLReportContants.SummaryJS);
		
		HTMLReportContants.TestCaseData_HTML=htmlReports.readDataFromTextFile(fileLocation + HTMLReportContants.TestCaseHTML);
		HTMLReportContants.TestCaseData_CSS=htmlReports.readFileLineByLine(fileLocation + HTMLReportContants.TestCaseCSS);
		HTMLReportContants.TestCaseData_JS=htmlReports.readFileLineByLine(fileLocation + HTMLReportContants.TestCaseJS);

		HTMLReportContants.SummaryJSONFilePath= HTMLReportContants.HTMLReportsDir+ HTMLReportContants.SummaryJSON;

		
	}
}
