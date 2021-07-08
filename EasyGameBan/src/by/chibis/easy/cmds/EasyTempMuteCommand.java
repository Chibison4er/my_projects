package by.chibis.easy.cmds;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import by.chibis.easy.EasyBanType;
import by.chibis.easy.checker.EasyTempMutemanager;
import by.chibis.easy.checker.PlayerGroupChecker;
import by.chibis.easy.checker.PlayerPunish;
import by.chibis.easy.groups.EasyGroupManager;
import by.chibis.easy.groups.EasyPlayerPunishController;
import by.chibis.easy.groups.PlayerObject;
import by.chibis.easy.lang.EasyLang;

public class EasyTempMuteCommand implements CommandExecutor
{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lable, String[] args)
	{
		if(sender instanceof Player)
		{
			Player p = (Player) sender;
			
			PlayerObject po = EasyPlayerPunishController.getPlayerObject(p.getName());
			
			if(po != null && po.getG().isWhetherTempMute())
			{
				if(po.getTempMuteLimit() > 0)
				{
					if(args.length < 3)
						p.sendMessage(EasyLang.TEMPMUTE_USAGE.getMsg(true));
					else
					{
						int muteTime = 1;
						
						try
						{
							
							muteTime = Integer.parseInt(args[1]);
							
							if(muteTime > po.getG().getTempMuteInMinutes())
								p.sendMessage(String.format(EasyLang.TEMPMUTE_MAX_TIME.getMsg(true), po.getG().getTempMuteInMinutes()));
							else if(muteTime <= 0)
								p.sendMessage(EasyLang.SYSTEM_LESS_TIME.getMsg(true));
							else
							{
								Player target = Bukkit.getPlayer(args[0]);
								String reason = "";
								
								if(p.getName().equalsIgnoreCase(args[0]))
									p.sendMessage(EasyLang.TEMPMUTE_YOURSELF.getMsg(true));
								else
								{
									for(int i = 2; i < args.length; i ++)
										reason += args[i] + "";
									
									if(po.getG().isWhetherPunishJunior())
									{
										if(EasyGroupManager.getGroupRank(PlayerGroupChecker.getUserG(args[0])) > EasyGroupManager.getGroupRank(po.getG().getGroup()))
											checkTarget(p, target, args, muteTime, reason);									
										else
											p.sendMessage(EasyLang.TEMPMUTE_DONATE_HIGTH.getMsg(true));
									}else
										checkTarget(p, target, args, muteTime, reason);	
								}
									
							}	
							
						}catch(Throwable ex){ p.sendMessage(EasyLang.TEMPMUTE_USAGE.getMsg(true)); }
					}
					
				}else
					p.sendMessage(EasyLang.TEMPMUTE_LIMIT_OUT.getMsg(true));
				
			}else
				p.sendMessage(EasyLang.TEMPMUTE_NOPERM.getMsg(true));
		}else
		{
			if(args.length < 3)
				sender.sendMessage("Usage: /tempmute <player> <time> <reason>");
			else
			{
				int muteTime = 1;
				
				try
				{
					
					muteTime = Integer.parseInt(args[1]);
					
					muteTime = muteTime <= 0 ? 1 : muteTime;
					
					Player target = Bukkit.getPlayer(args[0]);
					String reason = "";
					
					if(target != null)
					{
						for(int i = 2; i < args.length; i ++)
							reason += args[i] + "";
						
						target.sendMessage(String.format(EasyLang.TEMPMUTE_PLAYER.getMsg(true), muteTime, reason));
						sender.sendMessage("Player " + args[0] + " is muted on " + args[1] + " minuts!");
						tempMute(sender.getName(), target.getName(), muteTime);
					}
					
				}catch(Throwable ex){ sender.sendMessage("UsageExeption: /tempmute <player> <time> <reason>"); }
			}
		}
		
		return true;
	}
	
	void checkTarget(Player p, Player target, String[] args, int muteTime, String reason)
	{
		if(target != null)
		{
			target.sendMessage(String.format(EasyLang.TEMPMUTE_PLAYER.getMsg(true), muteTime, reason));
			Bukkit.broadcastMessage(EasyBanType.TEMPBAN.getTag() + String.format(EasyLang.TEMPMUTE_BROADCAST.getMsg(false), target.getName(), p.getName(), muteTime, reason));
			tempMute(p.getName(), target.getName(), muteTime);			
		}else
			p.sendMessage(EasyLang.SYSTEM_PLAYER_OFFLINE.getMsg(true));
	}
	
	void tempMute(String whoSetMute, String target, int time)
	{
		EasyTempMutemanager.addPlayer(target, new PlayerPunish(whoSetMute, time));
		if(!whoSetMute.equals("CONSOLE"))
			EasyPlayerPunishController.getPlayerObject(whoSetMute).setTempBanLimit(-1);
	}
}
