package uitests.testng.sd;

import java.util.ArrayList;
import java.util.HashMap;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import common_utilities.Utilities.Util;
import testsettings.TestRunSettings;
import constants.PrjConstants;
import pom.sd.HeadersPage;
import pom.sd.InitialScreening;
import pom.sd.Login;
import pom.sd.ScreeningsPage;
import report_utilities.common.ReportCommon;
import report_utilities.common.ScreenshotCommon;
import report_utilities.model.TestCaseParam;
import uitests.testng.common.TestNGCommon;

public class ScreeningFlowTest  extends TestNGCommon{
	
	private WebDriver driver;
    String browser = "";
    ScreenshotCommon scm = new ScreenshotCommon();
    Util util = new Util();
    HashMap<String, ArrayList<String>> testCaseDataExecution = new HashMap<>();
	ReportCommon testStepDetails = new ReportCommon();
    TestCaseParam testCaseParam = new TestCaseParam();
    
    public ScreeningFlowTest(){
    	
    	/*This method is initially left blank for now*/
    }
    
    @BeforeMethod
    public void setUpReport()
    {
        browser = TestRunSettings.getBrowser();
        testCaseParam.setTestCaseName("ScreeningHappyPath");
        testCaseParam.setModuleName("SD");
        testCaseParam.setBrowser(browser);
        testCaseParam.setTestCaseDescription(testCaseParam.getTestCaseName());
        initializeTestCase(testCaseParam);
        driver = initializeDriver();
    }
    
    @Test
    public void testScreeningHappyPath() throws Exception
    {
    	Login objlogin = new Login(driver, testCaseParam);
    	objlogin.processLogin(testCaseParam,"1");
    	Thread.sleep(PrjConstants.DELAY);
    	
    	HeadersPage objHeaderPage = new HeadersPage(driver,testCaseParam);
    	objHeaderPage.selectHeaderTab(testCaseParam,"Screenings");
    	Thread.sleep(PrjConstants.DELAY);
    	
    	ScreeningsPage objSceenings = new ScreeningsPage(driver,testCaseParam);
    	objSceenings.clickNewBtn(testCaseParam);
    	Thread.sleep(PrjConstants.DELAY);
    	
    	InitialScreening objInitialScreening = new InitialScreening(driver,testCaseParam);
    	objInitialScreening.addInitialScreeningInfo(testCaseParam,"1");
    	Thread.sleep(PrjConstants.DELAY);
    }

    @AfterMethod
    public void tearDownMethod()
    {
    	endTestCase(testCaseParam);
    	quitDriver(driver);  	
    }
}
