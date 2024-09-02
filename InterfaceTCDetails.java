package report_utilities.model;

import java.time.LocalDateTime;

import report_utilities.ExtentReport.ExtentConstants.TestStepStatus;

public class InterfaceTCDetails
{
    public long testRunId;
    public String testCaseId;
    public String module;
    public int iteration;
    public String interfaceName = "";
    public String requestData;
    public String responseData;
    public LocalDateTime startTime;
    public LocalDateTime endTime;
    public int duration;
    public String tcStatus = "";
    public TestStepStatus testCaseStatus;
    public String statusCode = "";
    public String errorMessage = "";
    public String fileFormat = "";


    public void setInterfaceTCDetails(long testRunId, String testCaseId,
        String moduleName, String interfaceName, int iteration,
       String requestData,String responseData,String fileFormat,LocalDateTime startTime,
       LocalDateTime endTime, String tcStatus,String statusCode,String errorMessage)
    {
        this.testRunId = testRunId;
        this.testCaseId = testCaseId;
        this.module = moduleName;
        this.interfaceName = interfaceName;
        this.iteration = iteration;
        this.requestData = requestData;
        this.responseData = responseData;
        this.startTime = startTime;
        this.endTime = startTime;
        this.tcStatus = tcStatus;
        this.testCaseStatus = setTestCaseStatus(tcStatus);
        this.fileFormat = fileFormat;
        this.startTime = startTime;
        this.endTime = endTime;
        this.duration = endTime.getSecond() - startTime.getSecond();
        this.statusCode = statusCode;
        this.errorMessage = errorMessage;
    }

    public TestStepStatus setTestCaseStatus(String tcStatus)
    {
        try
        {
        	if (tcStatus.toUpperCase().equalsIgnoreCase("PASS"))
            {
                testCaseStatus = TestStepStatus.PASS;
            }
            else if (tcStatus.toUpperCase().equalsIgnoreCase("PASSED"))
            {
                testCaseStatus = TestStepStatus.PASS;
            }
            else if (tcStatus.toUpperCase().equalsIgnoreCase("FAIL"))
            {
                testCaseStatus = TestStepStatus.FAIL;
            }
            else if (tcStatus.toUpperCase().equalsIgnoreCase("FAILED"))
            {
                testCaseStatus = TestStepStatus.FAIL;
            }
            else
            {
                testCaseStatus = TestStepStatus.SKIP;
            }
        }
        catch (Exception e)
        {
            testCaseStatus = TestStepStatus.SKIP;
        }

        return testCaseStatus;


    }

    public TestStepStatus getTestCaseStatus()
    {
        return testCaseStatus;
    }
    }

