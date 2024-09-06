package api_tests.testng.common;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.io.File;

import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import testsettings.TestRunSettings;
import api_utilities.api_helpers.APIController;
import api_utilities.models.APIReportModel;
import api_utilities.reports.ExtentReport;
import api_utilities.test_settings.APITestSettings;
import initialize_scripts.InitializeTestSettings;
import report_utilities.common.ReportCommon;
import report_utilities.model.TestCaseParam;
import report_utilities.extent_model.PageDetails;

public class APITestNGCommon {

	
	@BeforeSuite
    public static void testrunsetup()
    {
	
		  Path currentRelativePath = Paths.get(""); 
		  String prjPath=currentRelativePath.toAbsolutePath().toString();

		  
			InitializeTestSettings initObj = new InitializeTestSettings();
			initObj.loadConfigData(prjPath);
			initObj.loadAPIConfig(prjPath);

			
			ExtentReport extentReport = new ExtentReport();
			extentReport.startReport(TestRunSettings.getResultsPath(),"API", TestRunSettings.getEnvironment(),"API");
		
    }
	
	
	public void executeAPI(TestCaseParam testCaseParam,  String apiname,String apimodulename,String apifilename) throws Exception
    {

		String filePath = APITestSettings.getApiTestSettings().getApiDirectory() + File.separator + apifilename;

		APIController apiController = new APIController();
		ArrayList<APIReportModel> reportData=apiController.executeAPI(testCaseParam.getTestCaseName(),apimodulename,apiname,testCaseParam.getBrowser(),String.valueOf(testCaseParam.getIteration()),filePath);


	         for(APIReportModel apiReportModel : reportData)
	         {
	        	 
	        	 PageDetails pageDetails = new PageDetails();
	        	 pageDetails.setPageActionName(apiname);
	        	 pageDetails.setPageActionDescription(apiname);
	        	 

					ReportCommon testStepDetails = new ReportCommon();
					testStepDetails.logAPIDetails( testCaseParam,  apiname, apiname,pageDetails, LocalDateTime.now(), apiReportModel.getTestStepResult(),apiReportModel.getUrl(),apiReportModel.getRequest(), apiReportModel.getActualResponse());

	         }

    }
	
	
	@AfterSuite
	public void endTestSuite()
	{
		ExtentReport extentReport = new ExtentReport();
		extentReport.endReport();
	}
}
