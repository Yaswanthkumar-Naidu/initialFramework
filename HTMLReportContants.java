package report_utilities.Constants;

import java.util.ArrayList;
import java.util.List;

public class HTMLReportContants {

	//PlaceHolders
	//TestCase
	public static final String TC_Name="##TCName##";
	public static final String TC_Module="##ModuleName##";
	public static final String TC_Browser="##Browser##";
	public static final String TC_Status="##Status##";
	
	//Screenshot
	public static final String Scr_StepName="##StepName##";
	public static final String Scr_StepStatus="##StepStatus##";
	
	//TestSteps
	public static final String TS_StepName ="##StepName##";
	public static final String TS_StepDesc ="##StepDesc##";
	public static final String TS_Status ="##Status##";
	public static final String TS_jScreenshot ="##Screenshot##";
	public static final String TS_StartTime ="##StartTime##";
	public static final String TS_EndTime ="##EndTime##";
	public static final String TS_Duration ="##Duration##";
	
	//HTML FileLocation
	public static final String HTMLFileLocation="/src/main/resources/HTML/";
	
	
	//HTML FileLocation
	public static final String Template_HTMLIndex="HTMLIndex.html";
	public static final String Template_ScreenShot="ScreenShotTemplate.html";
	public static final String Template_HTMLTestCase="TCTemplate.html";
	public static final String Template_TestStepPre="TestStepPreTemplate.html";
	public static final String Template_CurrentTestStep="TSCurrentTemplate.html";
	public static final String  Template_HTMLSummary="HTMLSummaryTemplate.html";
	public static final String  Template_HTMLSummary_ModuleBrowserRow="HTMLSummaryModuleBrowserRow.html";
	public static final String  Template_HTMLSummary_TCRow="HTMLSummaryTCRow.html";
	public static final String  Template_HTMLTCLiveRow="HTMLTCLiveRow.html";
	public static final String  Template_HTMLTCLiveTemplate="HTMLTCLiveTemplate.html";
	
	
	//HTML Data
	
	public static String PlaceHolder_HTMLIndex="";
	public static  String PlaceHolder_ScreenShot="";
	public static  String PlaceHolder_HTMLTestCase="";
	public static  String PlaceHolder_TestStepPre="";
	public static  String PlaceHolder_CurrentTestStep="";
	public static  String  PlaceHolder_HTMLSummary="";
	public static  String  PlaceHolder_HTMLSummary_ModuleRow="";
	public static  String  PlaceHolder_HTMLSummary_BrowserRow="";
	public static  String  PlaceHolder_HTMLSummary_TCRow="";
	public static String PlaceHolder_HTMLTCLiveRow="";
	public static String PlaceHolder_HTMLTCLiveTemplate="";
	
	//HTMLReport File Name
	
	public static final String File_HTMLIndex="/index.html";
	public static final String File_ScreenShot="/ScreenShot.html";
	public static final String File_ScreenShotImage="/ScreenShot.jpeg";
	public static final String File_HTMLTestCase="/TCDetails.html";
	public static final String File_TestStep="/TSDetails.html";
	public static final String File_HTMLSummary="/HTML_Report_Summary.html";
	public static final String HTML_TCLiveReport="/HTML_TCLiveReport.html";

	//WebPage Refresh Time

	public static final String PageRefreshTimeInSecondsVar="##RefreshTime##";
	public static String PageRefreshTimeInSeconds="5";
	

	//HTML TC Live
	public static final String TCLive_TCName ="##TestCaseName##";
	public static final String TCLive_Module ="##Module##";
	public static final String TCLive_Browser ="##Browser##";
	public static final String TCLive_Status ="##Status##";
	public static final String TCLive_FilePath ="##FilePath##";	
	
	//HTML Summary
	
	//Module and Browser
	public static final String Summary_ModuleBrowser_SNO ="##SNO##";
	public static final String Summary_ModuleBrowser_Module ="##Module##";
	public static final String Summary_ModuleBrowser_TotalTC ="##TotalTC##";
	public static final String Summary_ModuleBrowser_PassedTC ="##PassedTC##";
	public static final String Summary_ModuleBrowser_FailedTC ="##FailedTC##";
	public static final String Summary_ModuleBrowser_SkippedTC ="##SkippedTC##";

	//TC Summary
	public static final String Summary_TC_SNO ="##SNO##";
	public static final String Summary_TC_TCName ="##TestCaseName##";
	public static final String Summary_TC_TCDesc ="##TestCaseDetails##";
	public static final String Summary_TC_Module ="##Module##";
	public static final String Summary_TC_Browser ="##Browser##";
	public static final String Summary_TC_Status ="##Status##";
	public static final String Summary_TC_StartTime ="##StartTime##";
	public static final String Summary_TC_EndTime ="##EndTime##";
	public static final String Summary_TC_Duration ="##Duration##";

	//PlaceHolders for DataRow
	public static final String Summary_DataRow_Module ="###ModuleData###";
	public static final String Summary_DataRow_Browser ="###BrowserData###";
	public static final String Summary_DataRow_TC ="###TestCaseData###";
	
	
	
	//New Files
	//JSON Data File Path
	
	public static  String HTMLJSONDataFilePath ="";
	public static  String HTMLResultsDir ="";
	
	
	
	//Static
	//TestSummary
	
	public static final String SummaryDashboard ="Dashboard.html";
	public static final String SummaryJS ="script.js";
	public static final String SummaryCSS ="style.css";
	public static final String SummaryJSON ="data.json";
	
	
	
	//TestSummary
	
	public static final String TestCaseHTML ="TestCase.html";
	public static final String TestCaseJS ="TCscript.js";
	public static final String TestCaseCSS ="TCstyle.css";
	public static final String TestCaseJSON ="TCData.json";

	public static final String TestCaseJS_Output ="script.js";
	public static final String TestCaseCSS_Output ="style.css";

	
	
	public static final String CSSFileName ="style.css";
	public static final String JSFileName ="script.js";
	
	
	
	//Data for Summary and TestCase
	
	
	
	public enum HTMLTCStatus {Passed,Failed,Skipped,Pending, InProgress};
	
	
	//Data from the files
	
	public static  String SummaryData_HTML="";
	public static  List<String> SummaryData_CSS=new ArrayList<String>();
	public static  List<String> HTMLSummaryData_JS=new ArrayList<String>();
	public static  String SummaryJSONFilePath="";
	
	public static  String TestCaseData_HTML="";
	public static  List<String> TestCaseData_CSS=new ArrayList<String>();
	public static  List<String> TestCaseData_JS=new ArrayList<String>();
	public static  String TestCaseJSONFilePath="";
	//HTML Reports Directory
	public static String HTMLReportsDir="";
	public static String TS_Screenshot="";
	public static final String HTMLReportsDirName="/HTMLReports/";


}
