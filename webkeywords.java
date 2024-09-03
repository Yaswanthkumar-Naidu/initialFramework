package common_utilities.common.action_keywords;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import report_utilities.Model.TestCaseParam;
import report_utilities.Model.ExtentModel.ExtentUtilities;
import report_utilities.Model.ExtentModel.PageDetails;
import report_utilities.common.ReportCommon;
import report_utilities.common.ScreenshotCommon;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;



public class webkeywords
{
	private static final String LOG_SWITCHED_TO = "Switched to: {}";

	public static enum SelectType
	{
		SELECT_BY_INDEX,
		SELECT_BY_TEXT,
		SELECT_BY_VALUE,
	}

	private static final webkeywords _instance = new webkeywords();
	private static final Logger logger =LoggerFactory.getLogger(webkeywords.class.getName());
	private static final String Status_Pass="PASS";
	private static final String Status_Fail="FAIL";
	private static final String Status_Done="DONE";
	ExtentUtilities extentUtilities = new ExtentUtilities();
	ScreenshotCommon SCM = new ScreenshotCommon();

	ReportCommon TestStepDetails = new ReportCommon();
	ReportCommon exceptionDetails = new ReportCommon();
	
	public static webkeywords Instance()
	{

		return _instance;

	}
	/// <summary>
	/// This method is use for 
	/// navigate to URL
	/// User can use param with Uri Ex: /home/contact
	/// </summary>
	/// <param name="url"></param>
	public void Navigate(WebDriver driver, String url, TestCaseParam testCaseParam,PageDetails pageDetails) throws Exception
	{
		String action_nv = "Navigate -> "+url;
		String actionDescription_nv = "";
		LocalDateTime startTime=  LocalDateTime.now();

		try
		{

			logger.info("Url = " + url);
			if (!(url.startsWith("http://") || url.startsWith("https://")))
				throw new Exception("URL is invalid format and cannot open page");
			driver.navigate().to(url);
			LocalDateTime EndTime =  LocalDateTime.now();
			//TestStepDetails.logTestStepDetails(_driver, testCaseParam, Action, ActionDescription, StartTime,"PASS");
			TestStepDetails.logTestStepDetails(driver, testCaseParam, action_nv, actionDescription_nv,pageDetails, startTime, Status_Done);
			logger.info("Successfully Navigated to " + url);

		}
		catch (Exception e)
		{
			logger.error("Failed ==> " + action_nv + actionDescription_nv);
			TestStepDetails.logExceptionDetails(driver, testCaseParam, action_nv, actionDescription_nv, startTime,e);
			TestStepDetails.logTestStepDetails(driver, testCaseParam, action_nv, actionDescription_nv,pageDetails, startTime, Status_Fail);

			throw e;
		}


	}


	/// <summary>
	/// This method will naviagte to URL 
	/// It require param with exactly in URL format 
	/// Ex: https://github.com/minhhoangvn http://github.com/minhhoangvn
	/// </summary>
	/// <param name="url"></param>
	public void OpenUrl(WebDriver _driver, String url, TestCaseParam testCaseParam,PageDetails pageDetails) throws Exception
	{
		String Action = "Navigate -->"+url ;
		String ActionDescription = "";
		LocalDateTime StartTime=  LocalDateTime.now();
		try
		{

			logger.info("Url = " + url);
			if (!(url.startsWith("http://") || url.startsWith("https://")))
				throw new Exception("URL is invalid format and cannot open page");
			_driver.navigate().to(url);
		}
		catch (Exception e)
		{
			logger.error("Failed ==> " + Action + ActionDescription);
			TestStepDetails.logExceptionDetails(_driver, testCaseParam, Action, ActionDescription, StartTime,e);
			throw e;
		}
	}
	/// <summary>
	/// This method is use for
	/// select option from dropdown list or combobox
	/// </summary>
	/// <param name="element"></param>
	/// <param name="type"></param>
	/// <param name="options"></param>
	public void Select(WebDriver _driver, WebElement element, SelectType type, String options, TestCaseParam testCaseParam,PageDetails pageDetails) throws Exception
	{

		String Action = "Select dropdown Value:"+options;
		String ActionDescription = "Select dropdown Value:-"+options;
		LocalDateTime StartTime=  LocalDateTime.now();

		try
		{

			logger.info("Option = " + options);

			switch (type)
			{
			case SELECT_BY_INDEX:
				try
				{


					if (options.equals("N//A") || options.equals("N/A") || options.equals("n//a") || options.equals("n/a") ) 
					{
						break;
					}					

					else
					{

						webkeywords.Instance().FluentWait(_driver,element);
						scrollIntoViewElement(_driver, element);
						if(element.isEnabled()==true) 
						{
							Action = "Selected Value:-"+options;
							ActionDescription = "Selected Value:-"+options;
							Select select = new Select(element);
							select.selectByIndex(Integer.parseInt(options));
							TestStepDetails.logTestStepDetails(_driver, testCaseParam, Action, ActionDescription,pageDetails, StartTime, Status_Done);
						}

					}

				}
				catch (Exception e)
				{
					logger.error("Failed ==> " + Action + ActionDescription);
					TestStepDetails.logExceptionDetails(_driver, testCaseParam, Action, ActionDescription, StartTime,e);
					TestStepDetails.logTestStepDetails(_driver, testCaseParam, Action, ActionDescription,pageDetails, StartTime, Status_Fail);

					throw e;
				}

				break;
			case SELECT_BY_TEXT:

				if (options.equals("N//A") || options.equals("N/A") || options.equals("n//a") || options.equals("n/a") ) 
				{
					break;
				}
				else
				{
					webkeywords.Instance().FluentWait(_driver, element);
					scrollIntoViewElement(_driver, element);
					if(element.isEnabled()==true && element.isDisplayed()==true) 
					{
						Action = "Selected Value:-"+options;
						ActionDescription = "Selected Value:-"+options;
						Select select1 = new Select(element);
						select1.selectByVisibleText(options);
						TestStepDetails.logTestStepDetails(_driver, testCaseParam, Action, ActionDescription,pageDetails, StartTime, Status_Done);
						//VerifyValueSelected(_driver, element,options,testCaseParam,pageDetails);
					}


				}
				break;
			case SELECT_BY_VALUE:

				webkeywords.Instance().FluentWait(_driver, element);
				scrollIntoViewElement(_driver, element);
				//WebKeywords.Instance().WaitElementToBeClickablenew(_driver, element, 5000);
				Select select2 = new Select(element);
				select2.selectByValue(options);
				TestStepDetails.logTestStepDetails(_driver, testCaseParam, Action, ActionDescription,pageDetails, StartTime, Status_Done);

				VerifyValueSelected(_driver, element,options,testCaseParam,pageDetails);
Thread.sleep(100);				break;
			default:
				throw new Exception("Get error in using Selected");
			}
		}
		catch (Exception e)
		{
			logger.error("Failed ==> " + Action + ActionDescription);
			TestStepDetails.logExceptionDetails(_driver, testCaseParam, Action, ActionDescription, StartTime,e);
			TestStepDetails.logTestStepDetails(_driver, testCaseParam, Action, ActionDescription,pageDetails, StartTime, Status_Fail);

			throw e;
		}
	}

	public void trymultipleclick(WebDriver _driver, WebElement element, TestCaseParam testCaseParam,PageDetails pageDetails) throws Exception
	{
		webkeywords.Instance().FluentWait(_driver, element);
		String Action="";
		try 
		{

			if(!getLocatorFromWebElement(element,_driver,testCaseParam,pageDetails).toString().equals(null)) 
			{ 
				Action = "Click --> "+getLocatorFromWebElement(element,_driver,testCaseParam,pageDetails).toString();
			} 
			else

			{
				Action = "Click --> "+getLocatorFromWebElement(element,_driver,testCaseParam,pageDetails).toString();
			}

		}
		catch(Exception e) 
		{
			Action = "Click --> "+getLocatorFromWebElement(element,_driver,testCaseParam,pageDetails).toString();
		}

		String ActionDescription = element.toString();
		LocalDateTime StartTime=  LocalDateTime.now();


		try
		{
			//WebKeywords.Instance().WaitElementClickable(_driver, element);	
			Actions _action = new Actions(_driver);
			_action.moveToElement(element).build().perform(); 
			element.click();
			TestStepDetails.logTestStepDetails(_driver, testCaseParam, Action, ActionDescription,pageDetails, StartTime, Status_Done);


		}
		catch (Exception e)
		{
			try
			{
				//By locatorvalue=getLocatortypeandvalue(element,_driver);
				//WebDriverWait wait = new WebDriverWait(_driver, 10000);
				Actions _action = new Actions(_driver);
				_action.moveToElement(element).build().perform(); 
				element.click();
				TestStepDetails.logTestStepDetails(_driver, testCaseParam, Action, ActionDescription,pageDetails, StartTime, Status_Done);


			}

			catch(Exception ex) 
			{
				try
				{
					//By locatorvalue=getLocatortypeandvalue(element,_driver);
					//WebDriverWait wait = new WebDriverWait(_driver, 10000);
					Actions _action = new Actions(_driver);
					_action.moveToElement(element).build().perform(); 
					element.click();
					TestStepDetails.logTestStepDetails(_driver, testCaseParam, Action, ActionDescription,pageDetails, StartTime, Status_Done);

					/*
					 * WaitwebElementVisible(_driver, element,200);
					 * 
					 * ((JavascriptExecutor)_driver).executeScript("arguments[0].click()",element);
					 */

				}

				catch(Exception exx) 
				{
					try
					{
						//WebKeywords.Instance().WaitElementClickable(_driver, element);	
						//By locatorvalue=getLocatortypeandvalue(element,_driver);
						//WebDriverWait wait = new WebDriverWait(_driver, 10000);
						Actions _action = new Actions(_driver);
						_action.moveToElement(element).build().perform(); 
						element.click();
						TestStepDetails.logTestStepDetails(_driver, testCaseParam, Action, ActionDescription,pageDetails, StartTime, Status_Done);

						/*
						 * WaitwebElementVisible(_driver, element,200);
						 * 
						 * ((JavascriptExecutor)_driver).executeScript("arguments[0].click()",element);
						 */

					}

					catch(Exception exxx) 
					{
						try
						{
								Actions _action = new Actions(_driver);
							_action.moveToElement(element).build().perform(); 
							element.click();
							TestStepDetails.logTestStepDetails(_driver, testCaseParam, Action, ActionDescription,pageDetails, StartTime, Status_Done);



						}

						catch(Exception exxxx) 
						{
							try
							{
								Actions _action = new Actions(_driver);
								_action.moveToElement(element).build().perform(); 
								element.click();
								TestStepDetails.logTestStepDetails(_driver, testCaseParam, Action, ActionDescription,pageDetails, StartTime, Status_Done);



							}

							catch(Exception exp) 
							{
								logger.error("Failed ==> " + Action + ActionDescription);
								TestStepDetails.logExceptionDetails(_driver, testCaseParam, Action, ActionDescription, StartTime,exp);
								TestStepDetails.logTestStepDetails(_driver, testCaseParam, Action, ActionDescription,pageDetails, StartTime, Status_Fail);

								throw e;

							}

						}

					}

				}

			}
			logger.error("Failed ==> " + Action + ActionDescription);
			TestStepDetails.logExceptionDetails(_driver, testCaseParam, Action, ActionDescription, StartTime,e);
			TestStepDetails.logTestStepDetails(_driver, testCaseParam, Action, ActionDescription,pageDetails, StartTime, Status_Fail);

			throw e;
		}
	}

	/// <summary>
	/// This method use for 
	/// click 
	/// </summary>
	/// <param name="element"></param>
/*	public void Click(WebDriver _driver, WebElement element, TestCaseParam testCaseParam,PageDetails pageDetails) throws Exception
	{
		String Action="";
		//Thread.sleep(100);
		WebKeywords.Instance().FluentWait(_driver, element);
		WebKeywords.Instance().WaitElementforelementclickable(_driver, element, 1000);
		ScrollIntoViewElement(_driver, element);
		String ActionDescription = element.toString();
		LocalDateTime StartTime=  LocalDateTime.now();
		
		try 
		{

			if(!getLocatorFromWebElement(element,_driver,testCaseParam,pageDetails).toString().equals(null)) 
			{ 
				Action = "Click --> "+getLocatorFromWebElement(element,_driver,testCaseParam,pageDetails).toString();
				ActionDescription = "Click --> "+getLocatorFromWebElement(element,_driver,testCaseParam,pageDetails).toString();
			} 
			else

			{
				Action = "Click --> "+getLocatorFromWebElement(element,_driver,testCaseParam,pageDetails).toString();
				ActionDescription = "Click --> "+getLocatorFromWebElement(element,_driver,testCaseParam,pageDetails).toString();

			}

		}
		catch(Exception e) 
		{
			Action = "Click --> "+getLocatorFromWebElement(element,_driver,testCaseParam,pageDetails).toString();
			ActionDescription = "Click --> "+getLocatorFromWebElement(element,_driver,testCaseParam,pageDetails).toString();

		} 

		
		try
			{		
				Thread.sleep(300);
				JavascriptExecutor executor = (JavascriptExecutor)_driver;
				executor.executeScript("arguments[0].scrollIntoView(true);", element);
				Thread.sleep(300);
				executor.executeScript("arguments[0].click();", element);

				TestStepDetails.logTestStepDetails(_driver, testCaseParam, Action, ActionDescription,pageDetails, StartTime, Status_Pass);
				Thread.sleep(900);

			}
			catch (Exception e)
			{
				try 
				{
					WebKeywords.Instance().WaitElementforelementclickable(_driver, element, 1000);
					JavascriptExecutor executor = (JavascriptExecutor)_driver;
					executor.executeScript("arguments[0].scrollIntoView(true);", element);
					Thread.sleep(300);
					executor.executeScript("arguments[0].click();", element);

					TestStepDetails.logTestStepDetails(_driver, testCaseParam, Action, ActionDescription,pageDetails, StartTime, Status_Done);

				}
				catch(Exception f) 
				{
					logger.error("Failed ==> " + Action + ActionDescription);
					TestStepDetails.logExceptionDetails(_driver, testCaseParam, Action, ActionDescription, StartTime,e);
					TestStepDetails.logTestStepDetails(_driver, testCaseParam, Action, ActionDescription,pageDetails, StartTime, Status_Fail);

					throw e;
				}

			}
		
	}
*/

