
package uitests.testng.common;

import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.nio.file.Path;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import initialize_scripts.InitializeTestSettings;
import report_utilities.common.ReportCommon;
import report_utilities.constants.HTMLReportContants;
import report_utilities.model.TestCaseParam;
import report_utilities.extent_model.ExtentUtilities;
import report_utilities.extent_model.TestRunDetails;
import report_utilities.html_model.TestManager;
import report_utilities.html_report.HTMLReports;
import report_utilities.html_report.InitializeHTMLReports;
import reusable.PageLoadTimingInterceptor;
import testsettings.TestRunSettings;

public class TestNGCommon {
	
	private static final Logger logger =LoggerFactory.getLogger(TestNGCommon.class.getName());
	
	static boolean initialized = false;
	static boolean postinitialized = false;
	static LocalDateTime startTime=LocalDateTime.now();
	public static final String TESTRUN_ID  = ""; 

	@BeforeSuite
	public void testRunSetUp() throws InterruptedException
	{
		Path currentRelativePath = Paths.get("");
		String prjPath=currentRelativePath.toAbsolutePath().toString();



		InitializeTestSettings initObj = new InitializeTestSettings();
		initObj.loadConfigData(prjPath);


		CommonImplementation commimplementation = new CommonImplementation();
		commimplementation.loadLoginData();

		report_utilities.extent_report.ExtentReport startliveextentreport = new report_utilities.extent_report.ExtentReport();
		startliveextentreport.startReport(TestRunSettings.getResultsPath(),TestRunSettings.getEnvironment());


		InitializeHTMLReports initializeHTMLReports = new InitializeHTMLReports();
		initializeHTMLReports.initHTMLReports(prjPath,TestRunSettings.getResultsPath());
		HTMLReports htmlReports= new HTMLReports();

		htmlReports.createTCHTMLLiveSummary();		
	}


	public void endTestCase(TestCaseParam testCaseParam){

		ExtentUtilities extentUtilities = new ExtentUtilities();
		extentUtilities.endTestCase(testCaseParam.getTestCaseName(), testCaseParam.getModuleName(), testCaseParam.getBrowser());

		TestManager.updateTestCaseStatus(testCaseParam.getTestCaseName(), testCaseParam.getModuleName(), testCaseParam.getBrowser(),HTMLReportContants.HTMLTCStatus.PASSED);
	}
	
	
	@AfterSuite
	public void testRunTearDown() throws Exception
	{
		try {
			report_utilities.extent_report.ExtentReport extentReport = new report_utilities.extent_report.ExtentReport();
			extentReport.createExtentReportModel(TestRunSettings.getResultsPath(), TestRunDetails.getTestCaseRepository(), "Test", "Test", "Test");
		} catch (Exception e) {
			logger.error("{} Failed to generate the extent test Results",e.getMessage());
		}

		try {

			report_utilities.extent_report.ExtentReport extentReportlive = new report_utilities.extent_report.ExtentReport();
			extentReportlive.endReport();
		}
		catch(Exception e)
		{
			logger.error("{} Failed to generate the Live Results",e.getMessage());
		}




		//Calculate Results
		try
		{
			ReportCommon reportcommon=new ReportCommon();
			reportcommon.calculateTestCaseResults(TestRunDetails.getTestCaseRepository());
			reportcommon.calculateModuleResults();
			reportcommon.calculateBrowserResults();
		}
		catch(Exception e)
		{
			logger.error("{} Failed to compute the test case details",e.getMessage());

		}


		//Extent Report
		try
		{
			report_utilities.extent_report.ExtentReport extentReport = new report_utilities.extent_report.ExtentReport();
			extentReport.createExtentReportModel(TestRunSettings.getResultsPath(),TestRunDetails.getTestCaseRepository(),"Test","Test","Test");
		}
		catch(Exception e)
		{
			logger.error("{} Failed to generate the extent Test Results",e.getMessage());

		}


		//ExtentReport By Category

		try
		{
			report_utilities.extent_report.ExtentReport extentReport = new report_utilities.extent_report.ExtentReport();
			extentReport.createExtentReportCategory(TestRunSettings.getResultsPath(),TestRunDetails.getTestCaseRepository(),"Test","Test","Test");
		}
		catch(Exception e)
		{
			logger.error("{} Failed to generate the extent Test Results",e.getMessage());
		}

		try
		{
			HTMLReports htmlReport = new HTMLReports();
			htmlReport.generateReport(TestRunSettings.getResultsPath());
		}
		catch(Exception e)
		{
			logger.error("{} Failed to generate the Excel Test Results",e.getMessage());

		}
	}
	
