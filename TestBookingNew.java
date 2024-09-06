package api_tests.testng.booking;

import api_tests.testng.common.APITestNGCommon;
import api_utilities.api_helpers.TestCaseHelper;
import api_utilities.test_settings.APITestSettings;

import org.testng.annotations.Test;

public class TestBookingNew extends APITestNGCommon {


	 @Test(groups = { "Booking" })
	    public void createToken() throws InterruptedException
	    {
		 
		 TestCaseHelper testCaseHelper= new TestCaseHelper();
		 
		 testCaseHelper.getTestCaseDetails(APITestSettings.getApiTestSettings().getApiTestSuiteFileName(), APITestSettings.getApiTestSettings().getApiTestSuiteSheetName());
		 
		 testCaseHelper.executeTestCases();
		 
	    }


}
