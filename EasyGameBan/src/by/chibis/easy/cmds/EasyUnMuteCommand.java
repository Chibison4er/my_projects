package by.chibis.easy.cmds;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import by.chibis.easy.EasyBanType;
import by.chibis.easy.checker.EasyTempMutemanager;
import by.chibis.easy.groups.EasyPlayerPunishController;
import by.chibis.easy.groups.PlayerObject;
import by.chibis.easy.lang.EasyLang;
import by.chibis.easy.mysql.MySqlWorker;

public class EasyUnMuteCommand implements CommandExecutor
{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lable, String[] args) 
	{
		if(sender instanceof Player)
		{
			Player p = (Player) sender;
			
			PlayerObject po = EasyPlayerPunishController.getPlayerObject(p.getName());
			
			if(po != null && po.getG().isWhetherUnMute())
			{
				
				if(po.getUnMuteLimit() > 0)
				{
					
					if(args.length < 1)
						p.sendMessage(EasyLang.UNMUTE_USAGE.getMsg(true));
					else
					{
						if(MySqlWorker.getBanType(args[0]) == EasyBanType.MUTE)
						{
							unMute(p.getName(), args[0]);
							Bukkit.broadcastMessage(EasyBanType.UNMUTE.getTag() + String.format(EasyLang.UNMUTE_PLAYER.getMsg(false), args[0], p.getName()));
							//p.sendMessage("Игрок " + args[0] + " размучен!");
						}else if(EasyTempMutemanager.getPlayerPunish(args[0]) != null)
						{
							EasyTempMutemanager.removePlayer(args[0]);
							Bukkit.broadcastMessage(EasyBanType.UNMUTE.getTag() + String.format(EasyLang.UNMUTE_PLAYER.getMsg(false), args[0], p.getName()));
						//	p.sendMessage("Игрок " + args[0] + " размучен!");
						}else
							p.sendMessage(EasyLang.UNMUTE_NOMUTE.getMsg(true));
					}

				}else
					p.sendMessage(EasyLang.UNMUTE_LIMIT_OUT.getMsg(true));
				
			}else
				p.sendMessage(EasyLang.UNMUTE_NOPERM.getMsg(true));
		}else
		{
			if(args.length < 1)
				sender.sendMessage("usage: /unmute <player>");
			else
			{
				if(MySqlWorker.getBanType(args[0]) == EasyBanType.MUTE)
				{
					unMute(sender.getName(), args[0]);
					sender.sendMessage("Player " + args[0] + " unmuted!");
				}else if(EasyTempMutemanager.getPlayerPunish(args[0]) != null)
				{
					EasyTempMutemanager.removePlayer(args[0]);
					Bukkit.broadcastMessage(EasyBanType.UNMUTE.getTag() + String.format(EasyLang.UNMUTE_PLAYER.getMsg(false), args[0], sender.getName()));
				//	p.sendMessage("Игрок " + args[0] + " размучен!");
				}else
					sender.sendMessage("Player not muted!");
			}
		}
		
		return true;
	}
	
	void unMute(String whoSetUnMute, String target)
	{
		MySqlWorker.addPlayerMute(target, whoSetUnMute, "UNMUTE", EasyBanType.NONE);
		if(!whoSetUnMute.equalsIgnoreCase("CONSOLE"))
			EasyPlayerPunishController.getPlayerObject(whoSetUnMute).setUnMuteLimit(-1);
	}
}
