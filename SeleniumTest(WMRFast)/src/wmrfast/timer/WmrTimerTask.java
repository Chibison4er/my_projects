package wmrfast.timer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.TimerTask;

import wmrfast.Main;
import wmrfast.wmrsite.WmrSiteItem;
import wmrfast.wmrsite.profile.WmrUserProfile;

public class WmrTimerTask extends TimerTask
{
	private Set<WmrSiteItem> items = new HashSet<>();
	
	private int watchebleIndex = 0;
	private WmrSiteItem curWatchElement = null;
	
	private boolean isStarted = false;
	private boolean isFirstLoop = true;
	private boolean debug = Main.isDebugEnabled();
	
	private WmrUserProfile profile;
	
	public WmrTimerTask() 
	{
		profile = Main.getSession().getMainPage().getUserProfile();
	}
	
	@Override
	public void run()
	{
		if(!isStarted)
		{
			isStarted = true;
			
			if(curWatchElement == null)
				startWatchLinks(0);
			
			return;
		}
		else
		{
			if(curWatchElement != null)
			{
				if(isWatchEnd())
				{
					if(items.size() - 1 >= watchebleIndex + 1)
					{
						startWatchLinks(watchebleIndex + 1);
					}else
					{
						if(debug)
							System.out.println("list: " + (items.size() - 1) + "Index: " + (watchebleIndex + 1));
						
						requestNewElements();
					}
					
					profile.updateDynamicInformation();
					profile.showUserInfo();
				}
			}
		}
	}
	
	public void startWatchLinks(int continueStartIndex)
	{
		try { Thread.currentThread().sleep(1000); } catch (InterruptedException e) { e.printStackTrace(); }
		
		if(debug)
			System.out.println(String.format("Start watch from %s/%s", continueStartIndex, items.size()-1));
		
		int curIndex = 0;
		
		for(WmrSiteItem watchItem : items)
		{
			if(continueStartIndex > items.size() - 1)
			{
				isStarted = false;
				
				break;
			}
			
			if(curIndex == continueStartIndex)
			{
				curWatchElement = watchItem;
				
				curWatchElement.watch();
				
				watchebleIndex ++;
				
				break;
			}
			
			curIndex ++;
		}
	}
	
	private boolean isWatchEnd() { return curWatchElement.isWatchEnd(); }
	
	public void requestNewElements()
	{
		reset();
		
		if(isFirstLoop)
		{
			isFirstLoop = false;
			Main.getSession().getMainPage().tryToFindMenuElement();
		}
		else
			Main.getSession().getWebDriver().navigate().refresh();
		
		updateList(Main.getSession().getMainPage().collectSiteList());
		
		if(items.size() == 0)
		{
			requestNewElements();
		}
		else
			startWatchLinks(0);
		
	}
	
	private void reset()
	{
		items.clear();
		watchebleIndex = 0;
		curWatchElement = null;
		
		isStarted = false;
	}
	
	public void updateList(ArrayList<WmrSiteItem> list)
	{
		isStarted = false;
		
		items.clear();
		
		items.addAll(list);
	}

}
