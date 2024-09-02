package report_utilities.model.html;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import report_utilities.Constants.HTMLReportContants;
import report_utilities.Constants.HTMLReportContants.HTMLTCStatus;
import report_utilities.common.ReportCommon;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class TestManager implements testCaseName {
	
	private TestManager() {
        // You can leave the constructor body empty or add any necessary initialization code
    }
	
	private static final Logger logger =LoggerFactory.getLogger(TestManager.class.getName());
	
	@JsonProperty("testCaseStats")
    private static TestCaseStats testCaseStats = new TestCaseStats();
	@JsonProperty("modules")
    private static List<Module> modules = new ArrayList<>();
	@JsonProperty("browsers")
    private static List<Browser> browsers = new ArrayList<>();
	@JsonProperty("testCases")
    private static List<TestCase> testCases = new ArrayList<>();
	
	

	private static final String STATUS_PENDING = "Pending";
	private static final String STATUS_INPROGRESS = "InProgess";
	private static final String STATUS_PASSED = "Passed";
	private static final String STATUS_FAILED = "Failed";
	private static final String STATUS_SKIPPED = "Skipped";

	
	
	public static void addTestStep(String testCaseName, String moduleName, String browserName, TestStep testStep) throws IOException {
	    for (TestCase testCase : testCases) {
	        if (testCase.getTestCaseName().equals(testCaseName) &&
	            testCase.getModule().equals(moduleName) &&
	            testCase.getBrowser().equals(browserName)) {
	         
	            int currentStepCount =0;
	            
	            if(testCase.getTestSteps()!=null)
	            {
	            	 currentStepCount =testCase.getTestSteps().size();
	            }
	            	
	            testStep.setStepNo(currentStepCount + 1);

	            if(currentStepCount==0) {
	            	updateTestStartTime(testCase);
	            }
	           
	            testCase.getTestSteps().add(testStep);

	            updateTestStepCounts(testCase);
	            updateCurrentStepDetails(testCase, testStep);
	            String dele = "/";
	            String testCaseFilePath= HTMLReportContants.HTMLReportsDir+dele+ testCaseName+ "_"
	            		+ moduleName+ "_" + browserName + dele +HTMLReportContants.TestCaseJSON;
	            ObjectMapper mapper = new ObjectMapper();
	            mapper.writeValue(new File(testCaseFilePath), testCase);
	            break;
	        }
	    }
	}


	

	// Method to update test step counts for a test case
	private static void updateTestStepCounts(TestCase testCase) {
	    int passedCount = 0;
	    int failedCount = 0;
	    for (TestStep step : testCase.getTestSteps()) {
	        if (step.getStatus().equals("Passed") || step.getStatus().equals("PASS")) {
	            passedCount++;
	        } else if (step.getStatus().equals("Failed") || step.getStatus().equals("FAIL")) {
	            failedCount++;
	        }
	    }
	    testCase.setPassed(passedCount);
	    testCase.setFailed(failedCount);
	    testCase.setTotalSteps(testCase.getTestSteps().size());
	}
	
	//Method to update start time of testcase
	private static void updateTestStartTime(TestCase testCase) {
		ReportCommon reportCommon = new ReportCommon();
		testCase.setStartTime(reportCommon.ConvertLocalDateTimetoSQLDateTime(LocalDateTime.now()));

	}
	private static void updateTestEndTime(TestCase testCase) {
		ReportCommon reportCommon = new ReportCommon();
		testCase.setEndTime(reportCommon.ConvertLocalDateTimetoSQLDateTime(LocalDateTime.now()));

	}

	// Method to update current step details for a test case
	private static void updateCurrentStepDetails(TestCase testCase, TestStep currentStep) {
	    CurrentStepData currentStepData = new CurrentStepData();
	    currentStepData.setCurrentStep(currentStep.getDescription());
	    currentStepData.setStatus(currentStep.getStatus());
	    testCase.setStatus(currentStep.getStatus());
	    testCase.setCurrentStepData(currentStepData);
	}

    public static void addTestCase(String testCaseName, String moduleName, String browserName) {
        TestCase newTestCase = new TestCase(testCaseName, moduleName, browserName, getTCStatus(HTMLTCStatus.Pending));
        testCases.add(newTestCase);
        updateStats(moduleName, browserName, getTCStatus(HTMLTCStatus.Pending), "increase");
        String tcDir=HTMLReportContants.HTMLReportsDir+"/"+ testCaseName+ "_" 	+ moduleName+ "_" + browserName;
        createDirectory(tcDir);
        createFile(tcDir +"/" + HTMLReportContants.TestCaseHTML , HTMLReportContants.TestCaseData_HTML);
        writeFileLineByLine(tcDir +"/" + HTMLReportContants.TestCaseCSS_Output , HTMLReportContants.TestCaseData_CSS);
        writeFileLineByLine(tcDir +"/" + HTMLReportContants.TestCaseJS_Output , HTMLReportContants.TestCaseData_JS);
    }

    public static void updateTestCaseStatus(String testCaseName, String moduleName, String browserName, HTMLTCStatus htmlTCStatus) {
        for (TestCase testCase : testCases) {
        	 if (testCase.getTestCaseName().equals(testCaseName) &&
     	            testCase.getModule().equals(moduleName) &&
     	            testCase.getBrowser().equals(browserName)) {
     	            
                String oldStatus = testCase.getStatus();
                if(testCase.getStatus().equalsIgnoreCase("FAIL")) {
                	testCase.setStatus(HTMLTCStatus.Failed.toString());
                }else {
                	testCase.setStatus(getTCStatus(htmlTCStatus));
                }
                
                updateStats(testCase.getModule(), testCase.getBrowser(), oldStatus, "decrease");
                updateStats(testCase.getModule(), testCase.getBrowser(), testCase.getStatus(), "increase");
	            updateTestEndTime(testCase);
	            String dele = "/";
	            String testCaseFilePath= HTMLReportContants.HTMLReportsDir+dele+ testCaseName+ "_"
	            		+ moduleName+ "_" + browserName + dele +HTMLReportContants.TestCaseJSON;
	            ObjectMapper mapper = new ObjectMapper();
	            tryCatch(testCase, testCaseFilePath, mapper);
                break;
            }
        }
    }



    private static void tryCatch(TestCase testCase, String testCaseFilePath, ObjectMapper mapper) {
        try {
            mapper.writeValue(new File(testCaseFilePath), testCase);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static void updateStats(String moduleName, String browserName, String status, String operation) {
        // Logic to find or create Module and Browser
        Module module = findOrCreateModule(moduleName);
        Browser browser = findOrCreateBrowser(browserName);

        // Update counts for Module and Browser
        updateCount(module,status, operation);
        updateCount(browser, status, operation);

        // Update global TestCaseStats
        updateGlobalCount(testCaseStats, status, operation);
        
        generateJson(HTMLReportContants.SummaryJSONFilePath);
    }

    private static Module findOrCreateModule(String moduleName) {
        for (Module module : modules) {
            if (module.getName().equals(moduleName)) {
                return module;
            }
        }
        Module newModule = new Module(moduleName, 0, 0, 0, 0);
        modules.add(newModule);
        return newModule;
    }

    private static Browser findOrCreateBrowser(String browserName) {
        for (Browser browser : browsers) {
            if (browser.getName().equals(browserName)) {
                return browser;
            }
        }
        Browser newBrowser = new Browser(browserName, 0, 0, 0, 0);
        browsers.add(newBrowser);
        return newBrowser;
    }

    private static void updateCount(Object obj, String status, String operation) {
        int change = operation.equals("increase") ? 1 : -1;
        if (obj instanceof Module) {
            Module module = (Module) obj;
            switch (status) {
                case STATUS_PASSED: module.setPassed(module.getPassed() + change); break;
                case STATUS_INPROGRESS: module.setInProgress(module.getInProgress() + change); break;
                case STATUS_FAILED: module.setFailed(module.getFailed() + change); break;
                case STATUS_PENDING: module.setPending(module.getPending() + change); break;
                case STATUS_SKIPPED: module.setSkipped(module.getSkipped() + change); break;
                default:
                    break;
            }
            if(status == "Failed") {
            	module.setPending(module.getPending() - change);
            }
        } else if (obj instanceof Browser) {
            Browser browser = (Browser) obj;
            switch (status) {
                case STATUS_PASSED: browser.setPassed(browser.getPassed() + change); break;
                case STATUS_INPROGRESS: browser.setInProgress(browser.getInProgress() + change); break;
                case STATUS_FAILED: browser.setFailed(browser.getFailed() + change); break;
                case STATUS_PENDING: browser.setPending(browser.getPending() + change); break;
                case STATUS_SKIPPED: browser.setSkipped(browser.getSkipped() + change); break;
                default:
                    break;
            }
            if(status == "Failed") {
            	browser.setPending(browser.getPending() - change);
            }
        }
        
    }

    private static void updateGlobalCount(TestCaseStats stats, String status, String operation) {
        int change = operation.equals("increase") ? 1 : -1;
        switch (status) {
            case STATUS_PASSED: stats.setPassed(stats.getPassed() + change); break;
            case STATUS_INPROGRESS: stats.setInProgress(stats.getInProgress() + change); break;
            case STATUS_FAILED: stats.setFailed(stats.getFailed() + change); break;
            case STATUS_PENDING: stats.setPending(stats.getPending() + change); break;
            case STATUS_SKIPPED: stats.setSkipped(stats.getSkipped() + change); break;
            default:
            break;
        }
        if(status == "Failed") {
        	stats.setPending(stats.getPending() - change);
        }
    }

    
    public static void createDirectory(String filePath)
    {
    	File dir = new File(filePath);
        if (!dir.exists()) dir.mkdirs();
    }
    
    public static void createFile(String filePath, String data) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(data);
        } catch (IOException e) {
            logger.error("File not created: {}", e.getMessage());
        }
    }
    
    public static void generateJson(String filePath) {
        ObjectMapper mapper = new ObjectMapper();

        try {
            // Write JSON to a file
            mapper.writeValue(Paths.get(filePath).toFile(), new Object[] {
                DataWrapper.testCaseStats,
                DataWrapper.modules,
                DataWrapper.browsers,
                DataWrapper.testCases,
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    
    public static String getTCStatus (HTMLTCStatus htmlTCStatus)
    {
    	String status="Pending";
    	
    	switch (htmlTCStatus) {
        case Passed: return STATUS_PASSED;
        case InProgress: return STATUS_INPROGRESS;
        case Failed: return STATUS_FAILED;
        case Pending: return STATUS_PENDING;
        case Skipped: return STATUS_SKIPPED;
    }
    	
    	
    	return status;
    }
    
    private static class DataWrapper {
    	public static TestCaseStats testCaseStats;
    	protected static List<Module> modules;
    	protected static List<Browser> browsers;
    	protected static List<TestCase> testCases;
    	
    	public DataWrapper(TestCaseStats stats, List<Module> mod, List<Browser> brws, List<TestCase> tcs) {
    	this.testCaseStats = stats;
    	this.modules = mod;
    	this.browsers = brws;
    	this.testCases = tcs;
    	
    	}
    	}

	public static void writeFileLineByLine(String filePath,List<String> testCaseDataCSS)
	{

		try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
	        for (String line : testCaseDataCSS) {
	            writer.write(line);
	            writer.newLine(); // To add a newline after each line
	        }
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}

}