	/// <summary>
	/// This method use for 
	/// click 
	/// </summary>
	/// <param name="element"></param>
	//Captures the performance metrics 
	public void Click(WebDriver _driver, WebElement element,String TestData, TestCaseParam testCaseParam,PageDetails pageDetails) throws Exception
	{
		String Action="";

		if((TestData.equalsIgnoreCase("n/a")==false)||(TestData.equalsIgnoreCase("n\\a"))==false||(TestData.equalsIgnoreCase("n\\\\a"))==false)
		{
		webkeywords.Instance().FluentWait(_driver, element);
		webkeywords.Instance().WaitElementforelementclickable(_driver, element, 1000);
		scrollIntoViewElement(_driver, element);
		String ActionDescription = element.toString();
		LocalDateTime StartTime=  LocalDateTime.now();
		String Xpath= getLocatorFromWebElement(element,_driver,testCaseParam,pageDetails);
		
		try 
		{

			if(!getLocatorFromWebElement(element,_driver,testCaseParam,pageDetails).toString().equals(null)) 
			{ 
				Action = "Click --> "+getLocatorFromWebElement(element,_driver,testCaseParam,pageDetails).toString();
				ActionDescription = "Click --> "+getLocatorFromWebElement(element,_driver,testCaseParam,pageDetails).toString();
			} 
			else

			{
				Action = "Click --> "+getLocatorFromWebElement(element,_driver,testCaseParam,pageDetails).toString();
				ActionDescription = "Click --> "+getLocatorFromWebElement(element,_driver,testCaseParam,pageDetails).toString();

			}
	
		}
		catch(Exception e) 
		{
			Action = "Click --> "+getLocatorFromWebElement(element,_driver,testCaseParam,pageDetails).toString();
			ActionDescription = "Click --> "+getLocatorFromWebElement(element,_driver,testCaseParam,pageDetails).toString();

		} 

		

			try
			{		
				
				Actions _action = new Actions(_driver);
				_action.moveToElement(element).build().perform();

				element.click();


				TestStepDetails.logTestStepDetails(_driver, testCaseParam, Action, ActionDescription,pageDetails, StartTime, Status_Pass);
				Thread.sleep(900);


			}
			catch (Exception e)
			{
				try 
				{
					webkeywords.Instance().WaitElementforelementclickable(_driver, element, 1000);
					JavascriptExecutor executor = (JavascriptExecutor)_driver;
					executor.executeScript("arguments[0].scrollIntoView(true);", element);
					Thread.sleep(300);
					executor.executeScript("arguments[0].click();", element);

					TestStepDetails.logTestStepDetails(_driver, testCaseParam, Action, ActionDescription,pageDetails, StartTime, Status_Done);

				}
				catch(Exception f) 
				{
					logger.error("Failed ==> " + Action + ActionDescription);
					TestStepDetails.logExceptionDetails(_driver, testCaseParam, Action, ActionDescription, StartTime,e);
					TestStepDetails.logTestStepDetails(_driver, testCaseParam, Action, ActionDescription,pageDetails, StartTime, Status_Fail);

					throw e;
				}

			}
			//break;
		//}
		}
	}


	public void Clickwithaction(WebDriver _driver, WebElement element, TestCaseParam testCaseParam,PageDetails pageDetails,String TestData) throws Exception
	{

		//Thread.sleep(100);
		String Action="";
		String ActionDescription = element.toString();
		LocalDateTime StartTime=  LocalDateTime.now();
		if((TestData.equalsIgnoreCase("n/a")==false)||(TestData.equalsIgnoreCase("n\\a"))==false||(TestData.equalsIgnoreCase("n\\\\a"))==false)
		{
		try {

			try 
			{
				webkeywords.Instance().FluentWait(_driver, element);
				webkeywords.Instance().WaitElementforelementclickable(_driver, element, 1000);
				scrollIntoViewElement(_driver, element);

				if(!getLocatorFromWebElement(element,_driver,testCaseParam,pageDetails).toString().equals(null)) 
				{ 
					Action = "Click --> "+getLocatorFromWebElement(element,_driver,testCaseParam,pageDetails).toString();
					ActionDescription = "Click --> "+getLocatorFromWebElement(element,_driver,testCaseParam,pageDetails).toString();
				} 
				else

				{
					Action = "Click --> "+getLocatorFromWebElement(element,_driver,testCaseParam,pageDetails).toString();
					ActionDescription = "Click --> "+getLocatorFromWebElement(element,_driver,testCaseParam,pageDetails).toString();

				}

			}
			catch(Exception e) 
			{
				Action = "Click --> "+getLocatorFromWebElement(element,_driver,testCaseParam,pageDetails).toString();
				ActionDescription = "Click --> "+getLocatorFromWebElement(element,_driver,testCaseParam,pageDetails).toString();

			} 



			try
			{		
				Actions _action = new Actions(_driver);
				_action.moveToElement(element).build().perform();

				element.click();


				TestStepDetails.logTestStepDetails(_driver, testCaseParam, Action, ActionDescription,pageDetails, StartTime, Status_Done);
				Thread.sleep(900);

			}
			catch (Exception e)
			{

				logger.error("Failed ==> " + Action + ActionDescription);
				TestStepDetails.logExceptionDetails(_driver, testCaseParam, Action, ActionDescription, StartTime,e);
				TestStepDetails.logTestStepDetails(_driver, testCaseParam, Action, ActionDescription,pageDetails, StartTime, Status_Fail);

				throw e;


			}

		}
		catch(Exception e)
		{
			throw e;
		}
		}
	}

