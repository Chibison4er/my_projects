package wmrfast.wmrsite.profile;

import org.openqa.selenium.WebDriver;

import wmrfast.utils.WmrUtils;

public class WmrUserProfile 
{
	enum UserFieldXPath
	{
		CUR_MONEY("//*[@id='help_osn_chet']"),
		USER_ID("//*[@id='help_moi_id']"), // не забыть получить значение элемента
		USER_LOGIN("//*[@id='blockbg'][1]"), // не забыть получить с элемента текст
		USER_RATING("//*[@id='help_reiting']"); // не забыть получить значение
		
		private String xPath;
		
		private UserFieldXPath(String xPath)  { this.xPath = xPath; }
		
		public String getXpath() { return xPath; }
	}
	
	private String curUserMoney = "";
	
	private String userID = "";
	private String userLogin = "";
	private String userRating = "";
	
	public WmrUserProfile() 
	{		
		userID = WmrUtils.getElement(UserFieldXPath.USER_ID.getXpath()).getText();
		userLogin = WmrUtils.getElement(UserFieldXPath.USER_LOGIN.getXpath()).getText();
		
		updateDynamicInformation();
	}
	
	public void updateDynamicInformation()
	{
		updateUserMoney();
		updateUserRating();
	}
	
	public void showUserInfo()
	{
		System.out.println(String.format("========\nCur_Money: %s\nCur_Rating: %s\n========", curUserMoney, userRating));
	}
	
	public void updateUserMoney() { curUserMoney = WmrUtils.getElement(UserFieldXPath.CUR_MONEY.getXpath()).getText(); }
	public void updateUserRating() { userRating = WmrUtils.getElement(UserFieldXPath.USER_RATING.xPath).getAttribute("title"); }
	
	public String getUserMoney() { return curUserMoney; }
	public String getUserID() { return userID; }
	public String getuserLogin() { return userLogin; }
	public String getUserRating() { return userRating; }
}
