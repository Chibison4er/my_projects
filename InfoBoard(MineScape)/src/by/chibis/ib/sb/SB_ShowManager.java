package by.chibis.ib.sb;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import by.chibis.ib.Main;
import by.chibis.ib.tabls.msBoard;

public class SB_ShowManager implements Runnable
{
	int index = 0;
	msBoard board;
	
	@Override
	public void run() { startShowSb(); }
	
	private void startShowSb()
	{
		if(index > Main.tableBoard.size() - 1)
			index = 0;
		
		board = Main.inst.getBoardByindex(index);
		
		Bukkit.getScheduler().scheduleSyncDelayedTask(Main.inst, new Runnable()
		{
			
			@Override
			public void run() 
			{
				for(Player p : Bukkit.getOnlinePlayers())
					board.showInfoBord(p);
				
				startShowSb();
				index ++;
			}
			
		}, board.getSbUpdateTime() * 20);
	}

}
