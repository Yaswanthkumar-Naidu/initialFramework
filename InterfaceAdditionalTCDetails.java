package report_utilities.model;

import report_utilities.extent_report.ExtentConstants.TestStepStatus;

public class InterfaceAdditionalTCDetails
{
    private long interfaceAdditionalTCDetailsId;
    private long testRunId;
    private String testCaseId;
    private String interfaceName;
    private String requestData;
    private String responseData;
    private TestStepStatus testCaseStatus;


    public void setInterfaceAdditionalTCDetails(long testRunId, String testCaseId, String interfaceName, String requestData, String responseData, TestStepStatus status)
    {
        this.setTestRunId(testRunId);
        this.setTestCaseId(testCaseId);
        this.setInterfaceName(interfaceName);
        this.setRequestData(requestData);
        this.setResponseData(responseData);
        this.setTestCaseStatus(status);
    }


	public void setResponseData(String responseData) {
		this.responseData = responseData;
	}


	public long getInterfaceAdditionalTCDetailsId() {
		return interfaceAdditionalTCDetailsId;
	}


	public void setInterfaceAdditionalTCDetailsId(long interfaceAdditionalTCDetailsId) {
		this.interfaceAdditionalTCDetailsId = interfaceAdditionalTCDetailsId;
	}


	public long getTestRunId() {
		return testRunId;
	}


	public void setTestRunId(long testRunId) {
		this.testRunId = testRunId;
	}


	public String getTestCaseId() {
		return testCaseId;
	}


	public void setTestCaseId(String testCaseId) {
		this.testCaseId = testCaseId;
	}


	public String getInterfaceName() {
		return interfaceName;
	}


	public void setInterfaceName(String interfaceName) {
		this.interfaceName = interfaceName;
	}


	public String getRequestData() {
		return requestData;
	}


	public void setRequestData(String requestData) {
		this.requestData = requestData;
	}


	public String getResponseData() {
		return responseData;
	}

	public TestStepStatus getTestCaseStatus() {
		return testCaseStatus;
	}


	public void setTestCaseStatus(TestStepStatus testCaseStatus) {
		this.testCaseStatus = testCaseStatus;
	}

}
