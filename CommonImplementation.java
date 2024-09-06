package uitests.testng.common;

import java.util.ArrayList;
import java.util.Map;

import common_utilities.common.PoiReadExcel;
import testsettings.TestRunSettings;
import constants.LoginConstants;

public class CommonImplementation {

	
	public void loadLoginData()
    {
		
		ArrayList<String> whereClauseTestData = new ArrayList<>();
		whereClauseTestData.add("Environment::" + TestRunSettings.getEnvironment());
		Map<String, ArrayList<String>> result = PoiReadExcel.fetchWithCondition(TestRunSettings.getApplicationCredentialsFileName(), TestRunSettings.getApplicationCredentialsSheetName(), whereClauseTestData);
	
		LoginConstants loginconst = new LoginConstants();
		loginconst.loadLoginData(result);
	}
		
	public Map<String, ArrayList<String>> getDefaultScreenTCData(String testCaseName, String testDataLocation) {
	    ArrayList<String> whereClauseTestData = new ArrayList<>();
	    whereClauseTestData.add("TestCase::" + testCaseName);
	    return PoiReadExcel.fetchWithCondition(testDataLocation + "/" + "Master_Default.xlsx", "Default", whereClauseTestData);

	}

}


