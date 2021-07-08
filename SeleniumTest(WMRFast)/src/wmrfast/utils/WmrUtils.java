package wmrfast.utils;

import java.time.Duration;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class WmrUtils 
{
	private static WebDriver driver = null;
	private static JavascriptExecutor jse = null;
	private static String baseWindow = null;
	
	private static long timeForWait = 10000;
	
	public static void closeAllExpectBase()
	{
		Set<String> allWindows = driver.getWindowHandles();
		
		allWindows.remove(baseWindow);
		
		for(String curWindow : allWindows)
		{
			driver.switchTo().window(curWindow);
			driver.close();
		}
		
		driver.switchTo().window(baseWindow);
	}
	
	public static WebElement getElement(String xpath)
	{
		WebDriverWait waitElement = new WebDriverWait(driver, timeForWait);
		
		waitElement.pollingEvery(Duration.ofMillis(200));
		waitElement.ignoring(StaleElementReferenceException.class).ignoring(ElementClickInterceptedException.class);
		
		return waitElement.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xpath)));
	}
	
	public static void clickElement(WebElement clicklbeElement)
	{
		
		jse.executeScript("arguments[0].click()", clicklbeElement);
	}
	
	public static WebDriver getDriver() { return driver; }
	
	public static void setWebDriver( WebDriver updDriver ) 
	{
		driver = updDriver; 
		jse = (JavascriptExecutor) updDriver;
	}
	public static void setBaseWindow( String updBaseWindow ) { baseWindow = updBaseWindow; }
}
