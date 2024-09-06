package uiperformanceutilities.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.WebDriver;

import uiperformanceutilities.utilities.NavigationTimeHelper;
import uiperformanceutilities.utilities.UIPerfUtilities;

public class UIPerformanceModel {

	private String sourceScreen="";
	private String destinationScreen="";
	private String sourceDestinationScreen="";
	private int screenCount=0;
	private String responseTime="";
	private long responseTimeMillisecond=0;
	private String testCaseName="";
	private String moduleName="";
	private String browserName="";
	private String startTime="";
	private String endtime="";
	private long pageLoadTime=0;
	private long ttfb=0;
	private long endtoendRespTime=0;
	
	private String navigationTimeDetails="";
	private long connectEnd=0;
	private String connectStart="";
	private String domComplete="";
	private String domContentLoadedEventEnd="";
	private String domContentLoadedEventStart="";
	private String domInteractive="";
	private String domLoading="";
	private String domainLookupEnd="";
	private String domainLookupStart="";
	private String fetchStart="";
	private String loadEventEnd="";
	private String loadEventStart="";
	private String navigationStart="";
	private String redirectEnd="";
	private String redirectStart="";
	private String requestStart="";
	private String responseEnd="";
	private String responseStart="";
	private String secureConnectionStart="";
	
	private String unloadEventStart="";
	
	public String getSourceScreen() {
		return sourceScreen;
	}

	public void setSourceScreen(String sourceScreen) {
		this.sourceScreen = sourceScreen;
	}


	public String getDestinationScreen() {
		return destinationScreen;
	}


	public void setDestinationScreen(String destinationScreen) {
		this.destinationScreen = destinationScreen;
	}


	public String getSourceDestinationScreen() {
		return sourceDestinationScreen;
	}


	public void setSourceDestinationScreen(String sourceDestinationScreen) {
		this.sourceDestinationScreen = sourceDestinationScreen;
	}


	public int getScreenCount() {
		return screenCount;
	}


	public void setScreenCount(int screenCount) {
		this.screenCount = screenCount;
	}


	public String getResponseTime() {
		return responseTime;
	}


	public void setResponseTime(String responseTime) {
		this.responseTime = responseTime;
	}


	public long getResponseTimeMillisecond() {
		return responseTimeMillisecond;
	}


	public void setResponseTimeMillisecond(long responseTimeMillisecond) {
		this.responseTimeMillisecond = responseTimeMillisecond;
	}


	public String getTestCaseName() {
		return testCaseName;
	}


	public void setTestCaseName(String testCaseName) {
		this.testCaseName = testCaseName;
	}


