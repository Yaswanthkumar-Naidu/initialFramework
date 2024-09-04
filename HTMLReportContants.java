package report_utilities.Constants;

import java.util.ArrayList;
import java.util.List;

public class HTMLReportContants {

	//PlaceHolders
	//TestCase
	public static final String TC_NAME ="##TCName##";
	public static final String TC_MODULE ="##ModuleName##";
	public static final String TC_BROWSER ="##Browser##";
	public static final String TC_STATUS ="##Status##";
	
	//Screenshot
	public static final String SCR_STEP_NAME ="##StepName##";
	public static final String SCR_STEP_STATUS ="##StepStatus##";
	
	//TestSteps
	public static final String TS_STEP_NAME ="##StepName##";
	public static final String TS_STEP_DESC ="##StepDesc##";
	public static final String TS_STATUS ="##Status##";
	public static final String TS_J_SCREENSHOT ="##Screenshot##";
	public static final String TS_START_TIME ="##StartTime##";
	public static final String TS_END_TIME ="##EndTime##";
	public static final String TS_DURATION ="##Duration##";
	
	//HTML FileLocation
	public static final String HTML_FILE_LOCATION ="/src/main/resources/HTML/";
	
	
	//HTML FileLocation
	public static final String TEMPLATE_HTML_INDEX ="HTMLIndex.html";
	public static final String TEMPLATE_SCREEN_SHOT ="ScreenShotTemplate.html";
	public static final String TEMPLATE_HTML_TEST_CASE ="TCTemplate.html";
	public static final String TEMPLATE_TEST_STEP_PRE ="TestStepPreTemplate.html";
	public static final String TEMPLATE_CURRENT_TEST_STEP ="TSCurrentTemplate.html";
	public static final String TEMPLATE_HTML_SUMMARY ="HTMLSummaryTemplate.html";
	public static final String TEMPLATE_HTML_SUMMARY_MODULE_BROWSER_ROW ="HTMLSummaryModuleBrowserRow.html";
	public static final String TEMPLATE_HTML_SUMMARY_TC_ROW ="HTMLSummaryTCRow.html";
	public static final String TEMPLATE_HTMLTC_LIVE_ROW ="HTMLTCLiveRow.html";
	public static final String TEMPLATE_HTMLTC_LIVE_TEMPLATE ="HTMLTCLiveTemplate.html";
	
	
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
	
	public static final String FILE_HTML_INDEX ="/index.html";
	public static final String FILE_SCREEN_SHOT ="/ScreenShot.html";
	public static final String FILE_SCREEN_SHOT_IMAGE ="/ScreenShot.jpeg";
	public static final String FILE_HTML_TEST_CASE ="/TCDetails.html";
	public static final String FILE_TEST_STEP ="/TSDetails.html";
	public static final String FILE_HTML_SUMMARY ="/HTML_Report_Summary.html";
	public static final String HTML_TC_LIVE_REPORT ="/HTML_TCLiveReport.html";

	//WebPage Refresh Time

	public static final String PAGE_REFRESH_TIME_IN_SECONDS_VAR ="##RefreshTime##";
	public static String PageRefreshTimeInSeconds="5";
	

	//HTML TC Live
	public static final String TC_LIVE_TC_NAME ="##TestCaseName##";
	public static final String TC_LIVE_MODULE ="##Module##";
	public static final String TC_LIVE_BROWSER ="##Browser##";
	public static final String TC_LIVE_STATUS ="##Status##";
	public static final String TC_LIVE_FILE_PATH ="##FilePath##";
	
	//HTML Summary
	
	//Module and Browser
	public static final String SUMMARY_MODULE_BROWSER_SNO ="##SNO##";
	public static final String SUMMARY_MODULE_BROWSER_MODULE ="##Module##";
	public static final String SUMMARY_MODULE_BROWSER_TOTAL_TC ="##TotalTC##";
	public static final String SUMMARY_MODULE_BROWSER_PASSED_TC ="##PassedTC##";
	public static final String SUMMARY_MODULE_BROWSER_FAILED_TC ="##FailedTC##";
	public static final String SUMMARY_MODULE_BROWSER_SKIPPED_TC ="##SkippedTC##";

	//TC Summary
	public static final String SUMMARY_TC_SNO ="##SNO##";
	public static final String SUMMARY_TC_TC_NAME ="##TestCaseName##";
	public static final String SUMMARY_TC_TC_DESC ="##TestCaseDetails##";
	public static final String SUMMARY_TC_MODULE ="##Module##";
	public static final String SUMMARY_TC_BROWSER ="##Browser##";
	public static final String SUMMARY_TC_STATUS ="##Status##";
	public static final String SUMMARY_TC_START_TIME ="##StartTime##";
	public static final String SUMMARY_TC_END_TIME ="##EndTime##";
	public static final String SUMMARY_TC_DURATION ="##Duration##";

	//PlaceHolders for DataRow
	public static final String SUMMARY_DATA_ROW_MODULE ="###ModuleData###";
	public static final String SUMMARY_DATA_ROW_BROWSER ="###BrowserData###";
	public static final String SUMMARY_DATA_ROW_TC ="###TestCaseData###";
	
	
	
	//New Files
	//JSON Data File Path
	
	public static  String HTMLJSONDataFilePath ="";
	public static  String HTMLResultsDir ="";
	
	
	
	//Static
	//TestSummary
	
	public static final String SUMMARY_DASHBOARD ="Dashboard.html";
	public static final String SUMMARY_JS ="script.js";
	public static final String SUMMARY_CSS ="style.css";
	public static final String SUMMARY_JSON ="data.json";
	
	
	
	//TestSummary
	
	public static final String TEST_CASE_HTML ="TestCase.html";
	public static final String TCSCRIPT_JS ="TCscript.js";
	public static final String TEST_CASE_CSS ="TCstyle.css";
	public static final String TEST_CASE_JSON ="TCData.json";

	public static final String TEST_CASE_JS_OUTPUT ="script.js";
	public static final String TEST_CASE_CSS_OUTPUT ="style.css";

	
	

	
	
	//Data for Summary and TestCase
	
	
	
	public enum HTMLTCStatus {PASSED, FAILED, SKIPPED, PENDING, IN_PROGRESS};
	
	
	//Data from the files
	
	public static  String SummaryData_HTML="";
	public static  List<String> SummaryData_CSS=new ArrayList<String>();
	public static  List<String> HTMLSummaryData_JS=new ArrayList<String>();
	public static  String SummaryJSONFilePath="";
	
	public static  String TestCaseData_HTML="";

	public static  List<String> TestCaseData_CSS=new ArrayList<String>();
	public static  List<String> testCaseDataJS =new ArrayList<String>();
	public static  String testCaseJSONFilePath ="";
	//HTML Reports Directory
	public static String htmlreportsdir ="";
	public static String tsScreenshot ="";
	public static final String HTML_REPORTS_DIR_NAME ="/HTMLReports/";


}