package by.chibis.msban;

import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import by.chibis.msban.cmds.MsBanCommand;
import by.chibis.msban.cmds.MsBanListCommand;
import by.chibis.msban.cmds.MsKickCommand;
import by.chibis.msban.cmds.MsMuteCommand;
import by.chibis.msban.cmds.MsUnbanCommand;
import by.chibis.msban.config.MsBanConfig;
import by.chibis.msban.groups.MsPunishTimeType;
import by.chibis.msban.listeners.MsBanListener;
import by.chibis.msban.sql.MsSql;
import by.chibis.msban.users.MsUserManager;
import mspermissions.MSPermissions;
import net.md_5.bungee.api.ChatColor;

public class MsBanMain extends JavaPlugin
{
	public static MsBanMain inst;
	
	PluginManager pm;
	public String name;
	
	public static boolean useMsPex = false;
	
	public void onEnable()
	{
		inst = this;
		
		pm = this.getServer().getPluginManager();
		
		name = this.getDescription().getName();
		MsBanConfig.createFile();
		
		if(pm.getPlugin("PermissionsEx") != null)
			useMsPex = false;
		else if(pm.getPlugin("MSPermissions") != null)
			useMsPex = true;
		
		pm.registerEvents(new MsBanListener(), this);
		
		this.getCommand("ban").setExecutor(new MsBanCommand());
		this.getCommand("kick").setExecutor(new MsKickCommand());
		this.getCommand("mute").setExecutor(new MsMuteCommand());
		this.getCommand("unban").setExecutor(new MsUnbanCommand());
		this.getCommand("banlist").setExecutor(new MsBanListCommand());
		
		if(useMsPex)
		{
			for(Player p : this.getServer().getOnlinePlayers())
			{
				if(!MSPermissions.Companion.getPlayerGroup(p.getName()).equalsIgnoreCase("default"))
					MsUserManager.addUser(p.getName());
			}
		}else
		{
			for(Player p : this.getServer().getOnlinePlayers())
			{
				
			}
		}
		
		MsSql.loadPunished();
	}
	
	public static MsPunishTimeType getTimeType(String time) { return MsPunishTimeType.valueOf(time.substring(time.length()-1, time.length())); }
	public static String getColored(String s) { return ChatColor.translateAlternateColorCodes('&', s); }
	
	public boolean isTimeAvalable(MsPunishTimeType max, int maxTime, MsPunishTimeType cur, int curTime)
	{
		long maxMills = getBanTime(maxTime, max);
		long minMills = getBanTime(curTime, cur);
		
		return maxMills >= minMills;
	}
	
	public static String convertSecondsToHMmSs(long mills) 
	{
		mills = mills - System.currentTimeMillis();
		
		long seconds = mills / 1000;
		
	    long s = seconds % 60;
	    long m = (seconds / 60) % 60;
	    long h = (seconds / (60 * 60)) % 24;
	    return String.format("%d:%02d:%02d", h,m,s);
	}
	
	public long getBanTime(int time, MsPunishTimeType type)
	{
		switch(type)
		{
			case h: { return System.currentTimeMillis() + time * (1000*60*60); }
			case m: { return System.currentTimeMillis() + time * (1000*60); }
			case s: { return System.currentTimeMillis() + time * 1000; }	
		}
		
		return 0;
	}
}
