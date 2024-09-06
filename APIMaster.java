package api_utilities.api_common;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import api_utilities.api_helpers.MSSQLUtilities;
import api_utilities.models.APIConfig;
import api_utilities.models.APIModel;
import api_utilities.models.MockData;
import api_utilities.models.ResponseValidationModel;
import api_utilities.models.StoreResponseModel;
import api_utilities.test_settings.APISessionData;
import api_utilities.test_settings.APITestSettings;

public class APIMaster {

    private static final Logger logger = LoggerFactory.getLogger(APIMaster.class.getName());

    static final String DATA_MODULE = "Module::";
    static final String DATA_INTERFACENAME = "InterfaceName::";
    static final String TESTDATAFILENAME = "TestDataFileName";
    static final String RESPONSETYPE = "ResponseType";
    static final String XPATH_JSONPATH = "xpath_jsonpath";
    static final String INTERFACENAME = "InterfaceName";
    static final String SLASH_SYMBOL = "////////////////////////////////////////////////////////////////////////////";

    public APIModel fetchInterfaceRepository(APIConfig interfaceTestSettings, String testCase, String moduleName, String interfaceName, String iteration, String apifilepath) throws FileNotFoundException {
        ArrayList<String> whereClause = new ArrayList<>();
        whereClause.add(DATA_MODULE + moduleName);
        whereClause.add(DATA_INTERFACENAME + interfaceName);

        Map<String, List<String>> interfacesArrayList = ExcelUtil.fetchWithCondition(apifilepath, interfaceTestSettings.getInterfaceTestCaseSheet(), whereClause);

        try {
            return processInterfaceData(interfaceTestSettings, testCase,  interfaceName, iteration, interfacesArrayList);
        } catch (Exception e) {
            logger.error("Error while reading the data for the Interfaces: {}", e.getMessage(), e);
            throw new FileNotFoundException("Error while reading the data for the Interfaces");
        }
    }

    private APIModel processInterfaceData(APIConfig interfaceTestSettings, String testCase, String interfaceName, String iteration, Map<String, List<String>> interfacesArrayList) throws FileNotFoundException {
        String requestPath = interfaceTestSettings.getRequestLocation() + interfacesArrayList.get("RequestFileName").get(0);
        String responsePath = interfacesArrayList.get("ResponseFileName").get(0).trim().isEmpty() ? "" : interfaceTestSettings.getResponseLocation() + interfacesArrayList.get("ResponseFileName").get(0);
        String testDataFileName = interfacesArrayList.get(TESTDATAFILENAME).get(0).trim().isEmpty() ? "" : interfacesArrayList.get(TESTDATAFILENAME).get(0);

        String module = interfacesArrayList.get("Module").get(0);
        String fileFormat = interfacesArrayList.get("FileFormat").get(0);
        String interfaceType = interfacesArrayList.get("InterfaceType").get(0);
        String needMock = interfacesArrayList.get("NeedMock").get(0);
        String mockedInterfaceName = interfacesArrayList.get("MockedInterfaceName").get(0);
        int timeout = getTimeout(interfaceTestSettings, interfacesArrayList);

        APIModel interFaceObj = new APIModel();
        Map<String, List<String>> serviceArrayList = fetchServiceData(interfaceTestSettings, module, interfaceName);

        String serviceURL = serviceArrayList.get("URL").get(0);
        String soapaction = serviceArrayList.get("SOAPAction").get(0);
        String methodType = serviceArrayList.get("MethodType").get(0);
        String certificateRequired = serviceArrayList.get("CertificateRequired").get(0);
        String certificateFileName = interfaceTestSettings.getCertificateLocation() + serviceArrayList.get("CertificateFileName").get(0);
        String certificatePassword = serviceArrayList.get("CertificatePassword").get(0);
        String dbquery = serviceArrayList.get("DBQuery").get(0);
        String dbexpectedvalue = serviceArrayList.get("DBExpectedValue").get(0);

        if (dbquery.contains("#StartDate#")) {
            dbquery = dbquery.replace("#StartDate#", String.valueOf(APISessionData.getSessionStartTime()));
        }

        MockData mockData = new MockData();
        interFaceObj.setMockDetails(needMock, mockedInterfaceName);

        if (interFaceObj.isMockRequired()) {
            mockData = fetchMockData(interfaceTestSettings, mockedInterfaceName);
        }

        String requestData = readFile(requestPath, "Request");
        String responseData = readFile(responsePath, "Response");

        Map<String, String> tempDictData = getTestData(interfaceTestSettings, testDataFileName, testCase, module, iteration);
        requestData = replacePlaceholders(requestData, tempDictData);
        responseData = replacePlaceholders(responseData, tempDictData);
        serviceURL = replacePlaceholders(serviceURL, tempDictData);
        serviceURL = APISessionData.replaceSessionData(testCase, module, "", iteration, serviceURL);

        if (interFaceObj.isMockRequired()) {
            mockData.setMockFileContent(readMockFile(interfaceTestSettings, mockData, tempDictData));
        }

        ResponseValidationModel responsevalidationModel = fetchResponseValidationData(interfaceTestSettings, testCase, module, interfaceName, iteration, tempDictData);
        StoreResponseModel storeResponseModel = fetchStoreResponseData(interfaceTestSettings, module, interfaceName);

        Map<String, String> headerData = fetchHeaderData(interfaceTestSettings, module, interfaceName, tempDictData, testCase, iteration);

        interFaceObj.setInterfaceDetails(module, interfaceName, fileFormat, interfaceType, serviceURL, soapaction, methodType, certificateRequired, certificateFileName, certificatePassword, requestData, responseData, needMock, mockedInterfaceName, mockData, headerData, timeout, responsevalidationModel, storeResponseModel, dbquery, dbexpectedvalue);

        return interFaceObj;
    }

