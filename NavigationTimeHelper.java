package uiperformanceutilities.utilities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonProperty;

import uiperformanceutilities.model.EntryModel;


import org.json.JSONException;
import org.json.JSONObject;


public class NavigationTimeHelper {
	
	private static final Logger logger =LoggerFactory.getLogger(NavigationTimeHelper.class.getName());

	  Map<String, Object> timings = null; 
	  static final String JAVASCRIPTFORPERFORMANCE = "var performance = window.performance || window.webkitPerformance || window.mozPerformance || window.msPerformance || {};var timings = performance.timing || {};return timings;"; 
	   
	  public String getAllTiming(WebDriver driver) throws JSONException {
		    JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
		    Object timingss = jsExecutor.executeScript(JAVASCRIPTFORPERFORMANCE);

		    if (timingss instanceof String) {
		        String strTimings = (String) timingss;
		        String s1 = strTimings.replace('=', ':');
		        JSONObject json = new JSONObject(s1);
		        return json.toString();
		    } else {
		        return timingss.toString();
		    }
		}

	  
	  public class RootGetAllTimings{
		  
		  public RootGetAllTimings() {
			  
			  throw new UnsupportedOperationException("Default constructor should not be used directly.");
			  
		  }
		  
		  	@JsonProperty("unloadEventEnd")
		    public int unloadEventEnd;
		  	@JsonProperty("responseEnd")
		    public long responseEnd;
		  	@JsonProperty("responseStart")
		    public long responseStart;
		  	@JsonProperty("domInteractive")
		    public long domInteractive;
		  	@JsonProperty("domainLookupEnd")
		    public long domainLookupEnd;
		  	@JsonProperty("unloadEventStart")
		    public int unloadEventStart;
		  	@JsonProperty("domComplete")
		    public long domComplete;
		  	@JsonProperty("domContentLoadedEventStart")
		    public long domContentLoadedEventStart;
		  	@JsonProperty("domainLookupStart")
		    public long domainLookupStart;
		  	@JsonProperty("redirectEnd")
		    public int redirectEnd;
		  	@JsonProperty("redirectStart")
		    public int redirectStart;
		  	@JsonProperty("connectEnd")
		    public long connectEnd;
		  	@JsonProperty("connectStart")
		    public long connectStart;
		  	@JsonProperty("loadEventStart")
		    public long loadEventStart;
		  	@JsonProperty("navigationStart")
		    public long navigationStart;
		  	@JsonProperty("requestStart")
		    public long requestStart;
		  	@JsonProperty("secureConnectionStart")
		    public long secureConnectionStart;
		  	@JsonProperty("fetchStart")
		    public long fetchStart;
		  	@JsonProperty("domContentLoadedEventEnd")
		    public long domContentLoadedEventEnd;
		  	@JsonProperty("domLoading")
		    public long domLoading;
		  	@JsonProperty("loadEventEnd")
		    public long loadEventEnd;
		}

	   public long getPageLoadTime(WebDriver driver) {
		   
		   JavascriptExecutor jsrunner = (JavascriptExecutor) driver;
		   
		   return (Long)jsrunner.executeScript("return (window.performance.timing.loadEventEnd-window.performance.timing.responseStart)");
	
	   }
	   
	   public long getTTFB(WebDriver driver) {
		   
		   JavascriptExecutor jsrunner = (JavascriptExecutor) driver;
		   
		   return (Long)jsrunner.executeScript("return (window.performance.timing.responseStart-window.performance.timing.navigationStart)");
	   }
	   
	   public long getEndtoendRespTime(WebDriver driver) {
		   
		   JavascriptExecutor jsrunner = (JavascriptExecutor) driver;
		   
		   return (Long)jsrunner.executeScript("return (window.performance.timing.loadEventEnd-window.performance.timing.navigationStart)");
           
	   }
	   
	   public String getDomComplete(WebDriver driver) {
		    JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
		    Object domComplete = jsExecutor.executeScript("return window.performance.getEntries()");

		    if (domComplete instanceof String) {
		        String domCompleteString = (String) domComplete;
		        return domCompleteString.replace('=', ':');
		    } else {
		        return domComplete.toString();
		    }
		}

	   
	   public List<String> getPerfEntries(WebDriver driver) {
		   
		   JavascriptExecutor jsrunner = (JavascriptExecutor) driver;
		   var domComplete = jsrunner.executeScript("return window.performance.getEntries().length");
			 
			 int entriesDOM = Integer.parseInt(domComplete.toString());
			 
			 ArrayList<String> entries = new ArrayList<>();	 
			 ArrayList<String> entriesNameString = new ArrayList<>();
			 
			 for (int i =0;i<entriesDOM;i++)
			 {
				 try {
					 var domNameEntries = jsrunner.executeScript("return window.performance.getEntries()["+i+"].name");
					 entriesNameString.add(domNameEntries.toString());
				 }
				 catch(Exception e) {
					e.printStackTrace();
				 }
				
			 } 		 
			 entries.add("Name|EntryType|Duration(ms)|InitiatorType|Size");
			for(int j=0;j<entriesNameString.size();j++) {
				
				String result = "";

				boolean resultCheck = true;
				
	            var duration =  jsrunner.executeScript("return window.performance.getEntriesByName("+"'" +entriesNameString.get(j)+"'"+ ")[0].duration");
	            var entryType =  jsrunner.executeScript("return window.performance.getEntriesByName("+"'" +entriesNameString.get(j)+"'"+ ")[0].entryType");
	            var initiatorType =  jsrunner.executeScript("return window.performance.getEntriesByName("+"'" +entriesNameString.get(j)+"'"+ ")[0].initiatorType");
	            var size =  jsrunner.executeScript("return window.performance.getEntriesByName("+"'" +entriesNameString.get(j)+"'"+ ")[0].transferSize");
	  
	            String strEntryType = entryType != null ? entryType.toString() : "N/A";
	            String strInitiatorType = initiatorType != null ? initiatorType.toString() : "N/A";
	            String strSize = size != null ? size.toString() : "N/A";
	            String strDuration = duration != null ? duration.toString() : "N/A";

	            if(resultCheck) {
	            	
	            	 
	   				result = entriesNameString.get(j) + " | " + strEntryType + " | " +strDuration + " | " +strInitiatorType + " | " +strSize;
	            }
	         
	            entries.add(result);
	            logger.info("{}",entries);
	            
			}
	            return entries;
	            
			}
	   
	
	   
