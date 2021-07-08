package by.chibis.msban.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;

import by.chibis.msban.MsBanMain;
import by.chibis.msban.config.MsBanConfigStorage;
import by.chibis.msban.groups.MsBanType;
import by.chibis.msban.sql.MsSql;
import by.chibis.msban.users.MsUserManager;
import mspermissions.MSPermissions;

public class MsBanListener implements Listener 
{
	@EventHandler
	public void onPlayerLogin(PlayerLoginEvent e)
	{
		MsBanType type = MsSql.getUserPunish(e.getPlayer().getName());
		
		if(!MSPermissions.Companion.getPlayerGroup(e.getPlayer().getName()).equalsIgnoreCase("default"))
			MsUserManager.addUser(e.getPlayer().getName());
		
		if(type == MsBanType.BAN)
		{
			long banTime = MsSql.getPunishTime(e.getPlayer().getName());
			
			if((banTime - System.currentTimeMillis()) <= 0)
				MsSql.unBan(e.getPlayer().getName());
			else
			{
				String reason = MsSql.getReason(e.getPlayer().getName());
				
				String banMsg = MsBanConfigStorage.banMsg
						.replaceAll("%target%", e.getPlayer().getName())
						.replaceAll("%who%", MsSql.getWhoSetBan(e.getPlayer().getName()))
						.replaceAll("%reason%", reason)
						.replaceAll("%time%", MsBanMain.convertSecondsToHMmSs(banTime));
				
				e.disallow(Result.KICK_OTHER, banMsg);
			}
		}
	}
	
	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent e)
	{
		if(MsSql.getUserPunish(e.getPlayer().getName()) == MsBanType.MUTE)
		{
			long time = MsSql.getPunishTime(e.getPlayer().getName());
			
			if((time  - System.currentTimeMillis()) <= 0)
				MsSql.unBan(e.getPlayer().getName());
			else
			{
				e.getPlayer().sendMessage(String.format(MsBanConfigStorage.msgs.get("inmute"), MsBanMain.convertSecondsToHMmSs(time)));
				e.setCancelled(true);
			}
		}	
	}
}
