package uitests.testng.common;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import constants.PrjConstants;

public class CommonOperations {
	
	private static final Logger logger =LoggerFactory.getLogger(CommonOperations.class.getName());

	private static WebDriverWait wait;

	public CommonOperations(WebDriver driver) {
	    initializeWait(driver);
	}

	private static void initializeWait(WebDriver driver) {
	    wait = new WebDriverWait(driver, 20);
	}

	
	public void pageRefresh(WebDriver driver) throws InterruptedException {
		driver.navigate().refresh();
		Thread.sleep(PrjConstants.DELAY);
	}
	
	public void switchToLastTab(WebDriver driver) {
		ArrayList<String> tabList = new ArrayList<>(driver.getWindowHandles());
		driver.switchTo().window(tabList.get(tabList.size()-1));
	}
	
	public static String getDate(String format, String whichDate) {

		if (whichDate.equalsIgnoreCase("today")) {
			SimpleDateFormat formatter = new SimpleDateFormat(format);
			Date date = new Date();
			return formatter.format(date);
		}
		else if (whichDate.equalsIgnoreCase("future")) {

			SimpleDateFormat formatter = new SimpleDateFormat(format);
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DATE, +1);
			Date date = cal.getTime();
			return formatter.format(date);
		} else if (whichDate.equalsIgnoreCase("past")) {

			SimpleDateFormat formatter = new SimpleDateFormat(format);
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.DATE, -1);
			Date date = cal.getTime();
			return formatter.format(date);
		} else if (whichDate.equalsIgnoreCase("FutureDate")) {

			SimpleDateFormat formatter = new SimpleDateFormat(format);
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.YEAR, +1);
			Date date = cal.getTime();
			return formatter.format(date);
		} else if (whichDate.equalsIgnoreCase("FutureDateAdded")) {

			SimpleDateFormat formatter = new SimpleDateFormat(format);
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.YEAR, +1);
			cal.add(Calendar.DATE, +5);
			Date date = cal.getTime(); 
			return formatter.format(date);
		}
		return null;
	}
	
	public static void selectDropdownvalue(WebDriver driver,String fieldName, String fieldValue) {
		List<WebElement> listElement = dropdownList(driver,fieldName);

		if (!listElement.isEmpty()) {
			for (WebElement listValue : listElement) {
				if (listValue.getText().equalsIgnoreCase(fieldValue)) {
					listValue.click();
					break;
				}
			}
		}else {
			logger.error("{} not found", fieldValue);
		}
	}
	
	public static List<WebElement> dropdownList(WebDriver driver,String elementName) {

		List<WebElement> dropdownList;

		String labelName = elementName;

		String genericDropdownListXpath1 = "//*[@id=string(//label[text()='" + labelName
				+ "']/@for)]//following::div/lightning-base-combobox-item/span";
		String genericDropdownListXpath2 = "//*[@id=string(//span[text()='" + labelName
				+ "']/@for)]//following::lightning-select/div/div/select";
		String genericDropdownListXpath3 = "//*[@aria-labelledby=string(//span[text()='" + labelName
				+ "']/parent::lightning-formatted-rich-text/@id)]//option";
		String genericDropdownListXpath4 = "//*[text()='" + labelName
				+ "']//following::lightning-base-combobox//button/span";
		String genericDropdownListXpath5 = "//div[@aria-labelledby=string(//span[text()='" + labelName
				+ "']/parent::label/@id)]/ul/li";
		String genericDropdownListXpath6 = "//div[@class='select-options']/ul/li/a[text()='" + labelName + "']";
		String genericDropdownListXpath7 = "//div[@role='listbox']/ul/li/div[@data-value='" + labelName + "']";
		String genericDropdownListXpath8 = "//span[text()='" + labelName
				+ "']/parent::label/parent::div/following-sibling::div//li";
		String genericDropdownListXpath9 = "//input[@role='combobox' and @data-value='" + labelName + "']";
		String genericDropdownListXpath10 = "//label/span[text()='" + labelName + "']//following::lightning-base-combobox/div/div/div[@role='listbox']/lightning-base-combobox-item";


		String xpathString = genericDropdownListXpath1 + " | " + genericDropdownListXpath2 + " | "
				+ genericDropdownListXpath3 + " | " + genericDropdownListXpath4 + " | "
				+ genericDropdownListXpath5 + " | " + genericDropdownListXpath6 + " | "
				+ genericDropdownListXpath7 + " | " + genericDropdownListXpath8+ " | " + genericDropdownListXpath9+ " | " + genericDropdownListXpath10;

		wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(xpathString)));

		dropdownList = driver.findElements(By.xpath(xpathString));

		return dropdownList;
	}

	public static WebElement radiobutton(WebDriver driver,String elementName) {
		WebElement radiobutton;

		String labelName =  elementName;

		String genericRradiobuttonXpath1 = "//label[@class='slds-radio']/div/span[text()='" + labelName + "']";
		String genericRradiobuttonXpath2 = "//label[@class='slds-radio__label']/span[text()='" + labelName
				+ "']";
		String genericRradiobuttonXpath3 = "//*[text()[normalize-space(.) = '" + labelName + "']]";
		String genericRradiobuttonXpath4 = "//input[@name='" + labelName + "']/../label";
		String genericRradiobuttonXpath5 = "//*[.='" + labelName + "']//input";
		String xpathString = genericRradiobuttonXpath1 + " | " + genericRradiobuttonXpath2 + " | "
				+ genericRradiobuttonXpath3 + " | " + genericRradiobuttonXpath4 + " | "
				+ genericRradiobuttonXpath5;

		wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath(xpathString)));

		radiobutton = driver.findElement(By.xpath(xpathString));

		return radiobutton;
	}
	
	public static void scrollTillElement(WebDriver driver,WebElement element) {
		JavascriptExecutor js = (JavascriptExecutor)driver;
		js.executeScript("arguments[0].scrollIntoView();", element);
	}
}
