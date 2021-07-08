package wmrfast.connection;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import wmrfast.wmrsite.WmrMainPage;

public class WmrOpenSession 
{
	private WmrMainPage mainPage;
	
	private WebDriver driver;
	
	private int maxTimeToWaitElemet = 30; // seconds
	
	public void openSession()
	{
		ChromeOptions options = new ChromeOptions();
		
		options.setPageLoadStrategy(PageLoadStrategy.NORMAL);
		
		driver = new ChromeDriver(options);
		
		driver.manage().timeouts().implicitlyWait(maxTimeToWaitElemet, TimeUnit.SECONDS);
		
		mainPage = new WmrMainPage(driver);
		mainPage.openLoginForm();
	}
	
	public WebDriver getWebDriver() { return driver; }
	
	public WmrMainPage getMainPage() { return mainPage; }
}
