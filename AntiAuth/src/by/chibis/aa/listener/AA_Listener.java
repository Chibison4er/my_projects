package by.chibis.aa.listener;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerLoginEvent;

import by.chibis.aa.Main;
import by.chibis.aa.msql.AA_Mysql;

public class AA_Listener implements Listener 
{
	ArrayList<String> authCmds = new ArrayList<>(Arrays.asList("/l", "/login"));
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onPlayerLogin(final PlayerLoginEvent e)
	{
		String ip = e.getAddress().getHostAddress().toString().replaceAll("//*", "");
		
		long time = AA_Mysql.getBanTime(ip);
		
		if(time == 0)
			return;
		else
		{
			time = time - System.currentTimeMillis();
			
			if(time > 0)
				e.disallow(PlayerLoginEvent.Result.KICK_OTHER, Main.inst.reason.replaceAll("%times%", convertSecondsToHMmSs(time)));
			else
				AA_Mysql.unBan(ip);
		}
	}
	
	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerCmds(final PlayerCommandPreprocessEvent e)
	{
		String cmd = e.getMessage().split(" ")[0];
		
		if(authCmds.contains(cmd))
		{
			Bukkit.getScheduler().scheduleSyncDelayedTask(Main.inst, new Runnable()
			{
				
				@Override
				public void run()
				{
					if(Main.inst.auth.api.isAuthenticated(e.getPlayer()))
						return;
					else
					{
						String ip = e.getPlayer().getAddress().getAddress().toString().replaceAll("//*", "");

						int att = AA_Mysql.getAttemptsByIp(ip) + 1;
						AA_Mysql.setAttempts(ip, att);
						
						if(att >= Main.inst.attempts)
						{
							long time = Main.inst.getBanTime();
							
							AA_Mysql.setBan(ip, time);
							e.getPlayer().kickPlayer(Main.inst.reason.replaceAll("%times%", convertSecondsToHMmSs(time - System.currentTimeMillis())));
						}else
							e.getPlayer().sendMessage(String.format(Main.inst.attemptsLeft, (Main.inst.attempts - att), Main.inst.attempts));
					}
					
				}
			}, 10);
		}
	}
	
	public static String convertSecondsToHMmSs(long mills) 
	{
		long seconds = mills / 1000;
		
	    long s = seconds % 60;
	    long m = (seconds / 60) % 60;
	    long h = (seconds / (60 * 60)) % 24;
	    return String.format("%d:%02d:%02d", h,m,s);
	}
}
