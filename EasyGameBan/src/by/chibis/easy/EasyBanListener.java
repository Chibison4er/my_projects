package by.chibis.easy;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;

import by.chibis.easy.checker.EasyMuteDisallowedCmds;
import by.chibis.easy.checker.EasyTempMutemanager;
import by.chibis.easy.checker.PlayerGroupChecker;
import by.chibis.easy.checker.PlayerPunish;
import by.chibis.easy.groups.EasyGroupManager;
import by.chibis.easy.groups.EasyPlayerPunishController;
import by.chibis.easy.groups.PlayerObject;
import by.chibis.easy.lang.EasyLang;
import by.chibis.easy.mysql.MySqlWorker;

public class EasyBanListener implements Listener
{
	@EventHandler
	public void onPlayerLogin(PlayerLoginEvent e)
	{
		Player p = e.getPlayer();

		if(MySqlWorker.isUserHas(p.getName()))
		{
			EasyBanType type = MySqlWorker.getBanType(p.getName());
			
			switch(type)
			{
				case BAN:
				{
					String s = OtherMethods.getStrWithNewLine(EasyLang.BAN_PLAYER_KICK_MSG.getMsg(false));
					String whoBan = MySqlWorker.getWhoSetBan(type, p.getName());
					String reason = MySqlWorker.getReason(type, p.getName());
					
				/*	e.disallow(PlayerLoginEvent.Result.KICK_OTHER, 
							"\nНаказание: " + type.toString() + 
							"\nВы были забанены: " + MySqlWorker.getWhoSetBan(type, p.getName()) + 
							"\nПо причине: " + MySqlWorker.getReason(type, p.getName()) + 
							"\n====================" + 
							"\nЕсли вы считаете что наказание не правомерно" + 
							"\nего можно обжаловать здесь:"+
							"\nvk.com/easygame");*/
					e.disallow(PlayerLoginEvent.Result.KICK_OTHER, String.format(EasyBan.plugin.banMsg, type.getTag(), p.getName(), whoBan, reason));
					
					break;
				}
				case TEMPBAN:
				{
					long time = MySqlWorker.getEndTempBan(p.getName()) - System.currentTimeMillis();
					
					if(time <= 0)
					{
						MySqlWorker.unBanPlayer(p.getName());
						return;
					}
					
				//	String s = OtherMethods.getStrWithNewLine(EasyLang.TEMPBAN_PLAYER.getMsg(false));
					String whoBan = MySqlWorker.getWhoSetBan(type, p.getName());
					String reason = MySqlWorker.getReason(type, p.getName());
					
					time = time / 1000 / 60;
					
					/*e.disallow(PlayerLoginEvent.Result.KICK_OTHER, 
							"\nНаказание: " + type.toString() + 
							"\nВы были забанены: " + MySqlWorker.getWhoSetBan(type, p.getName()) + 
							"\nПо причине: " + MySqlWorker.getReason(type, p.getName()) + 
							"\nВы будете разбанены через: " + time +  " минут" +
							"\n====================" + 
							"\nЕсли вы считаете что наказание не правомерно" + 
							"\nего можно обжаловать здесь:"+
							"\nvk.com/easygame");*/
					e.disallow(PlayerLoginEvent.Result.KICK_OTHER, String.format(EasyBan.plugin.tempBanMsg, type.getTag(), p.getName(), whoBan, reason, time));
					
					break;
				}
				default:
					break;
			}
		}
	}
	
	@EventHandler
	public void onPlayerChat(AsyncPlayerChatEvent e)
	{
		Player p = e.getPlayer();
		PlayerPunish pp = EasyTempMutemanager.getPlayerPunish(p.getName());
		
		if(MySqlWorker.isUserHas(p.getName()))
		{
			switch(MySqlWorker.getBanType(p.getName()))
			{
				case MUTE:
				{
					e.setCancelled(true);
					p.sendMessage(EasyLang.SYSTEM_GLOBAL_MUTE.getMsg(true));
					
					break;
				}
				default:
					break;
			}
		}
		
		if(pp != null)
		{
			switch(pp.getType())
			{
				case TEMPMUTE:
				{
					if( (pp.getEndMuteTime() - System.currentTimeMillis()) <= 0 )
						EasyTempMutemanager.removePlayer(p.getName());
					else
					{
						e.setCancelled(true);
						
						long time = (pp.getEndMuteTime() - System.currentTimeMillis()) / 60 / 1000;
						
						p.sendMessage(String.format(EasyLang.SYSTEM_AUTO_UNMUTE.getMsg(true), time));
					}
					
					break;
				}
				default:
					break;
			}
		}
		
		pp = null;
		p = null;
	}
	
	@EventHandler
	public void onPlayerCmds(PlayerCommandPreprocessEvent e)
	{
		Player p = e.getPlayer();
		
		String cmd = e.getMessage().split(" ")[0];
		
		if(MySqlWorker.isUserHas(p.getName()))
		{
			switch(MySqlWorker.getBanType(p.getName()))
			{
				case MUTE:
				{
					if(EasyMuteDisallowedCmds.isCmdDisallow(cmd))
					{
						e.setCancelled(true);
						p.sendMessage(EasyLang.SYSTEM_GLOBAL_MUTE.getMsg(true));
					}					
					
					break;
				}
				default:
					break;
			}
		}
		
		PlayerPunish pp = EasyTempMutemanager.getPlayerPunish(p.getName());
		
		if(pp != null)
		{
			if(EasyMuteDisallowedCmds.isCmdDisallow(cmd))
			{
				switch(pp.getType())
				{
					case TEMPMUTE:
					{
						if( (pp.getEndMuteTime() - System.currentTimeMillis()) <= 0 )
							EasyTempMutemanager.removePlayer(p.getName());
						else
						{
							e.setCancelled(true);
							
							long time = (pp.getEndMuteTime() - System.currentTimeMillis()) / 60 / 1000;
							
							p.sendMessage(String.format(EasyLang.SYSTEM_AUTO_UNMUTE.getMsg(true), time));
						}
						
						break;
					}
					default:
						break;
				}
			}
		}
		
		p = null;
		pp = null;
		cmd = null;
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerJoin(PlayerJoinEvent e)
	{
		Player p = e.getPlayer();
		String group = PlayerGroupChecker.getUserG(p.getName());
		
		if(group.equalsIgnoreCase("default"))
			return;
		else
		{
			if(EasyPlayerPunishController.getPlayerObject(p.getName()) == null)
				EasyPlayerPunishController.addPlayer(p.getName(), new PlayerObject(EasyGroupManager.getGroup(group)));
			else
				EasyPlayerPunishController.getPlayerObject(p.getName()).setG(EasyGroupManager.getGroup(group));
		}
		
		p = null;
		group = null;
	}
}
