package wmrfast.wmrsite;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import wmrfast.Main;
import wmrfast.timer.WmrTimerTask;
import wmrfast.utils.WmrUtils;
import wmrfast.utils.WmrlinkFilterSettings;
import wmrfast.wmrsite.profile.WmrUserProfile;

public class WmrMainPage 
{
	private String mainPage = "https://wmrfast.com/";
	
	private String loginFormButtonXpath = "//*[@id='logbtn']";
	
	private WebDriver driver;
	private String baseWindow;
	
	private WebElement menuElement;
	
	private boolean isLogeded = false;
	private boolean isDebug = true;
	
	private Timer timer;
	private WmrTimerTask wmrTask;
	
	private WmrUserProfile userProfile;
		
	public WmrMainPage(WebDriver driver) 
	{
		isDebug = Main.isDebugEnabled();
		
		this.driver = driver;
		
		driver.get(mainPage);
		baseWindow = this.driver.getWindowHandle();
		
		WmrUtils.setWebDriver(driver);
		WmrUtils.setBaseWindow(baseWindow);
		
		WmrUtils.closeAllExpectBase();

	}
	
	public void openLoginForm()
	{
		driver.findElement(By.xpath(loginFormButtonXpath)).click();
		
		new WmrLoginForm(driver, this);
		
		if(isDebug)
			System.out.println("LoginForm was open!");
	}
		
	public void setLoginStatus(boolean loginState)
	{ 
		isLogeded = loginState;
		
		if(isLogeded == true)
		{
			if(isDebug)
				System.out.println("Try to find & click in menu");
			
			userProfile = new WmrUserProfile();
			
			tryToFindMenuElement();
			
			startTimer();
			
			if(isDebug)
				System.out.println("Starting the Timer");
		}
	}
	
	private void startTimer()
	{
		if(isDebug)
			System.out.println("StartTimer");
		
		timer = new Timer();
		wmrTask = new WmrTimerTask();
		
		timer.schedule(wmrTask, 1, 1000);
		
		wmrTask.requestNewElements();
//		wmrTask.startWatchLinks(0);
	}
	
	public void tryToFindMenuElement()
	{
		WebDriverWait waitMenuItem = new WebDriverWait(driver, 9000);
		
		waitMenuItem.pollingEvery(Duration.ofMillis(200));
		waitMenuItem.ignoring(StaleElementReferenceException.class).ignoring(ElementClickInterceptedException.class);
		
		menuElement = waitMenuItem.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id='help_link_prosmotr_reklami1']")));
		WmrUtils.clickElement(menuElement);
		//menuElement.click();
		
		System.out.println("Find & clicked!");
		
	}
	
	public ArrayList<WmrSiteItem> collectSiteList()
	{
		if(isDebug)
			System.out.println("Start collect links!");
		
		try { Thread.currentThread().sleep(2000); } catch (InterruptedException e) { e.printStackTrace(); }
		
		if(isDebug)
			System.out.println("Create empty ArrayList");
			
		ArrayList<WmrSiteItem> items = new ArrayList<>();
		
		if(isDebug)
			System.out.println("Take WebElements in list by xpath");
		
		List<WebElement> elements = driver.findElements(By.xpath("//*[@class='no_active_link']"));//className("no_active_link"));
		
		// Формировать новый список использовав длинну текущего списка
		if(isDebug)
		{
			System.out.println("List<WebElement> elements size: " + elements.size());
			System.out.println("Clean List with elements");
		}
			
		List<WebElement> findElements = new ArrayList<>();
		
		for(int i = 0; i < elements.size(); i++)
		{
			if(i <= WmrlinkFilterSettings.maxLinks)			
				findElements.add(WmrUtils.getElement(String.format("//*[@class='no_active_link'][%s]", i+1)));
		}
		
		int count = 1;
		
		for(WebElement item : findElements)
		{
			items.add(new WmrSiteItem(driver, item, count));
			
			if(isDebug)
				System.out.println(String.format("Added! %s/%s id = %s", count, findElements.size(), item.getAttribute("id")));
			
			count ++;
		}
		
		return items;
	}
	
	public String getBaseWindow() { return baseWindow; }
	
	public WmrUserProfile getUserProfile() { return userProfile; }
}
