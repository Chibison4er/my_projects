package by.chibis.aa.attempts;

import java.util.Map;

import org.bukkit.Bukkit;

import by.chibis.aa.Main;
import by.chibis.aa.msql.AA_Mysql;

public class AA_Attempts implements Runnable
{
	
	@Override
	public void run() { check(); }
	
	private void check()
	{
		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.inst, new Runnable()
		{
			
			@Override
			public void run() 
			{
				for(Map.Entry<String, Integer> map : AA_Mysql.getAllAtt().entrySet())
					AA_Mysql.setAttempts(map.getKey(), 0);
				
				check();				
			}
			
		}, Main.inst.recoilAttTime * 20 * 60);
		
	}
	
}
