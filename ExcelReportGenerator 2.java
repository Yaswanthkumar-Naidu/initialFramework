package report_utilities.ExcelReport;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import report_utilities.Constants.ReportContants;
import report_utilities.Model.ExtentModel.TestCaseDetails;
import report_utilities.Model.ExtentModel.TestStepDetails;

public class ExcelReportGenerator {

	
	ArrayList<String> browserHeader = new ArrayList<>();
	ArrayList<String> moduleHeader = new ArrayList<>();
	ArrayList<String> testCaseHeader = new ArrayList<>();
	ArrayList<String> testStepHeader = new ArrayList<>();
	ArrayList<Integer> browserHeaderSize = new ArrayList<>();
	ArrayList<Integer> moduleHeaderSize = new ArrayList<>();
	ArrayList<Integer> testCaseHeaderSize = new ArrayList<>();
	ArrayList<Integer> testStepHeaderSize = new ArrayList<>();
	private static final Logger logger =LoggerFactory.getLogger(ExcelReportGenerator.class.getName());
	
	public void initializeColumnHeaders()
	{
		browserHeader.add("S.No");
		browserHeaderSize.add(10*250);
		browserHeader.add("Browser");
		browserHeaderSize.add(20*250);
		browserHeader.add("TotalTestCases");
		browserHeaderSize.add(20*250);
		browserHeader.add("TestCasesPassed");
		browserHeaderSize.add(20*250);
		browserHeader.add("TestCasesFailed");
		browserHeaderSize.add(20*250);
		browserHeader.add("TestCasesSkipped");
		browserHeaderSize.add(20*250);
		
		moduleHeader.add("S.No");
		moduleHeaderSize.add(10*250);
		moduleHeader.add("Module");
		moduleHeaderSize.add(20*250);
		moduleHeader.add("TotalTestCases");
		moduleHeaderSize.add(20*250);
		moduleHeader.add("TestCasesPassed");
		moduleHeaderSize.add(20*250);
		moduleHeader.add("TestCasesFailed");
		moduleHeaderSize.add(20*250);
		moduleHeader.add("TestCasesSkipped");
		moduleHeaderSize.add(20*250);
		
		testCaseHeader.add("S.No");
		testCaseHeaderSize.add(10*250);
		testCaseHeader.add("TestCaseName");
		testCaseHeaderSize.add(20*250);
		testCaseHeader.add("TestCaseDetails");
		testCaseHeaderSize.add(40*250);
		testCaseHeader.add("Module");
		testCaseHeaderSize.add(15*250);
		testCaseHeader.add("TestCaseCategory");
		testCaseHeaderSize.add(25*250);
		testCaseHeader.add("CaseNumber");
		testCaseHeaderSize.add(25*250);
		testCaseHeader.add("ApplicationNumber");
		testCaseHeaderSize.add(25*250);
		testCaseHeader.add("Browser");
		testCaseHeaderSize.add(15*250);
		testCaseHeader.add("Status");
		testCaseHeaderSize.add(15*250);
		testCaseHeader.add("StartTime");
		testCaseHeaderSize.add(25*250);
		testCaseHeader.add("EndTime");
		testCaseHeaderSize.add(25*250);
		testCaseHeader.add("Duration");
		testCaseHeaderSize.add(15*250);
		
		
		testStepHeader.add("S.No");
		testStepHeaderSize.add(10*250);
		testStepHeader.add("TestStepName");
		testStepHeaderSize.add(25*250);
		testStepHeader.add("TestStepDetails");
		testStepHeaderSize.add(40*250);
		testStepHeader.add("Status");
		testStepHeaderSize.add(15*250);
		testStepHeader.add("StartTime");
		testStepHeaderSize.add(25*250);
		testStepHeader.add("EndTime");
		testStepHeaderSize.add(25*250);
		testStepHeader.add("Duration");
		testStepHeaderSize.add(15*250);
		testStepHeader.add("Screenshot");
		testStepHeaderSize.add(15*250);
		testStepHeader.add("ErrorMessage");
		testStepHeaderSize.add(25*250);
		testStepHeader.add("ErrorDetails");
		testStepHeaderSize.add(40*250);
		
		
	}
	