    private int getTimeout(APIConfig interfaceTestSettings, Map<String, List<String>> interfacesArrayList) {
        try {
            return interfacesArrayList.get("Timeout").get(0).isEmpty() ? interfaceTestSettings.getDefaultServiceTimeout() : Integer.parseInt(interfacesArrayList.get("Timeout").get(0));
        } catch (Exception e) {
            logger.error("Error while retrieving the timeout value from the TestCasesheet. Hence setting the default timeout", e);
            return interfaceTestSettings.getDefaultServiceTimeout();
        }
    }

    private Map<String, List<String>> fetchServiceData(APIConfig interfaceTestSettings, String module, String interfaceName) {
        ArrayList<String> whereClauseUrl = new ArrayList<>();
        whereClauseUrl.add(DATA_MODULE + module);
        whereClauseUrl.add(DATA_INTERFACENAME + interfaceName);

        return ExcelUtil.fetchWithCondition(interfaceTestSettings.getUrlRepositorySheet(), interfaceTestSettings.getEnvironment(), whereClauseUrl);
    }

    private MockData fetchMockData(APIConfig interfaceTestSettings, String mockedInterfaceName) {
        MockData mockData = new MockData();
        ArrayList<String> whereClauseMock = new ArrayList<>();
        whereClauseMock.add("TemplateName::" + mockedInterfaceName);
        Map<String, List<String>> dictMockData = ExcelUtil.fetchWithCondition(interfaceTestSettings.getMockRepositorySheet(), interfaceTestSettings.getMockSheetName(), whereClauseMock);

        for (Map.Entry<String, List<String>> entry : dictMockData.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue().get(0);

            switch (key.toUpperCase()) {
                case "ID":
                    mockData.setId(value);
                    break;
                case "MOCKLOCATION":
                    mockData.setMockLocation(value);
                    break;
                case "TEMPLATENAME":
                    mockData.setTemplateName(value);
                    break;
                case "INTERFACETYPE":
                    mockData.setInterfaceType(value);
                    break;
                case "RECORDCOUNT":
                    mockData.setRecordCount(value);
                    break;
                case "MOCKFILENAME":
                    mockData.setMockFileName(value);
                    break;
                case "MOCKFILEFORMAT":
                    mockData.setMockFileFormat(value);
                    break;
                default:
                    break;
            }
        }
        return mockData;
    }