	public String getModuleName() {
		return moduleName;
	}


	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}


	public String getBrowserName() {
		return browserName;
	}


	public void setBrowserName(String browserName) {
		this.browserName = browserName;
	}


	public String getStartTime() {
		return startTime;
	}


	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}


	public String getEndtime() {
		return endtime;
	}


	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}


	public long getPageLoadTime() {
		return pageLoadTime;
	}


	public void setPageLoadTime(long pageLoadTime) {
		this.pageLoadTime = pageLoadTime;
	}


	public long getTtfb() {
		return ttfb;
	}


	public void setTtfb(long ttfb) {
		this.ttfb = ttfb;
	}


	public long getEndtoendRespTime() {
		return endtoendRespTime;
	}


	public void setEndtoendRespTime(long endtoendRespTime) {
		this.endtoendRespTime = endtoendRespTime;
	}


	public String getNavigationTimeDetails() {
		return navigationTimeDetails;
	}


	public void setNavigationTimeDetails(String navigationTimeDetails) {
		this.navigationTimeDetails = navigationTimeDetails;
	}


	public long getConnectEnd() {
		return connectEnd;
	}


	public void setConnectEnd(long connectEnd) {
		this.connectEnd = connectEnd;
	}


	public String getConnectStart() {
		return connectStart;
	}


	public void setConnectStart(String connectStart) {
		this.connectStart = connectStart;
	}


	public String getDomComplete() {
		return domComplete;
	}


	public void setDomComplete(String domComplete) {
		this.domComplete = domComplete;
	}


	public String getDomContentLoadedEventEnd() {
		return domContentLoadedEventEnd;
	}


	public void setDomContentLoadedEventEnd(String domContentLoadedEventEnd) {
		this.domContentLoadedEventEnd = domContentLoadedEventEnd;
	}


	public String getDomContentLoadedEventStart() {
		return domContentLoadedEventStart;
	}


	public void setDomContentLoadedEventStart(String domContentLoadedEventStart) {
		this.domContentLoadedEventStart = domContentLoadedEventStart;
	}


	public String getDomInteractive() {
		return domInteractive;
	}


	public void setDomInteractive(String domInteractive) {
		this.domInteractive = domInteractive;
	}


	public String getDomLoading() {
		return domLoading;
	}


	public void setDomLoading(String domLoading) {
		this.domLoading = domLoading;
	}


	public String getDomainLookupEnd() {
		return domainLookupEnd;
	}


	public void setDomainLookupEnd(String domainLookupEnd) {
		this.domainLookupEnd = domainLookupEnd;
	}


	public String getDomainLookupStart() {
		return domainLookupStart;
	}


	public void setDomainLookupStart(String domainLookupStart) {
		this.domainLookupStart = domainLookupStart;
	}


	public String getFetchStart() {
		return fetchStart;
	}


	public void setFetchStart(String fetchStart) {
		this.fetchStart = fetchStart;
	}


	public String getLoadEventEnd() {
		return loadEventEnd;
	}


	public void setLoadEventEnd(String loadEventEnd) {
		this.loadEventEnd = loadEventEnd;
	}


	public String getLoadEventStart() {
		return loadEventStart;
	}


	public void setLoadEventStart(String loadEventStart) {
		this.loadEventStart = loadEventStart;
	}


	public String getNavigationStart() {
		return navigationStart;
	}


	public void setNavigationStart(String navigationStart) {
		this.navigationStart = navigationStart;
	}


	public String getRedirectEnd() {
		return redirectEnd;
	}


	public void setRedirectEnd(String redirectEnd) {
		this.redirectEnd = redirectEnd;
	}


	public String getRedirectStart() {
		return redirectStart;
	}


	public void setRedirectStart(String redirectStart) {
		this.redirectStart = redirectStart;
	}


	public String getRequestStart() {
		return requestStart;
	}


	public void setRequestStart(String requestStart) {
		this.requestStart = requestStart;
	}


	public String getResponseEnd() {
		return responseEnd;
	}


	public void setResponseEnd(String responseEnd) {
		this.responseEnd = responseEnd;
	}


	public String getResponseStart() {
		return responseStart;
	}


	public void setResponseStart(String responseStart) {
		this.responseStart = responseStart;
	}


	public String getSecureConnectionStart() {
		return secureConnectionStart;
	}


	public void setSecureConnectionStart(String secureConnectionStart) {
		this.secureConnectionStart = secureConnectionStart;
	}


	public List<String> getEntries() {
		return entries;
	}


	public void setEntries(List<String> entries) {
		this.entries = entries;
	}


	public List<EntryModel> getEntryModel() {
		return entryModel;
	}


	public void setEntryModel(List<EntryModel> entryModel) {
		this.entryModel = entryModel;
	}


	public List<EntryModel> getLstEntryModel() {
		return lstEntryModel;
	}


	public void setLstEntryModel(List<EntryModel> lstEntryModel) {
		this.lstEntryModel = lstEntryModel;
	}


	public Map<String, ArrayList<EntryModel>> getMapEntryModel() {
		return mapEntryModel;
	}


	public void setMapEntryModel(Map<String, ArrayList<EntryModel>> mapEntryModel) {
		this.mapEntryModel = mapEntryModel;
	}


	public NavigationTimeHelper getNavigationTimeHelper() {
		return navigationTimeHelper;
	}


	public void setNavigationTimeHelper(NavigationTimeHelper navigationTimeHelper) {
		this.navigationTimeHelper = navigationTimeHelper;
	}



	public String getUnloadEventStart() {
		return unloadEventStart;
	}


	public void setUnloadEventStart(String unloadEventStart) {
		this.unloadEventStart = unloadEventStart;
	}


	private List<String> entries = new ArrayList<>();
	private List<EntryModel> entryModel = new ArrayList<>();
	private List<EntryModel> lstEntryModel = new ArrayList<>();
	private Map<String, ArrayList<EntryModel>> mapEntryModel = new HashMap<>();
	
	NavigationTimeHelper navigationTimeHelper = new NavigationTimeHelper(); 
	
	public UIPerformanceModel()
	{
		throw new UnsupportedOperationException("Default constructor should not be used directly.");
	}
	
	
	public UIPerformanceModel addUiPerfModel(String sourceScreen, String testCaseName, String moduleName, String browserName,
			long startTime,WebDriver driver)
	{
		
		long endTime= System.currentTimeMillis();
		UIPerfUtilities uiPerfUtilities = new UIPerfUtilities();
		
		UIPerformanceModel uiPerfModel= new UIPerformanceModel();
		
		uiPerfModel.sourceScreen=sourceScreen;
		uiPerfModel.destinationScreen=destinationScreen;
		uiPerfModel.sourceDestinationScreen=sourceScreen;
		uiPerfModel.screenCount=screenCount;
		uiPerfModel.testCaseName=testCaseName;
		uiPerfModel.moduleName=moduleName;
		uiPerfModel.browserName=browserName;
		uiPerfModel.startTime=uiPerfUtilities.localDateTimeToString(startTime);
		uiPerfModel.endtime=uiPerfUtilities.localDateTimeToString(endTime);
		uiPerfModel.pageLoadTime=navigationTimeHelper.getPageLoadTime(driver);
		uiPerfModel.ttfb=navigationTimeHelper.getTTFB(driver);
		uiPerfModel.endtoendRespTime=navigationTimeHelper.getEndtoendRespTime(driver);
		uiPerfModel.entries=navigationTimeHelper.getPerfEntries(driver);
		uiPerfModel.lstEntryModel=navigationTimeHelper.getEntryModelDetails(driver,startTime);
		uiPerfModel.mapEntryModel=navigationTimeHelper.groupedEntryMap(uiPerfModel.lstEntryModel);
		
		uiPerfModel.responseTimeMillisecond=uiPerfUtilities.getDurationinMillisecond(startTime, endTime);
		uiPerfModel.responseTime=uiPerfUtilities.getDuration(startTime, endTime);
		return uiPerfModel;
	}
	
}