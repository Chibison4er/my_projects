package wmrfast;

import wmrfast.connection.WmrOpenSession;

public class Main 
{
	static { System.setProperty("webdriver.chrome.driver", "C:\\chromedriver_win32\\chromedriver.exe"); }
	
	private static WmrOpenSession session;
	
	private static boolean debug = true;
	
	public static void main(String... args)
	{
		session = new WmrOpenSession();
		session.openSession();
		
	}
	
	public static boolean isDebugEnabled() { return debug; }
	
	public static WmrOpenSession getSession() { return session; }
}
