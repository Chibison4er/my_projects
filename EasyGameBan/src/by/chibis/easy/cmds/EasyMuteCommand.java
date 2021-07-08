package by.chibis.easy.cmds;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import by.chibis.easy.EasyBanType;
import by.chibis.easy.checker.PlayerGroupChecker;
import by.chibis.easy.groups.EasyGroupManager;
import by.chibis.easy.groups.EasyPlayerPunishController;
import by.chibis.easy.groups.PlayerObject;
import by.chibis.easy.lang.EasyLang;
import by.chibis.easy.mysql.MySqlWorker;

public class EasyMuteCommand implements CommandExecutor 
{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lable, String[] args) 
	{
		if(sender instanceof Player)
		{
			Player p = (Player) sender;
			
			PlayerObject po = EasyPlayerPunishController.getPlayerObject(p.getName());
			
			if(po != null && po.getG().isWhetherMute())
			{
				
				if(po.getMuteLimit() > 0)
				{
					
					if(args.length < 2)
						p.sendMessage(EasyLang.MUTE_USAGE.getMsg(true));
					else
					{
						if(p.getName().equalsIgnoreCase(args[0]))
							p.sendMessage(EasyLang.MUTE_YOURSELF.getMsg(true));
						else
						{
							Player target = Bukkit.getPlayer(args[0]);
							String reason = "";
							
							if(target != null)
							{
								if(MySqlWorker.getBanType(args[0]) == EasyBanType.MUTE)
									p.sendMessage(EasyLang.MUTE_ALREADY.getMsg(true));
								else
								{
									for(int i = 1; i < args.length; i++)
										reason += args[i] + " ";
									
									if(po.getG().isWhetherPunishJunior())
									{
										if(EasyGroupManager.getGroupRank(PlayerGroupChecker.getUserG(args[0])) > EasyGroupManager.getGroupRank(po.getG().getGroup()))
										{
											mutePlayer(p.getName(), args[0], reason, EasyBanType.MUTE);
											target.sendMessage(EasyLang.MUTE_PLAYER.getMsg(true));
									//		p.sendMessage("¬ы замутили игрока " + args[0] + " навсегда, с причиной " + reason);
											Bukkit.broadcastMessage(EasyBanType.MUTE.getTag() + String.format(EasyLang.MUTE_BROADCAST.getMsg(false), target.getName(), p.getName(), reason));
										}else
											p.sendMessage(EasyLang.MUTE_DONATE_HIGTH.getMsg(true));
									}else
									{
										mutePlayer(p.getName(), args[0], reason, EasyBanType.MUTE);
										target.sendMessage(EasyLang.MUTE_PLAYER.getMsg(true));
										Bukkit.broadcastMessage(EasyBanType.MUTE.getTag() + String.format(EasyLang.MUTE_BROADCAST.getMsg(false), target.getName(), p.getName(), reason));
									//	p.sendMessage("¬ы замутили игрока " + args[0] + " навсегда, с причиной " + reason);
									}
								}
									
							}else
								p.sendMessage(EasyLang.SYSTEM_PLAYER_OFFLINE.getMsg(true));
						}
					}
					
				}else
					p.sendMessage(EasyLang.MUTE_LIMIT_OUT.getMsg(true));
				
			}else
				p.sendMessage(EasyLang.MUTE_NOPERM.getMsg(true));
		}else
		{
			if(args.length < 2)
				sender.sendMessage("Usage: /mute <player> <reason>");
			else
			{
				Player target = Bukkit.getPlayer(args[0]);
				String reason = "";
				
				if(target != null)
				{
					for(int i = 1; i < args.length; i++)
						reason += args[i] + " ";
					
					mutePlayer(sender.getName(), args[0], reason, EasyBanType.MUTE);
					target.sendMessage(EasyLang.MUTE_PLAYER.getMsg(true));
					sender.sendMessage("Player " + args[0] + " has been muted by always, with reason: " + reason);
					Bukkit.broadcastMessage(EasyBanType.MUTE.getTag() + String.format(EasyLang.MUTE_BROADCAST.getMsg(false), target.getName(), sender.getName(), reason));
						
				}else
					sender.sendMessage("Player is offline!");
			}
		}
		
		return true;
	}
	
	void mutePlayer(String whoSetMute, String target, String reason, EasyBanType type)
	{
		MySqlWorker.addPlayerMute(target, whoSetMute, reason, type);
		
		if(!whoSetMute.equalsIgnoreCase("CONSOLE"))
			EasyPlayerPunishController.getPlayerObject(whoSetMute).setMuteLimit(-1);
	}
}