    private String readFile(String path, String type) throws FileNotFoundException  {
        try {
            String data = FileUtils.readFileToString(new File(path), StandardCharsets.UTF_8);
            logger.info("Read the {} File: {}", type, path);
            return data;
        } catch (Exception e) {
            logger.error("Error while reading the {} File: {}", type, path, e);
            throw new FileNotFoundException("Error while reading the " + type + " File: " + path);
        }
    }

    private Map<String, String> getTestData(APIConfig interfaceTestSettings, String testDataFileName, String testCase, String module, String iteration) throws FileNotFoundException {
        try {
            MSSQLUtilities sql = new MSSQLUtilities();
            APIUtil utilObj = new APIUtil();
            Map<String, String> tempDictData = utilObj.getTestData(interfaceTestSettings.getTestDataPath(), testDataFileName, testCase, module, APITestSettings.getBrowser(), interfaceTestSettings.getEnvironment(), iteration);

            for (Map.Entry<String, String> entry : tempDictData.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue().toUpperCase();

                if (value.contains("QUERY:")) {
                    value = value.replace("QUERY:", "");
                    value = sql.readDataDBValidation(APISessionData.envModel.getConnectionString(), value);
                    tempDictData.put(key, value == null ? "" : value);
                    logger.info("Query has been fetched from TestData: {}", value);
                }
            }
            return tempDictData;
        } catch (Exception e) {
            logger.error("Error while reading the TestData: {}", e.getMessage(), e);
            throw new FileNotFoundException("Error while reading the TestData. Please check if the Test Data Sheet is correctly configured for: " + testDataFileName);
        }
    }

    private String replacePlaceholders(String data, Map<String, String> tempDictData) {
        return tempDictData.entrySet().stream().reduce(data, (s, e) -> s.replace("#*" + e.getKey() + "*#", e.getValue()), (s1, s2) -> null);
    }

    private String readMockFile(APIConfig interfaceTestSettings, MockData mockData, Map<String, String> tempDictData) throws FileNotFoundException {
        try {
            String templateData = FileUtils.readFileToString(new File(interfaceTestSettings.getMockTemplateLocation() + File.separator + mockData.getTemplateName() + mockData.getMockFileFormat()), StandardCharsets.UTF_8).trim();
            return tempDictData.entrySet().stream().reduce(templateData, (s, e) -> s.replace(e.getKey(), e.getValue()), (s1, s2) -> null);
        } catch (Exception e) {
            logger.error("Error while reading the Mock File: {}", e.getMessage(), e);
            throw new FileNotFoundException("Error while reading the Mock File. Please check if the Test Data Sheet/Mock File is correctly configured for: " + mockData.getMockFileName());
        }
    }

