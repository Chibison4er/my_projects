package wmrfast.wmrsite;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import wmrfast.Main;
import wmrfast.utils.WmrUtils;
import wmrfast.utils.WmrlinkFilterSettings;

public class WmrSiteItem 
{
	private WebDriver driver;
	
	private WebElement aHrefClick;
	
	private String aHrefXpath = "//*[@class='no_active_link'][%s]//*[@class='normal'][2]/a";
	private String linkWatchInfoXpath = "//*[@class='no_active_link'][%s]//*[@class='normal'][3]";
	
	private int timeToWatch;
	private float moneyForWatch;
	
	private boolean isWatchEnd = false;
	private boolean isStartWatching = false;
	
	private boolean isCanWatchBySettings = true;
	
	private boolean isDebug = true;
	
	private long startWatchingSysTime, endWatchingTime;
	
	public WmrSiteItem(WebDriver driver, WebElement elementFromList, int listIndex) 
	{
		isDebug = Main.isDebugEnabled();
		
		this.driver = driver;
		
		aHrefXpath = String.format(aHrefXpath, listIndex);
		linkWatchInfoXpath = String.format(linkWatchInfoXpath, listIndex);
		
		aHrefClick = WmrUtils.getElement(aHrefXpath);
		
		WebElement watchInfoElement = WmrUtils.getElement(linkWatchInfoXpath);
		
		for(WebElement element : watchInfoElement.findElements(By.tagName("span")))
		{
			if(element.getAttribute("class").equalsIgnoreCase("clickprice"))
			{
				if(element.getAttribute("tim") != null)
				{
					timeToWatch = Integer.parseInt(element.getAttribute("tim"));
				}else
				{
					try{ moneyForWatch = Float.parseFloat(element.getText().replaceAll(" ", "")); }
					catch(NumberFormatException e)
					{ moneyForWatch = 0f; }
				}
			}
		
		}
		
		timeToWatch = Integer.parseInt(aHrefClick.getAttribute("timer"));
		
		if(timeToWatch > WmrlinkFilterSettings.minTimeForWatch || moneyForWatch < WmrlinkFilterSettings.minPriceForWatch)
			isCanWatchBySettings = false;
		
		if(isDebug)
		{
			System.out.println("Site link was added! Accept for watch? " + (isCanWatchBySettings ? "YES" : "NO"));
			System.out.println(String.format("Name: %s\nTime to watch: %s\nMoney ofr watch: %s", aHrefClick.getText(), timeToWatch, moneyForWatch));
		}
				
	}
	
	public void watch()
	{
		if(!isCanWatchBySettings)
		{
			isWatchEnd = true;
			
			return;
		}
		
		if(!isStartWatching)
		{
			WmrUtils.clickElement(aHrefClick);
			
			isStartWatching = true;
			
			startWatchingSysTime = System.currentTimeMillis();
			endWatchingTime = startWatchingSysTime + (timeToWatch + 3) * 1000;
		}
		
		for(;;)
		{
			if(endWatchingTime - System.currentTimeMillis() <= 0 )
			{
				isWatchEnd = true;
				WmrUtils.closeAllExpectBase();
				
				break;
			}else
			{
				break;
			}
		}
	}
	
	public boolean isWatchEnd() 
	{
		if(!isWatchEnd)
		{
			watch();
		}
		
		return isWatchEnd; 
	}
	
	public boolean isLinkAcceptSettings() { return isCanWatchBySettings; }
	
	public float getMoneyForWatch() { return moneyForWatch; }
	public int getTimeForWatch() { return timeToWatch; }
}