	   public Map <String,ArrayList<EntryModel>> groupedEntryMap(List<EntryModel> lstEntryModel)
	   {
		   HashMap <String,ArrayList<EntryModel>> mapEntryModel= new HashMap <>();
		   for(EntryModel entryModel: lstEntryModel)
		   {
			   if(mapEntryModel.containsKey(entryModel.getInitiatorType()))
			   {
				   ArrayList<EntryModel> lclentryModelList= mapEntryModel.get(entryModel.getInitiatorType());
				   lclentryModelList.add(entryModel);
				   mapEntryModel.put(entryModel.getInitiatorType(), lclentryModelList);   
			   }
			   
			   else
			   {
				   ArrayList<EntryModel> lclentryModelList= new ArrayList<>();
				   lclentryModelList.add(entryModel);
				   mapEntryModel.put(entryModel.getInitiatorType(), lclentryModelList);
			   }
		   }
		   
		   return mapEntryModel;
	   }
	   
	   
	   
	   
	   public List<EntryModel> getEntryModelDetails(WebDriver driver,long starttime)
	   {
		   ArrayList<EntryModel> entryModelList= new ArrayList<>();
		  
		   JavascriptExecutor jsrunner = (JavascriptExecutor) driver;
		   var domComplete = jsrunner.executeScript("return window.performance.getEntries().length");
			 
			 int entriesDOM = Integer.parseInt(domComplete.toString());
			 
			 ArrayList<String> entriesNameString = new ArrayList<>();
			 for (int i =0;i<entriesDOM;i++)
			 {
				 try {
					 var domNameEntries = jsrunner.executeScript("return window.performance.getEntries()["+i+"].name");
					 entriesNameString.add(domNameEntries.toString());
				 }
				 catch(Exception e) {
					e.printStackTrace();
				 }				
			 } 		 
			for(int j=0;j<entriesNameString.size();j++) 
			{
				EntryModel entryModel= new EntryModel();
	            var duration = jsrunner.executeScript("return window.performance.getEntriesByName("+"'" +entriesNameString.get(j)+"'"+ ")[0].duration");
	            var entryType = jsrunner.executeScript("return window.performance.getEntriesByName("+"'" +entriesNameString.get(j)+"'"+ ")[0].entryType");
	            var initiatorType = jsrunner.executeScript("return window.performance.getEntriesByName("+"'" +entriesNameString.get(j)+"'"+ ")[0].initiatorType");
	            var startTime=jsrunner.executeScript("return window.performance.getEntriesByName("+"'" +entriesNameString.get(j)+"'"+ ")[0].startTime");
	            var transferSize=jsrunner.executeScript("return window.performance.getEntriesByName("+"'" +entriesNameString.get(j)+"'"+ ")[0].transferSize");	           
	            

	            String strEntryType = Optional.ofNullable(entryType).map(Object::toString).orElse("N/A");
	            String strInitiatorType = Optional.ofNullable(initiatorType).map(Object::toString).orElse("N/A");
	            String strSize = Optional.ofNullable(transferSize).map(Object::toString).orElse("N/A");

	            long starTime = Optional.ofNullable(startTime)
	                    .filter(st -> !st.toString().equals("0"))
	                    .map(st -> Long.parseLong(st.toString().split("\\.")[0]))
	                    .orElse(0L);

	            long durationLong = Optional.ofNullable(duration)
	                    .filter(d -> !d.toString().equals("0"))
	                    .map(d -> Long.parseLong(d.toString().split("\\.")[0]))
	                    .orElse(0L);

	            Optional.ofNullable(duration)
	                    .filter(d -> !d.toString().equals("0"))
	                    .ifPresent(d -> logger.info("Duration: {}", d));
	            
	            entryModel.setEntryName(entriesNameString.get(j));
	            entryModel.setDuration(String.valueOf(durationLong));
	            entryModel.setEntryType(strEntryType);
	            entryModel.setInitiatorType(strInitiatorType);
	            entryModel.setTransferSize(strSize);
	            entryModel.setStartTime(UIPerfUtilities.addTimeinMilliseconds(starttime, starTime));
	            entryModel.setEndTime(UIPerfUtilities.addTimeinMilliseconds(starttime,starTime+durationLong));
	           
	            entryModelList.add(entryModel);
	            
			}
	            return entryModelList;		   
	   }
	   
}