    private ResponseValidationModel fetchResponseValidationData(APIConfig interfaceTestSettings, String testCase, String module, String interfaceName, String iteration, Map<String, String> tempDictData) throws FileNotFoundException {
        ResponseValidationModel responsevalidationModel = new ResponseValidationModel();
        try {
            ArrayList<String> whereClauseValidate = new ArrayList<>();
            whereClauseValidate.add("Validate::Yes");
            whereClauseValidate.add(DATA_MODULE + module);
            whereClauseValidate.add(DATA_INTERFACENAME + interfaceName);

            Map<String, List<String>> dictResponseValidationData = ExcelUtil.fetchWithCondition(interfaceTestSettings.getResponseValidationFilePath(), interfaceTestSettings.getResponseValidationSheetName(), whereClauseValidate);

            if (!dictResponseValidationData.isEmpty() && !dictResponseValidationData.get(RESPONSETYPE).isEmpty()) {
                responsevalidationModel.setResponseType(dictResponseValidationData.get(RESPONSETYPE).get(0));
                for (int i = 0; i < dictResponseValidationData.get(INTERFACENAME).size(); i++) {
                    String xpathJsonpath = dictResponseValidationData.get(XPATH_JSONPATH).get(i);
                    String expectedValue = replacePlaceholders(dictResponseValidationData.get("ExpectedValue").get(i), tempDictData);
                    expectedValue = APISessionData.replaceSessionData(testCase, module, "", iteration, expectedValue);

                    String dataElements = replacePlaceholders(dictResponseValidationData.get("ExpectedDataValue").get(i), tempDictData);
                    dataElements = APISessionData.replaceSessionData(testCase, module, "", iteration, dataElements);

                    if (!xpathJsonpath.isEmpty()) {
                        responsevalidationModel.getxPathJsonPath().put(xpathJsonpath, expectedValue);
                    }

                    if (!dataElements.isEmpty()) {
                        responsevalidationModel.getDataElements().add(dataElements);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("Error while reading the Response Validation Sheet: {}", e.getMessage(), e);
            throw new FileNotFoundException("Error while reading the Response Validation Sheet");
        }
        return responsevalidationModel;
    }

    private StoreResponseModel fetchStoreResponseData(APIConfig interfaceTestSettings, String module, String interfaceName) throws FileNotFoundException {
        StoreResponseModel storeResponseModel = new StoreResponseModel();
        try {
            ArrayList<String> whereClauseStore = new ArrayList<>();
            whereClauseStore.add("Capture::Yes");
            whereClauseStore.add(DATA_MODULE + module);
            whereClauseStore.add(DATA_INTERFACENAME + interfaceName);

            Map<String, List<String>> dictStoreResponseData = ExcelUtil.fetchWithCondition(interfaceTestSettings.getStoreResponseDataFilePath(), interfaceTestSettings.getStoreResponseDataSheetName(), whereClauseStore);

            if (!dictStoreResponseData.isEmpty() && !dictStoreResponseData.get(RESPONSETYPE).isEmpty()) {
                storeResponseModel.setResponseType(dictStoreResponseData.get(RESPONSETYPE).get(0));
                for (int i = 0; i < dictStoreResponseData.get(INTERFACENAME).size(); i++) {
                    String xpathJsonpath = dictStoreResponseData.get(XPATH_JSONPATH).get(i);
                    String variableName = dictStoreResponseData.get("VariableName").get(i);
                    storeResponseModel.getXpathJsonPath().put(xpathJsonpath, variableName);
                }
            }
        } catch (Exception e) {
            logger.error("Error while reading the Store Response Data Sheet: {}", e.getMessage(), e);
            throw new FileNotFoundException("Error while reading the Store Response Data Sheet");
        }
        return storeResponseModel;
    }

    private Map<String, String> fetchHeaderData(APIConfig interfaceTestSettings, String module, String interfaceName, Map<String, String> tempDictData, String testCase, String iteration) throws FileNotFoundException {
        Map<String, String> headerData = new HashMap<>();
        try {
            ArrayList<String> whereClauseHeader = new ArrayList<>();
            whereClauseHeader.add(DATA_MODULE + module);
            whereClauseHeader.add(DATA_INTERFACENAME + interfaceName);
            Map<String, List<String>> dictHeaderData = ExcelUtil.fetchWithCondition(interfaceTestSettings.getHeaderRepository(), interfaceTestSettings.getHeaderRepositorySheetName(), whereClauseHeader);

            if (!dictHeaderData.get(INTERFACENAME).isEmpty()) {
            	for (Map.Entry<String, List<String>> entry : dictHeaderData.entrySet()) {
            	    String headerKey = entry.getKey();
            	    String headerValue = replacePlaceholders(entry.getValue().get(0), tempDictData);
            	    headerData.put(headerKey, headerValue);
            	}
            }

            headerData = APISessionData.replaceSessionDataCollection(testCase, module, "", iteration, headerData);
        } catch (Exception e) {
            logger.error("Error while reading the data from the Header Repository: {}", e.getMessage(), e);
            throw new FileNotFoundException("Error while reading the data from the Header Repository");
        }
        return headerData;
    }

    public String replaceTestData(Map<String, String> testDictData, String value) {
        return testDictData.entrySet().stream().reduce(value, (s, e) -> s.replace(e.getKey(), e.getValue()), (s1, s2) -> null);
    }
}
