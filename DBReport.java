package api_utilities.Reports;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import api_utilities.Models.APIReportModel;
import api_utilities.Models.DBModel;
import api_utilities.Models.EnvModel;
import api_utilities.api_helpers.MSSQLUtilities;
import api_utilities.TestSettings.APISessionData;
import api_utilities.TestSettings.APITestSettings;


public class DBReport {

public void generateDBReport(String testCase,String module, String apiName,List<APIReportModel> apiReportModels) throws Exception
{

	 ArrayList<String> headers = new ArrayList<>();
     // Add the column names to the headers list
     headers.add("Env");
     headers.add("TestCaseName");
     headers.add("Module");
     headers.add("InterfaceName");
     headers.add("Result");
     headers.add("RequestData");
     headers.add("ResponseData");

     ArrayList<ArrayList<String>> queries = new ArrayList<>();
     // Add the data for each row as an ArrayList of strings
     
     for(APIReportModel apiReportModel: apiReportModels)
     {
     ArrayList<String> rowData = new ArrayList<>();
     // Add the values for the columns in the first row
     rowData.add(APITestSettings.Environment);
     rowData.add(testCase);
     rowData.add(module);
     rowData.add(apiName);
     rowData.add(apiReportModel.TestStepResult);
     rowData.add(apiReportModel.Request);
     apiReportModel.Response = sanitizeResponse(apiReportModel.Response);
     rowData.add(apiReportModel.Response);
     queries.add(rowData);
     }
	
     
     ObjectMapper mapper = new ObjectMapper();

     DBModel dbDetails = mapper.readValue(new File(APISessionData.envModel.ConnectionString), DBModel.class);
     EnvModel env= dbDetails.Env.get(APITestSettings.Environment);
     MSSQLUtilities mssqlUtilities=new MSSQLUtilities();
     mssqlUtilities.insertQueries(env.ConnectionString, APISessionData.DBLoggingTable, headers, queries);
	}

public String sanitizeResponse(String inputString)
{
    if (inputString.contains("'"))
    {
        inputString = inputString.replace("'", "");
        return inputString;
    }
    else
    {
        return inputString;
    }
}
}
