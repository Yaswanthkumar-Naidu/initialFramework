package api_utilities.api_helpers;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.aventstack.extentreports.ExtentTest;

import api_utilities.api_common.ExcelUtil;
import api_utilities.models.APIReportModel;
import api_utilities.models.APITestCaseModel;
import api_utilities.models.APITestStepModel;
import api_utilities.reports.ExcelReportGenerator;
import api_utilities.reports.ExtentReport;
import api_utilities.test_settings.APITestSettings;

public class TestCaseHelper {

   	 static final String TEST_CASE_CONSTANT="TestCase";
	 static  final String API_NAME="APIName";

    public void getTestCaseDetails(String fileName, String sheetName) throws FileNotFoundException {
    	
    	
        ExcelUtil poiObj = ExcelUtil.getInstance();
        ArrayList<String> whereClause = new ArrayList<>();
        whereClause.add("Execute::Yes");
        Map<String, List<String>> tcDetails = poiObj.fetchWithCondition(fileName, sheetName, whereClause);
        ArrayList<APITestCaseModel> apiTestCaseModels = new ArrayList<>();

        if (tcDetails.isEmpty() || tcDetails.get(TEST_CASE_CONSTANT).isEmpty()) {
            throw new FileNotFoundException("Incorrect Data Sheet or Record Count is 0 for ==> " + fileName);
        }

        populateTestCaseModels(tcDetails, apiTestCaseModels);
        HashMap<String, ArrayList<APITestStepModel>> apiTcData = new HashMap<>();

        for (APITestCaseModel apiTestCaseModel : apiTestCaseModels) {
            ArrayList<String> whereClauseTC = new ArrayList<>();
            whereClauseTC.add("Execute::Yes");

            Map<String, List<String>> tsDetails = poiObj.fetchWithCondition(apiTestCaseModel.getTestCaseFilePath(), APITestSettings.getApiTestSettings().getApiTestCaseSheetName(), whereClauseTC);

            if (tsDetails.isEmpty() || tsDetails.get(API_NAME).isEmpty()) {
                throw new FileNotFoundException("Incorrect Data Sheet or Record Count is 0 for ==> " + apiTestCaseModel.getTestCaseFilePath());
            }

            ArrayList<APITestStepModel> testStepModels = populateTestStepModels(tsDetails);
            apiTcData.put(apiTestCaseModel.getTestCase(), testStepModels);
        }
        APITestSettings.setApiTcExecData(apiTcData);
    }

    private void populateTestCaseModels(Map<String, List<String>> tcDetails, ArrayList<APITestCaseModel> apiTestCaseModels) {
        int tcSize = tcDetails.get(TEST_CASE_CONSTANT).size();
        for (int i = 0; i < tcSize; i++) {
            APITestCaseModel apiTestCaseModel = new APITestCaseModel();
            apiTestCaseModel.setTestCase(tcDetails.get(TEST_CASE_CONSTANT).get(i));
            apiTestCaseModel.setTestCaseDescription(tcDetails.get("TestCaseDescription").get(i));
            apiTestCaseModel.setUserStory(tcDetails.get("UserStory").get(i));
            apiTestCaseModel.setDirectory(tcDetails.get("Directory").get(i));
            apiTestCaseModel.setTestCaseFilePath(APITestSettings.getApiTestSettings().getApiTestCaseDirectory() + File.separator + apiTestCaseModel.getDirectory() + File.separator + apiTestCaseModel.getTestCase() + APITestSettings.getApiTestSettings().getExcelSheetExtension());
            apiTestCaseModels.add(apiTestCaseModel);
            APITestSettings.getApiTcInfo().put(apiTestCaseModel.getTestCase(), apiTestCaseModel);
        }
    }

    private ArrayList<APITestStepModel> populateTestStepModels(Map<String, List<String>> tsDetails) {
        ArrayList<APITestStepModel> testStepModels = new ArrayList<>();
        for (int i = 0; i < tsDetails.get(API_NAME).size(); i++) {
            APITestStepModel apiTestStepModel = new APITestStepModel();
            apiTestStepModel.setModule(tsDetails.get("Module").get(i));
            apiTestStepModel.setApiName(tsDetails.get(API_NAME).get(i));
            apiTestStepModel.setFilePath(APITestSettings.getApiTestSettings().getApiDirectory() + File.separator + tsDetails.get("FileName").get(i));
            apiTestStepModel.setStartIndexforIteration(Integer.parseInt(tsDetails.get("StartIndexforIteration").get(i)));
            int iterationCount = Integer.parseInt(tsDetails.get("IterationCount").get(i));
            apiTestStepModel.setEndIndexforIteration(apiTestStepModel.getStartIndexforIteration() + iterationCount - 1);
            testStepModels.add(apiTestStepModel);
        }
        return testStepModels;
    }

    public void executeTestCases() throws InterruptedException  {
        ExtentReport extentReport = new ExtentReport();
        ExcelReportGenerator report = new ExcelReportGenerator();
        for (Map.Entry<String, ArrayList<APITestStepModel>> entry : APITestSettings.getApiTcExecData().entrySet()) {
            String testCase = entry.getKey();
            APITestCaseModel apiTestCaseModel = APITestSettings.getApiTcInfo().get(testCase);
            ExtentTest extentTest = extentReport.startTest(testCase, apiTestCaseModel.getDirectory(), apiTestCaseModel.getUserStory(), testCase);
            ArrayList<APITestStepModel> aPITestStepModels = entry.getValue();

            for (APITestStepModel apiTestStepModel : aPITestStepModels) {
                executeTestStep(apiTestStepModel, testCase, extentTest, extentReport, report);
            }
        }
    }

    private void executeTestStep(APITestStepModel apiTestStepModel, String testCase, ExtentTest extentTest, ExtentReport extentReport, ExcelReportGenerator report) throws InterruptedException{
        APIController apiController = new APIController();
        if (apiTestStepModel.getStartIndexforIteration() == apiTestStepModel.getEndIndexforIteration()) {
            List<APIReportModel> reportData = apiController.executeAPI(testCase, apiTestStepModel.getModule(), apiTestStepModel.getApiName(), APITestSettings.getBrowser(), String.valueOf(apiTestStepModel.getStartIndexforIteration()), apiTestStepModel.getFilePath());
            extentReport.addTestStep(extentTest, apiTestStepModel.getModule(), apiTestStepModel.getApiName(), String.valueOf(apiTestStepModel.getStartIndexforIteration()), reportData);
            report.addTestStep(reportData, apiTestStepModel.getApiName());
        } else {
            for (int i = apiTestStepModel.getStartIndexforIteration(); i <= apiTestStepModel.getEndIndexforIteration(); i++) {
                List<APIReportModel> reportData = apiController.executeAPI(testCase, apiTestStepModel.getModule(), apiTestStepModel.getApiName(), APITestSettings.getBrowser(), String.valueOf(i), apiTestStepModel.getFilePath());
                extentReport.addTestStep(extentTest, apiTestStepModel.getModule(), apiTestStepModel.getApiName(), String.valueOf(i), reportData);
                report.addTestStep(reportData, apiTestStepModel.getApiName());
            }
        }
    }
}