	public void generateExcelReport(ArrayList<HashMap<UUID, TestCaseDetails>> testCaseRepository,String filePath) throws Exception
	{
		try {
		initializeColumnHeaders();
		
		ExcelReportCommon excelreport = new ExcelReportCommon();
		
		Workbook workbook=excelreport.createWorkbook();
		
		CreationHelper createHelper= excelreport.createExcelHelper(workbook);
		
		Sheet dashboardSheet= excelreport.createSheet(workbook, "Dashboard");
	
		excelreport.createHeader(workbook, dashboardSheet, moduleHeader,moduleHeaderSize,0);	
	
		excelreport.addTestModuleDetails(workbook, dashboardSheet, createHelper,ReportContants.moduleResults);

		ExcelRunSettings.dashboardRowCounter=ExcelRunSettings.dashboardRowCounter+2;
		
		excelreport.createHeader(workbook, dashboardSheet, browserHeader,browserHeaderSize,ExcelRunSettings.dashboardRowCounter);	
		
		excelreport.addTestBrowserDetails(workbook, dashboardSheet, createHelper,ReportContants.browserResults);
	
		
		Sheet testcaseDetails= excelreport.createSheet(workbook, "TestCases");

		for (HashMap<UUID,TestCaseDetails> DictTC : testCaseRepository)
		{
			TestCaseDetails testCaseDetails = DictTC.values().stream().findFirst().get();
			String testcaseName = testCaseDetails.TestCaseName;

			Sheet testStepDetails= excelreport.createSheet(workbook, testcaseName);
			ExcelRunSettings.tcSheetMapping.put(testcaseName,  testStepDetails);
		}
		excelreport.createHeader(workbook, testcaseDetails, testCaseHeader,testCaseHeaderSize,0);
	
		excelreport.addTestCaseDetails(workbook, testcaseDetails, createHelper,ReportContants.testcaseResults);

		if (testCaseRepository != null)
		{

			for (HashMap<UUID,TestCaseDetails> DictTC : testCaseRepository)
			{
				TestCaseDetails testCaseDetails = DictTC.values().stream().findFirst().get();
				String testcaseName = testCaseDetails.TestCaseName;

								
				Sheet testStepDetail= ExcelRunSettings.tcSheetMapping.get(testcaseName);
				excelreport.createHeader(workbook, testStepDetail, testStepHeader,testStepHeaderSize,0);

				ArrayList<TestStepDetails> testStepDetails = testCaseDetails.stepDetails;
				
				excelreport.addTestStepDetails(workbook, testStepDetail, createHelper, testStepDetails);


			}
		}
		
		excelreport.closeWorkBook(workbook, filePath);

		}
		catch(Exception e) 
		{
			logger.info(e.getMessage());
			logger.info(Arrays.toString(e.getStackTrace()));
			throw e;
		}
		
	}
	
	public void generateExcelReportTestCase(TestCaseDetails testCaseDetails,String filePath) throws Exception
	{
		initializeColumnHeaders();
		
		ExcelReportCommon excelreport = new ExcelReportCommon();
		
		Workbook workbook=excelreport.createWorkbook();
		
		CreationHelper createHelper= excelreport.createExcelHelper(workbook);
		

				String testcaseName = testCaseDetails.TestCaseName;

				Sheet testStepDetail= excelreport.createSheet(workbook, testcaseName);
				

				excelreport.createHeader(workbook, testStepDetail, testStepHeader,testStepHeaderSize,0);

				ArrayList<TestStepDetails> testStepDetails = testCaseDetails.stepDetails;
				
				excelreport.addTestStepDetails(workbook, testStepDetail, createHelper, testStepDetails);


		excelreport.closeWorkBook(workbook, filePath);

		
		
	}
}