	public void initializeTestCase(TestCaseParam testCaseParam)
	{
		ExtentUtilities extentUtilities = new ExtentUtilities();

		extentUtilities.initializeNewTestCase(testCaseParam.getTestCaseName(), testCaseParam.getTestCaseDescription(), testCaseParam.getModuleName(),testCaseParam.getTestCaseCategory(),testCaseParam.getCaseNumber(),testCaseParam.getApplicationNumber(), testCaseParam.getBrowser());
	}
	
	public WebDriver initializeDriver()
	{
		System.setProperty("webdriver.chrome.driver",
				"D:\\Apps\\chrome driver 126\\chromedriver-win64\\chromedriver.exe");
		ChromeOptions options= new ChromeOptions();
		options.setAcceptInsecureCerts(true);

		options.addArguments("start-maximized"); // open Browser in maximized mode
		options.addArguments("disable-infobars"); // disabling infobars
		options.addArguments("--disable-extensions"); // disabling extensions
		options.addArguments("--disable-gpu"); // applicable to windows os only
		options.addArguments("--disable-dev-shm-usage"); // overcome limited resource problems
		options.addArguments("--no-sandbox"); // Bypass OS security model
		options.addArguments("--use-fake-ui-for-media-stream=1");

		WebDriver driver= new ChromeDriver(options);
		driver.manage().deleteAllCookies();
		// Further actions based on runInHeadlessMode setting
		if (TestRunSettings.getCaptureUIPerformance().equalsIgnoreCase("Yes")){
			driver = PageLoadTimingInterceptor.createProxy(driver);
		}

		return driver;

	}



	public WebDriver initializeDriverIncognito()
	{
		ChromeOptions options= new ChromeOptions();
		options.setAcceptInsecureCerts(true);
		options.addArguments("headless");
		options.addArguments("start-maximized"); // open Browser in maximized mode
		options.addArguments("--start-fullscreen");
		options.addArguments("--incognito");
		options.addArguments("disable-infobars"); // disabling infobars
		options.addArguments("--disable-extensions"); // disabling extensions
		options.addArguments("--disable-gpu"); // applicable to windows os only
		options.addArguments("--disable-dev-shm-usage"); // overcome limited resource problems
		options.addArguments("--no-sandbox"); // Bypass OS security model
		WebDriver driver= new ChromeDriver(options);
		driver.manage().deleteAllCookies();

		return driver;
	}

	public WebDriver initializeAndroidDriver() throws IOException
	{
		DesiredCapabilities capabilities = new DesiredCapabilities();

		capabilities.setCapability("chromedriverExecutable","C:/SeleniumDrivers/chromedriver.exe");

		capabilities.setCapability("deviceName", "a52q");

		capabilities.setCapability("platformName", "Android");

		capabilities.setCapability("platformVersion", "12");

		capabilities.setCapability("browserName", "chrome");
		ChromeOptions options = new ChromeOptions();
		options.addArguments("--user-agent=Chrome/99.0.4844.73");
		capabilities.setCapability(ChromeOptions.CAPABILITY, options);
		
		return new RemoteWebDriver(new java.net.URL("http://127.0.0.1:4723/wd/hub"),capabilities);
	}

	public WebDriver initializeEdgeDriver()
	{
		System.setProperty("webdriver.edge.driver", "C:/SeleniumDrivers/msedgedriver.exe");
	
		return new EdgeDriver();
	}

	public void quitDriver(WebDriver driver)
	{
		try
		{
			driver.quit();
		}
		catch(Exception e)
		{
			logger.error("{} web driver is not quit",e.getMessage());
		}
	}


}