	public void Wait_ElementtobeVisible(WebDriver _driver, String Xpath, int TimeOut) throws Exception {
		int count = 0;
		int maxTries = 5;
		while (count < maxTries) {
			try {
				WebDriverWait wait = new WebDriverWait(_driver, TimeOut);
				boolean elementpresent = wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Xpath)))
						.isDisplayed();
				if ((elementpresent) == true) {
					break;
				}
			} catch (Exception e) {
				count++;
				if (count == maxTries) {
					break;
				}
			}
		}
	} 




	public void ClickWithoutwait(WebDriver _driver, WebElement element, TestCaseParam testCaseParam,PageDetails pageDetails,String TestData) throws Exception
	{
		String Action="";
		Thread.sleep(100);
		String ActionDescription = element.toString();
		LocalDateTime StartTime=  LocalDateTime.now();
		if((TestData.equalsIgnoreCase("n/a")==false)||(TestData.equalsIgnoreCase("n\\a"))==false||(TestData.equalsIgnoreCase("n\\\\a"))==false)
		{
		try 
		{

			if(!getLocatorFromWebElement(element,_driver,testCaseParam,pageDetails).toString().equals(null)) 
			{ 
				Action = "Click --> "+getLocatorFromWebElement(element,_driver,testCaseParam,pageDetails).toString();
				ActionDescription = "Click --> "+getLocatorFromWebElement(element,_driver,testCaseParam,pageDetails).toString();
			} 
			else

			{
				Action = "Click --> "+getLocatorFromWebElement(element,_driver,testCaseParam,pageDetails).toString();
				ActionDescription = "Click --> "+getLocatorFromWebElement(element,_driver,testCaseParam,pageDetails).toString();

			}

		}
		catch(Exception e) 
		{
			Action = "Click --> "+getLocatorFromWebElement(element,_driver,testCaseParam,pageDetails).toString();
			ActionDescription = "Click --> "+getLocatorFromWebElement(element,_driver,testCaseParam,pageDetails).toString();

		} 




		try
		{		


			JavascriptExecutor executor = (JavascriptExecutor)_driver;
			executor.executeScript("arguments[0].click();", element);

			TestStepDetails.logTestStepDetails(_driver, testCaseParam, Action, ActionDescription,pageDetails, StartTime, Status_Done);
			Thread.sleep(900);

		}
		catch (Exception e)
		{
			try 
			{
				JavascriptExecutor executor = (JavascriptExecutor)_driver;
				executor.executeScript("arguments[0].click();", element);

				TestStepDetails.logTestStepDetails(_driver, testCaseParam, Action, ActionDescription,pageDetails, StartTime, Status_Done);

			}
			catch(Exception f) 
			{
				logger.error("Failed ==> " + Action + ActionDescription);
				TestStepDetails.logExceptionDetails(_driver, testCaseParam, Action, ActionDescription, StartTime,e);
				TestStepDetails.logTestStepDetails(_driver, testCaseParam, Action, ActionDescription,pageDetails, StartTime, Status_Fail);

				throw e;
			}

		}
		}
	}
	public void waitElementToBeVisible(WebDriver _driver, WebElement element, int timeOut) throws Exception {
		int count = 0;
		int maxTries = 5;
		while (count < maxTries) {
			try {
				By locatorvalue = getLocatorvalue(element, _driver);
				WebDriverWait wait = new WebDriverWait(_driver, timeOut);
				wait.until(ExpectedConditions.presenceOfElementLocated(locatorvalue));
				break;
			} catch (Exception e) {
				Thread.sleep(10000);
				count++;
				if (++count == maxTries) {
					throw e;
				}
			}
		}
	}

	public void waitForElementToBeVisible(WebDriver _driver, String ElementID) throws Exception {
		int count = 0;
		int maxTries = 5;
		int timeOut=3000;
		while (count < maxTries) {
			try {
				WebElement element=_driver.findElement(By.xpath(ElementID));
				By locatorvalue = getLocatorvalue(element, _driver);
				WebDriverWait wait = new WebDriverWait(_driver, timeOut);
				wait.until(ExpectedConditions.presenceOfElementLocated(locatorvalue));
				break;
			} catch (Exception e) {
				Thread.sleep(10000);
				count++;
				if (++count == maxTries) {
					throw e;
				}
			}
		}
	}


	public String getLocatorFromWebElement(WebElement element,WebDriver _driver,TestCaseParam testCaseParam,PageDetails pageDetails) throws Exception
	{

		String locatoroutput= element.toString().split("->")[1].replaceFirst("(?s)(.*)\\]", "$1" + "");
		String[] Splitlocator=locatoroutput.split(": ");
		Splitlocator[0].replaceAll("\\s", "");	    
		Splitlocator[1].replaceAll("\\s", "");

		String text=getElementText(_driver, Splitlocator[0],Splitlocator[1],  testCaseParam,pageDetails);

		return text;
	}

	public void Hovermousetoelement(WebDriver _driver,WebElement element,TestCaseParam testCaseParam,PageDetails pageDetails) throws Exception
	{
		try 
		{
			Thread.sleep(1000);
			Actions action = new Actions(_driver);

			action.moveToElement(element).perform();
		}
		catch(Exception e) 
		{
			throw e;
		}
	}
	public void SwitchtoFrame(WebDriver _driver, WebElement element, TestCaseParam testCaseParam,PageDetails pageDetails) throws Exception
	{
		String Action = "Navigate -> ";
		String ActionDescription = "";
		LocalDateTime StartTime=  LocalDateTime.now();
		try
		{

			_driver.switchTo().frame(element);
			TestStepDetails.logTestStepDetails(_driver, testCaseParam, Action, ActionDescription,pageDetails, StartTime, Status_Done);

		}
		catch (Exception e)
		{
			logger.error("Failed ==> " + Action + ActionDescription);
			TestStepDetails.logExceptionDetails(_driver, testCaseParam, Action, ActionDescription, StartTime,e);
			throw e;
		}
	}

	public void switchToWindowByTitle(WebDriver _driver, String title, TestCaseParam testCaseParam,PageDetails pageDetails) throws Exception
	{
		String Action = "Navigate -> ";
		String ActionDescription = "";
		LocalDateTime StartTime=  LocalDateTime.now();
		try
		{
			Thread.sleep(2000);
			Set<String> windows=_driver.getWindowHandles();


			for (String handle: windows)
			{

				String myTitle = _driver.switchTo().window(handle).getTitle();
				// now apply the condition - moving to the window with blank title
				if (myTitle.equals(title))
				{

					_driver.switchTo().window(handle);
					logger.info("switched to Window--> {}",title);
					Thread.sleep(0);
					break;
				}
				else
				{
					try
					{
						_driver.switchTo().window(handle);
						logger.info("switched to main Window");
					}
					catch (Exception e)
					{
						logger.info("Unable to switch to Main Window");
					}
				}





			}
		}
		catch (Exception e)
		{
			logger.error("Failed ==> " + Action + ActionDescription);
			TestStepDetails.logExceptionDetails(_driver, testCaseParam, Action, ActionDescription, StartTime,e);
			throw e;
		}
	}


	public void Refresh(WebDriver _driver, TestCaseParam testCaseParam,PageDetails pageDetails) throws Exception
	{
		String Action = "Refresh the page ";
		String ActionDescription = "";
		LocalDateTime StartTime=  LocalDateTime.now();
		try
		{
			_driver.navigate().refresh();
			TestStepDetails.logTestStepDetails(_driver, testCaseParam, Action, ActionDescription,pageDetails, StartTime, Status_Done);

		}
		catch (Exception e)
		{
			logger.error("Failed ==> " + Action + ActionDescription);
			TestStepDetails.logExceptionDetails(_driver, testCaseParam, Action, ActionDescription, StartTime,e);
			TestStepDetails.logTestStepDetails(_driver, testCaseParam, Action, ActionDescription,pageDetails, StartTime, Status_Fail);

			throw e;
		}
	}

	public boolean FindElementBool(WebDriver _driver, By by, TestCaseParam testCaseParam, int... timeOut)
	{
		if(timeOut==null)
		{
			timeOut[0]=60;
		}

		boolean found = false;
		try
		{
			WaitElementVisible(_driver, by, timeOut[0]);
			found = true;
		}
		catch (Exception e)
		{
			found = false;
		}
		logger.info(by + "found = " + found);
		return found;
	}

	public void ZoombackToOriginal(WebDriver _driver, TestCaseParam testCaseParam,PageDetails pageDetails) throws Exception
	{
		String Action = "Navigate -> ";
		String ActionDescription = "";
		LocalDateTime StartTime=  LocalDateTime.now();
		try
		{


			JavascriptExecutor js = (JavascriptExecutor)_driver;

			js.executeScript("document.body.style.zoom='100%'");

		}
		catch (Exception e)
		{
			logger.error("Failed ==> " + Action + ActionDescription);
			TestStepDetails.logExceptionDetails(_driver, testCaseParam, Action, ActionDescription, StartTime,e);
			throw e;
		}
	}

	public void JSClick(WebDriver _driver, WebElement element, TestCaseParam testCaseParam,PageDetails pageDetails, int... timeOut) throws Exception
	{
		if(timeOut==null)
		{
			timeOut[0]=60;
		}

		String Action = "Navigate -> ";
		String ActionDescription = "";
		LocalDateTime StartTime=  LocalDateTime.now();
		try
		{

			WebDriverWait wait = new WebDriverWait(_driver, timeOut[0]);


			JavascriptExecutor js = (JavascriptExecutor)_driver;
			js.executeScript("arguments[0].click();", element);

		}
		catch (Exception e)
		{
			logger.error("Failed ==> " + Action + ActionDescription);
			TestStepDetails.logExceptionDetails(_driver, testCaseParam, Action, ActionDescription, StartTime,e);
			throw e;
		}
	}


	public void ClickRadioButton(WebDriver _driver, TestCaseParam testCaseParam, WebElement element) throws Exception
	{
		String Action = "Navigate -> ";
		String ActionDescription = "";
		LocalDateTime StartTime=  LocalDateTime.now();
		try
		{

			/*							WebDriverWait wait = new WebDriverWait(_driver, TimeSpan.FromSeconds(15));
		wait.until(ExpectedConditions.elementToBeClickable(element));
			 */
			for (int i = 0; i < 10; i++)
			{
				element.click();

				if (element.isSelected())
				{
					break;
				}


			}

		}
		catch (Exception e)
		{
			logger.error("Failed ==> " + Action + ActionDescription);
			TestStepDetails.logExceptionDetails(_driver, testCaseParam, Action, ActionDescription, StartTime,e);
			throw e;
		}
	}

	/// <summary>
	/// This method user for 
	/// enter text 
	/// </summary>
	/// <param name="element"></param>
	/// <param name="text"></param>
	public void SetText(WebDriver _driver, WebElement element, String text, TestCaseParam testCaseParam,PageDetails pageDetails) throws Exception
	{
		String Action = "Entered Text -> "+ text;
		String ActionDescription = "Entered Text -> "+ text;;

		LocalDateTime StartTime=  LocalDateTime.now();

		try
		{

			if(text.equalsIgnoreCase("N/A")|| text.equalsIgnoreCase("N//A"))
			{

			}

			else
			{
				webkeywords.Instance().FluentWait(_driver, element);
				WebDriverWait wait = new WebDriverWait(_driver, 1000);
				wait.until(ExpectedConditions.elementToBeClickable(element));				
				element.clear();
				element.sendKeys(text);
				boolean isDataFilled = false;
				while (!isDataFilled) {
					
					
					String textBoxValue=element.getAttribute("value");
					String textBoxtest=element.getText();
					 if (textBoxValue.matches("^[a-zA-Z0-9]+$")||textBoxtest.matches("^[a-zA-Z0-9]+$")) 
					 {
						 isDataFilled = true;
					 }
				        // Check if the value is alphabetical
				        else if (textBoxValue.matches("^[a-zA-Z]+$")||textBoxtest.matches("^[a-zA-Z]+$")) 
				        {
				        	isDataFilled = true;
				        }
				        else if (textBoxValue.matches("^[a-z A-Z]+$")||textBoxtest.matches("^[a-z A-Z]+$")) 
				        {
				        	isDataFilled = true;
				        }
				        // Check if the value is numeric
				        else if (textBoxValue.matches("^[0-9]+$")||textBoxtest.matches("^[0-9]+$")) 
				        {
				        	isDataFilled = true;
				        }
				        else if (textBoxValue.matches("^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[@#$%^&+=/-_]).+$")||textBoxtest.matches("^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[@#$%^&+=/_-]).+$")) {
				        	isDataFilled = true;				        }
				        // Check if the value is a combination of numeric and special characters
				        else if (textBoxValue.matches("^(?=.*[0-9])(?=.*[@#$%^&+=/-_]).+$")||textBoxtest.matches("^(?=.*[0-9])(?=.*[@#$%^&+=/-_]).+$")) {
				        	isDataFilled = true;				        }
				        // Check if the value is a combination of alphabet and special characters
				        else if (textBoxValue.matches("^(?=.*[a-zA-Z])(?=.*[@#$%^&+=/-_]).+$")||textBoxtest.matches("^(?=.*[a-zA-Z])(?=.*[@#$%^&+=/-_]).+$")) {
				        	isDataFilled = true;
				        	}
					 
				        else if (textBoxValue.matches("^(?=.*[@#$!%^&+=-]).+$")||textBoxtest.matches("^(?=.*[@#$!%^&+=-]).+$")) {
				        	isDataFilled = true;
				        	}
				       

		            else 
		            {
		            	
		               logger.info("Textbox is empty. Retrying...");
		                element.clear();

						
						JavascriptExecutor jsExecutor = (JavascriptExecutor) _driver;
			            jsExecutor.executeScript("arguments[0].value = arguments[1]", element, text);
		            }
		        }

				LocalDateTime endTime =  LocalDateTime.now();

				logger.info("Successfully Entered Text" + text + "to " + element);

				TestStepDetails.logTestStepDetails(_driver, testCaseParam, Action, ActionDescription,pageDetails, StartTime, Status_Done);

				VerifyValueEntered(_driver, element,text,testCaseParam,pageDetails);
				Thread.sleep(0);
			}
		}
		catch (Exception e)
		{
			logger.error("Failed ==> " + Action + ActionDescription);
			TestStepDetails.logExceptionDetails(_driver, testCaseParam, Action, ActionDescription, StartTime,e);
			TestStepDetails.logTestStepDetails(_driver, testCaseParam, Action, ActionDescription,pageDetails, StartTime, Status_Fail);

			throw e;
		}

	}
	
	public void SetText_MultipleRetry(WebDriver driver, WebElement element, String text, TestCaseParam testCaseParam,PageDetails pageDetails) throws Exception
	{
		String Action = "Entered Text -> "+ text;
		String ActionDescription = "Entered Text -> "+ text;;

		LocalDateTime startTime=  LocalDateTime.now();

		try
		{

			if(text.equalsIgnoreCase("N/A")|| text.equalsIgnoreCase("N//A"))
			{

			}

			
			else
			{
				webkeywords.Instance().FluentWait(driver, element);
				WebDriverWait wait = new WebDriverWait(driver, 1000);
				wait.until(ExpectedConditions.elementToBeClickable(element));				
				element.clear();
				element.sendKeys(text);
				
				boolean isDataFilled = false;
				while (!isDataFilled) {
					
					
					String textBoxValue=element.getAttribute("value");
					String textBoxtest=element.getText();
					 if (textBoxValue.matches("^[a-zA-Z0-9]+$")||textBoxtest.matches("^[a-zA-Z0-9]+$")) 
					 {
						 isDataFilled = true;
					 }
				        // Check if the value is alphabetical
				        else if (textBoxValue.matches("^[a-zA-Z]+$")||textBoxtest.matches("^[a-zA-Z]+$")) 
				        {
				        	isDataFilled = true;
				        }
				        // Check if the value is numeric
				        else if (textBoxValue.matches("^[0-9]+$")||textBoxtest.matches("^[0-9]+$")) 
				        {
				        	isDataFilled = true;
				        }
				        else if (textBoxValue.matches("^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[@#$%^&+=/-_]).+$")||textBoxtest.matches("^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[@#$%^&+=/_-]).+$")) {
				        	isDataFilled = true;				        }
				        // Check if the value is a combination of numeric and special characters
				        else if (textBoxValue.matches("^(?=.*[0-9])(?=.*[@#$%^&+=/-_]).+$")||textBoxtest.matches("^(?=.*[0-9])(?=.*[@#$%^&+=/-_]).+$")) {
				        	isDataFilled = true;				        }
				        // Check if the value is a combination of alphabet and special characters
				        else if (textBoxValue.matches("^(?=.*[a-zA-Z])(?=.*[@#$%^&+=/-_]).+$")||textBoxtest.matches("^(?=.*[a-zA-Z])(?=.*[@#$%^&+=/-_]).+$")) {
				        	isDataFilled = true;
				        	}
				       

		            else 
		            {
		            	
		               logger.info("Textbox is empty. Retrying...");
		                element.clear();

						
						JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
			            jsExecutor.executeScript("arguments[0].value = arguments[1]", element, text);
		            }
		        }

				logger.info("Successfully Entered Text" + text + "to " + element);
				TestStepDetails.logTestStepDetails(driver, testCaseParam, Action, ActionDescription,pageDetails, startTime, Status_Done);
				VerifyValueEntered(driver, element,text,testCaseParam,pageDetails);
				Thread.sleep(0);
			}
		}
		catch (Exception e)
		{
			logger.error("Failed ==> " + Action + ActionDescription);
			TestStepDetails.logExceptionDetails(driver, testCaseParam, Action, ActionDescription, startTime,e);
			TestStepDetails.logTestStepDetails(driver, testCaseParam, Action, ActionDescription,pageDetails, startTime, Status_Fail);

			throw e;
		}

	}

	public void SetTextwithoutverification(WebDriver _driver, WebElement element, String text, TestCaseParam testCaseParam,PageDetails pageDetails) throws Exception
	{
		String Action = "Entered Text -> "+ text;
		String ActionDescription = "Entered Text -> "+ text;;

		LocalDateTime StartTime=  LocalDateTime.now();

		try
		{

			if(text.equalsIgnoreCase("N/A")|| text.equalsIgnoreCase("N//A"))
			{

			}

			else
			{
				webkeywords.Instance().FluentWait(_driver, element);
				WebDriverWait wait = new WebDriverWait(_driver, 1000);
				wait.until(ExpectedConditions.elementToBeClickable(element));				
				element.clear();
				element.sendKeys(text);
				
				boolean isDataFilled = false;
				while (!isDataFilled) {
					
					
					String textBoxValue=element.getAttribute("value");
					String textBoxtest=element.getText();
					 if (textBoxValue.matches("^[a-zA-Z0-9]+$")||textBoxtest.matches("^[a-zA-Z0-9]+$")) 
					 {
						 isDataFilled = true;
					 }
				        // Check if the value is alphabetical
				        else if (textBoxValue.matches("^[a-zA-Z]+$")||textBoxtest.matches("^[a-zA-Z]+$")) 
				        {
				        	isDataFilled = true;
				        }
				        // Check if the value is numeric
				        else if (textBoxValue.matches("^[0-9]+$")||textBoxtest.matches("^[0-9]+$")) 
				        {
				        	isDataFilled = true;
				        }
				        else if (textBoxValue.matches("^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[@#$%^&+=/-_]).+$")||textBoxtest.matches("^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[@#$%^&+=/_-]).+$")) {
				        	isDataFilled = true;				        }
				        // Check if the value is a combination of numeric and special characters
				        else if (textBoxValue.matches("^(?=.*[0-9])(?=.*[@#$%^&+=/-_]).+$")||textBoxtest.matches("^(?=.*[0-9])(?=.*[@#$%^&+=/-_]).+$")) {
				        	isDataFilled = true;				        }
				        // Check if the value is a combination of alphabet and special characters
				        else if (textBoxValue.matches("^(?=.*[a-zA-Z])(?=.*[@#$%^&+=/-_]).+$")||textBoxtest.matches("^(?=.*[a-zA-Z])(?=.*[@#$%^&+=/-_]).+$")) {
				        	isDataFilled = true;
				        	}
				        
				       

		            else 
		            {
		            	
		                logger.info("Textbox is empty. Retrying...");
		                element.clear();
						//element.sendKeys(text);
						
						JavascriptExecutor jsExecutor = (JavascriptExecutor) _driver;
			            jsExecutor.executeScript("arguments[0].value = arguments[1]", element, text);
		            }
		        }



				LocalDateTime EndTime =  LocalDateTime.now();

				logger.info("Successfully Entered Text" + text + "to " + element);

				TestStepDetails.logTestStepDetails(_driver, testCaseParam, Action, ActionDescription,pageDetails, StartTime, Status_Done);

				Thread.sleep(0);
			}
		}
		catch (Exception e)
		{
			logger.error("Failed ==> " + Action + ActionDescription);
			TestStepDetails.logExceptionDetails(_driver, testCaseParam, Action, ActionDescription, StartTime,e);
			TestStepDetails.logTestStepDetails(_driver, testCaseParam, Action, ActionDescription,pageDetails, StartTime, Status_Fail);

			throw e;
		}

	}


	public void JSSetText(WebDriver _driver, WebElement element, String text, TestCaseParam testCaseParam,PageDetails pageDetails) throws Exception
	{
		String Action = "Navigate -> ";
		String ActionDescription = "";
		LocalDateTime StartTime=  LocalDateTime.now();


		try
		{
			waitweb_ElementVisible(_driver, element,200);

			 JavascriptExecutor js = (JavascriptExecutor)_driver;

			 js.executeScript("arguments[0].setAttribute('value','" + text + "');", element);
		}
		catch (WebDriverException e)
		{
			throw new Exception("Element is not enable for set text" + "\r\n" + "error: " + e.getMessage());
		}

		catch (Exception e)
		{
			throw new Exception("Element is not enable for set text" + "\r\n" + "error: " + e.getMessage() + e.getStackTrace());
		}

	}

	public String JSGetText(WebDriver _driver, WebElement element, TestCaseParam testCaseParam,PageDetails pageDetails) throws Exception
	{
		String Action = "Navigate -> ";
		String ActionDescription = "";
		LocalDateTime StartTime=  LocalDateTime.now();
		String webText = "";

		try
		{
			webText = element.getText().trim();

		}
		catch (WebDriverException e)
		{
			throw new Exception("Element is not enable for get text" + "\r\n" + "error: " + e.getMessage());
		}

		catch (Exception e)
		{
			throw new Exception("Element is not enable for get text" + "\r\n" + "error: " + e.getMessage() +  e.getStackTrace());
		}
		return webText;

	}

	public void Submit(WebDriver _driver, WebElement element, TestCaseParam testCaseParam,PageDetails pageDetails) throws Exception
	{
		String Action = "Navigate -> ";
		String ActionDescription = "";
		LocalDateTime StartTime=  LocalDateTime.now();
		try
		{
			waitweb_ElementVisible(_driver, element,200);
			element.submit();
			LocalDateTime EndTime =  LocalDateTime.now();
			logger.info("Successfully Clicked Button ==>" + element);
		}
		catch (Exception e)
		{
			logger.error("Failed ==> " + Action + ActionDescription);
			TestStepDetails.logExceptionDetails(_driver, testCaseParam, Action, ActionDescription, StartTime,e);
			throw e;
		}

	}

	/// <summary>
	/// This method user for 
	/// enter text 
	/// </summary>
	/// <param name="element"></param>
	/// <param name="text"></param>
	public void SetDate(WebDriver _driver, WebElement element, String text, TestCaseParam testCaseParam,PageDetails pageDetails) throws Exception
	{

		String Action = "Navigate -> ";
		String ActionDescription = "";
		LocalDateTime StartTime=  LocalDateTime.now();
		try


		{
			String ID = element.getAttribute("ID");
			((JavascriptExecutor)_driver).executeScript("document.getElementById('" + ID + "').removeAttribute('readonly',0);");


			element.click();

			((JavascriptExecutor)_driver).executeScript("document.getElementById('" + ID + "').setAttribute('value', '" + text + "')");

			_driver.findElement(By.xpath("//div[@id=\"fare_" + text + "\"]")).click();


		}
		catch (Exception e)
		{
			logger.error("Failed ==> " + Action + ActionDescription);
			TestStepDetails.logExceptionDetails(_driver, testCaseParam, Action, ActionDescription, StartTime,e);
			throw e;
		}

	}


	public void WaitElementforelementclickable(WebDriver _driver,WebElement element, int timeOut) throws Exception
	{

		try
		{	
			By locatorvalue=getLocatorvalue(element,_driver);

			WebDriverWait wait = new WebDriverWait(_driver, timeOut);
			wait.until(ExpectedConditions.elementToBeClickable(locatorvalue));

		}
		catch (WebDriverException e)
		{
			try 
			{
				Thread.sleep(1500);
				By locatorvalue=getLocatorvalue(element,_driver);

				WebDriverWait wait = new WebDriverWait(_driver, timeOut);
				wait.until(ExpectedConditions.elementToBeClickable(locatorvalue));
			}
			catch(WebDriverException f) 
			{

			}


		}
	}



	public static void waitForPageLoad(WebDriver driver) {


		//		boolean isLoaded = false;
		//	    
		//	    
		//	        String script = "if (typeof window != 'undefined' && window.document) { return window.document.readyState; } else { return 'notready'; }";
		//	        
		//	        try {
		//	        	
		//	        	isLoaded = ((JavascriptExecutor) driver).executeScript(script).equals("complete");
		//	        	
		//	        } 
		//	        catch (Exception ex) {
		//	        	isLoaded = Boolean.FALSE;
		//	        }
		//	       	    	    
		////	      
		//	    return isLoaded;
	}
	//	public static final ExpectedCondition<Boolean> EXPECT_DOC_READY_STATE = new ExpectedCondition<Boolean>() {
	//	    @Override
	//	    public Boolean apply(WebDriver driver) 
	//	    {
	//	        String script = "if (typeof window != 'undefined' && window.document) { return window.document.readyState; } else { return 'notready'; }";
	//	        Boolean result;
	//	        try {
	//	            result = ((JavascriptExecutor) driver).executeScript(script).equals("complete");
	//	        } catch (Exception ex) {
	//	            result = Boolean.FALSE;
	//	        }
	//	        return result;
	//	    }
	//	};


	public By getLocatorvalue(WebElement element,WebDriver _driver) throws Exception
	{

		String locatoroutput= element.toString().split("->")[1].replaceFirst("(?s)(.*)\\]", "$1" + "");
		String[] Splitlocator=locatoroutput.split(": ");
		Splitlocator[0].replaceAll("\\s", "");	    
		Splitlocator[1].replaceAll("\\s", "");


		By locatorvalue=getlocatorvalueforwait(_driver, Splitlocator[0],Splitlocator[1]);
		return locatorvalue;
	}

	public By getlocatorvalueforwait(WebDriver _driver, String LocatorType,String LocatorValue) throws Exception
	{
		String Action = "Navigate -> ";
		String ActionDescription = "";
		LocalDateTime StartTime=  LocalDateTime.now();
		try
		{
			By locatorvalue = null;
			LocatorType.strip().trim();
			LocatorValue.strip().trim();
			logger.info("Finding Element having ==> LocatorType = " + LocatorType + " => LocatorValue = " + LocatorValue);
			switch (LocatorType.toLowerCase())
			{
			case " id":
				locatorvalue = By.id(LocatorValue.trim());
				break;
			case " name":
				locatorvalue = By.name(LocatorValue.trim());
				break;
			case " xpath":
				locatorvalue = By.xpath(LocatorValue.trim());
				break;
			case " tag":
				locatorvalue = By.name(LocatorValue.trim());
				break;
			case " link text":
				locatorvalue = By.linkText(LocatorValue.trim());
				break;
			case " css":
				locatorvalue = By.cssSelector(LocatorValue.trim());
				break;
			case " class":
				locatorvalue = By.className(LocatorValue.trim());
				break;
			default:
				logger.info("Incorrect Locator Type ==> LocatorType = " + LocatorType);
				throw new Exception("Support FindElement with 'id' 'name' 'xpath' 'tag' 'link' 'css' 'class'");
			}
			return locatorvalue;
		}
		catch (Exception e)
		{
			logger.error("Failed ==> " + Action + ActionDescription);

			throw e;
		}

	}

	/// <summary>
	/// This method use for 
	/// wait element ready to click 
	/// </summary>
	/// <param name="locatorValue"></param>
	/// <param name="timeOut"></param>
	public void WaitElementToBeClickable(WebDriver _driver, By locatorValue, int timeOut)
	{
		try
		{
			WebDriverWait wait = new WebDriverWait(_driver, timeOut);
			wait.until(ExpectedConditions.elementToBeClickable(locatorValue));
		}
		catch (WebDriverException e)
		{
			//throw new OperationCanceledException("Get " + e.getMessage() + ", " + locatorValue + " is not ready for clickable");
		}
	}

	public void Waitforinvisibilityofelement(WebDriver _driver, By locator) throws Exception
	{
		try 
		{
			WebDriverWait wait = new WebDriverWait(_driver, 3000);
			wait.until(ExpectedConditions.invisibilityOfElementLocated(locator));
		}
		catch(Exception e) 
		{

		}

	}

	public String WP_getPageTitle(WebDriver _driver) 
	{
		try {
			String WP_PageTitle="";
			int len=_driver.findElements(By.xpath("//*[@alt='Print' and @tabindex='0' and @class='vertical-align-middle cursor-hand height-20px printDoc']/.././../div/h2/span/label")).size();
			if(len>0) 
			{
				WP_PageTitle=_driver.findElement(By.xpath("//*[@alt='Print' and @tabindex='0' and @class='vertical-align-middle cursor-hand height-20px printDoc']/.././../div/h2/span/label")).getText();
			}
			else 
			{
				WP_PageTitle="";
			}
			return WP_PageTitle;
		}
		catch(Exception e) 
		{
			throw e;
		}
	}




	public void FluentWait(WebDriver _driver, WebElement element) throws Exception
	{

		try
		{																		

			Wait<WebDriver> wait2 = new FluentWait<WebDriver>(_driver)
					.withTimeout(1000, TimeUnit.SECONDS)
					.pollingEvery(5, TimeUnit.SECONDS)
					.ignoring(Exception.class)
					.ignoring(NoSuchElementException.class)
					.ignoring(StaleElementReferenceException.class)
					.ignoring(ElementNotVisibleException.class);

			wait2.until(new Function<WebDriver, WebElement>() 
			{
				public WebElement apply(WebDriver driver) {

					if(element.isDisplayed()==true)
					{
						return element;

					}else
					{
						return null;
					}
				}
			});

		}
		catch (TimeoutException e)
		{

			e.printStackTrace();
		}
	}
	/// <summary>
	/// This method use for 
	/// wait element visible on DOM
	/// </summary>
	/// <param name="locatorValue"></param>
	/// <param name="timeOut"></param>
	public void WaitElementVisible(WebDriver _driver, By locatorValue, int timeOut)
	{


	//	LocalDateTime StartTime=  LocalDateTime.now();

		try
		{
			logger.info("Waiting for Element = " + locatorValue);
			WebDriverWait wait1 = new WebDriverWait(_driver, timeOut);
			wait1.until(ExpectedConditions.visibilityOfElementLocated(locatorValue));

		}
		catch (TimeoutException e)
		{
			logger.error("Get " + e.toString() + ", " + locatorValue + " is not visible");
			//throw new OperationCanceledException("Get " + e.getMessage() + ", " + locatorValue + " is not visible");
		}
	}

	public void WaitElementClickable(WebDriver _driver, WebElement element)
	{
		LocalDateTime StartTime=  LocalDateTime.now();

		try
		{
			logger.info("Waiting for Element = " + element);
			WebDriverWait wait = new WebDriverWait(_driver, 5000);
			wait.until(ExpectedConditions.elementToBeClickable(element));
		}
		catch (Exception e)
		{
			throw e;
		}
	}


	public void WaitElementClickable(WebDriver _driver, WebElement locatorValue, int timeOut)
	{
		LocalDateTime StartTime=  LocalDateTime.now();

		try
		{
			logger.info("Waiting for Element = " + locatorValue);
			WebDriverWait wait = new WebDriverWait(_driver, timeOut);
			wait.until(ExpectedConditions.elementToBeClickable(locatorValue));

		}
		catch (TimeoutException e)
		{
			logger.error("Get " + e.toString() + ", " + locatorValue + " is not visible");
			//throw new OperationCanceledException("Get " + e.getMessage() + ", " + locatorValue + " is not visible");
		}
	}
	public void WaitElementEnabled(WebDriver _driver, WebElement element, int timeOut) throws Exception
	{
		for (int i = 0; i < timeOut; i++)
		{
			try
			{
				if (element.isEnabled())
				{
					break;
				}



			}
			catch (WebDriverException e)
			{
				throw new Exception("Get " + e.getMessage() + ", " + "Element is not visible");
			}
		}
	}

	public void SwitchFocusToOtherWindow(WebDriver _driver, By pagetitlelocator)
	{
		try {
			String currentWindow = _driver.getWindowHandle();

			Set<String> allWindows =  _driver.getWindowHandles();
			for(String curWindow : allWindows){
				_driver.switchTo().window(curWindow);
				if(_driver.findElements(pagetitlelocator).size()>0) 
				{
					String PageTitle=_driver.findElement(pagetitlelocator).getText();
					logger.info(LOG_SWITCHED_TO,PageTitle);
				}
				else 
				{
					String PageTitle=_driver.getTitle();
					logger.info(LOG_SWITCHED_TO,PageTitle);

				}
			}

		}
		catch(Exception e) 
		{
			throw e;
		}
	}

	public void SwitchFocusToMainWindow(WebDriver _driver, By pagetitlelocator)
	{
		try {
			String currentWindow = _driver.getWindowHandle();
			// _driver.switchTo().window(currentWindow);
			String PageTitle=_driver.findElement(pagetitlelocator).getText();
			logger.info(LOG_SWITCHED_TO,PageTitle);
			Set<String> allWindows =  _driver.getWindowHandles();

			for(String curWindow : allWindows){
				_driver.switchTo().window(curWindow);
				if(_driver.findElements(pagetitlelocator).size()>0) 
				{
					PageTitle=_driver.findElement(pagetitlelocator).getText();
					logger.info(LOG_SWITCHED_TO,PageTitle);
					break;
				}
				else 
				{
					PageTitle=_driver.getTitle();
					logger.info(LOG_SWITCHED_TO,PageTitle);

				}
			}
		}
		catch(Exception e) 
		{
			throw e;
		}
	}
	/// <summary>
	/// This method use for 
	/// wait title of page contain String user want
	/// </summary>
	/// <param name="title"></param>
	/// <param name="timeOut"></param>
	public void WaitTitleContains(WebDriver _driver, String title) throws Exception
	{
		try
		{
			int timeOut=10000;
			WebDriverWait wait = new WebDriverWait(_driver, timeOut);
			wait.until(ExpectedConditions.titleContains(title));
		}
		catch (WebDriverException e)
		{
			throw new Exception("Get " + e.getMessage() + ", [" + title + "] is not displayed in WebPage title [" + _driver.getTitle() + "]");
		}
	}
	/// <summary>
	/// This method use for 
	/// get attribute of element in DOM
	/// </summary>
	/// <param name="element"></param>
	/// <param name="attribute"></param>
	/// <returns></returns>
	public String GetAttribute(WebDriver _driver, WebElement element, String attribute)
	{
		return element.getAttribute(attribute);
	}

	/// <summary>
	/// This method use for 
	/// get attribute of element in DOM
	/// </summary>
	/// <param name="element"></param>
	/// <param name="attribute"></param>
	/// <returns></returns>
	public void VerifyAttributeValue(WebDriver _driver, WebElement element, String attribute,String ExpectedText,TestCaseParam testCaseParam,PageDetails pageDetails) throws Exception
	{

		String Action = "Verify "+element.getAttribute(attribute)+" Attribute value-->"+ExpectedText;
		String ActionDescription = "Verify "+element.getAttribute(attribute)+" Attribute value"+ExpectedText;
		LocalDateTime StartTime = LocalDateTime.now();
		if(element.getAttribute(attribute).equalsIgnoreCase(ExpectedText)) 

		{
			TestStepDetails.logTestStepDetails(_driver, testCaseParam, Action, ActionDescription,pageDetails, StartTime, Status_Done);
		}
		else 
		{
			TestStepDetails.logTestStepDetails(_driver, testCaseParam, Action, ActionDescription,pageDetails, StartTime, Status_Fail);

		}
	}
	/// <summary>
	/// This method use for Driver title of page
	/// </summary>
	/// <returns></returns>
	public boolean GetTitle(WebDriver _driver,String ExpectedTitle, TestCaseParam testCaseParam,PageDetails pageDetails) throws Exception
	{

		String Action = "Navigate -> ";
		String ActionDescription = "";
		LocalDateTime StartTime=  LocalDateTime.now();
		try
		{

			String PageTitle = _driver.getTitle();
			boolean ValidTitle = false;
			if (ExpectedTitle.contains(PageTitle))
			{
				ValidTitle = true;
			}
			else
			{
				ValidTitle = false;
			}
			LocalDateTime EndTime =  LocalDateTime.now();


			logger.info("Successfully Navigated to " + PageTitle);
			return ValidTitle;


		}
		catch (Exception e)
		{
			logger.error("Failed ==> " + Action + ActionDescription);
			TestStepDetails.logExceptionDetails(_driver, testCaseParam, Action, ActionDescription, StartTime,e);
			throw e;
		}
	}
	/// <summary>
	/// This method is use for
	/// return value of css
	/// </summary>
	/// <param name="element"></param>
	/// <param name="value"></param>
	/// <returns></returns>
	public String GetCssValue(WebDriver _driver, WebElement element, String value)
	{
		return element.getCssValue(value);
	}
	/// <summary>
	/// This method is use for
	/// return source code of current page
	/// </summary>
	/// <returns></returns>
	public String GetPageSource(WebDriver _driver)
	{
		return _driver.getPageSource();
	}
	/// <summary>
	/// This method use for 
	/// wait page load completed
	/// </summary>
	/// <param name="driver"></param>
	/// <param name="time"></param>
	public void WaitForPageToLoad(WebDriver _driver, int time)
	{
		/*		TimeSpan timeout = new TimeSpan(0, 0, time);
		WebDriverWait wait = new WebDriverWait(_driver, timeout);
		JavascriptExecutor javascript = _driver as JavascriptExecutor;
		if (javascript == null)
			throw new ArgumentException("driver", "Driver must support javascript execution");
		wait.Until((d) =>
		{
			try
			{
				return javascript.executeScript("return document.readyState").equals("complete");
			}
			catch (InvalidOperationException e)
			{
				//Window is no longer available
				return e.getMessage().ToLower().Contains("unable to Driver browser");
			}
			catch (WebDriverException e)
			{
				//Browser is no longer available
				return e.getMessage().ToLower().Contains("unable to connect");
			}
			catch (Exception)
			{
				return false;
			}
		});
		 */	}
	/// <summary>
	/// This method use for
	/// set attribute of element
	/// </summary>
	/// <param name="element"></param>
	/// <param name="attributeName"></param>
	/// <param name="value"></param>
	public void SetAttribute(WebDriver _driver, WebElement element, String attributeName, String value, TestCaseParam testCaseParam,PageDetails pageDetails) throws Exception
	{
		String Action = "Navigate -> ";
		String ActionDescription = "";
		LocalDateTime StartTime=  LocalDateTime.now();
		try
		{

			WrapsDriver wrappedElement = (WrapsDriver) element;
			if (wrappedElement == null)
				throw new Exception("Element must wrap a web driver");

			_driver = wrappedElement.getWrappedDriver();
			JavascriptExecutor js = (JavascriptExecutor)_driver;
			if (js == null)
				throw new Exception("Element must wrap a web driver that supports javascript execution");
			js.executeScript("arguments[0].setAttribute(arguments[1], arguments[2])", element, attributeName, value);
		}
		catch (Exception e)
		{
			logger.error("Failed ==> " + Action + ActionDescription);
			TestStepDetails.logExceptionDetails(_driver, testCaseParam, Action, ActionDescription, StartTime,e);
			throw e;
		}
	}
	/// <summary>
	/// This method use for 
	/// clear any text on text field
	/// </summary>
	/// <param name="element"></param>
	public void ClearText(WebDriver _driver, WebElement element, TestCaseParam testCaseParam,PageDetails pageDetails) throws Exception
	{
		String Action = "Navigate -> ";
		String ActionDescription = "";
		LocalDateTime StartTime=  LocalDateTime.now();
		try
		{

			element.clear();
		}
		catch (Exception e)
		{
			logger.error("Failed ==> " + Action + ActionDescription);
			TestStepDetails.logExceptionDetails(_driver, testCaseParam, Action, ActionDescription, StartTime,e);
			throw e;
		}
	}
	/// <summary>
	/// This method is use for
	/// Execute javascript
	/// </summary>
	/// <param name="driver"></param>
	/// <returns></returns>
	public JavascriptExecutor JavaScript(WebDriver driver)
	{
		return (JavascriptExecutor)driver;
	}

	/// <summary>
	/// This method is use for
	/// return element
	/// </summary>
	/// <param name="value"></param>
	/// <returns></returns>
	public WebElement FindElement(WebDriver _driver, String value, TestCaseParam testCaseParam,PageDetails pageDetails) throws Exception
	{
		String Action = "Navigate -> ";
		String ActionDescription = "";
		LocalDateTime StartTime=  LocalDateTime.now();
		try
		{
			WebElement element = null;
			String LocatorType = value.split(";")[0];
			String LocatorValue = value.split(";")[1];
			logger.info("Finding Element having ==> LocatorType = " + LocatorType + " => LocatorValue = " + LocatorValue);
			switch (LocatorType.toLowerCase())
			{
			case "id":
				element = _driver.findElement(By.id(LocatorValue));
				break;
			case "name":
				element = _driver.findElement(By.name(LocatorValue));
				break;
			case "xpath":
				element = _driver.findElement(By.xpath(LocatorValue));
				break;
			case "tag":
				element = _driver.findElement(By.name(LocatorValue));
				break;
			case "link":
				element = _driver.findElement(By.linkText(LocatorValue));
				break;
			case "css":
				element = _driver.findElement(By.cssSelector(LocatorValue));
				break;
			case "class":
				element = _driver.findElement(By.className(LocatorValue));
				break;
			default:
				logger.info("Incorrect Locator Type ==> LocatorType = " + LocatorType);
				throw new Exception("Support FindElement with 'id' 'name' 'xpath' 'tag' 'link' 'css' 'class'");
			}
			return element;
		}
		catch (Exception e)
		{
			logger.error("Failed ==> " + Action + ActionDescription);
			TestStepDetails.logExceptionDetails(_driver, testCaseParam, Action, ActionDescription, StartTime,e);

			throw e;
		}

	}

	public String getElementText(WebDriver _driver, String LocatorType,String LocatorValue, TestCaseParam testCaseParam,PageDetails pageDetails) throws Exception
	{
		String Action = "Navigate -> ";
		String ActionDescription = "";
		LocalDateTime StartTime=  LocalDateTime.now();
		try
		{


			String Text=null;
			WebElement element = null;
			LocatorType.trim();
			LocatorValue.trim();
			logger.info("Finding Element having ==> LocatorType = " + LocatorType + " => LocatorValue = " + LocatorValue);
			switch (LocatorType.toLowerCase())
			{
			case " id":
				element = _driver.findElement(By.id(LocatorValue.trim()));
				Text=element.getAttribute("Value");
				if(Text==null) 
				{
					Text=element.getText();
				}
				break;
			case " name":
				element = _driver.findElement(By.name(LocatorValue.trim()));
				Text=element.getAttribute("Value");
				if(Text==null) 
				{
					Text=element.getText();
				}
				break;
			case " xpath":
				element = _driver.findElement(By.xpath(LocatorValue.trim()));
				Text=element.getAttribute("Value");
				if(Text==null) 
				{
					Text=element.getText();
				}
				break;
			case " tag":
				element = _driver.findElement(By.name(LocatorValue.trim()));
				Text=element.getAttribute("Value");
				if(Text==null) 
				{
					Text=element.getText();
				}
				break;
			case " link text":
				element = _driver.findElement(By.linkText(LocatorValue.trim()));
				Text=element.getAttribute("Value");
				if(Text==null) 
				{
					Text=element.getText();
				}
				break;
			case " css":
				element = _driver.findElement(By.cssSelector(LocatorValue.trim()));
				Text=element.getAttribute("Value");
				if(Text==null) 
				{
					Text=element.getText();
				}
				break;
			case " class":
				element = _driver.findElement(By.className(LocatorValue.trim()));
				Text=element.getAttribute("Value");
				if(Text==null) 
				{
					Text=element.getText();
				}
				break;
			default:
				logger.info("Incorrect Locator Type ==> LocatorType = " + LocatorType);
				throw new Exception("Support FindElement with 'id' 'name' 'xpath' 'tag' 'link' 'css' 'class'");
			}
			return Text;
		}
		catch (Exception e)
		{
			logger.error("Failed ==> " + Action + ActionDescription);
			TestStepDetails.logExceptionDetails(_driver, testCaseParam, Action, ActionDescription, StartTime,e);

			throw e;
		}

	}
	/// <summary>
	/// This method is use for
	/// return elements in list
	/// </summary>
	/// <param name="value"></param>
	/// <returns></returns>
	public ArrayList<WebElement> FindElements(WebDriver _driver, String value) throws Exception
	{
		List<WebElement> elements = null;
		String LocatorType = value.split(";")[0];
		String LocatorValue = value.split(";")[1];

		logger.info("Finding Element having ==> LocatorType = " + LocatorType + " => LocatorValue = " + LocatorValue);

		switch (LocatorType.toLowerCase())
		{
		case "id":
			elements = _driver.findElements(By.id(LocatorValue));
			break;
		case "name":
			elements = _driver.findElements(By.name(LocatorValue));
			break;
		case "xpath":
			elements = _driver.findElements(By.xpath(LocatorValue));
			break;
		case "tag":
			elements = _driver.findElements(By.name(LocatorValue));
			break;
		case "link":
			elements = _driver.findElements(By.linkText(LocatorValue));
			break;
		case "css":
			elements = _driver.findElements(By.cssSelector(LocatorValue));
			break;
		case "class":
			elements = _driver.findElements(By.className(LocatorValue));
			break;
		default:
			logger.info("Incorrect Locator Type ==> LocatorType = " + LocatorType);
			throw new Exception("Support FindElement with 'id' 'name' 'xpath' 'tag' 'link' 'css' 'class'");
		}
		return (ArrayList<WebElement>) elements;
	}

	public boolean IsElementVisible(WebElement element)
	{
		return element.isDisplayed();
	}

	public void waitweb_ElementVisible(WebDriver _driver, WebElement locatorValue, int timeOut) throws Exception
	{
	//	LocalDateTime StartTime = LocalDateTime.now();

		try
		{
			logger.info("Waiting for Element = " + locatorValue);
			WebDriverWait wait = new WebDriverWait(_driver, timeOut);
			boolean visible = IsElementVisible(locatorValue);
			while (true)
			{
				if(visible)
				{
					break;
				}
				else
				{

					visible = false;
				}
			}

		}
		catch (Exception e)
		{
			logger.error("Get " + e.toString() + ", " + locatorValue + " is not visible");
			throw new Exception("Get " + e.getMessage() + ", " + locatorValue + " is not visible");
		}
	}

	public void VerifyTextDisplayed(WebDriver driver, WebElement element, String text, TestCaseParam testCaseParam,PageDetails pageDetails, int timeout) throws Exception
	{

		String value = "";
		WebDriverWait wait = new WebDriverWait(driver, timeout);
		waitweb_ElementVisible(driver, element,timeout);

		value = JSGetText(driver, element, testCaseParam,pageDetails);

		if (value.equals(text.trim()))
		{
			logger.info("The value displayed in the application is as expected: ||" + value + "||");
		}
		else
		{
			logger.info("The value displayed in the application: ||" + value + "||, Expected Value: ||" + text + "||. This is not as expected");
		}
	}



	public void VerifyElementPresent(WebDriver driver, WebElement element,TestCaseParam testCaseParam, int timeout) throws Exception
	{
		String TestStepName = "VerifyElementPresent";
		String TestStepDescription = "VerifyElementPresent";
		LocalDateTime StartTime = LocalDateTime.now();
		Boolean isDisplayed = false;

		try
		{
			WebDriverWait wait = new WebDriverWait(driver, timeout);
			waitweb_ElementVisible(driver, element,timeout);
			isDisplayed = IsElementVisible(element);
			logger.info("Element present in application: " + isDisplayed);
			if (isDisplayed)
			{
				logger.info("The Element is displayed in the application");
				//testStepLog.Log("Verifying Element Present", "Element is present on the page: ||" + by + "||", Report.Status.PASS, driver, testCase, ScenarioName, module, currentIteration, browser, node);
			}
			else
			{
				logger.info("The Element is not displayed in the application");
				//testStepLog.Log("Verifying Element Present", "Element is hidden on the page: ||" + by + "||", Report.Status.FAIL, driver, testCase, ScenarioName, module, currentIteration, browser, node);
			}
		}
		catch (Exception e)
		{
			logger.error("Failed ==> " + TestStepDescription);
			exceptionDetails.logExceptionDetails(driver, testCaseParam, TestStepName, TestStepDescription, StartTime);
			throw e;
		}
	}


	public void DocumentUpload(WebDriver _driver, WebElement element,String Path,String DocName, TestCaseParam testCaseParam,PageDetails pageDetails) throws Exception
	{
		String Action = "Upload Document";
		String ActionDescription = "Upload Document";
		LocalDateTime StartTime = LocalDateTime.now();


		try
		{
			webkeywords.Instance().FluentWait(_driver, element);


			element.sendKeys(Path+DocName);

			TestStepDetails.logTestStepDetails(_driver, testCaseParam, Action, ActionDescription,pageDetails, StartTime, Status_Done);


		}
		catch (Exception e)
		{
			logger.error("Failed ==> " + Action + ActionDescription);
			TestStepDetails.logExceptionDetails(_driver, testCaseParam, Action, ActionDescription, StartTime,e);
			throw e;
		}
	}


	//*********************************SCREEN VALIDATIONS**************************************************
	public void VerifyDropdownSelection(WebDriver _driver, WebElement element, String options, TestCaseParam testCaseParam,PageDetails pageDetails) throws Exception
	{
		String Action = "Verify Dropdown-->Actual:"+options+":Expected:-"+element.getText();
		String ActionDescription = "Verify Dropdown-->Actual:"+options+":Expected:-"+element.getText();
		LocalDateTime StartTime = LocalDateTime.now();
		Boolean Valuefound = false;

		try
		{
			if(options.equals("N//A")||options.equals(null)) 
			{

			}
			else
			{

				webkeywords.Instance().FluentWait(_driver, element);
				Select select = new Select(element);
				List<WebElement> allOptions = select.getOptions();
				for(int i=0; i<allOptions.size(); i++) 
				{

					if(allOptions.get(i).getText().contains(options)) 
					{
						Valuefound=true;
						Action = "Verify Dropdown-->Actual:"+options+":Expected:-"+allOptions.get(i).getText();
						ActionDescription = "Verify Dropdown-->Actual:"+element.getText()+":Expected:-"+allOptions.get(i).getText();
						break;
					}
				}
				if(Valuefound) 
				{
					TestStepDetails.logTestStepDetails(_driver, testCaseParam, Action, ActionDescription,pageDetails, StartTime, Status_Done);
				}
				else 
				{
					TestStepDetails.logTestStepDetails(_driver, testCaseParam, Action, ActionDescription,pageDetails, StartTime, Status_Fail);

				}
			}
		}
		catch (Exception e)
		{
			logger.error("Failed ==> " + Action + ActionDescription);
			TestStepDetails.logExceptionDetails(_driver, testCaseParam, Action, ActionDescription, StartTime,e);
			throw e;
		}
	}


	public void VerifyDropdownText(WebDriver _driver, WebElement element, String options, TestCaseParam testCaseParam,PageDetails pageDetails) throws Exception
	{
		String Action = "Verify Dropdown Text-->Actual:"+options+":Expected:-"+element.getText();
		String ActionDescription = "Verify Dropdown Text-->Actual:"+options+":Expected:-"+element.getText();
		LocalDateTime StartTime = LocalDateTime.now();
		Boolean Valuefound = false;

		try
		{
			if(options.equals("N//A")||options.equals(null)) 
			{

			}
			else
			{
				webkeywords.Instance().FluentWait(_driver, element);
				Select select = new Select(element);
				List<WebElement> allOptions =select.getAllSelectedOptions();			
				for(int i=0; i<allOptions.size();i++) 
				{
					if(allOptions.get(i).getText().contains(options)) 
					{
						Valuefound=true;
						Action = "Verify Dropdown-->Actual:"+options+":Expected:-"+allOptions.get(i).getText();
						ActionDescription = "Verify Dropdown-->Actual:"+options+":Expected:-"+allOptions.get(i).getText();
						break;
					}
				}

				if(Valuefound) 
				{
					TestStepDetails.logTestStepDetails(_driver, testCaseParam, Action, ActionDescription,pageDetails, StartTime, Status_Done);
				}
				else 
				{
					TestStepDetails.logTestStepDetails(_driver, testCaseParam, Action, ActionDescription,pageDetails, StartTime, Status_Fail);

				}
			}
		}
		catch (Exception e)
		{
			logger.error("Failed ==> " + Action + ActionDescription);
			TestStepDetails.logExceptionDetails(_driver, testCaseParam, Action, ActionDescription, StartTime,e);
			throw e;
		}
	}

	public void verifytextentered(WebDriver _driver, WebElement element, String text, TestCaseParam testCaseParam, PageDetails pageDetails) throws Exception
	{
		String Action = "Verify Dropdown Selection-->"+text;
		String ActionDescription = "Verify Dropdown Selection"+text;
		LocalDateTime StartTime = LocalDateTime.now();
		Boolean Verified = false;

		try
		{
			if(text.equals("N//A")||text.equals(null)) 
			{

			}
			else
			{
				webkeywords.Instance().FluentWait(_driver, element);
				String EnteredText = element.getText();

				if(EnteredText.equals(text)) 
				{
					Verified=true;
				}

				if(Verified)
				{
					TestStepDetails.logTestStepDetails(_driver, testCaseParam, Action, ActionDescription,pageDetails, StartTime, Status_Pass);
				}
				else 
				{
					TestStepDetails.logTestStepDetails(_driver, testCaseParam, Action, ActionDescription,pageDetails, StartTime, Status_Fail);
				}
			}



		}
		catch (Exception e)
		{
			logger.error("Failed ==> " + Action + ActionDescription);
			TestStepDetails.logExceptionDetails(_driver, testCaseParam, Action, ActionDescription, StartTime,e);
			throw e;
		}
	}

	public void VerifyElementDisplayed(WebDriver _driver, WebElement element,String testdata, TestCaseParam testCaseParam,PageDetails pageDetails) throws Exception
	{
		String action_v1 = "Verify Element Displayed--"+element.getAttribute("Value");
		String actionDescription = "Verify Element Displayed--"+element.getAttribute("Value");
		LocalDateTime startTime = LocalDateTime.now();

		if((testdata.equalsIgnoreCase("n/a")==false)||(testdata.equalsIgnoreCase("n\\a"))==false||(testdata.equalsIgnoreCase("n\\\\a"))==false)
		{

		try
		{
			scrollIntoViewElement(_driver, element);
			Actions action = new Actions(_driver);
			action.moveToElement(element).perform();
			if(element.isDisplayed()==true) 
			{
				TestStepDetails.logTestStepDetails(_driver, testCaseParam, action_v1, actionDescription,pageDetails, startTime, "Pass");
			}
			else 
			{
				TestStepDetails.logTestStepDetails(_driver, testCaseParam, action_v1, actionDescription,pageDetails, startTime, Status_Fail);

			}

		}
		catch (Exception e)
		{
			logger.error("Failed ==> " + action_v1 + actionDescription);
			TestStepDetails.logExceptionDetails(_driver, testCaseParam, action_v1, actionDescription, startTime,e);
			throw e;
		}
		}
	}
	
	public void VerifyElementDisplayed_textattribute(WebDriver _driver, WebElement element,String TestData, TestCaseParam testCaseParam,PageDetails pageDetails) throws Exception
	{
		String Action = "Verify Element Displayed--"+element.getText();
		String ActionDescription = "Verify Element Displayed--"+element.getText();
		LocalDateTime StartTime = LocalDateTime.now();

		if((TestData.equalsIgnoreCase("n/a")==false)||(TestData.equalsIgnoreCase("n\\a"))==false||(TestData.equalsIgnoreCase("n\\\\a"))==false)
		{

		try
		{
			Actions action = new Actions(_driver);
			action.moveToElement(element).perform();
			if(element.isDisplayed()==true) 
			{
				TestStepDetails.logTestStepDetails(_driver, testCaseParam, Action, ActionDescription,pageDetails, StartTime, "Pass");
			}
			else 
			{
				TestStepDetails.logTestStepDetails(_driver, testCaseParam, Action, ActionDescription,pageDetails, StartTime, Status_Fail);

			}

		}
		catch (Exception e)
		{
			logger.error("Failed ==> " + Action + ActionDescription);
			TestStepDetails.logExceptionDetails(_driver, testCaseParam, Action, ActionDescription, StartTime,e);
			throw e;
		}
		}
	}

	public void verifyelementnotdisplayed(WebDriver _driver, List<WebElement> element, String TestData, TestCaseParam testCaseParam, PageDetails pageDetails) throws Exception
	{
		String Action = "Verify Element Not Displayed--"+ TestData;
		String ActionDescription = "Verify Element Not Displayed--"+ TestData;
		LocalDateTime StartTime = LocalDateTime.now();

		if((TestData.equalsIgnoreCase("n/a")==false)||(TestData.equalsIgnoreCase("n\\a"))==false||(TestData.equalsIgnoreCase("n\\\\a"))==false)
		{


		try
		{

			if(element.size()>0) 
			{
				TestStepDetails.logTestStepDetails(_driver, testCaseParam, Action, ActionDescription,pageDetails, StartTime, Status_Fail);
			}
			else 
			{
				TestStepDetails.logTestStepDetails(_driver, testCaseParam, Action, ActionDescription,pageDetails, StartTime, Status_Pass);
			}

		}
		catch (Exception e)
		{
			logger.error("Failed ==> " + Action + ActionDescription);
			TestStepDetails.logExceptionDetails(_driver, testCaseParam, Action, ActionDescription, StartTime,e);
			throw e;
		}
		}
	}
	
	
	public void verifyelementnotdisplayed(WebDriver _driver, String xpath, String TestData, TestCaseParam testCaseParam, PageDetails pageDetails) throws Exception
	{
		String Action = "Verify Element Not Displayed--"+ TestData;
		String ActionDescription = "Verify Element Not Displayed--"+ TestData;
		LocalDateTime StartTime = LocalDateTime.now();

		if((TestData.equalsIgnoreCase("n/a")==false)||(TestData.equalsIgnoreCase("n\\a"))==false||(TestData.equalsIgnoreCase("n\\\\a"))==false)
		{

		try
		{

			int size=_driver.findElements(By.xpath(xpath)).size();
			if(size>0) 
			{
				TestStepDetails.logTestStepDetails(_driver, testCaseParam, Action, ActionDescription,pageDetails, StartTime, Status_Fail);
			}
			else 
			{
				TestStepDetails.logTestStepDetails(_driver, testCaseParam, Action, ActionDescription,pageDetails, StartTime, Status_Pass);
			}

		}
		catch (Exception e)
		{
			logger.error("Failed ==> " + Action + ActionDescription);
			TestStepDetails.logExceptionDetails(_driver, testCaseParam, Action, ActionDescription, StartTime,e);
			throw e;
		}
		}
	}




	public void VerifyElementEnabled(WebDriver _driver, WebElement element,String TestData, TestCaseParam testCaseParam,PageDetails pageDetails) throws Exception
	{
		String Action = "Verify Element Enabled";
		String ActionDescription = "Verify Element Enabled";
		LocalDateTime StartTime = LocalDateTime.now();

				if (!TestData.equalsIgnoreCase("n/a") || !TestData.equalsIgnoreCase("n\\a") || !TestData.equalsIgnoreCase("n\\\\a"))

			{

		try
		{
			Actions action=new Actions(_driver);
			action.moveToElement(element);

			if(element.isEnabled())
			{
				TestStepDetails.logTestStepDetails(_driver, testCaseParam, Action, ActionDescription,pageDetails, StartTime, "Pass");
			}
			else 
			{
				TestStepDetails.logTestStepDetails(_driver, testCaseParam, Action, ActionDescription,pageDetails, StartTime, Status_Fail);

			}

		}
		catch (Exception e)
		{
			logger.error("Failed ==> " + Action + ActionDescription);
			TestStepDetails.logExceptionDetails(_driver, testCaseParam, Action, ActionDescription, StartTime,e);
			throw e;
		}
		}
	}

	public void VerifyElementSelected(WebDriver _driver, WebElement element,String TestData, TestCaseParam testCaseParam,PageDetails pageDetails) throws Exception
	{
		String action = "Verify Element Selected";
		String actionDescription = "Verify Element Selected";
		LocalDateTime startTime = LocalDateTime.now();

		if((TestData.equalsIgnoreCase("n/a")==false)||(TestData.equalsIgnoreCase("n\\a"))==false||(TestData.equalsIgnoreCase("n\\\\a"))==false)
		{


		try
		{

			if(element.isSelected()==true) 
			{
				TestStepDetails.logTestStepDetails(_driver, testCaseParam, action, actionDescription,pageDetails, startTime, "Pass");
			}
			else 
			{
				TestStepDetails.logTestStepDetails(_driver, testCaseParam, action, actionDescription,pageDetails, startTime, Status_Fail);

			}

		}
		catch (Exception e)
		{
			logger.error("Failed ==> " + action + actionDescription);
			TestStepDetails.logExceptionDetails(_driver, testCaseParam, action, actionDescription, startTime,e);
			throw e;
		}
		}
	}

	public void VerifyElementDisabled(WebDriver _driver, WebElement element,String TestData, TestCaseParam testCaseParam,PageDetails pageDetails) throws Exception
	{
		String Action = "Verify Element Disabled";
		String ActionDescription = "Verify Element Disabled";
		LocalDateTime StartTime = LocalDateTime.now();
		if((TestData.equalsIgnoreCase("n/a")==false)||(TestData.equalsIgnoreCase("n\\a"))==false||(TestData.equalsIgnoreCase("n\\\\a"))==false)
		{


		try
		{


			if(!element.getAttribute("disabled").equals("true")) 
			{
				TestStepDetails.logTestStepDetails(_driver, testCaseParam, Action, ActionDescription,pageDetails, StartTime, "Pass");
			}
			else 
			{
				TestStepDetails.logTestStepDetails(_driver, testCaseParam, Action, ActionDescription,pageDetails, StartTime, Status_Fail);

			}

		}
		catch (Exception e)
		{
			logger.error("Failed ==> " + Action + ActionDescription);
			TestStepDetails.logExceptionDetails(_driver, testCaseParam, Action, ActionDescription, StartTime,e);
			throw e;
		}
		}
	}

	public void VerifyCheckBoxChecked(WebDriver _driver, WebElement element,String TestData, TestCaseParam testCaseParam,PageDetails pageDetails) throws Exception
	{
		String Action = "Verify CheckBox Checked";
		String ActionDescription = "Verify CheckBox Checked";
		LocalDateTime StartTime = LocalDateTime.now();

		if((TestData.equalsIgnoreCase("n/a")==false)||(TestData.equalsIgnoreCase("n\\a"))==false||(TestData.equalsIgnoreCase("n\\\\a"))==false)
		{

		try
		{

			if(element.getAttribute("checked").equals("true")) 
			{
				TestStepDetails.logTestStepDetails(_driver, testCaseParam, Action, ActionDescription,pageDetails, StartTime, "Pass");
			}
			else 
			{
				TestStepDetails.logTestStepDetails(_driver, testCaseParam, Action, ActionDescription,pageDetails, StartTime, Status_Fail);

			}

		}
		catch (Exception e)
		{
			logger.error("Failed ==> " + Action + ActionDescription);
			TestStepDetails.logExceptionDetails(_driver, testCaseParam, Action, ActionDescription, StartTime,e);
			throw e;
		}
		}
	}

	public void VerifyElementTitle(WebDriver _driver, WebElement element,String TestData, TestCaseParam testCaseParam,PageDetails pageDetails) throws Exception
	{
		String Action = "Verify Element Title";
		String ActionDescription = "Verify Element Title";
		LocalDateTime StartTime = LocalDateTime.now();

		if((TestData.equalsIgnoreCase("n/a")==false)||(TestData.equalsIgnoreCase("n\\a"))==false||(TestData.equalsIgnoreCase("n\\\\a"))==false)
		{


		try
		{

			if(element.getAttribute("title").equals(TestData)) 
			{
				TestStepDetails.logTestStepDetails(_driver, testCaseParam, Action, ActionDescription,pageDetails, StartTime, "Pass");
			}
			else 
			{
				TestStepDetails.logTestStepDetails(_driver, testCaseParam, Action, ActionDescription,pageDetails, StartTime, Status_Fail);

			}

		}
		catch (Exception e)
		{
			logger.error("Failed ==> " + Action + ActionDescription);
			TestStepDetails.logExceptionDetails(_driver, testCaseParam, Action, ActionDescription, StartTime,e);
			throw e;
		}
		}
	}

	public void VerifyTextBoxValue(WebDriver _driver, WebElement element,String TestData, TestCaseParam testCaseParam,PageDetails pageDetails) throws Exception
	{
		String Action = "Verify TextBox Value";
		String ActionDescription = "Verify Element Title";
		LocalDateTime StartTime = LocalDateTime.now();

		if((TestData.equalsIgnoreCase("n/a")==false)||(TestData.equalsIgnoreCase("n\\a"))==false||(TestData.equalsIgnoreCase("n\\\\a"))==false)
		{

		try
		{
			webkeywords.Instance().FluentWait(_driver, element);
			if(element.getAttribute("value").equals(TestData)) 
			{
				TestStepDetails.logTestStepDetails(_driver, testCaseParam, Action, ActionDescription,pageDetails, StartTime, "Pass");
			}
			else 
			{
				TestStepDetails.logTestStepDetails(_driver, testCaseParam, Action, ActionDescription,pageDetails, StartTime, Status_Fail);

			}

		}
		catch (Exception e)
		{
			logger.error("Failed ==> " + Action + ActionDescription);
			TestStepDetails.logExceptionDetails(_driver, testCaseParam, Action, ActionDescription, StartTime,e);
			throw e;
		}
		}
	}

	public boolean VerifyTextDisplayed(WebDriver _driver, WebElement element,String testdata, TestCaseParam testCaseParam,PageDetails pageDetails) throws Exception
	{
		String action = "";
		String actionDescription = "";
		LocalDateTime startTime = LocalDateTime.now();

boolean isresult=false;
		
if((testdata.equalsIgnoreCase("n/a")==false)||(testdata.equalsIgnoreCase("n\\a"))==false||(testdata.equalsIgnoreCase("n\\\\a"))==false)
{


		try
		{
			//Text=Text.replaceAll("[^a-zA-Z0-9]", "");

			if(testdata.equals("N//A")||testdata.equals(null))
			{

			}
			else
			{
				scrollIntoViewElement(_driver, element);
				ZoomWebPage(_driver,"50%",testCaseParam,pageDetails);
				action="Actual Text Displayed-->"+element.getText()+"Contains Expected Text:-"+testdata;
				actionDescription = "Actual Text Displayed-->"+element.getText()+"Contains Expected Text:-"+testdata;
				webkeywords.Instance().FluentWait(_driver, element);
				if(testdata.contains("\""))
				{
					testdata=testdata.replaceAll("\"", "");
				}
				if(element.getText().toLowerCase().equalsIgnoreCase(testdata.toLowerCase()))
				{
					TestStepDetails.logVerificationDetails(_driver, testCaseParam, action, actionDescription, startTime, Status_Done, element.getText(), testdata);
					isresult=true;
				}
				else 
				{
					TestStepDetails.logTestStepDetails(_driver, testCaseParam, action, actionDescription,pageDetails, startTime, Status_Fail);

					isresult=false;
				}
			}
			ZoomWebPage(_driver,"100%",testCaseParam,pageDetails);
		}
		catch (Exception e)
		{
			logger.error("Failed ==> " + action + actionDescription);
			TestStepDetails.logExceptionDetails(_driver, testCaseParam, action, actionDescription, startTime,e);
			throw e;
		}
	}
		return isresult;
	}


	
	public boolean VerifyText(WebDriver _driver, WebElement element,String TestData, TestCaseParam testCaseParam,PageDetails pageDetails) throws Exception
	{
		String Action = "";
		String ActionDescription = "";
		LocalDateTime StartTime = LocalDateTime.now();


		try
		{
			//Text=Text.replaceAll("[^a-zA-Z0-9]", "");

			if(TestData.equals("N//A")||TestData.equals(null)) 
			{

			}
			else
			{
				scrollIntoViewElement(_driver, element);
				ZoomWebPage(_driver,"50%",testCaseParam,pageDetails);
				Action="Actual Text Displayed-->"+element.getText()+"Contains Expected Text:-"+TestData;
				ActionDescription = "Actual Text Displayed-->"+element.getText()+"Contains Expected Text:-"+TestData;
				webkeywords.Instance().FluentWait(_driver, element);
				if(TestData.contains("\"")) 
				{
					TestData=TestData.replaceAll("\"", "");
				}
				if(element.getText().toLowerCase().equalsIgnoreCase(TestData.toLowerCase())) 
				{
					TestStepDetails.logVerificationDetails(_driver, testCaseParam, Action, ActionDescription, StartTime, Status_Done, element.getText(), TestData);
					//TestStepDetails.logTestStepDetails(_driver, testCaseParam, Action, ActionDescription,pageDetails, StartTime, Status_Done);
					return true;
				}
				else 
				{
					TestStepDetails.logTestStepDetails(_driver, testCaseParam, Action, ActionDescription,pageDetails, StartTime, Status_Fail);
					return false;
				}
			}
			ZoomWebPage(_driver,"100%",testCaseParam,pageDetails);
		}
		catch (Exception e)
		{
			logger.error("Failed ==> " + Action + ActionDescription);
			TestStepDetails.logExceptionDetails(_driver, testCaseParam, Action, ActionDescription, StartTime,e);
			throw e;
		}
		return false;
		}

	

	public void VerifyTextDisplayed_valueattribute(WebDriver _driver, WebElement element,String TestData, TestCaseParam testCaseParam,PageDetails pageDetails) throws Exception
	{
		String Action = "Actual Text Displayed-->"+element.getText()+"Contains Expected Text:-"+TestData;
		String ActionDescription = "Actual Text Displayed-->"+element.getText()+"Contains Expected Text:-"+TestData;
		LocalDateTime StartTime = LocalDateTime.now();

		if((TestData.equalsIgnoreCase("n/a")==false)||(TestData.equalsIgnoreCase("n\\a"))==false||(TestData.equalsIgnoreCase("n\\\\a"))==false)
		{


		try
		{
			//Text=Text.replaceAll("[^a-zA-Z0-9]", "");

			if(TestData.equals("N//A")||TestData.equals(null)) 
			{

			}
			else
			{
				webkeywords.Instance().FluentWait(_driver, element);
				if(TestData.contains("\"")) 
				{
					TestData=TestData.replaceAll("\"", "");
				}
				if(element.getAttribute("value").toLowerCase().equals(TestData.toLowerCase())) 
				{
					TestStepDetails.logVerificationDetails(_driver, testCaseParam, Action, ActionDescription, StartTime, Status_Done, element.getText(), TestData);
				}
				else 
				{
					TestStepDetails.logTestStepDetails(_driver, testCaseParam, Action, ActionDescription,pageDetails, StartTime, Status_Fail);

				}
			}
		}
		catch (Exception e)
		{
			logger.error("Failed ==> " + Action + ActionDescription);
			TestStepDetails.logExceptionDetails(_driver, testCaseParam, Action, ActionDescription, StartTime,e);
			throw e;
		}
		}
	}

	public void VerifyTextDisplayed_titleattribute(WebDriver _driver, WebElement element,String TestData, TestCaseParam testCaseParam,PageDetails pageDetails) throws Exception
	{
		String Action = "Actual Text Displayed-->"+element.getText()+"Contains Expected Text:-"+TestData;
		String ActionDescription = "Actual Text Displayed-->"+element.getText()+"Contains Expected Text:-"+TestData;
		LocalDateTime StartTime = LocalDateTime.now();

		if((TestData.equalsIgnoreCase("n/a")==false)||(TestData.equalsIgnoreCase("n\\a"))==false||(TestData.equalsIgnoreCase("n\\\\a"))==false)
		{

		try
		{
			//Text=Text.replaceAll("[^a-zA-Z0-9]", "");

			if(TestData.equals("N//A")||TestData.equals(null)) 
			{

			}
			else
			{
				webkeywords.Instance().FluentWait(_driver, element);
				if(TestData.contains("\"")) 
				{
					TestData=TestData.replaceAll("\"", "");
				}
				if(element.getAttribute("title").toLowerCase().equals(TestData.toLowerCase())) 
				{
					TestStepDetails.logVerificationDetails(_driver, testCaseParam, Action, ActionDescription, StartTime, Status_Done, element.getText(), TestData);
					//TestStepDetails.logTestStepDetails(_driver, testCaseParam, Action, ActionDescription,pageDetails, StartTime, Status_Done);
				}
				else 
				{
					TestStepDetails.logTestStepDetails(_driver, testCaseParam, Action, ActionDescription,pageDetails, StartTime, Status_Fail);

				}
			}
		}
		catch (Exception e)
		{
			logger.error("Failed ==> " + Action + ActionDescription);
			TestStepDetails.logExceptionDetails(_driver, testCaseParam, Action, ActionDescription, StartTime,e);
			throw e;
		}
		}
		}



	public void VerifyTextDisplayed(WebDriver _driver, WebElement element,ArrayList<String> Text, TestCaseParam testCaseParam,PageDetails pageDetails) throws Exception
	{
		String Action = "Actual Text Displayed-->"+element.getText()+"Contains Expected Text:-"+Text;
		String ActionDescription = "Actual Text Displayed-->"+element.getText()+"Contains Expected Text:-"+Text;
		LocalDateTime StartTime = LocalDateTime.now();


		try
		{
			if(Text.equals("N//A")||Text.equals(null)) 
			{

			}
			else
			{
				webkeywords.Instance().FluentWait(_driver, element);
				if(element.getText().contains(Text.get(0))) 
				{
					TestStepDetails.logTestStepDetails(_driver, testCaseParam, Action, ActionDescription,pageDetails, StartTime, Status_Done);
				}
				else 
				{
					TestStepDetails.logTestStepDetails(_driver, testCaseParam, Action, ActionDescription,pageDetails, StartTime, Status_Fail);

				}
			}
		}
		catch (Exception e)
		{
			logger.error("Failed ==> " + Action + ActionDescription);
			TestStepDetails.logExceptionDetails(_driver, testCaseParam, Action, ActionDescription, StartTime,e);
			throw e;
		}
	}

	public boolean VerifyTableData(WebDriver _driver, ArrayList<WebElement> element,String Text, TestCaseParam testCaseParam,PageDetails pageDetails) throws Exception
	{
		String Action = "Table Columns Text Displayed-->";
		String ActionDescription = "Table Columns Text Displayed-->";
		LocalDateTime StartTime = LocalDateTime.now();
		boolean verifydata=false;

		try
		{
			if(Text.equals("N//A")||Text.equals(null)) 
			{

			}
			else
			{

				ArrayList<WebElement> Element=new ArrayList<WebElement>();
				Element=element;
				ArrayList<String> DataList = new ArrayList<String>();

				String[] dataCount = new String[element.size()];
				int l = 0;
				for (WebElement data : Element)
				{
					Action = "Actual Text Displayed-->"+data.getText()+"Expected:-"+Text;
					ActionDescription = "Actual Text Displayed-->"+data.getText()+"Expected:-"+Text;
					dataCount[l++] = data.getText();
					DataList.add(data.getText().toString());

					if(data.getText().contains(Text)) 
					{
						TestStepDetails.logTestStepDetails(_driver, testCaseParam, Action,ActionDescription,pageDetails, StartTime, Status_Pass);
						verifydata= true;
					} 
					else 
					{
						TestStepDetails.logTestStepDetails(_driver, testCaseParam, Action,ActionDescription,pageDetails, StartTime, Status_Fail);
						verifydata= false;
					}

				}
			}
			/*
			 * if (DataList.stream().anyMatch(s -> s.equals(ExpectedValue))) {
			 * TestStepDetails.logTestStepDetails(_driver, testCaseParam, Action,
			 * ActionDescription, StartTime, Status_Pass); } else {
			 * TestStepDetails.logTestStepDetails(_driver, testCaseParam, Action,
			 * ActionDescription, StartTime, Status_Fail);
			 * 
			 * }
			 */


			return 	verifydata;	


		}
		catch (Exception e)
		{
			logger.error("Failed ==> " + Action + ActionDescription);
			TestStepDetails.logExceptionDetails(_driver, testCaseParam, Action, ActionDescription, StartTime,e);
			return false;
		}
	}

	public void VerifyTableDataifNull(WebDriver _driver, ArrayList<WebElement> element, TestCaseParam testCaseParam,PageDetails pageDetails) throws Exception
	{
		String Action = "Table Columns Text Displayed-->";
		String ActionDescription = "Table Columns Text Displayed-->";
		LocalDateTime StartTime = LocalDateTime.now();


		try
		{
			ArrayList<WebElement> Element=new ArrayList<WebElement>();
			Element=element;
			ArrayList<String> DataList = new ArrayList<String>();

			String[] dataCount = new String[element.size()];
			int l = 0;
			for (WebElement data : Element)
			{
				Action = "Table Columns Text Displayed-->"+data.getText();
				ActionDescription = "Table Columns Text Displayed-->"+data.getText();
				dataCount[l++] = data.getText();
				DataList.add(data.getText().toString());
				if(!data.getText().equals(null)) 
				{
					TestStepDetails.logTestStepDetails(_driver, testCaseParam, Action, ActionDescription,pageDetails, StartTime, Status_Done);
				}
				else 
				{
					TestStepDetails.logTestStepDetails(_driver, testCaseParam, Action, ActionDescription,pageDetails, StartTime, Status_Fail);

				}
			}





		}
		catch (Exception e)
		{
			logger.error("Failed ==> " + Action + ActionDescription);
			TestStepDetails.logExceptionDetails(_driver, testCaseParam, Action, ActionDescription, StartTime,e);
			throw e;
		}
	}

	public void VerifyPartialTextDisplayed(WebDriver _driver, WebElement element,String text, TestCaseParam testCaseParam,PageDetails pageDetails) throws Exception
	{
		String Action = "Verify Element Partial Text";
		String ActionDescription = "Verify Element Partial Text";
		LocalDateTime StartTime = LocalDateTime.now();


		try
		{

			if(element.getText().toLowerCase().contains(text.toLowerCase())) 
			{
				TestStepDetails.logTestStepDetails(_driver, testCaseParam, Action, ActionDescription,pageDetails, StartTime, Status_Done);
			}
			else 
			{
				TestStepDetails.logTestStepDetails(_driver, testCaseParam, Action, ActionDescription,pageDetails, StartTime, Status_Fail);
			}

		}
		catch (Exception e)
		{
			logger.error("Failed ==> " + Action + ActionDescription);
			TestStepDetails.logExceptionDetails(_driver, testCaseParam, Action, ActionDescription, StartTime,e);
			throw e;

		}
	}
	public void VerifyPartialTextDisplayed(WebDriver _driver, WebElement element,String Text,String text, TestCaseParam testCaseParam,PageDetails pageDetails) throws Exception
	{
		String Action = "Verified "+text+" Partial Text from "+element.getText();
		String ActionDescription = "Verified "+text+" Partial Text from "+element.getText();
		LocalDateTime StartTime = LocalDateTime.now();


		try
		{

			if(element.getText().toLowerCase().contains(text.toLowerCase())) 
			{
				TestStepDetails.logTestStepDetails(_driver, testCaseParam, Action, ActionDescription,pageDetails, StartTime, Status_Done);
			}
			else 
			{
				TestStepDetails.logTestStepDetails(_driver, testCaseParam, Action, ActionDescription,pageDetails, StartTime, Status_Fail);
			}

		}
		catch (Exception e)
		{
			logger.error("Failed ==> " + Action + ActionDescription);
			TestStepDetails.logExceptionDetails(_driver, testCaseParam, Action, ActionDescription, StartTime,e);
			throw e;

		}
	}



	public void VerifyValueEntered(WebDriver _driver, WebElement element,String text, TestCaseParam testCaseParam,PageDetails pageDetails) throws Exception
	{
		String Action = "Verify Element  Text";
		String ActionDescription = "Verify Element  Text";
		LocalDateTime StartTime = LocalDateTime.now();


		try
		{
			if(text.equalsIgnoreCase("N//A")||text.equalsIgnoreCase("N/A")) 
			{

			}
			else
			{

				String ActualText=element.getAttribute("value");
				if(text.contains("\"")) 
				{
					text=text.replaceAll("\"", "");
				}
				if(ActualText.equals("___-__-____")) 
				{
					ActualText=ActualText.replace("___-__-____", "");
				}
				if(ActualText.contains("_-")) 
				{
					ActualText=ActualText.replaceAll("_", "");
					ActualText=ActualText.replaceAll("-", "");
				}


				Action = "Actual Value Displayed-->"+ActualText+"<--> Expected Value-->"+text;
				ActionDescription = "Actual Value Displayed-->"+ActualText+"<-->Expected Value-->"+text;

				if(ActualText.equalsIgnoreCase(text)) 
				{

					TestStepDetails.logTestStepDetails(_driver, testCaseParam, Action, ActionDescription,pageDetails, StartTime, Status_Done);
				}
				else 
				{
					TestStepDetails.logTestStepDetails(_driver, testCaseParam, Action, ActionDescription,pageDetails, StartTime, Status_Fail);
				}
			}	
		}
		catch (Exception e)
		{
			logger.error("Failed ==> " + Action + ActionDescription);
			TestStepDetails.logExceptionDetails(_driver, testCaseParam, Action, ActionDescription, StartTime,e);
			throw e;

		}
	}

	public void VerifyValueSelected(WebDriver _driver, WebElement element,String options, TestCaseParam testCaseParam,PageDetails pageDetails) throws Exception
	{
		String Action = "Verify Element  Text";
		String ActionDescription = "Verify Element  Text";
		LocalDateTime StartTime = LocalDateTime.now();


		try
		{
			if(options.equals("N//A") || options.equals("N/A") || options.equals("n//a") || options.equals("n/a")||options.equals(null)) 
			{

			}
			else
			{

				{								
					Select select = new Select(element);
					element=select.getFirstSelectedOption();

					String ActualText=element.getText();


					Action = "Actual Value Displayed-->"+ActualText+"<--> Expected Value-->"+options;
					ActionDescription = "Actual Value Displayed-->"+ActualText+"<-->Expected Value-->"+options;

					if(ActualText.equals(options)) 
					{

						TestStepDetails.logTestStepDetails(_driver, testCaseParam, Action, ActionDescription,pageDetails, StartTime, Status_Done);
					}
					else 
					{
						TestStepDetails.logTestStepDetails(_driver, testCaseParam, Action, ActionDescription,pageDetails, StartTime, Status_Fail);
					}
				}
			}


		}
		catch (Exception e)
		{
			logger.error("Failed ==> " + Action + ActionDescription);
			TestStepDetails.logExceptionDetails(_driver, testCaseParam, Action, ActionDescription, StartTime,e);
			throw e;

		}
	}



	public void VerifyDisabledPropertyOfElement(WebDriver _driver,WebElement element, TestCaseParam testCaseParam,PageDetails pageDetails) throws Exception
	{
		String Action = "Verify Disabled Property of Element";
		String ActionDescription = "Verify Disabled Property of Element";
		LocalDateTime StartTime = LocalDateTime.now();
		FluentWait(_driver,element);
		if(pageDetails.equals(null)) 
		{
			pageDetails.PageActionName="disability";
			pageDetails.PageActionDescription="";
		}
		try
		{
			Boolean displayed = true;
			displayed = element.isDisplayed();
			if (displayed)
			{
				String isDisabled = "";

				isDisabled = element.getAttribute("disabled");
				if(isDisabled==null) 
				{
					isDisabled="";
				}
				String isReadOnly = "";
				isReadOnly = element.getAttribute("readonly");
				if(isReadOnly==null) 
				{
					isReadOnly="";
				}
				String pointereventproperty="";
				pointereventproperty =element.getAttribute("style");
				boolean pointerevent=pointereventproperty.contains("pointer-events: none");
				if(pointerevent==true) 
				{
					Action = "The field is Disabled and Readonly, which is as expected";
					ActionDescription = "The field is Disabled and Readonly, which is as expected";
					TestStepDetails.logTestStepDetails(_driver, testCaseParam, Action, ActionDescription,pageDetails, StartTime, Status_Pass);                    
				}

				else 
				{
					if (isDisabled.equals("true") && isReadOnly.equals("true"))
					{
						Action = "The field is Disabled and Readonly, which is as expected";
						ActionDescription = "The field is Disabled and Readonly, which is as expected";
						TestStepDetails.logTestStepDetails(_driver, testCaseParam, Action, ActionDescription,pageDetails, StartTime, Status_Pass);

					}
					else if (isReadOnly.equals("") && isDisabled.equals("true"))
					{
						Action = "The field is Disabled, which is as expected";
						ActionDescription = "The field is Disabled, which is as expected";
						TestStepDetails.logTestStepDetails(_driver, testCaseParam, Action, ActionDescription,pageDetails, StartTime, Status_Pass);
					}
					else if (isReadOnly.equals("true")&&(isDisabled.equals("")||isDisabled.equals(null)))
					{
						Action = "The field is Readonly, which is as expected";
						ActionDescription = "The field is Readonly, which is as expected";
						TestStepDetails.logTestStepDetails(_driver, testCaseParam, Action, ActionDescription,pageDetails, StartTime, Status_Pass);

					}

					else if(isDisabled.equals("")||isDisabled.equals(null))
					{
						Action = "The field is not Disabled and Readonly, which is not as expected";
						ActionDescription = "The field is not Disabled and Readonly, which is not as expected";
						TestStepDetails.logTestStepDetails(_driver, testCaseParam, Action, ActionDescription,pageDetails, StartTime, Status_Fail);

					}

					else 
					{
						Action = "The field is not Disabled and Readonly, which is not as expected";
						ActionDescription = "The field is not Disabled and Readonly, which is not as expected";
						TestStepDetails.logTestStepDetails(_driver, testCaseParam, Action, ActionDescription,pageDetails, StartTime, Status_Fail);

					}
				}




			}
			else
			{
				Action = "The Element is not found";
				ActionDescription = "The field is not Disabled and Readonly, which is not as expected";
				TestStepDetails.logTestStepDetails(_driver, testCaseParam, Action, ActionDescription,pageDetails, StartTime, Status_Fail);

			}

		}
		catch (Exception e)
		{
			logger.error("Failed ==> " + Action + ActionDescription);
			TestStepDetails.logExceptionDetails(_driver, testCaseParam, Action, ActionDescription, StartTime,e);
			throw e;

		}
	}

	public void verifypropertyofelement(WebDriver _driver, WebElement element, String dataValue, TestCaseParam testCaseParam, PageDetails pageDetails) throws Exception
	{
		String Action = "Verify Property of Element-->"+element.getAttribute("Value");
		String ActionDescription = "Verify Property of Element-->"+element.getAttribute("Value");
		LocalDateTime StartTime = LocalDateTime.now();
		if(pageDetails.equals(null)) 
		{
			pageDetails.PageActionName="";
			pageDetails.PageActionDescription="";
		}
		try
		{
			Boolean displayed = true;
			displayed = element.isDisplayed();
			if (displayed)
			{
				String prop = "";
				String[] data = null;

				data = dataValue.split("&");
				prop=element.getAttribute(data[0]) ;

				if (prop.equals(data[1]))
				{
					Action = "The " + data[0] + " Property of the element is as expected";
					ActionDescription = "The " + data[0] + " Property of the element is as expected";
					TestStepDetails.logTestStepDetails(_driver, testCaseParam, Action, ActionDescription,pageDetails, StartTime, Status_Done);

				}
				else 
				{
					Action = "The " + data[0] + " Property of the element is not as expected";
					ActionDescription = "The " + data[0] + " Property of the element is not as expected";
					TestStepDetails.logTestStepDetails(_driver, testCaseParam, Action, ActionDescription,pageDetails, StartTime, Status_Fail);

				}
			}
		}
		catch (Exception e)
		{
			logger.error("Failed ==> " + Action + ActionDescription);
			TestStepDetails.logExceptionDetails(_driver, testCaseParam, Action, ActionDescription, StartTime,e);
			throw e;

		}
	}

	public void VerifyCheckBoxIsNotChecked(WebDriver _driver,WebElement element,TestCaseParam testCaseParam, PageDetails pageDetails ) throws Exception
	{
		String Action="";
		String ActionDescription="";
		LocalDateTime StartTime = LocalDateTime.now();
		try
		{

			if (!element.isSelected())
			{

				Action = "The element is Not checked";
				ActionDescription = "The element is Not checked";
				TestStepDetails.logTestStepDetails(_driver, testCaseParam, Action, ActionDescription,pageDetails, StartTime, Status_Done);

			}
			else
			{
				Action = "The element is  checked";
				ActionDescription = "The element is  checked";
				TestStepDetails.logTestStepDetails(_driver, testCaseParam, Action, ActionDescription,pageDetails, StartTime, Status_Fail);

			}
		}
		catch (Exception e)
		{

			logger.error("Failed ==> " + Action + ActionDescription);
			TestStepDetails.logExceptionDetails(_driver, testCaseParam, Action, ActionDescription, StartTime,e);
			throw e;
		}

	}

	public void DismissAlertMessage(WebDriver _driver, TestCaseParam testCaseParam, PageDetails pageDetails) throws Exception
	{


		String Action="";
		String ActionDescription="";
		LocalDateTime StartTime = LocalDateTime.now();


		try
		{
			_driver.switchTo().alert().dismiss();
			Action = "Successfully Dismissed Alert Message";
			ActionDescription = "Successfully Dismissed Alert Message";
			//TestStepDetails.logTestStepDetails(_driver, testCaseParam, Action, ActionDescription,pageDetails, StartTime, Status_Done);

		}
		catch (Exception e)
		{
			Action = "Failed to Dismiss Alert Message";
			ActionDescription = "Failed to Dismiss Alert Message";
			TestStepDetails.logTestStepDetails(_driver, testCaseParam, Action, ActionDescription,pageDetails, StartTime, Status_Fail);

		}


	}

	public void EnterTextByFunctionKeys(WebDriver _driver,WebElement element,Keys key,String KeyValue, TestCaseParam testCaseParam, PageDetails pageDetails) throws Exception
	{


		String Action="";
		String ActionDescription="";
		LocalDateTime StartTime = LocalDateTime.now();


		try
		{
			webkeywords.Instance().FluentWait(_driver, element);
			WebDriverWait wait = new WebDriverWait(_driver, 1000);
			wait.until(ExpectedConditions.elementToBeClickable(element));				

			element.sendKeys(key+KeyValue);

			LocalDateTime EndTime =  LocalDateTime.now();

			logger.info("Successfully Entered Text" + key + "to " + element);
		}
		catch (Exception e)
		{
			Action = "Failed to Dismiss Alert Message";
			ActionDescription = "Failed to Dismiss Alert Message";
			TestStepDetails.logTestStepDetails(_driver, testCaseParam, Action, ActionDescription,pageDetails, StartTime, Status_Fail);

		}


	}

	public void EnterTextByFunctionKeys(WebDriver _driver,WebElement element,Keys key, TestCaseParam testCaseParam, PageDetails pageDetails) throws Exception
	{


		String Action="";
		String ActionDescription="";
		LocalDateTime StartTime = LocalDateTime.now();


		try
		{
			webkeywords.Instance().FluentWait(_driver, element);
			WebDriverWait wait = new WebDriverWait(_driver, 1000);
			wait.until(ExpectedConditions.elementToBeClickable(element));				

			element.sendKeys(key);

			LocalDateTime EndTime =  LocalDateTime.now();

			logger.info("Successfully Entered Text" + key + "to " + element);
		}
		catch (Exception e)
		{
			Action = "Failed to Dismiss Alert Message";
			ActionDescription = "Failed to Dismiss Alert Message";
			TestStepDetails.logTestStepDetails(_driver, testCaseParam, Action, ActionDescription,pageDetails, StartTime, Status_Fail);

		}


	}

	public void ZoomWebPage(WebDriver _driver,String ZoomValue, TestCaseParam testCaseParam, PageDetails pageDetails) throws Exception
	{


		String Action="";
		String ActionDescription="";
		LocalDateTime StartTime = LocalDateTime.now();



		try
		{

			JavascriptExecutor executor = (JavascriptExecutor)_driver;
			executor.executeScript("document.body.style.zoom = '"+ZoomValue+"'");
			Action = "Sucessfully Zoomed the page to -->"+ZoomValue;
			ActionDescription = "Sucessfully Zoomed the page to -->"+ZoomValue;
			TestStepDetails.logTestStepDetails(_driver, testCaseParam, Action, ActionDescription,pageDetails, StartTime, Status_Done);


		}
		catch (Exception e)
		{
			Action = "Failed to Zoom the page";
			ActionDescription = "Failed to Zoom the page";
			TestStepDetails.logTestStepDetails(_driver, testCaseParam, Action, ActionDescription,pageDetails, StartTime, Status_Fail);

		}


	}

	public void MouseHover(WebDriver _driver,WebElement element, TestCaseParam testCaseParam, PageDetails pageDetails) throws Exception
	{


		String Action="Mouse Hover";
		String ActionDescription="Mouse Hover";
		LocalDateTime StartTime = LocalDateTime.now();



		try
		{
			webkeywords.Instance().FluentWait(_driver, element);
			Actions action = new Actions(_driver);

			action.moveToElement(element).build().perform();
			TestStepDetails.logTestStepDetails(_driver, testCaseParam, Action, ActionDescription,pageDetails, StartTime, Status_Done);


		}
		catch (Exception e)
		{
			Action = "Failed do mouse hover";
			ActionDescription = "Failed do mouse hover";
			TestStepDetails.logTestStepDetails(_driver, testCaseParam, Action, ActionDescription,pageDetails, StartTime, Status_Fail);

		}


	}

	public void VerifyDropdownValues(WebDriver _driver, WebElement element,String options, TestCaseParam testCaseParam,PageDetails pageDetails) throws Exception
	{
		String Action = "Verify Dropdown Values";
		String ActionDescription = "Verify Dropdown Values";
		LocalDateTime StartTime = LocalDateTime.now();


		try
		{
			if(options.equals("N//A") || options.equals("N/A") || options.equals("n//a") || options.equals("n/a")||options.equals(null)) 
			{

			}
			else
			{

				{		
					ArrayList<String> Actualvalues=new ArrayList<String>();
					ArrayList<String> Expectedvalues=new ArrayList<String>();
					Select select = new Select(element);
					int count=0;

					String[] exvalues=options.split(";");
					for(int i=0; i<exvalues.length;i++) 
					{
						Expectedvalues.add(exvalues[i]);
					}

					int expectedcount=Expectedvalues.size();
					List<WebElement> elements = select.getOptions();
					for (WebElement we : elements) {
						for (int i = 0; i < Expectedvalues.size(); i++) 
						{
							if (we.getText().equals(Expectedvalues.get(i))) 
							{
								Actualvalues.add(we.getText());
								count++;
								break;
							}
						}

					}
					if(count==0) 
					{
						TestStepDetails.logTestStepDetails(_driver, testCaseParam, Action, ActionDescription,pageDetails, StartTime, Status_Fail);

					}
					if(count==expectedcount) 
					{
						Action = "Verify Dropdown values<=>Expected Values="+Expectedvalues+"<=>Actual Values="+Actualvalues;
						ActionDescription = "Verify Dropdown values<=>Expected Values="+Expectedvalues+"<=>Actual Values="+Actualvalues;
						TestStepDetails.logTestStepDetails(_driver, testCaseParam, Action, ActionDescription,pageDetails, StartTime, Status_Done);

					}

				}	
			}
		}
		catch (Exception e)
		{
			logger.error("Failed ==> " + Action + ActionDescription);
			TestStepDetails.logExceptionDetails(_driver, testCaseParam, Action, ActionDescription, StartTime,e);
			throw e;

		}
	}

	public void ScrollUpPageToTheTop(WebDriver driver) throws Exception
	{

		String Action = "Scroll Up Page To The Top";
		String ActionDescription = "Scroll Up Page To The Top";



		try
		{

			((JavascriptExecutor) driver).executeScript("window.scrollTo(document.body.scrollHeight, 0)");


		}
		catch (Exception e)
		{
			logger.error("Failed ==> " + Action + ActionDescription);
			throw e;
		}



	}

	public void scrollIntoViewElement(WebDriver driver, WebElement element) throws Exception
	{

		webkeywords.Instance().FluentWait(driver, element);

		JavascriptExecutor js = (JavascriptExecutor) driver;

		js.executeScript("arguments[0].scrollIntoView(true);",element);

	}



	public void verifydropdownoptionnotavailable(WebDriver _driver, WebElement element, String options, TestCaseParam testCaseParam, PageDetails pageDetails) throws Exception
	{
		String Action = "Verifying Dropdown Options -->"+element.getText()+" : does not contain :-"+options;
		String ActionDescription = "Verifying Dropdown Options -->"+element.getText()+" : does not contain :-"+options;
		LocalDateTime StartTime = LocalDateTime.now();
		Boolean Valuefound = true;

		try
		{
			if ("N//A".equals(options) || options == null)
				{

			}
			else
			{

				webkeywords.Instance().FluentWait(_driver, element);
				Select select = new Select(element);
				List<WebElement> allOptions = select.getOptions();
				for(int i=0; i<allOptions.size(); i++) 
				{

					if(allOptions.get(i).getText().contains(options)) 
					{
						Valuefound=false;
						Action = "Verify Dropdown-->contains "+allOptions.get(i).getText()+"which is not as Expected";
						ActionDescription = "Verify Dropdown-->contains "+allOptions.get(i).getText()+"which is not as Expected";

						break;
					}
				}
				if(Valuefound) 
				{
					TestStepDetails.logVerificationDetails(_driver, testCaseParam, Action, ActionDescription, StartTime, Status_Done, element.getText(), options);
				}
				else 
				{
					TestStepDetails.logTestStepDetails(_driver, testCaseParam, Action, ActionDescription,pageDetails, StartTime, Status_Fail);

				}
			}
		}
		catch (Exception e)
		{
			logger.error("Failed ==> " + Action + ActionDescription);
			TestStepDetails.logExceptionDetails(_driver, testCaseParam, Action, ActionDescription, StartTime,e);
			throw e;
		}
	}




	public void openNewTabwithURL(WebDriver _driver, String URL, TestCaseParam testCaseParam, PageDetails pageDetails) throws Exception
	{
		String Action = "Open new tab with url";
		String ActionDescription = "Open new tab with url";
		LocalDateTime StartTime = LocalDateTime.now();


		try
		{
			JavascriptExecutor javaScript = (JavascriptExecutor) _driver;

			javaScript.executeScript("window.open(arguments[0])", URL);
			webkeywords.Instance().WaitForPageToLoad(_driver, 5000);
			Thread.sleep(5000);
			String currentWindow = _driver.getWindowHandle();
			logger.info("Current Window: " + currentWindow);

			int windowsCount = _driver.getWindowHandles().size();
			logger.info("Count of Windows: " + windowsCount);

			if (windowsCount > 1) 
			{
				logger.info("New Window is opened. Switching the control to the new window");
				ArrayList<String> tab = new ArrayList<>(_driver.getWindowHandles());
				_driver.switchTo().window(tab.get(1));
				webkeywords.Instance().WaitForPageToLoad(_driver, 5000);
			}

		}
		catch (Exception e)
		{
			logger.error("Failed ==> " + Action + ActionDescription);
			TestStepDetails.logExceptionDetails(_driver, testCaseParam, Action, ActionDescription, StartTime,e);
			throw e;
		}
	}

	//Pass the option as "title" attribute value on the web page
	public void VerifyDropdownValueSelected(WebDriver _driver, WebElement element,String options, TestCaseParam testCaseParam,PageDetails pageDetails) throws Exception
	{
		String Action = "Verify Element  Text";
		String ActionDescription = "Verify Element  Text";
		LocalDateTime StartTime = LocalDateTime.now();


		try
		{
			String ActualText=element.getAttribute("title");


			Action = "Actual Value Displayed-->"+ActualText+"<--> Expected Value-->"+options;
			ActionDescription = "Actual Value Displayed-->"+ActualText+"<-->Expected Value-->"+options;

			if(ActualText.equals(options)) 
			{

				TestStepDetails.logTestStepDetails(_driver, testCaseParam, Action, ActionDescription,pageDetails, StartTime, Status_Done);
			}
			else 
			{
				TestStepDetails.logTestStepDetails(_driver, testCaseParam, Action, ActionDescription,pageDetails, StartTime, Status_Fail);
			}



		}
		catch (Exception e)
		{
			logger.error("Failed ==> " + Action + ActionDescription);
			TestStepDetails.logExceptionDetails(_driver, testCaseParam, Action, ActionDescription, StartTime,e);
			throw e;

		}
	}


	//Added method by Thamodharan
	//To capture UI performance for navigating one screen to another 
