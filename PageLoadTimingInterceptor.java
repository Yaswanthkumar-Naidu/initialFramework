package reusable;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map.Entry;

import testsettings.TestRunSettings;
import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class PageLoadTimingInterceptor implements InvocationHandler {
	private static final Logger logger =LoggerFactory.getLogger(PageLoadTimingInterceptor.class.getName());
	   

    private WebDriver driver;
    
    public PageLoadTimingInterceptor(WebDriver driver) {
        this.driver = driver;
       
    }
    public static WebDriver createProxy(WebDriver driver) {
        Class<?>[] interfaces = {WebDriver.class, JavascriptExecutor.class,TakesScreenshot.class};
        
        Object proxyInstance = Proxy.newProxyInstance(
                WebDriver.class.getClassLoader(),
                interfaces,
                new PageLoadTimingInterceptor(driver)
        );
        
        if (proxyInstance instanceof WebDriver && proxyInstance instanceof JavascriptExecutor) 
        {
            return (WebDriver) proxyInstance;
        }
        if (proxyInstance instanceof WebDriver) {
            WebDriver proxyDriver = (WebDriver) proxyInstance;

            if (proxyInstance instanceof JavascriptExecutor) {
                return proxyDriver;
            } 
          
        } else {
            
            return null;
        }
		return driver;
    }
    
    private LinkedHashMap<String, String> methodPreviousUrls = new LinkedHashMap<>();
    private LinkedHashMap<String, String> previousmethodUrls = new LinkedHashMap<>();

    LocalTime previoustime = null;
    String previousUrl = null;
    String previousmethodurl=null;
    @Override
    public  Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        
        Object result; 
        

            result = method.invoke(driver, args);
            String currentUrl = driver.getCurrentUrl();
         
            String previousScreenName = previousUrl;
            String curentScreenName;
            try 
            {

            		curentScreenName=ScreenNameFetcher.getScreenName(currentUrl);

            }
            catch(Exception e)
            {
            	 curentScreenName=ScreenNameFetcher.getScreenName(currentUrl);
            }

        	if(previousScreenName==null ||previousScreenName.equalsIgnoreCase("null") ) 
        	{
        		previousScreenName="None";
        	}

        	if(curentScreenName==null ||curentScreenName.equalsIgnoreCase("null") ) 
        	{
        		curentScreenName=currentUrl;
        	}
            
        	
            LocalTime currentTime=LocalTime.now();            
            extracted(method, result, currentUrl, previousScreenName, curentScreenName, currentTime);
           
            if(methodPreviousUrls.isEmpty()) 
         	 {
            	previoustime=LocalTime.now();
        		previousmethodUrls.put(method.getName(), currentUrl);

         		 methodPreviousUrls.put(method.getName(), curentScreenName); 
         		previousUrl = methodPreviousUrls.get(method.getName());
         		previousmethodurl = previousmethodUrls.get(method.getName());
         	 }
        
        return result;
    }
	private void extracted(Method method, Object result, String currentUrl, String previousScreenName,
			String curentScreenName, LocalTime currentTime) {
		if (result != null && !implementsTakesScreenshot(result) && !currentUrl.equals(previousmethodurl)) {
		    	Duration duration = null ;
		    	long differenceInSeconds=0;
		    	
		    	
		    	if(previoustime != null) 
		    	{
		    		duration= Duration.between(previoustime, currentTime);

		    		differenceInSeconds = duration.getSeconds();
		    	}
		    	
		    	else
		    	{
		    		duration= Duration.between(currentTime, LocalTime.now());

		    		differenceInSeconds = duration.getSeconds();
		    	}
		    	
		        
		    	if(previousUrl!=null && differenceInSeconds>0) 
		    	{ 
		    		
		    		LinkedHashMap<String,String> screenNames=new LinkedHashMap<>();
		    		ArrayList<String> timediff=new ArrayList<>();

		    		screenNames.put(previousScreenName, curentScreenName);
		    		timediff.add(String.valueOf(differenceInSeconds));
		    		
		    		double pageLoadtiming=getPageLoadTimeInSeconds(driver);
		     		double dOMLoadtiming=getDomLoadTime(driver);
		    		
		    		extracted(screenNames, timediff, pageLoadtiming, dOMLoadtiming);

		    		 previousmethodUrls.put(method.getName(), currentUrl);
		    		 methodPreviousUrls.put(method.getName(), curentScreenName);
		    		 previousUrl = methodPreviousUrls.get(method.getName());
		    		 previousmethodurl = previousmethodUrls.get(method.getName());
		    		 previoustime=LocalTime.now();
		    	}
		
		    }  
		}
	private void extracted(LinkedHashMap<String, String> screenNames, ArrayList<String> timediff, double pageLoadtiming,
			double dOMLoadtiming) {
		appendValue(TestRunSettings.getUiperfdata(), screenNames , timediff,pageLoadtiming,dOMLoadtiming);
	}
    
    private static void appendValue(Map<HashMap<String, String>, ArrayList<String>> map, HashMap<String, String> key, ArrayList<String> value, double pageLoad, double domLoad) {
    	
    	boolean valuedescision=false;
    	
    	Map.Entry<HashMap<String, String>, ArrayList<String>> lastEntry = null;

        for (Map.Entry<HashMap<String, String>, ArrayList<String>> entry : map.entrySet())
        {
            lastEntry = entry;
        }
        if(lastEntry!=null) 
        {
        	 for(Entry<String, String> keyvalue: key.entrySet()) 
         	{
         		if (lastEntry.getKey().containsValue(keyvalue.getValue())) {
         			valuedescision=true;
                 }
         	}
        }
    	
        if  (map.containsKey(key) && !valuedescision) {
            ArrayList<String> existingValue = map.get(key);
            ArrayList<String> appendedValue = new ArrayList<>() ;
            
            int existingvaluesize=existingValue.size();
            for(int i=0; i<existingvaluesize; i++) 
            {
            	appendedValue.addAll(existingValue);
            }
            appendedValue.addAll(value);
            map.put(key, appendedValue);
        } 
        else if (!valuedescision)
        {
        	
            map.put(key, value);
            
            TestRunSettings.getPageLoadtime().add(String.valueOf(pageLoad));
    		TestRunSettings.getDomLoadtime().add(String.valueOf(domLoad));
        }
    }
       
    private boolean implementsTakesScreenshot(Object object) {
        try {
            Class<?> takesScreenshotClass = Class.forName("org.openqa.selenium.TakesScreenshot");

            return takesScreenshotClass.isAssignableFrom(object.getClass()) ||
                   (Proxy.isProxyClass(object.getClass()) &&
                    ((PageLoadTimingInterceptor) Proxy.getInvocationHandler(object)).driver.getClass().isAssignableFrom(takesScreenshotClass)) ||
                   object.getClass().getMethod("getScreenshotAs", OutputType.class) != null;
        } catch (ClassNotFoundException | NoSuchMethodException e) {
            return false;
        }
    }

        private double getPageLoadTimeInSeconds(WebDriver driver) {
            JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
            
            long pageLoadTimeMillis = (long) jsExecutor.executeScript(
                    "return (window.performance.timing.loadEventEnd - window.performance.timing.navigationStart);"
            );
       
            return pageLoadTimeMillis / 1000.0;

        }
        
        public static double getDomLoadTime(WebDriver driver) {
            JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;


            Object domLoadTime = jsExecutor.executeScript(
                    "return (window.performance.timing.domContentLoadedEventEnd - window.performance.timing.navigationStart);");

            return (domLoadTime instanceof Long) ? ((Long) domLoadTime) / 1000.0 : 0;
        }
        
        public static double getPageLoadStartTime(WebDriver driver) {
            JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;

            Object pageLoadStartTime = jsExecutor.executeScript(
                    "return window.performance.timing.navigationStart;");

            return (pageLoadStartTime instanceof Long) ? ((Long) pageLoadStartTime)/1000.0: 0;
        }

        public static double getPageReadyTime(WebDriver driver) {
            JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;

            Object pageReadyTime = jsExecutor.executeScript(
                    "return window.performance.timing.domContentLoadedEventEnd;");

            return (pageReadyTime instanceof Long) ? ((Long) pageReadyTime)/1000.0 : 0;
        }

        public static double getRedirectionTime(WebDriver driver, String url) {
            JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;

            jsExecutor.executeScript("window.location.href = arguments[0];", url);

            Object redirectionTime = jsExecutor.executeScript(
                    "return window.performance.timing.redirectEnd - window.performance.timing.redirectStart;");

            return (redirectionTime instanceof Long) ? ((Long) redirectionTime)/1000.0 : 0;
        }
                                
        public double pageloadtimeusingHttp(String url, long startTime) throws IOException 
        {
        	double loadTime = 0;
        	try {
              // Open the connection
              HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
              connection.setRequestMethod("GET");
              
              // Get the response code
              int responseCode = connection.getResponseCode();
              
              // Ensure a successful response code (e.g., 200 OK)
              if (responseCode == HttpURLConnection.HTTP_OK) {
                  long endTime = System.currentTimeMillis();
                   long loadTiming = endTime - startTime;
                   loadTime=loadTiming/1000.0;
              } 
              else 
              {
            	  loadTime = 0;
                  logger.info("Failed to load page. Response Code: {}",responseCode);
              }
        	}
        	catch(Exception e) 
        	{
        		loadTime = 0;
        	}
              
              return loadTime;
        }
        
        public double domloadtimeusingHttp(long startTime) 
        {
        	double loadTime = 0;
        	try {
        		 waitForDOMLoad(driver);

                 long endTime = System.currentTimeMillis();
                 long loadTiming = endTime - startTime;
                 loadTime=loadTiming/1000.0;
        	}
        	catch(Exception e) 
        	{
        		loadTime = 0;
        	}
              
              return loadTime;
        }
        
        private static void waitForDOMLoad(WebDriver driver) {
            JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;

            // Wait until the document.readyState is complete
            jsExecutor.executeScript("return document.readyState").equals("complete");
        }
       
}