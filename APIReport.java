package api_utilities.api_helpers;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import api_utilities.Models.APIReportModel;
import api_utilities.TestSettings.APITestSettings;
import report_utilities.Constants.ReportContants;
import report_utilities.Model.TestCaseParam;
import report_utilities.common.ReportCommon;

public class APIReport {

	private static final Logger logger =LoggerFactory.getLogger(APIReport.class.getName());
	public void addTestStep(TestCaseParam testCaseParam,List<APIReportModel> apiReportModels) throws Exception
	{
		
		ReportCommon reportCommon = new ReportCommon();
		reportCommon.logAPIModule(testCaseParam, testCaseParam.ModuleName + "===>" + testCaseParam.APIName +"==>" + testCaseParam.Iteration);
		

		reportCommon.logAPIModule(testCaseParam, "Request");
		if(apiReportModels.get(0).Request.contains("<")) {
			apiReportModels.get(0).Request = "<textarea style='color:black'>"+apiReportModels.get(0).Request+"</textarea>";
		}
		
		reportCommon.logAPITestStep(testCaseParam,"Request" +apiReportModels.get(0).Request,"Request" +apiReportModels.get(0).Request,LocalDateTime.now(),ReportContants.Status_Done,"","");
		if(apiReportModels.get(0).Response.contains("<")) {
			apiReportModels.get(0).Response = "<textarea style='color:black'>"+apiReportModels.get(0).Response+"</textarea>";
		}
		reportCommon.logAPITestStep(testCaseParam,"Response" +apiReportModels.get(0).Request,"Response" +apiReportModels.get(0).Response,LocalDateTime.now(),ReportContants.Status_Done,"","");

		
		
		reportCommon.logAPIModule(testCaseParam,"Results");

		for(APIReportModel apiReportModel:apiReportModels)
		{
			String apiStatus = ReportContants.Status_Done;
			if(apiReportModels.get(0).TestStepResult.equalsIgnoreCase("PASS"))
			{
				apiStatus=ReportContants.Status_Pass;
			}
			else if(apiReportModels.get(0).TestStepResult.equalsIgnoreCase("FAIL"))
			{
				apiStatus=ReportContants.Status_Fail;				
			}

			Thread.sleep(0);
			if(!(apiReportModels.get(0).ActualResponse.equals(""))) {

				reportCommon.logAPITestStep(testCaseParam,"Status Info :  "+ apiReportModels.get(0).StatusCode,"Status Info :  "+ apiReportModels.get(0).StatusCode,LocalDateTime.now(),ReportContants.Status_Done,"","");
				reportCommon.logAPITestStep(testCaseParam,"Validation :  "+ apiReportModel.XPathJSONKey,"Validation :  "+ apiReportModel.XPathJSONKey,LocalDateTime.now(),ReportContants.Status_Done,"","");
				reportCommon.logAPITestStep(testCaseParam,"<br> Expected Results <br>"+ apiReportModel.ExpectedResponse,"<br> Expected Results <br>"+ apiReportModel.ExpectedResponse,LocalDateTime.now(),ReportContants.Status_Done,"","");
				reportCommon.logAPITestStep(testCaseParam,"Actual Results <br>"+ apiReportModel.ActualResponse,"Actual Results <br>"+ apiReportModel.ActualResponse,LocalDateTime.now(),apiStatus,"","");

			}
		}

		if(APITestSettings.DBValidation.equalsIgnoreCase("Yes")) {
			String apiStatus = ReportContants.Status_Done;
			if(apiReportModels.get(apiReportModels.size()-1).TestStepResult.equalsIgnoreCase("PASS"))
			{
				apiStatus=ReportContants.Status_Pass;
			}
			else if(apiReportModels.get(apiReportModels.size()-1).TestStepResult.equalsIgnoreCase("FAIL"))
			{
				apiStatus=ReportContants.Status_Fail;				
			}
			
			reportCommon.logAPIModule(testCaseParam,"DB Validation Result");

			reportCommon.logAPITestStep(testCaseParam,"Query Validation :  "+ apiReportModels.get(apiReportModels.size()-1).DBValidation,"Query Validation :  "+ apiReportModels.get(apiReportModels.size()-1).DBValidation,LocalDateTime.now(),ReportContants.Status_Done,"","");
			reportCommon.logAPITestStep(testCaseParam,"<br> Expected DB Results <br>"+ apiReportModels.get(apiReportModels.size()-1).DBExpectedValue,"<br> Expected DB Results <br>"+ apiReportModels.get(apiReportModels.size()-1).DBExpectedValue,LocalDateTime.now(),ReportContants.Status_Done,"","");
			reportCommon.logAPITestStep(testCaseParam,"Actual DB Results <br>"+ apiReportModels.get(apiReportModels.size()-1).DBActualValue,"Actual DB Results <br>"+ apiReportModels.get(apiReportModels.size()-1).DBActualValue,LocalDateTime.now(),apiStatus,"","");
			
		}
	}


	public String writeDataToTextFile(String filePath, String fileName, String fileContent, String fileFormat) {
	    String dele = "/";
		filePath = filePath + dele + fileName + fileFormat;
	    try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
	        writer.write(fileContent);
	        filePath = filePath.replace("\\", "/");
	        filePath = "<a href = 'file:///" + filePath + "'>" + fileName + "</a>";
	        return filePath;
	    } catch (IOException e) {
	        logger.error("Exception while writing data to file: {}", e.getMessage(), e);
	        return filePath;
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



}
