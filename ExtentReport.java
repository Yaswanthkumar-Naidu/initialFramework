package api_utilities.Reports;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;

import api_utilities.Models.APIReportModel;
import api_utilities.TestSettings.APITestSettings;

public class ExtentReport {


	public static ExtentHtmlReporter htmlReporter;
	public static ExtentReports extent;
	public static HashMap<Integer, ExtentTest> dictExtentTestScenario = new HashMap<>();
	public static HashMap<Integer, String> dictExtentTestCase = new HashMap<>();
	public static HashMap<String, ExtentTest> tcExtentMapping = new HashMap<>();	
	private static final Logger logger =LoggerFactory.getLogger(ExtentReport.class.getName());

	public void startReport(String reportPath, String hostName, String environment, String userName)
	{
		try
		{
			reportPath = reportPath + "\\ExtentReport.html"; 
			htmlReporter = new ExtentHtmlReporter(reportPath);
			extent = new ExtentReports();
			extent.attachReporter(htmlReporter);
			extent.setSystemInfo("Host Name", hostName);
			extent.setSystemInfo("Environment", environment);
			extent.setSystemInfo("Username", userName);
		}
		catch(Exception e)
		{
			logger.info("Unable to initialize the Extent Report");
			logger.info(e.getMessage());
			logger.info(Arrays.toString(e.getStackTrace()));

		}
	}

	public void jenkinsReport(String reportPath) {
		reportPath = reportPath + "\\ExtentReport.html"; 
		String destinationPath=APITestSettings.HomePath+"\\JenkinsResult";
		String report = "\\ExtentReportAPI.html";

		try {
			Path source = Path.of(reportPath);
			Path destination = Path.of(destinationPath+report);

			Files.createDirectories(destination);

			File file = new File(destinationPath, report);
			if (file.exists()) {
				if (file.delete()) {
					logger.info("File deleted successfully.");
				} else {
					logger.info("Failed to delete the file.");
				}
			} 

			Files.copy(source, destination);
            logger.info("File copied successfully.");
		} catch (IOException e) {
			logger.info("An error occurred while copying the file: " + e.getMessage());
		}
	}


	public ExtentTest startTest(String testCase,String userStory, String module, String testCaseDescription)
	{
		try
		{
			ExtentTest tc = extent.createTest(testCase, testCaseDescription);
			ExtentTest test =tc.createNode("Iteration==>1");
			test.assignCategory(userStory);
			test.assignCategory(module);

			return test;

		}
		catch(Exception e)
		{
			logger.info("Unable to initialize the Extent Test Case ==>"+testCase );
			logger.info(e.getMessage());
			logger.info(Arrays.toString(e.getStackTrace()));


			throw e;

		}





	}




	public void endReport()

	{

		extent.flush();

	}


	public void addTestStep(ExtentTest extentTest, String module, String apiName, String iteration,ArrayList<APIReportModel> apiReportModels) throws InterruptedException
	{

		ExtentTest node=extentTest.createNode(module + "===>" + apiName +"==>" + iteration);
		ExtentTest requestNode=node.createNode("Request");
		if(apiReportModels.get(0).Request.contains("<")) {
			apiReportModels.get(0).Request = "<textarea style='color:black'>"+apiReportModels.get(0).Request+"</textarea>";
		}
		requestNode.log(Status.INFO , MarkupHelper.createLabel(apiReportModels.get(0).Request,ExtentColor.GREY));
		if(apiReportModels.get(0).Response.contains("<")) {
			apiReportModels.get(0).Response = "<textarea style='color:black'>"+apiReportModels.get(0).Response+"</textarea>";
		}
		ExtentTest responseNode=node.createNode("Response");
		responseNode.log(Status.INFO , MarkupHelper.createLabel( apiReportModels.get(0).Response,ExtentColor.GREY));

		ExtentTest resultNode=node.createNode("Results");

		int resultCounter=0;
		for(APIReportModel apiReportModel:apiReportModels)
		{
			resultCounter++;
			Status extentStatus = Status.INFO;
			if(apiReportModels.get(0).TestStepResult.equalsIgnoreCase("PASS"))
			{
				extentStatus=Status.PASS;
			}
			else if(apiReportModels.get(0).TestStepResult.equalsIgnoreCase("FAIL"))
			{
				extentStatus=Status.FAIL;				
			}

			Thread.sleep(0);
			if(!(apiReportModels.get(0).ActualResponse.equals(""))) {
				ExtentTest stepResult=resultNode.createNode("Result");
				stepResult.log(Status.INFO,"Status Info :  "+ apiReportModels.get(0).StatusCode);
				stepResult.log(Status.INFO,"Validation :  "+ apiReportModel.XPathJSONKey);
				stepResult.log(Status.INFO,"<br> Expected Results <br>"+ apiReportModel.ExpectedResponse);
				stepResult.log(extentStatus,"Actual Results <br>"+ apiReportModel.ActualResponse);
			}
		}

		if(APITestSettings.DBValidation.equalsIgnoreCase("Yes")) {
			Status extentStatus = Status.INFO;
			if(apiReportModels.get(apiReportModels.size()-1).TestStepResult.equalsIgnoreCase("PASS"))
			{
				extentStatus=Status.PASS;
			}
			else if(apiReportModels.get(apiReportModels.size()-1).TestStepResult.equalsIgnoreCase("FAIL"))
			{
				extentStatus=Status.FAIL;				
			}
			ExtentTest dbValidation=node.createNode("DB Validation Result");
			dbValidation.log(Status.INFO,"Query Validation :  "+ apiReportModels.get(apiReportModels.size()-1).DBValidation);
			dbValidation.log(Status.INFO,"<br> Expected DB Results <br>"+ apiReportModels.get(apiReportModels.size()-1).DBExpectedValue);
			dbValidation.log(extentStatus,"Actual DB Results <br>"+ apiReportModels.get(apiReportModels.size()-1).DBActualValue);
		}
	}


	public String writeDataToTextFile(String filePath, String fileName,String fileContent,String fileFormat)
	{
		filePath = filePath + File.separator + fileName + fileFormat;
		try(BufferedWriter writer = new BufferedWriter(new FileWriter(filePath)))
		{


			writer.write(fileContent);

			writer.close();

			filePath= filePath.replace("\\","/");

			filePath = "<a href = 'file:///"+ filePath + "'>"+ fileName + "</a>";
			return filePath;
		}

		catch (Exception e)
		{
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
