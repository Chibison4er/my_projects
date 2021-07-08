package wmrfast.wmrsite;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class WmrLoginForm 
{
	private String login = "Ваш_логин", 
				   pass = "Ваш_пароль";
	
	private String loginFieldXpath = "//*[@id='vhusername']",
				   passwordFieldXpath = "//*[@id='vhpass']";
	
	private WebDriver driver;
	private WmrMainPage mainPage;
	
	public WmrLoginForm(WebDriver driver, WmrMainPage mainPage) 
	{
		this.driver = driver;
		this.mainPage = mainPage;
		
		driver.findElement(By.xpath(loginFieldXpath)).sendKeys(login);
		driver.findElement(By.xpath(passwordFieldXpath)).sendKeys(pass);
		
		loginWithWaitingUserCaptha();
	}
	
	@SuppressWarnings("static-access")
	private void loginWithWaitingUserCaptha()
	{
		try{ Thread.currentThread().sleep(200); }catch(Throwable e){}
		
		for(;;)
		{
			/*if(driver.findElement(By.xpath("//*[@id='cap_text']")).getText().length() == 5 )
			{
				driver.findElement(By.xpath("//*[@id='vhod1']")).click();
				
				if(isLoggeded())
				{
					mainPage.setLoginStatus(true);
					System.out.println("Login is OK");
										
					break;
				}else
				{
					loginWithWaitingUserCaptha();
					
					System.out.println("Repeat");
				}
			}*/
			
			if(isLoggeded())
			{
				mainPage.setLoginStatus(true);
				System.out.println("Login is OK");
									
				break;
			}else
			{
				loginWithWaitingUserCaptha();
				
				System.out.println("Repeat");
			}
		}
	}
	
	private boolean isLoggeded()
	{
		return driver.findElement(By.xpath("//*[@id='help_moi_id']")) != null;
	}
}