public  void navigateToNextPage(WebDriver driver,WebElement element, TestCaseParam testCaseParam, PageDetails pageDetails ) throws Exception
{
	String Action = "Capture Page Response Time";
	String ActionDescription = "Capture Page Response Time";
	String sspHeader = "//div[@class='ssp-header']/..//h1";

//	NavigationTimeHelper navigationTimeHelper = new NavigationTimeHelper();
//	UIPerfModel uiPerfModel = new UIPerfModel();
//	UIPerfConstants uiPerfConstants = new UIPerfConstants();
//	
	String getHeaderBeforeClick = "";
	String getHeaderAfterClick = "";
	
	try {
		WebDriverWait wait=new WebDriverWait(driver, 3);
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//img[@alt='Spinner']")));
	    getHeaderBeforeClick = driver.findElement(By.xpath("//div[@class='ssp-menuItemDropDownHeader']/p")).getText();
	    if (getHeaderBeforeClick.equals("")){
	        getHeaderBeforeClick = driver.findElement(By.xpath(sspHeader)).getText();
	    }
	    
	} catch (Exception e) {
	  logger.info("Getting title of Current page");
	    getHeaderBeforeClick = driver.getTitle();
	}

	LocalDateTime startTime = LocalDateTime.now();
	if (!element.getText().equals("NULL") || !element.getText().equals("") || !element.getAttribute("title").isBlank()) {
	    try {
	        if (element.isDisplayed()) {
	            JavascriptExecutor executor = (JavascriptExecutor) driver;
	            executor.executeScript("arguments[0].click();", element);
	            WebDriverWait wait = new WebDriverWait(driver, 3);
	            wait.until(webDriver -> ((JavascriptExecutor) webDriver).executeScript("return document.readyState").equals("complete"));
	            //TestStepDetails.logTestStepDetails(driver, testCaseParam, Action, ActionDescription, pageDetails, StartTime, Status_Pass);
	        }
	    } catch (Exception e) {
	        logger.info("Unable to click on element");
	   }
		long startTimePerf = System.currentTimeMillis();
		
	    try {
	        WebDriverWait wait=new WebDriverWait(driver, 5);
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//img[@alt='Spinner']")));
		    getHeaderAfterClick = driver.findElement(By.xpath("//div[@class='ssp-menuItemDropDownHeader']/p")).getText();
	        if (getHeaderAfterClick.equals("")) {
	            getHeaderAfterClick = driver.findElement(By.xpath(sspHeader)).getText();
	        }
	    } catch (Exception e) {
	       logger.info("Getting Next Page Title");
	        getHeaderAfterClick = driver.getTitle();
	    }
	    
	    try {
	    	WebDriverWait wait=new WebDriverWait(driver, 5);
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//img[@alt='Spinner']")));
		   
			logger.info("Current Page title : {}",getHeaderBeforeClick);
			logger.info("Next Page title : {}",getHeaderAfterClick);
			

	        logger.info("Measuring performance metrics done");
	        
	} catch (NoSuchElementException e) {
	    logger.error("Failed ==> " + Action + ActionDescription);
	    TestStepDetails.logExceptionDetails(driver, testCaseParam, Action, ActionDescription, startTime, e);
	    throw e;
	}
	}


	

}
}